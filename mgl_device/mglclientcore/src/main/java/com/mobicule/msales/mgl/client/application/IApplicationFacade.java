package com.mobicule.msales.mgl.client.application;

import com.mobicule.msales.mgl.client.common.Response;

public interface IApplicationFacade
{
	public Response getUserDtail();

	public Response getCustomerListBasedOnConnObj(String buildingName, String status);
	
	public Response getCustomerListBasedOnConnObj(String buildingName, String status, int noOfRecordFrom, int noOfRecordsTo);

	public Response getCustomerListBasedOnConnObjFromSavedMeterReading(String connObj, String status);

	public Response getCustomerListBasedOnBuildingName(String buildingName);
	
	public Response getSearchedCustomerDataFrmDB(String buildingName, String searchKey, String status);
	
	public Response searchCustomerListBasedOnBuildingName(String buildingName);

	public Response getCustomerListBasedOnMeterNo(String meterNo);

	public Response getCustomerListBasedOnBPNO(String bpNo);

	public Response getCustomerListBasedOnContactNo(String contactNo);

	public Response getCustomerListBasedOnCustomerFlatNo(String flatNo);

	public Response getCustomerListBasedOnCustomerName(String customerName);

	public Response getBuildingList(String status);

	public Response getBookWalkCount(String status);

	public Response getTotalBookWalkCount();

	public void updateBookWalkStatus(String data);

	public int calculateBookWalkCountBasedOnStatus();

	public int getUnattemptedCount();

	public int getCompletedCount();

	public int getIncompleteCount();

	public void setUnattemptedCount(int unattemptedCount);

	public void setCompletedCount(int completedCount);

	public void setIncompleteCount(int incompleteCount);

	public Response getBookWalkSyncData();

	public void clearAllData();

	public void dropTable(String entity);
	
	public int getSavedCount(String entity);
}
