/**
 * 
 */
package com.mobicule.msales.mgl.client.updatecustomer;

import java.util.Vector;

import com.mobicule.msales.mgl.client.common.Response;

/**
 * @author nikita
 *
 */
public interface IUpdateCustomerFacade
{

	public IUpdateCustomer getCustomerUpdateBO();

	public Response updateCustomerDetails();
	
	public Response updateOneCustomerDetails(String customerDetails);

	public IUpdateCustomer getCustomerUpdate();

	public Response saveCustomerUpdate(boolean isReadingSubmittedToServer);

	public Vector fetchSavedCustomerUpdate(String mroNo);

	public Vector fetchAllSavedCustomerUpdateMroNo();

	public void initCustomerUpdateCycleWithSavedCustomerUpdate(String savedCustomerUpdate);

	public Response getOfflineUpdateCustomerDetail();

}
