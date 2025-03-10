package com.mobicule.android.msales.mgl.util;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.mobicule.android.component.logging.MobiculeLogger;

/**
 * 
* 
* <enter description here>
*
* @author Prashant <enter lastname>
* @see 
*
* @createdOn Oct 1, 2013
* @modifiedOn
*
* @copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
 */

public class DynamicUrl
{
	private static final String TAG = "DynamicUrl";

	/**
		This method check 2 Server Urls for connection 
		and the working url will be use for all 
		communication between client and server
	 */
	/*public static void setWorkingUrl()
	{
		try
		{
			String[] urls = new String[] { Constants.SERVER_URL_1+Constants.MGL_SERVER, Constants.SERVER_URL_2+Constants.MGL_SERVER };
			int i=1;
			int timeoutConnection = 5000;
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			
			for (String url : urls)
			{
				int response = 0;
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				httpGet.setParams(httpParameters);
				try
				{

					HttpResponse execute = httpClient.execute(httpGet);
					response = execute.getStatusLine().getStatusCode();
					if (response == 200)
					{
						Constants.DIAGNOSTIC_URL = url;
						Constants.LOGIN_URL = Constants.DIAGNOSTIC_URL + Constants.REQUEST_GATEWAY;
						
						return;
					}
					else
					{
					}
				}
				catch (Exception e)
				{
				}
				i++;	
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, "setWorkingUrl() : " + e.toString());
		}
	}
*/
	
	public static void setWorkingUrl()
	{
		try
		{
			String[] urls = new String[] { Constants.SERVER_URL_1+Constants.MGL_SERVER, Constants.SERVER_URL_2+Constants.MGL_SERVER };
			int i=1;
			int timeoutConnection = 3000;
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			
			for (String url : urls)
			{
				Log.e("URLS",""+url);

				int response = 0;
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				httpGet.setParams(httpParameters);
				try
				{
					//MobiculeLogger.showInfoLog("DynamicUrl","Checking connection . . .");
					MobiculeLogger.verbose("Checking connection . . .");

					HttpResponse execute = httpClient.execute(httpGet);
					response = execute.getStatusLine().getStatusCode();
					if (response == 200)
					{
						//MobiculeLogger.showErrorLog(TAG, "URL "+i+" WORKING : " + url);
						Constants.DIAGNOSTIC_URL = url;
						Constants.LOGIN_URL = Constants.DIAGNOSTIC_URL + Constants.REQUEST_GATEWAY;
						Constants.SYNC_URL = Constants.DIAGNOSTIC_URL + Constants.REQUEST_GATEWAY;
					//	HTTPNetworkService.getInstance().setServerUrl(url);
					//	MobiculeLogger.showInfoLog(TAG, "CURRENT DIAGNOSTIC URL : " + Constants.DIAGNOSTIC_URL);
						return;
					}
					else
					{
						//MobiculeLogger.showErrorLog(TAG, "URL "+i+" NOT WORKING : " + url);
						MobiculeLogger.verbose("URL "+i+" NOT WORKING : " + url);
					}
				}
				catch (Exception e)
				{
					//MobiculeLogger.showErrorLog(TAG, "URL "+i+" NOT WORKING : " + url);
					MobiculeLogger.verbose("URL "+i+" NOT WORKING : " + url);
				}
				//MobiculeLogger.showInfoLog(TAG, "Response :" + response);
				MobiculeLogger.verbose("Response :" + response);
				i++;
				
				
			}
		}
		catch (Exception e)
		{
			//MobiculeLogger.showErrorLog(TAG, "setWorkingUrl() : " + e.toString());
			MobiculeLogger.verbose("setWorkingUrl() : " + e.toString());
		}
	}
	
}
