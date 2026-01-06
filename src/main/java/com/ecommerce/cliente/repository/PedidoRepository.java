package com.ecommerce.cliente.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecommerce.entity.Pedido;
import com.ecommerce.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
	Optional<Pedido> findTopByOrderByIdPedidoDesc();

	Page<Pedido> findByUsuarioOrderByFechaDesc(Usuario usuario, Pageable pageable);

	Optional<Pedido> findByIdPedidoAndUsuario(Integer idPedido, Usuario usuario);

	@Query("select p.codigoPedido from Pedido p")
	List<String> findAllCodigos();

	boolean existsByCodigoPedido(String codigoPedido);
}