package com.mobicule.android.msales.mgl.downloadbookwalk.model;

import com.mobicule.android.msales.mgl.util.Constants;


public class SyncMessages
{
	//	-------------------------------SERVICES CONSTANTS-------------------------------
	public static final String SYNC_FACADE = "syncFacade";

	public static final String SYNC_PERSISTENCE_SERVICE = "syncPersistenceService";

	public static final String SYNC_COMMUNICATION_SERVICE = "syncCommunicationService";

	public static final String SYNC_REQUESTBUILDER_SERVICE = "syncRequestBuilder";

	//-------------------------------SYNC INFO TABLE NAME-------------------------------
	public static final String SYNCDATE_INFO_KEY = "lastSyncDate";

	//-------------------------------URL LINKS FOR SYNC-------------------------------
	public String sync_entity = "";

	/**
	 * @return the sync_entity
	 */
	public String getSync_entity()
	{
		return sync_entity;
	}

	/**
	 * @param sync_entity the sync_entity to set
	 */
	public void setSync_entity(String sync_entity)
	{
		this.sync_entity = sync_entity;
	}

	public static String sync_url = Constants.FIRST_URL;//http://59.144.17.219/msales-usha/api/get/prodcat/?login=12369&password=pwd&version=4.0

	/**
	 * @return the sync_url
	 */
	public static String getSync_url()
	{
		return sync_url;
	}

}
