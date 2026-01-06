package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dto.MarcaRequest;
import com.ecommerce.admin.dto.MarcaResponse;
import com.ecommerce.admin.dto.OpcionResponse;
import com.ecommerce.admin.repository.AdminMarcaRepository;
import com.ecommerce.admin.service.AdminMarcaService;
import com.ecommerce.entity.Marca;
import com.ecommerce.util.GeneradorCodigo;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminMarcaServiceImpl implements AdminMarcaService {

	private final GeneradorCodigo generador;
	private final AdminMarcaRepository repo;

	private static MarcaResponse map(Marca m) {
		return new MarcaResponse(m.getIdMarca(), m.getCodigo(), m.getNombre());
	}

	private String generarCodigo() {
		return generador.siguiente("MAR", repo.findAllCodigos().stream());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MarcaResponse> listar(String q, Pageable pageable) {
		var page = (q == null || q.isBlank()) ? repo.findAll(pageable)
				: repo.findByNombreContainingIgnoreCase(q.trim(), pageable);
		return page.map(AdminMarcaServiceImpl::map);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OpcionResponse> opciones() {
		return repo.findAllByOrderByNombreAsc().stream().map(m -> new OpcionResponse(m.getIdMarca(), m.getNombre()))
				.toList();
	}

	@Override
	public MarcaResponse crear(MarcaRequest req) {
		String codigo = (req.codigo() == null || req.codigo().isBlank()) ? generarCodigo()
				: req.codigo().trim().toUpperCase();

		if (repo.existsByCodigo(codigo))
			throw new IllegalArgumentException("Código duplicado");
		if (req.nombre() != null && repo.existsByNombreIgnoreCase(req.nombre().trim()))
			throw new IllegalArgumentException("Nombre duplicado");

		var m = new Marca();
		m.setCodigo(codigo);
		m.setNombre(req.nombre().trim());
		return map(repo.save(m));
	}

	@Override
	@Transactional(readOnly = true)
	public MarcaResponse obtener(Integer id) {
		return repo.findById(id).map(AdminMarcaServiceImpl::map)
				.orElseThrow(() -> new IllegalArgumentException("Marca no existe"));
	}

	@Override
	public MarcaResponse actualizar(Integer id, MarcaRequest req) {
		var m = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Marca no existe"));
		String nuevoCodigo = (req.codigo() == null || req.codigo().isBlank()) ? m.getCodigo()
				: req.codigo().trim().toUpperCase();

		if (!m.getCodigo().equalsIgnoreCase(nuevoCodigo) && repo.existsByCodigo(nuevoCodigo))
			throw new IllegalArgumentException("Código duplicado");
		if (!m.getNombre().equalsIgnoreCase(req.nombre().trim()) && repo.existsByNombreIgnoreCase(req.nombre().trim()))
			throw new IllegalArgumentException("Nombre duplicado");

		m.setCodigo(nuevoCodigo);
		m.setNombre(req.nombre().trim());
		return map(repo.save(m));
	}

	@Override
	public void eliminar(Integer id) {
		if (!repo.existsById(id))
			throw new IllegalArgumentException("Marca no existe");
		repo.deleteById(id);
	}

	@Override
	public String siguienteCodigo() {
		return generarCodigo();
	}
}
