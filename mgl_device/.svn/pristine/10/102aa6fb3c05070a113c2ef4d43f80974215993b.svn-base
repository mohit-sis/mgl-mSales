package com.mobicule.android.component.db;

import static com.mobicule.android.component.logging.MobiculeLogger.debug;
import static com.mobicule.android.component.logging.MobiculeLogger.isLogEnabled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.mobicule.android.component.logging.MobiculeLogger;

public class SQLiteDatabaseManager implements IDatabaseManager
{
	private static String DATABASE_NAME = "";

	private static IDatabaseManager uniqueInstance;

	private static DbConnectionManager connectionManager;

	public static final String TAG = "DataBaseAdapter";

	private static HashMap<String, SQLiteDatabaseManager> hashmap = new HashMap<String, SQLiteDatabaseManager>();

	private static HashMap<String, DbConnectionManager> hashmapDbConnectionManager = new HashMap<String, DbConnectionManager>();

	public static final String KEY_DATA = "data";

	public static final String KEY_ROWID = "_id";

	public static final String ALLOCATED_LEADS = "allocatedLeadsSync";

	private static final String LOCAL_LEAD = "localLeads";

	public static final String KEY_TAB_CODE = "tabCode";

	public static final String KEY_TAB_PRIORITY = "tabPriority";

	public static final String KEY_FIELD_CODE = "fieldCode";

	public static final String KEY_FIELD_PRIORITY = "fieldPriority";

	public static final String KEY_PRODUCT_CODE = "productCode";

	public static final String ENTITY_TAB = "tabSync";

	public static final String ENTITY_FIELD = "fieldSync";

	public static final String ENTITY_PRODUCT_FIELD_MAPPING = "productFieldMappingSync";

	private static final String KEY_LEAD_STATUS = "leadStatus";

	private static final String KEY_ERROR_MESSAGE = "submitErrorMessage";

	private static final String KEY_SUBMISSION_STATUS = "submissionStatus";

	private static final String KEY_CREATED_DATE = "createdDate";

	private static final String KEY_SAVED_DATE = "savedDate";

	public static final String KEY_LIST_OF_VALUES = "listOfValues";

	public static final String FIELD_INVESTIGATION = "fieldInvestigation";

	public static final String FIELD_VERIFICATION = "FIELDVERIFICATION";

	public static final String ALLOCATED_FI_SYNC = "ALLOCATEDFISYNC";

	public static final String BANK_BRANCH_SYNC = "bankBranchSync";

	public static final String STATE_SYNC = "stateSync";

	public static final String CITY_SYNC = "citySync";

	public static final String EMAIL_DATA_IMAGES = "emailDataImages";

	private static final String KEY_LEAD_INFO = "leadInfo";

	private static final String KEY_FI_DATA = "fiData";

	private static final String KEY_DATA_SUBMISSION_STATUS = "dataSubmissionStatus";

	private static final String KEY_IMAGE_SUBMISSION_STATUS = "imageSubmissionStatus";

	private static final String KEY_REJECTION_STATUS = "rejectionStatus";

	private static final String KEY_FORM_STATUS = "formStatus";

	private static final String KEY_SUBMISSION_DATE = "submissionDate";

	private static final String KEY_BANK_BRANCH_DESC = "bankBranchDesc";

	private static final String KEY_BANK_BRANCH_CODE = "bankBranchCode";

	private static final String KEY_STATE_CODE = "stateCode";

	private static final String KEY_STATE_DESC = "stateDesc";

	private static final String KEY_CITY_DESC = "cityDesc";

	private static final String KEY_CITY_CODE = "cityCode";

	/*--------------------TU------------------*/

	private static final String TU_CIBIL = "tuCIBIL";

	private static final String TU_PDF = "tuPDF";

	private static final String TU_FI = "tuFI";

	private static final String TU_LEAD = "tuLEAD";

	/*--------------------CIBIL------------------*/

	private static final String CIBIL_SCORE = "cibilFI";

	private static final String CIBIL_DETAILS = "cibilDetails";

	public static final String KEY_MOBI_ID = "mobileTransId";

	private static final String CIBIL_PDF = "cibilPDF";

	private static final String CIBIL_SCR = "cibilSCORE";

	//private static final String CIBIL_DETAILS = "cibilDetails";

	//  private static final String CIBIL_DETAILS = "cibilDetails";

	//STATUS_IMAGE----------------------

	public static final String STATUS_IMAGES = "StatusImages";

	//public static final String KEY_IMAGEPATH = "ImagePath";

	public static final String KEY_IMAGE_NAME = "imageName";

	public static final String KEY_STATUS = "status";

	public static final String KEY_REASON = "reason";

	private static final String KEY_IMAGE_PATH = "imagePath";
	
	public static final String TABLE_CUSTOMER_INFO = "CUSTOMER_INFO";
	
	public static final String BOOKWALK_TABLE = "BOOKWALK";

	public static final String TABLE_COLUMN_STATUS = "status";
	
	public static final String TABLE_COLUMN_BPNUMBER = "bpNumber";
	
	public static final String TABLE_COLUMN_BUILDING_NAME = "buildingName";
	
	public static final String TABLE_COLUMN_METERNO = "meterNo";
	
	public static final String TABLE_COLUMN_CONTACTNO = "contactNo";
	
	public static final String TABLE_COLUMN_CONTACTNO_OFFICE = "contactNoOffice";
	
	public static final String TABLE_COLUMN_CONTACTNO_RES = "contactNoRes";
	
	public static final String TABLE_COLUMN_CUSTOMERNAME = "customerName";
	
	public static final String TABLE_COLUMN_FLATNO = "flatNo";

	public static final String TABLE_COLUMN_DATA = "data";

	public static final String TABLE_COLUMN_BOOKWALK_ID = "bookwalk_id";

	private SQLiteDatabaseManager(Context context, String DB)
	{
		//connectionManager = new DbConnectionManager(context);

		//connectionManager = DbConnectionManager.getInstance(context);

	}

	public synchronized static IDatabaseManager getInstance(Context context, String dbName)
	{
		/*if (uniqueInstance == null)
		{*/

		if (hashmap.containsKey(dbName))
		{
			uniqueInstance = hashmap.get(dbName);

			connectionManager = hashmapDbConnectionManager.get(dbName);

			//connectionManager = DbConnectionManager.getInstance(context);
		}
		else
		{

			hashmap.put(dbName, new SQLiteDatabaseManager(context, dbName));

			hashmapDbConnectionManager.put(dbName, new DbConnectionManager(context, dbName));

			uniqueInstance = hashmap.get(dbName);

			connectionManager = hashmapDbConnectionManager.get(dbName);

		}

		return uniqueInstance;
	}

	public String getDatabase_Name()
	{
		return DATABASE_NAME;
	}

	public synchronized static void setDatabase_Name(String database_name)
	{
		DATABASE_NAME = database_name;
	}

	@Override
	public int add(String entity, String data)
	{
		int _id = -1;

		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			ContentValues initialValues = createContentValues(data);

			_id = (int) database.insert(entity, null, initialValues);

			connectionManager.closeDatabase();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			e.printStackTrace(FileUtil.exceptionToFile());
		}

		return _id;
	}
	
	@Override
	public int add(String entity, ContentValues data)
	{
		int _id = -1;

		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			//ContentValues initialValues = createContentValues(data);

			_id = (int) database.insert(entity, null, data);

			connectionManager.closeDatabase();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			e.printStackTrace(FileUtil.exceptionToFile());
		}

		return _id;
	}

	private ContentValues createContentValues(String data)
	{
		ContentValues values = new ContentValues();
		values.put(KEY_DATA, data);
		return values;
	}

	@Override
	public int add(String entity, String[] columnarray, String[] valuearray, String data)
	{
		int _id = -1;

		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			ContentValues values = new ContentValues();

			values.put(KEY_DATA, data);

			for (int i = 0; i < columnarray.length; i++)
			{
				values.put(columnarray[i], valuearray[i]);
			}
			_id = (int) database.insert(entity, null, values);

			connectionManager.closeDatabase();
			//			return _id;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			e.printStackTrace(FileUtil.exceptionToFile());
		}

		return _id;
	}

	@Override
	public boolean delete(String entity, int rowId)
	{
		boolean status = false;
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			status = database.delete(entity, KEY_ROWID + "=" + rowId, null) > 0;

			connectionManager.closeDatabase();

			return status;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			e.printStackTrace(FileUtil.exceptionToFile());
		}

		return status;
	}

	@Override
	public Cursor search(String entity, Clause clause)
	{
		Cursor list = null;
		//List<Map<Integer, String>> list = new Vector<Map<Integer, String>>();
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			list = database.rawQuery(createQuery(entity, clause), null);

			MobiculeLogger.verbose("return sarch result", "" + list.getCount());

			connectionManager.closeDatabase();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			e.printStackTrace(FileUtil.exceptionToFile());
		}

		return list;
	}

	private String createQuery(String entity, Clause clause)
	{
		boolean isFirstClause = true;

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT * FROM ");
		queryBuilder.append(entity);

		Clause currentClause = clause;
		Operator currentOperator = null;

		while (currentClause != null)
		{
			QueryItem currentQueryItem = currentClause.getLeftItem();
			Clause nextClause = currentClause.getRightItem();

			if (currentQueryItem == null)
			{
				break;
			}

			if (isFirstClause)
			{
				queryBuilder.append(" WHERE ");
				isFirstClause = false;
			}
			else
			{
				switch (currentOperator)
				{
					case AND:
						queryBuilder.append(" AND ");
						break;
					case OR:
						queryBuilder.append(" OR ");
						break;
					default:
						queryBuilder.append(" ");
						break;
				}
			}

			queryBuilder.append(KEY_DATA);
			queryBuilder.append(" LIKE ");
			queryBuilder.append("'");

			String key = currentQueryItem.getKey();

			if (key != null)
			{
				queryBuilder.append("%");
				queryBuilder.append("\"");
				queryBuilder.append(currentQueryItem.getKey());
				queryBuilder.append("\"");
				queryBuilder.append(":");
			}

			SearchCondition searchCondition = currentQueryItem.getSearchCondition();

			switch (searchCondition)
			{
				case CONTAINS:
					queryBuilder.append("%");
					queryBuilder.append(currentQueryItem.getValue());
					queryBuilder.append("%");
					break;
				case STARTS_WITH:
					queryBuilder.append(currentQueryItem.getValue());
					queryBuilder.append("%");
					break;
				case ENDS_WITH:
					queryBuilder.append("%");
					queryBuilder.append(currentQueryItem.getValue());
					break;
				case EQUALS:
					queryBuilder.append("\"");
					queryBuilder.append(currentQueryItem.getValue());
					queryBuilder.append("\"");
					break;
				default:
					break;
			}

			if (key != null)
			{
				queryBuilder.append("%");
			}

			queryBuilder.append("'");

			currentOperator = currentClause.getOperator();
			currentClause = nextClause;
		}

		queryBuilder.append(";");

		MobiculeLogger.verbose("**************Query**************", queryBuilder.toString());
		return queryBuilder.toString();
	}

	@Override
	public boolean update(String entity, String[] columnarray, String[] valuearray, String data, int rowId)
	{
		MobiculeLogger.verbose("sqlitedatabaseadapter update : ", entity);
		boolean status = false;
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			MobiculeLogger.verbose("new update data in databaseadapter : ", data);

			ContentValues updateValues = new ContentValues();

			updateValues.put(KEY_DATA, data);

			for (int i = 0; i < columnarray.length; i++)
			{
				updateValues.put(columnarray[i], valuearray[i]);
			}

			status = database.update(entity, updateValues, KEY_ROWID + "=" + rowId, null) > 0;

			connectionManager.closeDatabase();

			return status;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			e.printStackTrace(FileUtil.exceptionToFile());
		}

		return status;

	}

	@Override
	public boolean update(String entity, String data, int rowId)
	{
		MobiculeLogger.verbose("sqlitedatabaseadapter update  : ", entity);

		boolean status = false;
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			MobiculeLogger.verbose("old update data in databaseadapter : ", data);

			ContentValues updateValues = createContentValues(data);

			status = database.update(entity, updateValues, KEY_ROWID + "=" + rowId, null) > 0;

			connectionManager.closeDatabase();

			return status;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			e.printStackTrace(FileUtil.exceptionToFile());
		}

		return status;

	}
	
	@Override
	public boolean update(String entity, ContentValues data, int rowId)
	{
		MobiculeLogger.verbose("sqlitedatabaseadapter update  : ", entity);

		boolean status = false;
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			//Log.d("old update data in databaseadapter : ", data);

			//ContentValues updateValues = createContentValues(data);

			status = database.update(entity, data, KEY_ROWID + "=" + rowId, null) > 0;

			connectionManager.closeDatabase();

			return status;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			e.printStackTrace(FileUtil.exceptionToFile());
		}

		return status;

	}


	@Override
	public String[] getColumnNames(String entity)
	{
		String[] status = null;
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			status = database.rawQuery("SELECT * FROM " + entity + " ;", null).getColumnNames();

			connectionManager.closeDatabase();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			e.printStackTrace(FileUtil.exceptionToFile());
		}
		return status;
	}

	@Override
	public int getRowCount(String entity)
	{
		int status = 0;

		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			Cursor mCursor = database.rawQuery("SELECT Count(*) FROM " + entity + " ;", null);

			int count = (int) DatabaseUtils.queryNumEntries(database, entity);

			mCursor.close();

			status = count;

			connectionManager.closeDatabase();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			e.printStackTrace(FileUtil.exceptionToFile());
		}

		return status;
	}

	@Override
	public void dropTable(String entity)
	{
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			database.execSQL("DROP TABLE IF EXISTS " + entity + " ;");

			connectionManager.closeDatabase();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			e.printStackTrace(FileUtil.exceptionToFile());
		}
	}

	public ArrayList<String> getLeadIdList()
	{
		ArrayList<String> leadIdList = new ArrayList<String>();
		Cursor allocatedLeadsCursor = null;
		Cursor localLeadsCursor = null;
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			String selectallocatedLeadsQuery = "SELECT * from " + ALLOCATED_LEADS;

			allocatedLeadsCursor = database.rawQuery(selectallocatedLeadsQuery, null);

			MobiculeLogger.verbose("getLeadIdList Query : ", selectallocatedLeadsQuery);

			try
			{
				if (allocatedLeadsCursor.moveToFirst())
				{
					do
					{
						JSONObject jsonObject = new JSONObject(allocatedLeadsCursor.getString(allocatedLeadsCursor
								.getColumnIndex("data")));

						leadIdList.add(jsonObject.getString("Lead Id"));
					}
					while (allocatedLeadsCursor.moveToNext());

					allocatedLeadsCursor.close();
				}
			}
			catch (NullPointerException e)
			{
				e.printStackTrace(FileUtil.exceptionToFile());
				throw new RuntimeException("Id is not present storage");
			}


			String selectLocalLeadsQuery = "SELECT * from " + LOCAL_LEAD;

			localLeadsCursor = database.rawQuery(selectLocalLeadsQuery, null);

			if (isLogEnabled(Log.DEBUG))
			{
				debug("getLocalLeadIdList Query : " + selectLocalLeadsQuery);
			}

			try
			{
				if (localLeadsCursor.moveToFirst())
				{
					do
					{
						JSONObject jsonObject = new JSONObject(localLeadsCursor.getString(localLeadsCursor
								.getColumnIndex("data")));

						leadIdList.add(jsonObject.getString("Lead Id"));
					}
					while (localLeadsCursor.moveToNext());

					localLeadsCursor.close();
				}
			}
			catch (NullPointerException e)
			{
				e.printStackTrace(FileUtil.exceptionToFile());
				throw new RuntimeException("Id is not present storage");
			}

			connectionManager.closeDatabase();
		}
		catch (Exception e)
		{
			MobiculeLogger.verbose("SQLException", " got exception =========> ");
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		MobiculeLogger.info("leadIdList: " + leadIdList);

		return leadIdList;
	}

	public ArrayList<JSONObject> getFieldsForTabs(String productName, String tabCode)
	{
		ArrayList<JSONObject> fieldsList = new ArrayList<JSONObject>();
		//Cursor dataRow = null;
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			String selectQuery = "SELECT fields_table.data , mapping_table.listOfValues, mapping_table.data as mappingdata FROM FIELDSYNC fields_table JOIN PRODUCTFIELDMAPPINGSYNC mapping_table ON fields_table.fieldCode=mapping_table.fieldCode WHERE (mapping_table.productCode=? and tabCode=?) order by fields_table.fieldPriority asc";

			Cursor cursor = database.rawQuery(selectQuery, new String[] { productName, tabCode });

			if (isLogEnabled(Log.DEBUG))
			{
				debug("Fields Query : " + selectQuery, "productName : " + productName + " tabCode : " + tabCode);
			}

			//dataRow = databaseAdapter.getFieldsForTabs(productName, tabCode);

			try
			{
				if (cursor.moveToFirst())
				{
					do
					{
						//Log.d("Value"+dataRow.getCount(), "Data =========> "+dataRow.getString(dataRow.getColumnIndex("data")));
						//Log.d("Value"+dataRow.getCount(), "List =========> "+dataRow.getString(dataRow.getColumnIndex("listOfValues")));
						JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex("data")));
						jsonObject.put("listOfValues",
								new JSONArray(cursor.getString(cursor.getColumnIndex("listOfValues"))));

						JSONObject mappingData = new JSONObject(cursor.getString(cursor.getColumnIndex("mappingdata")));
						if (mappingData.has("mandatory"))
						{
							jsonObject.put("mandatory", mappingData.get("mandatory"));
						}
						else
						{
							jsonObject.put("mandatory", "No");
						}

						fieldsList.add(jsonObject);
					}
					while (cursor.moveToNext());
				}
				cursor.close();

				connectionManager.closeDatabase();
			}
			catch (NullPointerException e)
			{
				e.printStackTrace(FileUtil.exceptionToFile());
				throw new RuntimeException("Id is not present storage");
			}
		}
		catch (Exception e)
		{
			MobiculeLogger.verbose("SQLException", " got exception =========> ");
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		return fieldsList;
	}

	public ArrayList<JSONObject> getFieldsForTBS013Tabs(String productName, String tabCode, String customerSegment)
	{
		ArrayList<JSONObject> fieldsList = new ArrayList<JSONObject>();

		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			// TODO Auto-generated method stub

			//String selectQuery = "SELECT fields_table.data , mapping_table.listOfValues FROM FIELDSYNC fields_table JOIN PRODUCTFIELDMAPPINGSYNC mapping_table ON fields_table.fieldCode=mapping_table.fieldCode WHERE (mapping_table.productCode=? and tabCode=?) order by fields_table.fieldPriority asc";
			//String selectQuery = "SELECT fields_table.data , mapping_table.listOfValues , mapping_table.data as pfmData FROM FIELDSYNC fields_table JOIN PRODUCTFIELDMAPPINGSYNC mapping_table ON fields_table.fieldCode=mapping_table.fieldCode WHERE (mapping_table.productCode=? and tabCode=?) order by fields_table.fieldPriority asc";

			String selectQuery = "SELECT fields_table.data , mapping_table.listOfValues , mapping_table.data as pfmData FROM FIELDSYNC fields_table JOIN PRODUCTFIELDMAPPINGSYNC mapping_table ON fields_table.fieldCode=mapping_table.fieldCode WHERE (mapping_table.productCode=? and tabCode=?) and (fields_table.data like '%\"visibility for customer segment\":\""
					+ customerSegment
					+ "%' or fields_table.data like '%\"visibility for customer segment\":%,"
					+ customerSegment + "%') order by fields_table.fieldPriority asc";

			Cursor cursor = database.rawQuery(selectQuery, new String[] { productName, tabCode });

			if (isLogEnabled(Log.DEBUG))
			{
				debug("Fields Query : " + selectQuery, "productName : " + productName + " tabCode : " + tabCode
						+ " visiblityType : " + customerSegment);
			}

			try
			{
				if (cursor.moveToFirst())
				{
					do
					{
						//Log.d("Value"+dataRow.getCount(), "Data =========> "+dataRow.getString(dataRow.getColumnIndex("data")));
						//Log.d("Value"+dataRow.getCount(), "List =========> "+dataRow.getString(dataRow.getColumnIndex("listOfValues")));
						JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex("data")));
						jsonObject.put("listOfValues",
								new JSONArray(cursor.getString(cursor.getColumnIndex("listOfValues"))));

						JSONObject mappingData = new JSONObject(cursor.getString(cursor.getColumnIndex("pfmData")));
						if (mappingData.has("mandatory"))
						{
							jsonObject.put("mandatory", mappingData.get("mandatory"));
						}
						else
						{
							jsonObject.put("mandatory", "No");
						}

						fieldsList.add(jsonObject);
					}
					while (cursor.moveToNext());
				}
				cursor.close();

				connectionManager.closeDatabase();
			}
			catch (NullPointerException e)
			{
				e.printStackTrace(FileUtil.exceptionToFile());
				throw new RuntimeException("Id is not present storage");
			}
		}
		catch (Exception e)
		{
			MobiculeLogger.verbose("SQLException", " got exception =========> ");
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		return fieldsList;
	}

	public ArrayList<JSONObject> getAllFieldsForAllTabs(String productName, String inRangeString)
	{
		ArrayList<JSONObject> fieldsList = new ArrayList<JSONObject>();

		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			String tabsData = "SELECT fields_table.data, mapping_table.data as mappingdata FROM FIELDSYNC fields_table JOIN PRODUCTFIELDMAPPINGSYNC mapping_table ON fields_table.fieldCode=mapping_table.fieldCode WHERE (mapping_table.productCode=? and tabCode in ( "
					+ inRangeString + "))  order by fields_table.fieldPriority asc ";

			Cursor cursor = database.rawQuery(tabsData, new String[] { productName });

			if (isLogEnabled(Log.DEBUG))
			{
				debug("ALL FIELDS Details Query : " + tabsData, "For TAB'S : " + inRangeString);
			}

			try
			{
				if (cursor.moveToFirst())
				{
					do
					{
						JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex("data")));

						JSONObject mappingJson = new JSONObject(cursor.getString(cursor.getColumnIndex("mappingdata")));

						if (mappingJson.has("mandatory"))
						{
							jsonObject.put("mandatory", mappingJson.get("mandatory"));
						}
						else
						{
							jsonObject.put("mandatory", "No");
						}

						fieldsList.add(jsonObject);
					}
					while (cursor.moveToNext());
				}

				cursor.close();

				connectionManager.closeDatabase();
			}
			catch (NullPointerException e)
			{
				e.printStackTrace(FileUtil.exceptionToFile());
				throw new RuntimeException("Id is not present storage");
			}
		}
		catch (Exception e)
		{
			MobiculeLogger.verbose("SQLException", " got exception =========> ");
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		return fieldsList;
	}

	public ArrayList<String> getTabsDetails(String inRangeString)
	{
		ArrayList<String> tabsDetailsList = new ArrayList<String>();

		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			String tabsData = "select data from TABSYNC where tabCode in ( " + inRangeString
					+ ")  order by tabPriority asc ";

			Cursor cursor = database.rawQuery(tabsData, null);

			try
			{
				if (cursor.moveToFirst())
				{
					do
					{
						//Log.d("Value" + dataRow.getCount(), "=========> " + dataRow.getString(0));
						tabsDetailsList.add(cursor.getString(0));
					}
					while (cursor.moveToNext());
				}
				cursor.close();

				connectionManager.closeDatabase();
			}
			catch (NullPointerException e)
			{
				e.printStackTrace(FileUtil.exceptionToFile());
				throw new RuntimeException("Id is not present storage");
			}
		}
		catch (Exception e)
		{
			MobiculeLogger.verbose("SQLException", " got exception =========> ");
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		return tabsDetailsList;
	}

	public ArrayList<String> getDistinctFormTypeTabs(String productName, String formType)
	{

		ArrayList<String> tabsList = new ArrayList<String>();

		try
		{

			SQLiteDatabase database = connectionManager.openDatabase();

			Cursor mCursor = null;

			String selectQuery = "SELECT DISTINCT a.tabCode FROM FIELDSYNC a , PRODUCTFIELDMAPPINGSYNC b ,TABSYNC c WHERE a.fieldCode=b.fieldCode AND b.productCode='"
					+ productName
					+ "' AND  a.tabCode = c.tabCode AND c.data like '%\"formType\":\""
					+ formType
					+ "\"%'";

			mCursor = database.rawQuery(selectQuery, new String[] {});

			//Log.d("Value", "=========> got tabs ");
			//databaseAdapter.close();
			try
			{
				if (mCursor.moveToFirst())
				{
					do
					{
						MobiculeLogger.verbose("Value " + mCursor.getCount());
						MobiculeLogger.verbose("Value " + mCursor.getString(0));
						tabsList.add(mCursor.getString(0));
					}
					while (mCursor.moveToNext());
				}
				mCursor.close();

				connectionManager.closeDatabase();
			}
			catch (NullPointerException e)
			{
				e.printStackTrace(FileUtil.exceptionToFile());
				throw new RuntimeException("Id is not present storage");
			}
		}
		catch (Exception e)
		{
			MobiculeLogger.verbose("SQLException", " got exception =========> ");
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		MobiculeLogger.info("SQLITE TAB LIST DATA :-------" + tabsList);

		return tabsList;

	}

	public ArrayList<String> getDistinctTabs(String productName)
	{

		ArrayList<String> tabsList = new ArrayList<String>();

		try
		{

			SQLiteDatabase database = connectionManager.openDatabase();

			Cursor mCursor = null;

			String selectQuery = "SELECT DISTINCT tabCode FROM FIELDSYNC a JOIN PRODUCTFIELDMAPPINGSYNC b ON a.fieldCode=b.fieldCode WHERE b.productCode=?";

			mCursor = database.rawQuery(selectQuery, new String[] { productName });

			try
			{
				if (mCursor.moveToFirst())
				{
					do
					{
						MobiculeLogger.verbose("Value " + mCursor.getCount());
						MobiculeLogger.verbose("Value " + mCursor.getString(0));
						tabsList.add(mCursor.getString(0));
					}
					while (mCursor.moveToNext());
				}
				mCursor.close();

				connectionManager.closeDatabase();
			}
			catch (NullPointerException e)
			{
				e.printStackTrace(FileUtil.exceptionToFile());
				throw new RuntimeException("Id is not present storage");
			}
		}
		catch (Exception e)
		{
			MobiculeLogger.verbose("SQLException", " got exception =========> ");
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		MobiculeLogger.info("SQLITE TAB LIST DATA :-------" + tabsList);

		return tabsList;
	}

	@Override
	public String getData(String entity, int rowId)
	{
		Cursor dataRow;

		String data = null;

		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			dataRow = fetchRow(entity, database, rowId, SearchCondition.EQUALS_TO);

			connectionManager.closeDatabase();

			try
			{
				if (dataRow.moveToFirst())
				{
					data = dataRow.getString(dataRow.getColumnIndex("data"));
				}
				dataRow.close();
			}
			catch (NullPointerException e)
			{
				e.printStackTrace(FileUtil.exceptionToFile());
				throw new RuntimeException("Id is not present storage");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		return data;
	}

	public Cursor fetchRow(String entity, SQLiteDatabase database, long rowId, SearchCondition searchCondition)
	{
		String sql;
		Cursor mCursor = null;
		String operater = "=";

		if (searchCondition == SearchCondition.ALL)
		{
			mCursor = database.query(entity, new String[] { KEY_ROWID, KEY_DATA }, null, null, null, null, null);
		}
		else if (searchCondition == SearchCondition.LESS_THAN)
		{
			operater = " < ";
		}
		else if (searchCondition == SearchCondition.GREATER_THAN)
		{
			operater = " > ";
		}
		else if (searchCondition == SearchCondition.EQUALS_TO)
		{
			operater = " = ";
		}
		else if (searchCondition == SearchCondition.GREATER_EQUALS_TO)
		{
			operater = " >= ";
		}
		else if (searchCondition == SearchCondition.LESS_EQUALS_TO)
		{
			operater = " <= ";
		}

		sql = "SELECT * FROM " + entity + " WHERE " + KEY_ROWID + "" + operater + "" + rowId + ";";
		mCursor = database.rawQuery(sql, null);

		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	@Override
	public String getId(String entity, String data)
	{
		Cursor dataRow;
		String id = null;
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			dataRow = fetchRow(entity, database, data, SearchCondition.EQUALS_TO);

			connectionManager.closeDatabase();
			try
			{
				if (dataRow.moveToFirst())
				{
					id = "" + dataRow.getInt(dataRow.getColumnIndex("_id"));
				}
				dataRow.close();
			}
			catch (NullPointerException e)
			{
				id = null;
				e.printStackTrace(FileUtil.exceptionToFile());
				throw new RuntimeException("Data is not present in storage");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		return id;
	}

	public Cursor fetchRow(String entity, SQLiteDatabase database, String data, SearchCondition searchCondition)
	{
		String sql;
		Cursor mCursor = null;
		String operater = "=";

		if (searchCondition == SearchCondition.ALL)
		{
			mCursor = database.query(entity, new String[] { KEY_ROWID, KEY_DATA }, null, null, null, null, null);
		}
		else if (searchCondition == SearchCondition.LESS_THAN)
		{
			operater = " < ";
		}
		else if (searchCondition == SearchCondition.GREATER_THAN)
		{
			operater = " > ";
		}
		else if (searchCondition == SearchCondition.EQUALS_TO)
		{
			operater = " = ";
		}
		else if (searchCondition == SearchCondition.GREATER_EQUALS_TO)
		{
			operater = " >= ";
		}
		else if (searchCondition == SearchCondition.LESS_EQUALS_TO)
		{
			operater = " <= ";
		}

		sql = "SELECT * FROM " + entity + " WHERE " + KEY_DATA + "" + operater + "\"" + data + "\";";
		mCursor = database.rawQuery(sql, null);

		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	@Override
	public Cursor getTableNames()
	{
		Cursor tableName = null;
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			tableName = database
					.rawQuery(
							"SELECT name FROM sqlite_master WHERE type='table' and name not like 'sqlite_sequence'  and name not like 'android_metadata';",
							null);
			if (tableName != null)
			{
				tableName.moveToFirst();
			}

			connectionManager.closeDatabase();
		}
		catch (Exception e)
		{
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		return tableName;
	}

	@Override
	public void createTable(String entity)
	{
		try
		{

			if (entity.equalsIgnoreCase(""))
			{

			}
			else
			{
				SQLiteDatabase database = connectionManager.openDatabase();

				database.execSQL(open(entity));

				connectionManager.closeDatabase();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

	}

	private String open(String tableName)
	{
		String createTableStatement = "";

		MobiculeLogger.verbose("DataBaseHelper", "Inside createTable");

		if (tableName.equalsIgnoreCase(TABLE_CUSTOMER_INFO))
		{

			return createTableStatement = "CREATE TABLE IF NOT EXISTS " + TABLE_CUSTOMER_INFO
					+ " (_id INTEGER primary key autoincrement, " + TABLE_COLUMN_DATA + " Text not null,"
					+ TABLE_COLUMN_STATUS + " Text NOT NULL DEFAULT 'unattempted',"
					+ TABLE_COLUMN_BPNUMBER + " Text, "
					+ TABLE_COLUMN_METERNO + " Text, "
					+ TABLE_COLUMN_CONTACTNO + " Text, "
					+ TABLE_COLUMN_BUILDING_NAME + " Text, "
					+ TABLE_COLUMN_CONTACTNO_OFFICE + " Text, "
					+ TABLE_COLUMN_CONTACTNO_RES + " Text, "
					+ TABLE_COLUMN_CUSTOMERNAME + " Text, "
					+ TABLE_COLUMN_FLATNO + " Text, "
					+ TABLE_COLUMN_BOOKWALK_ID + " INTEGER, " + "FOREIGN KEY ("
					+ TABLE_COLUMN_BOOKWALK_ID + ") REFERENCES " + BOOKWALK_TABLE + "(_id));";
		}
		else
		{
			return createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName
					+ " (_id INTEGER primary key autoincrement, " + "data Text not null);";
		}

	}

	@Override
	public void renameTable(String oldName, String newName)
	{
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(oldName);

			database.execSQL("ALTER TABLE " + oldName + " RENAME TO " + newName + ";");

			connectionManager.closeDatabase();
		}
		catch (Exception e)
		{
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}
	}

	@Override
	public boolean updateLeadStatus(String entity, String leadStatus, int rowId)
	{
		MobiculeLogger.verbose("sqlitedatabaseadapter update  : ", entity);

		boolean status = false;
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			MobiculeLogger.verbose("updateLeadStatus in databaseadapter : ", leadStatus);

			ContentValues updateValues = new ContentValues();

			updateValues.put(KEY_LEAD_STATUS, leadStatus);

			status = database.update(entity, updateValues, KEY_ROWID + "=" + rowId, null) > 0;

			connectionManager.closeDatabase();

			return status;
		}
		catch (Exception e)
		{
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		return status;

	}

	@Override
	public boolean updateFIDataSubmissionStatus(String entity, String dataSubmissionStatus, int rowId)
	{
		MobiculeLogger.verbose("sqlitedatabaseadapter updateFIDataSubmissionStatus  : ", entity);

		boolean status = false;
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			MobiculeLogger.verbose("updateFIDataSubmissionStatus in  databaseadapter : ", "" + status);

			ContentValues updateValues = new ContentValues();

			updateValues.put(KEY_DATA_SUBMISSION_STATUS, dataSubmissionStatus);

			status = database.update(entity, updateValues, KEY_ROWID + "=" + rowId, null) > 0;

			connectionManager.closeDatabase();

			return status;
		}
		catch (Exception e)
		{
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		return status;

	}

	@Override
	public boolean updateFIImageSubmissionStatus(String entity, String imageSubmissionStatus, int rowId)
	{
		MobiculeLogger.verbose("sqlitedatabaseadapter updateFIDataSubmissionStatus  : ", entity);

		boolean status = false;
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			MobiculeLogger.verbose("updateFIImageSubmissionStatus in  databaseadapter : ", "" + status);

			ContentValues updateValues = new ContentValues();

			updateValues.put(KEY_IMAGE_SUBMISSION_STATUS, imageSubmissionStatus);

			status = database.update(entity, updateValues, KEY_ROWID + "=" + rowId, null) > 0;

			connectionManager.closeDatabase();

			return status;
		}
		catch (Exception e)
		{
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		return status;
	}

	@Override
	public boolean updateFIFormStatus(String entity, String formStatus, int rowId)
	{
		MobiculeLogger.verbose("sqlitedatabaseadapter updateFIDataSubmissionStatus  : ", entity);

		boolean status = false;
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			MobiculeLogger.verbose("updateFIFormStatus in  databaseadapter : ", "" + formStatus);

			ContentValues updateValues = new ContentValues();

			updateValues.put(KEY_FORM_STATUS, formStatus);

			status = database.update(entity, updateValues, KEY_ROWID + "=" + rowId, null) > 0;

			connectionManager.closeDatabase();

			return status;
		}
		catch (Exception e)
		{
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		return status;
	}

	@Override
	public boolean updateFIData(String entity, String fiData, int rowId)
	{
		MobiculeLogger.info("SQLiteDatabaseMAnager updateFIData fiData " + fiData + " entity " + entity);

		boolean status = false;
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			MobiculeLogger.verbose("updateFIFormStatus in  databaseadapter : ", fiData);

			ContentValues updateValues = new ContentValues();

			updateValues.put(KEY_FI_DATA, fiData);

			status = database.update(entity, updateValues, KEY_ROWID + "=" + rowId, null) > 0;

			MobiculeLogger.info("SQLiteDatabaseMAnager updateFIData status " + status);

			connectionManager.closeDatabase();

			return status;
		}
		catch (Exception e)
		{
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		return status;
	}

	@Override
	public Cursor executeRawQuery(String entity, String query)
	{

		Cursor list = null;
		//List<Map<Integer, String>> list = new Vector<Map<Integer, String>>();
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			MobiculeLogger.info("entity    : " + entity + "query    :" + query);

			MobiculeLogger.info("database.rawQuery(query, null)   :" + database.rawQuery(query, null));

			list = database.rawQuery(query, null);

			MobiculeLogger.verbose("return sarch result", "" + list.getCount());

			connectionManager.closeDatabase();

		}
		catch (Exception e)
		{
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		return list;

	}

	@Override
	public boolean updateFISubmissionDate(String entity, String submissionDate, int rowId)
	{
		MobiculeLogger.verbose("sqlitedatabaseadapter updateFISubmissionDate  : ", entity);

		boolean status = false;
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(entity);

			MobiculeLogger.verbose("updateLeadStatus in databaseadapter : ", submissionDate);

			ContentValues updateValues = new ContentValues();

			updateValues.put(KEY_SUBMISSION_DATE, submissionDate);

			status = database.update(entity, updateValues, KEY_ROWID + "=" + rowId, null) > 0;

			connectionManager.closeDatabase();

			return status;
		}
		catch (Exception e)
		{
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		return status;

	}

	public synchronized void insertInBulk(String tableName, JSONArray data)
	{
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable(tableName);

			try
			{
				if (data == null || data.isEmpty())
				{
					MobiculeLogger.debug(TAG, "insertInBulk data null ");
					return;
				}

				//MobiculeLogger.debug(TAG, "-------insertInBulk: " + tableName);

				database.beginTransaction();

				String sql = "Insert into " + tableName + " (" + KEY_DATA + ") values(?)"; //or Replace
				SQLiteStatement insert = database.compileStatement(sql);
				for (int i = 0; i < data.length(); i++)
				{
					//JSONParser jsonParser = JSONParser.getInstance();
					//jsonParser.setJson(data.get(i));

					insert.bindString(1, data.getJSONObject(i).toString());
					insert.execute();
				}
				database.setTransactionSuccessful();

				MobiculeLogger.debug(TAG, "**************insertInBulk********** Done");

			}
			catch (Exception e)
			{
				e.printStackTrace();
				MobiculeLogger.debug(TAG, "insertInBulk SQLException " + e.toString());
			}
			finally
			{
				database.endTransaction();
			}

			connectionManager.closeDatabase();
		}
		catch (Exception e)
		{
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}
	}

	@Override
	public boolean updateSyncTable(int rowId, String lastSyncDate)
	{
		MobiculeLogger.verbose("sqlitedatabaseadapter updateFISubmissionDate  : ", "SYNC");

		boolean status = false;
		try
		{
			SQLiteDatabase database = connectionManager.openDatabase();

			createTable("SYNC");

			MobiculeLogger.verbose("updateLeadStatus in databaseadapter : ", lastSyncDate);

			ContentValues updateValues = new ContentValues();

			updateValues.put(KEY_DATA, lastSyncDate);

			status = database.update("SYNC", updateValues, KEY_ROWID + "=" + rowId, null) > 0;

			connectionManager.closeDatabase();

			return status;
		}
		catch (Exception e)
		{
			e.printStackTrace(FileUtil.exceptionToFile());
			e.printStackTrace();
		}

		return status;

	}

}
