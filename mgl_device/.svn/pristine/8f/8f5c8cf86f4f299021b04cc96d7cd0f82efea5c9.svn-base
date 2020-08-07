/**
 * 
 */
package com.mobicule.msales.mgl.client.meterreading;

import java.util.Vector;

import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IMeterReadingInstance;

/**
 * @author nikita
 *
 */
public interface IMeterReadingFacade
{
	public IMeterReadingInstance getCurrentMeterReadingBO();
	
	public IMeterReadingInstance resetMeterReadingInstanceBO();

	public Response submitMeterReading();
	
	public Response submitOneMeterReading(String meterReading);
	
	public Response submitOneMeterReadingFromRecevier();

	public Response saveMeterReading(boolean isReadingSubmittedToServer);
	
	public Response saveMeterReading(String meterReading, boolean isReadingSubmittedToServer);

	public Vector fetchSavedMeterReading(String mroNO);

	public Vector fetchAllSavedMeterReadings();
	
	public Vector fetchAllSavedMeterReadingsOnlyMroNums();

	public IMeterReading setMeterReadingInstance(IMeterReadingInstance instance);

	public IMeterReading getMeterReading();

	public void initMeterReadingCycleWithSavedMeterReading(String savedMeterReading);

	public Response getSavedBuildingList();

	public Response getSavedCustomerList(String connObj);

	//	public void updateMeterReadingOnAutoTrigger();
	
	public boolean isCommercialCustomer(IMeterReadingInstance meterReadingBO);
}
