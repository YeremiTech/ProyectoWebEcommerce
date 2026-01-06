package com.ecommerce.admin.dto;

import java.math.BigDecimal;

public record ProductoResponse(Integer id, String codigo, String nombre, BigDecimal precio, Integer stock,
		Integer idCategoria, String categoria, Integer idMarca, String marca, String imagenUrl) {
}
