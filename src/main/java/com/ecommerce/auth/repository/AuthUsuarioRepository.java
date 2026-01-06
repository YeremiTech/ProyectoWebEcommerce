package com.ecommerce.auth.repository;

import com.ecommerce.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

public interface AuthUsuarioRepository extends JpaRepository<Usuario, Integer> {

	boolean existsByUsername(String username);

	boolean existsByCorreo(String correo);

	@Query("select u.codigoUsuario from Usuario u")
	List<String> findAllCodigos();

	boolean existsByCodigoUsuario(String codigoUsuario);

	boolean existsByCorreoIgnoreCaseAndIdUsuarioNot(String correo, Integer idUsuario);

	boolean existsByUsernameIgnoreCaseAndIdUsuarioNot(String username, Integer idUsuario);

	Optional<Usuario> findTopByOrderByIdUsuarioDesc();

	@EntityGraph(attributePaths = "rol")
	Optional<Usuario> findByUsername(String username);

	@EntityGraph(attributePaths = "rol")
	Page<Usuario> findAll(Pageable pageable);

	@EntityGraph(attributePaths = "rol")
	Page<Usuario> findByUsernameContainingIgnoreCaseOrCorreoContainingIgnoreCase(String username, String correo,
			Pageable pageable);

}
