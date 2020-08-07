package com.mobicule.msales.mgl.client.onmPlanning;

import java.util.ArrayList;
import java.util.Vector;

public interface IOnMPersistance
{
	public static final String KEY_UPDATECUSTOMER_SUBMIT = "customerUpdateSubmit";

	public void saveOnMReading(String entity, String data);

	public ArrayList<String> getOfflineOnMReadingDetail();

	public Vector fetchSavedOnMReading(String entity, String mroNo);

	Vector fetchAllSavedOnMReadingMroNo(String entity);
}
