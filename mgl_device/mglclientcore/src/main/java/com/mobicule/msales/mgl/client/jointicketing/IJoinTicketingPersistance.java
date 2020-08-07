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
public interface IJoinTicketingPersistance
{
	
	public static final String KEY_JT_SUBMIT = "jtSubmit"; 
	
	public boolean saveJoinTicketing(String entity, String data);
	
	public boolean searchSaveJoinTicketingStatusByMro(String entity, String data);
	
	public Vector<String> getSavedBuildingList();
	
	public Vector getSavedCustomerList(String connObj);
	
	public Vector<String> fetchSavedJoinTicketing(String entity, String mroNO);
	
	public Vector<String> fetchAllSavedJoinTicketings(String entity);
	
	public Vector<String> getTableLists();
	
	public List<String> fetchAllSavedJoinTicketingsOnlyMroNums(String entity);
	
	public List<String> getJoinTicketingDataByMro(String mroNo);
	
	public void addJoinTicketingImage(String imgJsonStr);
	
	public void deleteFromJoinTicketingImage(String imgJsonStr);
	
	public List<String> fetchAllJoinTicketingImages(String mro);

	

	
}
