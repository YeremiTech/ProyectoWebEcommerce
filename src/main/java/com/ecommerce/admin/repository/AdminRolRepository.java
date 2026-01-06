package com.ecommerce.admin.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.entity.Rol;

public interface AdminRolRepository extends JpaRepository<Rol, Integer> {
	Optional<Rol> findByNombreRol(String nombreRol);

	List<Rol> findAllByOrderByNombreRolAsc();
}
