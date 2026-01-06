package com.ecommerce.admin.repository;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.admin.dto.IReportePedidoRow;
import com.ecommerce.admin.dto.IReporteProductoRow;
import com.ecommerce.admin.dto.IReporteUsuarioRow;

@Repository
public interface AdminReporteRepository extends JpaRepository<com.ecommerce.entity.Usuario, Integer> {

	@Query(value = """
			SELECT p.id_pedido   AS idPedido,
			       p.codigo_pedido AS codigoPedido,
			       p.fecha        AS fecha,
			       e.nombre_estado AS estado,
			       u.id_usuario   AS idUsuario,
			       CONCAT(u.nombre,' ',u.apellido) AS nombreCompleto,
			       p.total        AS total
			FROM pedidos p
			JOIN usuarios u ON u.id_usuario = p.id_usuario
			JOIN estados_pedido e ON e.id_estado = p.id_estado
			WHERE (:from IS NULL OR DATE(p.fecha) >= :from)
			  AND (:to   IS NULL OR DATE(p.fecha) <= :to)
			  AND (:estado IS NULL OR e.nombre_estado = :estado)
			""", countQuery = """
			SELECT COUNT(1)
			FROM pedidos p
			JOIN usuarios u ON u.id_usuario = p.id_usuario
			JOIN estados_pedido e ON e.id_estado = p.id_estado
			WHERE (:from IS NULL OR DATE(p.fecha) >= :from)
			  AND (:to   IS NULL OR DATE(p.fecha) <= :to)
			  AND (:estado IS NULL OR e.nombre_estado = :estado)
			""", nativeQuery = true)
	Page<IReportePedidoRow> reportePedidos(@Param("from") LocalDate from, @Param("to") LocalDate to,
			@Param("estado") String estado, Pageable pageable);

	// ===== USUARIOS =====
	@Query(value = """
			SELECT u.id_usuario    AS idUsuario,
			       u.codigo_usuario AS codigoUsuario,
			       u.nombre         AS nombre,
			       u.apellido       AS apellido,
			       u.correo         AS correo,
			       u.username       AS username,
			       r.nombre_rol     AS rol,
			       u.imagen_url     AS imagenUrl,
			       u.fecha_creacion AS fechaCreacion
			FROM usuarios u
			JOIN roles r ON r.id_rol = u.id_rol
			WHERE (:from IS NULL OR DATE(u.fecha_creacion) >= :from)
			  AND (:to   IS NULL OR DATE(u.fecha_creacion) <= :to)
			  AND (:rol  IS NULL OR r.nombre_rol = :rol)
			  AND (:q IS NULL OR u.username LIKE CONCAT('%',:q,'%')
			                  OR u.correo   LIKE CONCAT('%',:q,'%')
			                  OR u.nombre   LIKE CONCAT('%',:q,'%')
			                  OR u.apellido LIKE CONCAT('%',:q,'%'))
			""", countQuery = """
			SELECT COUNT(1)
			FROM usuarios u
			JOIN roles r ON r.id_rol = u.id_rol
			WHERE (:from IS NULL OR DATE(u.fecha_creacion) >= :from)
			  AND (:to   IS NULL OR DATE(u.fecha_creacion) <= :to)
			  AND (:rol  IS NULL OR r.nombre_rol = :rol)
			  AND (:q IS NULL OR u.username LIKE CONCAT('%',:q,'%')
			                  OR u.correo   LIKE CONCAT('%',:q,'%')
			                  OR u.nombre   LIKE CONCAT('%',:q,'%')
			                  OR u.apellido LIKE CONCAT('%',:q,'%'))
			""", nativeQuery = true)
	Page<IReporteUsuarioRow> reporteUsuarios(@Param("from") LocalDate from, @Param("to") LocalDate to,
			@Param("rol") String rol, @Param("q") String q, Pageable pageable);

	@Query(value = """
			SELECT p.id_producto    AS idProducto,
			       p.codigo_producto AS codigoProducto,
			       p.nombre          AS nombre,
			       c.nombre_categoria AS categoria,
			       m.nombre_marca     AS marca,
			       p.precio          AS precio,
			       p.stock           AS stock,
			       p.imagen_url      AS imagenUrl,
			       p.fecha_creacion  AS fechaCreacion
			FROM productos p
			JOIN categorias c ON c.id_categoria = p.id_categoria
			JOIN marcas m     ON m.id_marca     = p.id_marca
			WHERE (:from IS NULL OR DATE(p.fecha_creacion) >= :from)
			  AND (:to   IS NULL OR DATE(p.fecha_creacion) <= :to)
			  AND (:idCategoria IS NULL OR p.id_categoria = :idCategoria)
			  AND (:idMarca     IS NULL OR p.id_marca     = :idMarca)
			  AND (:stockBelow  IS NULL OR p.stock <= :stockBelow)
			  AND (:q IS NULL OR p.nombre LIKE CONCAT('%',:q,'%'))
			""", countQuery = """
			SELECT COUNT(1)
			FROM productos p
			JOIN categorias c ON c.id_categoria = p.id_categoria
			JOIN marcas m     ON m.id_marca     = p.id_marca
			WHERE (:from IS NULL OR DATE(p.fecha_creacion) >= :from)
			  AND (:to   IS NULL OR DATE(p.fecha_creacion) <= :to)
			  AND (:idCategoria IS NULL OR p.id_categoria = :idCategoria)
			  AND (:idMarca     IS NULL OR p.id_marca     = :idMarca)
			  AND (:stockBelow  IS NULL OR p.stock <= :stockBelow)
			  AND (:q IS NULL OR p.nombre LIKE CONCAT('%',:q,'%'))
			""", nativeQuery = true)
	Page<IReporteProductoRow> reporteProductos(@Param("from") LocalDate from, @Param("to") LocalDate to,
			@Param("idCategoria") Integer idCategoria, @Param("idMarca") Integer idMarca,
			@Param("stockBelow") Integer stockBelow, @Param("q") String q, Pageable pageable);
}
