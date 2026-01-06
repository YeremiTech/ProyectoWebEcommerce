package com.ecommerce.admin.dto;

import jakarta.validation.constraints.*;

public record MarcaRequest(String codigo, @NotBlank @Size(max = 100) String nombre, String imagenUrl) {
}
