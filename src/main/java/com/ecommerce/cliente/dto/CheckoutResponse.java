package com.ecommerce.cliente.dto;

import java.math.BigDecimal;

public record CheckoutResponse(String codigoPedido, BigDecimal total) {
}
