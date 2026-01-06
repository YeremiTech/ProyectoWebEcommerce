package com.ecommerce.cliente.controller;

import com.ecommerce.cliente.dto.CarritoActualizarCantidadRequest;
import com.ecommerce.cliente.dto.CarritoItemRequest;
import com.ecommerce.cliente.dto.CarritoItemResponse;
import com.ecommerce.cliente.service.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente/carrito")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_CLIENTE')")
public class CarritoController {

	private final CarritoService service;

	@GetMapping
	public List<CarritoItemResponse> verCarrito(Authentication auth) {
		return service.verCarrito(auth.getName());
	}

	@PostMapping("/items")
	public List<CarritoItemResponse> agregar(Authentication auth, @RequestBody @Valid CarritoItemRequest req) {
		return service.agregarItem(auth.getName(), req);
	}

	@DeleteMapping("/items/{idItem}")
	public List<CarritoItemResponse> quitar(Authentication auth, @PathVariable Integer idItem) {
		return service.quitarItem(auth.getName(), idItem);
	}

	@PatchMapping("/items/{idItem}")
	public List<CarritoItemResponse> actualizar(Authentication auth, @PathVariable Integer idItem,
			@RequestBody @Valid CarritoActualizarCantidadRequest body) {
		return service.actualizarCantidad(auth.getName(), idItem, body.cantidad());
	}

	@DeleteMapping
	public ResponseEntity<Void> vaciar(Authentication auth) {
		service.vaciar(auth.getName());
		return ResponseEntity.noContent().build();
	}
}
