package com.ecommerce.cliente.dto;

import java.math.BigDecimal;

public record PedidoDetalleItemResponse(Integer idProducto, String producto, String categoria, String marca,
		BigDecimal precioUnitario, Integer cantidad, BigDecimal subtotal, String imagenUrl) {
}
