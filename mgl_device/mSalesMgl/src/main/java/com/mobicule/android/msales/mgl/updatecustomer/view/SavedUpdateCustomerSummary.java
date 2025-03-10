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
package com.mobicule.android.msales.mgl.updatecustomer.view;

import java.io.File;
import java.util.Vector;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.util.Base64;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading;
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomer;

/**
 * 
 * <enter description here>
 *
 * @author nikita <enter lastname>
 * @see 
 *
 * @createdOn 12-Apr-2012
 * @modifiedOn
 *
 * @copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
 */
public class SavedUpdateCustomerSummary extends DefaultUpdateCustomerActivity
{
	private JSONObject SavedupdateCustomer;

	private String SavedupdateCustomerString;

	private TextView currentContactNo;

	private TextView currentMeterNo;

	private TextView currentResidentialContactNumText;

	private TextView newResidentialContactNumText;

	private TextView currentOfficeContactNumText;

	private TextView newOfficeContactNumText;

	private TextView customerName;

	private TextView bpNo;

	private TextView mroNo;

	private TextView newContactNoText;

	private TextView newContactNoValue;

	private TextView newMeterNoText;

	private TextView newMeterNoValue;

	private String currentMeterNumber;

	private String currentContactNumber;

	private String newContactNumber;

	private String newMeterNumber;

	private String currentResContactNum;

	private String newResContactNum;

	private String currentOffContactNum;

	private String newOffContactNum;

	private Context context;
	private JSONObject SavedupdateCustomerStringJson;

	private LinearLayout ll_meterNumberLayout, ll_contactNumberLayout, ll_addressLayout;

	private TextView tv_currentFlat, tv_newFlat, tv_newFlatText, tv_currentFlatText, tv_currentFloor, tv_newFloor, tv_newFloorText, 
	tv_currentFloorText, tv_currentWing, tv_currentWingText, tv_newWing, tv_newWingText, tv_currentPlot,
	tv_currentPlotText, tv_newPlot, tv_newPlotText, tv_currentBuildingName, tv_currentBuildingNameText, tv_newBuildingName, tv_newBuildingNameText,
	tv_image1, tv_image2, tv_image3, tv_documentType, tv_documentTypeText, currentMeterNumberLabel, currentContactNumberLabel, currentResidentialContactNumLabel, newResidentialContactNumLabel, currentOfficeContactNumLabel, newOfficeContactNumLabel,tv_remarksLabel, tv_remarksValue;

	private ImageView iv_image1, iv_image2, iv_image3;

	@Override
	protected void onStart()
	{
		super.onStart();
		Constants.broadcastDialogContext = this;
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

		setContentView(R.layout.updatesaved);

		initalization();

		updateCustomerFacade.getCustomerUpdate().reset();

		context = this;
		//if (getIntent().hasExtra("SavedcustomerUpdate"))
		//{
			String mr_no_is=getIntent().getStringExtra("mrno");
			Vector savedRandomMeterReading = updateCustomerFacade.fetchSavedCustomerUpdate(mr_no_is);
			if (savedRandomMeterReading != null && savedRandomMeterReading.size() > 0)
			{
				try
				{
					SavedupdateCustomerStringJson = new JSONObject(savedRandomMeterReading.elementAt(0).toString());
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block

					handler.logCrashReport(e);
					e.printStackTrace();
				}
				//response = applicationFacade.getCustomerListBasedOnBPNO(mroNo);
				updateCustomerFacade
						.initCustomerUpdateCycleWithSavedCustomerUpdate(SavedupdateCustomerStringJson
								.toString());
			}
			//SavedupdateCustomerString = extras.getString("SavedcustomerUpdate");
			//SavedupdateCustomerString=SavedupdateCustomerStringJson.toString();
			
			MobiculeLogger.verbose("SavedcustomerUpdate");
			
			try
			{
				SavedupdateCustomer = new JSONObject(savedRandomMeterReading.elementAt(0).toString());
				
				customerName.setText(SavedupdateCustomer.getValue(Constants.FIELD_CUSTOMER_NAME).toString());

				bpNo.setText(SavedupdateCustomer.getValue(Constants.FIELD_BP_NO).toString());

				mroNo.setText(SavedupdateCustomer.getValue(Constants.KEY_MRO_NUMBER).toString());
				
				if(!SavedupdateCustomer.getValue(IUpdateCustomer.KEY_REMARK).toString().equals(""))
				{
					tv_remarksLabel.setVisibility(View.VISIBLE);
					tv_remarksValue.setVisibility(View.VISIBLE);
					tv_remarksValue.setText(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_REMARK).toString());
				}

				if(SavedupdateCustomer.getValue(Constants.FIELD_NEW_METER_NO).toString().equalsIgnoreCase(""))
				{
					ll_meterNumberLayout.setVisibility(View.GONE);
				}
				else
				{
					ll_meterNumberLayout.setVisibility(View.VISIBLE);
					currentMeterNumberLabel.setVisibility(View.VISIBLE);
					currentMeterNo.setVisibility(View.VISIBLE);
					currentMeterNo.setText(SavedupdateCustomer.getValue(Constants.FIELD_CURR_METER_NO).toString());
					newMeterNoText.setVisibility(View.VISIBLE);
					newMeterNoValue.setVisibility(View.VISIBLE);
					newMeterNoValue.setText(SavedupdateCustomer.getValue(Constants.FIELD_NEW_METER_NO).toString());
					
				}
				
				if(SavedupdateCustomer.getValue(Constants.FIELD_NEW_CONTACT_NO).toString().equalsIgnoreCase("") && SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_CONTACT_NO_HOME).toString().equalsIgnoreCase("") && SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_CONTACT_NO_OFFICE).toString().equalsIgnoreCase(""))
				{
					ll_contactNumberLayout.setVisibility(View.GONE);
				}
				else
				{
					ll_contactNumberLayout.setVisibility(View.VISIBLE);
					if(!SavedupdateCustomer.getValue(Constants.FIELD_NEW_CONTACT_NO).equals(""))
					{
						currentContactNumberLabel.setVisibility(View.VISIBLE);
						currentContactNo.setVisibility(View.VISIBLE);
						currentContactNo.setText(SavedupdateCustomer.getValue(Constants.FIELD_CURR_CONTACT_NO).toString());
						
						newContactNoText.setVisibility(View.VISIBLE);
						newContactNoValue.setVisibility(View.VISIBLE);
						newContactNoValue.setText(SavedupdateCustomer.getValue(Constants.FIELD_NEW_CONTACT_NO).toString());
					}
					
					if(!SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_CONTACT_NO_HOME).toString().equals(""))
					{
						currentResidentialContactNumLabel.setVisibility(View.VISIBLE);
						currentResidentialContactNumText.setVisibility(View.VISIBLE);
						currentResidentialContactNumText.setText(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_CURRENT_CONTACT_NO_HOME).toString());
						
						newResidentialContactNumLabel.setVisibility(View.VISIBLE);
						newResidentialContactNumText.setVisibility(View.VISIBLE);
						newResidentialContactNumText.setText(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_CONTACT_NO_HOME).toString());
						
					}
					
					if(!SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_CONTACT_NO_OFFICE).toString().equals(""))
					{
						currentOfficeContactNumLabel.setVisibility(View.VISIBLE);
						currentOfficeContactNumText.setVisibility(View.VISIBLE);
						currentOfficeContactNumText.setText(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_CURRENT_CONTACT_NO_OFFICE).toString());
						
						newOfficeContactNumLabel.setVisibility(View.VISIBLE);
						newOfficeContactNumText.setVisibility(View.VISIBLE);
						newOfficeContactNumText.setText(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_CONTACT_NO_OFFICE).toString());
					}
				}
				
				if(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_FLAT_NUMBER).toString().equals("") && SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_FLOOR_NUMBER).toString().equals("") && SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_PLOTNO).toString().equals("") && SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_WING).toString().equals("") && SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_BUILDING_NAME).toString().equals(""))
				{
					ll_addressLayout.setVisibility(View.VISIBLE);
				}
				else
				{
					ll_addressLayout.setVisibility(View.VISIBLE);
					if(!SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_FLAT_NUMBER).toString().equals(""))
					{
						tv_currentFlat.setVisibility(View.VISIBLE);
						tv_currentFlatText.setVisibility(View.VISIBLE);
						tv_currentFlatText.setText(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_FLAT_NUMBER).toString());
						
						tv_newFlat.setVisibility(View.VISIBLE);
						tv_newFlatText.setVisibility(View.VISIBLE);
						tv_newFlatText.setText(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_FLAT_NUMBER).toString());
					}
					if(!SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_FLOOR_NUMBER).toString().equals(""))
					{
						tv_currentFloor.setVisibility(View.VISIBLE);
						tv_currentFloorText.setVisibility(View.VISIBLE);
						tv_currentFloorText.setText(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_FLOOR_NUMBER).toString());
						
						tv_newFloor.setVisibility(View.VISIBLE);
						tv_newFloorText.setVisibility(View.VISIBLE);
						tv_newFloorText.setText(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_FLOOR_NUMBER).toString());
					}
					if(!SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_PLOTNO).toString().equals(""))
					{
						tv_currentPlot.setVisibility(View.VISIBLE);
						tv_currentPlotText.setVisibility(View.VISIBLE);
						tv_currentPlotText.setText(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_PLOTNO).toString());
						
						tv_newPlot.setVisibility(View.VISIBLE);
						tv_newPlotText.setVisibility(View.VISIBLE);
						tv_newPlotText.setText(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_PLOTNO).toString());
					}
					if(!SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_WING).toString().equals(""))
					{
						tv_currentWing.setVisibility(View.VISIBLE);
						tv_currentWingText.setVisibility(View.VISIBLE);
						tv_currentWingText.setText(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_WING).toString());
						
						tv_newWing.setVisibility(View.VISIBLE);
						tv_newWingText.setVisibility(View.VISIBLE);
						tv_newWingText.setText(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_WING).toString());
					}
					if(!SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_BUILDING_NAME).toString().equals(""))
					{
						tv_currentBuildingName.setVisibility(View.VISIBLE);
						tv_currentBuildingNameText.setVisibility(View.VISIBLE);
						tv_currentBuildingNameText.setText(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_BUILDING_NAME).toString());
						
						tv_newBuildingName.setVisibility(View.VISIBLE);
						tv_newBuildingNameText.setVisibility(View.VISIBLE);
						tv_newBuildingNameText.setText(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_NEW_BUILDING_NAME).toString());
					}
					
					tv_documentType.setVisibility(View.VISIBLE);
					tv_documentTypeText.setVisibility(View.VISIBLE);
					tv_documentTypeText.setText(SavedupdateCustomer.getValue(IUpdateCustomer.KEY_DOC_TYPE).toString());
					
					if(!SavedupdateCustomer.getValue(IUpdateCustomer.KEY_DOC_IMG_ONE).toString().equals(""))
					{
						tv_image1.setVisibility(View.VISIBLE);
						iv_image1.setVisibility(View.VISIBLE);
						
						String image2 = SavedupdateCustomer.getValue(IUpdateCustomer.KEY_DOC_IMG_ONE).toString();
						byte[] image2byteArray = Base64.decode(image2);
						
						Bitmap bitmap1 = BitmapFactory.decodeByteArray(image2byteArray, 0, image2byteArray.length);
						iv_image1.setImageBitmap(bitmap1);
						
					}
					if(!SavedupdateCustomer.getValue(IUpdateCustomer.KEY_DOC_IMG_TWO).toString().equals(""))
					{
						tv_image2.setVisibility(View.VISIBLE);
						iv_image2.setVisibility(View.VISIBLE);
						
						String image2 = SavedupdateCustomer.getValue(IUpdateCustomer.KEY_DOC_IMG_TWO).toString();
						byte[] image2byteArray = Base64.decode(image2);
						
						Bitmap bitmap1 = BitmapFactory.decodeByteArray(image2byteArray, 0, image2byteArray.length);
						iv_image2.setImageBitmap(bitmap1);
					}
					if(!SavedupdateCustomer.getValue(IUpdateCustomer.KEY_DOC_IMG_THREE).toString().equals(""))
					{
						tv_image3.setVisibility(View.VISIBLE);
						iv_image3.setVisibility(View.VISIBLE);
						
						String image2 = SavedupdateCustomer.getValue(IUpdateCustomer.KEY_DOC_IMG_THREE).toString();
						byte[] image2byteArray = Base64.decode(image2);
						
						Bitmap bitmap1 = BitmapFactory.decodeByteArray(image2byteArray, 0, image2byteArray.length);
						iv_image3.setImageBitmap(bitmap1);
					}
				}
				
				//Renu**
/*				Bundle b=getIntent().getExtras();
				customerName.setText(b.getString("name"));
				String bpNumber1 = b.getString("bpNo");
				bpNo.setText(bpNumber1);
				String mroNumber1 =  b.getString("mroNo");
				mroNo.setText(mroNumber1);

				
				newContactNoText.setVisibility(View.VISIBLE);
				newContactNoValue.setVisibility(View.VISIBLE);

				newMeterNoText.setVisibility(View.VISIBLE);
				newMeterNoValue.setVisibility(View.VISIBLE);

				currentContactNumber = b.getString("currentContactNo");
				currentContactNo.setText(currentContactNumber);

				newContactNumber = b.getString("newContactNo");
				newContactNoValue.setText(newContactNumber);

				currentMeterNumber =b.getString("currentMeter");
				currentMeterNo.setText(currentMeterNumber);
				newMeterNumber = b.getString("NewMeter");
				newMeterNoValue.setText(newMeterNumber);

				currentResContactNum =b.getString("currentResNo");
				newResContactNum = b.getString("newResNo");
				currentOffContactNum = b.getString("currentOfficeNo");
				newOffContactNum = b.getString("newOfficeNo");
				
				currentResidentialContactNumText.setText(currentResContactNum);
				newResidentialContactNumText.setText(newResContactNum);
				currentOfficeContactNumText.setText(currentOffContactNum);
				newOfficeContactNumText.setText(newOffContactNum);
*/
				//**
				
			}
			catch (Exception e)
			{

				handler.logCrashReport(e);
				e.printStackTrace();
			}
		//}
	}

	public void initalization()
	{
		bpNo = (TextView) findViewById(R.id.bpNumber);
		mroNo = (TextView) findViewById(R.id.mroNumber);
		customerName = (TextView) findViewById(R.id.customerName);
		
		ll_meterNumberLayout = (LinearLayout) findViewById(R.id.ll_meterNumberLayout);
		currentMeterNumberLabel = (TextView) findViewById(R.id.meternum_TextView);
		currentMeterNo = (TextView) findViewById(R.id.meterNumber);
		newMeterNoText = (TextView) findViewById(R.id.newMeternum_TextView);
		newMeterNoValue = (TextView) findViewById(R.id.newMeterNumber);
		
		ll_contactNumberLayout = (LinearLayout) findViewById(R.id.ll_contactNumberLayout);
		currentContactNumberLabel = (TextView) findViewById(R.id.contactNumberText);
		currentContactNo = (TextView) findViewById(R.id.contactNumber);
		newContactNoText = (TextView) findViewById(R.id.NewcontactNumberText);
		newContactNoValue = (TextView) findViewById(R.id.newcontactNumber);
		
		currentResidentialContactNumLabel = (TextView) findViewById(R.id.currentResidentialContactNum);
		currentResidentialContactNumText = (TextView) findViewById(R.id.currentResidentialContactNumText);
		newResidentialContactNumLabel = (TextView) findViewById(R.id.newResidentialContactNum);
		newResidentialContactNumText = (TextView) findViewById(R.id.newResidentialContactNumText);
		
		currentOfficeContactNumLabel = (TextView) findViewById(R.id.currentOfficeContactNum);
		currentOfficeContactNumText = (TextView) findViewById(R.id.currentOfficeContactNumText);
		newOfficeContactNumLabel = (TextView) findViewById(R.id.newOfficeContactNum);
		newOfficeContactNumText = (TextView) findViewById(R.id.newOfficeContactNumText);
		
		ll_addressLayout = (LinearLayout) findViewById(R.id.ll_addressUpdateLayout);
		
		tv_currentFlat = (TextView) findViewById(R.id.tv_currentFlat);
		tv_currentFlatText = (TextView) findViewById(R.id.tv_currentFlatText);
		tv_newFlat = (TextView) findViewById(R.id.tv_newFlat);
		tv_newFlatText = (TextView) findViewById(R.id.tv_newFlatText);
		
		tv_currentFloor = (TextView) findViewById(R.id.tv_currentFloor);
		tv_currentFloorText = (TextView) findViewById(R.id.tv_currentFloorText);
		tv_newFloor = (TextView) findViewById(R.id.tv_newFloor);
		tv_newFloorText = (TextView) findViewById(R.id.tv_newFloorText);
		
		tv_currentWing = (TextView) findViewById(R.id.tv_currentWing);
		tv_currentWingText = (TextView) findViewById(R.id.tv_currentWingText);
		tv_newWing = (TextView) findViewById(R.id.tv_newWing);
		tv_newWingText = (TextView) findViewById(R.id.tv_newWingText);
		
		tv_currentPlot = (TextView) findViewById(R.id.tv_currentPlot);
		tv_currentPlotText = (TextView) findViewById(R.id.tv_currentPlotText);
		tv_newPlot = (TextView) findViewById(R.id.tv_newPlot);
		tv_newPlotText = (TextView) findViewById(R.id.tv_newPlotText);
		
		tv_currentBuildingName = (TextView) findViewById(R.id.tv_currentBuildingName);
		tv_currentBuildingNameText = (TextView) findViewById(R.id.tv_currentBuildingNameText);
		tv_newBuildingName = (TextView) findViewById(R.id.tv_newBuildingName);
		tv_newBuildingNameText = (TextView) findViewById(R.id.tv_newBuildingNameText);
		
		tv_image1 = (TextView) findViewById(R.id.tv_image1);
		tv_image2 = (TextView) findViewById(R.id.tv_image2);
		tv_image3 = (TextView) findViewById(R.id.tv_image3);
		tv_documentType = (TextView) findViewById(R.id.tv_documentType);
		tv_documentTypeText = (TextView) findViewById(R.id.tv_documentTypeText);
		
		iv_image1 = (ImageView) findViewById(R.id.iv_image1);
		iv_image2 = (ImageView) findViewById(R.id.iv_image2);
		iv_image3 = (ImageView) findViewById(R.id.iv_image3);
		
		tv_remarksLabel = (TextView) findViewById(R.id.tv_remarksLabel);
		tv_remarksValue = (TextView) findViewById(R.id.tv_remarksValue);
		
	}
	
	@Override
	public void onBackPressed()
	{
		finish();
	}
}
