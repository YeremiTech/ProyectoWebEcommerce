package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categorias")
public class Categoria {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_categoria")
	private Integer idCategoria;

	@Column(name = "codigo_categoria", nullable = false, unique = true, length = 30)
	private String codigo;

	@Column(name = "nombre_categoria", nullable = false, unique = true, length = 100)
	private String nombre;
}
