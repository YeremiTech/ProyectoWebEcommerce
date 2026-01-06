package com.ecommerce.cliente.dto;

public record MetodoPagoResponse(Integer idMetodoPago, String tipoPago, String nombreTarjeta,
		String numeroTarjetaMasked, String direccionFacturacion) {
}
