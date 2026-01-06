package com.ecommerce.admin.dto;

import jakarta.validation.constraints.*;

public record CategoriaRequest(String codigo, @NotBlank @Size(max = 100) String nombre) {
}
