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
 * @project MahanagarGasLimitedNew
 */
package com.mobicule.android.msales.mgl.updatecustomer.view;

import java.io.File;
import java.sql.PreparedStatement;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.meterreading.view.CustomerInformation;
import com.mobicule.android.msales.mgl.meterreading.view.CustomerSummary;
import com.mobicule.android.msales.mgl.meterreading.view.MeterReading;
import com.mobicule.android.msales.mgl.mybookwalk.view.MyBookWalk;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.android.msales.mgl.util.FileUtil;

/**
 * 
 * <enter description here>
 * 
 * @author nikita
 * @see
 * 
 * @createdOn 20-Feb-2012
 * @modifiedOn
 * 
 * @copyright © 2010-2011 Mobicule Technologies Pvt. Ltd. All rights reserved.
 */
public class UpdateCustomerSummaryActivity extends DefaultUpdateCustomerActivity
{

	private Button contactNoMobileButton;

	private Button contactNoOfficeButton;

	private Button contactNoHomeButton;

	private Button meterNoButton;

	private Button customerNameButton;

	private TextView currentContactNoMobile;

	private TextView currentContactNoHome;

	private TextView currentContactNoOffice;

	private TextView currentMeterNo;

	private TextView customerName;

	private TextView bpNo;

	private TextView mroNo;

	private TextView newContactNoMobileText;

	private TextView newContactNoMobileValue;

	private TextView newContactNoHomeText;

	private TextView newContactNoHomeValue;

	private TextView newContactNoOfficeText;

	private TextView newContactNoOfficeValue;

	private TextView newMeterNoText;

	private TextView newMeterNoValue;

	private TextView newCustomerNameText;

	private TextView newCustomerNameValue;

	private TextView newFloorText;

	private TextView newFloorValue;

	private TextView newFlatText;

	private TextView newFlatValue;

	private TextView newWingText;

	private TextView newWingValue;

	private TextView newBuildingNameText;

	private TextView newBuildingNameValue;

	private TextView newPlotText;

	private TextView newPlotValue;

	private String updateMeterNo;

	private String updateContactNo;

	private String getUpdateCustomerJson;

	private String updateCustomerBysearch;

	private String updateCompletedCustomerBysearch;

	private String currentContactNumberMobile;

	private String currentContactNumberHome;

	private String currentContactNumberOffice;

	private String currentMeterNumber;

	private JSONObject updateCustomer;

	private JSONObject updateCustomerBySearch;

	private JSONObject updateCompletedCustomerBySearch;

	private Context context;

	private TextView flatNo;

	private TextView floorNo;

	private TextView buildName;

	private TextView plotNo;

	private TextView wingNo;

	protected String custNameType = "Customer Name";

	protected String floorType = "Floor Number";

	protected String flatType = "Flat Number";

	protected String plotType = "Plot Number";

	protected String wingType = "Wing Number";

	protected String buildNameType = "Building Name";

	private Button flatNumberButton;

	private Button plotNumberButton;

	private Button floorNumberButton;

	private Button wingButton;

	private Button buildnameButton;

	private EditText editTextRemark;

	private Button cancelButton;

	private Button updateButton;

	private enum ContactType
	{
		MOBILE, HOME, OFFICE
	};

	@Override
	protected void onStart()
	{
		super.onStart();
		Constants.broadcastDialogContext = this;
	}

	private void updateContactNo(final ContactType contactType)
	{
		try
		{
			final Dialog updateContactNoDialog = new Dialog(context);
			updateContactNoDialog.setContentView(R.layout.update_cont_num);

			final TextView updateTextView = (TextView) updateContactNoDialog.findViewById(R.id.update_TextView);
			final EditText updateEditText = (EditText) updateContactNoDialog.findViewById(R.id.update_contactNo);

			Button okButton = (Button) updateContactNoDialog.findViewById(R.id.ok);

			if (contactType == ContactType.MOBILE)
			{
				updateContactNoDialog.setTitle("Update Contact Number (Mobile)");
				updateTextView.setText("Enter New Contact Number (Mobile): ");
			}
			else if (contactType == ContactType.OFFICE)
			{
				updateContactNoDialog.setTitle("Update Contact Number (Office)");
				updateTextView.setText("Enter New Contact Number (Office): ");
			}
			else
			{
				updateContactNoDialog.setTitle("Update Contact Number (Home)");
				updateTextView.setText("Enter New Contact Number (Home): ");
			}

			okButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					updateContactNo = updateEditText.getText().toString().trim();
					if (updateContactNo.equals(""))
					{
						Toast.makeText(UpdateCustomerSummaryActivity.this, "Enter Contact Number", Toast.LENGTH_LONG)
								.show();
					}
					else
					{
						if (contactType == ContactType.MOBILE)
						{
							newContactNoMobileText.setVisibility(View.VISIBLE);
							newContactNoMobileValue.setVisibility(View.VISIBLE);
							newContactNoMobileValue.setText(updateContactNo);
							updateCustomerBO.setNewContactNoMobile(updateContactNo);
						}
						else if (contactType == ContactType.OFFICE)
						{
							newContactNoOfficeText.setVisibility(View.VISIBLE);
							newContactNoOfficeValue.setVisibility(View.VISIBLE);
							newContactNoOfficeValue.setText(updateContactNo);
							updateCustomerBO.setNewContactNoOffice(updateContactNo);
						}
						else
						{
							newContactNoHomeText.setVisibility(View.VISIBLE);
							newContactNoHomeValue.setVisibility(View.VISIBLE);
							newContactNoHomeValue.setText(updateContactNo);
							updateCustomerBO.setNewContactNoHome(updateContactNo);
						}

						updateContactNoDialog.dismiss();
					}
				}
			});

			updateContactNoDialog.show();
		}
		catch (Exception e)
		{
			FileUtil.writeToFile("//UpdateCustomerSummaryActivity : Exception :: " + e);

			handler.logCrashReport(e);
			MobiculeLogger.verbose("updateContactNo() - " + e.toString());
		}
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

		setContentView(R.layout.newupdate);

		updateCustomerFacade.getCustomerUpdate().reset();

		initalization();

		context = this;

		if (getIntent().hasExtra("updateCustomer"))
		{
			MobiculeLogger.verbose("updateCustomer");
			Bundle extras = getIntent().getExtras();
			getUpdateCustomerJson = extras.getString("updateCustomer");
			try
			{
				updateCustomer = new JSONObject(getUpdateCustomerJson);
				customerName.setText(updateCustomer.getValue(Constants.FIELD_CUSTOMER_NAME).toString());
				String bpNumber = updateCustomer.getValue(Constants.FIELD_BP_NO).toString();
				bpNo.setText(bpNumber);
				updateCustomerBO.setBPNumber(bpNumber);

				String flatNumber = updateCustomer.getValue(Constants.FIELD_FLAT_NO).toString();
				flatNo.setText(flatNumber);
				updateCustomerBO.setFlatNumber(flatNumber);

				String floorNumber = updateCustomer.getValue(getString(R.string.key_field_floor)).toString();
				floorNo.setText(floorNumber);
				updateCustomerBO.setFloor(floorNumber);

				String plot = updateCustomer.getValue(getString(R.string.key_field_plot)).toString();
				plotNo.setText(plot);
				updateCustomerBO.setPlot(plot);

				String buildNameStr = updateCustomer.getValue(getString(R.string.key_field_buildingname)).toString();
				buildName.setText(buildNameStr);
				updateCustomerBO.setBuildName(buildNameStr);

				String wing = updateCustomer.getValue(getString(R.string.key_field_wing)).toString();
				wingNo.setText(wing);
				updateCustomerBO.setWing(wing);

				String mroNumber = updateCustomer.getValue(Constants.KEY_MRO_NUMBER).toString();
				mroNo.setText(mroNumber);
				updateCustomerBO.setMroNo(mroNumber);

				currentContactNumberMobile = updateCustomer.getValue(Constants.KEY_CUSTOMER_CONTACT_NUMBER).toString();
				currentContactNoMobile.setText(currentContactNumberMobile);
				updateCustomerBO.setCurrContactNoMobile(currentContactNumberMobile);

				currentContactNumberHome = updateCustomer.getValue(Constants.KEY_CUSTOMER_LANDLINE_NUMBER).toString();
				currentContactNoHome.setText(currentContactNumberHome);
				updateCustomerBO.setCurrContactNoHome(currentContactNumberHome);

				currentContactNumberOffice = updateCustomer.getValue(Constants.KEY_CUSTOMER_OFFICE_NUMBER).toString();
				currentContactNoOffice.setText(currentContactNumberOffice);
				updateCustomerBO.setCurrContactNoOffice(currentContactNumberOffice);

				updateCustomerBO.setCustomerName(updateCustomer.getValue(Constants.FIELD_CUSTOMER_NAME).toString());

				currentMeterNumber = updateCustomer.getValue(Constants.FIELD_METER_NO).toString();
				currentMeterNo.setText(currentMeterNumber);
				updateCustomerBO.setCurrentMeterNO(currentMeterNumber);

				updateCustomerBO.setNewFlatNumber(Constants.EMPTY_STRING);
				updateCustomerBO.setNewFloor(Constants.EMPTY_STRING);
				updateCustomerBO.setNewBuildName(Constants.EMPTY_STRING);
				updateCustomerBO.setNewCustomerName(Constants.EMPTY_STRING);
				updateCustomerBO.setNewWing(Constants.EMPTY_STRING);
				updateCustomerBO.setNewPlot(Constants.EMPTY_STRING);
				updateCustomerBO.setNewContactNoMobile(Constants.EMPTY_STRING);
				updateCustomerBO.setNewContactNoHome(Constants.EMPTY_STRING);
				updateCustomerBO.setNewContactNoOffice(Constants.EMPTY_STRING);
				updateCustomerBO.setNewMeterNO(Constants.EMPTY_STRING);
				MobiculeLogger.verbose("UPdate customerbo" + updateCustomerBO.toJSON());

			}
			catch (JSONException e)
			{
				FileUtil.writeToFile("//UpdateCustomerSummaryActivity : Exception :: " + e);


				handler.logCrashReport(e);
				e.printStackTrace();
			}
		}
		else if (getIntent().hasExtra("updateCustomerBySearch"))
		{
			MobiculeLogger.verbose("updateCustomerBySearch     ");

			Bundle extras = getIntent().getExtras();
			updateCustomerBysearch = extras.getString("updateCustomerBySearch");

			MobiculeLogger.verbose("updateCustomerBySearch    json is:    " + updateCustomerBysearch);
			try
			{
				updateCustomerBySearch = new JSONObject(updateCustomerBysearch);
				customerName.setText(updateCustomerBySearch.getValue(Constants.FIELD_CUSTOMER_NAME).toString());
				String bpNumber = updateCustomerBySearch.getValue(Constants.FIELD_BP_NO).toString();
				bpNo.setText(bpNumber);
				updateCustomerBO.setBPNumber(bpNumber);
				String mroNumber = updateCustomerBySearch.getValue(Constants.KEY_MRO_NUMBER).toString();
				mroNo.setText(mroNumber);
				updateCustomerBO.setMroNo(mroNumber);

				currentContactNumberMobile = updateCustomerBySearch.getValue(Constants.KEY_CUSTOMER_CONTACT_NUMBER)
						.toString();
				currentContactNoMobile.setText(currentContactNumberMobile);
				updateCustomerBO.setCurrContactNoMobile(currentContactNumberMobile);

				currentContactNumberHome = updateCustomerBySearch.getValue(Constants.KEY_CUSTOMER_LANDLINE_NUMBER)
						.toString();
				currentContactNoHome.setText(currentContactNumberHome);
				updateCustomerBO.setCurrContactNoHome(currentContactNumberHome);

				currentContactNumberOffice = updateCustomerBySearch.getValue(Constants.KEY_CUSTOMER_OFFICE_NUMBER)
						.toString();
				currentContactNoOffice.setText(currentContactNumberOffice);
				updateCustomerBO.setCurrContactNoMobile(currentContactNumberOffice);

				updateCustomerBO.setCustomerName(updateCustomerBySearch.getValue(Constants.FIELD_CUSTOMER_NAME)
						.toString());

				currentMeterNumber = updateCustomerBySearch.getValue(Constants.FIELD_METER_NO).toString();
				currentMeterNo.setText(currentMeterNumber);
				updateCustomerBO.setCurrentMeterNO(currentMeterNumber);
				updateCustomerBO.setNewMeterNO("");
			}
			catch (JSONException e)
			{
				FileUtil.writeToFile("//UpdateCustomerSummaryActivity : Exception :: " + e);

				handler.logCrashReport(e);
				e.printStackTrace();
			}

		}
		else if (getIntent().hasExtra("updateCompletedCustomerBySearch"))
		{
			MobiculeLogger.verbose("UpdateCustomer    ", "updateCompletedCustomerBySearch     ");

			Bundle extras = getIntent().getExtras();
			updateCompletedCustomerBysearch = extras.getString("updateCompletedCustomerBySearch");

			MobiculeLogger.verbose("UpdateCustomer    ", "updateCustomerBySearch    json is:    " + updateCompletedCustomerBysearch);
			try
			{
				updateCompletedCustomerBySearch = new JSONObject(updateCompletedCustomerBysearch);
				customerName
						.setText(updateCompletedCustomerBySearch.getValue(Constants.FIELD_CUSTOMER_NAME).toString());
				String bpNumber = updateCompletedCustomerBySearch.getValue(Constants.FIELD_BP_NO).toString();
				bpNo.setText(bpNumber);
				updateCustomerBO.setBPNumber(bpNumber);
				String mroNumber = updateCompletedCustomerBySearch.getValue(Constants.KEY_MRO_NUMBER).toString();
				mroNo.setText(mroNumber);
				updateCustomerBO.setMroNo(mroNumber);

				currentContactNumberMobile = updateCompletedCustomerBySearch.getValue(
						Constants.KEY_CUSTOMER_CONTACT_NUMBER).toString();
				currentContactNoMobile.setText(currentContactNumberMobile);
				updateCustomerBO.setCurrContactNoMobile(currentContactNumberMobile);

				currentContactNumberHome = updateCompletedCustomerBySearch.getValue(
						Constants.KEY_CUSTOMER_LANDLINE_NUMBER).toString();
				currentContactNoHome.setText(currentContactNumberHome);
				updateCustomerBO.setCurrContactNoHome(currentContactNumberHome);

				currentContactNumberOffice = updateCompletedCustomerBySearch.getValue(
						Constants.KEY_CUSTOMER_OFFICE_NUMBER).toString();
				currentContactNoOffice.setText(currentContactNumberOffice);
				updateCustomerBO.setCurrContactNoMobile(currentContactNumberOffice);

				updateCustomerBO.setCustomerName(updateCompletedCustomerBySearch
						.getValue(Constants.FIELD_CUSTOMER_NAME).toString());

				currentMeterNumber = updateCompletedCustomerBySearch.getValue(Constants.FIELD_METER_NO).toString();
				currentMeterNo.setText(currentMeterNumber);
				updateCustomerBO.setCurrentMeterNO(currentMeterNumber);
				updateCustomerBO.setNewMeterNO("");
			}
			catch (JSONException e)
			{
				FileUtil.writeToFile("//UpdateCustomerSummaryActivity : Exception :: " + e);

				handler.logCrashReport(e);
				e.printStackTrace();
			}

		}

		contactNoMobileButton.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				updateContactNo(ContactType.MOBILE);
				/*final Dialog updateContactNoDialog = new Dialog(context);
				updateContactNoDialog.setContentView(R.layout.update_cont_num);
				updateContactNoDialog.setTitle("Update Contact Number");

				final EditText updateEditText = (EditText) updateContactNoDialog.findViewById(R.id.update_contactNo);

				Button okButton = (Button) updateContactNoDialog.findViewById(R.id.ok);

				okButton.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						updateContactNo = updateEditText.getText().toString().trim();
						if (updateContactNo.equals(""))
						{
							Toast.makeText(UpdateCustomerSummaryActivity.this, "Enter Contact Number",
									Toast.LENGTH_LONG).show();
						}
						else
						{
							newContactNoMobileText.setVisibility(View.VISIBLE);
							newContactNoMobileValue.setVisibility(View.VISIBLE);
							newContactNoMobileValue.setText(updateContactNo);
							updateCustomerBO.setNewContactNoMobile(updateContactNo);
							updateContactNoDialog.dismiss();
						}
					}
				});

				updateContactNoDialog.show();*/
			}
		});

		contactNoHomeButton.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				updateContactNo(ContactType.HOME);
			}
		});

		contactNoOfficeButton.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				updateContactNo(ContactType.OFFICE);
			}
		});

		meterNoButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{

				final Dialog updateMeterNoDialog = new Dialog(context);
				updateMeterNoDialog.setContentView(R.layout.update_meter_num);
				updateMeterNoDialog.setTitle("Update Meter Number");

				final EditText updateEditText = (EditText) updateMeterNoDialog.findViewById(R.id.uadatmeterNo);

				Button okButton = (Button) updateMeterNoDialog.findViewById(R.id.ok);

				okButton.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						updateMeterNo = updateEditText.getText().toString().trim();
						{
							if (updateMeterNo.equals(""))
							{
								Toast.makeText(UpdateCustomerSummaryActivity.this, "Enter Meter Number",
										Toast.LENGTH_LONG).show();
							}
							else
							{
								updateMeterNoDialog.dismiss();
								newMeterNoText.setVisibility(View.VISIBLE);
								newMeterNoValue.setVisibility(View.VISIBLE);
								newMeterNoValue.setText(updateMeterNo);
								updateCustomerBO.setNewMeterNO(updateMeterNo);
							}
						}
					}
				});

				updateMeterNoDialog.show();
			}

		});

		customerNameButton.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{

				CreateDialogUpdateData(custNameType);
			}

		});
		flatNumberButton.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{

				CreateDialogUpdateData(flatType);
			}

		});
		floorNumberButton.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{

				CreateDialogUpdateData(floorType);
			}

		});
		plotNumberButton.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{

				CreateDialogUpdateData(plotType);
			}

		});
		wingButton.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{

				CreateDialogUpdateData(wingType);
			}

		});
		buildnameButton.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{

				CreateDialogUpdateData(buildNameType);
			}

		});
		
		cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				
			}
		});
		
		updateButton = (Button) findViewById(R.id.updateButton);
		updateButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{

				new ApplicationAsk(UpdateCustomerSummaryActivity.this, new ApplicationService()
				{

					@Override
					public void postExecute()
					{
						AlertDialog.Builder updateDialog = new AlertDialog.Builder(UpdateCustomerSummaryActivity.this);
						updateDialog.setCancelable(false);
						updateDialog.setTitle("Update Customer Details");
						updateDialog.setMessage(response.getMessage());
						updateDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
								Intent broadcastIntent = new Intent();
								broadcastIntent.setAction("com.mobicule.ACTION_MY_BOOK_WALK");
								sendBroadcast(broadcastIntent);
								MobiculeLogger.verbose("update flag is *****    " + Constants.searchUpdate);
								if (Constants.searchUpdate == true)
								{
									startActivity(new Intent(UpdateCustomerSummaryActivity.this, MyBookWalk.class));
								}
								else if (Constants.COMPLTED_BOOKWALK == true)
								{
									startActivity(new Intent(UpdateCustomerSummaryActivity.this, CustomerInformation.class));
									Constants.COMPLTED_BOOKWALK = false;
								}
								else
								{
									Intent intent = new Intent(UpdateCustomerSummaryActivity.this, MeterReading.class);
									intent.putExtra("selectedCustomerJSON", getUpdateCustomerJson);
									startActivity(intent);
									//startActivity(new Intent(UpdateCustomerSummaryActivity.this, MeterReading.class));
								}
							}
						});
						updateDialog.show();
					}

					@Override
					public void execute()
					{
						updateCustomerBO.setRemark(editTextRemark.getText().toString().trim());
						response = updateCustomerFacade.saveCustomerUpdate(false);
					}
				}).execute();
			
			}
		});
		
	}

	private void CreateDialogUpdateData(final String updateDataType)
	{

		final Dialog updateCustomerDataDialog = new Dialog(context);
		updateCustomerDataDialog.setContentView(R.layout.update_customer_data);

		final EditText updateData = (EditText) updateCustomerDataDialog.findViewById(R.id.updatedata);
		final TextView update_TextView = (TextView) updateCustomerDataDialog.findViewById(R.id.update_TextView);

		update_TextView.setText("Enter New " + updateDataType);
		updateCustomerDataDialog.setTitle("Update " + updateDataType);
		Button okButton = (Button) updateCustomerDataDialog.findViewById(R.id.ok);

		okButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String updateCustomerData = updateData.getText().toString().trim();
				{
					if (updateCustomerData.equals(""))
					{
						Toast.makeText(UpdateCustomerSummaryActivity.this, "Enter " + updateDataType, Toast.LENGTH_LONG)
								.show();
					}
					else
					{
						updateCustomerDataDialog.dismiss();
						if (custNameType.equalsIgnoreCase(updateDataType))
						{
							newCustomerNameText.setVisibility(View.VISIBLE);
							newCustomerNameValue.setVisibility(View.VISIBLE);
							newCustomerNameValue.setText(updateCustomerData);
							updateCustomerBO.setNewCustomerName(updateCustomerData);
						}
						else if (flatType.equalsIgnoreCase(updateDataType))
						{
							newFlatText.setVisibility(View.VISIBLE);
							newFlatValue.setVisibility(View.VISIBLE);
							newFlatValue.setText(updateCustomerData);
							updateCustomerBO.setNewFlatNumber(updateCustomerData);
						}
						else if (floorType.equalsIgnoreCase(updateDataType))
						{
							newFloorText.setVisibility(View.VISIBLE);
							newFloorValue.setVisibility(View.VISIBLE);
							newFloorValue.setText(updateCustomerData);
							updateCustomerBO.setNewFloor(updateCustomerData);
						}

						else if (plotType.equalsIgnoreCase(updateDataType))
						{
							newPlotText.setVisibility(View.VISIBLE);
							newPlotValue.setVisibility(View.VISIBLE);
							newPlotValue.setText(updateCustomerData);
							updateCustomerBO.setNewPlot(updateCustomerData);
						}
						else if (wingType.equalsIgnoreCase(updateDataType))
						{
							newWingText.setVisibility(View.VISIBLE);
							newWingValue.setVisibility(View.VISIBLE);
							newWingValue.setText(updateCustomerData);
							updateCustomerBO.setNewWing(updateCustomerData);
						}

						else if (buildNameType.equalsIgnoreCase(updateDataType))
						{
							newBuildingNameText.setVisibility(View.VISIBLE);
							newBuildingNameValue.setVisibility(View.VISIBLE);
							newBuildingNameValue.setText(updateCustomerData);
							updateCustomerBO.setNewBuildName(updateCustomerData);
						}

					}
				}
			}
		});

		updateCustomerDataDialog.show();
	}

	public void initalization()
	{
		contactNoMobileButton = (Button) findViewById(R.id.contactNoMobileButton);
		contactNoHomeButton = (Button) findViewById(R.id.contactNoHomeButton);
		contactNoOfficeButton = (Button) findViewById(R.id.contactNoOfficeButton);
		meterNoButton = (Button) findViewById(R.id.meterNumberButton);
		customerNameButton = (Button) findViewById(R.id.customerNameButton);
		flatNumberButton = (Button) findViewById(R.id.flatNumberButton);
		floorNumberButton = (Button) findViewById(R.id.floorNumberButton);
		plotNumberButton = (Button) findViewById(R.id.plotNumberButton);
		wingButton = (Button) findViewById(R.id.wingNumberButton);
		buildnameButton = (Button) findViewById(R.id.buildNameButton);

		customerName = (TextView) findViewById(R.id.customerName);
		currentContactNoMobile = (TextView) findViewById(R.id.contactMobileNo);
		currentContactNoHome = (TextView) findViewById(R.id.contactHomeNo);
		currentContactNoOffice = (TextView) findViewById(R.id.contactOfficeNo);
		currentMeterNo = (TextView) findViewById(R.id.meterNo);
		bpNo = (TextView) findViewById(R.id.bpNumber);
		mroNo = (TextView) findViewById(R.id.mroNumber);

		newContactNoMobileText = (TextView) findViewById(R.id.newContactNoMobile_TextView);
		newContactNoMobileValue = (TextView) findViewById(R.id.newContactNoMobile);

		newContactNoHomeText = (TextView) findViewById(R.id.newContactNoHome_TextView);
		newContactNoHomeValue = (TextView) findViewById(R.id.newContactNoHome);

		newContactNoOfficeText = (TextView) findViewById(R.id.newContactNoOffice_TextView);
		newContactNoOfficeValue = (TextView) findViewById(R.id.newContactNoOffice);

		newMeterNoText = (TextView) findViewById(R.id.newMeterNumber_TextView);
		newMeterNoValue = (TextView) findViewById(R.id.newMeterNo);

		newCustomerNameText = (TextView) findViewById(R.id.newCustomerName_TextView);
		newCustomerNameValue = (TextView) findViewById(R.id.newCustomerName);

		newFlatText = (TextView) findViewById(R.id.newFlatNumber_TextView);
		newFlatValue = (TextView) findViewById(R.id.newFlatNo);

		newFloorText = (TextView) findViewById(R.id.newFloorNumber_TextView);
		newFloorValue = (TextView) findViewById(R.id.newFloorNo);

		newWingText = (TextView) findViewById(R.id.newWingNumber_TextView);
		newWingValue = (TextView) findViewById(R.id.newWingNo);

		newPlotText = (TextView) findViewById(R.id.newPlotNumber_TextView);
		newPlotValue = (TextView) findViewById(R.id.newPlotNo);

		newBuildingNameText = (TextView) findViewById(R.id.newBuildName_TextView);
		newBuildingNameValue = (TextView) findViewById(R.id.newBuildName);

		flatNo = (TextView) findViewById(R.id.flatNo);
		floorNo = (TextView) findViewById(R.id.floorNo);
		buildName = (TextView) findViewById(R.id.buildName);
		plotNo = (TextView) findViewById(R.id.plotNo);
		wingNo = (TextView) findViewById(R.id.wingNo);

		editTextRemark = (EditText) findViewById(R.id.custUpdate_EditTextRemark);

	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add("Update");
		menu.add("Cancel");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getTitle().equals("Update"))
		{
			new ApplicationAsk(UpdateCustomerSummaryActivity.this, new ApplicationService()
			{

				@Override
				public void postExecute()
				{
					AlertDialog.Builder updateDialog = new AlertDialog.Builder(UpdateCustomerSummaryActivity.this);
					updateDialog.setCancelable(false);
					updateDialog.setTitle("Update Customer Details");
					updateDialog.setMessage(response.getMessage());
					updateDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
							Intent broadcastIntent = new Intent();
							broadcastIntent.setAction("com.mobicule.ACTION_MY_BOOK_WALK");
							sendBroadcast(broadcastIntent);
							if (Constants.searchUpdate == true)
							{
								startActivity(new Intent(UpdateCustomerSummaryActivity.this, MyBookWalk.class));
							}
							else if (Constants.COMPLTED_BOOKWALK == true)
							{
								startActivity(new Intent(UpdateCustomerSummaryActivity.this, CustomerInformation.class));
								Constants.COMPLTED_BOOKWALK = false;
							}
							else
							{
								Intent intent = new Intent(UpdateCustomerSummaryActivity.this, MeterReading.class);
								intent.putExtra("selectedCustomerJSON", getUpdateCustomerJson);
								startActivity(intent);
								//startActivity(new Intent(UpdateCustomerSummaryActivity.this, MeterReading.class));
							}
						}
					});
					updateDialog.show();
				}

				@Override
				public void execute()
				{
					updateCustomerBO.setRemark(editTextRemark.getText().toString().trim());
					response = updateCustomerFacade.saveCustomerUpdate(false);
				}
			}).execute();
		}
		else if (item.getTitle().equals("Cancel"))
		{
			finish();

		}
		return super.onOptionsItemSelected(item);
	}*/
}
