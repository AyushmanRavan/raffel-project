package com.rafael.report.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.rafael.report.utils.TagData;

public class MapResultSetToTagDataList implements ResultSetExtractor<List<TagData>> {

	@Override
	public List<TagData> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<TagData> list = new ArrayList<TagData>();
		while (rs.next()) {
//			list.add(new TagData(rs.getRow(), rs.getString("Tag1Value"), rs.getString("Tag2Value"),
//					rs.getString("Tag3Value"), rs.getString("Tag4Value")));
			list.add(new TagData(rs.getString("Tag1Value"), rs.getString("Tag2Value"), rs.getString("Tag3Value"),
					rs.getString("Tag4Value")));
		}
		return list;
	}

}
