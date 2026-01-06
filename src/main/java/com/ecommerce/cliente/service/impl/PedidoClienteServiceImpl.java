package com.ecommerce.cliente.service.impl;

import com.ecommerce.auth.repository.AuthUsuarioRepository;
import com.ecommerce.cliente.dto.PedidoDetalleItemResponse;
import com.ecommerce.cliente.dto.PedidoDetalleResponse;
import com.ecommerce.cliente.dto.PedidoResumenResponse;
import com.ecommerce.cliente.repository.PedidoItemRepository;
import com.ecommerce.cliente.repository.PedidoRepository;
import com.ecommerce.cliente.service.PedidoClienteService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoClienteServiceImpl implements PedidoClienteService {

	private final AuthUsuarioRepository usuarioRepo;
	private final PedidoRepository pedidoRepo;
	private final PedidoItemRepository pedidoItemRepo;

	@Override
	@Transactional(readOnly = true)
	public Page<PedidoResumenResponse> listar(String username, Pageable pageable) {
		var u = usuarioRepo.findByUsername(username).orElseThrow();
		var page = pedidoRepo.findByUsuarioOrderByFechaDesc(u, pageable);
		return page.map(p -> new PedidoResumenResponse(p.getIdPedido(), p.getCodigoPedido(), p.getFecha().toInstant(),
				p.getEstado().getNombreEstado(), p.getTotal()));
	}

	@Override
	@Transactional(readOnly = true)
	public PedidoDetalleResponse detalle(String username, Integer idPedido) {
		var u = usuarioRepo.findByUsername(username).orElseThrow();
		var p = pedidoRepo.findByIdPedidoAndUsuario(idPedido, u)
				.orElseThrow(() -> new IllegalArgumentException("Pedido no existe"));

		var items = pedidoItemRepo.findByPedido(p).stream()
				.map(pi -> new PedidoDetalleItemResponse(pi.getProducto().getIdProducto(), pi.getProducto().getNombre(),
						pi.getProducto().getCategoria().getNombre(), pi.getProducto().getMarca().getNombre(),
						pi.getPrecioUnitario(), pi.getCantidad(),
						pi.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(pi.getCantidad())),
						pi.getProducto().getImagenUrl()))
				.collect(Collectors.toList());

		return new PedidoDetalleResponse(p.getIdPedido(), p.getCodigoPedido(), p.getFecha().toInstant(),
				p.getEstado().getNombreEstado(), p.getTotal(), items);
	}
}
