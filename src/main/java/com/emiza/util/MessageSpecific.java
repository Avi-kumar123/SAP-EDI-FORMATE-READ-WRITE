package com.emiza.util;

import java.sql.ResultSet;

public interface MessageSpecific {

	public ResultSet rs = null;
	public static Boolean errorGenerated = false;
	
	public String buildColumnValue(String s, String t, int l, String d,int p);
	public void setQuery(String sQ);
	public ResultSet getResultset();
	public void moveToNext();
	public String processBody(ResultSet rsCF, String msgType);
	public int getRecordCount();
	public String getErrorMessage();
	public void setErrorMessage(String  sErrorMessage);
	public void readNextLine();
	public int getRecordLineCount();
	

}
