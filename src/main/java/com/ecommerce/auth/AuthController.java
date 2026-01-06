package com.ecommerce.auth;

import com.ecommerce.admin.repository.AdminRolRepository;
import com.ecommerce.auth.dto.AuthResponse;
import com.ecommerce.auth.dto.LoginRequest;
import com.ecommerce.auth.dto.RegisterRequest;
import com.ecommerce.auth.service.AuthUsuarioService;
import com.ecommerce.entity.Usuario;
import com.ecommerce.security.JwtService;
import com.ecommerce.util.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authManager;
	private final AuthUsuarioService usuarioService;
	private final AdminRolRepository rolRepository;
	private final JwtService jwt;
	private final FileStorage fileStorage;

	// =========================
	// LOGIN
	// =========================
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginRequest r) {
		try {
			var uname = r.getUsername().trim().toLowerCase();
			authManager.authenticate(new UsernamePasswordAuthenticationToken(uname, r.getPassword()));

			var user = usuarioService.porUsername(uname)
					.orElseThrow(() -> new UsernameNotFoundException("No existe usuario"));

			String authority = user.getRol().getNombreRol();

			String token = jwt.generate(user.getUsername(), authority);

			return ResponseEntity.ok(new AuthResponse(token, user.getIdUsuario(), user.getCodigoUsuario(),
					user.getUsername(), user.getNombre(), user.getApellido(), authority, user.getImagenUrl()));
		} catch (AuthenticationException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
		}
	}

	// =========================
	// REGISTER MULTIPART
	// =========================
	@PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> registerMultipart(@RequestPart String nombre, @RequestPart String apellido,
			@RequestPart String correo, @RequestPart String username, @RequestPart String password,
			@RequestPart(required = false, name = "imagen") MultipartFile imagen) {
		return doRegister(nombre, apellido, correo, username, password, imagen);
	}

	// =========================
	// REGISTER JSON
	// =========================
	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerJson(@RequestBody @Valid RegisterRequest r) {
		return doRegister(r.getNombre(), r.getApellido(), r.getCorreo(), r.getUsername(), r.getPassword(), null);
	}

	// =========================
	// LÓGICA COMÚN DE REGISTRO
	// =========================
	private ResponseEntity<?> doRegister(String nombre, String apellido, String correo, String username,
			String password, MultipartFile imagen) {
		if (usuarioService.existeCorreo(correo)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Correo duplicado");
		}
		if (usuarioService.existeUsername(username)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuario duplicado");
		}

		var rolCliente = rolRepository.findByNombreRol("ROLE_CLIENTE")
				.orElseThrow(() -> new IllegalStateException("Configura ROLE_CLIENTE"));

		String ruta = null;
		try {
			ruta = (imagen != null) ? fileStorage.saveUserPhoto(imagen) : null;
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Foto inválida: " + e.getMessage());
		}

		var u = new Usuario();
		u.setCodigoUsuario(usuarioService.generarCodigoUsuario());
		u.setNombre(nombre.trim());
		u.setApellido(apellido.trim());
		u.setCorreo(correo.trim().toLowerCase());
		u.setUsername(username.trim().toLowerCase());
		u.setPassword(password);
		u.setRol(rolCliente);
		u.setImagenUrl(ruta);

		boolean ok = usuarioService.registrar(u);
		if (ok) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicado");
		}
	}
}
