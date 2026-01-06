package com.ecommerce.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ecommerce.admin.dto.UsuarioAdminRequest;
import com.ecommerce.admin.dto.UsuarioAdminResponse;

public interface AdminUsuarioService {
	Page<UsuarioAdminResponse> listar(String q, Pageable pageable);

	UsuarioAdminResponse obtener(Integer id);

	UsuarioAdminResponse crear(UsuarioAdminRequest req);

	UsuarioAdminResponse actualizar(Integer id, UsuarioAdminRequest req);

	void eliminar(Integer id);

	String siguienteCodigo();
}
