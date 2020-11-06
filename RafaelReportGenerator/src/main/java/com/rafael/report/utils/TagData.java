package com.rafael.report.utils;

public class TagData {
	private String tag1Value, tag2Value, tag3Value, tag4Value;
//	private int index;

	public TagData() {
		super();
	}

//	public TagData(int index, String tag1Value, String tag2Value, String tag3Value, String tag4Value) {
//		super();
//		this.index = index;
//		this.tag1Value = tag1Value;
//		this.tag2Value = tag2Value;
//		this.tag3Value = tag3Value;
//		this.tag4Value = tag4Value;
//	}

	public TagData(String tag1Value, String tag2Value, String tag3Value, String tag4Value) {
		super();
		this.tag1Value = tag1Value;
		this.tag2Value = tag2Value;
		this.tag3Value = tag3Value;
		this.tag4Value = tag4Value;
	}

	public String getTag1Value() {
		return tag1Value;
	}

	public void setTag1Value(String tag1Value) {
		this.tag1Value = tag1Value;
	}

	public String getTag2Value() {
		return tag2Value;
	}

	public void setTag2Value(String tag2Value) {
		this.tag2Value = tag2Value;
	}

	public String getTag3Value() {
		return tag3Value;
	}

	public void setTag3Value(String tag3Value) {
		this.tag3Value = tag3Value;
	}

	public String getTag4Value() {
		return tag4Value;
	}

	public void setTag4Value(String tag4Value) {
		this.tag4Value = tag4Value;
	}

//	public int getIndex() {
//		return index;
//	}
//
//	public void setIndex(int index) {
//		this.index = index;
//	}

}
