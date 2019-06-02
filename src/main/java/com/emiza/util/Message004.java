package com.emiza.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Message004 extends MessageCommon implements MessageSpecific {

	ResultSet rsBody = null;
	String po = null;

	public Message004(String orderId) {
		super();
		recordCount = 0;
		this.po = orderId;
		// TODO Auto-generated constructor stub
	}

	// columnCode is "Value" from ARTSANA_MESSAGE_FORMAT table
	// t is type C N D T
	// l is length mostly use for C and N;
	// d is for default value
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
					returnValue = formateColumnValue(returnValue.trim(), t, l);
					break;
				case "N":
					returnValue = String.valueOf(rsBody.getInt(columnNo));
					if (null == returnValue) {
						returnValue = "0";
					}
					returnValue = formateColumnValue(returnValue.trim(), t, l);
					break;
				}
			}

				break;
			case "RD": {
				columnNo = Integer.parseInt(d);
				returnValue = rsBody.getString(columnNo);
				if (null == returnValue) {
					returnValue = " ";
					returnValue = formateColumnValue(returnValue,"C",l);
				} else {
					returnValue = formateColumnValue(returnValue.trim(), t.trim(), l);
				}
			}
				break;
			case "D":
				returnValue = d;
				break;
			case "DF":
				returnValue = d;
				returnValue = formateColumnValue(returnValue.trim(), t, l);
				break;
			case "CD":
				returnValue = getCurrentDate();
				break;
			case "CT":
				returnValue = getCurrentTime();
				break;
			case "4-F01":
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

		switch (msgType) {
		case "2B":
			returnValue += processBody_2B(rsCF, msgType);
			break;
		case "3B":
			returnValue += processBody_3B(rsCF, msgType);
			break;
		}
		;

		// returnValue += "\r\n";
		return returnValue;

	}

	private String processBody_2B(ResultSet rsCF, String msgType) {
		String returnValue = "";
		String owner = "ARTS";
		String warehouse = "CHC02";

		String queryBody = "EXEC [dbo].[SP_EMIZA_ARTASANA_PO_ASN_HEADER] '" + owner + "', '" + warehouse + "', '" + po.trim() + "'";

System.out.println(queryBody);
		setQuery(queryBody);

		rsBody = getResultset();

		try {
			while (rsBody.next()) {
				po = rsBody.getString(2);
				if (po == null) {
					po = rsBody.getString(3).trim();
				}
				System.out.println("po: "+ po);
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
		String owner = "ARTS";
		String warehouse = "CHC02";

		String queryBody = "EXEC [dbo].[SP_EMIZA_ARTASANA_PO_ASN_BODY] '" + owner + "', '" + warehouse + "', '" + po.trim()
				+ "'";
		setQuery(queryBody);

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

}
