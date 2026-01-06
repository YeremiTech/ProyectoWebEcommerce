package com.ecommerce.admin.dto;

import java.sql.Timestamp;

public interface IReporteUsuarioRow {
	Integer getIdUsuario();

	String getCodigoUsuario();

	String getNombre();

	String getApellido();

	String getCorreo();

	String getUsername();

	String getRol();

	String getImagenUrl();

	Timestamp getFechaCreacion();
}
