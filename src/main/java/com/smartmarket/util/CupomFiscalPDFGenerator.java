package com.smartmarket.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.smartmarket.model.ItemVenda;
import com.smartmarket.model.Venda;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class CupomFiscalPDFGenerator {

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public static byte[] generate(Venda venda) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.setMargins(20, 20, 20, 20);

        document.add(new Paragraph("SMARTMARKET MINI")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(14));
        document.add(new Paragraph("MERCADINHO DO SEU ZÉ")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(12));
        document.add(new Paragraph("Rua das Flores, 123 - Centro")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10));
        document.add(new Paragraph("CNPJ: 12.345.678/0001-99 | IE: 123.456.789.012")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10));
        document.add(new Paragraph("--------------------------------------------------------------------")
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("CUPOM FISCAL - VENDA Nº " + String.format("%06d", venda.getId()))
                .setTextAlignment(TextAlignment.CENTER)
                .setBold());
        document.add(new Paragraph("Data: " + venda.getDataHoraFormatada() + " | Caixa: " + venda.getUsuario().getNome())
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10));
        document.add(new Paragraph("--------------------------------------------------------------------")
                .setTextAlignment(TextAlignment.CENTER));

        Table table = new Table(UnitValue.createPercentArray(new float[]{4, 1, 1.5f, 1.5f}))
                .useAllAvailableWidth();
        table.addHeaderCell(new Paragraph("ITEM").setBold().setFontSize(10));
        table.addHeaderCell(new Paragraph("QTD").setBold().setFontSize(10).setTextAlignment(TextAlignment.RIGHT));
        table.addHeaderCell(new Paragraph("VL UNIT").setBold().setFontSize(10).setTextAlignment(TextAlignment.RIGHT));
        table.addHeaderCell(new Paragraph("VL TOTAL").setBold().setFontSize(10).setTextAlignment(TextAlignment.RIGHT));

        for (ItemVenda item : venda.getItens()) {
            table.addCell(new Paragraph(item.getProduto().getNome()).setFontSize(9));
            table.addCell(new Paragraph(item.getQuantidade().setScale(0, RoundingMode.HALF_UP).toString()).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Paragraph(CURRENCY_FORMAT.format(item.getPrecoUnitario())).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Paragraph(CURRENCY_FORMAT.format(item.getSubtotal())).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
        }

        document.add(table);

        document.add(new Paragraph("--------------------------------------------------------------------")
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("TOTAL R$: " + CURRENCY_FORMAT.format(venda.getTotal()))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBold()
                .setFontSize(12));
        document.add(new Paragraph("Forma Pagamento: " + venda.getFormaPagamento().getDescricao())
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(10));
        document.add(new Paragraph("Valor Pago: " + CURRENCY_FORMAT.format(venda.getValorPago()))
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(10));
        document.add(new Paragraph("Troco: " + CURRENCY_FORMAT.format(venda.getTroco()))
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(10));

        document.add(new Paragraph("\nOBRIGADO PELA PREFERÊNCIA!")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(11));
        document.add(new Paragraph("Volte sempre! :)")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10));
        document.add(new Paragraph("--------------------------------------------------------------------")
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(venda.getDataHoraFormatada() + "   " + String.format("%06d", venda.getId()) + "-001-2025")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(8));

        document.close();
        return baos.toByteArray();
    }
}
