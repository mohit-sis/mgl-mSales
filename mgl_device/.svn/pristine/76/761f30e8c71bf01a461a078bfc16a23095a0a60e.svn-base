/**
 * 
 */
package com.mobicule.msales.mgl.client.updatecustomer;

import org.json.me.JSONObject;

/**
 * @author nikita
 *
 */
public class UpdateCustomerRequestBuilder extends UpdateCustomer
{

	private static final String ENTITY = "customer";

	public UpdateCustomerRequestBuilder(JSONObject jsonObject, JSONObject updateCustomer)
	{
		super(ENTITY, jsonObject);

		//setRequestData((JSONObject) updateCustomer.toJSON());
		setRequestData(updateCustomer);
	}

	public boolean shouldAddJsonArray()
	{
		return false;
	}

}
