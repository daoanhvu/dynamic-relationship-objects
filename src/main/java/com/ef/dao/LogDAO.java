package com.ef.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.ef.Common;
import com.ef.model.LogRecord;

public class LogDAO {
	
	private static final String TABLE_REQUEST = "t_request";
	private static final String TABLE_BLOCK = "t_block";
	
	private String dbhost;
	private String dbuser;
	private String dbpass;
	private String dbport;
	private String dbname;
	
	private Connection connection;
	
	
	public LogDAO(Properties props) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = null;
			dbhost = props.getProperty("database.host");
			dbuser = props.getProperty("database.user");
			dbpass = props.getProperty("database.password");
			dbport = props.getProperty("database.port");
			dbname = props.getProperty("database.name");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void openConnection() {
		try {
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("jdbc:mysql://");
			sBuilder.append(dbhost);
			sBuilder.append(":");
			sBuilder.append(dbport);
			sBuilder.append("/");
			sBuilder.append(dbname);
			//String url = "jdbc:mysql://localhost:3306/bkda";
			connection = DriverManager.getConnection(sBuilder.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeConnection() {
		try {
			if(connection != null)
				connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<LogRecord> findRequestByIP(String ip) {
		String sql = "SELECT _startdate, _ip, _method, _httpcode, _clientdes"
				+ " FROM " + TABLE_REQUEST + " WHERE _ip like ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, "%" + ip + "%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean saveLogRecord(LogRecord rd) {
		String sql = "INSERT INTO " + TABLE_REQUEST + "(_startdate, _ip, _method, _httpcode, _clientdes)"
				+ " VALUES(?, ?, ?, ?, ?)";
		boolean result = false;
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, Common.LOG_DATE_FORMATTER.format(rd.getStartDate()));
			ps.setString(2, rd.getIp());
			ps.setString(3, rd.getRequestMethod());
			ps.setInt(4, rd.getHttpCode());
			ps.setString(5, rd.getClientDescription());
			int r = ps.executeUpdate();
			result = (r >= 1 );
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	public boolean saveBlockRequest(Date startDate, String ip, int requestCount) {
		String sql = "INSERT INTO " + TABLE_BLOCK + "(_startdate, _ip, _numrequest)"
				+ " VALUES(?, ?, ?)";
		boolean result = false;
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, Common.LOG_DATE_FORMATTER.format(startDate));
			ps.setString(2, ip);
			ps.setInt(3, requestCount);
			int r = ps.executeUpdate();
			result = (r >= 1 );
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
}
