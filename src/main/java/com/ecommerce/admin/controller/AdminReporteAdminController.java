package com.ecommerce.admin.controller;

import com.ecommerce.admin.dto.IReportePedidoRow;
import com.ecommerce.admin.dto.IReporteProductoRow;
import com.ecommerce.admin.dto.IReporteUsuarioRow;
import com.ecommerce.admin.service.AdminReporteService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reportes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminReporteAdminController {

	private final AdminReporteService service;

	@GetMapping("/pedidos")
	public Page<IReportePedidoRow> pedidos(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
			@RequestParam(required = false) String estado, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "fecha,desc") String sort) {
		var sp = sort.split(",");
		Pageable pageable = PageRequest.of(page, size,
				Sort.by(Sort.Direction.fromString(sp.length > 1 ? sp[1] : "desc"), sp[0]));
		return service.pedidos(from, to, estado, pageable);
	}

	@GetMapping("/usuarios")
	public Page<IReporteUsuarioRow> usuarios(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
			@RequestParam(required = false) String rol, @RequestParam(required = false) String q,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "fechaCreacion,desc") String sort) {
		var sp = sort.split(",");
		Pageable pageable = PageRequest.of(page, size,
				Sort.by(Sort.Direction.fromString(sp.length > 1 ? sp[1] : "desc"), sp[0]));
		return service.usuarios(from, to, rol, q, pageable);
	}

	@GetMapping("/productos")
	public Page<IReporteProductoRow> productos(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
			@RequestParam(required = false) Integer idCategoria, @RequestParam(required = false) Integer idMarca,
			@RequestParam(required = false) Integer stockBelow, @RequestParam(required = false) String q,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "fechaCreacion,desc") String sort) {
		var sp = sort.split(",");
		Pageable pageable = PageRequest.of(page, size,
				Sort.by(Sort.Direction.fromString(sp.length > 1 ? sp[1] : "desc"), sp[0]));
		return service.productos(from, to, idCategoria, idMarca, stockBelow, q, pageable);
	}

	@GetMapping("/pedidos/excel")
	public void exportPedidosExcel(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
			@RequestParam(required = false) String estado, HttpServletResponse response) throws IOException {

		List<IReportePedidoRow> rows = service.pedidos(from, to, estado);

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_pedidos.xlsx");

		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("Pedidos");

		int rowIdx = 0;

		Row header = sheet.createRow(rowIdx++);
		header.createCell(0).setCellValue("CÓDIGO PEDIDO");
		header.createCell(1).setCellValue("FECHA");
		header.createCell(2).setCellValue("ESTADO");
		header.createCell(3).setCellValue("ID USUARIO");
		header.createCell(4).setCellValue("CLIENTE");
		header.createCell(5).setCellValue("TOTAL");

		for (IReportePedidoRow r : rows) {
			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(r.getCodigoPedido());
			row.createCell(1).setCellValue(r.getFecha().toString());
			row.createCell(2).setCellValue(r.getEstado());
			row.createCell(3).setCellValue(r.getIdUsuario());
			row.createCell(4).setCellValue(r.getNombreCompleto());
			row.createCell(5).setCellValue(r.getTotal().doubleValue());
		}

		wb.write(response.getOutputStream());
		wb.close();
	}

	@GetMapping("/pedidos/pdf")
	public void exportPedidosPdf(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
			@RequestParam(required = false) String estado, HttpServletResponse response) throws IOException {

		List<IReportePedidoRow> rows = service.pedidos(from, to, estado);

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_pedidos.pdf");

		Document doc = new Document(PageSize.A4.rotate());
		PdfWriter.getInstance(doc, response.getOutputStream());
		doc.open();

		doc.add(new Paragraph("Reporte de Pedidos"));
		doc.add(new Paragraph(" "));

		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);

		table.addCell("CÓDIGO PEDIDO");
		table.addCell("FECHA");
		table.addCell("ESTADO");
		table.addCell("ID USUARIO");
		table.addCell("CLIENTE");
		table.addCell("TOTAL");

		for (IReportePedidoRow r : rows) {
			table.addCell(r.getCodigoPedido());
			table.addCell(r.getFecha().toString());
			table.addCell(r.getEstado());
			table.addCell(String.valueOf(r.getIdUsuario()));
			table.addCell(r.getNombreCompleto());
			table.addCell(r.getTotal().toPlainString());
		}

		doc.add(table);
		doc.close();
	}

	@GetMapping("/usuarios/excel")
	public void exportUsuariosExcel(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
			@RequestParam(required = false) String rol, @RequestParam(required = false) String q,
			HttpServletResponse response) throws IOException {

		List<IReporteUsuarioRow> rows = service.usuarios(from, to, rol, q);

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_usuarios.xlsx");

		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("Usuarios");
		int rowIdx = 0;

		Row header = sheet.createRow(rowIdx++);
		header.createCell(0).setCellValue("ID");
		header.createCell(1).setCellValue("CÓDIGO");
		header.createCell(2).setCellValue("NOMBRE");
		header.createCell(3).setCellValue("APELLIDO");
		header.createCell(4).setCellValue("CORREO");
		header.createCell(5).setCellValue("USERNAME");
		header.createCell(6).setCellValue("ROL");
		header.createCell(7).setCellValue("FECHA CREACIÓN");

		for (IReporteUsuarioRow r : rows) {
			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(r.getIdUsuario());
			row.createCell(1).setCellValue(r.getCodigoUsuario());
			row.createCell(2).setCellValue(r.getNombre());
			row.createCell(3).setCellValue(r.getApellido());
			row.createCell(4).setCellValue(r.getCorreo());
			row.createCell(5).setCellValue(r.getUsername());
			row.createCell(6).setCellValue(r.getRol());
			row.createCell(7).setCellValue(r.getFechaCreacion().toString());
		}

		wb.write(response.getOutputStream());
		wb.close();
	}

	@GetMapping("/usuarios/pdf")
	public void exportUsuariosPdf(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
			@RequestParam(required = false) String rol, @RequestParam(required = false) String q,
			HttpServletResponse response) throws IOException {

		List<IReporteUsuarioRow> rows = service.usuarios(from, to, rol, q);

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_usuarios.pdf");

		Document doc = new Document(PageSize.A4.rotate());
		PdfWriter.getInstance(doc, response.getOutputStream());
		doc.open();

		doc.add(new Paragraph("Reporte de Usuarios"));
		doc.add(new Paragraph(" "));

		PdfPTable table = new PdfPTable(7);
		table.setWidthPercentage(100);

		table.addCell("USUARIO");
		table.addCell("CORREO");
		table.addCell("ROL");
		table.addCell("CÓDIGO");
		table.addCell("USERNAME");
		table.addCell("NOMBRE");
		table.addCell("FECHA CREACIÓN");

		for (IReporteUsuarioRow r : rows) {
			table.addCell(r.getNombre() + " " + r.getApellido());
			table.addCell(r.getCorreo());
			table.addCell(r.getRol());
			table.addCell(r.getCodigoUsuario());
			table.addCell(r.getUsername());
			table.addCell(r.getNombre() + " " + r.getApellido());
			table.addCell(r.getFechaCreacion().toString());
		}

		doc.add(table);
		doc.close();
	}

	@GetMapping("/productos/excel")
	public void exportProductosExcel(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
			@RequestParam(required = false) Integer idCategoria, @RequestParam(required = false) Integer idMarca,
			@RequestParam(required = false) Integer stockBelow, @RequestParam(required = false) String q,
			HttpServletResponse response) throws IOException {

		List<IReporteProductoRow> rows = service.productos(from, to, idCategoria, idMarca, stockBelow, q);

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_productos.xlsx");

		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("Productos");
		int rowIdx = 0;

		Row header = sheet.createRow(rowIdx++);
		header.createCell(0).setCellValue("ID");
		header.createCell(1).setCellValue("CÓDIGO");
		header.createCell(2).setCellValue("NOMBRE");
		header.createCell(3).setCellValue("CATEGORÍA");
		header.createCell(4).setCellValue("MARCA");
		header.createCell(5).setCellValue("PRECIO");
		header.createCell(6).setCellValue("STOCK");
		header.createCell(7).setCellValue("IMAGEN");
		header.createCell(8).setCellValue("FECHA CREACIÓN");

		for (IReporteProductoRow r : rows) {
			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(r.getIdProducto());
			row.createCell(1).setCellValue(r.getCodigoProducto());
			row.createCell(2).setCellValue(r.getNombre());
			row.createCell(3).setCellValue(r.getCategoria());
			row.createCell(4).setCellValue(r.getMarca());
			row.createCell(5).setCellValue(r.getPrecio().doubleValue());
			row.createCell(6).setCellValue(r.getStock());
			row.createCell(7).setCellValue(r.getImagenUrl());
			row.createCell(8).setCellValue(r.getFechaCreacion().toString());
		}

		wb.write(response.getOutputStream());
		wb.close();
	}

	@GetMapping("/productos/pdf")
	public void exportProductosPdf(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
			@RequestParam(required = false) Integer idCategoria, @RequestParam(required = false) Integer idMarca,
			@RequestParam(required = false) Integer stockBelow, @RequestParam(required = false) String q,
			HttpServletResponse response) throws IOException {

		List<IReporteProductoRow> rows = service.productos(from, to, idCategoria, idMarca, stockBelow, q);

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_productos.pdf");

		Document doc = new Document(PageSize.A4.rotate());
		PdfWriter.getInstance(doc, response.getOutputStream());
		doc.open();

		doc.add(new Paragraph("Reporte de Productos"));
		doc.add(new Paragraph(" "));

		PdfPTable table = new PdfPTable(7);
		table.setWidthPercentage(100);

		table.addCell("CÓDIGO");
		table.addCell("NOMBRE");
		table.addCell("CATEGORÍA");
		table.addCell("MARCA");
		table.addCell("STOCK");
		table.addCell("PRECIO");
		table.addCell("FECHA CREACIÓN");

		for (IReporteProductoRow r : rows) {
			table.addCell(r.getCodigoProducto());
			table.addCell(r.getNombre());
			table.addCell(r.getCategoria());
			table.addCell(r.getMarca());
			table.addCell(String.valueOf(r.getStock()));
			table.addCell(r.getPrecio().toPlainString());
			table.addCell(r.getFechaCreacion().toString());
		}

		doc.add(table);
		doc.close();
	}
}