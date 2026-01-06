package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dto.ProductoRequest;
import com.ecommerce.admin.dto.ProductoResponse;
import com.ecommerce.admin.repository.AdminCategoriaRepository;
import com.ecommerce.admin.repository.AdminMarcaRepository;
import com.ecommerce.admin.repository.AdminProductoRepository;
import com.ecommerce.admin.service.AdminProductoService;
import com.ecommerce.entity.Categoria;
import com.ecommerce.entity.Marca;
import com.ecommerce.entity.Producto;
import com.ecommerce.util.GeneradorCodigo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminProductoServiceImpl implements AdminProductoService {

	private final GeneradorCodigo generador;
	private final AdminProductoRepository repo;
	private final AdminCategoriaRepository categoriaRepo;
	private final AdminMarcaRepository marcaRepo;

	private ProductoResponse map(Producto p) {
		return new ProductoResponse(p.getIdProducto(), p.getCodigo(), p.getNombre(), p.getPrecio(), p.getStock(),
				p.getCategoria().getIdCategoria(), p.getCategoria().getNombre(), p.getMarca().getIdMarca(),
				p.getMarca().getNombre(), p.getImagenUrl());
	}

	private String generarCodigo() {
		return generador.siguiente("PRD", repo.findAllCodigos().stream());
	}

	@Override
	public String siguienteCodigo() {
		return generarCodigo();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProductoResponse> listar(String q, Pageable pageable) {
		Page<Producto> page = (q == null || q.isBlank()) ? repo.findAll(pageable)
				: repo.findByNombreContainingIgnoreCase(q.trim(), pageable);
		return page.map(this::map);
	}

	@Override
	public ProductoResponse crear(ProductoRequest req) {
		String codigo = (req.codigo() == null || req.codigo().isBlank()) ? generarCodigo()
				: req.codigo().trim().toUpperCase();

		if (repo.existsByCodigo(codigo)) {
			throw new IllegalArgumentException("Código duplicado");
		}

		Categoria cat = categoriaRepo.findById(req.idCategoria())
				.orElseThrow(() -> new IllegalArgumentException("Categoría no existe"));
		Marca mar = marcaRepo.findById(req.idMarca())
				.orElseThrow(() -> new IllegalArgumentException("Marca no existe"));

		Producto p = new Producto();
		p.setCodigo(codigo);
		p.setNombre(req.nombre().trim());
		p.setPrecio(req.precio());
		p.setStock(req.stock());
		p.setCategoria(cat);
		p.setMarca(mar);
		p.setImagenUrl(req.imagenUrl());

		return map(repo.save(p));
	}

	@Override
	@Transactional(readOnly = true)
	public ProductoResponse obtener(Integer id) {
		Producto p = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no existe"));
		return map(p);
	}

	@Override
	public ProductoResponse actualizar(Integer id, ProductoRequest req) {
		Producto p = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no existe"));

		String nuevoCodigo = (req.codigo() == null || req.codigo().isBlank()) ? p.getCodigo()
				: req.codigo().trim().toUpperCase();

		if (!p.getCodigo().equalsIgnoreCase(nuevoCodigo) && repo.existsByCodigo(nuevoCodigo)) {
			throw new IllegalArgumentException("Código duplicado");
		}

		Categoria cat = categoriaRepo.findById(req.idCategoria())
				.orElseThrow(() -> new IllegalArgumentException("Categoría no existe"));
		Marca mar = marcaRepo.findById(req.idMarca())
				.orElseThrow(() -> new IllegalArgumentException("Marca no existe"));

		p.setCodigo(nuevoCodigo);
		p.setNombre(req.nombre().trim());
		p.setPrecio(req.precio());
		p.setStock(req.stock());
		p.setCategoria(cat);
		p.setMarca(mar);
		if (req.imagenUrl() != null) {
			p.setImagenUrl(req.imagenUrl());
		}

		return map(repo.save(p));
	}

	@Override
	public void eliminar(Integer id) {
		if (!repo.existsById(id)) {
			throw new IllegalArgumentException("Producto no existe");
		}
		repo.deleteById(id);
	}
}
