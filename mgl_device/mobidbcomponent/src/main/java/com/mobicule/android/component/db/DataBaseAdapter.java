package com.mobicule.android.component.db;

import static com.mobicule.android.component.logging.MobiculeLogger.debug;
import static com.mobicule.android.component.logging.MobiculeLogger.isLogEnabled;

import java.util.ArrayList;

import org.json.me.JSONArray;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.mobicule.android.component.logging.MobiculeLogger;

public class DataBaseAdapter implements IDatabaseAdapter
{
	public static final String TAG = "DataBaseAdapter";

	// Database fields
	public static final String KEY_ROWID = "_id";

	public static final String KEY_DATA = "data";

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

	private static final String LOCAL_LEAD = "localLeads";

	public static final String KEY_LIST_OF_VALUES = "listOfValues";

	public static final String ALLOCATED_LEADS = "allocatedLeadsSync";

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

	//----------------------

	private int DATABASE_VERSION = 1;

	private String databaseName;

	private String tableName;

	private String createTableStatement;

	private Context context;

	SQLiteDatabase database;

	private DataBaseOpenHelper dbHelper;

	public DataBaseAdapter(Context context, String dataBaseName)
	{
		this.context = context;
		databaseName = dataBaseName;
	}

	public IDatabaseAdapter open(String entity) throws SQLException
	{
		dbHelper = new DataBaseOpenHelper(context, databaseName, DATABASE_VERSION);

		MobiculeLogger.debug("databaseName: " + databaseName);

		if (database == null)
		{
			database = dbHelper.getWritableDatabase();
		}
		else if (database != null && !database.isOpen())
		{
			database = dbHelper.getWritableDatabase();
		}

		if (entity.equalsIgnoreCase(""))
		{

			return this;
		}
		else
		{
			tableName = entity;
			createTable(tableName);
		}

		return this;
	}

	public void close()
	{
		//dbHelper.close();
	}

	private void createTable(String tableName)
	{

		if (tableName.equalsIgnoreCase(ENTITY_TAB))
		{

			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName + " ( " + KEY_ROWID
					+ " INTEGER primary key autoincrement, " + KEY_TAB_CODE + " Text not null," + KEY_TAB_PRIORITY
					+ " INTEGER not null ," + KEY_DATA + " Text not null);";
		}
		else if (tableName.equalsIgnoreCase(ENTITY_FIELD))
		{

			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName + " ( " + KEY_ROWID
					+ " INTEGER primary key autoincrement, " + KEY_FIELD_CODE + " Text not null," + KEY_FIELD_PRIORITY
					+ " INTEGER not null ," + KEY_TAB_CODE + " Text not null," + KEY_DATA + " Text not null);";
		}
		else if (tableName.equalsIgnoreCase(ENTITY_PRODUCT_FIELD_MAPPING))
		{

			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName + " ( " + KEY_ROWID
					+ " INTEGER primary key autoincrement, " + KEY_PRODUCT_CODE + " Text not null," + KEY_FIELD_CODE
					+ " Text not null," + KEY_LIST_OF_VALUES + " Text not null," + KEY_DATA + " Text not null);";
		}

		else if (tableName.equalsIgnoreCase(LOCAL_LEAD))
		{
			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName
					+ "(_id INTEGER primary key autoincrement, " + "data Text not null," + KEY_LEAD_STATUS
					+ " Text not null," + KEY_SUBMISSION_STATUS + " Text not null," + KEY_CREATED_DATE
					+ " Text not null," + KEY_SUBMISSION_DATE + " Text );";
		}
		else if (tableName.equalsIgnoreCase(ALLOCATED_LEADS))
		{
			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName
					+ "(_id INTEGER primary key autoincrement, " + KEY_LEAD_STATUS + " Text not null," + KEY_DATA
					+ " Text not null," + KEY_SUBMISSION_DATE + " Text not null," + KEY_ERROR_MESSAGE + " Text );";
		}
		else if (tableName.equalsIgnoreCase(FIELD_INVESTIGATION))
		{
			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName
					+ "(_id INTEGER primary key autoincrement, " + KEY_DATA + " Text not null," + KEY_FI_DATA
					+ " Text not null," + KEY_IMAGE_PATH + " Text not null," + KEY_DATA_SUBMISSION_STATUS
					+ " Text not null," + KEY_IMAGE_SUBMISSION_STATUS + " Text not null," + KEY_FORM_STATUS
					+ " Text not null," + KEY_CREATED_DATE + " Text not null," + KEY_ERROR_MESSAGE + " Text );";
		}
		else if (tableName.equalsIgnoreCase(ALLOCATED_FI_SYNC))
		{
			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName
					+ "(_id INTEGER primary key autoincrement, " + KEY_LEAD_STATUS + " Text not null," + KEY_DATA
					+ " Text not null," + KEY_SUBMISSION_DATE + " Text not null," + KEY_ERROR_MESSAGE + " Text );";
		}
		else if (tableName.equalsIgnoreCase(FIELD_VERIFICATION))
		{
			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName
					+ "(_id INTEGER primary key autoincrement, " + KEY_DATA + " Text not null," + KEY_FI_DATA
					+ " Text not null," + KEY_DATA_SUBMISSION_STATUS + " Text not null," + KEY_FORM_STATUS
					+ " Text not null," + KEY_CREATED_DATE + " Text not null," + KEY_ERROR_MESSAGE + " Text );";
		}

		else if (tableName.equalsIgnoreCase(EMAIL_DATA_IMAGES))
		{
			createTableStatement = "CREATE TABLE IF NOT EXISTS "
					+ tableName
					+ "(_id INTEGER primary key autoincrement, "
					+ KEY_DATA
					+ " Text not null, leadId Text not null, email Text not null, imgPath Text not null, status Text not null)";
		}
		
		//
		else if (tableName.equalsIgnoreCase(STATUS_IMAGES))
		{
			MobiculeLogger.info("before create table");
			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName
					+ "(_id INTEGER primary key autoincrement, " + KEY_IMAGE_PATH + " Text not null," + KEY_IMAGE_NAME
					+ " Text not null," + KEY_STATUS + " Text not null," + KEY_REASON + " Text not null , "
					+ "data Text not null)";
			MobiculeLogger.info("after create table");
		}
		
		else if (tableName.equalsIgnoreCase(CIBIL_SCORE))
		{
			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName
					+ " (_id INTEGER primary key autoincrement , " + KEY_MOBI_ID + " Text not null, " + KEY_DATA
					+ " Text not null)";
			MobiculeLogger.verbose("Cibil table created.......");
		}
		else if (tableName.equalsIgnoreCase(CIBIL_DETAILS))
		{
			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName
					+ " (_id INTEGER primary key autoincrement , " + KEY_MOBI_ID + " Text not null, " + KEY_DATA
					+ " Text not null)";
			MobiculeLogger.verbose("Cibil table created.......");
		}
		
		else if (tableName.equalsIgnoreCase(TU_FI))
		{
			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName
					+ " (_id INTEGER primary key autoincrement , " + KEY_MOBI_ID + " Text not null, " + KEY_DATA
					+ " Text not null)";
			MobiculeLogger.verbose("tuFI table created.......");
		}
		else if (tableName.equalsIgnoreCase(TU_LEAD))
		{
			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName
					+ " (_id INTEGER primary key autoincrement , " + KEY_MOBI_ID + " Text not null, " + KEY_DATA
					+ " Text not null)";
			MobiculeLogger.verbose("Cibil table created.......");
		}
		
		/*else if (tableName.equalsIgnoreCase(TU_CIBIL))
		{
			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName
					+ " (_id INTEGER primary key autoincrement , leadId Text not null, " + KEY_DATA + " Text not null)";
		}
		else if (tableName.equalsIgnoreCase(TU_PDF))
		{
			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName
					+ " (_id INTEGER primary key autoincrement , leadId Text not null, " + KEY_DATA + " Text not null)";
		}
		else if (tableName.equalsIgnoreCase(TU_FI))
		{
			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName
					+ " (_id INTEGER primary key autoincrement , leadId Text not null, " + KEY_DATA + " Text not null)";
		}*/

		else
		{
			createTableStatement = "CREATE TABLE IF NOT EXISTS " + tableName
					+ " (_id INTEGER primary key autoincrement, " + "data Text not null);";
		}
		database.execSQL(createTableStatement);
	}

	public long insert(String data)
	{
		ContentValues initialValues = createContentValues(data);
		return database.insert(tableName, null, initialValues);
	}
	
	public long insert(String entity, String data)
	{
		ContentValues initialValues = createContentValues(data);
		return database.insert(entity, null, initialValues);
	}

	public long insert(String entity, String[] columnarray, String[] valuearray, String data)
	{
		ContentValues values = new ContentValues();

		values.put(KEY_DATA, data);

		for (int i = 0; i < columnarray.length; i++)
		{
			values.put(columnarray[i], valuearray[i]);
		}
		return database.insert(entity, null, values);
	}

	@Override
	public boolean update(String entity, String[] columnarray, String[] valuearray, String data, long rowId)
	{

		MobiculeLogger.verbose("new update data in databaseadapter : ", data);

		ContentValues updateValues = new ContentValues();

		updateValues.put(KEY_DATA, data);

		for (int i = 0; i < columnarray.length; i++)
		{
			updateValues.put(columnarray[i], valuearray[i]);
		}

		return database.update(entity, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean update(long rowId, String data)
	{
		MobiculeLogger.verbose("old update data in databaseadapter : ", data);

		ContentValues updateValues = createContentValues(data);

		return database.update(tableName, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean delete(long rowId)
	{
		return database.delete(tableName, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean delete(String entity ,long rowId)
	{
		return database.delete(entity, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllRows()
	{
		return database.query(tableName, new String[] { KEY_ROWID, KEY_DATA }, null, null, null, null, null);
	}

	public Cursor fetchRow(long rowId)
	{
		Cursor mCursor = database.query(true, tableName, new String[] { KEY_ROWID, KEY_DATA }, KEY_ROWID + "=" + rowId,
				null, null, null, null, null);

		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor search(Clause clause)
	{
		return database.rawQuery(createQuery(clause), null);
	}
	
	public Cursor search(String entity, Clause clause)
	{
		return database.rawQuery(createQuery(entity,clause), null);
	}

	private String createQuery(Clause clause)
	{
		boolean isFirstClause = true;

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT * FROM ");
		queryBuilder.append(tableName);

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
	

	public Cursor fetchRow(String key, String value, SearchCondition searchCondition)
	{

		Cursor mCursor = null;

		Clause clause = new Clause();

		QueryItem item = new QueryItem();
		item.setKey(key);
		item.setValue(value);
		item.setSearchCondition(searchCondition);

		clause.setLeftItem(item);

		createQuery(clause);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchRow(long rowId, SearchCondition searchCondition)
	{
		String sql;
		Cursor mCursor = null;
		String operater = "=";

		if (searchCondition == SearchCondition.ALL)
		{
			mCursor = fetchAllRows();
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

		sql = "SELECT * FROM " + tableName + " WHERE " + KEY_ROWID + "" + operater + "" + rowId + ";";
		mCursor = database.rawQuery(sql, null);

		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchRow(Clause clause)
	{
		Cursor mCursor = null;

		QueryItem currentQueryItem = clause.getLeftItem();
		Operator currentOperator = clause.getOperator();
		Clause nextClause = clause.getRightItem();

		String sqlQuery;

		if (nextClause == null)
		{
			String queryValue = currentQueryItem.getValue();
			String fieldName = currentQueryItem.getKey();
			SearchCondition searchCondition = currentQueryItem.getSearchCondition();

			if (queryValue == null || fieldName == null || searchCondition == null)
			{
				throw new RuntimeException("Mal Formed Query Exception.");
			}
			else
			{
				return fetchRow(null, queryValue, searchCondition);
			}
		}
		else
		{
			ArrayList<QueryItem> leftQueryItems = new ArrayList<QueryItem>();
			leftQueryItems.add(currentQueryItem);

			ArrayList<Operator> operators = new ArrayList<Operator>();
			operators.add(currentOperator);

			ArrayList<Clause> clauses = new ArrayList<Clause>();
			do
			{
				clauses.add(nextClause);
				nextClause = nextClause.getRightItem();
			}
			while (nextClause != null);

			int i = 0;
			
			while (i < clauses.size())
			{
				leftQueryItems.add(clauses.get(i).getLeftItem());
				operators.add(clauses.get(i).getOperator());
				i++;
			}
			
			MobiculeLogger.verbose("Object Representation", "" + operators.toString());
			MobiculeLogger.verbose("Query Items" + leftQueryItems.toString());

			sqlQuery = "SELECT * FROM " + tableName + " WHERE ";
			String subQuery[] = new String[leftQueryItems.size()];

			for (i = 0; i < leftQueryItems.size(); i++)
			{

				if (leftQueryItems.get(i).getSearchCondition() != null)
				{
					if (leftQueryItems.get(i).getSearchCondition() == SearchCondition.ENDS_WITH)
						subQuery[i] = leftQueryItems.get(i).getKey() + " LIKE '%" + leftQueryItems.get(i).getValue()
								+ "' ";
					else if (leftQueryItems.get(i).getSearchCondition() == SearchCondition.STARTS_WITH)
						subQuery[i] = leftQueryItems.get(i).getKey() + " LIKE '" + leftQueryItems.get(i).getValue()
								+ "%' ";
					else
						subQuery[i] = leftQueryItems.get(i).getKey() + " LIKE '%" + leftQueryItems.get(i).getValue()
								+ "%'";
				}

			}

			for (i = 0; i < subQuery.length; i++)
			{
				if (operators.get(i) != null)
					sqlQuery = sqlQuery + " " + subQuery[i] + " " + operators.get(i);
				else
					sqlQuery = sqlQuery + " " + subQuery[i];
			}

			sqlQuery = sqlQuery + " ;";

			MobiculeLogger.verbose("Object Representation", sqlQuery);

			mCursor = database.rawQuery(sqlQuery, null);

			return mCursor;
		}
	}

	public int getSize(String table)
	{
		Cursor mCursor = database.rawQuery("SELECT Count(*) FROM " + tableName + " ;", null);
		//int count = mCursor.getCount();
		int count = (int) DatabaseUtils.queryNumEntries(database, table);
		mCursor.close();
		return count;
	}

	public String[] fetchColumnNames()
	{
		return database.rawQuery("SELECT * FROM " + tableName + " ;", null).getColumnNames();
	}

	public void drop(String entity)
	{
		database.execSQL("DROP TABLE IF EXISTS " + entity + " ;");
	}

	private ContentValues createContentValues(String data)
	{
		ContentValues values = new ContentValues();
		values.put(KEY_DATA, data);
		return values;
	}

	@Override
	public Cursor fetchRow(String data, SearchCondition searchCondition)
	{
		String sql;
		Cursor mCursor = null;
		String operater = "=";

		if (searchCondition == SearchCondition.ALL)
		{
			mCursor = fetchAllRows();
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

		sql = "SELECT * FROM " + tableName + " WHERE " + KEY_DATA + "" + operater + "\"" + data + "\";";
		mCursor = database.rawQuery(sql, null);

		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	@Override
	public Cursor getAllTableNames()
	{
		dbHelper = new DataBaseOpenHelper(context, databaseName, DATABASE_VERSION);

		if (database != null && !database.isOpen())
		{
			database = dbHelper.getWritableDatabase();
		}

		Cursor mCursor = database
				.rawQuery(
						"SELECT name FROM sqlite_master WHERE type='table' and name not like 'sqlite_sequence'  and name not like 'android_metadata';",
						null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	@Override
	public void alterTableName(String oldName, String newName)
	{
		MobiculeLogger.info("Alter Table Called Here :");
		database.execSQL("ALTER TABLE " + oldName + " RENAME TO " + newName + ";");
	}

	@Override
	public Cursor getFieldsForTabs(String productName, String tabCode)
	{
		// TODO Auto-generated method stub

		String selectQuery = "SELECT fields_table.data , mapping_table.listOfValues, mapping_table.data as mappingdata FROM FIELDSYNC fields_table JOIN PRODUCTFIELDMAPPINGSYNC mapping_table ON fields_table.fieldCode=mapping_table.fieldCode WHERE (mapping_table.productCode=? and tabCode=?) order by fields_table.fieldPriority asc";
		Cursor cursor = database.rawQuery(selectQuery, new String[] { productName, tabCode });
		if (isLogEnabled(Log.DEBUG))
		{
			debug("Fields Query : " + selectQuery, "productName : " + productName + " tabCode : " + tabCode);
		}

		return cursor;
	}
	
	@Override
	public Cursor getFieldsForTBS013Tabs(String productName, String tabCode, String customerSegment)
	{
		// TODO Auto-generated method stub

		//String selectQuery = "SELECT fields_table.data , mapping_table.listOfValues FROM FIELDSYNC fields_table JOIN PRODUCTFIELDMAPPINGSYNC mapping_table ON fields_table.fieldCode=mapping_table.fieldCode WHERE (mapping_table.productCode=? and tabCode=?) order by fields_table.fieldPriority asc";
		//String selectQuery = "SELECT fields_table.data , mapping_table.listOfValues , mapping_table.data as pfmData FROM FIELDSYNC fields_table JOIN PRODUCTFIELDMAPPINGSYNC mapping_table ON fields_table.fieldCode=mapping_table.fieldCode WHERE (mapping_table.productCode=? and tabCode=?) order by fields_table.fieldPriority asc";

		String selectQuery = "SELECT fields_table.data , mapping_table.listOfValues , mapping_table.data as pfmData FROM FIELDSYNC fields_table JOIN PRODUCTFIELDMAPPINGSYNC mapping_table ON fields_table.fieldCode=mapping_table.fieldCode WHERE (mapping_table.productCode=? and tabCode=?) and (fields_table.data like '%\"visibility for customer segment\":\""
				+ customerSegment
				+ "%' or fields_table.data like '%\"visibility for customer segment\":%,"
				+ customerSegment
				+ "%') order by fields_table.fieldPriority asc";
		Cursor cursor = database.rawQuery(selectQuery, new String[] { productName, tabCode });
		if (isLogEnabled(Log.DEBUG))
		{
			debug("Fields Query : " + selectQuery, "productName : " + productName + " tabCode : " + tabCode
					+ " visiblityType : " + customerSegment);
		}

		return cursor;
	}

	@Override
	public Cursor getTabsDetails(String inRangeString)
	{
		// TODO Auto-generated method stub

		String tabsData = "select data from TABSYNC where tabCode in ( " + inRangeString
				+ ")  order by tabPriority asc ";

		//MobiculeLogger.debug("tabsData: "+tabsData);

		Cursor cursor = database.rawQuery(tabsData, null);
		/*if(isLogEnabled(Log.DEBUG))
		{
			debug("TAB Details Query : "+ tabsData, "For TAB'S : "+inRangeString);
		}*/
		return cursor;
	}

	public Cursor getDistinctTabs(String productName)
	{
		//Log.d("DatabaAdapter", "Inside DatabaseAdapter=========> ");
		Cursor mCursor = null;

		String selectQuery = "SELECT DISTINCT tabCode FROM FIELDSYNC a JOIN PRODUCTFIELDMAPPINGSYNC b ON a.fieldCode=b.fieldCode WHERE b.productCode=?";

		mCursor = database.rawQuery(selectQuery, new String[] { productName });

		/*if(isLogEnabled(Log.DEBUG))
		{
			debug("Distinct TAB'S Query : "+ selectQuery, "For Product : "+productName);
		}*/

		return mCursor;
	}
	
	public Cursor getDistinctFormTypeTabs(String productName, String formType)
	{
		//Log.d("DatabaAdapter", "Inside DatabaseAdapter=========> ");
		Cursor mCursor = null;

		String selectQuery = "SELECT DISTINCT a.tabCode FROM FIELDSYNC a , PRODUCTFIELDMAPPINGSYNC b ,TABSYNC c WHERE a.fieldCode=b.fieldCode AND b.productCode='"
				+ productName + "' AND  a.tabCode = c.tabCode AND c.data like '%\"formType\":\"" + formType + "\"%'";

		mCursor = database.rawQuery(selectQuery, new String[] {});

		/*if(isLogEnabled(Log.DEBUG))
		{
			debug("Distinct TAB'S Query : "+ selectQuery, "For Product : "+productName);
		}*/

		return mCursor;
	}

	@Override
	public Cursor getLeadIdList()
	{
		String selectQuery = "SELECT * from " + ALLOCATED_LEADS;

		Cursor cursor = database.rawQuery(selectQuery, null);

		if (isLogEnabled(Log.DEBUG))
		{
			debug("getLeadIdList Query : " + selectQuery);
		}

		return cursor;
	}

	@Override
	public Cursor getLocalLeadIdList()
	{
		String selectQuery = "SELECT * from " + LOCAL_LEAD;

		Cursor cursor = database.rawQuery(selectQuery, null);

		if (isLogEnabled(Log.DEBUG))
		{
			debug("getLocalLeadIdList Query : " + selectQuery);
		}

		return cursor;
	}

	@Override
	public boolean updateLeadStatus(int rowId, String leadStatus)
	{
		MobiculeLogger.verbose("updateLeadStatus in databaseadapter : ", leadStatus);
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_LEAD_STATUS, leadStatus);

		return database.update(tableName, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	@Override
	public boolean updateLeadStatus(String entity, int rowId, String leadStatus)
	{
		MobiculeLogger.verbose("updateLeadStatus in databaseadapter : ", leadStatus);
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_LEAD_STATUS, leadStatus);

		return database.update(entity, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}

	@Override
	public boolean updateFIDataSubmissionStatus(int rowId, String status)
	{
		MobiculeLogger.verbose("updateFIDataSubmissionStatus in  databaseadapter : ", status);
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_DATA_SUBMISSION_STATUS, status);

		return database.update(tableName, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	@Override
	public boolean updateFIDataSubmissionStatus(String entity ,int rowId, String status)
	{
		MobiculeLogger.verbose("updateFIDataSubmissionStatus in  databaseadapter : ", status);
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_DATA_SUBMISSION_STATUS, status);

		return database.update(entity, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}

	@Override
	public boolean updateFIImageSubmissionStatus(int rowId, String status)
	{
		MobiculeLogger.verbose("updateFIImageSubmissionStatus in  databaseadapter : ", status);
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_IMAGE_SUBMISSION_STATUS, status);

		return database.update(tableName, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	@Override
	public boolean updateFIImageSubmissionStatus(String entity,int rowId, String status)
	{
		MobiculeLogger.verbose("updateFIImageSubmissionStatus in  databaseadapter : ", status);
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_IMAGE_SUBMISSION_STATUS, status);

		return database.update(entity, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}

	@Override
	public boolean updateFIFormStatus(int rowId, String status)
	{
		MobiculeLogger.verbose("updateFIFormStatus in  databaseadapter : ", status);
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_FORM_STATUS, status);

		return database.update(tableName, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	@Override
	public boolean updateFIFormStatus(String entity, int rowId, String status)
	{
		MobiculeLogger.verbose("updateFIFormStatus in  databaseadapter : ", status);
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_FORM_STATUS, status);

		return database.update(entity, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}

	@Override
	public boolean updateFIData(int rowId, String fiData)
	{
		MobiculeLogger.info("DataBaseAdapter updateFIData fiData "+fiData);
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_FI_DATA, fiData);

		return database.update(tableName, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	@Override
	public boolean updateFIData(String entity,int rowId, String fiData)
	{
		MobiculeLogger.info("DataBaseAdapter updateFIData fiData "+fiData);
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_FI_DATA, fiData);

		return database.update(entity, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}

	@Override
	public Cursor getAllFieldsForAllTabs(String productName, String inRangeString)
	{

		String tabsData = "SELECT fields_table.data, mapping_table.data as mappingdata FROM FIELDSYNC fields_table JOIN PRODUCTFIELDMAPPINGSYNC mapping_table ON fields_table.fieldCode=mapping_table.fieldCode WHERE (mapping_table.productCode=? and tabCode in ( "
				+ inRangeString + "))  order by fields_table.fieldPriority asc ";

		Cursor cursor = database.rawQuery(tabsData, new String[] { productName });

		if (isLogEnabled(Log.DEBUG))
		{
			debug("ALL FIELDS Details Query : " + tabsData, "For TAB'S : " + inRangeString);
		}
		return cursor;
	}

	@Override
	public Cursor executeRawQuery(String entity, String query)
	{
		//		Cursor cursor = null;
		//		
		//		open(entity);
		//		
		//		
		//			cursor  = database.rawQuery(query,null);
		//		//close();
		MobiculeLogger.info("entity    : "+entity+   "query    :"+query);
		MobiculeLogger.info("database.rawQuery(query, null)   :"+database.rawQuery(query, null));
		return database.rawQuery(query, null);

	}

	@Override
	public boolean updateFiSubmissionDate(int rowId, String submissionDate)
	{
		MobiculeLogger.verbose("updateLeadStatus in databaseadapter : ", submissionDate);
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_SUBMISSION_DATE, submissionDate);

		return database.update(tableName, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	@Override
	public boolean updateFiSubmissionDate(String entity,int rowId, String submissionDate)
	{
		MobiculeLogger.verbose("updateLeadStatus in databaseadapter : ", submissionDate);
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_SUBMISSION_DATE, submissionDate);

		return database.update(entity, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}

	@Override
	public void insertInBulk(String tableName, JSONArray data)
	{
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
	}
	
	@Override
	public boolean updateSyncTable(int rowId, String lastSyncDate)
	{
		MobiculeLogger.verbose("updateLeadStatus in databaseadapter : ", lastSyncDate);
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_DATA, lastSyncDate);

		return database.update(tableName, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	@Override
	public boolean updateSyncTable(String entity, int rowId, String lastSyncDate)
	{
		MobiculeLogger.verbose("updateLeadStatus in databaseadapter : ", lastSyncDate);
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_DATA, lastSyncDate);

		return database.update(entity, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}

}
