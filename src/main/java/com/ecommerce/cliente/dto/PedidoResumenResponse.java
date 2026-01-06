package com.ecommerce.cliente.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record PedidoResumenResponse(Integer idPedido, String codigoPedido, Instant fecha, String estado,
		BigDecimal total) {
}
