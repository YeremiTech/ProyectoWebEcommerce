package com.ecommerce.cliente.dto;

import jakarta.validation.constraints.*;

public record MetodoPagoRequest(@NotBlank String tipoPago, @NotBlank String nombreTarjeta,
		@Pattern(regexp = "\\d{13,19}") String numeroTarjeta, @Pattern(regexp = "\\d{3,4}") String cvv,
		@NotBlank String direccionFacturacion) {
}
