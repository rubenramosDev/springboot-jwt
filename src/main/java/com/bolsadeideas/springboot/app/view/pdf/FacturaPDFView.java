package com.bolsadeideas.springboot.app.view.pdf;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Map;

@Component("factura/ver")
public class FacturaPDFView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> map, Document document, PdfWriter pdfWriter, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Factura factura = (Factura) map.get("factura");
        PdfPTable table = new PdfPTable(1);
        table.setSpacingAfter(20);
        PdfPCell celda = null;
        celda = new PdfPCell(new Phrase("Datos del cliente:"));
        celda.setBackgroundColor(new Color(184, 218, 255));
        celda.setPadding(8f);

        table.addCell(celda);
        table.addCell(factura.getCliente().getNombre().concat(" ").concat(factura.getCliente().getApellido()));
        table.addCell(factura.getCliente().getEmail());

        PdfPTable table2 = new PdfPTable(1);
        table2.setSpacingAfter(20);

        celda = new PdfPCell(new Phrase("Datos de la factura:"));
        celda.setBackgroundColor(new Color(195, 230, 203));
        celda.setPadding(8f);

        table2.addCell(celda);
        table2.addCell("Folio: " + factura.getId());
        table2.addCell("Descripcion: " + factura.getDescripcion());
        table2.addCell("Fecha: " + factura.getCreateAt());

        document.add(table);
        document.add(table2);

        PdfPTable table3 = new PdfPTable(4);
        table3.setWidths(new float[]{3.5f, 1, 1, 1});
        table3.addCell("Producto");
        table3.addCell("Precio");
        table3.addCell("Cantidad");
        table3.addCell("Total");

        for (ItemFactura item : factura.getItems()) {
            table3.addCell(item.getProducto().getNombre());
            table3.addCell(item.getProducto().getPrecio().toString());
            celda = new PdfPCell(new Phrase(item.getCantidad().toString()));
            celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table3.addCell(celda);
            table3.addCell(item.calcularImporte().toString());
        }

        celda = new PdfPCell(new Phrase("Total: "));
        celda.setColspan(3);
        celda.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table3.addCell(celda);

        table3.addCell(factura.getTotal().toString());
        document.add(table3);

    }
}
