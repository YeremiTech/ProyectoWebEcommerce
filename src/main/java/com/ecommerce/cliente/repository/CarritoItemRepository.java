package com.ecommerce.cliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.entity.Carrito;
import com.ecommerce.entity.CarritoItem;
import com.ecommerce.entity.Producto;

import java.util.*;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Integer> {
	List<CarritoItem> findByCarrito(Carrito carrito);

	Optional<CarritoItem> findByCarritoAndProducto(Carrito carrito, Producto producto);

	void deleteByCarrito(Carrito carrito);
}