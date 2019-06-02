package com.emiza.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.emiza.constant.Constant;

public class Message008 extends MessageCommon implements MessageSpecific {
		
		String soNumber = "";
		String owner = "ARTS";
		String warehouse = "CHC02";
		
		boolean prcsBody2bFlag = false;
		
		ResultSet rsBody = null;

		public Message008(String orderId) {
			super();
			recordCount = 0;
			this.soNumber = orderId;
			// TODO Auto-generated constructor stub
		}

		// columnCode is "Value" from ARTSANA_MESSAGE_FORMAT table
		// t is type C N D T
		// l is length mostly use for C and N;
		// d is for default value
		// p is for data position
		public String buildColumnValue(String columnCode, String t, int l, String d, int p) {
			String returnValue = null;
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
				case "8-100": // article code may have hyphen so split and use 1st part e.g.
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
				case "8-F01":
					returnValue = formateColumnValue(String.valueOf(recordCount), t.trim(), l);
					break;

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return returnValue;

		}

		public String processBody(ResultSet rsCF, String msgType) {
			
			String returnValue = "";
			
			System.out.println("1" + msgType);

			switch (msgType) {
			case "2B":
				returnValue += processBody_2B(rsCF, msgType);
				//prcsBody2bFlag = true;
				break;
			case "3B":
			 	if(!prcsBody2bFlag)
					returnValue += processBody_3B(rsCF, msgType);
			 	
				break;
			case "4B":
			 	if(!prcsBody2bFlag)
					returnValue += processBody_4B(rsCF, msgType);
				break;
			case "5B":
			 	if(!prcsBody2bFlag)
					returnValue += processBody_5B(rsCF, msgType);
				break;
			}
			
			return returnValue;
		}

		private String processBody_2B(ResultSet rsCF, String msgType) {
			String returnValue = "";

			String queryBody = Constant.MESSAGAE008_PICK_PACK_CANCEL
									.replace("<WAREHOUSE-ID>", warehouse)
									.replace("<OWNER-ID>", owner)
									.replace("<SO-NUMBER>", soNumber);
			setQuery(queryBody);
			
			System.out.println(queryBody);

			rsBody = getResultset();

			try {
				while (rsBody.next()) {
					rsCF.beforeFirst();

					while (rsCF.next()) {

						returnValue += buildColumnValue(rsCF.getString(7).trim(), rsCF.getString(5).trim(), rsCF.getInt(6),
								rsCF.getString(8).trim(),0);
					}
					recordCount++;

					returnValue += "\r\n";
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return returnValue;
		}
		
		private String processBody_3B(ResultSet rsCF, String msgType) {
			String returnValue = "";
			
			

			String queryBody = Constant.MESSAGAE008_PICK_PACK_HEADER
									.replace("<WAREHOUSE-ID>", warehouse)
									.replace("<OWNER-ID>", owner)
									.replace("<SO-NUMBER>", soNumber);
			setQuery(queryBody);
			
			System.out.println(queryBody);

			rsBody = getResultset();

			try {
				while (rsBody.next()) {
					rsCF.beforeFirst();

					while (rsCF.next()) {

						returnValue += buildColumnValue(rsCF.getString(7).trim(), rsCF.getString(5).trim(), rsCF.getInt(6),
								rsCF.getString(8),0);
					}
					recordCount++;

					returnValue += "\r\n";
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return returnValue;
		}
		
		private String processBody_4B(ResultSet rsCF, String msgType) {
			String returnValue = "";

			String queryBody = Constant.MESSAGAE008_PICK_PACK_PACKAGE
									.replace("<WAREHOUSE-ID>", warehouse)
									.replace("<OWNER-ID>", owner)
									.replace("<SO-NUMBER>", soNumber);
			setQuery(queryBody);
			
			System.out.println(queryBody);

			rsBody = getResultset();

			try {
				while (rsBody.next()) {
					rsCF.beforeFirst();

					while (rsCF.next()) {
						returnValue += buildColumnValue(rsCF.getString(7).trim(), rsCF.getString(5).trim(), rsCF.getInt(6),
								rsCF.getString(8),0);
					}
					recordCount++;

					returnValue += "\r\n";
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return returnValue;
		}
		
		private String processBody_5B(ResultSet rsCF, String msgType) {
			String returnValue = "";

			String queryBody = Constant.MESSAGAE008_PICK_PACK_PACKAGE_DETAILS
									.replace("<WAREHOUSE-ID>", warehouse)
									.replace("<OWNER-ID>", owner)
									.replace("<SO-NUMBER>", soNumber);
			setQuery(queryBody);
			
			System.out.println(queryBody);

			rsBody = getResultset();

			try {
				while (rsBody.next()) {
					rsCF.beforeFirst();

					while (rsCF.next()) {
						returnValue += buildColumnValue(rsCF.getString(7).trim(), rsCF.getString(5).trim(), rsCF.getInt(6),
								rsCF.getString(8),0);
					}
					recordCount++;

					returnValue += "\r\n";
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return returnValue;
		}
}
