package com.mobicule.msales.mgl.client.onmPlanning;

import org.json.me.JSONObject;

public interface IOnMPlanning
{
	static final String KEY_NO_OF_BURNERS = "noOfBurner";

	static final String KEY_NO_OF_GAS_GEYSERS = "noOfGasGeyser";

	static final String KEY_METER_NO = "meterNo";

	static final String KEY_MSG_FRM_MR = "msgFromMr";
	
	static final String KEY_MSG_REMARKS = "msgRemarks";

	static final String KEY_MR_SUBMIT = "mrSubmit";
	
	static final String KEY_PLOT = "plot";

	static final String KEY_TIME = "time";

	static final String KEY_MR_CODE = "mrCode";

	static final String KEY_DATE = "date";

	static final String KEY_CONN_OBJ = "connObj";

	static final String KEY_MRO_NO = "mroNo";

	static final String KEY_CUSTOMER_NAME = "customerName";

	static final String KEY_STATUS = "status";

	static final String KEY_MSG_TO_MR = "msgToMr";

	static final String KEY_IMAGE = "image";

	static final String KEY_MR_REASON = "mrReason";

	static final String KEY_MRU_CODE = "mruCode";

	static final String KEY_IMAGE2 = "image2";

	static final String KEY_ADDRESS = "address";

	static final String KEY_CA_NO = "caNo";

	static final String KEY_BP_NO = "bpNo";
	
	static final String KEY_IMAGE3 = "image3";
	
	static final String KEY_IS_HOSE_AVAILABLE = "isHoseAvailable";
	
	public void init();

	public void init(JSONObject iOnMJsonObject);

	public void reset();

	public Object toJSON();

	public void setNoOfBurners(String noOfBurners);
	
	public String getNoOfBurners();
	
	public void setNoOfGasGeysers(String noOfGasGeysers);
	
	public String getNoOfGasGeysers();
	
	public void setMeterNo(String meterNo);
	
	public String getMeterNo();

	public void setMsgFrmMr(String msgFrmMr);
	
	public String getMsgFrmMr();

	public void setMsgRemarks(String msgRemarks);
	
	public String getMsgRemarks();

	public void setMrSubmit(String mrSubmit);
	
	public String getMrSubmit();

	public void setPlot(String plot);
	
	public String getPlot();

	public void setTime(String time);
	
	public String getTime();

	public void setMrCode(String mrCode);
	
	public String getMrCode();

	public void setDate(String date);
	
	public String getDate();

	public void setConnObj(String connObj);
	
	public String getConnObj();

	public void setMroNo(String mroNo);
	
	public String getMroNo();

	public void setCutsomerName(String customerName);
	
	public String getCutsomerName();

	public void setStatus(String status);
	
	public String getStatus();

	public void setMsgToMr(String msgToMr);
	
	public String getMsgToMr();

	public void setImage(String image);
	
	public String getImage();

	public void setMrReason(String mrReason);
	
	public String getMrReason();

	public void setImage2(String image2);
	
	public String getImage2();

	public void setAddress(String address);
	
	public String getAddress();

	public void setCANumber(String caNumber);
	
	public String getCANumber();

	public void setBPNumber(String bpNumber);
	
	public String getBPNumber();

	public void setImage3(String image3);
	
	public String getImage3();
	
	public void setMruCode(String mruCode);
	
	public String getMruCode();
	
	public void setIsHoseAvailable(String isHoseAvailable);
	
	public String getIsHoseAvailable();

}
