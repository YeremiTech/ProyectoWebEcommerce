package com.ecommerce.cliente.controller;

import com.ecommerce.cliente.dto.CheckoutResponse;
import com.ecommerce.cliente.service.CheckoutService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cliente/checkout")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_CLIENTE')")
public class CheckoutController {

	private final CheckoutService service;

	@PostMapping
	public ResponseEntity<?> confirmar(Authentication auth) {
		try {
			CheckoutResponse r = service.confirmar(auth.getName());
			return ResponseEntity.ok(r);
		} catch (IllegalStateException ex) {
			if ("NO_METODO_PAGO".equals(ex.getMessage())) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Debe registrar un método de pago");
			}
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
}