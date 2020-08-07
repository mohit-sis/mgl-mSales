/**
******************************************************************************
* C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
* <p>
* Copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved. 
* This is proprietary information of Mobicule Technologies Pvt. Ltd.and is 
* subject to applicable licensing agreements. Unauthorized reproduction, 
* transmission or distribution of this file and its contents is a 
* violation of applicable laws.
******************************************************************************
*
* @project mgl-client-core
*/
package com.mobicule.msales.mgl.client.jointicketing;

import org.json.me.JSONObject;

import com.mobicule.msales.mgl.client.common.IJSONDataStore;

/**
* 
* <enter description here>
*
* @author Ashish Shukla>
* @see 
*
* @createdOn 14-Apr-2014
* @modifiedOn
*
* @copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public interface IJoinTicketing extends IJSONDataStore
{
	static final String KEY_BP_NO = "bpNo";

	static final String KEY_MRO_NO = "mroNo";

	static final String KEY_MRU_CODE = "mruCode";

	static final String KEY_READINGS = "readings";

	static final String KEY_MRS_IMAGE_1 = "mrsImageFirst";

	static final String KEY_TURBINE_IMAGE = "turbineImage";

	static final String KEY_TURBINE_METER_READING = "turbineMeterReading";

	static final String KEY_TURBINE_SEAL_IMAGE = "turbineSealImage";

	static final String KEY_EVC_IMAGE_1 = "evcImageFirst";

	static final String KEY_CORRECTED_VALUE = "correctValue";

	static final String KEY_UNCORRECTED_VALUE = "uncorrectValue";

	static final String KEY_EVC_IMAGE_2 = "evcImageSecond";

	static final String KEY_TEMPERATURE = "temperature";

	static final String KEY_CORRECTION_FACTOR = "correctionFactor";

	static final String KEY_REMARKS = "remarks";

	static final String KEY_EVC_SEAL_IMAGE = "evcSealImage";

	static final String KEY_MRS_LOCKED_IMAGE = "mrsLockedImage";
	
	static final String KEY_METER_RIGHT_SIDE_IMAGE = "meterRightSideImage";
	
	static final String KEY_METER_LEFT_SIDE_IMAGE = "meterLeftSideImage";
	
	static final String KEY_METER_REAR_SIDE_IMAGE = "meterRearSideImage";
	
	static final String KEY_METER_REAR_SIDE_IMAGE_REMARK = "meterRearSideImageRemark";
	
	static final String KEY_METER_RIGHT_SIDE_IMAGE_REMARK = "meterRightSideImageRemark";
	
	static final String KEY_METER_LEFT_SIDE_IMAGE_REMARK = "meterLeftSideImageRemark";

	static final String KEY_METER_READER_SIGNATURE = "meterReaderSignature";

	static final String KEY_CLIENT_SIGNATURE = "clientSignature";

	static final String KEY_SIGNATURE_PATH = "signaturePath";

	static final String KEY_DEVICE_TIME = "deviceTime";

	static final String KEY_DEVICE_DATE = "deviceDate";

	static final String KEY_OUTLET_PRESSURE = "pressure";

	static final String KEY_NO_OF_ATTEMPTS = "noOfAttempts";

	static final String KEY_CONN_OBJ = "connObj";

	static final String KEY_STATUS = "status";

	static final String KEY_METER_NUMBER = "meterNumber";

	static final String KEY_AREA = "area";

	public void setJoinTicketingInstance(IJoinTicketing instance);

	public IJoinTicketing getJoinTicketingInstance(String mroNo);

	public IJoinTicketing getInstance();

	public void setBpNo(String bpno);

	public String getBpNo();

	public void setMroNo(String mroNumber);

	public String getMroNo();

	public void setMrsImage(String mrs);

	public String getMrsImage();

	public void setTurbineImage(String turbine);

	public String getTurbineImage();

	public void setTurbineMeterReading(String turbinereading);

	public String getTurbineMeterReading();

	public void setTurbineSealImage(String seal);

	public String getTurbineSealImage();

	public void setEvcImage1(String evc1);

	public String getEvcImage1();

	public void setCorrectedValue(String corrected);

	public String getCorrectedValue();

	public void setUnCorrectedValue(String uncorrected);

	public String getUnCorrectedValue();

	public void setEvcImage2(String evc2);

	public String getEvcImage2();

	public void setTemperature(String temperature);

	public String getTemperature();

	public void setCorrectionFactor(String correctionFactor);

	public String getCorrectionFactor();

	public void setEvcSealImage(String evcSeal);

	public String getEvcSealImage();

	public void setMrsLockedImage(String mrsLocked);

	public String getMrsLockedImage();
	
	public void setMeterBoxLeftSideImage(String meterBoxLeftSideImage);

	public String getMeterBoxLeftSideImage();
	
	public void setMeterBoxRightSideImage(String meterBoxRightSideImage);

	public String getMeterBoxRightSideImage();
	
	public void setMeterBoxRearSideImage(String meterBoxRearSideImage);

	public String getMeterBoxRearSideImage();
	
	public void setMeterBoxRearSideImageRemark(String meterBoxRearSideImageRemark);

	public String getMeterBoxRearSideImageRemark();
	
	public void setMeterBoxRightSideImageRemark(String meterBoxRightSideImageRemark);

	public String getMeterBoxRightSideImageRemark();
	
	public void setMeterBoxLeftSideImageRemark(String meterBoxLeftSideImageRemark);

	public String getMeterBoxLeftSideImageRemark();

	public void setSignaturePath(String client);

	public String getSignaturePath();

	public void setDeviceTime(String time);

	public String getDeviceTime();

	public void setDeviceDate(String date);

	public String getDeviceDate();

	public void setJtSubmitted(String isReadingSubmittedToServer);

	public String getJtSubmitted();

	public void setMruCode(String mruCode);

	public String getMruCode();

	public void init(JSONObject jsonObject);

	public void reset();

	public void setOutletPressure(String outletPre);

	public String getOutletPressure();

	public void setJoinRemarks(String remarks);

	public String getJoinRemarks();

	public void setMeterReaderSign(String sign);

	public String getMeterReaderSign();

	public void setClientSign(String sign);

	public String getClientSign();

	public void setConnObj(String connObj);

	public String getConnObj();

	public void setStatus(String status);

	public String getStatus();

	public void setMeterNumber(String meterNumber);

	public String getMeterNumber();

	public void resetReadingList();

	public void setArea(String area);

	public String getArea();
}