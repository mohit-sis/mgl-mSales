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
* @project Msales_Usha_Android_Merged
*/
package com.mobicule.msales.mgl.client.common;

import org.json.me.JSONObject;

import com.mobicule.component.util.CoreMobiculeLogger;

/**
* 
* <enter description here>
*
* @author Sathya Sheela
* @see 
*
* @createdOn Feb 10, 2012
* @modifiedOn
*
* @copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class Parser implements IParser
{
	private String responseStatus = "";

	private JSONObject responseDataJson = null;

	private static final String USER_RESPONSE_MESSAGE = "message";

	private static final String KEY_DATA = "data";

	private Response response;

	private static IParser instance;

	public static IParser getNewInstance()
	{
		instance = new Parser();
		return instance;
	}

	public static IParser getPreviousInstance()
	{
		return instance;
	}

	public static void setPreviousInstance(IParser parserInstance)
	{
		instance = parserInstance;
	}

	public Response parseResponse(String result)
	{
		response = new Response(false, NETWORK_FAILED, null);
		responseStatus = "";
		
		CoreMobiculeLogger.log(" result .... s " + result);
		
		try
		{
			if (result.startsWith("{") && result.endsWith("}"))
			{
				responseDataJson = new JSONObject(result);
				responseStatus = responseDataJson.getString("status");
				
				CoreMobiculeLogger.log("responsestatus " + responseStatus);
				
				if (responseStatus.toUpperCase().equals("SUCCESS"))
				{
					response = new Response(true, responseDataJson.getString(USER_RESPONSE_MESSAGE), responseDataJson);
					return response;
				}
				else if (responseStatus.toUpperCase().equals("ERROR"))
				{
					response = new Response(false, responseDataJson.getString(USER_RESPONSE_MESSAGE), responseDataJson);
					return response;
				}
				else if (responseStatus.indexOf(AUTHENTICATION_FAILURE) != -1)
				{
					response = new Response(false, responseDataJson.getString(USER_RESPONSE_MESSAGE), responseDataJson);
					return response;
				}
				else if (responseStatus.toUpperCase().indexOf("MINOR") != -1)
				{
					response = new Response(false, responseDataJson.getString(USER_RESPONSE_MESSAGE),
							responseDataJson.getString(KEY_DATA));
					return response;
				}
				else if (responseStatus.toUpperCase().indexOf("MAJOR") != -1)
				{
					CoreMobiculeLogger.log("inside major");
					response = new Response(false, responseDataJson.getString(USER_RESPONSE_MESSAGE),
							responseDataJson.getString(KEY_DATA));
					return response;
				}
				else
				{
					response = new Response(false, responseDataJson.getString(USER_RESPONSE_MESSAGE), responseDataJson);
					return response;
				}
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * @return the responseStatus
	 */
	public String getResponseStatus()
	{
		return responseStatus;
	}

	/**
	 * @param responseStatus the responseStatus to set
	 */
	public void setResponseStatus(String responseStatus)
	{
		this.responseStatus = responseStatus;
	}

	/**
	 * @return the response
	 */
	public Response getResponse()
	{
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(Response response)
	{
		this.response = response;
	}
}
