package com.ecommerce.admin.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductoRequest(String codigo, @NotBlank @Size(max = 255) String nombre,
		@NotNull @DecimalMin("0.0") BigDecimal precio, @NotNull @Min(0) Integer stock, @NotNull Integer idCategoria,
		@NotNull Integer idMarca, String imagenUrl) {
}
