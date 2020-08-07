package com.mobicule.component.util;

public class CoreConstants
{
	//Json Keys

	public static final String EMPTY_STRING = "";
	
	public static final String KEY_DATA = "data";

	public static final String USER_DETAIL_JESONOBJECT = "user";

	public static final String KEY_METER_READING = "meterReading";

	//*****************************************************************************************	
	// Response object fields
	
	

	public static final String USER_RESPONSE_STATUS = "status";

	public static final String USER_RESPONSE_MESSAGE = "message";

	public static final String USER_RESPONSE_DATA = "data";
	
	public static final String VERSION_CHANGE = "CHANGE";

	public static final String STATUS_RESPONSE_SUCCESS = "success";

	public static final String STATUS_RESPONSE_FAILURE = "failure";

	public static final String FIELD_ERROR = "error";

	public static final String FIELD_PENDING = "pending";

	public static final String FIELD_FAILED = "failed";

	public static final String FIELD_VERSION_NO = "bwVersion";

	public static final String FIELD_BWK_NAME = "bwkName";

	public static final String FIELD_CUSTOMER_LIST = "customerList";

	public static final String FIELD_BP_NO = "bpNo";
	
	public static final String FIELD_MRU_CODE = "mruCode";

	public static final String FIELD_MRO_NO = "mroNo";
	
	public static final String FIELD_CONN_OBJ = "connObj";
	
	public static final String FIELD_MR_CODE = "mrCode";
	
	public static final String FIELD_READING_DATE = "date";
	
	public static final String FEY_READING = "readings";
	
	public static final String COM_MRU_CODE = "com";
	
	public static String SYNC_URL="";

	///////////////////////////////////////////////////////

	//DataBase Table Names

	public static final String TABLE_SAVED_METER_READING = "SAVED_METER_READING";
	
	public static final String TABLE_SAVED_JOIN_TICKETING = "SAVED_JOIN_TICKETING";
	
	public static final String TABLE_JOIN_TICKETING_IMAGE = "JOIN_TICKETING_IMAGE";

	public static final String TABLE_RANDOM_METER_READING = "RANDOM_METER_READING";

	public static final String TABLE_CUSTOMER_UPDATE = "CUSTOMER_UPDATE";

	public static final String TABLE_CUSTOMER_INFO = "CUSTOMER_INFO";
	
	public static final String TABLE_OnM_PLANNING = "OnM_PLANNING";

	public static final String BOOKWALK_TABLE = "BOOKWALK";

	public static final String TABLE_COLUMN_STATUS = "status";

	public static final String TABLE_COLUMN_DATA = "data";

	public static final String TABLE_COLUMN_BOOKWALK_ID = "bookwalk_id";
	
	public static final String TABLE_SEQUENCE_NUMBER_UPDATE = "SEQUENCE_NUMBER_UPDATE";

	public static final String PAGE_SIZE = "100";

	public static final String TABLE_SYNC = "SYNC";

	public static final String FIELD_UNATTEMPTED = "unattempted";

	public static final String MSG_FAILED_TO_TAKE_MR = "Failed to complete work given";
	
	public static final String ALERT_MSG_BOOKWALK_SYNC_AGAIN = "Kindly Download the BookWalk Sequence again.";

	public static final String FIELD_COMPLETED = "completed";

	public static final String FAILED_MR_CODE = "49";
	
	public static final String KEY_JT_SUBMIT = "jtSubmit";

	public static final String KEY_MR_SUBMIT = "mrSubmit";

	public static final String KEY_UPDATECUSTOMER_SUBMIT = "customerUpdateSubmit";

	public static final String KEY_RMR_SUBMIT = "rmrSubmit";

	public static final String KEY_NO_OF_ATTEMPTS = "noOfAttempts";
	
	public static final String TABLE_COLUMN_BPNUMBER = "bpNumber";
	
	public static final String TABLE_COLUMN_METERNO = "meterNo";
	
	public static final String TABLE_COLUMN_CONTACTNO = "contactNo";
	
	public static final String TABLE_COLUMN_CUSTOMERNAME = "customerName";
	
	public static final String TABLE_COLUMN_FLATNO = "flatNo";

	public static final String TABLE_COLUMN_BUILDING_NAME = "buildingName";
	
	public static final String TABLE_COLUMN_CONTACTNO_OFFICE = "contactNoOffice";
	
	public static final String TABLE_COLUMN_CONTACTNO_RES = "contactNoRes";
	

	private static boolean isAutotriggerOn = false;

	public static final String CREATE_CUSTOMER_INFO = "CREATE TABLE IF NOT EXISTS " + CoreConstants.TABLE_CUSTOMER_INFO
			+ " (_id INTEGER primary key autoincrement, " + CoreConstants.TABLE_COLUMN_DATA + " Text not null,"
			+ CoreConstants.TABLE_COLUMN_STATUS + " Text NOT NULL DEFAULT 'unattempted',"
			+ CoreConstants.TABLE_COLUMN_BOOKWALK_ID + " INTEGER, " + "FOREIGN KEY ("
			+ CoreConstants.TABLE_COLUMN_BOOKWALK_ID + ") REFERENCES " + CoreConstants.BOOKWALK_TABLE + "(_id));";

	/*CREATE_CUSTOMER_INFO = "CREATE TABLE " + CoreConstants.TABLE_CUSTOMER_INFO + " (_id INTEGER primary key autoincrement, "
	+ CoreConstants.TABLE_COLUMN_DATA + " Text not null,"
	+ CoreConstants.TABLE_COLUMN_STATUS + " Text NOT NULL DEFAULT 'unattempted'," 
	+ CoreConstants.TABLE_COLUMN_BOOKWALK_ID+ " INTEGER);";*/

	public static final String CREATE_BOOKWALK_TABLE = "CREATE TABLE IF NOT EXISTS " + CoreConstants.BOOKWALK_TABLE
			+ " (_id INTEGER primary key autoincrement, " + "data Text not null);";
	
	public static final String CREATE_SAVED_METER_READING_TABLE = "CREATE TABLE IF NOT EXISTS " + CoreConstants.TABLE_SAVED_METER_READING
			+ " (_id INTEGER primary key autoincrement, " + "data Text not null);";
	
	public static final String CREATE_CUSTOMER_UPDATE_TABLE = "CREATE TABLE IF NOT EXISTS " + CoreConstants.TABLE_CUSTOMER_UPDATE
			+ " (_id INTEGER primary key autoincrement, " + "data Text not null);";

	public static final String CREATE_SAVED_JOIN_TICKETING_TABLE = "CREATE TABLE IF NOT EXISTS " + CoreConstants.TABLE_SAVED_JOIN_TICKETING
			+ " (_id INTEGER primary key autoincrement, " + "data Text not null);";
	
	public static final String CREATE_JOIN_TICKETING_IMAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + CoreConstants.TABLE_JOIN_TICKETING_IMAGE
			+ " (_id INTEGER primary key autoincrement, " + "data Text not null);";
	
	public static boolean isAutotriggerOn()
	{
		return isAutotriggerOn;
	}

	public static void setAutotriggerOn(boolean isAutotriggerOn)
	{
		CoreConstants.isAutotriggerOn = isAutotriggerOn;
	}
}
