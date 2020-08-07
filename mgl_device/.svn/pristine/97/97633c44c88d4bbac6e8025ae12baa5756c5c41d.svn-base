/**
******************************************************************************
* C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
* <p>
* Copyright © 2011-2012 Mobicule Technologies Pvt. Ltd. All rights reserved. 
* This is proprietary information of Mobicule Technologies Pvt. Ltd.and is 
* subject to applicable licensing agreements. Unauthorized reproduction, 
* transmission or distribution of this file and its contents is a 
* violation of applicable laws.
******************************************************************************
*
* @project mgl-client-core
*/
package com.mobicule.msales.mgl.client.randommeterreading;

import java.util.Vector;

/**
* 
* <enter description here>
*
* @author nikita <enter lastname>
* @see 
*
* @createdOn 07-Apr-2012
* @modifiedOn
*
* @copyright © 2010-2011 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public interface IRandomMeterReadingPersistance
{

	public static final String KEY_RMR_SUBMIT = "rmrSubmit";

	public void saveRandomMeterReading(String entity, String data);

	public Vector getOfflineRandomMeterReadingDetail();

	public Vector fetchSavedRandomMeterReading(String entity, String customerName);

	Vector fetchAllSavedRandomMeterReadings(String entity);
}
