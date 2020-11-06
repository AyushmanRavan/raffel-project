package com.rafael.report.services;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.rafael.report.dtos.Mail;
import com.rafael.report.utils.CustomFileWritter;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	public JavaMailSender javaMailSender;

	@Override
	public boolean sendEmail(Mail mail) {
		boolean status = sendHtmlMail(mail);
		CustomFileWritter.write("sendEmail", "sendEmail function called and received status is: " + status);
		return status;
	}

	private boolean sendHtmlMail(Mail mail) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			helper.setFrom(mail.getFrom());
			helper.setTo(mail.getTo().toArray(new String[mail.getTo().size()]));
			helper.setSubject(mail.getSubject());
			helper.setText(mail.getMessage(), mail.isHtml());

			if (mail.getCc().size() > 0) {
				helper.setCc(mail.getCc().toArray(new String[mail.getCc().size()]));
			}
			if (mail.getBcc().size() > 0) {
				helper.setBcc(mail.getBcc().toArray(new String[mail.getBcc().size()]));
			}

//			 for (Object attachment: mail.getAttachments()) {
//	             File file = ((ClassPathResource) attachment).getFile();
//	             mimeMessageHelper.addAttachment(file.getName(), file);
//	         }

			mail.getAttachments().forEach(file -> {
				try {
					FileSystemResource resource = new FileSystemResource(file);
					helper.addAttachment(file.getName(), resource);
				} catch (MessagingException ex) {
					throw new RuntimeException("Problem attaching file to email", ex);

				}
			});
			if (mail.getAttachments() != null && !mail.getAttachments().isEmpty()) {
				javaMailSender.send(mimeMessage);
				CustomFileWritter.write("sendHtmlMail",
						"sendHtmlMail function called successfully with status is: true");
				return true;
			} else {
				CustomFileWritter.write("sendHtmlMail",
						"sendHtmlMail function failed because file not found with status is: false");
				return false;
			}
		} catch (MessagingException ex) {
			CustomFileWritter.write("sendHtmlMail",
					"sendHtmlMail function failed because mail preparing with status is: false  s"+ex.getLocalizedMessage());
			return false;
		}
	}

	// FileSystemResource file = new FileSystemResource(new File(attachment));
	// helper.addAttachment(file.getName(), file);

	// ClassPathResource classPathResource = new
	// ClassPathResource("Attachment.pdf");
	// helper.addAttachment(classPathResource.getFilename(), classPathResource);

	// helper.addAttachment("attachment-document-name.jpg", new
	// ClassPathResource("memorynotfound-logo.jpg"));
	// String inlineImage = "<img src=\"cid:logo.png\"></img><br/>";

//		System.out.println(mail);

}
