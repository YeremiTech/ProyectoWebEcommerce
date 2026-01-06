package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "marcas")
public class Marca {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_marca")
	private Integer idMarca;

	@Column(name = "codigo_marca", nullable = false, unique = true, length = 30)
	private String codigo;

	@Column(name = "nombre_marca", nullable = false, unique = true, length = 100)
	private String nombre;

}
