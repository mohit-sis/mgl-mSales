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

import com.mobicule.msales.mgl.client.common.IJSONDataStore;

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
public interface IRandomMeterReading
{
	static final String KEY_METER_NO = "meterNo";

	static final String KEY_BP_NO = "bpNo";

	static final String KEY_IMAGES = "image";
	
	static final String KEY_IMAGES2 = "image2";

	static final String KEY_READINGS = "readings";

	static final String KEY_CUSTOMER_NAME = "customerName";

	static final String KEY_CUSTOMER_CONTACT = "contactNo";

	static final String KEY_APP_FORM_NO = "appFormNo";

	static final String KEY_CA_NO = "caNo";

	public void init();

	public void reset();

	public void setRandomMeterReadingInstance(IRandomMeterReadingInstance instance);

	public IRandomMeterReadingInstance getMeterReadingInstance(String customerName);

	public JSONArray toJsonArray();

	public interface IRandomMeterReadingInstance extends IJSONDataStore
	{
		public void init(JSONObject randomMeterReadingData);

		public void reset();

		public void setSelectedStatus(String status);

		public String getSelectedStatus();

		public void setStatusValue(String statusValue);

		public String getStatusValue();

		public void setMeterNumber(String meterNumber);

		public String getMeterNumber();

		public void setBpNumber(String bpNumber);

		public String getBpNumber();

		public void setApplicationFormNo(String applicationFormNo);

		public String getApplicationFormNo();

		public void setCANo(String caNumb);

		public String getCANo();

		public void setCustomerName(String customerName);

		public String getCustomerName();

		public void setCustomerContactNo(String customerContactNo);

		public String getCustomerContactNo();

		public void setImage(String image);

		public String getImage();
		
		public void setImage2(String image);

		public String getImage2();

		public void setReadings(IReadings readings);

		//public IReadings getReading(String itemCode);

		//public String getReading();

		public void resetReadingList();

		public void setMrSubmitted(String isReadingSubmittedToServer);

		public String getMrSubmitted();

	}

	public interface IReadings extends IJSONDataStore
	{
		public void setMeterReading(String meterReading);

		public String getMeterReading();

		public void setReadingTime(String readingTime);

		public String getReadingTime();

		public void setDate(String date);

		public String getDate();
	}

}
