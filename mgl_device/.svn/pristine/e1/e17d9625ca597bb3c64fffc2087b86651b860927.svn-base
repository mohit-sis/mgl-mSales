/**
 * 
 */
package com.mobicule.msales.mgl.client.meterreading;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mobicule.component.json.JSONUtil;
import com.mobicule.component.string.StringUtil;

/**
 * @author nikita
 *
 */
public class DefaultMeterReading implements IMeterReading
{
	private JSONArray meterRedingJsonArray;

	private static int srNo = 0;

	private static int maxMeterReading = 3;

	public DefaultMeterReading()
	{
		init();
	}

	public void init()
	{
		meterRedingJsonArray = new JSONArray();
	}

	public void reset()
	{
		init();
	}

	public void setMeterReadingInstance(IMeterReadingInstance instance)
	{
		meterRedingJsonArray.add(instance.toJSON());
	}

	public IMeterReadingInstance getMeterReadingInstance(String mroNo)
	{
		for (int i = 0; i < meterRedingJsonArray.length(); i++)
		{
			IMeterReadingInstance instance = new DefaultMeterReadingInstance();
			instance.init((JSONObject) JSONUtil.get(meterRedingJsonArray, i));
			if (mroNo.equalsIgnoreCase((String) instance.getMroNumber()))
			{
				return instance;
			}
		}

		return null;
	}

	public JSONArray toJsonArray()
	{
		return meterRedingJsonArray;
	}

	public static class DefaultMeterReadingInstance implements IMeterReadingInstance
	{
		private static final String KEY_LOCK = "lock";

		private static final String KEY_CA_NO = "caNo";

		private static final String KEY_MR_SUBMIT = "mrSubmit";

		private JSONObject meterReadingJson;

		private JSONArray readingCounts;

		public DefaultMeterReadingInstance()
		{
			init();
		}

		private void init()
		{
			meterReadingJson = new JSONObject();
			resetReadingList();
		}

		public void init(JSONObject meterReadingData)
		{
			meterReadingJson = meterReadingData;
			if (JSONUtil.getJSONProperty(meterReadingJson, KEY_READINGS) != null)
			{
				if (JSONUtil.getJSONProperty(meterReadingJson, KEY_READINGS) instanceof JSONArray)
				{
					readingCounts = (JSONArray) JSONUtil.getJSONProperty(meterReadingJson, KEY_READINGS);
				}
			}
		}

		public void reset()
		{
			init();
		}

		public void resetReadingList()
		{
			readingCounts = new JSONArray();
			JSONUtil.setJSONProperty(meterReadingJson, KEY_READINGS, readingCounts);
		}

		public String getMruCode()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_MRU_CODE);
		}

		public String getBpNumber()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_BP_NO);
		}

		public String getImage()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_IMAGES);
		}

		public String getImage2()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_IMAGES2);
		}

		public String getImage3()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_IMAGES3);
		}

		public String getMeterNumber()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_METER_NO);
		}

		public String getMrReason()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_MR_REASON);
		}

		public String getMroNumber()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_MRO_NO);
		}

		public String getNoOfAttempts()
		{
			String noOfAttempts = (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_NO_OF_ATTEMPTS);
			if (StringUtil.isValid(noOfAttempts))
			{
				return noOfAttempts;
			}
			else
			{
				return "0";
			}
		}

		// mruCode setter
		public void setMruCode(String mruCode)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_MRU_CODE, mruCode);
		}

		public void setBpNumber(String bpNumber)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_BP_NO, bpNumber);
		}
		
		public void setLastMeterReadingDate(String lastMeterReadingDate)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_LAST_METER_READING_DATE, lastMeterReadingDate);
		}
		
		public String getLastMeterReadingDate()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_LAST_METER_READING_DATE);
		}

		public void setImage(String image)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_IMAGES, image);
		}

		public void setImage2(String image2)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_IMAGES2, image2);
		}

		public void setImage3(String image3)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_IMAGES3, image3);
		}

		public void setMeterNumber(String meterNumber)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_METER_NO, meterNumber);
		}

		public void setMrReason(String mrReason)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_MR_REASON, mrReason);
		}

		public void setMroNumber(String mroNumber)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_MRO_NO, mroNumber);
		}

		public void setNoOfAttempts(String noOfAttempts)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_NO_OF_ATTEMPTS, noOfAttempts);
		}

		public String getCustomerName()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_CUSTOMER_NAME);
		}

		public void setCustomerName(String customerName)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_CUSTOMER_NAME, customerName);
		}

		public String getCustomerAddress()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_CUSTOMER_ADDRESS);
		}

		public void setCustomerAddress(String customerAddress)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_CUSTOMER_ADDRESS, customerAddress);

		}

		public String getCustomerContactNo()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_CUSTOMER_CONTACT);
		}

		public void setCustomerContactNo(String customerContactNo)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_CUSTOMER_CONTACT, customerContactNo);
		}

		public Object toJSON()
		{
			return meterReadingJson;
		}

		public void setReadings(IReadings currentReadings)
		{
			try
			{
				if (readingCounts.length() > 0)
				{
					JSONObject jsonObject = null;
					try
					{
						jsonObject = (JSONObject) readingCounts.get(readingCounts.length() - 1);
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
					DefaultReadings savedReading = new DefaultMeterReading.DefaultReadings(jsonObject);

					srNo = Integer.parseInt(savedReading.getSerialNo()) + 1;
				}
				else
				{
					srNo = 0;
				}
				currentReadings.setSerialNo(String.valueOf(srNo));
				if (readingCounts.length() == maxMeterReading)
				{
					readingCounts.remove(readingCounts.get(0));
				}
				readingCounts.add(currentReadings.toJSON());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		public JSONArray getReadingList()
		{
			return readingCounts;
		}

		public String getReading()
		{
			JSONObject jsonObject = null;
			try
			{
				jsonObject = (JSONObject) readingCounts.get(readingCounts.length() - 1);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}

			DefaultReadings readings = new DefaultMeterReading.DefaultReadings(jsonObject);
			return readings.getMeterReading();
		}

		public void setConnObj(String connObj)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_CONN_OBJ, connObj);
		}

		public String getConnObj()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_CONN_OBJ);
		}

		public void setStatus(String status)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_STATUS, status);
		}

		public String getStatus()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_STATUS);
		}

		public void setLock(String lock)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_LOCK, lock);
		}

		public void setCaNo(String caNo)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_CA_NO, caNo);
		}

		public String getCaNo()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_CA_NO);
		}

		public void setArea(String area)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_AREA, area);
		}

		public void setMrSubmitted(String isReadingSubmittedToServer)//0-No, 1-yes
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_MR_SUBMIT, isReadingSubmittedToServer);
		}

		public String getMrSubmitted()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_MR_SUBMIT);
		}

		public void setprevRead(String prevRead)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_CUSTOMER_PREVIOUS_READING, prevRead);
		}

		public String getprevRead()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_CUSTOMER_PREVIOUS_READING);
		}

		public void setCustLandNo(String CustLandNo)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_CUSTOMER_LANDLINE_NO, CustLandNo);
		}

		public String getCustLandNo()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_CUSTOMER_LANDLINE_NO);
		}

		public void setCustOfficeNo(String CustOfficeNo)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_CUSTOMER_OFFICE_NO, CustOfficeNo);
		}

		public String getCustOfficeNo()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_CUSTOMER_OFFICE_NO);
		}

		@Override
		public void setInspection(IInspection inspection)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_INSPECTION, inspection.toJSON());
		}

		@Override
		public JSONObject getInspection()
		{
			return (JSONObject) JSONUtil.getJSONProperty(meterReadingJson, KEY_INSPECTION);
		}

		@Override
		public void removeInspection()
		{
			JSONUtil.removeJSONProperty(meterReadingJson, KEY_INSPECTION);
		}

		@Override
		public void setFlat(String flat)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_FLAT, flat);

		}

		@Override
		public String getFlat()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_FLAT);
		}

		@Override
		public void setFloor(String floor)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_FLOOR, floor);

		}

		@Override
		public String getFloor()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_FLOOR);
		}

		@Override
		public void setPlot(String plot)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_PLOT, plot);

		}

		@Override
		public String getPlot()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_PLOT);
		}

		@Override
		public void setWing(String wing)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_WING, wing);

		}

		@Override
		public String getWing()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_WING);
		}

		@Override
		public void setMsgToMr(String msgToMr)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_MSG_TO_MR, msgToMr);

		}

		@Override
		public String getMsgToMr()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_MSG_TO_MR);
		}

		@Override
		public void setMsgFromMr(String msgFromMr)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_MSG_FROM_MR, msgFromMr);

		}

		@Override
		public String getMsgFromMr()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_MSG_FROM_MR);
		}

		@Override
		public void setMsgRemarks(String msgRemarks)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_MSG_REMARKS, msgRemarks);

		}

		@Override
		public String getMsgRemarksPrimary()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_MSG_FROM_MR_PRIMARY);
		}

		@Override
		public void setMsgRemarksPrimary(String msgRemarksPrimary)
		{
			JSONUtil.setJSONProperty(meterReadingJson, KEY_MSG_FROM_MR_PRIMARY, msgRemarksPrimary);

		}

		
		@Override
		public String getMsgRemarks()
		{
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_MSG_REMARKS);
		}

		@Override
		public void setIsRelocation(String isRelocation)
		{
			// TODO Auto-generated method stub
			JSONUtil.setJSONProperty(meterReadingJson, KEY_IS_RELOCATION, isRelocation);
			
		}

		@Override
		public String getIsRelocation()
		{
			// TODO Auto-generated method stub
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_IS_RELOCATION);
		}

		@Override
		public void setIsLPGSupplier(String isLPGSupplier)
		{
			// TODO Auto-generated method stub
			JSONUtil.setJSONProperty(meterReadingJson, KEY_IS_LPGSUPPLIER, isLPGSupplier);
		}

		@Override
		public String getLPGSupplier()
		{
			// TODO Auto-generated method stub
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_IS_LPGSUPPLIER);
		}

		@Override
		public void setIsSerailCorrect(String isSerailCorrect)
		{
			// TODO Auto-generated method stub
			JSONUtil.setJSONProperty(meterReadingJson, KEY_SERIAL_CORRECT, isSerailCorrect);
		}

		@Override
		public String getIsSerailCorrect()
		{
			// TODO Auto-generated method stub
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_SERIAL_CORRECT);
		}

		@Override
		public void setIsSerailVisible(String isSerialVisible)
		{
			// TODO Auto-generated method stub
			JSONUtil.setJSONProperty(meterReadingJson, KEY_SERIAL_VISIBLE, isSerialVisible);
			
		}

		@Override
		public String getIsSerailVisible()
		{
			// TODO Auto-generated method stub
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_SERIAL_VISIBLE);
		}

		@Override
		public void setIsPremiselCorrect(String isPremiseCorrect)
		{
			// TODO Auto-generated method stub
			JSONUtil.setJSONProperty(meterReadingJson, KEY_PREMISE_CORRECT, isPremiseCorrect);
		}

		@Override
		public String getIsPremiselCorrect()
		{
			// TODO Auto-generated method stub
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_PREMISE_CORRECT);
		}

		@Override
		public void setIsReEnterMeterReading(String isReEnterMeterReading)
		{
			// TODO Auto-generated method stub
			JSONUtil.setJSONProperty(meterReadingJson, KEY_REENTER_METER_READING, isReEnterMeterReading);
		}

		@Override
		public String getReEnterMeterReading()
		{
			// TODO Auto-generated method stub
			return (String) JSONUtil.getJSONProperty(meterReadingJson, KEY_REENTER_METER_READING);
		}

		
		
	}

	public static class DefaultReadings implements IReadings
	{
		private static final String KEY_READING_TIME_LIST = "time";

		private static final String KEY_METER_READING = "meterReading";

		private static final String KEY_METER_READING_DATE = "date";

		private static final String KEY_SERIAL_NO = "srNo";

		private JSONObject readings;

		public DefaultReadings()
		{
			readings = new JSONObject();
			//readingTime = new JSONArray();
			//JSONUtil.setJSONProperty(readings, KEY_READING_TIME_LIST, readingTime);
		}

		public DefaultReadings(JSONObject readings)
		{
			this.readings = readings;
			//	readingTime = (JSONArray) JSONUtil.getJSONProperty(readings, KEY_READING_TIME_LIST);
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

		public void setSerialNo(String srNo)
		{
			JSONUtil.setJSONProperty(readings, KEY_SERIAL_NO, srNo);
		}

		public String getSerialNo()
		{
			String srNo = (String) JSONUtil.getJSONProperty(readings, KEY_SERIAL_NO);
			if (StringUtil.isValid(srNo))
			{
				return srNo;
			}
			else
			{
				return "0";
			}
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

		public String getMrCode()
		{
			return (String) JSONUtil.getJSONProperty(readings, KEY_MR_REASON_CODE);
		}

		public void setMrCode(String mrCode)
		{
			JSONUtil.setJSONProperty(readings, KEY_MR_REASON_CODE, mrCode);
		}

	}

	public static class DefaultInspection implements IInspection
	{
		private static final String KEY_METER_INDEX_TAMPERED = "meterInxTamp";

		private static final String KEY_METER_INDEX_TAMPERED_REMARK = "meterInxTampRem";

		private static final String KEY_METER_PROTECTION_SEAL = "meterProtSeal";

		private static final String KEY_METER_PROTECTION_SEAL_REMARK = "meterProtSealRem";

		private static final String KEY_INPUT_SEAL_NO = "inpSealNo";

		private static final String KEY_METER_BYPASS = "meterByPassArr";

		private static final String KEY_METER_BYPASS_REMARK = "meterByPassArrRem";

		private static final String KEY_CURRENT_SEAL_NO = "sr";

		private JSONObject inspection;

		public DefaultInspection()
		{
			inspection = new JSONObject();
		}

		public DefaultInspection(JSONObject inspection)
		{
			this.inspection = inspection;
		}

		@Override
		public Object toJSON()
		{
			return inspection;
		}

		@Override
		public void setIsMeterIndexTampered(String meterIndexTampered)
		{
			JSONUtil.setJSONProperty(inspection, KEY_METER_INDEX_TAMPERED, meterIndexTampered);
		}

		@Override
		public String isMeterIndexTampered()
		{
			return JSONUtil.getJSONProperty(inspection, KEY_METER_INDEX_TAMPERED).toString();
		}

		@Override
		public void setMeterIndexTamperedRemark(String meterIndexTamperedRemark)
		{
			JSONUtil.setJSONProperty(inspection, KEY_METER_INDEX_TAMPERED_REMARK, meterIndexTamperedRemark);
		}

		@Override
		public String getMeterIndexTamperedRemark()
		{
			return (String) JSONUtil.getJSONProperty(inspection, KEY_METER_INDEX_TAMPERED_REMARK);
		}

		@Override
		public void setMeterProtectionSeal(String meterProtectionSeal)
		{
			JSONUtil.setJSONProperty(inspection, KEY_METER_PROTECTION_SEAL, meterProtectionSeal);
		}

		@Override
		public String getMeterProtectionSeal()
		{
			return (String) JSONUtil.getJSONProperty(inspection, KEY_METER_PROTECTION_SEAL);
		}

		@Override
		public void setInputSealNo(String inputSealNo)
		{
			JSONUtil.setJSONProperty(inspection, KEY_INPUT_SEAL_NO, inputSealNo);
		}

		@Override
		public String getInputSealNo()
		{
			return (String) JSONUtil.getJSONProperty(inspection, KEY_INPUT_SEAL_NO);
		}

		@Override
		public void setMeterProtectionSealRemark(String meterProtectionSealRemark)
		{
			JSONUtil.setJSONProperty(inspection, KEY_METER_PROTECTION_SEAL_REMARK, meterProtectionSealRemark);
		}

		@Override
		public String getMeterProtectionSealRemark()
		{
			return (String) JSONUtil.getJSONProperty(inspection, KEY_METER_PROTECTION_SEAL_REMARK);
		}

		@Override
		public void setIsMeterByPass(String meterBypass)
		{
			JSONUtil.setJSONProperty(inspection, KEY_METER_BYPASS, meterBypass);
		}

		@Override
		public String isMeterByPass()
		{
			return JSONUtil.getJSONProperty(inspection, KEY_METER_BYPASS).toString();
		}

		@Override
		public void setMeterByPassRemark(String meterByPassRemark)
		{
			JSONUtil.setJSONProperty(inspection, KEY_METER_BYPASS_REMARK, meterByPassRemark);
		}

		@Override
		public String getMeterByPassRemark()
		{
			return (String) JSONUtil.getJSONProperty(inspection, KEY_METER_BYPASS_REMARK);
		}

		@Override
		public void setCurrentSealNo(String currentSealNo)
		{
			JSONUtil.setJSONProperty(inspection, KEY_CURRENT_SEAL_NO, currentSealNo);
		}

		@Override
		public String getCurrentSealNo()
		{
			return (String) JSONUtil.getJSONProperty(inspection, KEY_CURRENT_SEAL_NO);
		}
	}
}
