package com.mobicule.msales.mgl.client.login.interfaces;

import org.json.me.JSONObject;

import com.mobicule.msales.mgl.client.common.Response;

public interface ILoginBusinessService
{
	public final String NETWORK_FAILED = "Network Connection Failure";

	public abstract Response validateInput(String username, String password, String imeinumber);

	public abstract Response isOffineLoginMode();

	public abstract Response setOffineLoginMode(JSONObject userDetail);

	public abstract Response setOnlineLoginMode(JSONObject jsonObject);

	public abstract Response submit(JSONObject jsonObject);

	public abstract Response storeUserDetails(JSONObject jsonObject);

	public abstract Response loginInOfflineMode();//(String username, String password);

	public abstract Response signIn(String username, String password, String imeinumber);

	public abstract void setOnlineLoginFlag(boolean isOnlineLogin);

	public abstract boolean isOnlineLogin();
}
