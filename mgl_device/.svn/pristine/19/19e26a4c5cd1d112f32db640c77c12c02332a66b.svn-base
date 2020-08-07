package com.mobicule.android.msales.mgl.onmplaning.model;

import org.json.me.JSONObject;

import android.content.Context;

import com.mobicule.android.msales.mgl.commons.model.CommunicationChannel;
import com.mobicule.android.msales.mgl.updatecustomer.model.DefaultUpdateCustomerCommunicationService;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.DynamicUrl;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginBusinessService;
import com.mobicule.msales.mgl.client.onmPlanning.IOnMCommunication;

public class DefaultOnMPlanningCommunicationService extends CommunicationChannel implements IOnMCommunication
{

	private Context context;

	private String postdata;

	private String posturl;

	private static DefaultOnMPlanningCommunicationService instance;

	public DefaultOnMPlanningCommunicationService(Context context)
	{
		this.context = context;
	}

	public static DefaultOnMPlanningCommunicationService getCommunicationService(Context context)
	{
		if (instance == null)
		{
			instance = new DefaultOnMPlanningCommunicationService(context);
		}
		return instance;
	}

	@Override
	public Response submit(JSONObject jsonObject)
	{
		DynamicUrl.setWorkingUrl();
		posturl = Constants.LOGIN_URL;
		postdata = jsonObject.toString();

		try
		{
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
