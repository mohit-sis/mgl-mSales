package com.mobicule.msales.mgl.client.common;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mobicule.component.util.CoreMessages;

public abstract class HeaderRequestBuilder implements IRequestBuilder
{
	public static final String IMEINUMBER = "imei";

	public static final String PASSWORD = "pass";

	public static final String LOGIN = "name";

	public static final String ENTITY = "entity";

	public static final String ACTION = "action";

	public static final String TYPE = "type";

	public static final String DATA = "data";

	public static final String USER = "user";

	public static final String KEY_VERSION_CONTROL = "version";
	
	private String type;

	private String entity;

	private String action;

	private JSONObject requestJson;

	private JSONObject userJson;

	protected JSONObject dataJson;

	protected JSONArray dataJsonArray;

	public HeaderRequestBuilder(String type, String entity, String action, JSONObject userJson)
	{
		super();
		this.type = type;
		this.entity = entity;
		this.action = action;
		this.userJson = userJson;

		requestJson = new JSONObject();
		dataJsonArray = new JSONArray();
	}

	protected void initDataJson()
	{
		dataJson = new JSONObject();
	}

	private void addRequestHeader()
	{
		try
		{
			requestJson.put(TYPE, type);
			requestJson.put(ENTITY, entity);
			requestJson.put(ACTION, action);
		}
		catch (JSONException e)
		{
			throw new RequestBuilderException(CoreMessages.EXCEPTION_ON_ADDING_HEADER_DATA);
		}
	}

	public void addAuthorizationDetails()
	{
		try
		{
			requestJson.put(USER, userJson);
		}
		catch (JSONException e)
		{
			throw new RequestBuilderException(CoreMessages.EXCEPTION_ON_ADDING_REQUEST_DATA);
		}
	}

	private void addRequestData()
	{
		try
		{
			requestJson.put(DATA, dataJson);

		}
		catch (JSONException e)
		{
			throw new RequestBuilderException(CoreMessages.EXCEPTION_ON_ADDING_REQUEST_DATA);
		}
	}

	private void addRequestDataJsonArray()
	{
		try
		{
			requestJson.put(DATA, dataJsonArray);

		}
		catch (JSONException e)
		{
			throw new RequestBuilderException(CoreMessages.EXCEPTION_ON_ADDING_REQUEST_DATA);
		}
	}

	public final JSONObject build()
	{
		addRequestHeader();

		if (shouldAddAuthorizationDetails())
		{
			addAuthorizationDetails();
		}
		if (shouldAddJsonArray())
		{
			addRequestDataJsonArray();
		}
		else
		{
			addRequestData();
		}
		return requestJson;
	}

	public abstract boolean shouldAddAuthorizationDetails();

	public abstract boolean shouldAddJsonArray();

	public void put(String key, Object value)
	{
		try
		{
			if (dataJson == null)
			{
				initDataJson();
			}
			dataJson.put(key, value);
		}
		catch (JSONException e)
		{
			throw new RequestBuilderException(CoreMessages.EXCEPTION_ON_ADDING_DATA);
		}
	}

	public void setRequestData(JSONObject value)
	{
		dataJson = value;
	}

	public void setRequestDataJsonArray(JSONArray value)
	{
		dataJsonArray = value;
	}
}
