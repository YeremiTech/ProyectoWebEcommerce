package com.ecommerce.auth.service;

import java.util.Optional;

import com.ecommerce.admin.dto.ActualizarPerfilRequest;
import com.ecommerce.admin.dto.PerfilResponse;
import com.ecommerce.entity.Usuario;

public interface AuthUsuarioService {
	boolean registrar(Usuario usuario);

	Optional<Usuario> porUsername(String username);

	boolean existeCorreo(String correo);

	boolean existeUsername(String username);

	String generarCodigoUsuario();

	boolean existeCodigo(String codigo);

	PerfilResponse perfilDeUsuario(String username);

	PerfilResponse actualizarPerfilDeUsuario(String username, ActualizarPerfilRequest req, String nuevaImagenUrl);

	void cambiarContrasenaDeUsuario(String username, String currentPassword, String newPassword);

}
