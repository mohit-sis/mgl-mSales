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

import java.util.List;
import java.util.Vector;

import com.mobicule.msales.mgl.client.common.Response;

/**
* 
* <enter description here>
*
* @author Ashish  Shukla>
* @see 
*
* @createdOn 15-Apr-2014
* @modifiedOn
*
* @copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public interface IJoinTicketingFacade
{
	public boolean isCommercialCustomer(IJoinTicketing joinTicketingBO);
	
    public IJoinTicketing getCurrentJoinTicketingBO();
	
	public IJoinTicketing resetJoinTicketingBO();
	
	public Response getSavedBuildingList();
	
	public Response getSavedCustomerList(String connObj);
	
	public Response submitOneJoinTicketingFromRecevier();

	public Response saveJoinTicketing(boolean isReadingSubmittedToServer);
	
	public Vector<String> fetchSavedJoinTicketing(String mroNO);

	public Vector<String> fetchAllSavedJoinTicketings();
	
	public IJoinTicketing setJoinTicketingInstance(IJoinTicketing instance);

	public IJoinTicketing getJoinTicketing();

	public void initJoinTicketingCycleWithSavedJoinTicketing(String savedJoinTicketing);

	public List<String> fetchAllSavedJoinTicketingsOnlyMroNums();

	public List<String> getJoinTicketingDataByMro(String mroNo);
	
	public void saveJoinTicketingImages(String mroNo);
	
	public void addJoinTicketingImage(String imgJsonStr);
	
	public void deleteFromJoinTicketingImage(String imgJsonStr);
}
