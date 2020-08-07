package com.mobicule.msales.mgl.client.meterreading;

import org.json.me.JSONObject;

import com.mobicule.msales.mgl.client.common.StringAuthenticatedRequestBuilder;

public abstract class StringMeterReadingRequestBuilder extends StringAuthenticatedRequestBuilder
{
	private static final String TYPE = "meterreading";

	private static final String METER_READING_ACTION = "submit";

	public StringMeterReadingRequestBuilder(String entity, JSONObject userJson)
	{
		super(TYPE, entity, METER_READING_ACTION, userJson);
	}

}
