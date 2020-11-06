package com.rafael.report.services;

import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.*;
import com.rafael.report.utils.StaticData;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class PdfReportHeaderFooterPageEvent extends PdfPageEventHelper {

	private PdfTemplate pdfTemplate;
	private Image total;

	public void onOpenDocument(PdfWriter writer, Document document) {
		pdfTemplate = writer.getDirectContent().createTemplate(30, 16);
		try {
			total = Image.getInstance(pdfTemplate);
			total.setRole(PdfName.ARTIFACT);
		} catch (DocumentException ex) {
			throw new ExceptionConverter(ex);
		}
	}

//	onStartPage method of PdfPageEventHelper class which gets called when document.open() is called.
//	You may set HEADER in this method.
//	It also gets called whenever document.newPage() is called.

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
//		onEndPage method of PdfPageEventHelper class is called just before document is about to be closed.
//		You may set FOOTER in this method.
		addHeader(writer);
		addFooter(writer);
	}

	private void addHeader(PdfWriter writer) {
		PdfPTable header = new PdfPTable(2);
		try {
			// set defaults
			header.setWidths(new int[] { 3, 24 });
			header.setTotalWidth(527);
			header.setLockedWidth(true);
			header.getDefaultCell().setFixedHeight(45);
			header.getDefaultCell().setBorder(Rectangle.BOTTOM);
			header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

			// Inserting image in Header
//			Image logo = Image.getInstance(PdfReportHeaderFooterPageEvent.class.getResource("/logo_icon1.jpg"));
			Image logo = Image.getInstance(
					StaticData.DIR_PATH_NAME + File.separator + "images" + File.separator + "logo_icon1.jpg");
//			logo.scaleAbsolute(120f, 60f);// image width,height
			header.addCell(logo);

			// add text
			PdfPCell text = new PdfPCell();
			text.setPaddingBottom(15);
			text.setPaddingLeft(10);
			text.setBorder(Rectangle.BOTTOM);
			text.setBorderColor(BaseColor.LIGHT_GRAY);
			text.addElement(new Phrase("RAFFLES OIL LFTZ ENTERPRISES",
					new Font(FontFamily.HELVETICA, 12, Font.BOLDITALIC, BaseColor.RED)));
			text.addElement(new Phrase("REF & FRAC DAILY REPORT",
					new Font(FontFamily.HELVETICA, 12, Font.BOLDITALIC, BaseColor.RED)));
			header.addCell(text);

			// write content
			header.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		} catch (MalformedURLException e) {
			throw new ExceptionConverter(e);
		} catch (IOException e) {
			throw new ExceptionConverter(e);
		}
	}

	private void addFooter(PdfWriter writer) {
		PdfPTable footer = new PdfPTable(3);
		try {
			// set defaults
			footer.setWidths(new int[] { 24, 2, 1 });
			footer.setTotalWidth(527);
			footer.setLockedWidth(true);
			footer.getDefaultCell().setFixedHeight(40);
			footer.getDefaultCell().setBorder(Rectangle.TOP);
			footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

			// add copyright
			footer.addCell(new Phrase("\u00A9 Designed and developed by Analogic Automation Pvt. Ltd.",
					new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD)));

			// add current page count
			footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			footer.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()),
					new Font(Font.FontFamily.HELVETICA, 8)));

			// add placeholder for total page count
			PdfPCell totalPageCount = new PdfPCell(total);
			totalPageCount.setBorder(Rectangle.TOP);
			totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
			footer.addCell(totalPageCount);

			// write page
			PdfContentByte canvas = writer.getDirectContent();
			canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
			footer.writeSelectedRows(0, -1, 34, 50, canvas);
			canvas.endMarkedContentSequence();
		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}
	}

	public void onCloseDocument(PdfWriter writer, Document document) {
		int totalLength = String.valueOf(writer.getPageNumber()).length();
		int totalWidth = totalLength * 5;
		ColumnText.showTextAligned(pdfTemplate, Element.ALIGN_RIGHT,
				new Phrase(String.valueOf(writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)), totalWidth,
				6, 0);
	}
}
