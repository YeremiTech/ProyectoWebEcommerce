package com.ecommerce.admin.dto;

import jakarta.validation.constraints.*;

public record UsuarioAdminRequest(@NotBlank String nombre, @NotBlank String apellido, @Email @NotBlank String correo,
		@NotBlank String username, String password, @NotNull Integer idRol, String imagenUrl) {
}
