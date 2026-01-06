package com.ecommerce.cliente.service;

import java.util.List;

import com.ecommerce.cliente.dto.CarritoItemRequest;
import com.ecommerce.cliente.dto.CarritoItemResponse;

public interface CarritoService {
	List<CarritoItemResponse> verCarrito(String username);

	List<CarritoItemResponse> agregarItem(String username, CarritoItemRequest req);

	List<CarritoItemResponse> quitarItem(String username, Integer idItem);

	List<CarritoItemResponse> actualizarCantidad(String username, Integer idItem, int cantidad);

	void vaciar(String username);
}
