package com.ecommerce.admin.controller;

import com.ecommerce.admin.dto.UsuarioAdminRequest;
import com.ecommerce.admin.dto.UsuarioAdminResponse;
import com.ecommerce.admin.service.AdminUsuarioService;
import com.ecommerce.util.FileStorage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
public class AdminUsuarioController {

	private final AdminUsuarioService service;
	private final FileStorage fileStorage;

	@GetMapping
	public Page<UsuarioAdminResponse> listar(@RequestParam(required = false) String q, Pageable pageable) {
		return service.listar(q, pageable);
	}

	@GetMapping("/{id}")
	public UsuarioAdminResponse obtener(@PathVariable Integer id) {
		return service.obtener(id);
	}

	@GetMapping("/siguiente-codigo")
	public ResponseEntity<String> siguienteCodigo() {
		return ResponseEntity.ok(service.siguienteCodigo());
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UsuarioAdminResponse> crear(@RequestBody @Valid UsuarioAdminRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(req));
	}

	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public UsuarioAdminResponse actualizar(@PathVariable Integer id, @RequestBody @Valid UsuarioAdminRequest req) {
		return service.actualizar(id, req);
	}

	@PostMapping(path = "/with-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<UsuarioAdminResponse> crearConFoto(@RequestParam String nombre, @RequestParam String apellido,
			@RequestParam String correo, @RequestParam String username, @RequestParam(required = false) String password,
			@RequestParam Integer idRol, @RequestParam(required = false, name = "imagen") MultipartFile imagen) {
		String ruta = null;
		if (imagen != null && !imagen.isEmpty()) {
			try {
				ruta = fileStorage.saveUserPhoto(imagen);
			} catch (Exception e) {
				throw new IllegalArgumentException("Foto inválida");
			}
		}

		var req = new UsuarioAdminRequest(nombre, apellido, correo, username, password, idRol, ruta);

		var creado = service.crear(req);
		return ResponseEntity.status(HttpStatus.CREATED).body(creado);
	}

	@PutMapping(path = "/{id}/with-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public UsuarioAdminResponse actualizarConFoto(@PathVariable Integer id, @RequestParam String nombre,
			@RequestParam String apellido, @RequestParam String correo, @RequestParam String username,
			@RequestParam(required = false) String password, @RequestParam Integer idRol,
			@RequestParam(required = false, name = "imagen") MultipartFile imagen) {
		String ruta = null;
		if (imagen != null && !imagen.isEmpty()) {
			try {
				ruta = fileStorage.saveUserPhoto(imagen);
			} catch (Exception e) {
				throw new IllegalArgumentException("Foto inválida");
			}
		}

		var req = new UsuarioAdminRequest(nombre, apellido, correo, username, password, idRol, ruta);

		return service.actualizar(id, req);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
		service.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
