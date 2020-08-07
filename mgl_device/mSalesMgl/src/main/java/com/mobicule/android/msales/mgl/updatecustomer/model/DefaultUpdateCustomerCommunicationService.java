/**
******************************************************************************
* C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
* <p>
* Copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved. 
* This is proprietary information of Mobicule Technologies Pvt. Ltd.and is 
* subject to applicable licensing agreements. Unauthorized reproduction, 
* transmission or distribution of this file and its contents is a 
* violation of applicable laws.
******************************************************************************
*
* @project mSalesMgl
*/
package com.mobicule.android.msales.mgl.updatecustomer.model;

import org.json.me.JSONObject;

import android.content.Context;

import com.mobicule.android.msales.mgl.commons.model.CommunicationChannel;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.DynamicUrl;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginBusinessService;
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomerCommunication;

/**
* 
* <enter description here>
*
* @author nikita
* @see 
*
* @createdOn Mar 24, 2012
* @modifiedOn
*
* @copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class DefaultUpdateCustomerCommunicationService extends CommunicationChannel implements
		IUpdateCustomerCommunication
{
	private Context context;

	private String postdata;

	private String posturl;

	private static DefaultUpdateCustomerCommunicationService instance;

	public DefaultUpdateCustomerCommunicationService(Context context)
	{
		this.context = context;
	}

	public static DefaultUpdateCustomerCommunicationService getCommunicationService(Context context)
	{
		if (instance == null)
		{
			instance = new DefaultUpdateCustomerCommunicationService(context);
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
