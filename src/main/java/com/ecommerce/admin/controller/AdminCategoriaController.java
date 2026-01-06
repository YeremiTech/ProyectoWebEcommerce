package com.ecommerce.admin.controller;

import com.ecommerce.admin.dto.CategoriaRequest;
import com.ecommerce.admin.dto.CategoriaResponse;
import com.ecommerce.admin.service.AdminCategoriaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categorias")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminCategoriaController {

	private final AdminCategoriaService service;

	@GetMapping
	public Page<CategoriaResponse> listar(@RequestParam(required = false) String q, Pageable pageable) {
		return service.listar(q, pageable);
	}

	@GetMapping("/{id}")
	public CategoriaResponse obtener(@PathVariable Integer id) {
		return service.obtener(id);
	}

	@GetMapping("/siguiente-codigo")
	public ResponseEntity<String> siguienteCodigo() {
		return ResponseEntity.ok(service.siguienteCodigo());
	}

	@PostMapping
	public ResponseEntity<CategoriaResponse> crear(@RequestBody @Valid CategoriaRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(req));
	}

	@PutMapping("/{id}")
	public CategoriaResponse actualizar(@PathVariable Integer id, @RequestBody @Valid CategoriaRequest req) {
		return service.actualizar(id, req);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
		service.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
