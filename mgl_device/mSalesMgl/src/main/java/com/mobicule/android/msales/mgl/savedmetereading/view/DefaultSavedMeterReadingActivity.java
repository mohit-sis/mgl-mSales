package com.mobicule.android.msales.mgl.savedmetereading.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;

import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.IOCContainer;
import com.mobicule.android.msales.mgl.menu.view.MainMenu;
import com.mobicule.android.msales.mgl.meterreading.model.DefaultMeterReadingPersistenceService;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.crashnotifier.IGenericExceptionHandler;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IMeterReadingInstance;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingFacade;

import java.io.File;

/**
* 
* <enter description here>
*
* @author Sathya Sheela
* @see 
*
* @createdOn April 6, 2012
* @modifiedOn
*
* @copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class DefaultSavedMeterReadingActivity extends Activity
{

	protected JSONParser jsonParser;

	protected IMeterReadingFacade meterReadingFacade;

	protected IMeterReadingInstance meterReadingBO;

	protected IApplicationFacade applicationFacade;

	protected DefaultMeterReadingPersistenceService meterReadingPersistance;

	protected static Response response;
	IGenericExceptionHandler handler;

	public DefaultSavedMeterReadingActivity()
	{
		defaultInitialization();
	}

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

	private void defaultInitialization()
	{
		try
		{

			meterReadingFacade = (IMeterReadingFacade) IOCContainer.getInstance(DefaultSavedMeterReadingActivity.this)
					.getBean("DefaultMeterReadingFacade");
			meterReadingBO = meterReadingFacade.getCurrentMeterReadingBO();
			applicationFacade = (IApplicationFacade) IOCContainer.getInstance(this).getBean("ApplicationFacade");
			meterReadingPersistance = (DefaultMeterReadingPersistenceService) IOCContainer.getInstance(this).getBean(
					"DefaultMeterReadingPersistanceService");
			jsonParser = JSONParser.getInstance();
			// handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
		}
		catch (Exception e)
		{
			//handler.logCrashReport(e);
			e.printStackTrace();
		}
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
			MobiculeLogger.verbose("Logout in progress --> Saved Meter Reading activity");
			DefaultSavedMeterReadingActivity.this.finish();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void goHome(View v)
	{
		Intent intent = new Intent(this, MainMenu.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}
}
