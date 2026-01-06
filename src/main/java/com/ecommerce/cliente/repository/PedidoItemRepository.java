package com.ecommerce.cliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.entity.Pedido;
import com.ecommerce.entity.PedidoItem;

import java.util.List;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Integer> {
	List<PedidoItem> findByPedido(Pedido pedido);
}
