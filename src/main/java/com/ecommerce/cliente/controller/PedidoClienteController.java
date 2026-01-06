package com.ecommerce.cliente.controller;

import com.ecommerce.cliente.dto.PedidoDetalleResponse;
import com.ecommerce.cliente.dto.PedidoResumenResponse;
import com.ecommerce.cliente.service.PedidoClienteService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cliente/pedidos")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_CLIENTE')")
public class PedidoClienteController {

	private final PedidoClienteService service;

	@GetMapping
	public Page<PedidoResumenResponse> listar(Authentication auth, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fecha"));
		return service.listar(auth.getName(), pageable);
	}

	@GetMapping("/{idPedido}")
	public PedidoDetalleResponse detalle(Authentication auth, @PathVariable Integer idPedido) {
		return service.detalle(auth.getName(), idPedido);
	}
}
