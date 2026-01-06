package com.ecommerce.admin.dto;

import jakarta.validation.constraints.*;

public record ActualizarPerfilRequest(@NotBlank String nombre, @NotBlank String apellido,
		@Email @NotBlank String correo, @NotBlank String username) {
}
