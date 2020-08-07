/**
******************************************************************************
* C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
* <p>
* Copyright ? 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved. 
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

import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.randommeterreading.IRandomMeterReading.IRandomMeterReadingInstance;

/**
* 
* <enter description here>
*
* @author nikita
* @see 
*
* @createdOn Mar 20, 2012
* @modifiedOn
*
* @copyright ? 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public interface IRandomMeterReadingFacade
{
	public IRandomMeterReadingInstance getRandomMeterReadingBO();

	public IRandomMeterReadingInstance resetrandomMeterReadingInstanceBO();

	public Response submitRandomMeterReading();
	
	public Response submitOneRandomMeterReading(String ramdomMeterReading);
	
	public Response submitOneRandomMeterReadingFromReceiver();

	public Response saveRandomMeterReading(boolean isReadingSubmittedToServer);

	//public Response saveRandomMeterReading();

	public Vector fetchSavedRandomMeterReading(String customerName);

	public Vector fetchAllSavedMeterReadings();

	public IRandomMeterReading getRandomMeterReading();

	public IRandomMeterReading setRandomMeterReadingInstance(IRandomMeterReadingInstance instance);

	public void initRandomMeterReadingCycleWithSavedRandomMeterReading(String savedRandomMeterReading);

	public Response getOfflineRandomMeterReadingDetail();
}
