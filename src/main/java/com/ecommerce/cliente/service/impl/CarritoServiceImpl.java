package com.ecommerce.cliente.service.impl;

import com.ecommerce.admin.repository.AdminProductoRepository;
import com.ecommerce.auth.repository.AuthUsuarioRepository;
import com.ecommerce.cliente.dto.CarritoItemRequest;
import com.ecommerce.cliente.dto.CarritoItemResponse;
import com.ecommerce.cliente.repository.CarritoItemRepository;
import com.ecommerce.cliente.repository.CarritoRepository;
import com.ecommerce.cliente.service.CarritoService;
import com.ecommerce.entity.Carrito;
import com.ecommerce.entity.CarritoItem;
import com.ecommerce.entity.Usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

	private final AuthUsuarioRepository usuarioRepo;
	private final CarritoRepository carritoRepo;
	private final CarritoItemRepository itemRepo;
	private final AdminProductoRepository productoRepo;

	private static CarritoItemResponse map(CarritoItem it) {
		var p = it.getProducto();
		var subtotal = it.getPrecioUnitario().multiply(BigDecimal.valueOf(it.getCantidad()));
		return new CarritoItemResponse(it.getIdItem(), p.getIdProducto(), p.getNombre(), p.getCategoria().getNombre(),
				p.getMarca().getNombre(), it.getCantidad(), it.getPrecioUnitario(), subtotal, p.getImagenUrl());
	}

	private Carrito ensureCarrito(Usuario u) {
		return carritoRepo.findByUsuario(u).orElseGet(() -> {
			var c = new Carrito();
			c.setUsuario(u);
			return carritoRepo.save(c);
		});
	}

	@Override
	@Transactional(readOnly = true)
	public List<CarritoItemResponse> verCarrito(String username) {
		var u = usuarioRepo.findByUsername(username).orElseThrow();
		var c = ensureCarrito(u);
		return itemRepo.findByCarrito(c).stream().map(CarritoServiceImpl::map).toList();
	}

	@Override
	@Transactional
	public List<CarritoItemResponse> agregarItem(String username, CarritoItemRequest req) {
		var u = usuarioRepo.findByUsername(username).orElseThrow();
		var c = ensureCarrito(u);
		var p = productoRepo.findById(req.idProducto())
				.orElseThrow(() -> new IllegalArgumentException("Producto no existe"));

		if (p.getStock() < req.cantidad())
			throw new IllegalArgumentException("Stock insuficiente");

		var existente = itemRepo.findByCarritoAndProducto(c, p).orElse(null);
		if (existente == null) {
			var it = new CarritoItem();
			it.setCarrito(c);
			it.setProducto(p);
			it.setCantidad(req.cantidad());
			it.setPrecioUnitario(p.getPrecio());
			itemRepo.save(it);
		} else {
			int nueva = existente.getCantidad() + req.cantidad();
			if (p.getStock() < nueva)
				throw new IllegalArgumentException("Stock insuficiente");
			existente.setCantidad(nueva);
			itemRepo.save(existente);
		}
		return verCarrito(username);
	}

	@Override
	@Transactional
	public List<CarritoItemResponse> quitarItem(String username, Integer idItem) {
		var u = usuarioRepo.findByUsername(username).orElseThrow();
		var c = ensureCarrito(u);
		var it = itemRepo.findById(idItem).orElseThrow(() -> new IllegalArgumentException("Item no existe"));
		if (!it.getCarrito().getIdCarrito().equals(c.getIdCarrito()))
			throw new IllegalArgumentException("Item no pertenece a tu carrito");
		itemRepo.delete(it);
		return verCarrito(username);
	}

	@Override
	@Transactional
	public List<CarritoItemResponse> actualizarCantidad(String username, Integer idItem, int cantidad) {
		var u = usuarioRepo.findByUsername(username).orElseThrow();
		var c = ensureCarrito(u);
		var it = itemRepo.findById(idItem).orElseThrow(() -> new IllegalArgumentException("Item no existe"));
		if (!it.getCarrito().getIdCarrito().equals(c.getIdCarrito()))
			throw new IllegalArgumentException("Item no pertenece a tu carrito");

		var p = it.getProducto();
		if (p.getStock() < cantidad)
			throw new IllegalArgumentException("Stock insuficiente");

		it.setCantidad(cantidad);
		itemRepo.save(it);
		return verCarrito(username);
	}

	@Override
	@Transactional
	public void vaciar(String username) {
		var u = usuarioRepo.findByUsername(username).orElseThrow();
		var c = ensureCarrito(u);
		itemRepo.deleteByCarrito(c);
	}
}
