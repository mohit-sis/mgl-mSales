/**
******************************************************************************
* C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
* <p>
* Copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved. 
* This is proprietary information of Mobicule Technologies Pvt. Ltd.and is 
* subject to applicable licensing agreements. Unauthorized reproduction, 
* transmission or distribution of this file and its contents is a 
* violation of applicable laws.
******************************************************************************
*
* @project mSalesMgl
*/
package com.mobicule.android.msales.mgl.updatecustomer.model;

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
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomerPersistance;

/**
 * 
 * <enter description here>
 *
 * @author nikita <enter lastname>
 * @see 
 *
 * @createdOn 11-Apr-2012
 * @modifiedOn
 *
 * @copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
 */
public class DefaultUpdateCustomerPersistanceService implements IUpdateCustomerPersistance
{

	private Context context;

	private static DefaultUpdateCustomerPersistanceService instance;

	private SQLiteDatabaseManager databaseManager;

	private JSONParser jsonParser;

	private static final String DATA = "data";

	public DefaultUpdateCustomerPersistanceService(Context context)
	{
		this.context = context;
		jsonParser = JSONParser.getInstance();
		databaseManager = (SQLiteDatabaseManager) SQLiteDatabaseManager.getInstance(context, Constants.DATABASE_NAME);
	}

	public static IUpdateCustomerPersistance getPersistenceService(Context context)
	{
		if (instance == null)
		{
			instance = new DefaultUpdateCustomerPersistanceService(context);
		}
		return instance;
	}

	@Override
	public void saveUpdateCustomer(String entity, String data)
	{
		jsonParser.setJson(data);

		String currentCustomersaved = jsonParser.getValue(CoreConstants.FIELD_MRO_NO);
		
		MobiculeLogger.verbose("DefaultUpdateCustomerPersistanceService    currentCustomersaved   is:  "
				+ currentCustomersaved);
		
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

	public void close(Cursor cursor)
	{
		if (cursor != null)
		{
			cursor.close();
		}
	}

	@Override
	public Vector getOfflineUpdateCustomerDetail()
	{
		Vector savedCustomers = new Vector();
		Cursor cursor = databaseManager.search(CoreConstants.TABLE_CUSTOMER_UPDATE, null);
		if (cursor.getCount() > 0)
		{
			while (cursor.moveToNext())
			{
				savedCustomers.add(cursor.getString(cursor.getColumnIndex(DATA)));
			}
		}
		close(cursor);
		return savedCustomers;
	}

	@Override
	public Vector fetchSavedUpdateCustomer(String entity, String mroNo)
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
		return tableNames;
	}

	@Override
	public Vector fetchAllSavedUpdateCustomerMroNo(String entity)
	{
		Vector savedCustomerSaved = new Vector();
		
		Clause randomMeterReadingClause = new Clause(new QueryItem(KEY_UPDATECUSTOMER_SUBMIT, "0",
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
		MobiculeLogger.verbose("saved random metereaing data - "+ savedCustomerSaved.toString());
		return savedCustomerSaved;
		
		/*Vector savedCustomerSaved = new Vector();
		//check for saved meter readings and not the submitted ones.
		Clause randomMeterReadingClause = new Clause(new QueryItem(KEY_UPDATECUSTOMER_SUBMIT, "0",
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
						savedCustomerSaved.add(cursor.getString(cursor.getColumnIndex(DATA)));
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
		return savedCustomerSaved;*/
	}

}
