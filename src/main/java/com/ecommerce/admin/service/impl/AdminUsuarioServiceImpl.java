package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dto.UsuarioAdminRequest;
import com.ecommerce.admin.dto.UsuarioAdminResponse;
import com.ecommerce.admin.repository.AdminRolRepository;
import com.ecommerce.admin.repository.AdminUsuarioRepository;
import com.ecommerce.admin.service.AdminUsuarioService;
import com.ecommerce.entity.Usuario;
import com.ecommerce.util.GeneradorCodigo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUsuarioServiceImpl implements AdminUsuarioService {

	private final AdminUsuarioRepository repo;
	private final AdminRolRepository rolRepo;
	private final GeneradorCodigo generador;
	private final PasswordEncoder encoder;

	private static UsuarioAdminResponse map(Usuario u) {
		return new UsuarioAdminResponse(u.getIdUsuario(), u.getCodigoUsuario(), u.getNombre(), u.getApellido(),
				u.getCorreo(), u.getUsername(), u.getRol().getNombreRol(), u.getImagenUrl());
	}

	@Override
	public String siguienteCodigo() {
		return generador.siguiente("USR", repo.findAllCodigos().stream());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UsuarioAdminResponse> listar(String q, Pageable pageable) {
		Page<Usuario> page = (q == null || q.isBlank()) ? repo.findAll(pageable)
				: repo.findByUsernameContainingIgnoreCaseOrCorreoContainingIgnoreCase(q.trim(), q.trim(), pageable);

		return page.map(AdminUsuarioServiceImpl::map);
	}

	@Override
	@Transactional(readOnly = true)
	public UsuarioAdminResponse obtener(Integer id) {
		var u = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));
		return map(u);
	}

	@Override
	public UsuarioAdminResponse crear(UsuarioAdminRequest r) {
		var rol = rolRepo.findById(r.idRol()).orElseThrow(() -> new IllegalArgumentException("Rol no existe"));

		var u = new Usuario();
		u.setCodigoUsuario(siguienteCodigo());
		u.setNombre(r.nombre().trim());
		u.setApellido(r.apellido().trim());
		u.setCorreo(r.correo().trim().toLowerCase());
		u.setUsername(r.username().trim().toLowerCase());

		u.setPassword(encoder.encode(r.password()));

		u.setRol(rol);
		u.setImagenUrl(r.imagenUrl());

		return map(repo.save(u));
	}

	@Override
	public UsuarioAdminResponse actualizar(Integer id, UsuarioAdminRequest r) {
		var u = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));

		var rol = rolRepo.findById(r.idRol()).orElseThrow(() -> new IllegalArgumentException("Rol no existe"));

		u.setNombre(r.nombre().trim());
		u.setApellido(r.apellido().trim());
		u.setCorreo(r.correo().trim().toLowerCase());
		u.setUsername(r.username().trim().toLowerCase());

		if (r.password() != null && !r.password().isBlank()) {
			u.setPassword(encoder.encode(r.password()));
		}

		if (r.imagenUrl() != null) {
			u.setImagenUrl(r.imagenUrl());
		}

		u.setRol(rol);

		return map(repo.save(u));
	}

	@Override
	public void eliminar(Integer id) {
		if (!repo.existsById(id)) {
			throw new IllegalArgumentException("Usuario no existe");
		}
		repo.deleteById(id);
	}
}
