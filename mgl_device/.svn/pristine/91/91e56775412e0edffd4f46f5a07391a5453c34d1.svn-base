package com.mobicule.msales.mgl.client.downloadbookwalk.implementation;

import org.json.me.JSONObject;

public class SyncCompletionRequestBuilder extends SyncRequestBuilder
{
	public SyncCompletionRequestBuilder(String entity, String lastSyncDate, JSONObject userJson)
	{
		super(entity, COMPLETION, userJson);
		put(LAST_SYNC_DATE, lastSyncDate);
		put(PAGE_NUMBER, new Integer(0));
		put(PAGE_SIZE, new Integer(5));
	}
}
