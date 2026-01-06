package com.ecommerce.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ecommerce.admin.dto.MarcaRequest;
import com.ecommerce.admin.dto.MarcaResponse;
import com.ecommerce.admin.dto.OpcionResponse;

public interface AdminMarcaService {
	Page<MarcaResponse> listar(String q, Pageable pageable);

	MarcaResponse crear(MarcaRequest req);

	MarcaResponse obtener(Integer id);

	MarcaResponse actualizar(Integer id, MarcaRequest req);

	void eliminar(Integer id);

	List<OpcionResponse> opciones();

	String siguienteCodigo();
}
