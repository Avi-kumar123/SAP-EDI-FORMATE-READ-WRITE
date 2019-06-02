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

public class Message002 extends MessageCommon implements MessageSpecific {

	File file = null;
	BufferedReader br = null;

	String readHeader;
	String readBody;
	String readFooter;
	String dataFileName = "";
	String sFileLine = "";

	ResultSet rsBody = null;
	ResultSet rsBody2B = null;
	ResultSet rsBody3B = null;
	ResultSet rsBody4B = null;

	public Message002(String filePath, String fileName) {
		this.dataFileName = fileName;
		recordCount = 0;
		try {
			file = new File(filePath + "" + fileName);
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			setErrorMessage("Error while reading the file");
		}
	}

	@Override
	public String buildColumnValue(String columnCode, String t, int l, String d,int p) {
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
			case "12-100": // article code may have hyphen so split and use 1st part e.g.
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
			case "12-F01":
				returnValue = formateColumnValue(String.valueOf(recordCount), t, l);
				break;

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return returnValue;

	}

	public void checkFileName() {

		try {
			String filename = FilenameUtils.getName(file.getAbsolutePath());
			String getName = FilenameUtils.removeExtension(filename);
			readHeader = br.readLine();
			System.out.println("FileName :" + getName);
			System.out.println("Header :" + readHeader.substring(0, 9));
			if (getName.equals(readHeader.substring(0, 9))) {

			} else {
				setErrorMessage("file name and header mismatch");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean checkFileHeaderFooter() {
		Boolean returnValue = null;
		String tmp;
		try {
			readHeader = br.readLine();

			while ((tmp = br.readLine()) != null) {
				readFooter = tmp;
			}

			String lastLine = readFooter;
			if (readHeader.substring(0, 9).equals(lastLine.substring(0, 9))) {
				returnValue = true;
			} else {
				setErrorMessage("file header and footer mismatch");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnValue;

	}

	public String readValue() {
		String returnValue = null;
		try {
			br.readLine();
			while ((readBody = br.readLine()) != null) {

				if (readBody.startsWith("01")) {
					System.out.println(readBody);
				} else if (readBody.startsWith("02")) {
					System.out.println(readBody);
				} else if (readBody.startsWith("03")) {
					System.out.println(readBody);

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		Boolean bodyRecCompleted = false;
		String replaceValue  = "";

		try {
			while (sFileLine != null && !bodyRecCompleted) {
				readNextLine();
				sFileLine = sFileLine+"||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||";
				sLineCode = sFileLine.substring(0, 2);
				rsCF.beforeFirst();
				// rsBody variable use for getting line format
				switch (sLineCode) {
				case "01":
					setQuery(Constant.GET_MESSAGE.replace(Constant.REPLACE_MSG_CODE, "2")
							.replace(Constant.REPLACE_MSG_TYPE, "2B"));
					break;
				case "02":
					setQuery(Constant.GET_MESSAGE.replace(Constant.REPLACE_MSG_CODE, "2")
							.replace(Constant.REPLACE_MSG_TYPE, "3B"));
					break;
				case "03":
					setQuery(Constant.GET_MESSAGE.replace(Constant.REPLACE_MSG_CODE, "2")
							.replace(Constant.REPLACE_MSG_TYPE, "4B"));
					break;
				default:
					bodyRecCompleted = true;
					try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (!bodyRecCompleted) {
					
					rsCF = getResultset();

					while (rsCF.next()) {
						//System.out.println("position 10 : "+rsCF.getInt(10)+" length : "+rsCF.getInt(6));
						returnValue += "'" + sFileLine.substring(rsCF.getInt(10) - 1, rsCF.getInt(6)+rsCF.getInt(10) - 1) + "', ";
						returnValue = returnValue.replace("|", " ");
						
					}
					// remove last comma and space

					returnValue = "'"+getCurrentDate()+getCurrentTime()+"',"+returnValue.substring(0, returnValue.length() - 2);
					returnValue = Constant.SP_EMIZA_ARTSANA_REPLENISHMENTDELIVERY.replace(Constant.REPLACE_DATA_VALUE,
							returnValue);
					System.out.println(returnValue);
					setQuery(returnValue);
					returnValue = "";
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnValue;
	}

}
