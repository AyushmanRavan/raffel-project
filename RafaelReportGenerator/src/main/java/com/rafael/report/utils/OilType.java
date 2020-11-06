package com.rafael.report.utils;

public enum OilType {

	CP_OLEIN("CP OLEIN"), 
	CPO("CPO"), 
	
	RBDPO("RBDPO"), 
	RBDP_OLEIN("RBDP OLEIN"), 
	
	SUPER_OLEIN("SUPER OLEIN"), 
	OLEIN_IV_58("OLEIN IV 58"), 
	
	PMF("PMF"),
	PFAD("PFAD");

	private String key;

	OilType(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
