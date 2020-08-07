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
package com.mobicule.android.msales.mgl.jointicketing.view;

import org.json.me.JSONObject;

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
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.crashnotifier.GenericExceptionHandler;
import com.mobicule.crashnotifier.IGenericExceptionHandler;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.jointicketing.IJoinTicketing;
import com.mobicule.msales.mgl.client.jointicketing.IJoinTicketingFacade;
import com.mobicule.msales.mgl.client.jointicketing.IJoinTicketingPersistance;

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
public class DefaultJoinTicketingActivity extends Activity
{

	protected JSONParser jsonParser;

	protected IJoinTicketingFacade joinTicketingFacade;

	protected IJoinTicketing joinTicketingBO;

	protected IApplicationFacade applicationFacade;

	protected IJoinTicketingPersistance joinTicketingPersistance;

	protected static Response response;

	public DefaultJoinTicketingActivity()
	{
		defaultInitialization();
	}

	protected void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		this.logOutReceiver();
		
	}

	private void defaultInitialization()
	{
		try
		{
			joinTicketingFacade = (IJoinTicketingFacade) IOCContainer.getInstance(DefaultJoinTicketingActivity.this)
					.getBean("DefaultJoinTicketingFacade");
			joinTicketingBO = joinTicketingFacade.getCurrentJoinTicketingBO();
			applicationFacade = (IApplicationFacade) IOCContainer.getInstance(this).getBean("ApplicationFacade");
			joinTicketingPersistance = (IJoinTicketingPersistance) IOCContainer.getInstance(this).getBean(
					"DefaultJoinTicketingPersistenceService");
			jsonParser = JSONParser.getInstance();
		}
		catch (Exception e)
		{
			/*IGenericExceptionHandler handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
			handler.setIsLogEnable(true);*/
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
			MobiculeLogger.verbose("Logout in progress --> Meter Reading activity");
			DefaultJoinTicketingActivity.this.finish();
		}
	};

	public void goHome(View v)
	{
		joinTicketingFacade.deleteFromJoinTicketingImage("");

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

	public void buildImageJsonAndSaveInDb(final String entity, final String imgStr, final String remark)
	{
		MobiculeLogger.verbose("buildImageJsonAndSaveInDb - inside buildImageJsonAndSaveInDb");

		try
		{
			JSONObject imageJson = new JSONObject();

			imageJson.put("type", "jointTicket");
			imageJson.put("entity", entity);
			imageJson.put("action", "submit");

			JSONObject imgReqJson = new JSONObject();

			Response responseTemp = applicationFacade.getUserDtail();

			imageJson.put("user", new JSONObject(responseTemp.getData().toString()));
			imgReqJson.put("mroNo", joinTicketingBO.getMroNo().toString());

			MobiculeLogger.verbose("imgReqJson.put(mroNo) - "+ joinTicketingBO.getMroNo().toString());

			imgReqJson.put("image", imgStr);
			imgReqJson.put("jtSubmit", "0");
			imgReqJson.put("imageRemark", remark);
			imageJson.put("data", imgReqJson);

			MobiculeLogger.verbose("buildImageJsonAndSaveInDb - imageJson: " + imageJson);

			joinTicketingFacade.addJoinTicketingImage(imageJson.toString());

		}
		catch (Exception e)
		{/*
			IGenericExceptionHandler handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
			handler.setIsLogEnable(true);*/
			e.printStackTrace();
		}
	}

}
