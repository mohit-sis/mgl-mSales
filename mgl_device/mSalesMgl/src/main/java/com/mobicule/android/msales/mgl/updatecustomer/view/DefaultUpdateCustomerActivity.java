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
* @project mSalesMgl
*/
package com.mobicule.android.msales.mgl.updatecustomer.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;

import com.mobicule.android.msales.mgl.commons.model.IOCContainer;
import com.mobicule.android.msales.mgl.menu.view.MainMenu;
import com.mobicule.android.msales.mgl.updatecustomer.model.DefaultUpdateCustomerPersistanceService;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.component.json.parser.IJSONParser;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.crashnotifier.GenericExceptionHandler;
import com.mobicule.crashnotifier.IGenericExceptionHandler;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingFacade;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IMeterReadingInstance;
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomer;
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomerFacade;

import java.io.File;

/**
* 
* <enter description here>
*
* @author nikita
* @see 
*
* @createdOn Mar 24, 2012
* @modifiedOn
*
* @copyright ? 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class DefaultUpdateCustomerActivity extends Activity
{
	public IApplicationFacade applicationFacade;

	public IJSONParser jsonParser;

	protected static Response response;

	protected IUpdateCustomerFacade updateCustomerFacade;

	protected IUpdateCustomer updateCustomerBO;
	
	protected IMeterReadingFacade meterReadingFacade;
	
	protected IMeterReadingInstance meterReadingBO;

	protected DefaultUpdateCustomerPersistanceService customerUpdatePersistance;
	
	IGenericExceptionHandler handler;

	public DefaultUpdateCustomerActivity()
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
			DefaultUpdateCustomerActivity.this.finish();
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

			updateCustomerFacade = (IUpdateCustomerFacade) IOCContainer.getInstance(DefaultUpdateCustomerActivity.this)
					.getBean("DefaultUpdateCustomerFacade");
			updateCustomerBO = updateCustomerFacade.getCustomerUpdateBO();
			meterReadingFacade = (IMeterReadingFacade) IOCContainer.getInstance(DefaultUpdateCustomerActivity.this).getBean("DefaultMeterReadingFacade");
			meterReadingBO = meterReadingFacade.getCurrentMeterReadingBO();
			applicationFacade = (IApplicationFacade) IOCContainer.getInstance(this).getBean("ApplicationFacade");
			customerUpdatePersistance = (DefaultUpdateCustomerPersistanceService) IOCContainer.getInstance(this)
					.getBean("DefaultCustomerUpdatePersistanceService");
			jsonParser = JSONParser.getInstance();
			//handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
		}
		catch (Exception e)
		{
			//handler.logCrashReport(e);
			e.printStackTrace();
		}
	}
}
