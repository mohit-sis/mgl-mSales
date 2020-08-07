/**
 * 
 */
package com.mobicule.msales.mgl.client.meterreading;

import java.util.Vector;

/**
 * @author nikita
 *
 */
public interface IMeterReadingPersistance
{
	public static final String KEY_MR_SUBMIT = "mrSubmit";

	public boolean saveMeterReading(String entity, String data);

	public boolean deleteMeterReading(String entity, String data);

	public Vector fetchSavedMeterReading(String entity, String mroNo);

	public Vector getSavedBuildingList();

	public Vector getSavedCustomerList(String connObj);

	Vector fetchAllSavedMeterReadings(String entity);
	
	Vector fetchAllSavedMeterReadingsOnlyMroNums(String entity);

	boolean searchSaveMeterReadingStatusByMro(String entity, String data);
}
