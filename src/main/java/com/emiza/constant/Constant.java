package com.emiza.constant;

public class Constant {
	public static final String DATABASE = "database";
	public static final String DATABASELOG = "databaselog";
	public static final String DATABASEAUDIT = "databaseAudit";
	public static final String SUSER = ";user=";
	public static final String USER = "user";
	public static final String SPASSWORD = ";password=";
	public static final String SDATABASE = ";databaseName=";
	public static final String SERVER = "server";
	public static final String PORT = "port";
	public static final String NAME = "name";
	public static final String PASSWORD = "password";
	public static final String JDBCSQL = "jdbc:sqlserver://";
	public static final String CONFIG = "config.ini";
	public static final String LOCALHOST = "localhost";
	
	public static final String MESSAGE_STOCK_INVENTORY = "EXEC [dbo].[SP_EMIZA_GET_ARTSANA_MESSAGE_STOCK]";
	public static final String MESSAGAE008_PICK_PACK_CANCEL = "EXEC [dbo].[SP_EMIZA_GET_ARTSANA_MESSAGE_PICK_PACK_CANCEL] '<WAREHOUSE-ID>', '<OWNER-ID>', '<SO-NUMBER>'";
	public static final String MESSAGAE008_PICK_PACK_HEADER = "EXEC [dbo].[SP_EMIZA_GET_ARTSANA_MESSAGE_PICK_PACK_HEADER] '<WAREHOUSE-ID>', '<OWNER-ID>', '<SO-NUMBER>'";
	public static final String MESSAGAE008_PICK_PACK_PACKAGE = "EXEC [dbo].[SP_EMIZA_GET_ARTSANA_MESSAGE_PICK_PACK_PACKAGE] '<WAREHOUSE-ID>', '<OWNER-ID>', '<SO-NUMBER>'";
	public static final String MESSAGAE008_PICK_PACK_PACKAGE_DETAILS = "EXEC [dbo].[SP_EMIZA_GET_ARTSANA_MESSAGE_PICK_PACK_PACKAGE_DETAILS] '<WAREHOUSE-ID>', '<OWNER-ID>', '<SO-NUMBER>'";
	public static final String GET_MESSAGE_TYPE = "Select distinct MSG_TYPE from ARTSANA_MESSAGE_FORMAT where MSG_CODE = <MSG_CODE> order by MSG_TYPE";
	public static final String REPLACE_MSG_CODE = "<MSG_CODE>";
	public static final String GET_MESSAGE = "select  * from ARTSANA_MESSAGE_FORMAT where MSG_CODE = <MSG_CODE> and MSG_TYPE = '<MSG_TYPE>' order by MSG_TYPE, FIELD_NO ASC";
	public static final String REPLACE_MSG_TYPE ="<MSG_TYPE>";
	public static final String DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";
	public static final String SP_EMIZA_ARTSANA_REPLENISHMENTDELIVERY = "EXEC [dbo].[SP_EMIZA_ARTSANA_REPLENISHMENTDELIVERY] <DATA_VALE>";
	public static final String REPLACE_DATA_VALUE = "<DATA_VALE>"; 
	public static final String SP_EMIZA_OUTBOUND_DELIVERY = "EXEC [dbo].[SP_EMIZA_OUTBOUND_DELIVERY] <DATA_VALE>";
	public static final String SP_EMIZA_ARTSANA_MATERIAL_MASTER = "EXEC [dbo].[SP_EMIZA_ARTSANA_MATERIAL_MASTER] <DATA_VALE>";

	
}
