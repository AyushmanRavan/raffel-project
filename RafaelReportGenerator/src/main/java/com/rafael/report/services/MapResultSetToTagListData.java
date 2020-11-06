package com.rafael.report.services;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.rafael.report.utils.StaticData;
import com.rafael.report.utils.SummaryType;

public class MapResultSetToTagListData implements ResultSetExtractor<Map<String, BigDecimal>> {

	private static BigDecimal notEmpty(String str) {
		if (str != null && !str.trim().isEmpty()) {
			return new BigDecimal(StaticData.df.format(Float.valueOf(str)));
		} else {
			return new BigDecimal(StaticData.df.format(Float.valueOf(0)));
		}
	}

	@Override
	public Map<String, BigDecimal> extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<String, BigDecimal> dataMap = new HashMap<>();
		while (rs.next()) {
			switch (rs.getRow()) {
			case 1:
				dataMap.put(SummaryType.Tag1Value.getKey(), notEmpty(rs.getString("Tag1Value")));
				dataMap.put(SummaryType.Tag2Value.getKey(), notEmpty(rs.getString("Tag2Value")));
				dataMap.put(SummaryType.Tag3Value.getKey(), notEmpty(rs.getString("Tag3Value")));
				dataMap.put(SummaryType.Tag4Value.getKey(), notEmpty(rs.getString("Tag4Value")));
				break;
			case 2:
				dataMap.put(SummaryType.Tag5Value.getKey(), notEmpty(rs.getString("Tag1Value")));
				dataMap.put(SummaryType.Tag6Value.getKey(), notEmpty(rs.getString("Tag2Value")));
				dataMap.put(SummaryType.Tag7Value.getKey(), notEmpty(rs.getString("Tag3Value")));
				dataMap.put(SummaryType.Tag8Value.getKey(), notEmpty(rs.getString("Tag4Value")));
				break;
			case 3:
				dataMap.put(SummaryType.Tag9Value.getKey(), notEmpty(rs.getString("Tag1Value")));
				dataMap.put(SummaryType.Tag10Value.getKey(), notEmpty(rs.getString("Tag2Value")));
				dataMap.put(SummaryType.Tag11Value.getKey(), notEmpty(rs.getString("Tag3Value")));
				dataMap.put(SummaryType.Tag12Value.getKey(), notEmpty(rs.getString("Tag4Value")));
				break;
			case 4:
				dataMap.put(SummaryType.Tag13Value.getKey(), notEmpty(rs.getString("Tag1Value")));
				dataMap.put(SummaryType.Tag14Value.getKey(), notEmpty(rs.getString("Tag2Value")));
				dataMap.put(SummaryType.Tag15Value.getKey(), notEmpty(rs.getString("Tag3Value")));
				break;
			}

		}

		return dataMap;
	}

}
