/**
 * 
 */
package com.mobicule.msales.mgl.client.meterreading;

import org.json.me.JSONObject;

import com.mobicule.msales.mgl.client.common.Response;

/**
 * @author nikita
 *
 */
public interface IMeterReadingCommunication
{
	public Response submit(JSONObject jsonObject);

	public Response submitString(String postdata);
}
