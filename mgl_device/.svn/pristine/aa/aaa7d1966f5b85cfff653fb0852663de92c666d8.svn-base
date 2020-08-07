package com.mobicule.msales.mgl.client.onmPlanning;

import java.util.Vector;

import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomer;

public interface IOnMFacade
{
	public IOnMPlanning getOnMPlanningBO();

	public Response OnMCustomerDetails();
	
	public Response OnMOneCustomerDetails(String customerDetails);

	public IUpdateCustomer getOnMPlanning();

	public Response saveOnMPlanning(boolean isReadingSubmittedToServer);

	public Vector fetchSavedOnM(String mroNo);

	public Vector fetchAllSavedOnMMroNo();

	public void initCustomerCycleWithSavedOnMPlanning(String savedCustomerUpdate);

	public Response getOfflineOnMPlanningDetail();
}
