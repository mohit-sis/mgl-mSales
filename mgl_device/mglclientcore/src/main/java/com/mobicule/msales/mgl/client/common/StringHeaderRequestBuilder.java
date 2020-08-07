package com.mobicule.msales.mgl.client.common;

import org.json.me.JSONObject;

public abstract class StringHeaderRequestBuilder implements IStringRequestBuilder
{
	public static final String IMEINUMBER = "imei";

	public static final String PASSWORD = "pass";

	public static final String LOGIN = "name";

	public static final String ENTITY = "entity";

	public static final String ACTION = "action";

	public static final String TYPE = "type";

	public static final String DATA = "data";

	public static final String USER = "user";

	private String type;

	private String entity;

	private String action;

	private StringBuffer requestJson;

	private JSONObject userJson;

	protected String dataJsonArray;

	public StringHeaderRequestBuilder(String type, String entity, String action, JSONObject userJson)
	{
		super();
		this.type = type;
		this.entity = entity;
		this.action = action;
		this.userJson = userJson;
		requestJson = new StringBuffer();
		dataJsonArray = new String();
	}

	private void addRequestHeader()
	{
		requestJson.append('"');
		requestJson.append(TYPE);
		requestJson.append('"');
		requestJson.append(':');
		requestJson.append('"');
		requestJson.append(type);
		requestJson.append('"');
		//requestJson.append(TYPE + ':' + type);
		requestJson.append(',');

		requestJson.append('"');
		requestJson.append(ENTITY);
		requestJson.append('"');
		requestJson.append(':');
		requestJson.append('"');
		requestJson.append(entity);
		requestJson.append('"');
		//requestJson.append(ENTITY + ':' + entity);
		requestJson.append(',');

		requestJson.append('"');
		requestJson.append(ACTION);
		requestJson.append('"');
		requestJson.append(':');
		requestJson.append('"');
		requestJson.append(action);
		requestJson.append('"');
		//requestJson.append(ACTION + ':' + action);
		requestJson.append(',');
	}

	public void addAuthorizationDetails()
	{
		requestJson.append('"');
		requestJson.append(USER);
		requestJson.append('"');
		requestJson.append(':');

		requestJson.append(userJson);

		//requestJson.append(USER + ':' + userJson);
		requestJson.append(',');
	}

	private void addRequestDataJsonArray()
	{
		requestJson.append('"');
		requestJson.append(DATA);
		requestJson.append('"');
		requestJson.append(':');
		requestJson.append(dataJsonArray);
		//requestJson.append(DATA + ':' + dataJsonArray);
	}

	public abstract boolean shouldAddAuthorizationDetails();

	public void setRequestDataJsonArray(String value)
	{
		dataJsonArray = value;
	}

	public String build()
	{
		requestJson = new StringBuffer();
		requestJson.append('{');
		addRequestHeader();

		if (shouldAddAuthorizationDetails())
		{
			addAuthorizationDetails();
		}
		addRequestDataJsonArray();
		requestJson.append('}');
		return requestJson.toString();
	}

}
