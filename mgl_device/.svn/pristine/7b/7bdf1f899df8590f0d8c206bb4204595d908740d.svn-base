package com.mobicule.msales.mgl.client.common;

import com.mobicule.component.util.CoreConstants;

public class LoginRequestBuilder extends UnauthenticatedRequestBuilder
{
	private static final String REQUEST_TYPE = "login";

	public LoginRequestBuilder(String login, String password, String imei) 
	{
		super(REQUEST_TYPE, CoreConstants.EMPTY_STRING, CoreConstants.EMPTY_STRING);

		put(LOGIN, login);
		put(PASSWORD, password);
		put(IMEINUMBER, imei);
		
	}

	public boolean shouldAddJsonArray()
	{
		return false;
	}
}
