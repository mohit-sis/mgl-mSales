package com.mobicule.msales.mgl.client.downloadbookwalk.implementation;

import org.json.me.JSONObject;

import com.mobicule.component.util.CoreMobiculeLogger;

public class SyncInitRequestBuilder extends SyncRequestBuilder
{
	public SyncInitRequestBuilder(String entity, String lastSyncDate, JSONObject userJson, Integer pageSize)
	{
		
		super(entity, INIT, userJson);
		CoreMobiculeLogger.log("SyncInitRequestBuilder"+userJson);
		put(LAST_SYNC_DATE, lastSyncDate);
		put(PAGE_NUMBER, new Integer(0));
		put(PAGE_SIZE, pageSize);
	}
}
