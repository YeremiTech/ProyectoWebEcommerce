package com.ecommerce.admin.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface IReportePedidoRow {
	Integer getIdPedido();

	String getCodigoPedido();

	Timestamp getFecha();

	String getEstado();

	Integer getIdUsuario();

	String getNombreCompleto();

	BigDecimal getTotal();
}
