package com.ecommerce.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ecommerce.admin.dto.CategoriaRequest;
import com.ecommerce.admin.dto.CategoriaResponse;
import com.ecommerce.admin.dto.OpcionResponse;

public interface AdminCategoriaService {

	Page<CategoriaResponse> listar(String q, Pageable pageable);

	CategoriaResponse crear(CategoriaRequest req);

	CategoriaResponse obtener(Integer id);

	CategoriaResponse actualizar(Integer id, CategoriaRequest req);

	void eliminar(Integer id);

	List<OpcionResponse> opciones();

	String siguienteCodigo();
}
