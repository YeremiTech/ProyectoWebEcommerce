package com.ecommerce.admin.controller;

import com.ecommerce.admin.dto.ProductoRequest;
import com.ecommerce.admin.dto.ProductoResponse;
import com.ecommerce.admin.service.AdminProductoService;
import com.ecommerce.util.FileStorage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/admin/productos")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
public class AdminProductoController {

	private final AdminProductoService service;
	private final FileStorage fileStorage;

	@GetMapping
	public Page<ProductoResponse> listar(@RequestParam(required = false) String q, Pageable pageable) {
		return service.listar(q, pageable);
	}

	@GetMapping("/{id}")
	public ProductoResponse obtener(@PathVariable Integer id) {
		return service.obtener(id);
	}

	@GetMapping("/siguiente-codigo")
	public ResponseEntity<String> siguienteCodigo() {
		return ResponseEntity.ok(service.siguienteCodigo());
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductoResponse> crear(@RequestBody @Valid ProductoRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(req));
	}

	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ProductoResponse actualizar(@PathVariable Integer id, @RequestBody @Valid ProductoRequest req) {
		return service.actualizar(id, req);
	}

	@PostMapping(path = "/with-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ProductoResponse> crearConFoto(@RequestParam(required = false) String codigo,
			@RequestParam String nombre, @RequestParam BigDecimal precio, @RequestParam Integer stock,
			@RequestParam Integer idCategoria, @RequestParam Integer idMarca,
			@RequestParam(required = false, name = "imagen") MultipartFile imagen) {
		String ruta = null;
		if (imagen != null && !imagen.isEmpty()) {
			try {
				ruta = fileStorage.saveProductoPhoto(imagen);
			} catch (Exception e) {
				throw new IllegalArgumentException("Foto inválida");
			}
		}

		ProductoRequest req = new ProductoRequest(codigo, nombre, precio, stock, idCategoria, idMarca, ruta);

		ProductoResponse res = service.crear(req);
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}

	@PutMapping(path = "/{id}/with-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ProductoResponse actualizarConFoto(@PathVariable Integer id, @RequestParam(required = false) String codigo,
			@RequestParam String nombre, @RequestParam BigDecimal precio, @RequestParam Integer stock,
			@RequestParam Integer idCategoria, @RequestParam Integer idMarca,
			@RequestParam(required = false, name = "imagen") MultipartFile imagen) {
		String ruta = null;
		if (imagen != null && !imagen.isEmpty()) {
			try {
				ruta = fileStorage.saveProductoPhoto(imagen);
			} catch (Exception e) {
				throw new IllegalArgumentException("Foto inválida");
			}
		}

		ProductoRequest req = new ProductoRequest(codigo, nombre, precio, stock, idCategoria, idMarca, ruta);

		return service.actualizar(id, req);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
		service.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
