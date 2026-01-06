package com.ecommerce.cliente.dto;

import java.math.BigDecimal;

public record ProductoClienteResponse(Integer id, String codigo, String nombre, BigDecimal precio, String categoria,
		String marca, String imagenUrl) {
}
