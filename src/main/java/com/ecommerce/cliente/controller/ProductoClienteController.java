package com.ecommerce.cliente.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.cliente.dto.ProductoClienteResponse;
import com.ecommerce.cliente.service.CatalogoClienteService;

@RestController
@RequestMapping("/api/cliente/productos")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_CLIENTE')")
public class ProductoClienteController {

	private final CatalogoClienteService service;

	@GetMapping
	public Page<ProductoClienteResponse> listar(@RequestParam(required = false) String q,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size,
			@RequestParam(defaultValue = "nombre,asc") String sort) {

		var parts = sort.split(",");
		var pageable = PageRequest.of(page, size,
				Sort.by(Sort.Direction.fromString(parts.length > 1 ? parts[1] : "asc"), parts[0]));
		return service.listarProductos(q, pageable);
	}
}
