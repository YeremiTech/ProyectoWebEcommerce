package com.ecommerce.cliente.service;

import com.ecommerce.cliente.dto.MetodoPagoRequest;
import com.ecommerce.cliente.dto.MetodoPagoResponse;

public interface PagoService {
	MetodoPagoResponse guardarActualizar(String username, MetodoPagoRequest req);

	MetodoPagoResponse obtener(String username);
}
