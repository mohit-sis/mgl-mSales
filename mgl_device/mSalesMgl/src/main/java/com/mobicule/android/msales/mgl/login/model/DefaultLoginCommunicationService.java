package com.mobicule.android.msales.mgl.login.model;

import org.json.me.JSONObject;

import android.content.Context;
import android.util.Log;

import com.mobicule.android.msales.mgl.commons.model.CommunicationChannel;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.DynamicUrl;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginBusinessService;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginCommunicationService;

/**
 * 
 * <enter description here>
 * 
 * @author nikita
 * @see
 * 
 * @createdOn Mar 1, 2012
 * @modifiedOn
 * 
 * @copyright � 2010-2011 Mobicule Technologies Pvt. Ltd. All rights reserved.
 */
public class DefaultLoginCommunicationService extends CommunicationChannel implements ILoginCommunicationService
{

	private Context context;

	public static final String PASSWORD = "pass";

	public static final String IMEINUMBER = "imei";

	private String postdata;

	private String posturl;

	private static DefaultLoginCommunicationService instance;

	public DefaultLoginCommunicationService(Context context)
	{
		this.context = context;
	}

	public static DefaultLoginCommunicationService getCommunicationService(Context context)
	{
		if (instance == null)
		{
			instance = new DefaultLoginCommunicationService(context);
		}
		return instance;
	}

	@Override
	public Response submit(JSONObject jsonObject)
	{
		DynamicUrl.setWorkingUrl();
		posturl = Constants.LOGIN_URL;
		postdata = jsonObject.toString();
		Log.e("posturl",""+posturl);
		Log.e("postdata",""+postdata);

		try
		{
			String result = sendRequest(posturl, postdata);
			Log.e("result",""+result);

			return parserInstance.parseResponse(result);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return new Response(false, ILoginBusinessService.NETWORK_FAILED, null);
	}

}
