/**
 * 
 */
package com.mobicule.msales.mgl.client.updatecustomer;

import org.json.me.JSONObject;

/**
 * @author nikita
 *
 */
public interface IUpdateCustomer
{

	static final String KEY_CURRENT_METER_NO = "currMeterNo";

	static final String KEY_NEW_METER_NO = "newMeterNo";

	static final String KEY_CURRENT_CONTACT_NO_MOBILE = "currContactNo";

	static final String KEY_NEW_CONTACT_NO_MOBILE = "newContactNo";
	
	static final String KEY_CURRENT_CONTACT_NO_HOME = "oldTelRes";

	static final String KEY_NEW_CONTACT_NO_HOME = "newTelRes";
	
	static final String KEY_CURRENT_CONTACT_NO_OFFICE = "oldTelOffice";

	static final String KEY_NEW_CONTACT_NO_OFFICE = "newTelOffice";

	static final String KEY_BP_NO = "bpNo";

	static final String KEY_CUSTOMER_NAME = "customerName";

	static final String KEY_NEW_CUSTOMER_NAME = "newCustomerName";

	static final String KEY_MRO_NO = "mroNo";

	static final String KEY_FLAT_NUMBER = "flat";

	static final String KEY_NEW_FLAT_NUMBER = "newFlat";

	static final String KEY_FLOOR_NUMBER = "floor";

	static final String KEY_NEW_FLOOR_NUMBER = "newFloor";

	static final String KEY_PLOTNO = "plot";

	static final String KEY_NEW_PLOTNO = "newPlot";

	static final String KEY_WING = "wing";

	static final String KEY_NEW_WING = "newWing";

	static final String KEY_BUILDING_NAME = "buildName";

	static final String KEY_NEW_BUILDING_NAME = "newBuildName";
	
	static final String KEY_REMARK = "remark";
	
	static final String KEY_DOC_TYPE = "documentType";
	
	static final String KEY_DOC_IMG_ONE = "documentImageOne";
	
	static final String KEY_DOC_IMG_TWO = "documentImageTwo";
	
	static final String KEY_DOC_IMG_THREE = "documentImageThree";

	public void init();

	public void init(JSONObject iupdateJsonObject);

	public void reset();

	public Object toJSON();

	public void setCurrentMeterNO(String currentMeterNo);

	public String getCurrentMeterNO();

	public void setNewMeterNO(String newMeterNo);

	public String getNewMeterNO();

	public void setNewCustomerName(String customerName);

	public String getNewCustomerName();

	public void setCurrContactNoMobile(String currentContactNoMobile);

	public String getCurrContactNoMobile();

	public void setNewContactNoMobile(String newContactNoMobile);

	public String getNewContactNoMobile();
	
	
	public void setCurrContactNoHome(String currentContactNoHome);

	public String getCurrContactNoHome();

	public void setNewContactNoHome(String newContactNoHome);

	public String getNewContactNoHome();
	
	
	public void setCurrContactNoOffice(String currentContactNoOffice);

	public String getCurrContactNoOffice();

	public void setNewContactNoOffice(String newContactNoOffice);

	public String getNewContactNoOffice();
	
	

	public void setBPNumber(String bpNo);

	public String getBPNumber();

	public void setFlatNumber(String flatNumber);

	public String getFlatNumber();

	public void setNewFlatNumber(String newFlatNumber);

	public String getNewFlatNumber();

	public void setFloor(String floor);

	public String getFloor();

	public void setNewFloor(String newFloor);

	public String getNewFloor();

	public void setBuildName(String buildName);

	public String getBuildName();

	public void setNewBuildName(String newBuildName);

	public String getNewBuildName();

	public void setPlot(String plot);

	public String getPlot();

	public void setNewPlot(String newPlot);

	public String getNewPlot();

	public void setWing(String wing);

	public String getWing();
	
	public void setNewWing(String newWing);

	public String getNewWing();

	public void setMrSubmitted(String isReadingSubmittedToServer);

	public String getMrSubmitted();

	public void setCustomerName(String customerName);

	public String getCustomerName();

	public void setMroNo(String mroNo);

	public String getMroNo();
	
	public void setRemark(String remark);

	public String getRemark();
	
	public void setDocType(String docType);
	
	public String getDocType();
	
	public void setDocImgOne(String imgOne);
	
	public String getDocImgOne();
	
	public void setDocImgTwo(String imgTwo);
	
	public String getDocImgTwo();
	
	public void setDocImgThree(String imgThree);
	
	public String getDocImgThree();
}
