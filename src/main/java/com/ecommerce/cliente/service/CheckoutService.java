package com.ecommerce.cliente.service;

import com.ecommerce.cliente.dto.CheckoutResponse;

public interface CheckoutService {
	CheckoutResponse confirmar(String username);
}
