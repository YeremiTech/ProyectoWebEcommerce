package com.ecommerce.admin.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ecommerce.admin.dto.IReportePedidoRow;
import com.ecommerce.admin.dto.IReporteProductoRow;
import com.ecommerce.admin.dto.IReporteUsuarioRow;

public interface AdminReporteService {
	Page<IReportePedidoRow> pedidos(LocalDate from, LocalDate to, String estado, Pageable pageable);

	Page<IReporteUsuarioRow> usuarios(LocalDate from, LocalDate to, String rol, String q, Pageable pageable);

	Page<IReporteProductoRow> productos(LocalDate from, LocalDate to, Integer idCategoria, Integer idMarca,
			Integer stockBelow, String q, Pageable pageable);

	List<IReportePedidoRow> pedidos(LocalDate from, LocalDate to, String estado);

	List<IReporteUsuarioRow> usuarios(LocalDate from, LocalDate to, String rol, String q);

	List<IReporteProductoRow> productos(LocalDate from, LocalDate to, Integer idCategoria, Integer idMarca,
			Integer stockBelow, String q);
}
