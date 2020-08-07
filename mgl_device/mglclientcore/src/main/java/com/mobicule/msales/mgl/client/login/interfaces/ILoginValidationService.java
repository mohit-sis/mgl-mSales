package com.mobicule.msales.mgl.client.login.interfaces;

import com.mobicule.msales.mgl.client.common.Response;

public interface ILoginValidationService
{
	public Response validateInput(String username, String password, String imeinumber);
}
