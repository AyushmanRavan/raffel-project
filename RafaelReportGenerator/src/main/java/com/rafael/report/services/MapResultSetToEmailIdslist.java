package com.rafael.report.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.rafael.report.dtos.Email;

public class MapResultSetToEmailIdslist implements ResultSetExtractor<List<Email>> {

	@Override
	public List<Email> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Email> emailIdList = new ArrayList<Email>();
		while (rs.next()) {
			emailIdList.add(new Email(rs.getString("email_id"), rs.getBoolean("to")));
		}
		return emailIdList;
	}

}
