package com.ecommerce.auth.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.admin.dto.ActualizarPerfilRequest;
import com.ecommerce.admin.dto.PerfilResponse;
import com.ecommerce.auth.repository.AuthUsuarioRepository;
import com.ecommerce.auth.service.AuthUsuarioService;
import com.ecommerce.entity.Usuario;
import com.ecommerce.util.GeneradorCodigo;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthUsuarioServiceImpl implements AuthUsuarioService {

	private final AuthUsuarioRepository repo;
	private final GeneradorCodigo generador;
	private final PasswordEncoder encoder;

	@Override
	public String generarCodigoUsuario() {
		return generador.siguiente("USR", repo.findAllCodigos().stream());
	}

	@Override
	public boolean registrar(Usuario u) {
		if (repo.existsByUsername(u.getUsername()) || repo.existsByCorreo(u.getCorreo()))
			return false;

		if (u.getCodigoUsuario() == null || u.getCodigoUsuario().isBlank()) {
			u.setCodigoUsuario(generarCodigoUsuario());
		}

		u.setUsername(u.getUsername().trim().toLowerCase());
		u.setCorreo(u.getCorreo().trim().toLowerCase());
		u.setPassword(encoder.encode(u.getPassword()));

		repo.save(u);
		return true;
	}

	@Override
	public Optional<Usuario> porUsername(String username) {
		return repo.findByUsername(username.trim().toLowerCase());
	}

	@Override
	public boolean existeCorreo(String c) {
		return repo.existsByCorreo(c.trim().toLowerCase());
	}

	@Override
	public boolean existeUsername(String u) {
		return repo.existsByUsername(u.trim().toLowerCase());
	}

	@Override
	public boolean existeCodigo(String c) {
		return repo.existsByCodigoUsuario(c);
	}

	private static PerfilResponse map(Usuario u) {
		return new PerfilResponse(u.getIdUsuario(), u.getCodigoUsuario(), u.getNombre(), u.getApellido(), u.getCorreo(),
				u.getUsername(), u.getRol().getNombreRol(), u.getImagenUrl());
	}

	@Override
	@Transactional(readOnly = true)
	public PerfilResponse perfilDeUsuario(String username) {
		var u = repo.findByUsername(username.trim().toLowerCase())
				.orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));
		return map(u);
	}

	@Override
	public PerfilResponse actualizarPerfilDeUsuario(String username, ActualizarPerfilRequest req,
			String nuevaImagenUrl) {
		var u = repo.findByUsername(username.trim().toLowerCase())
				.orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));

		String correo = req.correo().trim().toLowerCase();
		String uname = req.username().trim().toLowerCase();

		if (repo.existsByCorreoIgnoreCaseAndIdUsuarioNot(correo, u.getIdUsuario()))
			throw new IllegalArgumentException("El correo ya está registrado por otro usuario");
		if (repo.existsByUsernameIgnoreCaseAndIdUsuarioNot(uname, u.getIdUsuario()))
			throw new IllegalArgumentException("El username ya está registrado por otro usuario");

		u.setNombre(req.nombre().trim());
		u.setApellido(req.apellido().trim());
		u.setCorreo(correo);
		u.setUsername(uname);
		if (nuevaImagenUrl != null && !nuevaImagenUrl.isBlank()) {
			u.setImagenUrl(nuevaImagenUrl);
		}
		return map(repo.save(u));
	}

	@Override
	public void cambiarContrasenaDeUsuario(String username, String currentPassword, String newPassword) {
		var u = repo.findByUsername(username.trim().toLowerCase())
				.orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));
		if (!encoder.matches(currentPassword, u.getPassword()))
			throw new org.springframework.security.authentication.BadCredentialsException(
					"La contraseña actual no es válida");
		u.setPassword(encoder.encode(newPassword));
		repo.save(u);
	}

}
