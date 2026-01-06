package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dto.CategoriaRequest;
import com.ecommerce.admin.dto.CategoriaResponse;
import com.ecommerce.admin.dto.OpcionResponse;
import com.ecommerce.admin.repository.AdminCategoriaRepository;
import com.ecommerce.admin.service.AdminCategoriaService;
import com.ecommerce.entity.Categoria;
import com.ecommerce.util.GeneradorCodigo;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCategoriaServiceImpl implements AdminCategoriaService {

	private final AdminCategoriaRepository repo;
	private final GeneradorCodigo generador;

	private static CategoriaResponse map(Categoria c) {
		return new CategoriaResponse(c.getIdCategoria(), c.getCodigo(), c.getNombre());
	}

	private String generarCodigo() {
		return generador.siguiente("CAT", repo.findAllCodigos().stream());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CategoriaResponse> listar(String q, Pageable pageable) {
		var page = (q == null || q.isBlank()) ? repo.findAll(pageable)
				: repo.findByNombreContainingIgnoreCase(q.trim(), pageable);
		return page.map(AdminCategoriaServiceImpl::map);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OpcionResponse> opciones() {
		return repo.findAllByOrderByNombreAsc().stream().map(c -> new OpcionResponse(c.getIdCategoria(), c.getNombre()))
				.toList();
	}

	@Override
	public CategoriaResponse crear(CategoriaRequest req) {
		String codigo = (req.codigo() == null || req.codigo().isBlank()) ? generarCodigo()
				: req.codigo().trim().toUpperCase();

		if (repo.existsByCodigo(codigo))
			throw new IllegalArgumentException("Código duplicado");
		if (req.nombre() != null && repo.existsByNombreIgnoreCase(req.nombre().trim()))
			throw new IllegalArgumentException("Nombre duplicado");

		var c = new Categoria();
		c.setCodigo(codigo);
		c.setNombre(req.nombre().trim());
		return map(repo.save(c));
	}

	@Override
	@Transactional(readOnly = true)
	public CategoriaResponse obtener(Integer id) {
		var c = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoría no existe"));
		return map(c);
	}

	@Override
	public CategoriaResponse actualizar(Integer id, CategoriaRequest req) {
		var c = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoría no existe"));

		String nuevoCodigo = (req.codigo() == null || req.codigo().isBlank()) ? c.getCodigo()
				: req.codigo().trim().toUpperCase();

		if (!c.getCodigo().equalsIgnoreCase(nuevoCodigo) && repo.existsByCodigo(nuevoCodigo))
			throw new IllegalArgumentException("Código duplicado");
		if (!c.getNombre().equalsIgnoreCase(req.nombre().trim()) && repo.existsByNombreIgnoreCase(req.nombre().trim()))
			throw new IllegalArgumentException("Nombre duplicado");

		c.setCodigo(nuevoCodigo);
		c.setNombre(req.nombre().trim());
		return map(repo.save(c));
	}

	@Override
	public void eliminar(Integer id) {
		if (!repo.existsById(id))
			throw new IllegalArgumentException("Categoría no existe");
		repo.deleteById(id);
	}

	@Override
	public String siguienteCodigo() {
		return generarCodigo();
	}
}
