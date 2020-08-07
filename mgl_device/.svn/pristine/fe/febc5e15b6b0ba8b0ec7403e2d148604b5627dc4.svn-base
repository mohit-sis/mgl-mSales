package com.mobicule.msales.mgl.client.application;

import java.util.Vector;

public interface IApplicationPersistence
{
	public abstract String getUserDetail();

	public Vector load(String entity, String key, String value);

	public Vector load(String entity);

	public Vector getCustomerListBasedOnMeterNo(String meterNo);

	public Vector getCustomerListBasedOnConnObj(String buildingName, String status);
	
	public Vector getCustomerListBasedOnConnObj(String buildingName, String status, int noOfRecordsFrom, int noOfRecordsTo);

	public Vector getCustomerListBasedOnConnObjFromSavedMeterReading(String connObj, String status);

	public Vector getCustomerListBasedOnBPNO(String bpNo);

	public Vector getCustomerListBasedOnCustomerFlatNo(String flatNo);

	public Vector getCustomerListBasedOnContactNo(String contactNo);

	public Vector getCustomerListBasedOnCustomerName(String customerName);
	
	public Vector getSearchCustomerDataFrmDB(String buildingName, String searchKey, String status);

	public Vector getBuildingList(String status);

	public int getBookWalkCount(String status);

	public int getTotalBookWalkCount();

	public Vector getCustomerListBasedOnBuildingName(String buildingName);
	
	public Vector searchCustomerListBasedOnBuildingName(String buildingName);

	public void updateBookWalkStatus(String data);

	public int calculateBookWalkCountBasedOnStatus();

	public int getUnattemptedCount();

	public int getCompletedCount();

	public int getIncompleteCount();

	public void setUnattemptedCount(int unattemptedCount);

	public void setCompletedCount(int completedCount);

	public void setIncompleteCount(int incompleteCount);

	public Vector getBookWalkSyncData(String entity);

	public void clearAllData();

	public void dropTable(String entity);
	
	public int getSavedCount(String entity);
}