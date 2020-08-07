package com.mobicule.android.msales.mgl.onmplaning.model;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mobicule.android.component.db.Clause;
import com.mobicule.android.component.db.QueryItem;
import com.mobicule.android.component.db.SQLiteDatabaseManager;
import com.mobicule.android.component.db.SearchCondition;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.msales.mgl.client.onmPlanning.IOnMPersistance;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;

public class DefaultOnMPlanningPersistanceService implements IOnMPersistance
{

	private Context context;

	private static DefaultOnMPlanningPersistanceService instance;

	private SQLiteDatabaseManager databaseManager;

	private JSONParser jsonParser;

	private static final String DATA = "data";

	public DefaultOnMPlanningPersistanceService(Context context)
	{
		this.context = context;
		jsonParser = JSONParser.getInstance();
		databaseManager = (SQLiteDatabaseManager) SQLiteDatabaseManager.getInstance(context, Constants.DATABASE_NAME);
	}

	public static IOnMPersistance getPersistenceService(Context context)
	{
		if (instance == null)
		{
			instance = new DefaultOnMPlanningPersistanceService(context);
		}
		return instance;
	}

	@Override
	public void saveOnMReading(String entity, String data)
	{
		
		MobiculeLogger.verbose("DefaultOnMPlanningPersistanceService    data is:  " + data);
		jsonParser.setJson(data);

		String currentCustomersaved = jsonParser.getValue(CoreConstants.FIELD_MRO_NO);
		
		MobiculeLogger.verbose("DefaultOnMPlanningPersistanceService    currentCustomersaved   is:  "+ currentCustomersaved);
		
		Cursor cursor = null;

		cursor = databaseManager.search(entity, new Clause(new QueryItem(CoreConstants.FIELD_MRO_NO,
				currentCustomersaved, SearchCondition.CONTAINS), null, null));
		
		if (cursor.getCount() > 0)
		{
			if (cursor.moveToFirst())
			{
				databaseManager.update(entity, data, cursor.getInt(cursor.getColumnIndex("_id")));
			}
			close(cursor);
		}

		else
		{
			close(cursor);
			databaseManager.add(entity, data);
		}

	}

	@Override
	public ArrayList<String> getOfflineOnMReadingDetail()
	{
		ArrayList<String> savedCustomers = new ArrayList<String>();
		try
		{

			Clause randomMeterReadingClause = new Clause(new QueryItem(CoreConstants.KEY_MR_SUBMIT, "0",
					SearchCondition.EQUALS), null, null);

			Cursor cursor = null;

			if (getTableLists().contains(CoreConstants.TABLE_OnM_PLANNING))
			{
				cursor = databaseManager.search(CoreConstants.TABLE_OnM_PLANNING, randomMeterReadingClause);
				if (cursor != null)
				{
					if (cursor.getCount() > 0)
					{
						while (cursor.moveToNext())
						{
							if (cursor.getString(cursor.getColumnIndex(DATA)) != null)
							{

								try {


									JSONObject jsonData = new JSONObject(cursor.getString(cursor.getColumnIndex(DATA)));
									JSONObject jsonValue = new JSONObject();
									jsonValue.put(Constants.KEY_MRO_NUMBER, jsonData.get(Constants.KEY_MRO_NUMBER));
									jsonValue.put(Constants.KEY_CUSTOMER_NAME, jsonData.get(Constants.KEY_CUSTOMER_NAME));
									jsonValue.put(Constants.KEY_BP_NUMBER, jsonData.get(Constants.KEY_BP_NUMBER));
									//savedCustomers.add(cursor.getString(cursor.getColumnIndex(DATA)));
									savedCustomers.add(jsonValue.toString());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
					close(cursor);
				}
			}

			/*Cursor cursor = databaseManager.search(CoreConstants.TABLE_OnM_PLANNING, null); // ---------------- change table name
			if (cursor.getCount() > 0)
			{
				while (cursor.moveToNext())
				{
					savedCustomers.add(cursor.getString(cursor.getColumnIndex(DATA)));
				}
			}
			close(cursor);*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return savedCustomers;
	}

	@Override
	public Vector fetchSavedOnMReading(String entity, String mroNo)
	{
		Vector savedRandomMeterReading = new Vector();
		Clause clause = new Clause(new QueryItem(CoreConstants.FIELD_MRO_NO, mroNo, SearchCondition.EQUALS), null, null);
		Cursor cursor = null;
		try
		{
			cursor = databaseManager.search(entity, clause);

			if (cursor.getCount() > 0)
			{
				while (cursor.moveToNext())
				{
					savedRandomMeterReading.add(cursor.getString(cursor.getColumnIndex(DATA)));
				}
			}
			close(cursor);
		}
		catch (NullPointerException e)
		{
			close(cursor);
			throw new RuntimeException("DATA NOT PRESENT");
		}
		return savedRandomMeterReading;
	}

	public Vector<String> getTableLists()
	{
		Vector<String> tableNames = new Vector<String>();
		Log.v("APPLICATION PERSISTANCE", "GET TABLE LIST");
		Cursor cursor = databaseManager.getTableNames();
		Log.v("NUMBER OF TABLES", " COUNT = " + cursor.getCount());
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
		Log.d("tablesNames", tableNames.toString());
		return tableNames;
	}

	@Override
	public Vector fetchAllSavedOnMReadingMroNo(String entity)
	{
		Vector savedCustomerSaved = new Vector();

		Clause randomMeterReadingClause = new Clause(new QueryItem(CoreConstants.KEY_MR_SUBMIT, "0",
				SearchCondition.EQUALS), null, null);

		Cursor cursor = null;
		try
		{
			if (getTableLists().contains(entity))
			{
				cursor = databaseManager.search(entity, randomMeterReadingClause);
				if (cursor.getCount() > 0)
				{
					while (cursor.moveToNext())
					{
						jsonParser.setJson(cursor.getString(cursor.getColumnIndex(DATA)));

						savedCustomerSaved.add(jsonParser.getValue("mroNo"));
					}
				}
				close(cursor);
			}
		}
		catch (NullPointerException e)
		{
			close(cursor);
			throw new RuntimeException("DATA NOT PRESENT");
		}
		Log.d("saved random meter data", savedCustomerSaved.toString());
		return savedCustomerSaved;

	}

	public void close(Cursor cursor)
	{
		if (cursor != null)
		{
			cursor.close();
		}
	}

}
