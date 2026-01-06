package com.ecommerce.cliente.repository;

import com.ecommerce.entity.Carrito;
import com.ecommerce.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
	Optional<Carrito> findByUsuario(Usuario usuario);
}