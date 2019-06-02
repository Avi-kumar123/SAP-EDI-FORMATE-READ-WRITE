package com.emiza.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.FilenameUtils;

import com.emiza.constant.Constant;

public class Message003 extends MessageCommon implements MessageSpecific {

	File file = null;
	BufferedReader br = null;

	String dataFileName = "";
	String dataFilePath = "";
	String sFileLine = "";

	ResultSet rsBody = null;
	ResultSet rsBody2B = null;
	ResultSet rsBody3B = null;
	ResultSet rsBody4B = null;

	Boolean bodyRecCompleted = false;
	int fileLines = 0;
	
	int count = 0;

	public Message003(String filePath, String fileName) {
		this.dataFileName = fileName;
		this.dataFilePath = filePath;
		recordCount = 0;
		recordLineCount = 0;
		countTotalLines();
		try {
			file = new File(filePath + "" + fileName);
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			setErrorMessage("Error while reading the file");
		}
		
	}

	@Override
	public String buildColumnValue(String columnCode, String t, int l, String d, int p) {
		String returnValue = null;
		columnCode = columnCode.trim();
		t = t.trim();

		int columnNo = 0;
		try {
			switch (columnCode) {

			case "R": {
				columnNo = Integer.parseInt(d);
				switch (t) {
				case "C":
					returnValue = rsBody.getString(columnNo);
					if (null == returnValue) {
						returnValue = " ";
					}
					returnValue = formateColumnValue(returnValue, t, l);
					break;
				case "N":
					returnValue = String.valueOf(rsBody.getInt(columnNo));
					if (null == returnValue) {
						returnValue = "0";
					}
					returnValue = formateColumnValue(returnValue, t, l);
					break;
				}
			}

				break;
			case "D":
				returnValue = d;
				break;
			case "CD":
				returnValue = getCurrentDate();
				break;
			case "CT":
				returnValue = getCurrentTime();
				break;
			case "7-100": // article code may have hyphen so split and use 1st part e.g.
							// 09003448000000-056018
				columnNo = Integer.parseInt(d);
				returnValue = rsBody.getString(columnNo);
				if (null == returnValue) {
					returnValue = " ";
				} else {
					returnValue = returnValue.split("-")[0];
				}
				returnValue = formateColumnValue(returnValue, t, l);
				break;
			case "7-F01":
				returnValue = formateColumnValue(String.valueOf(recordCount), t, l);
				break;

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return returnValue;

	}

	public void readNextLine() {
		
		try {
			sFileLine = br.readLine();
			recordCount++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			setErrorMessage(e.getMessage());
		}
	}

	public String processBody(ResultSet rsCF, String msgType) {
		return processBody_2B(rsCF, msgType);

	}

	private String processBody_2B(ResultSet rsCF, String msgType) {
		String returnValue = "";
		String sLineCode = "";

		String replaceValue = "";
		int totalBodyCount = fileLines-2;

		try {
			
			while (sFileLine != null && !bodyRecCompleted && totalBodyCount>0) {
				readNextLine();
				if (sFileLine != null) {
					sFileLine = sFileLine
							+ "||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||";
				} else {
					bodyRecCompleted = true;
					
				}

				// sLineCode = sFileLine.substring(0, 2);
				rsCF.beforeFirst();
				// rsBody variable use for getting line format
				/*
				 * switch (sLineCode.trim()) { case "0":
				 */
				setQuery(Constant.GET_MESSAGE.replace(Constant.REPLACE_MSG_CODE, "3").replace(Constant.REPLACE_MSG_TYPE,
						"2B"));
				/*
				 * break; default: bodyRecCompleted = true; break; }
				 */

				if (bodyRecCompleted) {
					try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}

				if (!bodyRecCompleted) {

					rsCF = getResultset();

					while (rsCF.next()) {
						
						 returnValue += "'" + sFileLine.substring(rsCF.getInt(10) - 1, rsCF.getInt(6)+rsCF.getInt(10) - 1) + "', ";
							returnValue = returnValue.replace("|", " ");
						
					}
					// remove last comma and space
					
					returnValue = "'" + getCurrentDate() + getCurrentTime() + "',"
							+ returnValue.substring(0, returnValue.length() - 2);
					
					returnValue = Constant.SP_EMIZA_ARTSANA_MATERIAL_MASTER.replace(Constant.REPLACE_DATA_VALUE, returnValue);
					System.out.println(returnValue);
					 setQuery(returnValue);
					returnValue = "";
				}
				totalBodyCount--;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnValue;
	}
	
	public int countTotalLines() {
		String value = "";
		file = new File(dataFilePath + "" + dataFileName);
		try {
			BufferedReader brFile = new BufferedReader(new FileReader(file));
			while((value = brFile.readLine()) != null) {
				recordLineCount ++;
				fileLines ++ ;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recordLineCount;

	}

}
