package com.bolsadeideas.springboot.app.view.xlsx;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component("factura/ver.xlsx")
public class FacturaXlxsView extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"Factura-View.xlsx\"");

        Factura factura = (Factura) map.get("factura");
        Sheet sheet = workbook.createSheet();
        //Forma una
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);

        cell.setCellValue("Datos del cliente");
        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue(factura.getCliente().getNombre().concat(" ").concat(factura.getCliente().getApellido()));

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue(factura.getCliente().getEmail());

        //Forma dos
        sheet.createRow(4).createCell(0).setCellValue("Datos de la factura");
        sheet.createRow(5).createCell(0).setCellValue("Folio: ".concat(factura.getId().toString()));
        sheet.createRow(6).createCell(0).setCellValue("Descripcion: ".concat(factura.getDescripcion()));
        sheet.createRow(7).createCell(0).setCellValue("Fecha: ".concat(factura.getCreateAt().toString()));

        CellStyle cellStyleTHead = workbook.createCellStyle();
        cellStyleTHead.setBorderBottom(BorderStyle.MEDIUM);
        cellStyleTHead.setBorderTop(BorderStyle.MEDIUM);
        cellStyleTHead.setBorderLeft(BorderStyle.MEDIUM);
        cellStyleTHead.setBorderRight(BorderStyle.MEDIUM);
        cellStyleTHead.setFillForegroundColor(IndexedColors.GREEN.index);
        cellStyleTHead.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle cellStyleTBody = workbook.createCellStyle();
        cellStyleTBody.setBorderBottom(BorderStyle.THIN);
        cellStyleTBody.setBorderTop(BorderStyle.THIN);
        cellStyleTBody.setBorderLeft(BorderStyle.THIN);
        cellStyleTBody.setBorderRight(BorderStyle.THIN);

        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue("Producto");
        header.createCell(1).setCellValue("Precio");
        header.createCell(2).setCellValue("Cantidad");
        header.createCell(3).setCellValue("Total");

        header.getCell(0).setCellStyle(cellStyleTHead);
        header.getCell(1).setCellStyle(cellStyleTHead);
        header.getCell(2).setCellStyle(cellStyleTHead);
        header.getCell(3).setCellStyle(cellStyleTHead);

        int rowNumber = 10;
        for (ItemFactura item : factura.getItems()) {
            Row fila = sheet.createRow(rowNumber++);

            cell = fila.createCell(0);
            cell.setCellValue(item.getProducto().getNombre());
            cell.setCellStyle(cellStyleTBody);

            cell = fila.createCell(1);
            cell.setCellValue(item.getProducto().getPrecio());
            cell.setCellStyle(cellStyleTBody);

            cell = fila.createCell(2);
            cell.setCellValue(item.getCantidad());
            cell.setCellStyle(cellStyleTBody);

            cell = fila.createCell(3);
            cell.setCellValue(item.calcularImporte());
            cell.setCellStyle(cellStyleTBody);
        }

        Row filaTotal = sheet.createRow(rowNumber);
        filaTotal.createCell(2).setCellValue("Total");
        filaTotal.createCell(3).setCellValue(factura.getTotal());
    }
}
