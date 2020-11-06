package com.rafael.report.dtos;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rafael.report.services.AppUtil;

public class Mail {
	private String from;
	private List<String> to;
	private List<String> cc;
	private List<String> bcc;
	private String subject;
	private String message;
	private List<File> attachments;
//	 private List <Object> attachments;
	private boolean isHtml;

	public Mail() {
		this.to = new ArrayList<String>();
		this.cc = new ArrayList<String>();
		this.bcc = new ArrayList<String>();
	}

	public Mail(String from, String toList, String subject, String message) {
		this();
		this.from = from;
		this.subject = subject;
		this.message = message;
		this.to.addAll(Arrays.asList(splitByComma(toList)));
	}

	public Mail(String from, String toList, String ccList, String subject, String message) {
		this();
		this.from = from;
		this.subject = subject;
		this.message = message;
		this.to.addAll(Arrays.asList(splitByComma(toList)));
		this.cc.addAll(Arrays.asList(splitByComma(ccList)));
	}

	public Mail(String from, String toList, String ccList, String bccList, String subject, String message) {
		this();
		this.from = from;
		this.subject = subject;
		this.message = message;
		this.to.addAll(Arrays.asList(splitByComma(toList)));
		this.cc.addAll(Arrays.asList(splitByComma(ccList)));
		this.bcc.addAll(Arrays.asList(splitByComma(bccList)));
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public List<String> getCc() {
		return cc;
	}

	public void setCc(List<String> cc) {
		this.cc = cc;
	}

	public List<String> getBcc() {
		return bcc;
	}

	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isHtml() {
		return isHtml;
	}

	public void setHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}

	public List<File> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<File> attachments) {
		this.attachments = attachments;
	}

	private String[] splitByComma(String toMultiple) {
		return toMultiple.split(",");
	}

	public String getToAsList() {
		return AppUtil.concatenate(this.to, ",");
	}

	@Override
	public String toString() {
		return "Mail [from=" + from + ", to=" + to + ", cc=" + cc + ", bcc=" + bcc + ", subject=" + subject
				+ ", message=" + message + ", attachments=" + attachments + ", isHtml=" + isHtml + "]";
	}

}
