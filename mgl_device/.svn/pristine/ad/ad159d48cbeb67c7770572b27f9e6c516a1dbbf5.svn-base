/**
 * 
 */
package com.mobicule.msales.mgl.client.timevariation;

import org.json.me.JSONObject;

import com.mobicule.component.json.JSONUtil;
import com.mobicule.component.string.StringUtil;

/**
 * @author nikita
 *
 */
public class DefaultTimeVariation implements ITimeVariation
{

	private JSONObject timeVariationJson;

	public DefaultTimeVariation()
	{
		init();
	}

	public void init()
	{
		timeVariationJson = new JSONObject();
	}

	public void reset()
	{
		init();
	}

	public Object toJSON()
	{
		return timeVariationJson;
	}

	public void setDeviceDate(String deviceDate)
	{
		if (StringUtil.isValid(deviceDate))
		{
			JSONUtil.setJSONProperty(timeVariationJson, KEY_DEVICE_DATELIST, deviceDate);
		}

	}

	public String getDeviceDate()
	{
		return (String) JSONUtil.getJSONProperty(timeVariationJson, KEY_DEVICE_DATELIST);
	}

	public void setDeviceTime(String deviceTime)
	{
		if (StringUtil.isValid(deviceTime))
		{
			JSONUtil.setJSONProperty(timeVariationJson, KEY_DEVICE_TIMELIST, deviceTime);
		}
	}

	public String getDeviceTime()
	{
		return (String) JSONUtil.getJSONProperty(timeVariationJson, KEY_DEVICE_TIMELIST);
	}

}
