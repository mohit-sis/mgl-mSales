/**
 *
 */
package com.mobicule.msales.mgl.client.meterreading;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.mobicule.msales.mgl.client.common.IJSONDataStore;

/**
 * @author nikita
 *
 */
public interface IMeterReading
{
	static final String COM_MRU_CODE = "COM_121";

	static final String KEY_MRU_CODE = "mruCode"; // mruCode 

	static final String KEY_METER_NO = "meterNo";

	static final String KEY_BP_NO = "bpNo";

	static final String KEY_LAST_METER_READING_DATE = "prevReadDate";

	static final String KEY_MRO_NO = "mroNo";

	static final String KEY_IMAGES = "image";

	static final String KEY_IMAGES2 = "image2";

	static final String KEY_IMAGES3 = "image3";

	static final String KEY_INSPECTION = "inspection"; // inspection

	static final String KEY_READINGS = "readings";

	static final String KEY_MR_REASON = "mrReason";

	static final String KEY_MR_REASON_CODE = "mrCode";

	static final String KEY_NO_OF_ATTEMPTS = "noOfAttempts";

	static final String KEY_CUSTOMER_NAME = "customerName";

	static final String KEY_CUSTOMER_ADDRESS = "address";

	static final String KEY_CUSTOMER_CONTACT = "contactNo";//"customerContactNo";

	static final String KEY_CONN_OBJ = "connObj";

	static final String KEY_STATUS = "status";

	static final String KEY_CUSTOMER_LANDLINE_NO = "resNo";

	static final String KEY_CUSTOMER_OFFICE_NO = "offcNo";

	static final String KEY_CUSTOMER_PREVIOUS_READING = "prevRead";

	static final String KEY_AREA = "area";

	static final String KEY_FLAT = "flat";

	static final String KEY_PLOT = "plot";

	static final String KEY_WING = "wing";

	static final String KEY_FLOOR = "floor";

	static final String KEY_MSG_TO_MR = "msgToMr";

	static final String KEY_MSG_FROM_MR = "msgFromMr";

	static final String KEY_MSG_FROM_MR_PRIMARY = "msgFromMrPrimary";

	static final String KEY_MSG_REMARKS = "msgRemarks";

	static final String KEY_IS_RELOCATION = "relocation";

	static final String KEY_IS_LPGSUPPLIER = "lpgSupplier";

	static final String KEY_SERIAL_CORRECT = "serialNoCorrect";

	static final String KEY_SERIAL_VISIBLE = "serialNoVisible";

	static final String KEY_PREMISE_CORRECT = "premiseCorrect";

	static final String KEY_REENTER_METER_READING = "reenterMeterReading";

	static final String KEY_TYPE = "keyType";

	public void init();

	public void reset();

	public void setMeterReadingInstance(IMeterReadingInstance instance);

	public IMeterReadingInstance getMeterReadingInstance(String mroNo);

	public JSONArray toJsonArray();

	public interface IMeterReadingInstance extends IJSONDataStore
	{
		public void init(JSONObject meterReadingData);

		public void reset();

		public void setMruCode(String mruCode); // mruCode setter

		public String getMruCode(); // mruCode getter

		public void setMeterNumber(String meterNumber);

		public String getMeterNumber();

		public void setCustomerAddress(String customerAddress);

		public String getCustomerAddress();

		public void setFlat(String flat);

		public String getFlat();

		public void setFloor(String floor);

		public String getFloor();

		public void setPlot(String plot);

		public String getPlot();

		public void setWing(String wing);

		public String getWing();

		public void setMsgToMr(String msgToMR);

		public String getMsgToMr();

		public void setMsgFromMr(String msgFromMr);

		public String getMsgFromMr();

		public void setCustomerContactNo(String customerContactNo);

		public String getCustomerContactNo();

		public void setCustLandNo(String CustLandNo);

		public String getCustLandNo();

		public void setCustOfficeNo(String CustOfficeNo);

		public String getCustOfficeNo();

		public void setCustomerName(String customerName);

		public void setConnObj(String connObj);

		public String getConnObj();

		public String getCustomerName();

		public void setBpNumber(String bpNumber);

		public String getBpNumber();

		public void setMroNumber(String mroNumber);

		public String getMroNumber();

		public void setImage(String image);

		public String getImage();

		public void setImage2(String image);

		public String getImage2();

		public void setImage3(String image);

		public String getImage3();

		public void setReadings(IReadings readings);

		public String getReading();

		public JSONArray getReadingList();

		public void setMrReason(String mrReason);

		public void setMsgRemarks(String msgRemarks);

		public String getMsgRemarks();

		public void setMsgRemarksPrimary(String msgRemarksPrimary);

		public String getMsgRemarksPrimary();

		public String getMrReason();

		public void setLastMeterReadingDate(String lastMeterReadingDate);

		public String getLastMeterReadingDate();

		public void setNoOfAttempts(String noOfAttempts);

		public String getNoOfAttempts();

		public void resetReadingList();

		public void setStatus(String status);

		public String getStatus();

		public void setLock(String lock);

		public void setCaNo(String caNo);

		public String getCaNo();

		public void setArea(String area);

		public void setMrSubmitted(String isReadingSubmittedToServer);

		public String getMrSubmitted();

		public void setprevRead(String prevRead);

		public String getprevRead();

		public void setIsSerailCorrect(String isSerailCorrect);

		public String getIsSerailCorrect();

		public void setIsSerailVisible(String isSerialVisible);

		public String getIsSerailVisible();

		public void setIsPremiselCorrect(String isPremiseCorrect);

		public String getIsPremiselCorrect();

		public void setIsRelocation(String isRelocation);

		public String getIsRelocation();

		public void setIsLPGSupplier(String isLPGSupplier);

		public String getLPGSupplier();



		public void setIsReEnterMeterReading(String isReEnterMeterReading);

		public String getReEnterMeterReading();

		public void setInspection(IInspection inspection); //inspection setter

		public JSONObject getInspection(); //inspection getter

		public void removeInspection();
	}

	public interface IReadings extends IJSONDataStore
	{
		public void setMeterReading(String meterReading);

		public void setSerialNo(String srNo);

		public String getSerialNo();

		public String getMeterReading();

		public void setReadingTime(String readingTime);

		public String getReadingTime();

		public void setDate(String date);

		public String getDate();

		public void setMrCode(String mrCode);

		public String getMrCode();
	}

	public interface IInspection extends IJSONDataStore
	{
		public void setIsMeterIndexTampered(String meterIndexTampered);

		public String isMeterIndexTampered();

		public void setMeterIndexTamperedRemark(String meterIndexTamperedRemark);

		public String getMeterIndexTamperedRemark();

		public void setMeterProtectionSeal(String meterProtectionSeal);

		public String getMeterProtectionSeal();

		public void setInputSealNo(String inputSealNo);

		public String getInputSealNo();

		public void setMeterProtectionSealRemark(String meterProtectionSealRemark);

		public String getMeterProtectionSealRemark();

		public void setIsMeterByPass(String meterBypass);

		public String isMeterByPass();

		public void setMeterByPassRemark(String meterByPassRemark);

		public String getMeterByPassRemark();

		public void setCurrentSealNo(String currentSealNo);

		public String getCurrentSealNo();
	}
}