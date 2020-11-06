package com.rafael.report.schedulars;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rafael.report.dtos.Email;
import com.rafael.report.dtos.Mail;
import com.rafael.report.services.EmailService;
import com.rafael.report.services.EmailTemplate;
import com.rafael.report.services.JdbcDataService;
import com.rafael.report.services.PdfReportService;
import com.rafael.report.utils.StaticData;

@Component
public class EmailSchedular {

	private EmailService emailService;

	private PdfReportService pdfReportService;

	private JdbcDataService jdbcDataService;
	
	public static boolean insertFlag = false;

	@Autowired
	public EmailSchedular(EmailService emailService, PdfReportService pdfReportService,
			JdbcDataService jdbcDataService) {
		this.emailService = emailService;
		this.pdfReportService = pdfReportService;
		this.jdbcDataService = jdbcDataService;
	}

//  "0 0 * * * *" = the top of every hour of every day. 
//	"*/10 * * * * *" = every ten seconds. 
//	"0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day. 
//	"0 0 6,19 * * *" = 6:00 AM and 7:00 PM every day. 
//	"0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day. 
//	"0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays 
//	"0 0 0 25 12 ?" = every Christmas Day at midnight

	@Scheduled(cron = "0 1 0 * * *", zone = "Africa/Luanda")
	public void resetSchedular() {
		jdbcDataService.insertEmailActivityWithFalseValues();
	}
	
	@Scheduled( initialDelay = 3000,fixedRate = 300000) // @Scheduled(fixedRate = 300000)
	public void TimeslotSheduler()   {
		if(!insertFlag) {
		jdbcDataService.insertEmailActivityWithFalseValues();
		insertFlag=true;
		}
	}
	

	// <second> <minute> <hour> <day-of-month> <month> <day-of-week>
	@Scheduled(cron = "0 0/5 7/1 * * *", zone = "Africa/Luanda")
	public void sheduleMorningEmailReport() {
		LocalDateTime currentLocalDateTime = LocalDateTime.now();
//		System.out.println("First Mail fucntionality called :" + currentLocalDateTime);
		if (jdbcDataService.isMailDelivereyStatus("first")) {

			if (currentLocalDateTime.getHour() >= 7 && currentLocalDateTime.getMinute() >= 2) {

				LocalDateTime startLocalDateTime = LocalDateTime.of(LocalDate.now().minusDays(1),
						LocalTime.of(19, 02, 01));
				LocalDateTime endLocalDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(7, 02, 01));

				// create pdf and add this attachment pdf to mail.
				boolean firstMail = createPdfAndSendEmail(startLocalDateTime, endLocalDateTime, StaticData.FIRST,
						getFileName("report"), getFileName("difference"));
//				System.out.println("First Mail fucntionality called :" + firstMail);
				if (firstMail)
					jdbcDataService.updateEmailActivityWithFlagValues("first");
			}
		}
	}

	// <second> <minute> <hour> <day-of-month> <month> <day-of-week>
	@Scheduled(cron = "0 0/5 19/1 * * *", zone = "Africa/Luanda")
	public void sheduleEveningEmailReport() {
		LocalDateTime currentLocalDateTime = LocalDateTime.now();
//		System.out.println("Second Mail functionality called  :" + currentLocalDateTime);
		if (jdbcDataService.isMailDelivereyStatus("second")) {

			if (currentLocalDateTime.getHour() >= 19 && currentLocalDateTime.getMinute() >= 2) {

				LocalDateTime startLocalDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(7, 02, 01));
				LocalDateTime endLocalDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 02, 01));

				// create pdf and add this attachment pdf to mail.
				boolean secondMail = createPdfAndSendEmail(startLocalDateTime, endLocalDateTime, StaticData.SECOND,
						getFileName("report"), getFileName("difference"));
//				System.out.println("Second Mail functionality called  :" + secondMail);
				if (secondMail)
					jdbcDataService.updateEmailActivityWithFlagValues("second");

			}
		}

	}

	public boolean createPdfAndSendEmail(LocalDateTime startDateTime, LocalDateTime endDateTime, String shiftName,
			String fileReport, String fileDifference) {

		List<String> to = new ArrayList<String>();
		List<String> cc = new ArrayList<String>();
		List<File> attachments = new ArrayList<>();
		EmailTemplate template;
		String subject;
		Mail mail;

		pdfReportService.createReportPdfFile(startDateTime, endDateTime, shiftName, fileReport);
		pdfReportService.createDifferencePdfFile(startDateTime, endDateTime, fileDifference, shiftName);
		List<Email> emailIdList = pdfReportService.getAllEmailIds();

		if (emailIdList != null && !emailIdList.isEmpty()) {
			for (Email lst : emailIdList) {
				if (lst.isTo())
					to.add(lst.getEmailId());
				else
					cc.add(lst.getEmailId());
			}

			if ("First".equals(shiftName)) {
				subject = StaticData.SUBJECT_MORNING;
				template = new EmailTemplate(StaticData.TEMPLATE_MORNING);
			} else {
				subject = StaticData.SUBJECT_EVENING;
				template = new EmailTemplate(StaticData.TEMPLATE_EVENING);

//				pdfReportService.createDifferencePdfFile(startDateTime, endDateTime, fileDifference);
//				attachments.add(new File(StaticData.DIR_PATH_NAME + File.separator + fileDifference));
			}

			if (to != null && !to.isEmpty()) {
				Map<String, String> replacements = new HashMap<String, String>();
				replacements.put("user", "Ajay");
				replacements.put("today", endDateTime.format(StaticData.formatter));
				String message = template.getTemplate(replacements);

				if (cc != null && !cc.isEmpty())
					mail = new Mail(StaticData.FROM, String.join(",", to), String.join(",", cc), subject, message);
				else
					mail = new Mail(StaticData.FROM, String.join(",", to), subject, message);

				attachments.add(new File(StaticData.DIR_PATH_NAME + File.separator + fileReport));
				attachments.add(new File(StaticData.DIR_PATH_NAME + File.separator + fileDifference));
				mail.setAttachments(attachments);
				mail.setHtml(true);
				return emailService.sendEmail(mail);
//				return true;
			}

		}

		return false;
	}

	public String getFileName(String pdfFileName) {
		String fileName = pdfFileName + "-" + LocalDateTime.now();
		return fileName.replace(".", "-").replace(":", "-") + ".pdf";
	}

//	new ClassName().traverseDirectory(new File(DIR_PATH_NAME + File.separator + "New folder"));

	public void traverseDirectory(File directory) {
		for (final File file : directory.listFiles()) {
			if (file.isFile()) {
				if (file.getName().endsWith(".pdf"))
					file.delete();
			}

		}
	}
}

//List<Object> attachments = new ArrayList<Object>();
//attachments.add(new ClassPathResource("analogic_icon.PNG"));
//attachments.add(new ClassPathResource("Image_1.png"));
//attachments.add(new ClassPathResource("logo.png"));
//attachments.add(new ClassPathResource("itext.pdf"));

//String[] list = directory.list();
//if (list == null || list.length == 0 || list.length > 0) {
//	System.out.println("Dirctory is empty!");
//} else {
//	System.out.println("Dirctory is not empty!");
//}