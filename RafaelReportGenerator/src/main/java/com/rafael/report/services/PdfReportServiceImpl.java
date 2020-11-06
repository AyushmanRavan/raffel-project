package com.rafael.report.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rafael.report.dtos.Email;
import com.rafael.report.utils.DataModel;
import com.rafael.report.utils.OilType;
import com.rafael.report.utils.StaticData;
import com.rafael.report.utils.SummaryType;
import com.rafael.report.utils.TagData;

@SuppressWarnings("unused")
@Service
public class PdfReportServiceImpl implements PdfReportService {

	@Autowired
	private JdbcDataService jdbcDataService;

	@Override
	public void createReportPdfFile(LocalDateTime startDateTime, LocalDateTime endDateTime, String shiftName,
			String fileName) {
		try {
			File file = new File(StaticData.DIR_PATH_NAME + File.separator + fileName);

			// Create FileOutputStream - the file in which created PDF will be stored
			OutputStream fileout = new FileOutputStream(file);

			// create Document (This document will represent the PDF document)
			Document document = newDocument();

			// whenever any PDF element is added to document,
			// it will get written to FileOutputStream
			PdfWriter writer = newPdfWriter(document, fileout);

			// add header and footer
			writer.setPageEvent(new PdfReportHeaderFooterPageEvent());
			writer.setBoxSize("art", new Rectangle(0, 0, 559, 788));
			// open document
			document.open();

			Map<OilType, BigDecimal> calculatedValues = addReportTableData(document, startDateTime, endDateTime,
					shiftName);
			document.newPage();
			addReportTableSummary(document, calculatedValues);

			document.close(); // close document
			fileout.close(); // close fileOutputStream
//			System.out.println(
//					"Pdf Report created successfully.. >> " + StaticData.DIR_PATH_NAME + File.separator + fileName);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void createDifferencePdfFile(LocalDateTime startDateTime, LocalDateTime endDateTime, String fileName,
			String shiftName) {
		try {
			File file = new File(StaticData.DIR_PATH_NAME + File.separator + fileName);

			// Create FileOutputStream - the file in which created PDF will be stored
			OutputStream fileout = new FileOutputStream(file);

			// create Document (This document will represent the PDF document)
			Document document = newDocument();

			// whenever any PDF element is added to document,
			// it will get written to FileOutputStream
			PdfWriter writer = newPdfWriter(document, fileout);

			// add header and footer
			writer.setPageEvent(new PdfReportHeaderFooterPageEvent());
			writer.setBoxSize("art", new Rectangle(0, 0, 559, 788));
			// open document
			document.open();

			// Actual logic start here.
			Paragraph paragraph = new Paragraph();
			addEmptyLine(paragraph, 1);
			document.add(paragraph);

			document.add(addHeaderInfo(startDateTime, endDateTime));
			addDifferenceTableData(document, startDateTime, endDateTime, shiftName);

			document.close(); // close document
			fileout.close(); // close fileOutputStream
//			System.out.println(
//					"Pdf Difference created successfully.. >> " + StaticData.DIR_PATH_NAME + File.separator + fileName);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void addDifferenceTableData(Document document, LocalDateTime startDateTime, LocalDateTime endDateTime,
			String shiftName) throws DocumentException {

		Font bfBold12 = new Font(FontFamily.HELVETICA, 11, Font.BOLDITALIC, new BaseColor(0, 0, 0));
		Font bf12 = new Font(FontFamily.HELVETICA, 10);

		// creating table and set the column width
		PdfPTable table = new PdfPTable(4);// Create 4 columns in table.
		table.setWidths(new float[] { 3, 3, 3, 3 });
		table.setHeaderRows(2);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);// Space Before table starts, like margin-top in CSS
		table.setSpacingAfter(10f);// Space After table starts, like margin-Bottom in CSS
		table.setHorizontalAlignment(Element.ALIGN_CENTER);

		// add cell of table - header cell row first
		table.addCell(createCell("MASS FLOW METER", bfBold12, 0, "", BaseColor.LIGHT_GRAY));
		table.addCell(createCell("", bfBold12, 0, "", BaseColor.LIGHT_GRAY));
		table.addCell(createCell("", bfBold12, 0, "", BaseColor.LIGHT_GRAY));
		table.addCell(createCell("", bfBold12, 0, "", BaseColor.LIGHT_GRAY));

		// add cell of table - header cell row first
		table.addCell(createCell("", bfBold12, 0, "", BaseColor.LIGHT_GRAY));
		table.addCell(createCell("INITIAL READING", bfBold12, 0, "", BaseColor.LIGHT_GRAY));
		table.addCell(createCell("FINAL READING", bfBold12, 0, "", BaseColor.LIGHT_GRAY));
		table.addCell(createCell("DIFT.MT", bfBold12, 0, "", BaseColor.LIGHT_GRAY));
		
		Map<String, BigDecimal> initialData, finalData;
		if ("First".equals(shiftName)) {
			// Call previous time slot data (initial reading)
			System.out.println("MORNING Mail fucntionality called :");
			initialData = jdbcDataService.getSectionDataForDifference(startDateTime.minusMinutes(30), startDateTime,
					"Section4");
			// Call current time slot data (final reading)
			finalData = jdbcDataService.getSectionDataForDifference(endDateTime.minusMinutes(30), endDateTime,
					"Section3");
			System.out.println("MORNING initialData:"+initialData.size());
			System.out.println("MORNING finalData:"+finalData.size());
		} else {
			// Call previous time slot data (initial reading)
			System.out.println("EVENING Mail fucntionality called :" );
			initialData = jdbcDataService.getSectionDataForDifference(startDateTime.minusMinutes(30), startDateTime,
					"Section3");
			// Call current time slot data (final reading)
			finalData = jdbcDataService.getSectionDataForDifference(endDateTime.minusMinutes(30), endDateTime,
					"Section4");
			System.out.println("EVENING initialData:"+initialData.size());
			System.out.println("EVENING finalData:"+finalData.size());
		}

		if (initialData == null || initialData.isEmpty()) {
			System.out.println("initialData");
			SummaryType.getSummaryTypes()
					.forEach(key -> initialData.put(key, new BigDecimal(StaticData.df.format(Float.valueOf(0)))));
		}
		if (finalData == null || finalData.isEmpty()) {
			System.out.println("finalData");
			SummaryType.getSummaryTypes()
					.forEach(key -> finalData.put(key, new BigDecimal(StaticData.df.format(Float.valueOf(0)))));
		}
		//////////////////////////////////// REFINERY////////////////////////////////////
		finalData.forEach((k,v)->System.out.println("Item : " + k + " Count : " + v));
		document.add(addHeading("REFINERY"));
		addTableBody(table, bfBold12, "MFT REF IN (500TPD)", initialData.get(SummaryType.Tag1Value.getKey()),
				finalData.get(SummaryType.Tag1Value.getKey()), new BaseColor(173, 216, 230));

		addTableBody(table, bfBold12, "MFT REF OUT (500TPD)", initialData.get(SummaryType.Tag2Value.getKey()),
				finalData.get(SummaryType.Tag2Value.getKey()), new BaseColor(135, 206, 235));

		addTableBody(table, bfBold12, "MFT REF IN (1000TPD)", initialData.get(SummaryType.Tag3Value.getKey()),
				finalData.get(SummaryType.Tag3Value.getKey()), new BaseColor(173, 216, 230));

		addTableBody(table, bfBold12, "MFT REF OUT (1000TPD)", initialData.get(SummaryType.Tag4Value.getKey()),
				finalData.get(SummaryType.Tag4Value.getKey()), new BaseColor(135, 206, 235));
		document.add(table);
		table.deleteBodyRows();
		//////////////////////////////////// FRACTIONATION////////////////////////////////////
		document.add(addHeading("FRACTIONATION"));
		addTableBody(table, bfBold12, "FRAC FEED MFT (OLD)", initialData.get(SummaryType.Tag5Value.getKey()),
				finalData.get(SummaryType.Tag5Value.getKey()), new BaseColor(173, 216, 230));

		addTableBody(table, bfBold12, "FRAC FEED MFT (NEW)", initialData.get(SummaryType.Tag6Value.getKey()),
				finalData.get(SummaryType.Tag6Value.getKey()), new BaseColor(135, 206, 235));

		addTableBody(table, bfBold12, "OLEIN TRANSFER MFT (OLD)", initialData.get(SummaryType.Tag7Value.getKey()),
				finalData.get(SummaryType.Tag7Value.getKey()), new BaseColor(173, 216, 230));

		addTableBody(table, bfBold12, "OLEIN TRANSFER MFT (NEW)", initialData.get(SummaryType.Tag8Value.getKey()),
				finalData.get(SummaryType.Tag8Value.getKey()), new BaseColor(135, 206, 235));

		addTableBody(table, bfBold12, "STEARINE TRANSFER MFT", initialData.get(SummaryType.Tag9Value.getKey()),
				finalData.get(SummaryType.Tag9Value.getKey()), new BaseColor(173, 216, 230));
		document.add(table);
		table.deleteBodyRows();
		//////////////////////////////////// TANK-FARM////////////////////////////////////
		document.add(addHeading("TANK FARM"));
		addTableBody(table, bfBold12, "CPOLEIN PIT 1-MFT 19-1", initialData.get(SummaryType.Tag10Value.getKey()),
				finalData.get(SummaryType.Tag10Value.getKey()), new BaseColor(173, 216, 230));

		addTableBody(table, bfBold12, "CPOLEIN PIT 2-MFT 19-2", initialData.get(SummaryType.Tag11Value.getKey()),
				finalData.get(SummaryType.Tag11Value.getKey()), new BaseColor(135, 206, 235));

		addTableBody(table, bfBold12, "MFT BOTTLING-MFT 22-2", initialData.get(SummaryType.Tag12Value.getKey()),
				finalData.get(SummaryType.Tag12Value.getKey()), new BaseColor(173, 216, 230));

		addTableBody(table, bfBold12, "MFT OLEIN 1-MFT 22-1", initialData.get(SummaryType.Tag13Value.getKey()),
				finalData.get(SummaryType.Tag13Value.getKey()), new BaseColor(135, 206, 235));

		addTableBody(table, bfBold12, "MFT OLEIN 1-MFT 22-3", initialData.get(SummaryType.Tag14Value.getKey()),
				finalData.get(SummaryType.Tag14Value.getKey()), new BaseColor(173, 216, 230));

		addTableBody(table, bfBold12, "RBD-MFT 20-1", initialData.get(SummaryType.Tag15Value.getKey()),
				finalData.get(SummaryType.Tag15Value.getKey()), new BaseColor(135, 206, 235));
		document.add(table);
		table.deleteBodyRows();

	}

	private void addTableBody(PdfPTable table, Font font, String tag1Value, BigDecimal initialValue,
			BigDecimal finalValue, BaseColor backgroundColor) {

		table.addCell(createCell(tag1Value, font, 0, "ALIGN_LEFT", backgroundColor));
		table.addCell(createCell(String.valueOf(initialValue), font, 0, "ALIGN_RIGHT", backgroundColor));
		table.addCell(createCell(String.valueOf(finalValue), font, 0, "ALIGN_RIGHT", backgroundColor));
		table.addCell(createCell(String.valueOf(subtraction(initialValue, finalValue)), font, 0, "ALIGN_RIGHT",
				backgroundColor));
	}

	public BigDecimal subtraction(BigDecimal initialValue, BigDecimal finalValue) {
		return finalValue.subtract(initialValue);
	}

	private PdfPTable addHeaderInfo(LocalDateTime startDateTime, LocalDateTime endDateTime) throws DocumentException {
		// FONT NAME FONT SIZE FONT STYLE FONT COLOUR
		Font bfBold12 = new Font(FontFamily.HELVETICA, 11, Font.BOLDITALIC, new BaseColor(0, 0, 0));
		Font bf12 = new Font(FontFamily.HELVETICA, 10);

		// creating table and set the column width
		PdfPTable pdfPTable = new PdfPTable(5);// Create 5 columns in table.
		pdfPTable.setWidths(new float[] { 3, 3, 3, 3, 3 });
		pdfPTable.setHeaderRows(1);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setSpacingBefore(10f);// Space Before table starts, like margin-top in CSS
		pdfPTable.setSpacingAfter(10f);// Space After table starts, like margin-Bottom in CSS
		pdfPTable.setHorizontalAlignment(Element.ALIGN_CENTER);

		// First Row - First Cell
		pdfPTable.addCell(createCell("DATE:", Element.ALIGN_LEFT, 0, bfBold12));
		pdfPTable.addCell(createCell(endDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
				Element.ALIGN_LEFT, 0, bf12));

		// First Row - Second Cell
		pdfPTable.addCell(createCell("", Element.ALIGN_LEFT, 0, bfBold12));
		pdfPTable.addCell(createCell("", Element.ALIGN_LEFT, 0, bf12));

		// First Row - Third Cell
		pdfPTable.addCell(createCell("", Element.ALIGN_LEFT, 0, bfBold12));

		// Second Row - First Cell
		pdfPTable.addCell(createCell("TIME:", Element.ALIGN_LEFT, 0, bfBold12));
		pdfPTable.addCell(createCell(endDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("KK:mm:ss a")),
				Element.ALIGN_LEFT, 0, bf12));

		// Second row - Second Cell
		pdfPTable.addCell(createCell("", Element.ALIGN_LEFT, 0, bfBold12));
		pdfPTable.addCell(createCell("", Element.ALIGN_LEFT, 0, bf12));

		// Second Row - Third Cell
		pdfPTable.addCell(createCell("", Element.ALIGN_LEFT, 0, bf12));

		return pdfPTable;
	}

	public Paragraph addHeading(String heading) {
		Paragraph paragraph = new Paragraph(heading);
		paragraph.setAlignment(Element.ALIGN_CENTER);
		paragraph.setFont(new Font(FontFamily.HELVETICA, 11, Font.BOLDITALIC, BaseColor.BLUE));
		return paragraph;
	}

	private Map<OilType, BigDecimal> addReportTableData(Document document, LocalDateTime startDateTime,
			LocalDateTime endDateTime, String shiftName) throws DocumentException {
		List<DataModel> staticDataList = StaticData.getStaticData();

		List<TagData> sectionOneData = jdbcDataService.getSectionDataForReport(startDateTime, endDateTime, "Section1");
		List<TagData> sectionTwoData = jdbcDataService.getSectionDataForReport(startDateTime, endDateTime, "Section2");

		List<TagData> sortedDataList = new ArrayList<>();

		// sorting data as per query output
		for (int index = 0; index < sectionOneData.size(); index++) {
			if (index > 1) {
				sortedDataList.add(sectionOneData.get(index));
			} else if (index == 0) {
				sortedDataList.add(sectionOneData.get(index));
			}
		}
		sortedDataList.add(sectionOneData.get(1));

		sectionOneData = sortedDataList;

		// adding section 2 data

		for (TagData data : sectionTwoData) {
			sectionOneData.add(data);
		}

		Map<OilType, BigDecimal> initialValues = StaticData.getStaticMapData();

		// FONT NAME FONT SIZE FONT STYLE FONT COLOUR
		Font bfBold12 = new Font(FontFamily.HELVETICA, 11, Font.BOLDITALIC, new BaseColor(0, 0, 0));
		Font bf12 = new Font(FontFamily.HELVETICA, 10);

		// creating table and set the column width
		PdfPTable table = new PdfPTable(5);
		// float[] columnWidths = { 1f, 2f, 1f } here second column will be twice as
		// first and third and then use
		table.setWidths(new float[] { 3, 3, 3, 3, 3 });
		table.setHeaderRows(4);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);// Space Before table starts, like margin-top in CSS
		table.setSpacingAfter(10f);// Space After table starts, like margin-Bottom in CSS
		table.setHorizontalAlignment(Element.ALIGN_CENTER);

		// First Row - First Cell
		table.addCell(createCell("DATE:", Element.ALIGN_LEFT, 0, bfBold12));
		table.addCell(createCell(endDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
				Element.ALIGN_LEFT, 0, bf12));

		// First Row - Second Cell
		table.addCell(createCell("", Element.ALIGN_LEFT, 0, bfBold12));
		table.addCell(createCell("", Element.ALIGN_LEFT, 0, bf12));

		// First Row - Third Cell
		table.addCell(createCell("", Element.ALIGN_LEFT, 0, bfBold12));

		// Second Row - First Cell
		table.addCell(createCell("TIME:", Element.ALIGN_LEFT, 0, bfBold12));
		table.addCell(createCell(endDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("KK:mm:ss a")),
				Element.ALIGN_LEFT, 0, bf12));

		// Second row - Second Cell
		table.addCell(createCell("", Element.ALIGN_LEFT, 0, bfBold12));
		table.addCell(createCell("", Element.ALIGN_LEFT, 0, bf12));

		// Second Row - Third Cell
		table.addCell(createCell("", Element.ALIGN_LEFT, 0, bf12));

		// add cell of table - header cell row first
		table.addCell(createCell("OIL STORAGE TANKS", bfBold12, 0, "", BaseColor.LIGHT_GRAY));
		table.addCell(createCell("CAPACITY(MT)", bfBold12, 0, "", BaseColor.LIGHT_GRAY));
		table.addCell(createCell("WT/MM(KG)", bfBold12, 0, "", BaseColor.LIGHT_GRAY));
		table.addCell(createCell(startDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy KK:mm:ss a")), bfBold12,
				2, "", BaseColor.LIGHT_GRAY));

		// add cell of table - header cell row second
		table.addCell(createCell("", bfBold12, 0, "", BaseColor.LIGHT_GRAY));
		table.addCell(createCell("", bfBold12, 0, "", BaseColor.LIGHT_GRAY));
		table.addCell(createCell("", bfBold12, 0, "", BaseColor.LIGHT_GRAY));
		table.addCell(createCell("WET DIP(MM)", bfBold12, 0, "", BaseColor.LIGHT_GRAY));
		table.addCell(createCell("QTY(MT)", bfBold12, 0, "", BaseColor.LIGHT_GRAY));

		int index = 0;
		Paragraph paragraph;
		String tagValue;
		String storageName;
		// looping the table cell for adding definition

		if (sectionOneData != null && !sectionOneData.isEmpty()) {
//			System.out.println("SectionOneData : " + sectionOneData.size());
//			System.out.println("SectionTwoData : " + sectionTwoData.size());
			for (TagData data : sectionOneData) {

				// first row data
				storageName = staticDataList.get(index).getStorageName();
				addTableData(table, bfBold12, staticDataList.get(index).getStorageName(),
						staticDataList.get(index).getCapacity(), staticDataList.get(index).getWtmm(),
						data.getTag1Value(), data.getTag2Value(), new BaseColor(173, 216, 230));

				initialValues = StaticData.addQTY(initialValues, storageName,
						StaticData.df.format(Float.valueOf(data.getTag2Value())));
				index++;

				// second row data
				if (staticDataList.size() != index) {
					storageName = staticDataList.get(index).getStorageName();
					addTableData(table, bfBold12, staticDataList.get(index).getStorageName(),
							staticDataList.get(index).getCapacity(), staticDataList.get(index).getWtmm(),
							data.getTag3Value(), data.getTag4Value(), new BaseColor(135, 206, 235));

					initialValues = StaticData.addQTY(initialValues, storageName,
							StaticData.df.format(Float.valueOf(data.getTag4Value())));
					index++;
				}

			}

		} else {
			table.addCell(createCell("No data found.",
					new Font(FontFamily.HELVETICA, 12, Font.BOLDITALIC, BaseColor.RED), 5, "", BaseColor.WHITE));
		}
		paragraph = new Paragraph();
		addEmptyLine(paragraph, 1);
		document.add(paragraph);
		document.add(table);
		return initialValues;
	}

	private void addTableData(PdfPTable table, Font font, String storageName, String capacity, String wtmm,
			String tagValue1, String tagValue2, BaseColor backgroundColor) {

		table.addCell(createCell(storageName, font, 0, "ALIGN_LEFT", backgroundColor));
		table.addCell(createCell(capacity, font, 0, "ALIGN_RIGHT", backgroundColor));
		table.addCell(createCell(wtmm, font, 0, "ALIGN_RIGHT", backgroundColor));

		if (!tagValue1.isEmpty()) {
			table.addCell(createCell(StaticData.df.format(Float.valueOf(tagValue1)), font, 0, "ALIGN_RIGHT",
					backgroundColor));
		} else {
			table.addCell(createCell(StaticData.df.format(Float.valueOf(0)), font, 0, "ALIGN_RIGHT", backgroundColor));
		}

		if (!tagValue2.isEmpty()) {
			table.addCell(createCell(StaticData.df.format(Float.valueOf(tagValue2)), font, 0, "ALIGN_RIGHT",
					backgroundColor));
		} else {
			table.addCell(createCell(StaticData.df.format(Float.valueOf(0)), font, 0, "ALIGN_RIGHT", backgroundColor));
		}

	}

	private void addReportTableSummary(Document document, Map<OilType, BigDecimal> calculatedValues)
			throws DocumentException {
		// FONT NAME FONT SIZE FONT STYLE FONT COLOUR
		Font bfBold12 = new Font(FontFamily.HELVETICA, 11, Font.BOLDITALIC, new BaseColor(0, 0, 0));
		Font bf12 = new Font(FontFamily.HELVETICA, 10);

		Paragraph paragraph = new Paragraph("Summary");
		paragraph.setAlignment(Element.ALIGN_CENTER);
		paragraph.setFont(new Font(FontFamily.HELVETICA, 11, Font.BOLDITALIC, BaseColor.BLUE));
		document.add(paragraph);

		// creating table and set the column width
		PdfPTable table = new PdfPTable(3);
		table.setWidths(new float[] { 3, 3, 3 });
		table.setHeaderRows(1);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);// Space Before table starts, like margin-top in CSS
		table.setSpacingAfter(10f);// Space After table starts, like margin-Bottom in CSS
		table.setHorizontalAlignment(Element.ALIGN_CENTER);

		table.addCell(createCell("Oil type", bfBold12, 0, "", BaseColor.LIGHT_GRAY));
		table.addCell(createCell("Total capacity(MT)", bfBold12, 0, "", BaseColor.LIGHT_GRAY));
		table.addCell(createCell("Actual available(MT)", bfBold12, 0, "", BaseColor.LIGHT_GRAY));

		calculatedValues.forEach((key, value) -> {
			table.addCell(createCell(key.getKey(), bfBold12, 0, "ALIGN_LEFT", new BaseColor(173, 216, 230)));
			switch (key.getKey()) {

			case "CP OLEIN":
				table.addCell(createCell("19,128.5", bfBold12, 0, "ALIGN_RIGHT", new BaseColor(173, 216, 230)));
				break;

			case "CPO":
				table.addCell(createCell("3,825.7", bfBold12, 0, "ALIGN_RIGHT", new BaseColor(173, 216, 230)));
				break;

			case "RBDPO":
				table.addCell(createCell("4,843.06", bfBold12, 0, "ALIGN_RIGHT", new BaseColor(173, 216, 230)));
				break;

			case "RBDP OLEIN":
				table.addCell(createCell("4,843.06", bfBold12, 0, "ALIGN_RIGHT", new BaseColor(173, 216, 230)));
				break;

			case "SUPER OLEIN":
				table.addCell(createCell("1,130.4", bfBold12, 0, "ALIGN_RIGHT", new BaseColor(173, 216, 230)));
				break;

			case "OLEIN IV 58":
				table.addCell(createCell("3,165.12", bfBold12, 0, "ALIGN_RIGHT", new BaseColor(173, 216, 230)));
				break;

			case "PMF":
				table.addCell(createCell("6,086.5", bfBold12, 0, "ALIGN_RIGHT", new BaseColor(173, 216, 230)));
				break;

			case "PFAD":
				table.addCell(createCell("2,231.36", bfBold12, 0, "ALIGN_RIGHT", new BaseColor(173, 216, 230)));
				break;

			}

			table.addCell(createCell(StaticData.df.format(Float.valueOf(value.toPlainString())), bfBold12, 0,
					"ALIGN_RIGHT", new BaseColor(173, 216, 230)));
		});

		document.add(table);
	}

	private PdfPCell createCell(String text, Font font, int colspan, String horizontalAlignment,
			BaseColor backgroundColor) {

		PdfPCell cell;
		// create a new cell with the specified Text and Font

		cell = new PdfPCell(new Phrase(text.trim(), font));

		// set the cell alignment
		if (horizontalAlignment.equalsIgnoreCase("ALIGN_LEFT")) {
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		} else if (horizontalAlignment.equalsIgnoreCase("ALIGN_RIGHT")) {
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		} else {
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		}

		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBackgroundColor(backgroundColor);
		// set the cell column span in case you want to merge two or more cells
		if (colspan > 0) {
			cell.setColspan(colspan);
		}

		// in case there is no text and you wan to create an empty row
		if (text.trim().equalsIgnoreCase("")) {
			cell.setMinimumHeight(10f);
		}

		return cell;
	}

	private PdfPCell createCell(String text, int align, int colspan, Font font) {

		// create a new cell with the specified Text and Font
		PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));

		// set the cell alignment
		cell.setHorizontalAlignment(align);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

		// set the cell column span in case you want to merge two or more cells
		// cell.setColspan(colspan);
		// in case there is no text and you wan to create an empty row
		if (text.trim().equalsIgnoreCase("")) {
			cell.setMinimumHeight(10f);
		}

		cell.setBorder(Rectangle.NO_BORDER);

		return cell;

	}

	private Document newDocument() {
		return new Document(PageSize.A4, 36, 36, 90, 36);
	}

	private PdfWriter newPdfWriter(Document document, OutputStream fos) throws DocumentException {
		return PdfWriter.getInstance(document, fos);
	}

	private void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	@Override
	public List<Email> getAllEmailIds() {
		return jdbcDataService.getAllEmailIds();
	}

}
