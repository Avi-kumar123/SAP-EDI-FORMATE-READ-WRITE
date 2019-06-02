package com.emiza.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.emiza.constant.Constant;

public class Message013 extends MessageCommon implements MessageSpecific {
	
	ResultSet rsBody = null;

	public Message013() {
		super();
		recordCount = 0;
		// TODO Auto-generated constructor stub
	}

	// columnCode is "Value" from ARTSANA_MESSAGE_FORMAT table
	// t is type C N D T
	// l is length mostly use for C and N;
	// d is for default value
	//p is data position
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
			case "13-100": // article code may have hyphen so split and use 1st part e.g.
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
			case "13-F01":
				returnValue = formateColumnValue(String.valueOf(recordCount), t, l);
				break;

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return returnValue;

	}

	public String processBody(ResultSet rsCF, String msgType) {
		return processBody_2B(rsCF, msgType);

	}

	private String processBody_2B(ResultSet rsCF, String msgType) {
		String returnValue = "";

		setQuery(Constant.MESSAGE_STOCK_INVENTORY);

		rsBody = getResultset();

		try {
			while (rsBody.next()) {
				rsCF.beforeFirst();

				while (rsCF.next()) {
					returnValue += buildColumnValue(rsCF.getString(7), rsCF.getString(5), rsCF.getInt(6),
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
