package com.mobicule.android.msales.mgl.commons.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.mobicule.android.component.db.Clause;
import com.mobicule.android.component.db.Operator;
import com.mobicule.android.component.db.QueryItem;
import com.mobicule.android.component.db.SQLiteDatabaseManager;
import com.mobicule.android.component.db.SearchCondition;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.component.json.JSONUtil;
import com.mobicule.component.json.parser.IJSONParser;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.component.string.StringUtil;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.msales.mgl.client.application.IApplicationPersistence;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncePersistenceService;

/**
* 
* <enter description here>
*
* @author Sathya Sheela
* @see 
*
* @createdOn Mar 6, 2012
* @modifiedOn
*
* @copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class ApplicationPersistance implements IApplicationPersistence
{
	private final String TAG = "ApplicationPersistance";

	private static ApplicationPersistance instance = null;

	private SQLiteDatabaseManager databaseManager;

	private IJSONParser parser;

	private int unattemptedCount = 0;
	
	public static final String KEY_JT_SUBMIT = "jtSubmit";

	public static final String KEY_MR_SUBMIT = "mrSubmit";

	private static final String DATA = "data";

	private String searchQuery;

	public int getUnattemptedCount()
	{
		return unattemptedCount;
	}

	public void setUnattemptedCount(int unattemptedCount)
	{
		this.unattemptedCount = unattemptedCount;
	}

	private int completedCount = 0;

	public int getCompletedCount()
	{
		return completedCount;
	}

	public void setCompletedCount(int completedCount)
	{
		this.completedCount = completedCount;
	}

	public int getIncompleteCount()
	{ 
		return incompleteCount;
	}

	public void setIncompleteCount(int incompleteCount)
	{
		this.incompleteCount = incompleteCount;
	}

	private int incompleteCount = 0;  

	public ApplicationPersistance(Context context)
	{
		MobiculeLogger.verbose("!!! Application Persistance called...");
		databaseManager = (SQLiteDatabaseManager) SQLiteDatabaseManager.getInstance(context,Constants.DATABASE_NAME);
		databaseManager.createTable(CoreConstants.BOOKWALK_TABLE);
		databaseManager.createTable(CoreConstants.TABLE_CUSTOMER_INFO);
		databaseManager.createTable(CoreConstants.TABLE_SAVED_JOIN_TICKETING);
		databaseManager.createTable(CoreConstants.TABLE_JOIN_TICKETING_IMAGE);
		databaseManager.createTable(CoreConstants.TABLE_OnM_PLANNING);
		parser = JSONParser.getInstance();
	}
  
	public static IApplicationPersistence getApplicationPersistance(Context context)
	{
		if (instance == null)
		{
			instance = new ApplicationPersistance(context);
		}
		return instance;
	}

	private Vector getRecordStore(Vector<String> data, Cursor cursor)
	{
		while (cursor.moveToNext())
		{
			data.add(cursor.getString(cursor.getColumnIndex("data")));
		}
		return data;
	}

	@Override
	public String getUserDetail()
	{
		String data = "";
		Cursor cursor = null;
		try
		{
			cursor = databaseManager.search(Constants.USER_STORAGE, null);
			data = getRecord(cursor);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		MobiculeLogger.verbose("ApplicationPersistance", "getUserDetail	:" + data);
		return data;

	}

	private String getRecord(Cursor cursor)
	{
		String data = "";
		try
		{
			while (cursor.moveToNext())
			{
				data = cursor.getString(cursor.getColumnIndex("data"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public Vector load(String entity, String Key, String Value)
	{

		Vector<String> row = new Vector<String>();

		Cursor cursor = databaseManager.search(entity, new Clause(new QueryItem(Key, Value, SearchCondition.EQUALS),
				null, null));
		try
		{
			if (cursor.moveToFirst())
			{
				for (int i = 0; i < cursor.getCount(); i++)
				{
					row.add(cursor.getString(cursor.getColumnIndex("data")));
					MobiculeLogger.verbose("Persistance for table names DefaultSyncPersistenceService " + cursor.getString(cursor.getColumnIndex("data")));
					cursor.moveToNext();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return row;

	}

	@Override
	public Vector load(String entity)
	{
		String str;
		Vector<String> row = new Vector<String>();
		Cursor cursor = databaseManager.search(entity, null);
		try
		{
			MobiculeLogger.verbose("Persistance for load ApplicationPersistance", entity + " " + cursor.getCount());
			if (cursor.moveToFirst())
			{
				for (int i = 0; i < cursor.getCount(); i++)
				{
					str = cursor.getString(cursor.getColumnIndex("data"));
					if ((str != null) && (str.length() != 0))
					{
						row.add(str);
					}

					MobiculeLogger.verbose("Persistance for load ApplicationPersistance" +
							entity + " " + cursor.getString(cursor.getColumnIndex("data")));
					cursor.moveToNext();

				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return row;
	}

	@Override
	public Vector getCustomerListBasedOnMeterNo(String meterNo)
	{
		Vector<String> tableNames = getTableLists();

		Vector<String> data = new Vector<String>();

		Cursor cursor = null;
		try
		{
			//startwith query
			/*searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\"" + Constants.FIELD_METER_NO + "\":\"" + meterNo
					+ "%';";
			*/
			
			searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_METERNO + " LIKE '%" + meterNo + "%';";
			
			
			//searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE " + CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\"" + Constants.FIELD_METER_NO + "\":\"%" + meterNo + "%\"%';";
			cursor = databaseManager.executeRawQuery(CoreConstants.TABLE_CUSTOMER_INFO, searchQuery);
			if (cursor.getCount() > 0)
			{
				data = getRecordStoreWithConnObj(cursor);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return data;
	}

	public Vector<String> getCustomerListBasedOnConnObjFromSavedMeterReading(String connObj, String status)
	{
		Vector<String> savedMeterReading = new Vector<String>();
		//check for saved meter readings and not the submitted ones.
		/*Clause meterReadingClause = new Clause(new QueryItem(KEY_MR_SUBMIT, "0", SearchCondition.EQUALS), null, null);
		Clause statusClause = new Clause(new QueryItem(Constants.FIELD_STATUS, status, SearchCondition.EQUALS),
				null, null);
		Clause clause = new Clause(new QueryItem(Constants.FIELD_CONN_OBJ, connObj, SearchCondition.CONTAINS),
				null, null);*/
		/* 
				new Clause(new QueryItem(Constants.ITEM_CODE, itemCode, SearchCondition.CONTAINS),
						Operator.AND, 
						new Clause(new QueryItem(
				Constants.RETAILER_LIST, retailerCode, SearchCondition.CONTAINS), null, null)));*/

		Clause newclause = new Clause(new QueryItem(Constants.FIELD_CONN_OBJ, connObj, SearchCondition.CONTAINS),
				Operator.AND, new Clause(new QueryItem(Constants.FIELD_STATUS, status, SearchCondition.EQUALS), null,
						null));

		Cursor cursor = null;
		try
		{
			cursor = databaseManager.search(CoreConstants.TABLE_SAVED_METER_READING, newclause);

			if (cursor.getCount() > 0)
			{
				while (cursor.moveToNext())
				{

					savedMeterReading.add(cursor.getString(cursor.getColumnIndex(DATA)));
				}
			}
		}
		catch (NullPointerException e)
		{
			throw new RuntimeException("DATA NOT PRESENT");
		}
		finally
		{
			close(cursor);
		}
		return savedMeterReading;
	}

	public int find_idInDataBase(String entity, String conObj)
	{
		Cursor cursor = null;
		try
		{
			if (entity.equals(CoreConstants.BOOKWALK_TABLE))
			{
				cursor = databaseManager.search(entity, new Clause(new QueryItem(CoreConstants.FIELD_CONN_OBJ, conObj,
						SearchCondition.EQUALS), null, null));
				
				if (cursor.getCount() == 0)
				{
					MobiculeLogger.verbose("add "+ entity + " data : " + conObj);
					return -1;
				}
				else
				{
					if (cursor.moveToFirst())
					{
						MobiculeLogger.verbose("update "+ entity + " data : " + conObj);
						return cursor.getInt(cursor.getColumnIndex("_id"));
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return -1;
	}

	@Override
	public Vector<String> getCustomerListBasedOnConnObj(String connObj, String status)
	{
		Vector<String> tableNames = new Vector<String>();

		Vector<String> data = new Vector<String>();
		Cursor cursor = null;
		try
		{
			int bookwalk_id = find_idInDataBase(CoreConstants.BOOKWALK_TABLE, connObj);
			searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_BOOKWALK_ID + "='" + bookwalk_id + "' AND "
					+ CoreConstants.TABLE_COLUMN_STATUS + "='" + status + "';";

			cursor = databaseManager.executeRawQuery(CoreConstants.TABLE_CUSTOMER_INFO, searchQuery);
			if (cursor.getCount() > 0)
			{
				data = getRecordStore(data, cursor);
			}
			/*Clause statusClause = new Clause(new QueryItem(Constants.FIELD_STATUS, status, SearchCondition.EQUALS),
					null, null);
			Clause clause = new Clause(new QueryItem(Constants.FIELD_CONN_OBJ, connObj, SearchCondition.CONTAINS),
					null, null);
			cursor = databaseManager.search(IBookWalkSqncePersistenceService.BOOKWALK_TABLE, clause);
			if (cursor.getCount() > 0)
			{
				data = getRecordStore(data);
			}
			close(cursor);
			for (int i = 0; i < data.size(); i++)
			{
				parser.setJson(data.elementAt(i).toString());
				tableNames.add(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_"
						+ parser.getValue(Constants.FIELD_CONN_OBJ).toString());
			}
			data = new Vector<String>();
			for (int i = 0; i < tableNames.size(); i++)
			{
				String tableName = tableNames.elementAt(i).toString();
				if (tableName.indexOf(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_") != -1)
				{
					cursor = databaseManager.search(tableName, statusClause);
					if (cursor.getCount() > 0)
					{
						data = getRecordStore(data);
					}
					close(cursor);
				}
			}*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return data;
	}

	public void close(Cursor cursorObj)
	{
		if (cursorObj != null)
		{
			cursorObj.close();
			cursorObj = null;
		}
	}

	@Override
	public Vector getCustomerListBasedOnBuildingName(String buildingName)
	{
		Vector<String> tableNames = new Vector<String>();

		Vector<String> data = new Vector<String>();

		Cursor cursor = null;
		Cursor customerCursor = null;
		try
		{
			int bookwalk_id = 0;
			String connObj = "";

			//Startwith query
			searchQuery = "SELECT * FROM " + CoreConstants.BOOKWALK_TABLE + " WHERE " + CoreConstants.TABLE_COLUMN_DATA
					+ " LIKE '%\"" + Constants.FIELD_CONN_NAME + "\":\"" + buildingName + "%';";
			
			cursor = databaseManager.executeRawQuery(CoreConstants.BOOKWALK_TABLE, searchQuery);
			if (cursor.getCount() > 0)
			{
				while (cursor.moveToNext())
				{
					bookwalk_id = cursor.getInt(cursor.getColumnIndex("_id"));
					connObj = new JSONObject(cursor.getString(cursor.getColumnIndex("data")))
							.getString(Constants.FIELD_CONN_OBJ);
					searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
							+ CoreConstants.TABLE_COLUMN_BOOKWALK_ID + "='" + bookwalk_id + "';";
					customerCursor = databaseManager.executeRawQuery(CoreConstants.BOOKWALK_TABLE, searchQuery);
					if (customerCursor.getCount() > 0)
					{
						while (customerCursor.moveToNext())
						{

							JSONObject jsonObject = new JSONObject(customerCursor.getString(customerCursor
									.getColumnIndex("data")));
							jsonObject.put(Constants.FIELD_CONN_OBJ, connObj);
							data.add(jsonObject.toString());
						}

					}
					close(customerCursor);
				}
			}

			/*Clause clause = new Clause(
					new QueryItem(Constants.FIELD_CONN_NAME, buildingName, SearchCondition.CONTAINS), null, null);
			cursor = databaseManager.search(IBookWalkSqncePersistenceService.BOOKWALK_TABLE, clause);
			if (cursor.getCount() > 0)
			{
				data = getRecordStore(data);
			}
			close(cursor);
			for (int i = 0; i < data.size(); i++)
			{
				parser.setJson(data.elementAt(i).toString());
				tableNames.add(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_"
						+ parser.getValue(Constants.FIELD_CONN_OBJ).toString());
			}
			data = new Vector<String>();
			for (int i = 0; i < tableNames.size(); i++)
			{
				String tableName = tableNames.elementAt(i).toString();
				if (tableName.indexOf(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_") != -1)
				{
					cursor = databaseManager.search(tableName, null);
					if (cursor.getCount() > 0)
					{
						data = getRecordStore(data);
					}
					close(cursor);
				}
			}*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
			close(customerCursor);
		}
		return data;
	}

	@Override
	public Vector searchCustomerListBasedOnBuildingName(String buildingName)
	{
		Vector<String> tableNames = new Vector<String>();

		Vector<String> data = new Vector<String>();

		Cursor cursor = null;
		Cursor customerCursor = null;
		try
		{
			int bookwalk_id = 0;
			String connObj = "";

			//Startwith query
			searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE " + CoreConstants.TABLE_COLUMN_BUILDING_NAME
					+ " LIKE '%" + buildingName + "%';";
			
			cursor = databaseManager.executeRawQuery(CoreConstants.BOOKWALK_TABLE, searchQuery);
			if (cursor.getCount() > 0)
			{
				while (cursor.moveToNext())
				{
					data = getRecordStoreWithConnObj(cursor);
				}
			}

			/*Clause clause = new Clause(
					new QueryItem(Constants.FIELD_CONN_NAME, buildingName, SearchCondition.CONTAINS), null, null);
			cursor = databaseManager.search(IBookWalkSqncePersistenceService.BOOKWALK_TABLE, clause);
			if (cursor.getCount() > 0)
			{
				data = getRecordStore(data);
			}
			close(cursor);
			for (int i = 0; i < data.size(); i++)
			{
				parser.setJson(data.elementAt(i).toString());
				tableNames.add(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_"
						+ parser.getValue(Constants.FIELD_CONN_OBJ).toString());
			}
			data = new Vector<String>();
			for (int i = 0; i < tableNames.size(); i++)
			{
				String tableName = tableNames.elementAt(i).toString();
				if (tableName.indexOf(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_") != -1)
				{
					cursor = databaseManager.search(tableName, null);
					if (cursor.getCount() > 0)
					{
						data = getRecordStore(data);
					}
					close(cursor);
				}
			}*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
			close(customerCursor);
		}
		return data;
	}

	
	@Override
	public Vector<String> getBuildingList(String status)
	{
		Vector<String> data = new Vector<String>();
		Vector<String> tempV = new Vector<String>();
		Cursor cursor = null;
		try
		{
			//select count(c._id), b.data, b._id from BOOKWALK as b, CUSTOMER_INFO as c where b._id=c.bookwalk_id and c.status="unattempted" group by b.data, b._id
			//searchQuery = "select count(c._id) as count, b.data, b._id from BOOKWALK as b, CUSTOMER_INFO as c where b._id=c.bookwalk_id and c.status=\""+status+"\" group by b.data, b._id";
			searchQuery = "select count(c._id) as count, b.data, b._id from BOOKWALK as b, CUSTOMER_INFO as c where b._id=c.bookwalk_id and c.status='"
					+ status + "' group by b.data, b._id order by b._id";

			cursor = databaseManager.executeRawQuery(CoreConstants.BOOKWALK_TABLE, searchQuery);
			if (cursor.getCount() > 0)
			{
				data = getRecordStore(data, cursor);

				cursor.moveToFirst();
				for (int i = 0; i < data.size(); i++)
				{

					JSONObject jsonObject = new JSONObject(data.elementAt(i).toString());
					
					MobiculeLogger.verbose("Application Persistance  jsonobject is ::  " + jsonObject.toString());
					
					JSONUtil.setJSONProperty(jsonObject, Constants.FIELD_CUSTOMER_COUNT,cursor.getString(cursor.getColumnIndex("count")));
					
					MobiculeLogger.verbose("Constants.FIELD_CUSTOMER_COUNT    ::::   " + Constants.FIELD_CUSTOMER_COUNT);
					
					MobiculeLogger.verbose("cursor.getString(cursor.getColumnIndex()  :::::  "+ cursor.getString(cursor.getColumnIndex("count")));
					
					//data.remove(data.lastElement());
					tempV.add(jsonObject.toString());
					cursor.moveToNext();
				}

			}

			/*Vector<String> tableNames = getTableLists();
			int index = IBookWalkSqncePersistenceService.BOOKWALK_TABLE.length() + 1;
			Clause customerListclause = new Clause(
					new QueryItem(Constants.FIELD_STATUS, status, SearchCondition.EQUALS), null, null);
			Clause bookWalkClause;
			for (int i = 0; i < tableNames.size(); i++)
			{
				String tableName = tableNames.elementAt(i).toString();
				if (tableName.indexOf(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_") != -1)
				{
					cursor = databaseManager.search(tableName, customerListclause);
					int customerCount = cursor.getCount();
					if (customerCount > 0)
					{
						String connObj = tableName.substring(index);
						bookWalkClause = new Clause(new QueryItem(Constants.FIELD_CONN_OBJ, connObj,
								SearchCondition.EQUALS), null, null);
						close(cursor);
						cursor = databaseManager
								.search(IBookWalkSqncePersistenceService.BOOKWALK_TABLE, bookWalkClause);
						if (cursor.getCount() > 0)
						{
							data = getRecordStore(data);
							JSONObject jsonObject = new JSONObject(data.lastElement().toString());
							JSONUtil.setJSONProperty(jsonObject, Constants.FIELD_CUSTOMER_COUNT, customerCount);
							data.remove(data.lastElement());
							data.add(jsonObject.toString());
						}
					}
					close(cursor);
				}
			}*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return tempV;
	}

	public Vector<String> getTableLists()
	{
		Vector<String> tableNames = new Vector<String>();
		Cursor cursor = null;
		cursor = databaseManager.getTableNames();
		MobiculeLogger.verbose("NUMBER OF TABLES", " COUNT = " + cursor.getCount());
		try
		{
			if (cursor.moveToFirst())
			{
				for (int i = 0; i < cursor.getCount(); i++)
				{
					tableNames.add(cursor.getString(cursor.getColumnIndex("name")));
					cursor.moveToNext();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		MobiculeLogger.verbose("tablesNames", tableNames.toString());
		return tableNames;
	}

	@Override
	public Vector getCustomerListBasedOnBPNO(String bpNO)
	{
		Vector<String> tableNames = getTableLists();

		Vector data = new Vector<String>();
		Cursor cursor = null;
		try
		{
			/*searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\"" + Constants.FIELD_PLOT_NO + "\":%" + bpNO + "%';";*/
			/*searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\"" + Constants.FIELD_BP_NO + "\":\"" + bpNO + "%';";*/

			searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_BPNUMBER + " LIKE '%" + bpNO + "%';";
			
			cursor = databaseManager.executeRawQuery(CoreConstants.TABLE_CUSTOMER_INFO, searchQuery);
			if (cursor.getCount() > 0)
			{
				data = getRecordStoreWithConnObj(cursor);
			}
			/*Clause clause = new Clause(new QueryItem(Constants.FIELD_BP_NO, bpNO, SearchCondition.CONTAINS), null, null);
			for (int i = 0; i < tableNames.size(); i++)
			{
				if (tableName.indexOf(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_") != -1)
				{
					cursor = databaseManager.search(tableName, clause);
					if (cursor.getCount() > 0)
					{
						data = getRecordStore(data);
					}
					close(cursor);
				}
			}
			data = startsWith(data, bpNO, Constants.FIELD_BP_NO);*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}

		return data;
	}

	@Override
	public Vector getCustomerListBasedOnCustomerFlatNo(String flatNo)
	{
		Vector<String> tableNames = getTableLists();

		Vector data = new Vector<String>();
		Cursor cursor = null;
		try
		{
			/*searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\"" + Constants.FIELD_FLAT_NO + "\":\"" + flatNo
					+ "%';";
*/
			
			searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_FLATNO + " LIKE '%" + flatNo + "%';";

			
			/*searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\"" + Constants.FIELD_FLAT_NO + "\":%"
					+ flatNo + "%';";*/
			cursor = databaseManager.executeRawQuery(CoreConstants.TABLE_CUSTOMER_INFO, searchQuery);
			if (cursor.getCount() > 0)
			{
				data = getRecordStoreWithConnObj(cursor);
			}
			/*Clause clause = new Clause(new QueryItem(Constants.FIELD_BP_NO, bpNO, SearchCondition.CONTAINS), null, null);
			for (int i = 0; i < tableNames.size(); i++)
			{
				String tableName = tableNames.elementAt(i).toString();
				if (tableName.indexOf(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_") != -1)
				{
					cursor = databaseManager.search(tableName, clause);
					if (cursor.getCount() > 0)
					{
						data = getRecordStore(data);
					}
					close(cursor);
				}
			}
			data = startsWith(data, bpNO, Constants.FIELD_BP_NO);*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}

		return data;
	}

	@Override
	public Vector getCustomerListBasedOnContactNo(String contactNo)
	{
		Vector<String> tableNames = getTableLists();

		Vector<String> data = new Vector<String>();

		Cursor cursor = null;
		try
		{
			/*searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\"" + Constants.FIELD_CONTACT_NO + "\":\"" + contactNo
					+ "%';";*/
			/*searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\"" + Constants.FIELD_CONTACT_NO + "\":\"" + contactNo
					+ "%' OR " + CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\"" + Constants.KEY_CUSTOMER_OFFICE_NUMBER
					+ "\":\"" + contactNo + "%' OR " + CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\""
					+ Constants.KEY_CUSTOMER_LANDLINE_NUMBER + "\":\"" + contactNo + "%';";*/
			
			searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_CONTACTNO + " LIKE '%" + contactNo
					+ "%' OR " + CoreConstants.TABLE_COLUMN_CONTACTNO_OFFICE + " LIKE '%" + contactNo + "%' OR " + CoreConstants.TABLE_COLUMN_CONTACTNO_RES + " LIKE '%"
				    + contactNo + "%';";
			
			cursor = databaseManager.executeRawQuery(CoreConstants.TABLE_CUSTOMER_INFO, searchQuery);
			if (cursor.getCount() > 0)
			{
				data = getRecordStoreWithConnObj(cursor);
			}
			/*Clause clause = new Clause(new QueryItem(Constants.FIELD_CONTACT_NO, contactNo, SearchCondition.CONTAINS),
					null, null);
			for (int i = 0; i < tableNames.size(); i++)
			{
				String tableName = tableNames.elementAt(i).toString();
				if (tableName.indexOf(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_") != -1)
				{
					cursor = databaseManager.search(tableName, clause);
					if (cursor.getCount() > 0)
					{
						data = getRecordStore(data);
					}
					close(cursor);
				}
			}
			data = startsWith(data, contactNo, Constants.FIELD_CONTACT_NO);*/  
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}

		return data;
	}

	public Vector startsWith(Vector data, String contactNo, String key)
	{
		JSONObject jsonObject;
		Vector sortedV = new Vector();
		MobiculeLogger.verbose("Application persistance  ", "startsWith     " + data);
		try
		{
			for (int i = 0; i < data.size(); i++)
			{
				jsonObject = new JSONObject(data.elementAt(i).toString());
				String contactNumber = jsonObject.getString(key);
				if (contactNumber.startsWith(contactNo))
				{
					sortedV.add(data.elementAt(i).toString());
				}
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		return sortedV;

	}

/*	public Vector<String> getRecordStoreWithConnObj(Cursor cursor,String customerName)
	{
		Vector<String> customerV = new Vector<String>();
		try
		{
			while (cursor.moveToNext())
			{
				JSONObject jsonObject1 = null;
				JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex("data")).toString());
				int bookwalk_id = cursor.getInt(cursor.getColumnIndex(CoreConstants.TABLE_COLUMN_BOOKWALK_ID));
				searchQuery = "SELECT * FROM " + CoreConstants.BOOKWALK_TABLE + " WHERE _id=" + bookwalk_id + ";";
				Cursor bookwalkCursor = databaseManager.executeSearchQuary(CoreConstants.BOOKWALK_TABLE,
						CoreConstants.CREATE_BOOKWALK_TABLE, searchQuery);
				if (bookwalkCursor.getCount() > 0)
				{
					bookwalkCursor.moveToNext();
					jsonObject1 = new JSONObject(bookwalkCursor.getString(bookwalkCursor.getColumnIndex("data"))
							.toString());
					jsonObject.put(Constants.FIELD_CONN_OBJ, jsonObject1.getString(Constants.FIELD_CONN_OBJ));
				}
				close(bookwalkCursor);
				String customerNameFromDb = jsonObject.getString(Constants.FIELD_CUSTOMER_NAME);
				if(customerNameFromDb.toLowerCase().contains(customerName.toLowerCase())){
				customerV.add(jsonObject.toString());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return customerV;
	}*/
	
	public Vector<String> getRecordStoreWithConnObj(Cursor cursor)
	{
		Vector<String> customerV = new Vector<String>();
		try
		{
			while (cursor.moveToNext())
			{
				JSONObject jsonObject1 = null;
				JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex("data")).toString());
				int bookwalk_id = cursor.getInt(cursor.getColumnIndex(CoreConstants.TABLE_COLUMN_BOOKWALK_ID));
				searchQuery = "SELECT * FROM " + CoreConstants.BOOKWALK_TABLE + " WHERE _id=" + bookwalk_id + ";";
				Cursor bookwalkCursor = databaseManager.executeRawQuery(CoreConstants.BOOKWALK_TABLE, searchQuery);
				if (bookwalkCursor.getCount() > 0)
				{
					bookwalkCursor.moveToNext();
					jsonObject1 = new JSONObject(bookwalkCursor.getString(bookwalkCursor.getColumnIndex("data"))
							.toString());
					jsonObject.put(Constants.FIELD_CONN_OBJ, jsonObject1.getString(Constants.FIELD_CONN_OBJ));
					MobiculeLogger.verbose("ApplicationPersistanc", "search connObj	:" + jsonObject1.getString(Constants.FIELD_CONN_OBJ));
				}
				close(bookwalkCursor);
				customerV.add(jsonObject.toString());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return customerV;
	}

	@Override
	public Vector getCustomerListBasedOnCustomerName(String customerName)
	{
		Vector<String> tableNames = getTableLists();

		Vector<String> data = new Vector<String>();

		Cursor cursor = null;
		try
		{
			/*searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\"" + Constants.FIELD_CUSTOMER_NAME + "\":\""
					+ customerName + "%';";*/
			searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_CUSTOMERNAME + " LIKE '%" + customerName + "%';";
		
			/*searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\"" + Constants.FIELD_CUSTOMER_NAME + "\":\"%"
					+ customerName + "%\"%';";*/
			//searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + ";";
			
			
			cursor = databaseManager.executeRawQuery(CoreConstants.TABLE_CUSTOMER_INFO, searchQuery);
			if (cursor.getCount() > 0)
			{
				data = getRecordStoreWithConnObj(cursor);
			}
			Enumeration e=data.elements();

			   // let us print all the elements available in enumeration
			   while (e.hasMoreElements()) {         
			   MobiculeLogger.verbose("Number = " + e.nextElement());
			   }  
		/*	cursor.moveToFirst();
			do{
				String cursorData = cursor.getString(cursor.getColumnIndex(CoreConstants.TABLE_COLUMN_DATA));
				String cusrsorBookwalkId = cursor.getString(cursor.getColumnIndex(CoreConstants.TABLE_COLUMN_BOOKWALK_ID));
				JSONObject cursorDataJsonObject = new JSONObject(cursorData);
				String customerNameFromCursor = cursorDataJsonObject.getString(Constants.FIELD_CUSTOMER_NAME);
				if(customerNameFromCursor.equals(customerName)){
					customerList.add(cursorDataJsonObject);
				}
			}while(cursor.moveToNext());

			if(customerList.size() > 0){
				data = getRecordStoreWithConnObj(customerList);
			}*/
			/*Clause clause = new Clause(new QueryItem(Constants.FIELD_CUSTOMER_NAME, customerName,
					SearchCondition.CONTAINS), null, null);
			for (int i = 0; i < tableNames.size(); i++)
			{
				String tableName = tableNames.elementAt(i).toString();
				if (tableName.indexOf(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_") != -1)
				{
					cursor = databaseManager.search(tableName, clause);
					if (cursor.getCount() > 0)
					{
						data = getRecordStore(data);
					}
					close(cursor);
				}
			}*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return data;
	}

	@Override
	public int getBookWalkCount(String status)
	{
		int count = 0;
		Cursor cursor = null;
		try
		{
			searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_STATUS + "='" + status + "';";
			cursor = databaseManager.executeRawQuery(CoreConstants.TABLE_CUSTOMER_INFO, searchQuery);
			if (cursor.getCount() > 0)
			{
				count = cursor.getCount();
			}
			/*Vector<String> tableNames = getTableLists();
			Clause clause = new Clause(new QueryItem(Constants.FIELD_STATUS, status, SearchCondition.EQUALS), null,
					null);
			for (int i = 0; i < tableNames.size(); i++)
			{
				String tableName = tableNames.elementAt(i).toString();
				if (tableName.indexOf(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_") != -1)
				{
					cursor = databaseManager.search(tableName, clause);
					count += cursor.getCount();
					close(cursor);
				}
			}*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return count;
	}

	public int calculateBookWalkCountBasedOnStatus()
	{
		int count = 0;
		Cursor cursor = null;
		try
		{
			Vector<String> tableNames = getTableLists();
			Vector<String> data = new Vector<String>();
			unattemptedCount = 0;
			completedCount = 0;
			incompleteCount = 0;

			searchQuery = "select count(_id) as id,status from customer_info group by status";
			if (getTableLists().contains(CoreConstants.BOOKWALK_TABLE)
					&& getTableLists().contains(CoreConstants.TABLE_CUSTOMER_INFO))
			{
				cursor = databaseManager.executeRawQuery(CoreConstants.BOOKWALK_TABLE, searchQuery);

				if (cursor.getCount() > 0)
				{

					while (cursor.moveToNext())
					{
						String status = cursor.getString(cursor.getColumnIndex("status"));
						if (status.equals(Constants.FIELD_COMPLETED))
						{
							completedCount = cursor.getInt(cursor.getColumnIndex("id"));
						}
						if (status.equals(Constants.FIELD_UNATTEMPTED))
						{
							unattemptedCount = cursor.getInt(cursor.getColumnIndex("id"));
						}
						if (status.equals(Constants.FIELD_INCOMPLETE))
						{
							incompleteCount = cursor.getInt(cursor.getColumnIndex("id"));
						}
					}
				}
			}
			/*searchQuery = "SELECT * FROM "+ CoreConstants.TABLE_CUSTOMER_INFO +" WHERE "+CoreConstants.TABLE_COLUMN_STATUS+"='"+Constants.FIELD_UNATTEMPTED+"';";
			cursor = databaseManager.executeSearchQuary(CoreConstants.TABLE_CUSTOMER_INFO, CoreConstants.CREATE_CUSTOMER_INFO, searchQuery);
			if (cursor.getCount() >0)
			{
				unattemptedCount = cursor.getCount();
			}
			close(cursor);
			
			searchQuery = "SELECT * FROM "+ CoreConstants.TABLE_CUSTOMER_INFO +" WHERE "+CoreConstants.TABLE_COLUMN_STATUS+"='"+Constants.FIELD_COMPLETED+"';";
			cursor = databaseManager.executeSearchQuary(CoreConstants.TABLE_CUSTOMER_INFO, CoreConstants.CREATE_CUSTOMER_INFO, searchQuery);
			if (cursor.getCount() >0)
			{
				completedCount = cursor.getCount();
			}
			close(cursor);
			
			searchQuery = "SELECT * FROM "+ CoreConstants.TABLE_CUSTOMER_INFO +" WHERE "+CoreConstants.TABLE_COLUMN_STATUS+"='"+Constants.FIELD_INCOMPLETE+"';";
			cursor = databaseManager.executeSearchQuary(CoreConstants.TABLE_CUSTOMER_INFO, CoreConstants.CREATE_CUSTOMER_INFO, searchQuery);
			if (cursor.getCount() >0)
			{
				incompleteCount = cursor.getCount();
			}
			close(cursor);*/
			/*String bookwalkStatus = "";
			for (int i = 0; i < tableNames.size(); i++)
			{
				String tableName = tableNames.elementAt(i).toString();
				if (tableName.indexOf(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_") != -1)
				{
					data = new Vector();
					cursor = databaseManager.search(tableName, null);
					data = getRecordStore(data);
					close(cursor);
					for (int j = 0; j < data.size(); j++)
					{
						parser.setJson(data.elementAt(j).toString());
						bookwalkStatus = parser.getValue(Constants.FIELD_STATUS);
						if (bookwalkStatus.equals(Constants.FIELD_UNATTEMPTED))
						{
							unattemptedCount += 1;
						}
						else if (bookwalkStatus.equals(Constants.FIELD_COMPLETED))
						{
							completedCount += 1;
						}
						else if (bookwalkStatus.equals(Constants.FIELD_INCOMPLETE))
						{
							incompleteCount += 1;
						}
					}
				}
			}*/
			count = unattemptedCount + completedCount + incompleteCount;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return count;
	}

	public Vector<String> addConnectionObj(String connObj, Vector<String> searchCoustomerV)
	{
		Vector<String> dataV = new Vector<String>();
		for (int i = 0; i < searchCoustomerV.size(); i++)
		{
			try
			{
				JSONObject jsonObject = new JSONObject(searchCoustomerV.elementAt(i).toString());
				jsonObject.put(Constants.FIELD_CONN_OBJ, connObj);
				dataV.add(jsonObject.toString());
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		return dataV;
	}

	@Override
	public int getTotalBookWalkCount()
	{
		int count = 0;
		Cursor cursor = null;
		try
		{
			searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + ";";
			cursor = databaseManager.executeRawQuery(CoreConstants.TABLE_CUSTOMER_INFO, searchQuery);
			if (cursor.getCount() > 0)
			{
				count = cursor.getCount();
			}
			/*Vector<String> tableNames = getTableLists();
			for (int i = 0; i < tableNames.size(); i++)
			{
				String tableName = tableNames.elementAt(i).toString();
				if (tableName.indexOf(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_") != -1)
				{
					cursor = databaseManager.search(tableName, null);
					count += cursor.getCount();
					close(cursor);
				}
			}*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return count;
	}

	@Override
	public void updateBookWalkStatus(String data)
	{
		try
		{
			parser.setJson(data);
			String connObj = parser.getValue(Constants.FIELD_CONN_OBJ);
			String meterReadingStatus = parser.getValue(Constants.FIELD_STATUS);
			//String bpNo = parser.getValue(Constants.FIELD_BP_NO);
			String mroNo = parser.getValue(CoreConstants.FIELD_MRO_NO);
			MobiculeLogger.verbose("updateBookWalkStatus	connObj:" + connObj + "	status:" + meterReadingStatus);
			if (StringUtil.isValid(connObj))
			{
				updateCustomerTable(connObj, meterReadingStatus, mroNo);
				//				updateBookWalkTable(connObj);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void updateCustomerTable(String connObj, String meterReadingStatus, String mroNo) throws JSONException
	{
		Cursor cursor = null;
		try
		{
			JSONObject dataJson = null;
			Vector updateData = new Vector<String>();
			String data;

			if (StringUtil.isValid(meterReadingStatus))
			{
				// to replace status in bookwalk_connObj table with the meter reading status
				int bookwalk_id = 0;
				int row_id = 0;
				String customerData = "";
				searchQuery = "SELECT * FROM " + CoreConstants.BOOKWALK_TABLE + " WHERE "
						+ CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\"" + Constants.FIELD_CONN_OBJ + "\":%" + connObj
						+ "%';";
				cursor = databaseManager.executeRawQuery(CoreConstants.BOOKWALK_TABLE, searchQuery);
				if (cursor.getCount() > 0)
				{
					while (cursor.moveToNext())
					{
						bookwalk_id = cursor.getInt(cursor.getColumnIndex("_id"));
					}
				}
				close(cursor);
				searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
						+ CoreConstants.TABLE_COLUMN_BOOKWALK_ID + "='" + bookwalk_id + "' AND "
						+ CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\"" + CoreConstants.FIELD_MRO_NO + "\":%" + mroNo
						+ "%';";
				cursor = databaseManager.executeRawQuery(CoreConstants.TABLE_CUSTOMER_INFO, searchQuery);
				if (cursor.getCount() > 0)
				{
					cursor.moveToNext();
					row_id = cursor.getInt(cursor.getColumnIndex("_id"));
					customerData = cursor.getString(cursor.getColumnIndex("data"));
				}
				close(cursor);
				
				MobiculeLogger.verbose("customerData :: "+customerData);
				
				JSONObject jsonObject = new JSONObject(customerData);
				jsonObject.put(Constants.FIELD_STATUS, meterReadingStatus);
				ContentValues contentValues = new ContentValues();
				contentValues.put(CoreConstants.TABLE_COLUMN_STATUS, meterReadingStatus);
				contentValues.put(CoreConstants.TABLE_COLUMN_DATA, jsonObject.toString());
				if (row_id != 0)
				{
					databaseManager.update(CoreConstants.TABLE_CUSTOMER_INFO,
							contentValues, row_id);
				}

				/*cursor = databaseManager.search(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_" + connObj,
						new Clause(new QueryItem(Constants.KEY_BP_NUMBER, bpNo, SearchCondition.EQUALS), null, null));
				if (cursor.getCount() > 0)
				{
					updateData = getRecordStore(updateData);
					if (updateData != null && updateData.size() > 0)
					{
						data = updateData.elementAt(0).toString();
						parser.setJson(data);
						String status = parser.getValue(Constants.FIELD_STATUS);
						if (StringUtil.isValid(status))
						{
							dataJson = new JSONObject(data);
							JSONUtil.setJSONProperty(dataJson, Constants.FIELD_STATUS, meterReadingStatus);
						}
						if (JSONUtil.isValid(dataJson) && cursor.moveToFirst())
						{
							databaseManager.update(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_" + connObj,
									dataJson.toString(), cursor.getInt(cursor.getColumnIndex("_id")));
						}
					}
					close(cursor);
				}*/
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{

			close(cursor);
		}
	}

	private void updateBookWalkTable(String connObj) throws JSONException
	{
		Cursor cursor = null;
		try
		{
			int unattemptedCount = getBookCount(connObj, Constants.FIELD_UNATTEMPTED);

			int completeCount = getBookCount(connObj, Constants.FIELD_COMPLETED);

			int incompleteCount = getBookCount(connObj, Constants.FIELD_INCOMPLETE);

			cursor = databaseManager.search(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_" + connObj, null);
			int totalCount = cursor.getCount();
			close(cursor);
			if (totalCount == unattemptedCount)
			{
				updateBookWalkTableBasedOnStatus(connObj, Constants.FIELD_UNATTEMPTED);
			}
			else if (totalCount == completeCount)
			{
				updateBookWalkTableBasedOnStatus(connObj, Constants.FIELD_COMPLETED);
			}
			else if (totalCount == incompleteCount)
			{
				updateBookWalkTableBasedOnStatus(connObj, Constants.FIELD_INCOMPLETE);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
	}

	private void updateBookWalkTableBasedOnStatus(String connObj, String bookWalkStatus) throws JSONException
	{
		Cursor cursor = null;
		try
		{
			JSONObject dataJson = null;
			Vector updateData = new Vector<String>();
			String data;
			cursor = databaseManager.search(IBookWalkSqncePersistenceService.BOOKWALK_TABLE, new Clause(new QueryItem(
					Constants.FIELD_CONN_OBJ, connObj, SearchCondition.EQUALS), null, null));
			if (cursor.getCount() > 0)
			{
				updateData = getRecordStore(updateData, cursor);
				if (updateData != null && updateData.size() > 0)
				{
					data = updateData.elementAt(0).toString();
					parser.setJson(data);
					String status = parser.getValue(Constants.FIELD_STATUS);
					if (StringUtil.isValid(status))
					{
						dataJson = new JSONObject(data);
						JSONUtil.setJSONProperty(dataJson, Constants.FIELD_STATUS, bookWalkStatus);
					}
					if (JSONUtil.isValid(dataJson) && cursor.moveToFirst())
					{
						databaseManager.update(IBookWalkSqncePersistenceService.BOOKWALK_TABLE, dataJson.toString(),
								cursor.getInt(cursor.getColumnIndex("_id")));
					}
				}
				close(cursor);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
	}

	private int getBookCount(String connObj, String status)
	{
		Cursor cursor = null;
		int count = 0;

		try
		{
			count = 0;
			String bookwalk_id = null;
			searchQuery = "SELECT * FROM " + CoreConstants.BOOKWALK_TABLE + " WHERE " + CoreConstants.TABLE_COLUMN_DATA
					+ " LIKE '%\"" + Constants.FIELD_CONN_OBJ + "\":%" + connObj + "%';";
			cursor = databaseManager.executeRawQuery(CoreConstants.BOOKWALK_TABLE, searchQuery);
			if (cursor.getCount() > 0)
			{
				while (cursor.moveToNext())
				{
					bookwalk_id = cursor.getString(cursor.getColumnIndex("_id"));
				}
			} 
			close(cursor);

			searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_BOOKWALK_ID + "='" + bookwalk_id + "' AND "
					+ CoreConstants.TABLE_COLUMN_STATUS + "='" + status + "';";
			cursor = databaseManager.executeRawQuery(CoreConstants.BOOKWALK_TABLE, searchQuery);
			if (cursor.getCount() > 0)
			{
				count = cursor.getCount();
			}
			close(cursor);

			/*cursor = databaseManager.search(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_" + connObj,
					new Clause(new QueryItem(Constants.FIELD_STATUS, status, SearchCondition.EQUALS), null, null));
			count = cursor.getCount();*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return count;
	}

	@Override
	public Vector<String> getBookWalkSyncData(String entity)
	{
		Vector<String> syncData = new Vector<String>();
		Cursor cursor = null;
		try
		{
			cursor = databaseManager.search(entity, null);

			if (cursor.getCount() > 0)
			{
				while (cursor.moveToNext())
				{
					syncData.add(cursor.getString(cursor.getColumnIndex(CoreConstants.KEY_DATA)));
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("DATA NOT PRESENT");
		}
		finally
		{
			close(cursor);
		}
		return syncData;
	}

	@Override
	public void clearAllData()
	{
		Vector<String> tableNames = getTableLists();
		for (int i = 0; i < tableNames.size(); i++)
		{
			dropTable(tableNames.elementAt(i).toString());
		}
	}

	public void dropTable(String tableName)
	{
		MobiculeLogger.verbose("dropping table", tableName);
		databaseManager.dropTable(tableName);
	}

	@Override
	public int getSavedCount(String entity)
	{
		Cursor cursor = null;
		try
		{
			Clause meterReadingClause = null;
			if (entity.equals(CoreConstants.TABLE_CUSTOMER_UPDATE))
			{
				meterReadingClause = new Clause(new QueryItem(CoreConstants.KEY_UPDATECUSTOMER_SUBMIT, "0",
						SearchCondition.EQUALS), null, null);
			}
			else if (entity.equals(CoreConstants.TABLE_SAVED_METER_READING))
			{
				meterReadingClause = new Clause(new QueryItem(KEY_MR_SUBMIT, "0", SearchCondition.EQUALS), null, null);
			}
			else if (entity.equals(CoreConstants.TABLE_SAVED_JOIN_TICKETING))
			{
				meterReadingClause = new Clause(new QueryItem(KEY_JT_SUBMIT, "0", SearchCondition.EQUALS), Operator.AND, 
						new Clause(new QueryItem("entity", "jointTicket", SearchCondition.EQUALS), null, null));
			}
			else if(entity.endsWith(CoreConstants.TABLE_OnM_PLANNING))
			{
				meterReadingClause = new Clause(new QueryItem(KEY_MR_SUBMIT, "0", SearchCondition.EQUALS), null, null);
			}

			cursor = databaseManager.search(entity, meterReadingClause);
			if (cursor != null)
			{
				return cursor.getCount();
			}
			else
			{
				MobiculeLogger.verbose("getSavedCount() - No Records Found (null cursor)");
				return 0;
			}
		}
		catch (Exception e)
		{
			MobiculeLogger.verbose("getSavedCount() - " + e.toString());
			return 0;
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}

	}

	@Override
	public Vector getCustomerListBasedOnConnObj(String buildingName, String status, int noOfRecordsFrom,
			int noOfRecordsTo)
	{
		Vector<String> tableNames = new Vector<String>();

		Vector<String> data = new Vector<String>();
		Cursor cursor = null;
		try
		{
			int bookwalk_id = find_idInDataBase(CoreConstants.BOOKWALK_TABLE, buildingName);
			searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_BOOKWALK_ID + "='" + bookwalk_id + "' AND "
					+ CoreConstants.TABLE_COLUMN_STATUS + "='" + status + "' LIMIT " + noOfRecordsFrom + " , " + 10 + " ;";

			cursor = databaseManager.executeRawQuery(CoreConstants.TABLE_CUSTOMER_INFO, searchQuery);
			if (cursor.getCount() > 0)
			{
				data = getRecordStore(data, cursor);
			}
			/*Clause statusClause = new Clause(new QueryItem(Constants.FIELD_STATUS, status, SearchCondition.EQUALS),
					null, null);
			Clause clause = new Clause(new QueryItem(Constants.FIELD_CONN_OBJ, connObj, SearchCondition.CONTAINS),
					null, null);
			cursor = databaseManager.search(IBookWalkSqncePersistenceService.BOOKWALK_TABLE, clause);
			if (cursor.getCount() > 0)
			{
				data = getRecordStore(data);
			}
			close(cursor);
			for (int i = 0; i < data.size(); i++)
			{
				parser.setJson(data.elementAt(i).toString());
				tableNames.add(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_"
						+ parser.getValue(Constants.FIELD_CONN_OBJ).toString());
			}
			data = new Vector<String>();
			for (int i = 0; i < tableNames.size(); i++)
			{
				String tableName = tableNames.elementAt(i).toString();
				if (tableName.indexOf(IBookWalkSqncePersistenceService.BOOKWALK_TABLE + "_") != -1)
				{
					cursor = databaseManager.search(tableName, statusClause);
					if (cursor.getCount() > 0)
					{
						data = getRecordStore(data);
					}
					close(cursor);
				}
			}*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return data;

	}

	@Override
	public Vector getSearchCustomerDataFrmDB(String buildingName, String searchKey, String status)
	{
		Vector<String> tableNames = new Vector<String>();

		Vector<String> data = new Vector<String>();
		Cursor cursor = null;
		try
		{
			int bookwalk_id = find_idInDataBase(CoreConstants.BOOKWALK_TABLE, buildingName);
			searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
					+ CoreConstants.TABLE_COLUMN_STATUS + "='" + status + "' AND "
					+ CoreConstants.TABLE_COLUMN_BOOKWALK_ID + "='" + bookwalk_id + "' AND ( " 
					+ CoreConstants.TABLE_COLUMN_BPNUMBER + " LIKE '%" + searchKey + "%' OR " 
					+ CoreConstants.TABLE_COLUMN_METERNO + " LIKE '%" + searchKey + "%' OR "  
					+ CoreConstants.TABLE_COLUMN_CONTACTNO + " LIKE '%" + searchKey + "%' OR " 
					+ CoreConstants.TABLE_COLUMN_CONTACTNO_OFFICE + " LIKE '%" + searchKey + "%' OR "
					+ CoreConstants.TABLE_COLUMN_CONTACTNO_RES + " LIKE '%" + searchKey + "%' OR "
					+ CoreConstants.TABLE_COLUMN_BUILDING_NAME + " LIKE '%" + searchKey + "%' OR "
					+ CoreConstants.TABLE_COLUMN_CUSTOMERNAME + " LIKE '%" + searchKey + "%' OR "
					+ CoreConstants.TABLE_COLUMN_FLATNO + " LIKE '%" + searchKey + "%');";

			cursor = databaseManager.executeRawQuery(CoreConstants.TABLE_CUSTOMER_INFO, searchQuery);
			if (cursor.getCount() > 0)
			{
				data = getRecordStore(data, cursor);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return data;
	}

}
