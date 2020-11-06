package com.rafael.report.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticData {
	public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final String DIR_PATH_NAME = "G:/reports";
	public static final String LOG_FILE = "log-files/msg_log.txt";
	public static final String FIRST = "First";
	public static final String SECOND = "Second";

	public static final String SUBJECT_MORNING = "Daily Report of Refinery and Fractionation.";
	public static final String TEMPLATE_MORNING = "MORNING.html";

	public static final String SUBJECT_EVENING = "Closing Report of Refinery and Fractionation.";
	public static final String TEMPLATE_EVENING = "EVENING.html";

	public static final String FROM = "raffles.tankfarm@gmail.com";
	
	public static final DecimalFormat df = new DecimalFormat("0.00");

	public static Map<OilType, BigDecimal> getStaticMapData() {
		Map<OilType, BigDecimal> bigDecimalMap = new HashMap<OilType, BigDecimal>();

		bigDecimalMap.put(OilType.CP_OLEIN, new BigDecimal("0.00"));
		bigDecimalMap.put(OilType.CPO, new BigDecimal("0.00"));

		bigDecimalMap.put(OilType.RBDPO, new BigDecimal("0.00"));
		bigDecimalMap.put(OilType.RBDP_OLEIN, new BigDecimal("0.00"));

		bigDecimalMap.put(OilType.SUPER_OLEIN, new BigDecimal("0.00"));
		bigDecimalMap.put(OilType.OLEIN_IV_58, new BigDecimal("0.00"));

		bigDecimalMap.put(OilType.PMF, new BigDecimal("0.00"));
		bigDecimalMap.put(OilType.PFAD, new BigDecimal("0.00"));

		return bigDecimalMap;
	}

	public static List<DataModel> getStaticData() {

		return Arrays.asList(new DataModel("19 A - CP OLEIN", "3825.70", "255.05"),
				new DataModel("19 B - PMF", "3825.70", "255.05"), 
				new DataModel("19 C - CP OLEIN", "3825.70", "255.05"),
				new DataModel("19 D - CPO", "3825.70", "255.05"),

				new DataModel("19 E - CP OLEIN", "3825.70", "255.05"),
				new DataModel("19 F - RBDPO", "3825.70", "255.05"),
				new DataModel("19 G - RBDP OLEIN", "3825.70", "255.05"),
				new DataModel("19 H - CP OLEIN", "3825.70", "255.05"),

				new DataModel("19 I - CP OLEIN", "3825.70", "255.05"),

				new DataModel("20 A - RBDPO", "1017.36", "101.74"),
				new DataModel("20 B - RBDP OLEIN", "1017.36", "101.74"),
				new DataModel("20 C - OLEIN IV 58", "1017.36", "101.74"),
				new DataModel("20 D - OLEIN IV 58", "1017.36", "101.74"),

				new DataModel("22 A - SUPER OLEIN", "565.20", "45.22"),
				new DataModel("22 B - SUPER OLEIN", "565.20", "45.22"),
				new DataModel("22 C - OLEIN IV 58", "565.20", "45.22"),
				new DataModel("22 D - OLEIN IV 58", "565.20", "45.22"),

				new DataModel("25 A - PFAD", "557.84", "21.13"), 
				new DataModel("25 B - PFAD", "557.84", "21.13"),
				new DataModel("25 C - PFAD", "557.84", "21.13"), 
				new DataModel("25 D - PFAD", "557.84", "21.13"),

				new DataModel("26 A - PMF", "565.2", "45.22"), 
				new DataModel("26 B - PMF", "565.2", "45.22"),
				new DataModel("26 C - PMF", "565.2", "45.22"), 
				new DataModel("26 D - PMF", "565.2", "45.22"));
	}

	public static Map<OilType, BigDecimal> addQTY(Map<OilType, BigDecimal> initialValues, String storageName,
			String value) {
		if (Arrays.asList("19 A - CP OLEIN", "19 C - CP OLEIN", "19 E - CP OLEIN", "19 H - CP OLEIN", "19 I - CP OLEIN")
				.contains(storageName)) {
			initialValues.put(OilType.CP_OLEIN, initialValues.get(OilType.CP_OLEIN).add(new BigDecimal(value)));
		}
		if (Arrays.asList("19 D - CPO").contains(storageName)) {
			initialValues.put(OilType.CPO, initialValues.get(OilType.CPO).add(new BigDecimal(value)));
		}
		if (Arrays.asList("19 F - RBDPO", "20 A - RBDPO").contains(storageName)) {
			initialValues.put(OilType.RBDPO, initialValues.get(OilType.RBDPO).add(new BigDecimal(value)));
		}
		if (Arrays.asList("19 G - RBDP OLEIN", "20 B - RBDP OLEIN").contains(storageName)) {
			initialValues.put(OilType.RBDP_OLEIN, initialValues.get(OilType.RBDP_OLEIN).add(new BigDecimal(value)));
		}
		if (Arrays.asList("22 A - SUPER OLEIN", "22 B - SUPER OLEIN").contains(storageName)) {
			initialValues.put(OilType.SUPER_OLEIN, initialValues.get(OilType.SUPER_OLEIN).add(new BigDecimal(value)));
		}
		if (Arrays.asList("20 C - OLEIN IV 58", "20 D - OLEIN IV 58", "22 C - OLEIN IV 58", "22 D - OLEIN IV 58")
				.contains(storageName)) {
			initialValues.put(OilType.OLEIN_IV_58, initialValues.get(OilType.OLEIN_IV_58).add(new BigDecimal(value)));
		}
		if (Arrays.asList("19 B - PMF", "26 A - PMF", "26 B - PMF", "26 C - PMF", "26 D - PMF").contains(storageName)) {
			initialValues.put(OilType.PMF, initialValues.get(OilType.PMF).add(new BigDecimal(value)));
		}
		if (Arrays.asList("25 A - PFAD", "25 B - PFAD", "25 C - PFAD", "25 D - PFAD").contains(storageName)) {
			initialValues.put(OilType.PFAD, initialValues.get(OilType.PFAD).add(new BigDecimal(value)));
		}
		return initialValues;
	}

}
