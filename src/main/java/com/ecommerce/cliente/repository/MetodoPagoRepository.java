package com.ecommerce.cliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.entity.MetodoPago;
import com.ecommerce.entity.Usuario;

import java.util.*;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer> {
	Optional<MetodoPago> findFirstByUsuarioOrderByIdMetodoPagoDesc(Usuario usuario);

	boolean existsByUsuario(Usuario usuario);
}
