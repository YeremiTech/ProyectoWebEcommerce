package com.ecommerce.admin.controller;

import com.ecommerce.admin.dto.MarcaRequest;
import com.ecommerce.admin.dto.MarcaResponse;
import com.ecommerce.admin.service.AdminMarcaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/marcas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminMarcaController {

	private final AdminMarcaService service;

	@GetMapping
	public Page<MarcaResponse> listar(@RequestParam(required = false) String q, Pageable pageable) {
		return service.listar(q, pageable);
	}

	@GetMapping("/{id}")
	public MarcaResponse obtener(@PathVariable Integer id) {
		return service.obtener(id);
	}

	@GetMapping("/siguiente-codigo")
	public ResponseEntity<String> siguienteCodigo() {
		return ResponseEntity.ok(service.siguienteCodigo());
	}

	@PostMapping
	public ResponseEntity<MarcaResponse> crear(@RequestBody @Valid MarcaRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(req));
	}

	@PutMapping("/{id}")
	public MarcaResponse actualizar(@PathVariable Integer id, @RequestBody @Valid MarcaRequest req) {
		return service.actualizar(id, req);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
		service.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
