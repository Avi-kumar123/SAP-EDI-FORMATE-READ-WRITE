package com.emiza.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.FilenameUtils;

import com.emiza.constant.Constant;

public class MessageEngine {

	int msgCode;
	String path;
	String orderId;

	MessageCommon msgCom = null;
	MessageSpecific msgSpec;

	File file = null;
	FileWriter fw = null;
	BufferedWriter bw = null;
	BufferedReader br = null;

	int recordCount = 0;

	Boolean createfile = false;

	String getFilePath = "";
	String getFileName = "";

	public MessageEngine(int messageCode, String filePath, String fileName,String orders) {
		this.msgCode = messageCode;
		msgCom = new MessageCommon();

		this.getFilePath = filePath;
		this.getFileName = fileName;
		this.orderId = orders;
		
		
		// String def_fileName = "\\IN1ERP" + String.format("%03d ", messageCode) + "_";
		// String fileName = "C:\\Users\\HP\\Documents\\Purchase_Order" + def_fileName;
		
		// instantiate appropriate class for message code
		try {
			switch (messageCode) {
			case 2:
				createfile = false;
				msgSpec = new Message002(getFilePath, getFileName);
				break;
			case 3:
				createfile = false;
				msgSpec = new Message003(getFilePath, getFileName);
				break;
			case 4:
				createfile = true;
				getFileName = "IN1ERP004_";
				msgSpec = new Message004(orderId);
				break;
			case 6:
				createfile = true;
				getFileName = "IN1ERP006_";
				msgSpec = new Message006();
				break;
			case 7:
				createfile = false;
				msgSpec = new Message007(getFilePath, getFileName);
				break;
			case 8:
				createfile = true;
				getFileName = "IN1ERP008_";
				msgSpec = new Message008(orderId);
				break;
			case 12:
				createfile = true;
				getFileName = "IN1ERP012_";
				msgSpec = new Message012();
				break;
			case 13:
				createfile = true;
				getFileName = "IN1ERP013_";
				msgSpec = new Message013();
				break;
			}
		} catch (Exception ex) {

		}
		if (createfile) {
			file = new File(filePath + getFileName + msgCom.getCurrentDate() + "" + msgCom.getCurrentTime() + ".txt");
			if (file.exists()) {
				file.delete();
			}

			if (!file.exists()) {
				try {
					file.createNewFile();
					fw = new FileWriter(file, true);
					bw = new BufferedWriter(fw);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		recordCount = 0;
	}

	public boolean startProcessing() {
		boolean returnValue = true;

		msgCom.setQuery(Constant.GET_MESSAGE_TYPE.replace(Constant.REPLACE_MSG_CODE, String.valueOf(msgCode)));

		ResultSet rsSection = msgCom.getResultset();

		// process all section Header Body Footer
		try {
			while (rsSection.next() && !msgCom.errorGenerated && !msgSpec.errorGenerated) {

				String msgType = rsSection.getString(1).substring(1, 2);

				String processResult = "";

				switch (msgType) {
				case "H":
					processResult = processHeader(rsSection.getString(1));
					saveToFile(processResult, false);
					break;
				case "B":

					msgSpec.setQuery(Constant.GET_MESSAGE.replace(Constant.REPLACE_MSG_CODE, String.valueOf(msgCode))
							.replace(Constant.REPLACE_MSG_TYPE, rsSection.getString(1)));
					
					ResultSet rsBody = msgSpec.getResultset();
					saveToFile(msgSpec.processBody(rsBody, rsSection.getString(1)), false);
					break;
				case "F":
					saveToFile(processFooter(rsSection.getString(1)), false);
					break;
				}
			}
			saveToFile("", true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			msgCom.setErrorMessage(e.getMessage());
			returnValue = false;
		}

		return returnValue;
	}

	public String processHeader(String msgType) {
		String returnValue = "";
		msgSpec.setQuery(Constant.GET_MESSAGE.replace(Constant.REPLACE_MSG_CODE, String.valueOf(msgCode))
				.replace(Constant.REPLACE_MSG_TYPE, msgType));


		ResultSet rsHeader = msgSpec.getResultset();

		try {
			if (!createfile) {
				msgSpec.readNextLine();

			}
			
			while (rsHeader.next()) {
				if (createfile) {
					// this is for creating data file
					returnValue += msgSpec.buildColumnValue(rsHeader.getString(7), rsHeader.getString(5),
							rsHeader.getInt(6), rsHeader.getString(8), 0);
				} else {
					// this is for reading data file
					
					returnValue += msgSpec.buildColumnValue(rsHeader.getString(7), rsHeader.getString(5),
							rsHeader.getInt(6), rsHeader.getString(8), rsHeader.getInt(10));
					
				}

			}
			// check for file name correctness here
			System.out.println("getFileName:"+getFileName);
			System.out.println("returnValue:"+returnValue);
			System.out.println("Count :"+msgSpec.getRecordLineCount());
			
			returnValue += "\r\n";

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnValue;
	}

	public String processFooter(String msgType) {
		String returnValue = "";

		msgSpec.setQuery(Constant.GET_MESSAGE.replace(Constant.REPLACE_MSG_CODE, String.valueOf(msgCode))
				.replace(Constant.REPLACE_MSG_TYPE, msgType));
		ResultSet rsFooter = msgSpec.getResultset();
		try {
			if (createfile) {
			while (rsFooter.next()) {
				
				returnValue += msgSpec.buildColumnValue(rsFooter.getString(7), rsFooter.getString(5),
						rsFooter.getInt(6), rsFooter.getString(8), 0);
				
			}
			}else {
				int count = msgSpec.getRecordCount();
				System.out.println(count-2);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnValue;
	}

	private void saveToFile(String sf, boolean bClose) {

		if (createfile) {
			try {

				if (bClose) {
					bw.close();
					fw.close();
				} else {
					bw.write(sf);
					bw.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
