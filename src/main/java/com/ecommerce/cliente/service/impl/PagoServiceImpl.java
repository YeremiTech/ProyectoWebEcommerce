package com.ecommerce.cliente.service.impl;

import com.ecommerce.auth.repository.AuthUsuarioRepository;
import com.ecommerce.cliente.dto.MetodoPagoRequest;
import com.ecommerce.cliente.dto.MetodoPagoResponse;
import com.ecommerce.cliente.repository.MetodoPagoRepository;
import com.ecommerce.cliente.service.PagoService;
import com.ecommerce.entity.MetodoPago;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

	private final AuthUsuarioRepository usuarioRepo;
	private final MetodoPagoRepository metodoRepo;

	private static MetodoPagoResponse map(MetodoPago mp) {
		String masked = mp.getNumeroTarjeta() == null ? null
				: "**** **** **** " + mp.getNumeroTarjeta().substring(Math.max(mp.getNumeroTarjeta().length() - 4, 0));
		return new MetodoPagoResponse(mp.getIdMetodoPago(), mp.getTipoPago(), mp.getNombreTarjeta(), masked,
				mp.getDireccionFacturacion());
	}

	@Override
	@Transactional
	public MetodoPagoResponse guardarActualizar(String username, MetodoPagoRequest req) {
		var u = usuarioRepo.findByUsername(username).orElseThrow();
		var mp = metodoRepo.findFirstByUsuarioOrderByIdMetodoPagoDesc(u).orElse(new MetodoPago());
		mp.setUsuario(u);
		mp.setTipoPago(req.tipoPago());
		mp.setNombreTarjeta(req.nombreTarjeta());
		mp.setNumeroTarjeta(req.numeroTarjeta());
		mp.setCvv(req.cvv());
		mp.setDireccionFacturacion(req.direccionFacturacion());
		return map(metodoRepo.save(mp));
	}

	@Override
	@Transactional(readOnly = true)
	public MetodoPagoResponse obtener(String username) {
		var u = usuarioRepo.findByUsername(username).orElseThrow();
		var mp = metodoRepo.findFirstByUsuarioOrderByIdMetodoPagoDesc(u).orElse(null);
		return mp == null ? null : map(mp);
	}
}
