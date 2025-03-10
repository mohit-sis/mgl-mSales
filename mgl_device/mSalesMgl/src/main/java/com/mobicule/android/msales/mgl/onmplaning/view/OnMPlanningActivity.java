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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.meterreading.view.CustomListAdapter;
import com.mobicule.android.msales.mgl.meterreading.view.DefaultMeterReadingActivity;
import com.mobicule.android.msales.mgl.meterreading.view.MeterReading;
import com.mobicule.android.msales.mgl.meterreading.view.MeterReadingFeild;
import com.mobicule.android.msales.mgl.meterreading.view.MeterReadingNotesListsConstants;
import com.mobicule.android.msales.mgl.meterreading.view.Summary;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.android.msales.mgl.util.CustomSpinnerAdapter;
import com.mobicule.android.msales.mgl.util.FileUtil;

/**
* 
* <enter description here>
*
* @author namrata <enter lastname>
* @see 
*
* @createdOn 19-Apr-2017
* @modifiedOn
*
* @copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class OnMPlanningActivity extends DefaultOnMPlanningActivity implements OnClickListener
{

	Spinner sp_noOfBurners, sp_noOfGeysers, sp_msgFrmMeterReader;

	RadioGroup rg_isHoseAvailable;

	RadioButton rb_yes, rb_no;

	ImageView YesMeterHose, NoMeterHose;

	Boolean isYesMeterHose, isNoMeterHose;

	LinearLayout ll_meterHoselayout,ll_hinditextmessage;

	Button b_next, b_cancel;

	EditText et_remarks;

	List<String> noOfBurnersList = new ArrayList<String>();

	List<String> noOfGeysersList = new ArrayList<String>();

	List<String> msgFrmMeterReaderList = new ArrayList<String>();

	String strBurnerCount, strGeyserCount, strMeterReader, strHoseAvailable;
	String[] OnMSpinnerHinditext = null;
	

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

		setContentView(R.layout.activity_onmplanning_reading);

		initializeComponents();
		fillSpinnerData();
		spinnerClickEvent();

		FileUtil.writeToFile("//OnMPlanningActivity : meterReadingBO :: " + meterReadingBO.toJSON().toString());

	}

	private void initializeComponents()
	{

		isYesMeterHose = false;
		isNoMeterHose = false;

		YesMeterHose = (ImageView) findViewById(R.id.imageyesmeterHose);
		NoMeterHose = (ImageView) findViewById(R.id.imagenometerHose);

		YesMeterHose.setOnClickListener(this);
		NoMeterHose.setOnClickListener(this);
		/*rg_isHoseAvailable = (RadioGroup) findViewById(R.id.rg_meterHose);*/

		/*rb_yes = (RadioButton) findViewById(R.id.rb_yes);
		rb_yes.setOnClickListener(this);
		
		rb_no = (RadioButton) findViewById(R.id.rb_no);
		rb_no.setOnClickListener(this);*/

		b_next = (Button) findViewById(R.id.btn_next);
		b_next.setOnClickListener(this);

		//b_cancel = (Button) findViewById(R.id.btn_cancel);
		//b_cancel.setOnClickListener(this);

		sp_noOfBurners = (Spinner) findViewById(R.id.sp_noBurners);
		sp_noOfGeysers = (Spinner) findViewById(R.id.sp_noOfGeysers);
		sp_msgFrmMeterReader = (Spinner) findViewById(R.id.sp_meter_message);

		et_remarks = (EditText) findViewById(R.id.et_remarks);

		ll_meterHoselayout = (LinearLayout) findViewById(R.id.ll_meterHoseLayout);
		
		ll_hinditextmessage = (LinearLayout) findViewById(R.id.ll_hindi_text_message);
		
		ll_hinditextmessage.setOnClickListener(this);

	}
	
	private void showCustomAlertDialog(String[] codesList, String[] alertMessageHindiList,
			String[] alertMessageEnglishiList, String title)
	{
		// custom dialog
		final Dialog dialog = new Dialog(this);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);

		//dialog.setTitle(title);

		dialog.setContentView(R.layout.alert_dialog_custom);

		TextView titleText = (TextView) dialog.findViewById(R.id.alertDialogTitleText);

		titleText.setText(title);

		// set the custom dialog components - text, image and button
		ListView dialogList = (ListView) dialog.findViewById(R.id.list);

		CustomListAdapter adapter = new CustomListAdapter(this, R.layout.alert_dialog_list_item, codesList,
				alertMessageHindiList, alertMessageEnglishiList);

		adapter.notifyDataSetChanged();

		dialogList.setAdapter(adapter);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	public void fillSpinnerData()
	{

		sp_noOfBurners.setAdapter(new CustomSpinnerAdapter(OnMPlanningActivity.this, R.layout.customspinner,
				OnMListConstants.noOfBurners, 1));

		sp_noOfGeysers.setAdapter(new CustomSpinnerAdapter(OnMPlanningActivity.this, R.layout.customspinner,
				OnMListConstants.noOfGasGeysers, 1));

		sp_msgFrmMeterReader.setAdapter(new CustomSpinnerAdapter(OnMPlanningActivity.this, R.layout.customspinner,
				OnMListConstants.msgFrmMRForHoseNotAvailable, 1));

		/*ArrayAdapter<String> dataAdapterOne = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, OnMListConstants.noOfBurners);
		//dataAdapterOne.setDropDownViewResource(android.R.layout.simple_spinner_item);
		sp_noOfBurners.setAdapter(dataAdapterOne);

		ArrayAdapter<String> dataAdapterTwo = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, OnMListConstants.noOfGasGeysers);
		//dataAdapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_item);
		sp_noOfGeysers.setAdapter(dataAdapterTwo);

		ArrayAdapter<String> dataAdapterThird = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, OnMListConstants.msgFrmMRForHoseNotAvailable);
		//dataAdapterThird.setDropDownViewResource(android.R.layout.simple_spinner_item);
		sp_msgFrmMeterReader.setAdapter(dataAdapterThird);
		*/
	}

	public void spinnerClickEvent()
	{

		sp_noOfBurners.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				strBurnerCount = parent.getItemAtPosition(position).toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});

		sp_noOfGeysers.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				strGeyserCount = parent.getItemAtPosition(position).toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});

		sp_msgFrmMeterReader.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				strMeterReader = parent.getItemAtPosition(position).toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});

	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub

		switch (v.getId())
		{
			/*case R.id.btn_cancel:
				finish();

				break;*/
			case R.id.btn_next:

				onMPlanningBO.setAddress(meterReadingBO.getCustomerAddress());
				onMPlanningBO.setBPNumber(meterReadingBO.getBpNumber());
				onMPlanningBO.setCANumber(meterReadingBO.getCaNo());
				onMPlanningBO.setConnObj(meterReadingBO.getConnObj());
				onMPlanningBO.setCutsomerName(meterReadingBO.getCustomerName());
				onMPlanningBO.setMeterNo(meterReadingBO.getMeterNumber());
				onMPlanningBO.setMrCode(MeterReadingFeild.mrCode); // ---------------- set mrCode from meter reading BO
				onMPlanningBO.setMroNo(meterReadingBO.getMroNumber());
				onMPlanningBO.setMrReason(meterReadingBO.getMrReason());
				onMPlanningBO.setMrSubmit(meterReadingBO.getMrSubmitted());
				onMPlanningBO.setMsgFrmMr(strMeterReader);
				onMPlanningBO.setMsgRemarks(et_remarks.getText().toString());
				onMPlanningBO.setMsgToMr(meterReadingBO.getMsgToMr());
				onMPlanningBO.setNoOfBurners(strBurnerCount);
				onMPlanningBO.setNoOfGasGeysers(strGeyserCount);
				onMPlanningBO.setStatus(meterReadingBO.getStatus());
				onMPlanningBO.setMruCode(meterReadingBO.getMruCode());
				onMPlanningBO.setIsHoseAvailable(strHoseAvailable);

				/*if (rb_yes.isChecked())*/if (isYesMeterHose)
				{

					Intent intent1 = new Intent(OnMPlanningActivity.this, OnMTakePictureActivity.class);
					intent1.putExtra("strHoseAvailable", strHoseAvailable);
					startActivity(intent1);

				}
				/*else if (rb_no.isChecked())*/else if (isNoMeterHose)
				{

					MobiculeLogger.verbose("//spinner selected item : "
							+ sp_msgFrmMeterReader.getSelectedItem().toString());

					if (sp_msgFrmMeterReader.getSelectedItem().toString().equalsIgnoreCase("Select"))
					{
						Toast.makeText(getApplicationContext(), "Please select Message from meter reader",
								Toast.LENGTH_LONG).show();
					}
					else
					{

						Intent intent = new Intent(OnMPlanningActivity.this, OnMPlanningSummaryActivity.class);
						intent.putExtra("strHoseAvailable", strHoseAvailable);
						startActivity(intent);

					}
					onMPlanningBO.setImage("");
					onMPlanningBO.setImage2("");
					onMPlanningBO.setImage3("");

				}
				else
				{
					Toast.makeText(getApplicationContext(), "Please select MGL Gas Hose Available", Toast.LENGTH_LONG)
							.show();
				}
				break;

			/*case R.id.rb_yes:
				ll_meterHoselayout.setVisibility(View.GONE);
				strHoseAvailable = "Yes";
				strMeterReader = "";
				sp_msgFrmMeterReader.setSelection(0);
				break;

			case R.id.rb_no:
				ll_meterHoselayout.setVisibility(View.VISIBLE);
				strHoseAvailable= "No";
				strMeterReader = "";
				sp_msgFrmMeterReader.setSelection(0);
				break;*/

			case R.id.imageyesmeterHose:
				ll_meterHoselayout.setVisibility(View.GONE);
				strHoseAvailable = "Yes";
				strMeterReader = "";
				sp_msgFrmMeterReader.setSelection(0);
				YesMeterHose.setImageDrawable(this.getResources().getDrawable(R.drawable.selected_radio_button_40x40));
				NoMeterHose.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				isYesMeterHose = true;
				isNoMeterHose = false;

				break;
			case R.id.imagenometerHose:
				ll_meterHoselayout.setVisibility(View.VISIBLE);
				strHoseAvailable = "No";
				strMeterReader = "";
				sp_msgFrmMeterReader.setSelection(0);

				YesMeterHose.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				NoMeterHose.setImageDrawable(this.getResources().getDrawable(R.drawable.selected_radio_button_40x40));

				isYesMeterHose = false;
				isNoMeterHose = true;
				break;
				
			case R.id.ll_hindi_text_message:
				String[] codesForMessageList = {};
				OnMSpinnerHinditext=OnMListConstants.msgFrmMRForHoseNotAvailable;
				
				showCustomAlertDialog(codesForMessageList,
						MeterReadingNotesListsConstants.getOnMHindiText(getApplicationContext()),
						OnMSpinnerHinditext, "Message From Reader");
				break;
				
			default:
				break;
		}

	}

}
