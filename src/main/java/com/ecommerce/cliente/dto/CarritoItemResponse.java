package com.ecommerce.cliente.dto;

import java.math.BigDecimal;

public record CarritoItemResponse(Integer idItem, Integer idProducto, String producto, String categoria, String marca,
		Integer cantidad, BigDecimal precioUnitario, BigDecimal subtotal, String imagenUrl) {
}
