/**
******************************************************************************
* C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
* <p>
* Copyright ? 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved. 
* This is proprietary information of Mobicule Technologies Pvt. Ltd.and is 
* subject to applicable licensing agreements. Unauthorized reproduction, 
* transmission or distribution of this file and its contents is a 
* violation of applicable laws.
******************************************************************************
*
* @project mSalesMgl
*/
package com.mobicule.android.msales.mgl.randommeterreading.model;

import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import com.mobicule.android.component.db.Clause;
import com.mobicule.android.component.db.QueryItem;
import com.mobicule.android.component.db.SQLiteDatabaseManager;
import com.mobicule.android.component.db.SearchCondition;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.msales.mgl.client.randommeterreading.IRandomMeterReadingPersistance;

/**
* 
* <enter description here>
*
* @author nikita
* @see 
*
* @createdOn Mar 20, 2012
* @modifiedOn
*
* @copyright ? 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class DefaultRandomMeterReadingPersistenceService implements IRandomMeterReadingPersistance
{
	private Context context;

	private static DefaultRandomMeterReadingPersistenceService instance;

	private SQLiteDatabaseManager databaseManager;

	private JSONParser jsonParser;

	private static final String DATA = "data";

	public DefaultRandomMeterReadingPersistenceService(Context context)
	{
		this.context = context;
		jsonParser = JSONParser.getInstance();
		databaseManager = (SQLiteDatabaseManager) SQLiteDatabaseManager.getInstance(context,Constants.DATABASE_NAME);
	}

	public static IRandomMeterReadingPersistance getPersistenceService(Context context)
	{
		if (instance == null)
		{
			instance = new DefaultRandomMeterReadingPersistenceService(context);
		}
		return instance;
	}

	public Vector<String> getTableLists()
	{
		Vector<String> tableNames = new Vector<String>();
		MobiculeLogger.verbose("APPLICATION PERSISTANCE - GET TABLE LIST");
		Cursor cursor = databaseManager.getTableNames();
		MobiculeLogger.verbose("NUMBER OF TABLES - COUNT = " + cursor.getCount());
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
		MobiculeLogger.verbose("tablesNames - "+ tableNames.toString());
		return tableNames;
	}

	@Override
	public void saveRandomMeterReading(String entity, String data)
	{
		Cursor cursor = null;
		try
		{
			jsonParser.setJson(data);

			String currentRandomMeterReading = jsonParser.getValue(Constants.KEY_CUSTOMER_NAME);
			
			MobiculeLogger.verbose("DefaultRandomMeterReadingPersistenceService    currentRandomMeterReading is:  "+ currentRandomMeterReading);

			cursor = databaseManager.search(entity, new Clause(new QueryItem(Constants.KEY_CUSTOMER_NAME,
					currentRandomMeterReading, SearchCondition.CONTAINS), null, null));
			
			if (cursor.getCount() > 0)
			{
				if (cursor.moveToFirst())
				{
					databaseManager.update(entity, data, cursor.getInt(cursor.getColumnIndex("_id")));
				}
			}
			else
			{
				databaseManager.add(entity, data);
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

	public void close(Cursor cursor)
	{
		if (cursor != null)
		{
			cursor.close();
		}
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
		finally
		{
			close(cursor);
		}
		return data;
	}

	@Override
	public Vector getOfflineRandomMeterReadingDetail()
	{
		String data = "";
		Vector savedRandomMeterReading = new Vector();
		Cursor cursor = databaseManager.search(CoreConstants.TABLE_RANDOM_METER_READING, null);
		try
		{
			if (cursor.getCount() > 0)
			{
				while (cursor.moveToNext())
				{
					savedRandomMeterReading.add(cursor.getString(cursor.getColumnIndex(DATA)));
				}
			}
			close(cursor);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return savedRandomMeterReading;
	}

	@Override
	public Vector fetchSavedRandomMeterReading(String entity, String customerName)
	{
		Vector savedRandomMeterReading = new Vector();
		Clause clause = new Clause(new QueryItem(Constants.KEY_CUSTOMER_NAME, customerName, SearchCondition.EQUALS),
				null, null);
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
		}
		catch (NullPointerException e)
		{
			throw new RuntimeException("DATA NOT PRESENT");
		}
		finally
		{
			close(cursor);
		}
		return savedRandomMeterReading;
	}

	@Override
	public Vector fetchAllSavedRandomMeterReadings(String entity)
	{
		Vector savedRandomMeterReading = new Vector();
		//check for saved meter readings and not the submitted ones.
		Clause randomMeterReadingClause = new Clause(new QueryItem(KEY_RMR_SUBMIT, "0", SearchCondition.EQUALS), null,
				null);
		Cursor cursor = null;
		try
		{
			if (getTableLists().contains(entity))
			{
				cursor = databaseManager.search(entity, randomMeterReadingClause);

				if (cursor != null)
				{
					if (cursor.getCount() > 0)
					{
						while (cursor.moveToNext())
						{
							savedRandomMeterReading.add(cursor.getString(cursor.getColumnIndex(DATA)));
						}
					}
				}
				close(cursor);
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
		return savedRandomMeterReading;
	}

}
