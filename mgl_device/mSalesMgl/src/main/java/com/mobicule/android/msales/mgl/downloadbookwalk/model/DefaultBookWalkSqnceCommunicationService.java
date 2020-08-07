package com.mobicule.android.msales.mgl.downloadbookwalk.model;

import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.CommunicationChannel;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.DynamicUrl;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncCommunicationService;

/**
* 
* <enter description here>
*
* @author Sathya Sheela
* @see 
*
* @createdOn Mar 6, 2012
* @modifiedOn
*
* @copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class DefaultBookWalkSqnceCommunicationService extends CommunicationChannel implements
		IBookWalkSqncCommunicationService
{
	private static IBookWalkSqncCommunicationService instance = null;

	private String posturl;

	private String postdata;

	public synchronized static IBookWalkSqncCommunicationService getDefaultSyncCommunicationService()
	{
		if (instance == null)
		{
			instance = new DefaultBookWalkSqnceCommunicationService();
		}
		return instance;
	}

	@Override
	public String sendSyncRequest(String syncRequest)
	{


		try
		{
			DynamicUrl.setWorkingUrl();
			posturl = Constants.SYNC_URL;//SyncMessages.getSync_url().trim();
			postdata = syncRequest;
			String result = sendRequest(posturl, postdata);

			MobiculeLogger.verbose("Sync Data : " + result);
			return result;
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
