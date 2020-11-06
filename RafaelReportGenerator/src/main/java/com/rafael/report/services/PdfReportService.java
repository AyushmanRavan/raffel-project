package com.rafael.report.services;

import java.time.LocalDateTime;
import java.util.List;

import com.rafael.report.dtos.Email;

public interface PdfReportService {
	public void createReportPdfFile(LocalDateTime startDateTime, LocalDateTime endDateTime, String shiftName,
			String fileName);

	public void createDifferencePdfFile(LocalDateTime startDateTime, LocalDateTime endDateTime, String fileName,
			String shiftName);

	public List<Email> getAllEmailIds();
}
