package com.mobicule.component.json;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mobicule.component.util.CoreMobiculeLogger;

public class JSONUtil
{
	public static void setJSONProperty(JSONObject jsonObject, String key, Object value)
	{
		try
		{
			jsonObject.put(key, value);
		}
		catch (JSONException e)
		{
			// log error
			CoreMobiculeLogger.log("JSONUTIL: setJSONProperty", e);
		}
	}

	public static Object getJSONProperty(JSONObject jsonObject, String key)
	{
		try
		{
			return jsonObject.get(key);
		}
		catch (JSONException e)
		{
			// log error
			CoreMobiculeLogger.log("JSONUTIL: getJSONProperty", e);
		}
		return null;
	}

	public static Object get(JSONArray jsonArray, int index)
	{
		try
		{
			return jsonArray.get(index);
		}
		catch (JSONException e)
		{
			// log error
			CoreMobiculeLogger.log("JSONUTIL: get", e);
		}
		return null;
	}

	public static boolean isValid(JSONObject jsonObject)
	{
		return ((jsonObject != null) && jsonObject.length() > 0) ? true : false;
	}

	public static boolean isValid(JSONArray jsonArray)
	{
		return ((jsonArray != null) && jsonArray.length() > 0) ? true : false;
	}
	
	public static Object removeJSONProperty(JSONObject jsonObject, String key)
	{
		try
		{
			if(jsonObject.has(key))
			{
				return jsonObject.remove(key);
			}
		}
		catch (Exception e)
		{
			CoreMobiculeLogger.log("JSONUTIL: getJSONProperty", e);
		}
		return null;
	}
}
