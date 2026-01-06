package com.ecommerce.cliente.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ecommerce.cliente.dto.ProductoClienteResponse;

public interface CatalogoClienteService {
	Page<ProductoClienteResponse> listarProductos(String q, Pageable pageable);
}
