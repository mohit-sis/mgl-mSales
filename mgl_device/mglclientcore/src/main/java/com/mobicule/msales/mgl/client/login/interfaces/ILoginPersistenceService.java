package com.mobicule.msales.mgl.client.login.interfaces;

import org.json.me.JSONObject;

import com.mobicule.msales.mgl.client.common.Response;

public interface ILoginPersistenceService
{
	public final String USER_DETAIL_USERNAME = "name";

	public final String USER_DETAIL_PASSWORD = "pass";

	public final String USER_DETAIL_IMEINUMBER = "imei";

	public final String DELAY = "delay";
 
	public boolean isOffineLoginMode();//is db created

	public Response setOffineLoginMode(JSONObject jsonObject);//verify the user detail from db

	public Response getOfflineLoginDetail();// get data from db for pre-populate

	public Response storeUserDetails(JSONObject jsonObject);
}
