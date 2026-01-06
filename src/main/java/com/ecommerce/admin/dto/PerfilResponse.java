package com.ecommerce.admin.dto;

public record PerfilResponse(Integer id, String codigoUsuario, String nombre, String apellido, String correo,
		String username, String role, String imagenUrl) {
}
