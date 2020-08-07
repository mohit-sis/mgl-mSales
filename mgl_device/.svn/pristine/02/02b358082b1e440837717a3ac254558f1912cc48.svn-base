package com.mobicule.msales.mgl.client.downloadbookwalk.implementation;

import org.json.me.JSONObject;

public class SyncPageRequestBuilder extends SyncRequestBuilder
{

	public SyncPageRequestBuilder(String entity, String entityState, int pageCount, String lastSyncDate,
			JSONObject userJson, Integer pageSize)
	{
		super(entity, entityState, userJson);
		put(LAST_SYNC_DATE, lastSyncDate);
		put(PAGE_NUMBER, new Integer(pageCount));
		put(PAGE_SIZE, pageSize);
	}
}
