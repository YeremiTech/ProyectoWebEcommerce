package com.ecommerce.cliente.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ecommerce.cliente.dto.PedidoDetalleResponse;
import com.ecommerce.cliente.dto.PedidoResumenResponse;

public interface PedidoClienteService {
	Page<PedidoResumenResponse> listar(String username, Pageable pageable);

	PedidoDetalleResponse detalle(String username, Integer idPedido);
}
