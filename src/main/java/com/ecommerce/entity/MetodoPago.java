package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "metodos_pago")
public class MetodoPago {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_metodo_pago")
	private Integer idMetodoPago;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@Column(name = "tipo_pago", nullable = false)
	private String tipoPago;
	@Column(name = "nombre_tarjeta")
	private String nombreTarjeta;
	@Column(name = "numero_tarjeta")
	private String numeroTarjeta;
	@Column(name = "cvv", nullable = false)
	private String cvv;
	@Column(name = "direccion_facturacion")
	private String direccionFacturacion;
}
