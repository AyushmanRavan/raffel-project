package com.rafael.report.dtos;

public class Email {

	private String emailId;
	private boolean to;

	public Email() {
	}

	public Email(String emailId, boolean to) {
		this.emailId = emailId;
		this.to = to;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public boolean isTo() {
		return to;
	}

	public void setTo(boolean to) {
		this.to = to;
	}

}
