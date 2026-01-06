package com.ecommerce.admin.repository;

import com.ecommerce.entity.Marca;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminMarcaRepository extends JpaRepository<Marca, Integer> {

	@Query("select m.codigo from Marca m")
	List<String> findAllCodigos();

	boolean existsByCodigo(String codigo);

	boolean existsByNombreIgnoreCase(String nombre);

	Page<Marca> findByNombreContainingIgnoreCase(String q, Pageable p);

	Optional<Marca> findTopByOrderByIdMarcaDesc();

	List<Marca> findAllByOrderByNombreAsc();
}
