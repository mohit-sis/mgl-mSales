package com.mobicule.msales.mgl.client.common;

public abstract class UnauthenticatedRequestBuilder extends HeaderRequestBuilder
{
	public UnauthenticatedRequestBuilder(String type, String entity, String action)
	{
		super(type, entity, action, null);
	}

	public final boolean shouldAddAuthorizationDetails()
	{
		return false;
	}
}