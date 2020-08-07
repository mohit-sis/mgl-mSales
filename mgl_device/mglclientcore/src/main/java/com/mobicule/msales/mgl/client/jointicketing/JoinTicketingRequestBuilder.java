/**
******************************************************************************
* C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
* <p>
* Copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved. 
* This is proprietary information of Mobicule Technologies Pvt. Ltd.and is 
* subject to applicable licensing agreements. Unauthorized reproduction, 
* transmission or distribution of this file and its contents is a 
* violation of applicable laws.
******************************************************************************
*
* @project mgl-client-core
*/
package com.mobicule.msales.mgl.client.jointicketing;

import org.json.me.JSONObject;

import com.mobicule.msales.mgl.client.common.AuthenticatedRequestBuilder;

/**
* 
* <enter description here>
*
* @author Ashish Shukla>
* @see 
*
* @createdOn 15-Apr-2014
* @modifiedOn
*
* @copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class JoinTicketingRequestBuilder extends AuthenticatedRequestBuilder
{
	private static final String TYPE = "jointTicket";

	private static final String JOIN_TICKETING_ACTION = "submit";

	public JoinTicketingRequestBuilder(JSONObject userJson,String entity,JSONObject dataJson)
	{
		super(TYPE, entity, JOIN_TICKETING_ACTION, userJson);
		setRequestData(dataJson);
	}

	@Override
	public boolean shouldAddJsonArray()
	{
		return false;
	}
}
