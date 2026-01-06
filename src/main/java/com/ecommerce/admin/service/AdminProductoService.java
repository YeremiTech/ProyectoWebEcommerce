package com.ecommerce.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ecommerce.admin.dto.ProductoRequest;
import com.ecommerce.admin.dto.ProductoResponse;

public interface AdminProductoService {
	Page<ProductoResponse> listar(String q, Pageable pageable);

	ProductoResponse crear(ProductoRequest req);

	ProductoResponse obtener(Integer id);

	ProductoResponse actualizar(Integer id, ProductoRequest req);

	void eliminar(Integer id);

	String siguienteCodigo();
}
