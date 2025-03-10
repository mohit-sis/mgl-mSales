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
package com.mobicule.android.msales.mgl.savedmetereading.view;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.onmplaning.view.DefaultOnMPlanningActivity;
import com.mobicule.android.msales.mgl.util.Base64;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.crashnotifier.GenericExceptionHandler;
import com.mobicule.crashnotifier.IGenericExceptionHandler;
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
* 
* <enter description here>
*
* @author namrata <enter lastname>
* @see 
*
* @createdOn 12-May-2017
* @modifiedOn
*
* @copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class SavedOnMSummaryActivity extends DefaultOnMPlanningActivity
{

	private TextView tv_bpNo, tv_mroNo, tv_timeNDate, tv_msgFrmMr, tv_msgRemarks, tv_noOfBurners, tv_noOfGeysers,
			tv_image1Label, tv_image2Label;

	private ImageView iv_image1, iv_image2;

	private Context context;

	private JSONObject savedOnMPlanningJson;

	private JSONObject savedOnMJson;

	private byte[] imageByte;

	private TextView tv_msgFrmMrLabel;
	
	IGenericExceptionHandler handler;

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

		setContentView(R.layout.saved_onm_planning_summary);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		initializeComponents();
		Log.e("SavedOnMSummaryActivity","SavedOnMSummaryActivity");

		//onmFacade.getOnMPlanning().reset();
		
		context = this;
		
			String mr_no_is=getIntent().getStringExtra("mrno");
			Vector savedRandomMeterReading = onmFacade.fetchSavedOnM(mr_no_is);
			if (savedRandomMeterReading != null && savedRandomMeterReading.size() > 0)
			{
				try
				{
					savedOnMPlanningJson = new JSONObject(savedRandomMeterReading.elementAt(0).toString());
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					handler.logCrashReport(e);
					e.printStackTrace();
				}
				//response = applicationFacade.getCustomerListBasedOnBPNO(mroNo);
				onmFacade
						.initCustomerCycleWithSavedOnMPlanning(savedOnMPlanningJson
								.toString());
			}
			//SavedupdateCustomerString = extras.getString("SavedcustomerUpdate");
			//SavedupdateCustomerString=SavedupdateCustomerStringJson.toString();
			
			MobiculeLogger.verbose("SavedcustomerUpdate");
			
			try
			{
				savedOnMJson = new JSONObject(savedRandomMeterReading.elementAt(0).toString());
				tv_bpNo.setText(savedOnMJson.getValue(Constants.FIELD_BP_NO).toString());
				
				tv_mroNo.setText(savedOnMJson.getValue(Constants.KEY_MRO_NUMBER).toString());

				tv_msgRemarks.setText(savedOnMJson.getValue(Constants.KEY_MSG_REMARKS).toString());

				tv_noOfBurners.setText(savedOnMJson.getValue(Constants.KEY_NO_OF_BURNERS).toString());
				
				tv_noOfGeysers.setText(savedOnMJson.getValue(Constants.KEY_NO_OF_GEYSERS).toString());

				tv_timeNDate.setText(savedOnMJson.getValue(Constants.KEY_DATE).toString() + " " + savedOnMJson.getValue(Constants.KEY_TIME).toString());
				
				String isHoseAvailable = savedOnMJson.getValue(Constants.KEY_IS_HOSE_AVALABLE).toString();
				
				if(isHoseAvailable.equalsIgnoreCase("Yes"))
				{
					tv_msgFrmMr.setVisibility(View.GONE);
					tv_msgFrmMrLabel.setVisibility(View.GONE);
					
					tv_image1Label.setVisibility(View.VISIBLE);
					tv_image2Label.setVisibility(View.VISIBLE);
					
					iv_image1.setVisibility(View.VISIBLE);
					iv_image2.setVisibility(View.VISIBLE);
					
					imageByte = Base64.decode(savedOnMJson.getValue(Constants.KEY_IMAGE1).toString());

					Bitmap image1Bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
					iv_image1.setImageBitmap(image1Bitmap);
					
					imageByte = Base64.decode(savedOnMJson.getValue(Constants.KEY_IMAGE2).toString());
					Bitmap image2Bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
					iv_image2.setImageBitmap(image2Bitmap);
					
				}
				else if(isHoseAvailable.equalsIgnoreCase("No"))
				{
					tv_msgFrmMr.setVisibility(View.VISIBLE);
					tv_msgFrmMrLabel.setVisibility(View.VISIBLE);
					tv_msgFrmMr.setText(savedOnMJson.getValue(Constants.KEY_MSG_FROM_MR).toString());
					
					tv_image1Label.setVisibility(View.GONE);
					tv_image2Label.setVisibility(View.GONE);
					
					iv_image1.setVisibility(View.GONE);
					iv_image2.setVisibility(View.GONE);
				}
				
			}
			catch(JSONException e)
			{
				handler.logCrashReport(e);
				e.printStackTrace();
			}
			catch (IOException e)
			{
				handler.logCrashReport(e);
				e.printStackTrace();
			}

	}

	private void initializeComponents()
	{

		tv_bpNo = (TextView) findViewById(R.id.tv_bpno2);

		tv_mroNo = (TextView) findViewById(R.id.tv_code2);

		tv_timeNDate = (TextView) findViewById(R.id.tv_date2);
		
		tv_msgFrmMrLabel = (TextView) findViewById(R.id.tv_message1);

		tv_msgFrmMr = (TextView) findViewById(R.id.tv_message2);

		tv_msgRemarks = (TextView) findViewById(R.id.tv_remark2);

		tv_noOfBurners = (TextView) findViewById(R.id.tv_burner2);

		tv_noOfGeysers = (TextView) findViewById(R.id.tv_geyser2);

		tv_image1Label = (TextView) findViewById(R.id.tv_photo1);

		tv_image2Label = (TextView) findViewById(R.id.tv_photo2);
		
		iv_image1 = (ImageView) findViewById(R.id.iv_photo1);
		
		iv_image2 = (ImageView) findViewById(R.id.iv_photo2);
		
		//handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());

	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		finish();
	}

}
