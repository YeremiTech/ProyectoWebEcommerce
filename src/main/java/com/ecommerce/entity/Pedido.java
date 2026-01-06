package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pedido")
	private Integer idPedido;

	@Column(name = "codigo_pedido", nullable = false, unique = true)
	private String codigoPedido;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@Column(name = "fecha")
	private Timestamp fecha;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_estado")
	private EstadoPedido estado;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal total;
}
