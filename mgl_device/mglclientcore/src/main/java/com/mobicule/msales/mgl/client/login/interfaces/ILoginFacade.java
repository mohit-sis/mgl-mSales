package com.mobicule.msales.mgl.client.login.interfaces;

import com.mobicule.msales.mgl.client.common.Response;

public interface ILoginFacade
{
	public abstract Response isOffineLoginMode();

	public abstract Response signIn(String username, String password, String imeinumber);

	public abstract void setOnlineLoginFlag(boolean isOnlineLogin);
}