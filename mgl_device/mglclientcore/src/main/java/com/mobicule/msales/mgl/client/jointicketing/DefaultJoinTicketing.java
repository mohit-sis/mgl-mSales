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

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.mobicule.component.json.JSONUtil;

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
public class DefaultJoinTicketing implements IJoinTicketing
{
	private static final String KEY_JT_SUBMIT = "jtSubmit";

	private IJoinTicketing joinTicketingBO;

	private JSONArray joinTicketingJsonArray;

	private JSONObject jointicket;

	public DefaultJoinTicketing()
	{
		init();
	}

	private void init()
	{
		jointicket = new JSONObject();
	}

	public void reset()
	{
		init();
	}

	public DefaultJoinTicketing(JSONObject jointicket)
	{
		this.jointicket = jointicket;
	}

	public Object toJSON()
	{
		return jointicket;
	}

	@Override
	public void setMruCode(String mruCode)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_MRU_CODE, mruCode);
	}

	@Override
	public String getMruCode()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_MRU_CODE);
	}

	public void setJoinTicketingInstance(IJoinTicketing instance)
	{
		joinTicketingJsonArray.add(instance.toJSON());
	}

	public IJoinTicketing getJoinTicketingInstance(String mroNo)
	{
		for (int i = 0; i < joinTicketingJsonArray.length(); i++)
		{
			IJoinTicketing instance = new DefaultJoinTicketing();
			instance.init((JSONObject) JSONUtil.get(joinTicketingJsonArray, i));
			if (mroNo.equalsIgnoreCase((String) instance.getMroNo()))
			{
				return instance;
			}
		}

		return null;
	}

	public IJoinTicketing getInstance()
	{

		joinTicketingBO = new DefaultJoinTicketing();

		return joinTicketingBO;
	}

	public void init(JSONObject jsonObject)
	{
		joinTicketingJsonArray = new JSONArray();

	}

	@Override
	public void setBpNo(String bpno)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_BP_NO, bpno);
	}

	@Override
	public String getBpNo()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_BP_NO);
	}

	@Override
	public void setMroNo(String mrono)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_MRO_NO, mrono);
	}

	@Override
	public String getMroNo()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_MRO_NO);
	}

	@Override
	public void setMrsImage(String mrs)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_MRS_IMAGE_1, mrs);
	}

	@Override
	public String getMrsImage()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_MRS_IMAGE_1);
	}

	@Override
	public void setTurbineImage(String turbine)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_TURBINE_IMAGE, turbine);
	}

	@Override
	public String getTurbineImage()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_TURBINE_IMAGE);
	}

	@Override
	public void setTurbineMeterReading(String turbinereading)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_TURBINE_METER_READING, turbinereading);
	}

	@Override
	public String getTurbineMeterReading()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_TURBINE_METER_READING);
	}

	@Override
	public void setTurbineSealImage(String seal)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_TURBINE_SEAL_IMAGE, seal);
	}

	@Override
	public String getTurbineSealImage()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_TURBINE_SEAL_IMAGE);
	}

	@Override
	public void setEvcImage1(String evc1)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_EVC_IMAGE_1, evc1);
	}

	@Override
	public String getEvcImage1()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_EVC_IMAGE_1);
	}

	@Override
	public void setCorrectedValue(String corrected)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_CORRECTED_VALUE, corrected);
	}

	@Override
	public String getCorrectedValue()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_CORRECTED_VALUE);
	}

	@Override
	public void setUnCorrectedValue(String uncorrected)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_UNCORRECTED_VALUE, uncorrected);
	}

	@Override
	public String getUnCorrectedValue()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_UNCORRECTED_VALUE);
	}

	@Override
	public void setEvcImage2(String evc2)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_EVC_IMAGE_2, evc2);
	}

	@Override
	public String getEvcImage2()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_EVC_IMAGE_2);
	}

	@Override
	public void setTemperature(String temperature)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_TEMPERATURE, temperature);
	}

	@Override
	public String getTemperature()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_TEMPERATURE);
	}

	@Override
	public void setEvcSealImage(String evcSeal)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_EVC_SEAL_IMAGE, evcSeal);
	}

	@Override
	public String getEvcSealImage()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_EVC_SEAL_IMAGE);
	}

	@Override
	public void setMrsLockedImage(String mrsLocked)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_MRS_LOCKED_IMAGE, mrsLocked);
	}

	@Override
	public String getMrsLockedImage()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_MRS_LOCKED_IMAGE);
	}

	@Override
	public void setSignaturePath(String client)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_SIGNATURE_PATH, client);
	}

	@Override
	public String getSignaturePath()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_SIGNATURE_PATH);
	}

	@Override
	public void setDeviceTime(String time)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_DEVICE_TIME, time);
	}

	@Override
	public String getDeviceTime()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_DEVICE_TIME);
	}

	@Override
	public void setDeviceDate(String date)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_DEVICE_DATE, date);
	}

	@Override
	public String getDeviceDate()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_DEVICE_DATE);
	}

	@Override
	public void setJtSubmitted(String isReadingSubmittedToServer)//0-No, 1-yes
	{
		JSONUtil.setJSONProperty(jointicket, KEY_JT_SUBMIT, isReadingSubmittedToServer);
	}

	@Override
	public String getJtSubmitted()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_JT_SUBMIT);
	}

	@Override
	public void setCorrectionFactor(String correctionFactor)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_CORRECTION_FACTOR, correctionFactor);
	}

	@Override
	public String getCorrectionFactor()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_CORRECTION_FACTOR);
	}

	@Override
	public void setOutletPressure(String outletPre)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_OUTLET_PRESSURE, outletPre);
	}

	@Override
	public String getOutletPressure()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_OUTLET_PRESSURE);
	}

	@Override
	public void setJoinRemarks(String remarks)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_REMARKS, remarks);
	}

	@Override
	public String getJoinRemarks()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_REMARKS);
	}

	@Override
	public void setMeterReaderSign(String sign)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_METER_READER_SIGNATURE, sign);
	}

	@Override
	public String getMeterReaderSign()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_METER_READER_SIGNATURE);
	}

	@Override
	public void setClientSign(String sign)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_CLIENT_SIGNATURE, sign);
	}

	@Override
	public String getClientSign()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_CLIENT_SIGNATURE);
	}

	public void setNoOfAttempts(String noOfAttempts)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_NO_OF_ATTEMPTS, noOfAttempts);
	}

	@Override
	public void setConnObj(String connObj)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_CONN_OBJ, connObj);
	}

	@Override
	public String getConnObj()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_CONN_OBJ);
	}

	@Override
	public void setMeterNumber(String meterNumber)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_METER_NUMBER, meterNumber);
	}

	@Override
	public String getMeterNumber()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_METER_NUMBER);
	}

	public void resetReadingList()
	{
		joinTicketingJsonArray = new JSONArray();
		JSONUtil.setJSONProperty(jointicket, KEY_READINGS, joinTicketingJsonArray);
	}

	@Override
	public void setStatus(String status)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_STATUS, status);
	}

	@Override
	public String getStatus()
	{

		return (String) JSONUtil.getJSONProperty(jointicket, KEY_STATUS);
	}

	@Override
	public void setArea(String area)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_AREA, area);
	}

	@Override
	public String getArea()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_AREA);
	}

	@Override
	public void setMeterBoxLeftSideImage(String meterBoxLeftSideImage)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_METER_LEFT_SIDE_IMAGE, meterBoxLeftSideImage);
		
	}

	@Override
	public String getMeterBoxLeftSideImage()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_METER_LEFT_SIDE_IMAGE);
	}

	@Override
	public void setMeterBoxRightSideImage(String meterBoxRightSideImage)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_METER_RIGHT_SIDE_IMAGE, meterBoxRightSideImage);
	}

	@Override
	public String getMeterBoxRightSideImage()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_METER_RIGHT_SIDE_IMAGE);
	}

	@Override
	public void setMeterBoxRearSideImage(String meterBoxRearSideImage)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_METER_REAR_SIDE_IMAGE, meterBoxRearSideImage);
	}

	@Override
	public String getMeterBoxRearSideImage()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_METER_REAR_SIDE_IMAGE);
	}

	@Override
	public void setMeterBoxRearSideImageRemark(String meterBoxRearSideImageRemark)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_METER_REAR_SIDE_IMAGE_REMARK, meterBoxRearSideImageRemark);
	}

	@Override
	public String getMeterBoxRearSideImageRemark()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_METER_REAR_SIDE_IMAGE_REMARK);
	}

	@Override
	public void setMeterBoxRightSideImageRemark(String meterBoxRightSideImageRemark)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_METER_RIGHT_SIDE_IMAGE_REMARK, meterBoxRightSideImageRemark);
		
	}

	@Override
	public String getMeterBoxRightSideImageRemark()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_METER_RIGHT_SIDE_IMAGE_REMARK);
	}

	@Override
	public void setMeterBoxLeftSideImageRemark(String meterBoxLeftSideImageRemark)
	{
		JSONUtil.setJSONProperty(jointicket, KEY_METER_LEFT_SIDE_IMAGE_REMARK, meterBoxLeftSideImageRemark);
		
	}

	@Override
	public String getMeterBoxLeftSideImageRemark()
	{
		return (String) JSONUtil.getJSONProperty(jointicket, KEY_METER_LEFT_SIDE_IMAGE_REMARK);
	}
}