package com.mobicule.android.msales.mgl.meterreading.model;

import org.json.me.JSONObject;

import android.content.Context;

import com.mobicule.android.msales.mgl.commons.model.CommunicationChannel;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.DynamicUrl;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginBusinessService;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingCommunication;

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
public class DefaultMeterReadingCommunicationService extends CommunicationChannel implements IMeterReadingCommunication
{
	private Context context;

	private String postdata;

	private String posturl;

	private static DefaultMeterReadingCommunicationService instance;

	public DefaultMeterReadingCommunicationService(Context context)
	{
		this.context = context;
	}

	public static DefaultMeterReadingCommunicationService getCommunicationService(Context context)
	{
		if (instance == null)
		{
			instance = new DefaultMeterReadingCommunicationService(context);
		}
		return instance;
	}

	@Override
	public Response submit(JSONObject jsonObject)
	{

		try
		{
			DynamicUrl.setWorkingUrl();
			posturl = Constants.LOGIN_URL;
			postdata = jsonObject.toString();

			String result = sendRequest(posturl, postdata);
			return parserInstance.parseResponse(result);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return new Response(false, ILoginBusinessService.NETWORK_FAILED, null);
	}

	@Override
	public Response submitString(String postdata)
	{

		try
		{
			DynamicUrl.setWorkingUrl();
			posturl = Constants.LOGIN_URL;
			String result = sendRequest(posturl, postdata);
			return parserInstance.parseResponse(result);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return new Response(false, ILoginBusinessService.NETWORK_FAILED, null);
	}
}
