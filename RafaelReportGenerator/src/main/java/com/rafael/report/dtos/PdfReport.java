package com.rafael.report.dtos;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
//import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
//import com.itextpdf.text.pdf.PdfPTable;

public class PdfReport {
	private static final String FILE_PATH_NAME = "c:/reports/itext.pdf";
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		PdfReport pdfReport =new PdfReport();
//		try {
//			pdfReport.createPdf(FILE_PATH_NAME);
//		} catch (IOException | DocumentException ex) {
//			ex.printStackTrace();
//		}
//	}

	public PdfPCell createCell(String content, int colspan, int rowspan, int border) {
		Font font = new Font(FontFamily.HELVETICA, 10);
		PdfPCell cell = new PdfPCell(new Phrase(content, font));
		cell.setColspan(colspan);
		cell.setRowspan(rowspan);
		cell.setBorder(border);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		return cell;
	}

	public void createPdf(String dest) throws IOException, DocumentException {
		Font font = new Font(FontFamily.HELVETICA, 10);
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(dest));
		document.open();
		PdfPTable table = new PdfPTable(8);
		table.setWidthPercentage(100);
		table.addCell(createCell("Examination", 1, 2, PdfPCell.BOX));
		table.addCell(createCell("Board", 1, 2, PdfPCell.BOX));
		table.addCell(createCell("Month and Year of Passing", 1, 2, PdfPCell.BOX));
		table.addCell(createCell("", 1, 1, PdfPCell.TOP));
		table.addCell(createCell("Marks", 2, 1, PdfPCell.TOP));
		table.addCell(createCell("Percentage", 1, 2, PdfPCell.BOX));
		table.addCell(createCell("Class / Grade", 1, 2, PdfPCell.BOX));
		table.addCell(createCell("", 1, 1, PdfPCell.BOX));
		table.addCell(createCell("Obtained", 1, 1, PdfPCell.BOX));
		table.addCell(createCell("Out of", 1, 1, PdfPCell.BOX));
		table.addCell(createCell("12th / I.B. Diploma", 1, 1, PdfPCell.BOX));
		table.addCell(createCell("", 1, 1, PdfPCell.BOX));
		table.addCell(createCell("", 1, 1, PdfPCell.BOX));
		table.addCell(createCell("Aggregate (all subjects)", 1, 1, PdfPCell.BOX));
		table.addCell(createCell("", 1, 1, PdfPCell.BOX));
		table.addCell(createCell("", 1, 1, PdfPCell.BOX));
		table.addCell(createCell("", 1, 1, PdfPCell.BOX));
		table.addCell(createCell("", 1, 1, PdfPCell.BOX));
		document.add(table);
		document.close();
	}
}
