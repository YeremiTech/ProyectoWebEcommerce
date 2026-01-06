package com.ecommerce.admin.dto;

import jakarta.validation.constraints.*;

public record CambiarContrasenaRequest(@NotBlank String currentPassword,
		@NotBlank @Size(min = 6, max = 100) String newPassword) {
}
