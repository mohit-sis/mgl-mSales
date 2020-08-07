package com.mobicule.msales.mgl.client.downloadbookwalk.implementation;

import org.json.me.JSONObject;

import com.mobicule.component.util.CoreMobiculeLogger;
import com.mobicule.msales.mgl.client.common.AuthenticatedRequestBuilder;

public abstract class SyncRequestBuilder extends AuthenticatedRequestBuilder
{
	public static final String SYNC = "sync";

	public static final String INIT = "init";

	public static final String ADD = "add";

	public static final String MODIFY = "modify";

	public static final String DELETE = "delete";

	public static final String COMPLETION = "completion";

	public static final String PAGE_NUMBER = "pageNumber";

	public static final String PAGE_SIZE = "pageSize";

	public static final String LAST_SYNC_DATE = "lastSyncDate";

	public static final String DSE_CODE = "dseCode";
	
	/*http://192.168.1.201/mobicule/products/MFieldForce/MGL-mSales/
		MGLClient/Android/branches/Mgl(02-07-2012)*/

	public SyncRequestBuilder(String entity, String action, JSONObject userJson)
	{
		super(SYNC, entity, action, userJson);
		CoreMobiculeLogger.log("SyncRequestBuilder"+userJson);
	}

	public boolean shouldAddJsonArray()
	{
		return false;
	}
}
