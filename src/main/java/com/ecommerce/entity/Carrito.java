package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carritos")
public class Carrito {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_carrito")
	private Integer idCarrito;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@Column(name = "fecha_creacion")
	private Timestamp fechaCreacion;
	@Column(name = "fecha_actualizacion")
	private Timestamp fechaActualizacion;

	@PrePersist
	void pre() {
		var now = new Timestamp(System.currentTimeMillis());
		fechaCreacion = now;
		fechaActualizacion = now;
	}

	@PreUpdate
	void upd() {
		fechaActualizacion = new Timestamp(System.currentTimeMillis());
	}
}
