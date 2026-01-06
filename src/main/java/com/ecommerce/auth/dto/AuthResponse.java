package com.ecommerce.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
	private String accessToken;
	private Integer id;
	private String codigoUsuario;
	private String username;
	private String nombre;
	private String apellido;
	private String role;
	private String imagenUrl;
}
