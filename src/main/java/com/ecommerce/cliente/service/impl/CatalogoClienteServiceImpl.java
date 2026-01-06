package com.ecommerce.cliente.service.impl;

import com.ecommerce.admin.repository.AdminProductoRepository;
import com.ecommerce.cliente.dto.ProductoClienteResponse;
import com.ecommerce.cliente.service.CatalogoClienteService;
import com.ecommerce.entity.Producto;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CatalogoClienteServiceImpl implements CatalogoClienteService {

	private final AdminProductoRepository productoRepo;

	private static ProductoClienteResponse map(Producto p) {
		return new ProductoClienteResponse(p.getIdProducto(), p.getCodigo(), p.getNombre(), p.getPrecio(),
				p.getCategoria().getNombre(), p.getMarca().getNombre(), p.getImagenUrl());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProductoClienteResponse> listarProductos(String q, Pageable pageable) {
		var page = (q == null || q.isBlank()) ? productoRepo.findAll(pageable)
				: productoRepo.findByNombreContainingIgnoreCase(q.trim(), pageable);
		return page.map(CatalogoClienteServiceImpl::map);
	}
}
