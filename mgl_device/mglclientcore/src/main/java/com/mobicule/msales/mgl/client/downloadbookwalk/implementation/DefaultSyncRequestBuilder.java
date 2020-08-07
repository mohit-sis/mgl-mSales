package com.mobicule.msales.mgl.client.downloadbookwalk.implementation;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mobicule.component.util.CoreConstants;
import com.mobicule.component.util.CoreMobiculeLogger;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqnceRequestBuilder;

public class DefaultSyncRequestBuilder implements IBookWalkSqnceRequestBuilder
{
	private static IBookWalkSqnceRequestBuilder instance = null;

	private String userJson;

	/**
	 * @return the userJson
	 */
	public String getUserJson()
	{
		return userJson;
	}

	/**
	 * @param userJson the userJson to set
	 */
	public void setUserJson(String userJson)
	{
		this.userJson = userJson;
	}

	private Integer pageSize;

	private DefaultSyncRequestBuilder(String userJson, Integer pageSize)
	{
		this.userJson = userJson;
		this.pageSize = pageSize;
	}

	public static synchronized IBookWalkSqnceRequestBuilder getInstance(String userJson, Integer pageSize)
	{
		if (instance == null)
		{
			instance = new DefaultSyncRequestBuilder(userJson, pageSize);
		}
		return instance;
	}

	public String createSyncCompletionRequest(String entity, String lastSyncDate)
	{
		try
		{
			return new SyncCompletionRequestBuilder(entity, lastSyncDate, new JSONObject(userJson)).build().toString();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return CoreConstants.EMPTY_STRING;
	}

	public String createSyncInitRequest(String entity, String lastSyncDate)
	{
		try
		{
			CoreMobiculeLogger.log("DefaultSyncRequestBuilder"+userJson);
			return new SyncInitRequestBuilder(entity, lastSyncDate, new JSONObject(userJson), pageSize).build()
					.toString();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return CoreConstants.EMPTY_STRING;
	}

	public String createSyncPageRequest(String entity, String entityState, int pageCount, String lastSyncDate)
	{
		try
		{
			return new SyncPageRequestBuilder(entity, entityState, pageCount, lastSyncDate, new JSONObject(userJson),
					pageSize).build().toString();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return CoreConstants.EMPTY_STRING;
	}
}
