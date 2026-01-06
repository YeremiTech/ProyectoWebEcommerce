package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Integer idUsuario;

	@Column(name = "codigo_usuario", nullable = false)
	private String codigoUsuario;

	private String nombre;
	private String apellido;
	private String correo;
	private String username;
	private String password;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_rol")
	private Rol rol;

	@Column(name = "imagen_url")
	private String imagenUrl;
}
