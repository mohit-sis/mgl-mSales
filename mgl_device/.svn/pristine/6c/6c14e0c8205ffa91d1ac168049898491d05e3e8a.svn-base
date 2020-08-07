/**
 * 
 */
package com.mobicule.msales.mgl.client.meterreading;

import org.json.me.JSONObject;

/**
 * @author nikita
 *
 */
public class ScheduledMeterReadingRequestBuilder extends MeterReadingRequestBuilder
{
	private static final String ENTITY = "scheduled";

	public ScheduledMeterReadingRequestBuilder(JSONObject userJson, IMeterReading meterReading)
	{
		super(ENTITY, userJson);
		setRequestDataJsonArray(meterReading.toJsonArray());
	}

	public final boolean shouldAddJsonArray()
	{
		return true;
	}
}
