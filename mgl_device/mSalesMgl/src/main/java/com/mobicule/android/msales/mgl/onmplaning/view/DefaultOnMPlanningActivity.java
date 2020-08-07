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
* @project mSalesMgl
*/
package com.mobicule.android.msales.mgl.onmplaning.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.mobicule.android.msales.mgl.commons.model.IOCContainer;
import com.mobicule.android.msales.mgl.menu.view.MainMenu;
import com.mobicule.android.msales.mgl.meterreading.model.DefaultMeterReadingPersistenceService;
import com.mobicule.android.msales.mgl.onmplaning.model.DefaultOnMPlanningPersistanceService;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.component.json.parser.IJSONParser;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.crashnotifier.IGenericExceptionHandler;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IMeterReadingInstance;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingFacade;
import com.mobicule.msales.mgl.client.onmPlanning.IOnMFacade;
import com.mobicule.msales.mgl.client.onmPlanning.IOnMPlanning;

import java.io.File;

/**
* 
* <enter description here>
*
* @author namrata <enter lastname>
* @see 
*
* @createdOn 24-Apr-2017
* @modifiedOn
*
* @copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class DefaultOnMPlanningActivity extends Activity
{

	public IApplicationFacade applicationFacade;

	public IJSONParser jsonParser;

	protected static Response response;

	protected IOnMFacade onmFacade;

	protected IOnMPlanning onMPlanningBO;

	protected DefaultOnMPlanningPersistanceService onmPlanningPersistance;
	
	protected IMeterReadingFacade meterReadingFacade;

	protected IMeterReadingInstance meterReadingBO;

	protected DefaultMeterReadingPersistenceService meterReadingPersistance;
	
	IGenericExceptionHandler handler;

	public DefaultOnMPlanningActivity()
	{
		defaultInitialization();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//For CrashFile
		if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {


			File path = Environment.getExternalStorageDirectory();

			File syncDir = new File(path + "/MGL_LOGS");
			File logDirectory = new File(syncDir + "/log");

			if (!syncDir.exists()) {
				syncDir.mkdir();
			}
			if (!logDirectory.exists()) {
				logDirectory.mkdir();
			}

			Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(

					logDirectory.getPath(), "https://crash.ezycom.co.in/upload.php"));

		}

		this.logOutReceiver();
	}

	private void logOutReceiver()
	{
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.mobicule.ACTION_LOGOUT");
		intentFilter.addAction("com.mobicule.ACTION_MY_BOOK_WALK");
		registerReceiver(broadcastReceiver, intentFilter);
	}

	protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			DefaultOnMPlanningActivity.this.finish();
		}
	};

	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}*/
	
	@Override
	public void onBackPressed() 
	{		
	}

	public void goHome(View v)
	{
		startActivity(new Intent(this, MainMenu.class));
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	private void defaultInitialization()
	{
		try
		{

			meterReadingFacade = (IMeterReadingFacade) IOCContainer.getInstance(DefaultOnMPlanningActivity.this)
					.getBean("DefaultMeterReadingFacade");
			meterReadingBO = meterReadingFacade.getCurrentMeterReadingBO();
			applicationFacade = (IApplicationFacade) IOCContainer.getInstance(this).getBean("ApplicationFacade");
			meterReadingPersistance = (DefaultMeterReadingPersistenceService) IOCContainer.getInstance(this).getBean(
					"DefaultMeterReadingPersistanceService");
			onmFacade = (IOnMFacade) IOCContainer.getInstance(DefaultOnMPlanningActivity.this)
					.getBean("DefaultOnMPlanningFacade");
			onMPlanningBO = onmFacade.getOnMPlanningBO();
			applicationFacade = (IApplicationFacade) IOCContainer.getInstance(this).getBean("ApplicationFacade");
			onmPlanningPersistance = (DefaultOnMPlanningPersistanceService) IOCContainer.getInstance(this)
					.getBean("DefaultOnMPlanningPersistanceService");
			jsonParser = JSONParser.getInstance();
			
		//	handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
		}
		catch (Exception e)
		{
			//IGenericExceptionHandler handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
		
			//handler.logCrashReport(e); 
			e.printStackTrace();
		}
	}

}
