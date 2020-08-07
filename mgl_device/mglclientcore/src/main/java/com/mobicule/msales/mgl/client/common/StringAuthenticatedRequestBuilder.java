package com.mobicule.msales.mgl.client.common;

import org.json.me.JSONObject;

public abstract class StringAuthenticatedRequestBuilder extends StringHeaderRequestBuilder
{
	public StringAuthenticatedRequestBuilder(String type, String entity, String action, JSONObject userJson)
	{
		super(type, entity, action, userJson);
	}

	public final boolean shouldAddAuthorizationDetails()
	{
		return true;
	}
}
