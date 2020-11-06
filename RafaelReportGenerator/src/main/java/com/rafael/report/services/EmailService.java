package com.rafael.report.services;

import com.rafael.report.dtos.Mail;

public interface EmailService {
	public boolean sendEmail(Mail mail);
}
