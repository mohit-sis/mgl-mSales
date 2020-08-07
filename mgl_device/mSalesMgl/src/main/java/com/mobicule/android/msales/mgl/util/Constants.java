
package com.mobicule.android.msales.mgl.util;

import android.app.ActivityManager;
import android.content.Context;

public class Constants
{
	//public static final String SERVER_URL_1 =  "http://182.72.129.214:8088";
	//public static final String SERVER_URL_2 =  "http://182.72.129.214:8088";

	//public static final String SERVER_URL_1 =  "http://172.31.1.36:8080"; //local pune
	//public static final String SERVER_URL_2 =  "http://172.31.1.36:8080";

	//public static final String SERVER_URL_1 = "http://219.65.116.9:8080"; //UAT old
	//public static final String SERVER_URL_2 ="http://219.65.116.9:8080";

	//public static final String SERVER_URL_1 = "http://61.95.151.51:8080"; //UAT new
	//public static final String SERVER_URL_2 = "http://61.95.151.51:8080";

	//public static final String SERVER_URL_1 = "http://14.141.125.91:8080"; //UAT new - 26/03/2018
	//public static final String SERVER_URL_2 = "http://14.141.125.91:8080";

//	public static final String SERVER_URL_1 = "http://59.185.249.202:8080"; //UAT new - 05/04/2018 current
//	public static final String SERVER_URL_2 = "http://59.185.249.202:8080";

	//public static final String SERVER_URL_1 = "http://c96e9248.ngrok.io"; //UAT new - 05/04/2018 current
	//public static final String SERVER_URL_2 = "http://c96e9248.ngrok.io";

//	//---------------------------------
	public static final String SERVER_URL_1 = "http://14.141.125.82:8080";   //ashish local
	public static final String SERVER_URL_2 = "http://14.141.125.82:8080";
	//------------------------------------
//	//---------------------------------
//	public static final String SERVER_URL_1 = "http://10.1.1.10";   //ashish local
//	public static final String SERVER_URL_2 = "http://10.1.1.10";
	//------------------------------------onActivityResult


	//public static final String SERVER_URL_1 = "http://172.31.1.4:8080";
	//public static final String SERVER_URL_2 = "http://172.31.1.4:8080";

	//public static final String SERVER_URL_1 = "http://182.72.129.214:8087"; // MNGL
	//public static final String SERVER_URL_2 = "http://182.72.129.214:8087";

	//public static final String SERVER_URL_1 = "http://172.31.1.4:9090"; //UAT new
	//public static final String SERVER_URL_2 ="http://172.31.1.4:9090";

	//public static final String SERVER_URL_1 = "http://14.141.107.134:8081"; //Pune Local New
	//public static final String SERVER_URL_2 = "http://14.141.107.134:8081";


	//local deepti
	//http://localhost:8443/mgl-msales-server/

	/*public static final String SERVER_URL_1 = "http://10.1.1.10:8443"; //deepti
	public static final String SERVER_URL_2 = "http://10.1.1.10:8443";*/


	//public static final String SERVER_URL_1 = "http://10.1.1.130:8085";
	//public static final String SERVER_URL_2 = "http://10.1.1.130:8085";

	//public static final String MGL_SERVER = "/mgl-msales-server/";

	//public static final String SERVER_URL_1 = "http://182.72.129.214:8082"; //MIDC local pointing
	//public static final String SERVER_URL_2 = "http://182.72.129.214:8082";

	//LIVE
	//public static final String SERVER_URL_1 ="http://14.141.147.122:8080";
	//public static final String SERVER_URL_2 ="http://14.141.147.122:8080";

	//LIVE
	//public static final String SERVER_URL_1 = "http://14.141.147.122:8080";
	//public static final String SERVER_URL_2 = "http://122.15.117.196:8080";

	//LIVE New IP current
//
//	public static final String SERVER_URL_1 = "http://122.15.117.196:8080";
//	public static final String SERVER_URL_2 = "http://122.15.117.196:8080";

	//public static final String MGL_SERVER = "/mgl-msales-server/"; //local pune

	//public static final String MGL_SERVER = "/mgl-msales-meterReading-server/"; //UAT/Mumbai
	//public static final String MGL_SERVER = "/mgl-msales-server-meterReading/"; // UAT New
	//public static final String MGL_SERVER = "/mgl-msales-server/";  //UAT New - 31/07/2017
	//public static final String MGL_SERVER = "/mgl-msales-server/"; //UAT New - 26/03/2018

	public static final String MGL_SERVER = "/mgl-msales-server/"; //live


	//public static final String MGL_SERVER = "/mgl-msales-server/";// Pune local new
	//public static final String MGL_SERVER = "/mgl-msales-server/";// local
	//public static final String MGL_SERVER = "/mngl-msales-server/"; //MNGL
	//public static final String MGL_SERVER = "/mgl-msales-newserver/";
	//public static final String MGL_SERVER = "/midc-msales-server/"; //MIDC

	//INSPECTION /mgl-msales-server-inspection/

	//public static final String MGL_SERVER = "/mgl-msales-server-inspection/";

	public static String DIAGNOSTIC_URL = SERVER_URL_1 + MGL_SERVER;

	public static final String REQUEST_GATEWAY = "RequestGateway";

	public static final String FIRST_URL = DIAGNOSTIC_URL + REQUEST_GATEWAY;

	public static String LOGIN_URL = FIRST_URL;

	public static String LOGIN_PARAM = "";

	public static String SYNC_URL = "";

	public static String BACKGROUND_ACTIVITY_DELAY = "";

	public static String TIME_VARIATION_SUBMISSION_DELAY = "";

	public static String timeVariableKey = "com.mobicule.msales.mgl.timeVariable";

	public static String bgActivityKey = "com.mobicule.msales.mgl.bgActivity";

	public static String SYNC_STATUS = "";

	public static String START_DATE = "0";

	public static String END_DATE = "0";

	public static String EMPTY_STRING = "";

	public static String BOOKWALK_ALARM_TIME = "0";

	public static final String FIELD_USER_DETAIL = "userdetail";

	public static final String USER_NAME = "name";

	public static final String LOGIN_NAME = "login";

	public static final String FIELD_PASSWORD = "pass";

	public static final String FIELD_IMEI_NUMBER = "imei";

	public static final String FIELD_KEY_METER_READING = "meterreading";

	public static final String KEY_CONN_COUNT = "connCount";

	public static final String KEY_CONN_NAME = "connName";

	public static final String KEY_LOCATION = "location";

	public static final String KEY_CONN_ADDRESS = "connAddress";

	public static final String KEY_BP_NUMBER = "bpNo";

	public static final String KEY_CUSTOMER_NAME = "customerName";

	public static final String KEY_CUSTOMER_ADDRESS = "address";

	public static final String KEY_CUSTOMER_LIST = "customerList";

	public static final String KEY_CUSTOMER_METER_NUMBER = "meterNo";

	public static final String KEY_CUSTOMER_CONTACT_NUMBER = "contactNo";

	public static final String KEY_CUSTOMER_LANDLINE_NUMBER = "resNo";

	public static final String KEY_CUSTOMER_OFFICE_NUMBER = "offcNo";

	public static final String FIELD_BP_NO = "bpNo";

	public static final String FIELD_ADDRESS = "address";

	public static final String FIELD_CUSTOMER_NAME = "customerName";

	public static final String KEY_READING_TIME_LIST = "readingTime";

	public static final String KEY_METER_READING = "meterReading";

	public static final String KEY_LAST_METER_READING_DATE = "prevReadDate";

	public static final String KEY_READINGS = "readings";

	public static final String KEY_MRO_NUMBER = "mroNo";

	public static final String KEY_MRU_CODE = "mruCode";

	public static final String KEY_CURRENT_SEAL_NO = "sr";

	public static final String KEY_No_OF_ATTEMPTS = "noOfAttempts";

	public static final String KEY_MESSAGE_TO_MR = "msgToMr";

	public static final String KEY_CUSTOMER_PREVIOUS_READING = "prevRead";

	public static String KEY_METER_READING_TYPE = "";

	public static final String KEY_PLAUSIBLE = "Plausible";

	public static final String KEY_IMPLAUSIBLE = "Implausible";

	public static final String KEY_INCOMPLETE = "Incomplete";

	public static final String KEY_RE_ENTER_METER_READING = "reenetermeterreading";

	public static final String ALERT_MSG_EXIT_APPLICATION = "Are you sure you want to exit the application?";

	public static final String ALERT_MSG_MR_VALIDATION = "Please enter atleast three digit Meter Readings";

	public static final String ALERT_MSG_BOOKWALK_SYNC_AGAIN = "Kindly Download the BookWalk Sequence again.";

	public static final String ALERT_MSG_NO_BOOKWALK_ASSIGNED = "You have not been assigned a Bookwalk Sequence.";

	//public static final String OnMPLANNINGBO_KEY="onmPlanning";

	// --------------------database and table
	// name---------------------------------------

	public static final String DATABASE_NAME = "MOBICULE_DB";

	public static final String USER_STORAGE = "user";

	public static final String TABLE_RANDOM_METER_READING = "RANDOM_METER_READING";

	public static final String TABLE_SAVED_METER_READING = "SAVED_METER_READING";

	public static final String USER_IMEI_STORE = "imeiStorage";

	public static final String synckey = "com.mobicule.msales.mgl.bookwalksqnce";

	public static final String FIELD_CONN_NAME = "connName";

	public static final String FIELD_METER_NO = "meterNo";

	public static final String FIELD_CONTACT_NO = "contactNo";

	public static final String FIELD_FLAT_NO = "flat";

	public static final String FIELD_BUILDING_NAME = "buildName";

	public static final String FIELD_CURR_CONTACT_NO = "currContactNo";

	public static final String FIELD_NEW_CONTACT_NO = "newContactNo";

	public static final String FIELD_CURR_METER_NO = "currMeterNo";

	public static final String FIELD_NEW_METER_NO = "newMeterNo";

	public static final String FIELD_CONN_OBJ = "connObj";

	public static final String ALERT_MSG_LOGOUT_APPLICATION = "Are you sure you want to log out of the application?";

	public static final String FIELD_UNATTEMPTED = "unattempted";

	public static final String FIELD_STATUS = "status";

	public static final String FIELD_SYNC_STATUS = "syncStatus";

	public static final String FIELD_FAILED_TO_READ = "failed";

	public static final String FIELD_COMPLETED = "completed";

	public static final String FIELD_INCOMPLETE = "incomplete";

	public static final String FIELD_CUSTOMER_COUNT = "customerCount";

	public static final String FIELD_VERSION_NO = "bwVersion";

	public static final String FIELD_BWK_NAME = "bwkName";

	public static final String FIELD_START_DATE = "strtDate";

	public static final String FIELD_END_DATE = "endDate";

	public static final String FIELD_LAST_SYNC_DATE = "lastSyncDate";

	public static final String KEY_CUSTOMER_CA_NUMBER = "caNo";

	public static final String KEY_BACKGROUND_ACTIVITY_DELAY = "bkActDelay";

	public static final String KEY_AVERAGE_CONSUMPTION = "avgCons";

	public static final String KEY_PREVIOUS_READING_DATE = "prevReadDate";

	public static final String KEY_TIME_VARIATION_SUBMISSION_DELAY = "timeVarDelay";

	public static final String MSALES_MGL_SHARED_PREFERENCE = "com.mobicule.msales.mgl";

	public static final String FIELD_BLKWLK_ALARM_TIME = "bkwlkAlarmTime";

	public static final String DATE_TIME_FORMAT_CLIENT = "yyyy-mm-dd";// yyyy-mm-DD// HH:mm:s//dd/MM/yyyy
	// //dd/MM/yyyy
	// HH:mm:s

	public static final String KEY_TIME = "time";

	public static final String KEY_DATE = "date";

	public static final String KEY_NO_OF_BURNERS = "noOfBurner";

	public static final String KEY_NO_OF_GEYSERS = "noOfGasGeyser";

	public static final String KEY_IS_HOSE_AVALABLE = "isHoseAvailable";

	public static final String KEY_MSG_FROM_MR = "msgFromMr";

	public static final String KEY_IMAGE1 = "image";

	public static final String KEY_IMAGE2 = "image2";

	public static final String KEY_MSG_REMARKS = "msgRemarks";

	public static String ALARM_NOTIFICATION_TITLE = "";

	public static final String ALARM_TICKER_TEXT = "BookWalk Reminder!";

	public static String ALARM_NOTIFICATION_TEXT = "";

	public static String SUBMIT = "Submit";

	public static String INSPECTION_DETAILS = "Inspection Details";

	public static final String GOOGLE_URL = "http://www.google.co.in/";

	public static final String AUTO_TRIGGER = "Kindly note that your unattempted readings are being converting to completed";

	public static boolean autoAlarmNotification = false;

	public static boolean searchSelection = false;

	public static boolean searchUpdate = false;

	public static boolean isRegistered = false;

	public static boolean isThreePicsSelected = false;

	public static boolean isTwoPicsSelected = false;

	public static boolean isOnePicsSelected = false;

	public static int threePicsCapCnt = 0;

	public static int photoCapturedCount = 0;

	// Actions

	public static String BACKGROUND_ACTION = "SendBackgroundReceiverAction";

	public static String AUTOTRIGGER_ACTION = "AutoTriggerAction";

	public static Context broadcastDialogContext = null;

	public static boolean isbookwalkSyncAgain = false;

	public static boolean isBroadcastRegistered = false;

	public static String ALERT_NOT_ASSIGNED_BOOKWALK = "You have not been assigned a Bookwalk Sequence.";

	public static boolean COMPLTED_BOOKWALK = false;

	public static String JoinConnObj = "";

	public static boolean IS_SYNC_RUNNING = false;

	public static boolean IS_FileLog = true;          //added by ashish

	// TODO: 7/6/19

	public static String IMAGE_DIRECTORY_NAME = "MGL/images";

	public static String PREVIOUS_AVAIL_CAPTURE_PATH = "";
    public static String imagePath;

	public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
