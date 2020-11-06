package com.rafael.report.utils;

public class DataModel {

	private String storageName;
	private String capacity;
	private String wtmm;

	public DataModel() {
		super();
	}

	public DataModel(String storageName, String capacity, String wtmm) {
		super();
		this.storageName = storageName;
		this.capacity = capacity;
		this.wtmm = wtmm;
	}

	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getWtmm() {
		return wtmm;
	}

	public void setWtmm(String wtmm) {
		this.wtmm = wtmm;
	}

	@Override
	public String toString() {
		return "DataModel [storageName=" + storageName + ", capacity=" + capacity + ", wtmm=" + wtmm + "]";
	}

}
