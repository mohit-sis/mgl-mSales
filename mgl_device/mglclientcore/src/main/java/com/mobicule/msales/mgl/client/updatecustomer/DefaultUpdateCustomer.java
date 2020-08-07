/**
 * 
 */
package com.mobicule.msales.mgl.client.updatecustomer;

import org.json.me.JSONObject;

import com.mobicule.component.json.JSONUtil;

/**
 * @author nikita
 *
 */
public class DefaultUpdateCustomer implements IUpdateCustomer
{

	private JSONObject updateCustomerJson;

	private static final String KEY_UPDATECUSTOMER_SUBMIT = "customerUpdateSubmit";

	public DefaultUpdateCustomer()
	{
		init();
	}

	public void init()
	{
		updateCustomerJson = new JSONObject();
	}

	public void reset()
	{
		init();
	}

	public Object toJSON()
	{
		return updateCustomerJson;
	}

	public String getBPNumber()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_BP_NO);
	}

	public String getCurrContactNoMobile()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_CURRENT_CONTACT_NO_MOBILE);
	}

	public String getCurrentMeterNO()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_CURRENT_METER_NO);
	}

	public String getNewContactNoMobile()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_NEW_CONTACT_NO_MOBILE);
	}
	
	
	public String getCurrContactNoHome()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_CURRENT_CONTACT_NO_HOME);
	}
	
	public String getNewContactNoHome()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_NEW_CONTACT_NO_HOME);
	}
	
	public String getCurrContactNoOffice()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_CURRENT_CONTACT_NO_OFFICE);
	}
	
	public String getNewContactNoOffice()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_NEW_CONTACT_NO_OFFICE);
	}

	public String getNewMeterNO()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_NEW_METER_NO);
	}

	public void setBPNumber(String bpNo)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_BP_NO, bpNo);
	}

	public void setCurrContactNoMobile(String currentContactNo)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_CURRENT_CONTACT_NO_MOBILE, currentContactNo);
	}

	public void setCurrentMeterNO(String currentMeterNo)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_CURRENT_METER_NO, currentMeterNo);
	}

	public void setNewContactNoMobile(String newContactNo)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_NEW_CONTACT_NO_MOBILE, newContactNo);
	}
	
	public void setCurrContactNoHome(String currentContactNoHome)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_CURRENT_CONTACT_NO_HOME, currentContactNoHome);
	}
	
	public void setNewContactNoHome(String newContactNoHome)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_NEW_CONTACT_NO_HOME, newContactNoHome);
	}
	
	
	public void setCurrContactNoOffice(String currentContactNoOffice)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_CURRENT_CONTACT_NO_OFFICE, currentContactNoOffice);
	}
	
	public void setNewContactNoOffice(String newContactNoOffice)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_NEW_CONTACT_NO_OFFICE, newContactNoOffice);
	}
	

	public void setNewMeterNO(String newMeterNo)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_NEW_METER_NO, newMeterNo);
	}

	public void setMrSubmitted(String isReadingSubmittedToServer)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_UPDATECUSTOMER_SUBMIT, isReadingSubmittedToServer);
	}

	public String getMrSubmitted()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_UPDATECUSTOMER_SUBMIT);
	}

	public void setCustomerName(String customerName)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_CUSTOMER_NAME, customerName);
	}

	public String getCustomerName()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_CUSTOMER_NAME);
	}

	public void setMroNo(String mroNo)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_MRO_NO, mroNo);
	}

	public String getMroNo()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_MRO_NO);
	}

	public void init(JSONObject iupdateJsonObject)
	{
		updateCustomerJson = iupdateJsonObject;
	}

	@Override
	public void setFlatNumber(String flatNumber)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_FLAT_NUMBER, flatNumber);

	}

	@Override
	public String getFlatNumber()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_FLAT_NUMBER);
	}

	@Override
	public void setFloor(String floor)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_FLOOR_NUMBER, floor);

	}

	@Override
	public String getFloor()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_FLOOR_NUMBER);
	}

	@Override
	public void setBuildName(String buildName)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_BUILDING_NAME, buildName);
	}

	@Override
	public String getBuildName()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_BUILDING_NAME);
	}

	@Override
	public void setPlot(String plot)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_PLOTNO, plot);

	}

	@Override
	public String getPlot()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_PLOTNO);
	}

	@Override
	public void setWing(String wing)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_WING, wing);
	}

	@Override
	public String getWing()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_WING);
	}

	@Override
	public void setNewCustomerName(String customerName)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_NEW_CUSTOMER_NAME, customerName);
		
		
	}

	@Override
	public String getNewCustomerName()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_NEW_CUSTOMER_NAME);
		
	}

	@Override
	public void setNewFlatNumber(String newFlatNumber)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_NEW_FLAT_NUMBER, newFlatNumber);
		
	}

	@Override
	public String getNewFlatNumber()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_NEW_FLAT_NUMBER);
	}

	@Override
	public void setNewFloor(String newFloor)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_NEW_FLOOR_NUMBER, newFloor);
		
	}

	@Override
	public String getNewFloor()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_NEW_FLOOR_NUMBER);
	}

	@Override
	public void setNewBuildName(String newBuildName)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_NEW_BUILDING_NAME, newBuildName);
		
	}

	@Override
	public String getNewBuildName()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_NEW_BUILDING_NAME);
	}

	@Override
	public void setNewPlot(String newPlot)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_NEW_PLOTNO, newPlot);
		
	}

	@Override
	public String getNewPlot()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_NEW_PLOTNO);
	}

	@Override
	public void setNewWing(String newWing)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_NEW_WING, newWing);
		
	}

	@Override
	public String getNewWing()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_NEW_WING);
	}
	
	@Override
	public void setRemark(String remark)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_REMARK, remark);
		
	}

	@Override
	public String getRemark()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_REMARK);
	}

	@Override
	public void setDocType(String docType)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_DOC_TYPE, docType);
	}

	@Override
	public String getDocType()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_DOC_TYPE);
	}

	@Override
	public void setDocImgOne(String imgOne)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_DOC_IMG_ONE, imgOne);
	}

	@Override
	public String getDocImgOne()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_DOC_IMG_ONE);
	}

	@Override
	public void setDocImgTwo(String imgTwo)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_DOC_IMG_TWO, imgTwo);
	}

	@Override
	public String getDocImgTwo()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_DOC_IMG_TWO);
	}

	@Override
	public void setDocImgThree(String imgThree)
	{
		JSONUtil.setJSONProperty(updateCustomerJson, KEY_DOC_IMG_THREE, imgThree);
	}

	@Override
	public String getDocImgThree()
	{
		return (String) JSONUtil.getJSONProperty(updateCustomerJson, KEY_DOC_IMG_THREE);
	}

}
