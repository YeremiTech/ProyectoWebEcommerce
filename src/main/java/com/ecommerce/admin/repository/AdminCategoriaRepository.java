package com.ecommerce.admin.repository;

import com.ecommerce.entity.Categoria;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminCategoriaRepository extends JpaRepository<Categoria, Integer> {

	@Query("select c.codigo from Categoria c")
	List<String> findAllCodigos();

	boolean existsByCodigo(String codigo);

	boolean existsByNombreIgnoreCase(String nombre);

	Page<Categoria> findByNombreContainingIgnoreCase(String q, Pageable pageable);

	Optional<Categoria> findTopByOrderByIdCategoriaDesc();

	List<Categoria> findAllByOrderByNombreAsc();
}
