package com.ecommerce.admin.repository;

import com.ecommerce.entity.Producto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminProductoRepository extends JpaRepository<Producto, Integer> {

	@Query("select p.codigo from Producto p")
	List<String> findAllCodigos();

	boolean existsByCodigo(String codigo);

	Page<Producto> findByNombreContainingIgnoreCase(String q, Pageable p);

	Optional<Producto> findTopByOrderByIdProductoDesc();
}
