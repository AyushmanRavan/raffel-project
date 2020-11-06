package com.rafael.report.utils;

import java.util.ArrayList;
import java.util.List;

public enum SummaryType {
	Tag1Value("Tag1Value"), Tag2Value("Tag2Value"), Tag3Value("Tag3Value"), Tag4Value("Tag4Value"),

	Tag5Value("Tag5Value"), Tag6Value("Tag6Value"), Tag7Value("Tag7Value"), Tag8Value("Tag8Value"),
	Tag9Value("Tag9Value"),

	Tag10Value("Tag10Value"), Tag11Value("Tag11Value"), Tag12Value("Tag12Value"), Tag13Value("Tag13Value"),
	Tag14Value("Tag14Value"), Tag15Value("Tag15Value");

	private String key;

	SummaryType(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public static List<String> getSummaryTypes() {
		List<String> summaryType = new ArrayList<String>();

		summaryType.add(Tag1Value.getKey());
		summaryType.add(Tag2Value.getKey());
		summaryType.add(Tag3Value.getKey());
		summaryType.add(Tag4Value.getKey());
		summaryType.add(Tag5Value.getKey());
		summaryType.add(Tag6Value.getKey());

		summaryType.add(Tag7Value.getKey());
		summaryType.add(Tag8Value.getKey());
		summaryType.add(Tag9Value.getKey());
		summaryType.add(Tag10Value.getKey());
		summaryType.add(Tag11Value.getKey());
		summaryType.add(Tag12Value.getKey());

		summaryType.add(Tag13Value.getKey());
		summaryType.add(Tag14Value.getKey());
		summaryType.add(Tag15Value.getKey());

		return summaryType;
	}
}
