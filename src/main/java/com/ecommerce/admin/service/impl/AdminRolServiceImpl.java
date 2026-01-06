package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dto.OpcionResponse;
import com.ecommerce.admin.repository.AdminRolRepository;
import com.ecommerce.admin.service.AdminRolService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminRolServiceImpl implements AdminRolService {
	private final AdminRolRepository repo;

	@Override
	public List<OpcionResponse> opciones() {
		return repo.findAllByOrderByNombreRolAsc().stream().map(r -> new OpcionResponse(r.getIdRol(), r.getNombreRol()))
				.toList();
	}
}
