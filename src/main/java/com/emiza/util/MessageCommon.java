package com.emiza.util;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.ini4j.Ini;

import com.emiza.constant.Constant;

public class MessageCommon {

	Util util = new Util();

	Connection conn = null;
	public ResultSet rs;

	String queryString = null;

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Constant.DATE_FORMATE);
	LocalDateTime now = LocalDateTime.now();

	int recordCount = 0;
	int recordLineCount = 0;

	public Boolean errorGenerated = false;
	private String sErrorMessage = "";

	public String getErrorMessage() {
		return sErrorMessage;

	}

	public void setErrorMessage(String setErrorMessage) {
		errorGenerated = true;
		this.sErrorMessage = setErrorMessage;

	}

	public void setQuery(String query) {
		this.queryString = query;
		prepareRecordSet();
	}

	public void prepareRecordSet() {

		try {
			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());

			/* Getting configuration file instance. */
			Ini ini = util.getIni();
			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());

			/* Creating connection url string. */
			String connection = Constant.JDBCSQL + ini.fetch(Constant.DATABASE, Constant.SERVER)
					+ ini.fetch(Constant.DATABASE, Constant.PORT) + Constant.SDATABASE
					+ ini.fetch(Constant.DATABASE, Constant.DATABASE) + Constant.SUSER
					+ ini.fetch(Constant.USER, Constant.NAME) + Constant.SPASSWORD
					+ ini.fetch(Constant.USER, Constant.PASSWORD);

			/* Connection to database. */
			conn = DriverManager.getConnection(connection);
			if (conn != null) {

				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = stmt.executeQuery(queryString);

			}

		} catch (Exception ex) {
			// log an exception. for example:
		//	ex.printStackTrace();
		}

	}

	public Object getColumnValue(int value) {
		Object dataValue = null;
		try {
			rs.first();
			dataValue = rs.getObject(value);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataValue;
	}

	// value is column value;
	// t is type C N D T;
	// l is length mostly use for C and N;
	public String formateColumnValue(String value, String t, int l) {
		String returnValue = null;
		//System.out.println("VAL = " + value);
		switch (t) {

		case "C":
			returnValue = String.format("%-" + l + "s", value);
			break;
		case "N":
			returnValue = String.format("%0" + l + "d", Integer.parseInt(value));
			break;
		case "RD":
			returnValue = value.substring(0, 4) + // year
					value.substring(5, 7) + // month
					value.substring(8, 10); // date
			break;
			
		case "D":
			returnValue = value.substring(0, 4) + // year
					value.substring(5, 7) + // month
					value.substring(8, 10); // date
			break;
		case "RT":
			returnValue = value.substring(11, 13) + // hours
					value.substring(14, 16) + // minuts
					value.substring(17, 19); // seconds
		}

		return returnValue;

	}

	public ResultSet getResultset() {

		return rs;

	}

	public String getCurrentDate() {
		String returnValue = null;

		returnValue = now.toString().substring(0, 4) + // year
				now.toString().substring(5, 7) + // month
				now.toString().substring(8, 10); // date

		return returnValue;
	}

	public String getCurrentTime() {
		String returnValue = null;

		returnValue = now.toString().substring(11, 13) + // hours
				now.toString().substring(14, 16) + // minuts
				now.toString().substring(17, 19); // seconds

		return returnValue;
	}

	public void moveToNext() {
		try {
			rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getRecordCount() {

		return recordCount;

	}
	
	public int getRecordLineCount() {

		return recordLineCount;

	}

	public void readNextLine() {

		// overriding in extended class
	}

}
