package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carrito_items")
public class CarritoItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_item")
	private Integer idItem;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_carrito")
	private Carrito carrito;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_producto")
	private Producto producto;

	@Column(nullable = false)
	private Integer cantidad;
	@Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
	private BigDecimal precioUnitario;
}
