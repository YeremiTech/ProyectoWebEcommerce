package com.ecommerce.cliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.entity.EstadoPedido;

import java.util.Optional;

public interface EstadoPedidoRepository extends JpaRepository<EstadoPedido, Integer> {
	Optional<EstadoPedido> findByNombreEstado(String nombre);
}