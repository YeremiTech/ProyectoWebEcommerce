package com.ecommerce.cliente.service.impl;

import com.ecommerce.admin.repository.AdminProductoRepository;
import com.ecommerce.auth.repository.AuthUsuarioRepository;
import com.ecommerce.cliente.dto.CheckoutResponse;
import com.ecommerce.cliente.repository.CarritoItemRepository;
import com.ecommerce.cliente.repository.CarritoRepository;
import com.ecommerce.cliente.repository.EstadoPedidoRepository;
import com.ecommerce.cliente.repository.MetodoPagoRepository;
import com.ecommerce.cliente.repository.PedidoItemRepository;
import com.ecommerce.cliente.repository.PedidoRepository;
import com.ecommerce.cliente.service.CheckoutService;
import com.ecommerce.entity.EstadoPedido;
import com.ecommerce.entity.Pedido;
import com.ecommerce.entity.PedidoItem;
import com.ecommerce.util.GeneradorCodigo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

	private final GeneradorCodigo generador;
	private final AuthUsuarioRepository usuarioRepo;
	private final CarritoRepository carritoRepo;
	private final CarritoItemRepository itemRepo;
	private final AdminProductoRepository productoRepo;
	private final MetodoPagoRepository metodoRepo;
	private final EstadoPedidoRepository estadoRepo;
	private final PedidoRepository pedidoRepo;
	private final PedidoItemRepository pedidoItemRepo;

	private String generarCodigo() {
		return generador.siguiente("PED", pedidoRepo.findAllCodigos().stream());
	}

	@Override
	@Transactional
	public CheckoutResponse confirmar(String username) {
		var u = usuarioRepo.findByUsername(username).orElseThrow();
		var carrito = carritoRepo.findByUsuario(u).orElseThrow(() -> new IllegalStateException("Carrito vacío"));
		var items = itemRepo.findByCarrito(carrito);

		if (items.isEmpty())
			throw new IllegalStateException("Carrito vacío");
		if (metodoRepo.findFirstByUsuarioOrderByIdMetodoPagoDesc(u).isEmpty())
			throw new IllegalStateException("NO_METODO_PAGO");

		BigDecimal total = BigDecimal.ZERO;
		for (var it : items) {
			var p = productoRepo.findById(it.getProducto().getIdProducto()).orElseThrow();
			if (p.getStock() < it.getCantidad())
				throw new IllegalStateException("Stock insuficiente para " + p.getNombre());
			total = total.add(it.getPrecioUnitario().multiply(BigDecimal.valueOf(it.getCantidad())));
		}

		for (var it : items) {
			var p = productoRepo.findById(it.getProducto().getIdProducto()).orElseThrow();
			p.setStock(p.getStock() - it.getCantidad());
			productoRepo.save(p);
		}

		var estadoPendiente = estadoRepo.findByNombreEstado("Pendiente")
				.orElseGet(() -> estadoRepo.save(new EstadoPedido(null, "Pendiente")));

		var pedido = new Pedido();
		pedido.setCodigoPedido(generarCodigo());
		pedido.setUsuario(u);
		pedido.setFecha(new Timestamp(System.currentTimeMillis()));
		pedido.setEstado(estadoPendiente);
		pedido.setTotal(total);
		pedido = pedidoRepo.save(pedido);

		for (var it : items) {
			var pi = new PedidoItem();
			pi.setPedido(pedido);
			pi.setProducto(it.getProducto());
			pi.setCantidad(it.getCantidad());
			pi.setPrecioUnitario(it.getPrecioUnitario());
			pedidoItemRepo.save(pi);
		}

		itemRepo.deleteByCarrito(carrito);

		return new CheckoutResponse(pedido.getCodigoPedido(), total);
	}
}
