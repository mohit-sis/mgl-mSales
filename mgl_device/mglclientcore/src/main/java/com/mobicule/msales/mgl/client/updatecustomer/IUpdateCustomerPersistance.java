/**
 * 
 */
package com.mobicule.msales.mgl.client.updatecustomer;

import java.util.Vector;

/**
 * @author nikita
 *
 */
public interface IUpdateCustomerPersistance
{
	public static final String KEY_UPDATECUSTOMER_SUBMIT = "customerUpdateSubmit";

	public void saveUpdateCustomer(String entity, String data);

	public Vector getOfflineUpdateCustomerDetail();

	public Vector fetchSavedUpdateCustomer(String entity, String mroNo);

	Vector fetchAllSavedUpdateCustomerMroNo(String entity);
	
	//Vector fetchAllSavedUpdateCustomerMroNo(String entity);
}
