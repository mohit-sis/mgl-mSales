/**
******************************************************************************
* C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
* <p>
* Copyright ? 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved. 
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

import com.mobicule.component.json.JSONUtil;
import com.mobicule.component.string.StringUtil;

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
* @copyright ? 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class DefaultRandomMeterReading implements IRandomMeterReading
{

	private JSONArray randomMeterRedingJsonArray;

	public DefaultRandomMeterReading()
	{
		init();
	}

	public void init()
	{
		randomMeterRedingJsonArray = new JSONArray();
	}

	public void reset()
	{
		init();
	}

	public IRandomMeterReadingInstance getMeterReadingInstance(String customerName)
	{
		for (int i = 0; i < randomMeterRedingJsonArray.length(); i++)
		{
			IRandomMeterReadingInstance instance = new DefaultRandomMeterReadingInstance();
			instance.init((JSONObject) JSONUtil.get(randomMeterRedingJsonArray, i));
			if (customerName.equalsIgnoreCase((String) instance.getCustomerName()))
			{
				return instance;
			}
		}

		return null;
	}

	public void setRandomMeterReadingInstance(IRandomMeterReadingInstance instance)
	{
		randomMeterRedingJsonArray.add(instance.toJSON());
	}

	public JSONArray toJsonArray()
	{
		return randomMeterRedingJsonArray;
	}

	public static class DefaultRandomMeterReadingInstance implements IRandomMeterReadingInstance
	{

		private JSONObject randomMeterReadingJson;

		private JSONObject readingCounts;

		/*private String status;

		private String statusValue;*/

		private static final String KEY_RMR_SUBMIT = "rmrSubmit";

		private static final String KEY_STATUS = "SaveRMRStatus";

		private static final String KEY_STATUS_VALUE = "RMRStatus";

		public DefaultRandomMeterReadingInstance()
		{
			init();
		}

		private void init()
		{
			randomMeterReadingJson = new JSONObject();
			resetReadingList();
		}

		public void init(JSONObject randomMeterReadingData)
		{
			randomMeterReadingJson = randomMeterReadingData;
			if (JSONUtil.getJSONProperty(randomMeterReadingJson, KEY_READINGS) != null)
			{
				if (JSONUtil.getJSONProperty(randomMeterReadingJson, KEY_READINGS) instanceof JSONObject)
				{
					readingCounts = (JSONObject) JSONUtil.getJSONProperty(randomMeterReadingJson, KEY_READINGS);
				}
			}
		}

		public void reset()
		{
			init();
		}

		public void resetReadingList()
		{
			readingCounts = new JSONObject();
			JSONUtil.setJSONProperty(randomMeterReadingJson, KEY_READINGS, readingCounts);
		}

		public String getApplicationFormNo()
		{
			return (String) JSONUtil.getJSONProperty(randomMeterReadingJson, KEY_APP_FORM_NO);
		}

		public String getBpNumber()
		{
			return (String) JSONUtil.getJSONProperty(randomMeterReadingJson, KEY_BP_NO);
		}

		public String getCANo()
		{
			return (String) JSONUtil.getJSONProperty(randomMeterReadingJson, KEY_CA_NO);
		}

		public String getCustomerContactNo()
		{
			return (String) JSONUtil.getJSONProperty(randomMeterReadingJson, KEY_CUSTOMER_CONTACT);
		}

		public String getCustomerName()
		{
			return (String) JSONUtil.getJSONProperty(randomMeterReadingJson, KEY_CUSTOMER_NAME);
		}

		public void setImage(String image)
		{
			JSONUtil.setJSONProperty(randomMeterReadingJson, KEY_IMAGES, image);
		}

		public String getImage()
		{
			return (String) JSONUtil.getJSONProperty(randomMeterReadingJson, KEY_IMAGES);
		}
		
		public void setImage2(String image)
		{
			JSONUtil.setJSONProperty(randomMeterReadingJson, KEY_IMAGES2, image);
		}

		public String getImage2()
		{
			return (String) JSONUtil.getJSONProperty(randomMeterReadingJson, KEY_IMAGES2);
		}

		public String getMeterNumber()
		{
			return (String) JSONUtil.getJSONProperty(randomMeterReadingJson, KEY_METER_NO);
		}

		public String getReading()
		{
			//			System.out.println("Default Reading " + readingCounts);
			//			JSONObject jsonObject = null;
			//			try
			//			{
			//				jsonObject = readingCounts.toJSONArray(names)
			//				System.out.println("DefaultRandomMeterReadingInstance  getReading   :  " + jsonObject);
			//
			//			}
			//			catch (JSONException e)
			//			{
			//				e.printStackTrace();
			//			}
			//
			DefaultReadings readings = new DefaultRandomMeterReading.DefaultReadings(readingCounts);
			return readings.getMeterReading();
		}

		public String getSelectedStatus()
		{
			return (String) JSONUtil.getJSONProperty(randomMeterReadingJson, KEY_STATUS);
		}

		public String getStatusValue()
		{
			return (String) JSONUtil.getJSONProperty(randomMeterReadingJson, KEY_STATUS_VALUE);
		}

		public void setSelectedStatus(String status)
		{
			JSONUtil.setJSONProperty(randomMeterReadingJson, KEY_STATUS, status);
		}

		public void setStatusValue(String statusValue)
		{
			JSONUtil.setJSONProperty(randomMeterReadingJson, KEY_STATUS_VALUE, statusValue);
		}

		public void setApplicationFormNo(String applicationFormNo)
		{
			JSONUtil.setJSONProperty(randomMeterReadingJson, KEY_APP_FORM_NO, applicationFormNo);
		}

		public void setBpNumber(String bpNumber)
		{
			JSONUtil.setJSONProperty(randomMeterReadingJson, KEY_BP_NO, bpNumber);
		}

		public void setCANo(String caNumb)
		{
			JSONUtil.setJSONProperty(randomMeterReadingJson, KEY_CA_NO, caNumb);
		}

		public void setCustomerContactNo(String customerContactNo)
		{
			JSONUtil.setJSONProperty(randomMeterReadingJson, KEY_CUSTOMER_CONTACT, customerContactNo);
		}

		public void setCustomerName(String customerName)
		{
			JSONUtil.setJSONProperty(randomMeterReadingJson, KEY_CUSTOMER_NAME, customerName);
		}

		public void setMeterNumber(String meterNumber)
		{
			JSONUtil.setJSONProperty(randomMeterReadingJson, KEY_METER_NO, meterNumber);
		}

		public void setReadings(IReadings readings)
		{
			JSONUtil.setJSONProperty(randomMeterReadingJson, KEY_READINGS, readings.toJSON());
			//readingCounts = (JSONObject) readings;
		}

		public Object toJSON()
		{
			return randomMeterReadingJson;
		}

		public void setMrSubmitted(String isReadingSubmittedToServer)
		{
			JSONUtil.setJSONProperty(randomMeterReadingJson, KEY_RMR_SUBMIT, isReadingSubmittedToServer);
		}

		public String getMrSubmitted()
		{
			return (String) JSONUtil.getJSONProperty(randomMeterReadingJson, KEY_RMR_SUBMIT);
		}

	}

	public static class DefaultReadings implements IReadings
	{

		private static final String KEY_READING_TIME_LIST = "time";//common key for MR and RMR

		private static final String KEY_METER_READING = "meterReading";

		private static final String KEY_METER_READING_DATE = "date";//common key for MR and RMR

		private JSONObject readings;

		public DefaultReadings()
		{
			readings = new JSONObject();
		}

		public DefaultReadings(JSONObject readings)
		{
			this.readings = readings;
		}

		public String getMeterReading()
		{
			return (String) JSONUtil.getJSONProperty(readings, KEY_METER_READING);
		}

		public String getReadingTime()
		{
			return (String) JSONUtil.getJSONProperty(readings, KEY_READING_TIME_LIST);
		}

		public void setMeterReading(String meterReading)
		{
			JSONUtil.setJSONProperty(readings, KEY_METER_READING, meterReading);
		}

		public String getDate()
		{
			return (String) JSONUtil.getJSONProperty(readings, KEY_METER_READING_DATE);
		}

		public void setDate(String date)
		{
			if (StringUtil.isValid(date))
			{
				JSONUtil.setJSONProperty(readings, KEY_METER_READING_DATE, date);
			}
		}

		public void setReadingTime(String readingTime)
		{
			if (StringUtil.isValid(readingTime))
			{
				JSONUtil.setJSONProperty(readings, KEY_READING_TIME_LIST, readingTime);
			}
		}

		public Object toJSON()
		{
			return readings;
		}

	}

}
