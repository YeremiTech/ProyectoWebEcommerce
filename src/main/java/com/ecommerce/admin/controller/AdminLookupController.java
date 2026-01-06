package com.ecommerce.admin.controller;

import com.ecommerce.admin.dto.OpcionResponse;
import com.ecommerce.admin.service.AdminCategoriaService;
import com.ecommerce.admin.service.AdminMarcaService;
import com.ecommerce.admin.service.AdminRolService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/lookups")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminLookupController {

	private final AdminCategoriaService categoriaService;
	private final AdminMarcaService marcaService;
	private final AdminRolService rolService;

	@GetMapping("/categorias")
	public List<OpcionResponse> categorias() {
		return categoriaService.opciones();
	}

	@GetMapping("/marcas")
	public List<OpcionResponse> marcas() {
		return marcaService.opciones();
	}

	@GetMapping("/roles")
	public List<OpcionResponse> roles() {
		return rolService.opciones();
	}
}
