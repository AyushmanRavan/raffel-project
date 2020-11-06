package com.rafael.report.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;

import com.rafael.report.dtos.Email;
import com.rafael.report.utils.StaticData;
import com.rafael.report.utils.TagData;

@Component
public class JdbcDataService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${application.database.name}")
	private String database_name;

	public List<TagData> getSectionDataForReport(LocalDateTime startDateTime, LocalDateTime endDateTime,
			String tableName) {
		String sql = "SELECT * FROM " + database_name + ".[dbo].[" + tableName
				+ "] where [Date_Time1] >= ? and [Date_Time1] <= ? order by Date_Time1";
		String query = "SELECT * FROM " + database_name + ".[dbo].[" + tableName + "] where [Date_Time1] >= '"
				+ startDateTime.format(StaticData.formatter) + "' and [Date_Time1] <= '"
				+ endDateTime.format(StaticData.formatter) + "' order by Date_Time1";
//		System.out.println(query);
		Object[] params = new Object[] { startDateTime.format(StaticData.formatter),
				endDateTime.format(StaticData.formatter) };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
		return jdbcTemplate.query(sql, params, types, new MapResultSetToTagDataList());
	}

	public Map<String, BigDecimal> getSectionDataForDifference(LocalDateTime startDateTime, LocalDateTime endDateTime,
			String tableName) {
		String sql = "SELECT * FROM [" + database_name + "].[dbo].[" + tableName
				+ "] where [Date_Time1] >= ? and [Date_Time1] <= ? order by SourceName";
		String query = "SELECT * FROM [" + database_name + "].[dbo].[" + tableName + "] where [Date_Time1] >= '"
				+ startDateTime.format(StaticData.formatter) + "' and [Date_Time1] <= '"
				+ endDateTime.format(StaticData.formatter) + "' order by SourceName";
		System.out.println(query);
		Object[] params = new Object[] { startDateTime.format(StaticData.formatter),
				endDateTime.format(StaticData.formatter) };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
		return jdbcTemplate.query(sql, params, types, new MapResultSetToTagListData());
	}

	public List<Email> getAllEmailIds() {
		String sql = "SELECT * FROM [" + database_name + "].[dbo].[email]";
		return jdbcTemplate.query(sql, new MapResultSetToEmailIdslist());
	}

	public void insertEmailActivityWithFalseValues() {

		final String INSERT_SQL = "insert into [" + database_name
				+ "].[dbo].[email_schedule_activity] (email_schedule_date,first_email,second_email) values(?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_SQL);
				ps.setString(1, LocalDate.now().toString());
				ps.setBoolean(2, false);
				ps.setBoolean(3, false);
				return ps;
			}

		});

	}

	public void updateEmailActivityWithFlagValues(String email_type) {
		final String UPDATE_SQL = getUpdateQuery(email_type);

		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(UPDATE_SQL);
				ps.setBoolean(1, true);
				ps.setString(2, LocalDate.now().toString());
				return ps;
			}

		});

	}

	public String getUpdateQuery(String email_type) {

		if ("first".equalsIgnoreCase(email_type)) {
			return "update [" + database_name
					+ "].[dbo].[email_schedule_activity] set [first_email] = ? where [email_schedule_date] = ?";
		} else {
			return "update [" + database_name
					+ "].[dbo].[email_schedule_activity] set [second_email] = ? where [email_schedule_date] = ?";
		}
	}

	public boolean isMailDelivereyStatus(String email_type) {
		final String STATUS_SQL = getMailStatus(email_type);

		int result = jdbcTemplate.queryForObject(STATUS_SQL, new Object[] { false, LocalDate.now().toString() },
				Integer.class);

		return result > 0;
	}

	public String getMailStatus(String email_type) {

		if ("first".equalsIgnoreCase(email_type)) {
			return "select count(*) from [" + database_name
					+ "].[dbo].[email_schedule_activity] where [first_email] = ? and [email_schedule_date] = ?";
		} else {
			return "select count(*) from [" + database_name
					+ "].[dbo].[email_schedule_activity] where [second_email] = ? and [email_schedule_date] = ?";
		}
	}
}
//public List<TagData> getSectionTwoData(LocalDateTime startDateTime, LocalDateTime endDateTime, String tableName) {
//String sql = "SELECT * FROM [" + database_name + "].[dbo].[" + tableName
//		+ "] where [Date_Time1] >='2020-06-24 19:00:02.3560000' and [Date_Time1]<='2020-06-25 07:00:02.3560000'";
//return jdbcTemplate.query(sql, new MapResultSetToTagDataList());
//}
