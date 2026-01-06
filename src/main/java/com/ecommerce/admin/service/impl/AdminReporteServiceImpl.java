package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dto.IReportePedidoRow;
import com.ecommerce.admin.dto.IReporteProductoRow;
import com.ecommerce.admin.dto.IReporteUsuarioRow;
import com.ecommerce.admin.repository.AdminReporteRepository;
import com.ecommerce.admin.service.AdminReporteService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReporteServiceImpl implements AdminReporteService {

	private final AdminReporteRepository repo;

	@Override
	public Page<IReportePedidoRow> pedidos(LocalDate from, LocalDate to, String estado, Pageable pageable) {
		return repo.reportePedidos(from, to, (estado == null || estado.isBlank()) ? null : estado, pageable);
	}

	@Override
	public Page<IReporteUsuarioRow> usuarios(LocalDate from, LocalDate to, String rol, String q, Pageable pageable) {
		return repo.reporteUsuarios(from, to, (rol == null || rol.isBlank()) ? null : rol,
				(q == null || q.isBlank()) ? null : q, pageable);
	}

	@Override
	public Page<IReporteProductoRow> productos(LocalDate from, LocalDate to, Integer idCategoria, Integer idMarca,
			Integer stockBelow, String q, Pageable pageable) {
		return repo.reporteProductos(from, to, idCategoria, idMarca, stockBelow, (q == null || q.isBlank()) ? null : q,
				pageable);
	}

	@Override
	public List<IReportePedidoRow> pedidos(LocalDate from, LocalDate to, String estado) {
		return repo.reportePedidos(from, to, (estado == null || estado.isBlank()) ? null : estado, Pageable.unpaged())
				.getContent();
	}

	@Override
	public List<IReporteUsuarioRow> usuarios(LocalDate from, LocalDate to, String rol, String q) {
		return repo.reporteUsuarios(from, to, (rol == null || rol.isBlank()) ? null : rol,
				(q == null || q.isBlank()) ? null : q, Pageable.unpaged()).getContent();
	}

	@Override
	public List<IReporteProductoRow> productos(LocalDate from, LocalDate to, Integer idCategoria, Integer idMarca,
			Integer stockBelow, String q) {
		return repo.reporteProductos(from, to, idCategoria, idMarca, stockBelow, (q == null || q.isBlank()) ? null : q,
				Pageable.unpaged()).getContent();
	}
}