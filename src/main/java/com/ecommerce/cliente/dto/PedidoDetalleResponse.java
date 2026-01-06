package com.ecommerce.cliente.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PedidoDetalleResponse(Integer idPedido, String codigoPedido, Instant fecha, String estado,
		BigDecimal total, List<PedidoDetalleItemResponse> items) {
}
