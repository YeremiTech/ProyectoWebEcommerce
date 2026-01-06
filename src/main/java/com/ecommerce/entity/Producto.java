package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_producto")
	private Integer idProducto;

	@Column(name = "codigo_producto", nullable = false, unique = true, length = 30)
	private String codigo;

	@Column(nullable = false, length = 255)
	private String nombre;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal precio;

	@Column(nullable = false)
	private Integer stock;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_categoria")
	private Categoria categoria;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_marca")
	private Marca marca;

	@Column(name = "imagen_url")
	private String imagenUrl;
}
