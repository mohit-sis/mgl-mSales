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
package com.mobicule.android.msales.mgl.commons.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.msales.mgl.client.common.IParser;
import com.mobicule.msales.mgl.client.common.Parser;

/**
* 
* <enter description here>
*
* @author Sathya Sheela
* @see 
*
* @createdOn Feb 13, 2012
* @modifiedOn
*
* @copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class CommunicationChannel
{

	private HttpClient client;

	private HttpPost request;

	private HttpResponse httpresponse;

	protected static IParser parserInstance;

	private BufferedReader in = null;

	public String sendRequest(String postUrl, String postData)
	{
		StringBuffer sb = new StringBuffer("");
		parserInstance = Parser.getNewInstance();
		//postUrl+="&client=android";
		
		try
		{
			client = new DefaultHttpClient();
			request = new HttpPost(postUrl);
			request.setEntity(new StringEntity(postData));
			httpresponse = client.execute(request);
			MobiculeLogger.verbose("Post Url is : " + postUrl);
			MobiculeLogger.verbose("Post Data is: " + postData);
			in = new BufferedReader(new InputStreamReader(httpresponse.getEntity().getContent()));

			String line = "";

			while ((line = in.readLine()) != null)
			{
				sb.append(line);
			}
			in.close();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		String response = sb.toString();
		MobiculeLogger.verbose("response from server"+ response);
		return response;
	}
}
