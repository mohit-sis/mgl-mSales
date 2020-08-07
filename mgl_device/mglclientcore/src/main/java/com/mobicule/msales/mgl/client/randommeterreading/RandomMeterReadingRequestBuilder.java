/**
******************************************************************************
* C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
* <p>
* Copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved. 
* This is proprietary information of Mobicule Technologies Pvt. Ltd.and is 
* subject to applicable licensing agreements. Unauthorized reproduction, 
* transmission or distribution of this file and its contents is a 
* violation of applicable laws.
******************************************************************************
*
* @project mgl-client-core
*/
package com.mobicule.msales.mgl.client.randommeterreading;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.mobicule.msales.mgl.client.meterreading.MeterReadingRequestBuilder;

/**
* 
* <enter description here>
*
* @author nikita
* @see 
*
* @createdOn Mar 20, 2012
* @modifiedOn
*
* @copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class RandomMeterReadingRequestBuilder extends MeterReadingRequestBuilder
{

	private static final String ENTITY = "random";

	public RandomMeterReadingRequestBuilder(JSONObject userJson, JSONArray randomMeterReading)
	{
		super(ENTITY, userJson);
		//setRequestDataJsonArray(randomMeterReading.toJsonArray());
		setRequestDataJsonArray(randomMeterReading);
	}

	public boolean shouldAddJsonArray()
	{
		return true;
	}

}
