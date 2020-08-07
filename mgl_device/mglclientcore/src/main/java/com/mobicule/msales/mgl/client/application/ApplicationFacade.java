package com.mobicule.msales.mgl.client.application;

import java.util.Vector;

import com.mobicule.component.config.ApplicationConfiguration;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.component.util.CoreMobiculeLogger;
import com.mobicule.msales.mgl.client.common.Response;

public class ApplicationFacade implements IApplicationFacade
{
	private static IApplicationFacade instance;

	private final IApplicationPersistence applicationPersistance;

	private ApplicationConfiguration applicationConfiguration;

	private ApplicationFacade(IApplicationPersistence applicationPersistance)
	{
		this.applicationPersistance = applicationPersistance;
	}

	public static IApplicationFacade getInstance(IApplicationPersistence applicationPersistance)
	{
		if (instance == null)
		{
			instance = new ApplicationFacade(applicationPersistance);
		}
		return instance;
	}

	public synchronized Response getUserDtail()
	{
		return new Response(true, CoreConstants.EMPTY_STRING, applicationPersistance.getUserDetail());
	}

	public ApplicationConfiguration getXMLApplicationCongigurationObject()
	{
		return applicationConfiguration;
	}

	public void setXMLApplicationCongigurationObject(ApplicationConfiguration applicationConfiguration)
	{
		this.applicationConfiguration = applicationConfiguration;
	}

	public Vector load(String entity, String key, String value)
	{
		return applicationPersistance.load(entity, key, value);
	}

	public Response getCustomerListBasedOnMeterNo(String meterNo)
	{
		Vector data = applicationPersistance.getCustomerListBasedOnMeterNo(meterNo);
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}

	public Response getCustomerListBasedOnConnObj(String connObj, String status)
	{
		Vector data = applicationPersistance.getCustomerListBasedOnConnObj(connObj, status);
		
		CoreMobiculeLogger.log("Status : " + status + "Vector data" + data.toString());
		
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}

	public Response getCustomerListBasedOnConnObjFromSavedMeterReading(String connObj, String status)
	{
		Vector data = applicationPersistance.getCustomerListBasedOnConnObjFromSavedMeterReading(connObj, status);
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}

	public Response getCustomerListBasedOnBuildingName(String buildingName)
	{
		Vector data = applicationPersistance.getCustomerListBasedOnBuildingName(buildingName);
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}

	public Response searchCustomerListBasedOnBuildingName(String buildingName)
	{
		Vector data = applicationPersistance.searchCustomerListBasedOnBuildingName(buildingName);
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}

	
	public Response getBuildingList(String status)
	{
		Vector data = applicationPersistance.getBuildingList(status);
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}

	public Response getCustomerListBasedOnBPNO(String bpNo)
	{
		Vector data = applicationPersistance.getCustomerListBasedOnBPNO(bpNo);
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}

	public Response getCustomerListBasedOnCustomerFlatNo(String flatNo)
	{
		Vector data = applicationPersistance.getCustomerListBasedOnCustomerFlatNo(flatNo);
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);

	}

	public Response getCustomerListBasedOnContactNo(String contactNo)
	{
		Vector data = applicationPersistance.getCustomerListBasedOnContactNo(contactNo);
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}

	public Response getCustomerListBasedOnCustomerName(String customerName)
	{
		Vector data = applicationPersistance.getCustomerListBasedOnCustomerName(customerName);
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}

	public Response getBookWalkCount(String status)
	{
		int data = applicationPersistance.getBookWalkCount(status);
		return new Response(true, "", String.valueOf(data));
	}

	public Response getTotalBookWalkCount()
	{
		int data = applicationPersistance.getTotalBookWalkCount();
		return new Response(true, "", String.valueOf(data));
	}

	public void updateBookWalkStatus(String data)
	{
		applicationPersistance.updateBookWalkStatus(data);
	}

	public int calculateBookWalkCountBasedOnStatus()
	{
		int count = applicationPersistance.calculateBookWalkCountBasedOnStatus();
		return count;
	}

	public int getUnattemptedCount()
	{
		int count = applicationPersistance.getUnattemptedCount();
		return count;
	}

	public int getCompletedCount()
	{
		int count = applicationPersistance.getCompletedCount();
		return count;
	}

	public int getIncompleteCount()
	{
		int count = applicationPersistance.getIncompleteCount();
		return count;
	}

	public void setUnattemptedCount(int unattemptedCount)
	{
		applicationPersistance.setUnattemptedCount(unattemptedCount);
	}

	public void setCompletedCount(int completedCount)
	{
		applicationPersistance.setCompletedCount(completedCount);
	}

	public void setIncompleteCount(int incompleteCount)
	{
		applicationPersistance.setIncompleteCount(incompleteCount);
	}

	public Response getBookWalkSyncData()
	{
		Vector data = applicationPersistance.getBookWalkSyncData(CoreConstants.TABLE_SYNC);
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}

	public void clearAllData()
	{
		applicationPersistance.clearAllData();
	}

	public void dropTable(String entity)
	{
		applicationPersistance.dropTable(entity);
	}

	@Override
	public int getSavedCount(String entity)
	{
		return applicationPersistance.getSavedCount(entity);
	}

	@Override
	public Response getCustomerListBasedOnConnObj(String buildingName, String status, int noOfRecordFrom,
			int noOfRecordsTo)
	{
		Vector data = applicationPersistance.getCustomerListBasedOnConnObj(buildingName, status, noOfRecordFrom, noOfRecordsTo);
		
		CoreMobiculeLogger.log("Status : " + status + "Vector data" + data.toString());
		
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}

	@Override
	public Response getSearchedCustomerDataFrmDB(String buildingName, String searchKey, String status)
	{
		
		Vector data = applicationPersistance.getSearchCustomerDataFrmDB(buildingName, searchKey, status);
		
		CoreMobiculeLogger.log("Status : " + status + "Vector data" + data.toString());
		
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}
}
