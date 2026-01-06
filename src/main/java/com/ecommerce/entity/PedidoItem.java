package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedido_items")
public class PedidoItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pedido_item")
	private Integer idPedidoItem;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_pedido")
	private Pedido pedido;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_producto")
	private Producto producto;

	@Column(nullable = false)
	private Integer cantidad;

	@Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
	private BigDecimal precioUnitario;
}
