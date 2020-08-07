package com.mobicule.msales.mgl.client.onmPlanning;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

public class OnMPlanningRequestBuilder extends OnMPlanning
{

	private static final String ENTITY = "scheduled";
	
	public OnMPlanningRequestBuilder(JSONObject jsonObject, JSONObject onMPlanning)
	{
		super(ENTITY, jsonObject);
		//setRequestData((JSONObject) updateCustomer.toJSON());
		//setRequestData(onMPlanning);
		JSONArray onMDataJsonArray = new JSONArray();
		onMDataJsonArray.add(onMPlanning);
		setRequestDataJsonArray(onMDataJsonArray);
	}

	@Override
	public boolean shouldAddJsonArray()
	{
		return true;
	}
	
}
