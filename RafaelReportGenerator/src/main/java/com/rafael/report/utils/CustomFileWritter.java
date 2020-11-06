package com.rafael.report.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

public class CustomFileWritter {
	
	

	public static void write(String functionName, String msg) {
		try {
			String currentDateTime = StaticData.formatter.format(LocalDateTime.now());
			File file = new File(StaticData.DIR_PATH_NAME + File.separator + StaticData.LOG_FILE);

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			PrintWriter printWriter = new PrintWriter(bufferedWriter);

			printWriter.println("");
			printWriter.println(currentDateTime);
			printWriter.println(functionName);
			printWriter.println(msg);

			printWriter.flush();
			printWriter.close();
		} catch (Exception ex) {
			System.out.println(stack2string(ex));
		}
	}

	private static String stack2string(Exception ex) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			return "------\r\n" + sw.toString() + "------\r\n";
		} catch (Exception e2) {
			return "bad stack2string";
		}
	}
}
