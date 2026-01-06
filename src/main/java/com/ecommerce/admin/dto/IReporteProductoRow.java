package com.ecommerce.admin.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface IReporteProductoRow {
	Integer getIdProducto();

	String getCodigoProducto();

	String getNombre();

	String getCategoria();

	String getMarca();

	BigDecimal getPrecio();

	Integer getStock();

	String getImagenUrl();

	Timestamp getFechaCreacion();
}
