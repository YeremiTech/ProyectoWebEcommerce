package com.ecommerce.cliente.controller;

import com.ecommerce.cliente.dto.MetodoPagoRequest;
import com.ecommerce.cliente.dto.MetodoPagoResponse;
import com.ecommerce.cliente.service.PagoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cliente/pago")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_CLIENTE')")
public class PagoController {

	private final PagoService service;

	@GetMapping
	public MetodoPagoResponse obtener(Authentication auth) {
		return service.obtener(auth.getName());
	}

	@PostMapping
	public MetodoPagoResponse crear(Authentication auth, @RequestBody @Valid MetodoPagoRequest req) {
		return service.guardarActualizar(auth.getName(), req);
	}

	@PutMapping
	public MetodoPagoResponse actualizar(Authentication auth, @RequestBody @Valid MetodoPagoRequest req) {
		return service.guardarActualizar(auth.getName(), req);
	}
}
