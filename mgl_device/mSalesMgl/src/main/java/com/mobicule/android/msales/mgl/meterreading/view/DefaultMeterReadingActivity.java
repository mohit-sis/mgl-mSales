/**
******************************************************************************
* C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
* <p>
* Copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved. 
* This is proprietary information of Mobicule Technologies Pvt. Ltd.and is 
* subject to applicable licensing agreements. Unauthorized reproduction, 
* transmission or distribution of this file and its contents is a 
* violation of applicable laws.
******************************************************************************
*
* @project mSalesMgl
*/
package com.mobicule.android.msales.mgl.meterreading.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.IOCContainer;
import com.mobicule.android.msales.mgl.menu.view.MainMenu;
import com.mobicule.android.msales.mgl.meterreading.model.DefaultMeterReadingPersistenceService;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IMeterReadingInstance;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingFacade;

/**
* 
* <enter description here>
*
* @author nikita 
* @see 
*
* @createdOn Mar 13, 2012
* @modifiedOn
*
* @copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class DefaultMeterReadingActivity extends Activity
{

	protected JSONParser jsonParser;

	protected IMeterReadingFacade meterReadingFacade;

	protected IMeterReadingInstance meterReadingBO;

	protected IApplicationFacade applicationFacade;

	protected DefaultMeterReadingPersistenceService meterReadingPersistance;

	protected static Response response;

	public DefaultMeterReadingActivity()
	{
		defaultInitialization();
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		Icepick.restoreInstanceState(this, savedInstanceState);
		this.logOutReceiver();

	}

	private void defaultInitialization()
	{
		try
		{
			meterReadingFacade = (IMeterReadingFacade) IOCContainer.getInstance(DefaultMeterReadingActivity.this)
					.getBean("DefaultMeterReadingFacade");
			meterReadingBO = meterReadingFacade.getCurrentMeterReadingBO();
			applicationFacade = (IApplicationFacade) IOCContainer.getInstance(this).getBean("ApplicationFacade");
			meterReadingPersistance = (DefaultMeterReadingPersistenceService) IOCContainer.getInstance(this).getBean(
					"DefaultMeterReadingPersistanceService");
			jsonParser = JSONParser.getInstance();
		}
		catch (Exception e)
		{
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
			MobiculeLogger.verbose("onReceive - Logout in progress --> Meter Reading activity");
			DefaultMeterReadingActivity.this.finish();
		}
	};

	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (Constants.isThreePicsSelected)
			{
				if (Constants.threePicsCapCnt > 2 || Constants.threePicsCapCnt <= 0)
				{
					finish();
				}
				else
				{
					return false;
				}
			}
			else
			{
				finish();
			}

		}
		return super.onKeyDown(keyCode, event);
	}
*/
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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
//		Icepick.saveInstanceState(this,outState);
	}


}
