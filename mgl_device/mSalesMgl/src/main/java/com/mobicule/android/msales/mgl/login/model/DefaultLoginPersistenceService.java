/**
******************************************************************************
* C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
* <p>
* Copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved. 
* This is proprietary information of Mobicule Technologies Pvt. Ltd.and is 
* subject to applicable licensing agreements. Unauthorized reproduction, 
* transmission or distribution of this file and its contents is a 
* violation of applicable laws.
******************************************************************************
*
* @project mSalesMgl
*/
package com.mobicule.android.msales.mgl.login.model;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import android.content.Context;
import android.database.Cursor;
import com.mobicule.android.component.db.Clause;
import com.mobicule.android.component.db.Operator;
import com.mobicule.android.component.db.QueryItem;
import com.mobicule.android.component.db.SQLiteDatabaseManager;
import com.mobicule.android.component.db.SearchCondition;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.FileUtil;
import com.mobicule.component.string.StringUtil;
import com.mobicule.component.util.CoreMessages;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginPersistenceService;

/**
* 
* <enter description here>
*
* @author Sathya Sheela
* @see 
*
* @createdOn Mar 1, 2012
* @modifiedOn
*
* @copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class DefaultLoginPersistenceService implements ILoginPersistenceService
{

	private Context context;

	private static DefaultLoginPersistenceService instance = null;

	private SQLiteDatabaseManager databaseManager;

	public DefaultLoginPersistenceService(Context context)
	{
		//this.context = context;
		databaseManager = (SQLiteDatabaseManager) SQLiteDatabaseManager.getInstance(context,Constants.DATABASE_NAME);
	}

	public static ILoginPersistenceService getPersistenceService(Context context)
	{
		if (instance == null)
		{
			instance = new DefaultLoginPersistenceService(context);
		}
		return instance;
	}

	@Override
	public boolean isOffineLoginMode()
	{
		/*// need to implement
		return false;*/
		int isDataAvailable = databaseManager.getRowCount(Constants.USER_STORAGE);
		MobiculeLogger.verbose(".....isOffineLoginMode - "+ isDataAvailable + "" + Constants.USER_STORAGE);
		if (isDataAvailable > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public Response setOffineLoginMode(JSONObject jsonObject)
	{
		try
		{
			JSONObject userDetail = (JSONObject) jsonObject.getValue("data");
			
			String loginNameValue = userDetail.getString(Constants.USER_NAME);

			String passwordValue = userDetail.getString(Constants.FIELD_PASSWORD);
			String imelValue = userDetail.getString(Constants.FIELD_IMEI_NUMBER);

			Clause clause = new Clause(new QueryItem(Constants.USER_NAME, loginNameValue, SearchCondition.EQUALS),
					Operator.AND, new Clause(new QueryItem(Constants.FIELD_PASSWORD, passwordValue,
							SearchCondition.EQUALS), Operator.AND, new Clause(new QueryItem(
							Constants.FIELD_IMEI_NUMBER, imelValue, SearchCondition.EQUALS), Operator.AND, null)));

			Cursor cursor = databaseManager.search(Constants.USER_STORAGE, clause);
			
			MobiculeLogger.verbose("*/**/*/SEARCH RESULT*/*/*/*/" + cursor.getCount());
			if (cursor.getCount() == 1)
			{
				close(cursor);
				return new Response(true, CoreMessages.OFFLINE_LOGIN_SUCCESS, null);
			}
			close(cursor);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			FileUtil.writeToFile("//DefaultLoginPersistenceService : Exception :: " + e);

		}

		return new Response(false, CoreMessages.NO_OFFLINE_RECORDS, null);
	}

	public void close(Cursor cursor)
	{
		if (cursor != null)
		{
			cursor.close();
		}
	}

	@Override
	public Response getOfflineLoginDetail()
	{
		/*// need to implement
		return new Response(false, "", null);*/

		String data = "";
		Cursor row = databaseManager.search(Constants.USER_STORAGE, null);
		if (row.getCount() > 0)
		{
			if (row.moveToFirst())
			{
				data = row.getString(row.getColumnIndex("data"));
			}
		}
		row.close();
		if (StringUtil.isValid(data))
		{
			try
			{
				MobiculeLogger.verbose("VALUE OF OFFLINE LOGIN"+ data);
				JSONObject OfflineLogin = new JSONObject(data);
				return new Response(true, CoreMessages.LOGIN_OFFLINE, OfflineLogin);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		return new Response(false, CoreMessages.OFFLINE_LOGIN_FAIL, null);
	}

	@Override
	public Response storeUserDetails(JSONObject jsonObject)
	{
		this.removeExistingDetail();
		
		MobiculeLogger.verbose("Login Persitance; json:  " + jsonObject);
		
		int rowid;
		try
		{
			rowid = databaseManager.add(Constants.USER_STORAGE, jsonObject.toString());
			MobiculeLogger.verbose("Created Row ID - "+ jsonObject.toString() + " [" + rowid + " ]");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return new Response(true, null, null);
	}

	public void removeExistingDetail()
	{
		MobiculeLogger.verbose("removeExistingDetail REMOVE EXISTING DATA CALLED " + Constants.USER_STORAGE);
		databaseManager.dropTable(Constants.USER_STORAGE);
	}

}
