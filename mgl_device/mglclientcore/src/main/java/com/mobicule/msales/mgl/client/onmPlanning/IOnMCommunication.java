package com.mobicule.msales.mgl.client.onmPlanning;

import org.json.me.JSONObject;

import com.mobicule.msales.mgl.client.common.Response;

public interface IOnMCommunication
{
	public Response submit(JSONObject jsonObject);
}
