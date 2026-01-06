package com.ecommerce.auth.controller;

import com.ecommerce.admin.dto.ActualizarPerfilRequest;
import com.ecommerce.admin.dto.CambiarContrasenaRequest;
import com.ecommerce.admin.dto.PerfilResponse;
import com.ecommerce.auth.service.AuthUsuarioService;
import com.ecommerce.util.FileStorage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/miPerfil")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ProfileController {

	private final AuthUsuarioService users;
	private final FileStorage storage;

	@GetMapping
	public PerfilResponse miPerfil(Authentication auth) {
		return users.perfilDeUsuario(auth.getName());
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public PerfilResponse actualizarPerfil(Authentication auth, @Valid @RequestBody ActualizarPerfilRequest req) {
		return users.actualizarPerfilDeUsuario(auth.getName(), req, null);
	}

	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public PerfilResponse actualizarPerfil(Authentication auth, @RequestPart String nombre,
			@RequestPart String apellido, @RequestPart String correo, @RequestPart String username,
			@RequestPart(name = "imagen", required = false) MultipartFile imagen) {

		var req = new ActualizarPerfilRequest(nombre, apellido, correo, username);

		String ruta = null;
		try {
			if (imagen != null && !imagen.isEmpty()) {
				ruta = storage.saveUserPhoto(imagen);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Foto inválida: " + e.getMessage());
		}

		return users.actualizarPerfilDeUsuario(auth.getName(), req, ruta);
	}

	@PatchMapping("/password")
	public ResponseEntity<Void> cambiarContrasena(Authentication auth, @Valid @RequestBody CambiarContrasenaRequest r) {
		users.cambiarContrasenaDeUsuario(auth.getName(), r.currentPassword(), r.newPassword());
		return ResponseEntity.noContent().build();
	}
}
