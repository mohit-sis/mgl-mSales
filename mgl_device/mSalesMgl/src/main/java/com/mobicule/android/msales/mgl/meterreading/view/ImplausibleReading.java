package com.mobicule.android.msales.mgl.meterreading.view;

import java.io.File;
import java.security.spec.MGF1ParameterSpec;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.android.msales.mgl.util.CustomSpinnerAdapter;

public class ImplausibleReading extends DefaultMeterReadingActivity implements OnClickListener
{

	private final String TAG = "MeterReading";

	private String selectedCustomerJson;

	private Spinner messageFromReaderSpinner, codeSpinner;

	private RadioButton rb_serailcorrectY, rb_serailvisibleY, rb_premiseY, rb_serailcorrectN, rb_serailvisibleN,
			rb_premiseN;

	private LinearLayout ll_serialvisible, ll_serialcorrect, ll_premise, ll_reEnter, ll_messageFromReader, ll_code,
			ll_remarks, ll_LPGSupplier, ll_lastMeterRedingLayout, ll_hinditextmessage, ll_hinditextcode,
			ll_radioButtonsLayoutLPG;

	private ImageView premiseYes, premiseNo, serialCorrectyes, serialCorrectno, serailVisibleyes, serailVisibleno, HPL,
			Indane, Bharat, Customer;

	Boolean ispremiseYes, ispremiseNo, isserialCorrectyes, isserialCorrectno, isserailVisibleyes, isserailVisibleno,
			isHPLselected, isBharatSelected, isIndaneSelected, isCustomerSelected;

	private TextView tv_lastMeterReadingDate;

	private RadioButton rb_HPL, rb_Indane, rb_Bharat, rb_Customer;

	private String[] codesAndReasonsList, codeList, messageFromReadersList;

	private MeterReaderMsg meterReaderMsg;

	private RadioGroup radiogrp;

	private Button nextButton, cancelButton;

	private EditText ed_remarks, ed_reEnter;

	String remarksvalue, reEnterValue;

	private String activityFromIntent, codeandreasonlist, codelist;

	private String concateArray[];

	private String isSerialNumberVisible = "", isSerialNumberCorrect = "", isPremiseCorrect = "";

	String LPGSupplier;

	private CheckBox final_attempt;

	private String implausibleType;

	String[] msgListImplausibleHigh = null;

	String[] msgListImplausibleLow = null;

	String[] msgListMeterNormal = null;

	@Override
	protected void onStart()
	{
		super.onStart();
		Constants.broadcastDialogContext = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
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

		setContentView(R.layout.implausibereading);

		initializeFields();

		showLayout();

		meterReaderMsg = new MeterReaderMsg();

		activityFromIntent = getIntent().getStringExtra("activity");

		MobiculeLogger.verbose("activityFromIntent - " + activityFromIntent);

		codesAndReasonsList = getIntent().getStringArrayExtra("CODE_AND_REASONS_LIST");

		codeList = getIntent().getStringArrayExtra("CODESLIST");

		String extraStr_customerSummary = "selectedCustomerJSON";

		if (getIntent().hasExtra("implausibleType"))
		{
			implausibleType = getIntent().getStringExtra("implausibleType");
		}

		Bundle bundle = getIntent().getExtras();

		/*setValidations();*/

		if (getIntent().hasExtra(extraStr_customerSummary))
		{
			selectedCustomerJson = bundle.getString(extraStr_customerSummary);
		}

		Intent intent = getIntent();
		Boolean value = intent.getBooleanExtra("ZEROCONSUMPTION", false);

		int allReasonsListLength = MeterReadingNotesListsConstants.allReasonsList.length;

		concateArray = new String[allReasonsListLength];

		for (int i = 0; i < concateArray.length; i++)
		{
			concateArray[i] = MeterReadingNotesListsConstants.allReasonsList[i] + " "
					+ MeterReadingNotesListsConstants.allCodesList[i];

		}

		MobiculeLogger.verbose("allReasonsListLength - " + allReasonsListLength);
		MobiculeLogger.verbose("concateArray length - " + concateArray.length);

		codeSpinner.setAdapter(new CustomSpinnerAdapter(ImplausibleReading.this, R.layout.customspinner,
				getListOfMRCodes(MeterReadingNotesListsConstants.meternormalreason,
						MeterReadingNotesListsConstants.meternormalcode), 1));

		tv_lastMeterReadingDate.setText(meterReadingBO.getLastMeterReadingDate());
		//meterReadingBO.setMruCode("99");

		/*if (rb_serailcorrectY.isChecked() && rb_serailvisibleY.isChecked() && rb_premiseY.isChecked())
		{
			ll_remarks.setVisibility(View.VISIBLE);
			ll_reEnter.setVisibility(View.VISIBLE);
			ll_code.setVisibility(View.VISIBLE);
			messageFromReaderSpinner.setVisibility(View.VISIBLE);
			//ll_LPGSupplier.setVisibility(View.VISIBLE);
			messageFromReaderSpinner.setAdapter(new CustomSpinnerAdapter(ImplausibleReading.this, R.layout.customspinner,
					MeterReaderMsg.msglist_106_implausibleYYY, 1));
		
		}
		else if (rb_serailvisibleY.isChecked() && rb_serailcorrectN.isChecked() && rb_premiseY.isChecked())
		{
			ll_remarks.setVisibility(View.VISIBLE);
			ll_reEnter.setVisibility(View.VISIBLE);
			ll_code.setVisibility(View.VISIBLE);
			messageFromReaderSpinner.setVisibility(View.VISIBLE);
		//	ll_LPGSupplier.setVisibility(View.VISIBLE);
			messageFromReaderSpinner.setAdapter(new CustomSpinnerAdapter(ImplausibleReading.this, R.layout.customspinner,
					MeterReaderMsg.msglist_105_implausibleYNY, 1));
		}

		else
		{
			ll_remarks.setVisibility(View.GONE);
			ll_reEnter.setVisibility(View.GONE);
			ll_code.setVisibility(View.VISIBLE);
			messageFromReaderSpinner.setVisibility(View.GONE);
			ll_LPGSupplier.setVisibility(View.GONE);
		}
		*/
		messageFromReaderSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3)
			{
				String selectedMessageItem = (String) parent.getItemAtPosition(arg2);
				MobiculeLogger.verbose("Message Selected" + selectedMessageItem);
				LPGSupplier = "";
				resetLPGSupplierRadioButton();
				meterReadingBO.setMsgRemarksPrimary(selectedMessageItem);
				if (selectedMessageItem.equalsIgnoreCase("Recently stopped using PNG.Now using LPG"))
				{
					MobiculeLogger.verbose("MsgFrmMR clicked : Now using LPG - " + selectedMessageItem);
					ll_radioButtonsLayoutLPG.setVisibility(View.VISIBLE);
				}
				else
				{
					MobiculeLogger.verbose("MsgFrmMR clicked : !Now using LPG " + selectedMessageItem);
					ll_radioButtonsLayoutLPG.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				MobiculeLogger.verbose("OnNothingSelected");
			}
		});

	}

	private String[] getListOfMRCodes(String[] reasonsList, String[] codesList)
	{

		String[] concatResonsAndCodes = new String[reasonsList.length];
		for (int i = 0; i < concatResonsAndCodes.length; i++)
		{
			concatResonsAndCodes[i] = reasonsList[i] + " " + codesList[i];
		}

		return concatResonsAndCodes;
	}

	private void showLayout()
	{
		/*if (rb_serailvisibleY.isChecked() && rb_serailcorrectY.isChecked() && rb_premiseY.isChecked())*/
		if (isserailVisibleyes && isserialCorrectyes && ispremiseYes)
		{
			ll_remarks.setVisibility(View.VISIBLE);
			ll_reEnter.setVisibility(View.VISIBLE);
			ll_code.setVisibility(View.VISIBLE);
			ll_messageFromReader.setVisibility(View.VISIBLE);
			ll_lastMeterRedingLayout.setVisibility(View.VISIBLE);

			if (implausibleType.equalsIgnoreCase("HighConsumption"))
			{
				Constants.KEY_METER_READING_TYPE = Constants.KEY_RE_ENTER_METER_READING;
				messageFromReaderSpinner.setAdapter(new CustomSpinnerAdapter(ImplausibleReading.this,
						R.layout.customspinner, MeterReaderMsg.msglist_106_implausibleYYY, 1));
			}
			else if (implausibleType.equalsIgnoreCase("LowConsumption"))
			{
				Constants.KEY_METER_READING_TYPE = Constants.KEY_RE_ENTER_METER_READING;
				messageFromReaderSpinner.setAdapter(new CustomSpinnerAdapter(ImplausibleReading.this,
						R.layout.customspinner, MeterReaderMsg.msglist_105_implausibleYNY, 1));
			}
			else if (implausibleType.equalsIgnoreCase("NegativeConsumption"))
			{
				Constants.KEY_METER_READING_TYPE = Constants.KEY_RE_ENTER_METER_READING;
				messageFromReaderSpinner.setAdapter(new CustomSpinnerAdapter(ImplausibleReading.this,
						R.layout.customspinner, MeterReaderMsg.msglist_105_implausibleYNY, 1));
			}

		}

		/*if (rb_serailvisibleY.isChecked() && rb_serailcorrectN.isChecked() && rb_premiseY.isChecked())*/
		if (isserailVisibleyes && isserialCorrectno && ispremiseYes)
		{
			ll_remarks.setVisibility(View.VISIBLE);
			ll_reEnter.setVisibility(View.VISIBLE);
			ll_code.setVisibility(View.VISIBLE);
			ll_messageFromReader.setVisibility(View.VISIBLE);
			ll_lastMeterRedingLayout.setVisibility(View.VISIBLE);

			if (implausibleType.equalsIgnoreCase("HighConsumption"))
			{
				Constants.KEY_METER_READING_TYPE = Constants.KEY_RE_ENTER_METER_READING;
				messageFromReaderSpinner.setAdapter(new CustomSpinnerAdapter(ImplausibleReading.this,
						R.layout.customspinner, MeterReaderMsg.msglist_106_implausibleYYY, 1));
			}
			else if (implausibleType.equalsIgnoreCase("LowConsumption"))
			{
				Constants.KEY_METER_READING_TYPE = Constants.KEY_RE_ENTER_METER_READING;
				messageFromReaderSpinner.setAdapter(new CustomSpinnerAdapter(ImplausibleReading.this,
						R.layout.customspinner, MeterReaderMsg.msglist_105_implausibleYNY, 1));
			}
			else if (implausibleType.equalsIgnoreCase("NegativeConsumption"))
			{
				Constants.KEY_METER_READING_TYPE = Constants.KEY_RE_ENTER_METER_READING;
				messageFromReaderSpinner.setAdapter(new CustomSpinnerAdapter(ImplausibleReading.this,
						R.layout.customspinner, MeterReaderMsg.msglist_105_implausibleYNY, 1));
			}

		}

		/*if (rb_serailvisibleN.isChecked())*/
		if(isserailVisibleyes)
		{
			ll_serialcorrect.setVisibility(View.VISIBLE);
		}
		
		if (isserailVisibleno)
		{
			ll_serialcorrect.setVisibility(View.GONE);
		}

		/*if (rb_serailvisibleN.isChecked() && rb_premiseY.isChecked())*/
		if (isserailVisibleno && ispremiseYes)
		{
			ll_remarks.setVisibility(View.VISIBLE);
			ll_reEnter.setVisibility(View.VISIBLE);
			ll_code.setVisibility(View.VISIBLE);
			ll_messageFromReader.setVisibility(View.VISIBLE);
			ll_lastMeterRedingLayout.setVisibility(View.VISIBLE);

			if (implausibleType.equalsIgnoreCase("HighConsumption"))
			{
				Constants.KEY_METER_READING_TYPE = Constants.KEY_RE_ENTER_METER_READING;
				messageFromReaderSpinner.setAdapter(new CustomSpinnerAdapter(ImplausibleReading.this,
						R.layout.customspinner, MeterReaderMsg.msglist_106_implausibleYYY, 1));
			}
			else if (implausibleType.equalsIgnoreCase("LowConsumption"))
			{
				Constants.KEY_METER_READING_TYPE = Constants.KEY_RE_ENTER_METER_READING;
				messageFromReaderSpinner.setAdapter(new CustomSpinnerAdapter(ImplausibleReading.this,
						R.layout.customspinner, MeterReaderMsg.msglist_105_implausibleYNY, 1));
			}
			else if (implausibleType.equalsIgnoreCase("NegativeConsumption"))
			{
				Constants.KEY_METER_READING_TYPE = Constants.KEY_RE_ENTER_METER_READING;
				messageFromReaderSpinner.setAdapter(new CustomSpinnerAdapter(ImplausibleReading.this,
						R.layout.customspinner, MeterReaderMsg.msglist_105_implausibleYNY, 1));
			}

		}

		/*if (rb_serailvisibleN.isChecked() && rb_premiseN.isChecked())*/
		if (isserailVisibleno && ispremiseNo)
		{
			ll_remarks.setVisibility(View.GONE);
			ll_reEnter.setVisibility(View.GONE);
			ll_code.setVisibility(View.GONE);
			ll_messageFromReader.setVisibility(View.GONE);

			showDialog();
		}

		/*if (rb_serailvisibleY.isChecked() && rb_serailcorrectY.isChecked() && rb_premiseN.isChecked())*/
		if (isserailVisibleyes && isserialCorrectyes && ispremiseNo)
		{
			ll_remarks.setVisibility(View.GONE);
			ll_reEnter.setVisibility(View.GONE);
			ll_code.setVisibility(View.GONE);
			ll_messageFromReader.setVisibility(View.GONE);
			ll_lastMeterRedingLayout.setVisibility(View.GONE);
			showDialog();
		}
		/*if (rb_serailvisibleY.isChecked() && rb_serailcorrectN.isChecked() && rb_premiseN.isChecked())*/
		if (isserailVisibleyes && isserialCorrectyes && ispremiseNo)
		{
			ll_remarks.setVisibility(View.GONE);
			ll_reEnter.setVisibility(View.GONE);
			ll_code.setVisibility(View.GONE);
			ll_messageFromReader.setVisibility(View.GONE);
			ll_lastMeterRedingLayout.setVisibility(View.GONE);
			showDialog();
		}

	}

	private void showDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(ImplausibleReading.this);
		builder.setCancelable(true);

		builder.setMessage("Go to Correct Premise");
		builder.setInverseBackgroundForced(true);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Intent intent = new Intent(ImplausibleReading.this, BuildingListActivity.class);
				startActivity(intent);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	private void goToPhotoActivity()
	{
		Constants.threePicsCapCnt = 0;

		MobiculeLogger.verbose("goToPhotoActivity");
		MobiculeLogger.verbose("goToPhotoActivity : codesAndReasonsList[0]" + codesAndReasonsList[0]);
		MobiculeLogger.verbose("item is:" + messageFromReaderSpinner.getSelectedItem() + ":" + " concateArray[8]:"
				+ concateArray[8].toString() + ":");

		String messageSelected = messageFromReaderSpinner.getSelectedItem().toString();
		MobiculeLogger.verbose("messageSelected:" + messageSelected);
		if ((codeSpinner.getSelectedItem().equals(codesAndReasonsList[0])))
		{

			if ((messageSelected.equalsIgnoreCase("Recently stopped using PNG,now using electrical appliance"))
					|| (messageSelected.equalsIgnoreCase("Recently stopped using PNG.Now using LPG")))
			{

				Constants.isTwoPicsSelected = true;
				Constants.isOnePicsSelected = false;
			}

			else
			{
				Constants.isOnePicsSelected = true;
				Constants.isTwoPicsSelected = false;
			}

		}

		Intent intent = new Intent(ImplausibleReading.this, PhotoIntentActivity.class);
		intent.putExtra("activity", activityFromIntent);
		intent.putExtra(
				"CODE_AND_REASONS_LIST",
				getListOfMRCodes(MeterReadingNotesListsConstants.meterNormalReasonsList,
						MeterReadingNotesListsConstants.meterNormalCodeList));
		intent.putExtra("CODESLIST", MeterReadingNotesListsConstants.meterNormalCodeList);
		intent.putExtra("ZEROCONSUMPTION", false);
		intent.putExtra("activity", activityFromIntent);
		startActivity(intent);

	}

	/*private boolean setValidations()
	{
		int id = radiogrp.getCheckedRadioButtonId();
		String messageSelected = messageFromReaderSpinner.getSelectedItem().toString();
		if (messageSelected.equals("Select"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Message", Toast.LENGTH_LONG).show();
			return false;
		}

		else if (id == -1)
		{
		
			Toast.makeText(getApplicationContext(), "Please Select LPG Supplier", Toast.LENGTH_LONG).show();
			return false;
		}
		else
		{
			return true;

		}

	}*/

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

	private void initializeFields()
	{

		ispremiseYes = false;
		ispremiseNo = false;
		isserialCorrectyes = false;
		isserialCorrectno = false;
		isserailVisibleyes = false;
		isserailVisibleno = false;

		isHPLselected = false;
		isBharatSelected = false;
		isIndaneSelected = false;
		isCustomerSelected = false;

		/*rb_serailcorrectY = (RadioButton) findViewById(R.id.rb_yesserialcorrect);
		rb_serailcorrectY.setOnClickListener(this);
		rb_serailcorrectN = (RadioButton) findViewById(R.id.rb_noserialcorrect);
		rb_serailcorrectN.setOnClickListener(this);
		rb_serailvisibleY = (RadioButton) findViewById(R.id.rb_yesserialvisible);
		rb_serailvisibleY.setOnClickListener(this);
		rb_serailvisibleN = (RadioButton) findViewById(R.id.rb_noserialvisible);
		rb_serailvisibleN.setOnClickListener(this);
		rb_premiseY = (RadioButton) findViewById(R.id.rb_yespremise);
		rb_premiseY.setOnClickListener(this);
		rb_premiseN = (RadioButton) findViewById(R.id.rb_nopremise);
		rb_premiseN.setOnClickListener(this);*/
		codeSpinner = (Spinner) findViewById(R.id.sp_code);
		messageFromReaderSpinner = (Spinner) findViewById(R.id.sp_msgfromreader);
		ll_reEnter = (LinearLayout) findViewById(R.id.ll_reEnter);
		ll_serialvisible = (LinearLayout) findViewById(R.id.ll_serialvisible);
		ll_serialcorrect = (LinearLayout) findViewById(R.id.ll_serialcorrect);
		ll_premise = (LinearLayout) findViewById(R.id.ll_premise);
		ll_messageFromReader = (LinearLayout) findViewById(R.id.ll_msgfromreader);
		ll_LPGSupplier = (LinearLayout) findViewById(R.id.ll_LPGSupplier);
		ll_code = (LinearLayout) findViewById(R.id.ll_code);
		ll_lastMeterRedingLayout = (LinearLayout) findViewById(R.id.ll_lastMeterRedingLayout);
		ll_remarks = (LinearLayout) findViewById(R.id.ll_remarks);
		ed_remarks = (EditText) findViewById(R.id.ed_remarks);
		ed_reEnter = (EditText) findViewById(R.id.ed_reEnter);
		nextButton = (Button) findViewById(R.id.btn_next);
		final_attempt = (CheckBox) findViewById(R.id.checkbox);
		ll_hinditextmessage = (LinearLayout) findViewById(R.id._hindi_text_message);
		ll_hinditextcode = (LinearLayout) findViewById(R.id.ll_hindi_text_code);
		tv_lastMeterReadingDate = (TextView) findViewById(R.id.tv_lastMeterReadingDate);
		ll_radioButtonsLayoutLPG = (LinearLayout) findViewById(R.id.ll_radioButtonsLayoutLPG);
		premiseNo = (ImageView) findViewById(R.id.imagepremiseno);
		premiseYes = (ImageView) findViewById(R.id.imagepremiseyes);
		serailVisibleno = (ImageView) findViewById(R.id.imagenovisible);
		serailVisibleyes = (ImageView) findViewById(R.id.imageyesvisible);
		serialCorrectno = (ImageView) findViewById(R.id.imagenoserialcorrect);
		serialCorrectyes = (ImageView) findViewById(R.id.imageyesserialcorrect);

		HPL = (ImageView) findViewById(R.id.imageHPL);
		Indane = (ImageView) findViewById(R.id.imageIndane);
		Customer = (ImageView) findViewById(R.id.imageCustomer);
		Bharat = (ImageView) findViewById(R.id.imageBharat);
		/*rb_HPL = (RadioButton) findViewById(R.id.rb_HpLPGSupplier);
		rb_Bharat = (RadioButton) findViewById(R.id.rb_bharatLPGSupplier);
		rb_Indane = (RadioButton) findViewById(R.id.rb_IndaneLPGSupplier);
		rb_Customer = (RadioButton) findViewById(R.id.rb_custLPGSupplier);

		rb_HPL.setOnClickListener(this);
		rb_Bharat.setOnClickListener(this);
		rb_Indane.setOnClickListener(this);
		rb_Customer.setOnClickListener(this);
*/		
		ll_hinditextmessage.setOnClickListener(this);
		ll_hinditextcode.setOnClickListener(this);

		premiseNo.setOnClickListener(this);
		premiseYes.setOnClickListener(this);
		serailVisibleno.setOnClickListener(this);
		serailVisibleyes.setOnClickListener(this);
		serialCorrectno.setOnClickListener(this);
		serialCorrectyes.setOnClickListener(this);

		HPL.setOnClickListener(this);
		Indane.setOnClickListener(this);
		Customer.setOnClickListener(this);
		Bharat.setOnClickListener(this);

		nextButton.setOnClickListener(this);
		cancelButton = (Button) findViewById(R.id.btn_cancel);
		nextButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		//radiogrp = (RadioGroup) findViewById(R.id.rg_LPGSupplier);
		remarksvalue = ed_remarks.getText().toString();
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())

		{
			case R.id.btn_next:
				
				try{
				InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm1.hideSoftInputFromWindow(ed_reEnter.getWindowToken(), 0);
				InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm2.hideSoftInputFromWindow(ed_remarks.getWindowToken(), 0);
				String messageSelected = messageFromReaderSpinner.getSelectedItem().toString();
				remarksvalue = ed_remarks.getText().toString();
				reEnterValue = ed_reEnter.getText().toString();

				if (reEnterValue.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please enter meter reading value", Toast.LENGTH_LONG)
							.show();

				}
				else if (reEnterValue.length() < 3)
				{
					Toast.makeText(getApplicationContext(), "Please enter atleast 3 digit Meter Reading",
							Toast.LENGTH_LONG).show();

				}
				else if (ll_radioButtonsLayoutLPG.getVisibility() == View.VISIBLE && !isHPLselected
						&& !isIndaneSelected && !isBharatSelected && !isCustomerSelected)
				/*(ll_radioButtonsLayoutLPG.getVisibility() == View.VISIBLE && !rb_Bharat.isChecked()
					&& !rb_Customer.isChecked() && !rb_Indane.isChecked() && !rb_HPL.isChecked())*/
				{
					Toast.makeText(getApplicationContext(), "Please select LPG Supplier", Toast.LENGTH_LONG)
							.show();

				}

				else if (messageSelected.equals("Select"))
				{
					Toast.makeText(getApplicationContext(), "Please Select Message", Toast.LENGTH_LONG).show();

				}
				else
				{

					MobiculeLogger.verbose("Message in remarks field - " + remarksvalue);
					MobiculeLogger.verbose("Message in reEnterValue field - " + reEnterValue);
					MobiculeLogger.verbose("In Remarks Field" + meterReadingBO.toJSON().toString());
					
					meterReadingBO.setIsLPGSupplier(LPGSupplier);
					meterReadingBO.setIsReEnterMeterReading(reEnterValue);
					meterReadingBO.setMsgRemarks(remarksvalue);
					meterReadingBO.setMsgRemarksPrimary(messageSelected);
					meterReadingBO.setIsSerailCorrect(isSerialNumberCorrect);
					meterReadingBO.setIsSerailVisible(isSerialNumberVisible);
					meterReadingBO.setIsPremiselCorrect(isPremiseCorrect);
					goToPhotoActivity();
				}

				/*if (setValidations())
				{
					goToPhotoActivity();
				}*/
				}
				catch(Exception e)
				{
					MobiculeLogger.verbose("" + e.toString());
				}

				break;

			case R.id.imagenovisible:

				serailVisibleno.setImageDrawable(this.getResources()
						.getDrawable(R.drawable.selected_radio_button_40x40));
				serailVisibleyes.setImageDrawable(this.getResources().getDrawable(
						R.drawable.unselected_radio_button_40x40));
				isserailVisibleno = true;
				isserailVisibleyes = false;
				isSerialNumberVisible = "No";
				LPGSupplier = "";
				resetLPGSupplierRadioButton();
				showLayout();
				break;

			case R.id.imageyesvisible:

				serailVisibleno.setImageDrawable(this.getResources().getDrawable(
						R.drawable.unselected_radio_button_40x40));
				serailVisibleyes.setImageDrawable(this.getResources().getDrawable(
						R.drawable.selected_radio_button_40x40));
				isserailVisibleno = false;
				isserailVisibleyes = true;
				isSerialNumberVisible = "Yes";

				LPGSupplier = "";
				resetLPGSupplierRadioButton();
				showLayout();
				break;

			case R.id.imageyesserialcorrect:

				serialCorrectyes.setImageDrawable(this.getResources().getDrawable(
						R.drawable.selected_radio_button_40x40));
				serialCorrectno.setImageDrawable(this.getResources().getDrawable(
						R.drawable.unselected_radio_button_40x40));
				isserialCorrectno = false;
				isserialCorrectyes = true;

				isSerialNumberCorrect = "Yes";
				
				//meterReadingBO.setIsSerailCorrect(isSerialNumberCorrect);
				
				LPGSupplier = "";
				resetLPGSupplierRadioButton();
				showLayout();

				break;

			case R.id.imagenoserialcorrect:

				serialCorrectyes.setImageDrawable(this.getResources().getDrawable(
						R.drawable.unselected_radio_button_40x40));
				serialCorrectno.setImageDrawable(this.getResources()
						.getDrawable(R.drawable.selected_radio_button_40x40));
				isserialCorrectno = true;
				isserialCorrectyes = false;

				isSerialNumberCorrect = "No";
				LPGSupplier = "";
				resetLPGSupplierRadioButton();
				showLayout();

				break;

			case R.id.imagepremiseno:

				premiseNo.setImageDrawable(this.getResources().getDrawable(R.drawable.selected_radio_button_40x40));
				premiseYes.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				ispremiseNo = true;
				ispremiseYes = false;

				isPremiseCorrect = "No";
				LPGSupplier = "";
				resetLPGSupplierRadioButton();
				showLayout();

				break;

			case R.id.imagepremiseyes:
				premiseNo.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				premiseYes.setImageDrawable(this.getResources().getDrawable(R.drawable.selected_radio_button_40x40));
				ispremiseNo = false;
				ispremiseYes = true;

				isPremiseCorrect = "Yes";
				LPGSupplier = "";
				resetLPGSupplierRadioButton();
				showLayout();

				break;

			/*case R.id.rb_yesserialcorrect:

				isSerialNumberCorrect = "Yes";
				//meterReadingBO.setIsSerailCorrect(isSerialNumberCorrect);
				LPGSupplier = "";
				showLayout();

				break;

			case R.id.rb_noserialcorrect:

				isSerialNumberCorrect = "No";
				LPGSupplier = "";
				showLayout();

				break;

			case R.id.rb_yesserialvisible:

				isSerialNumberVisible = "Yes";

				LPGSupplier = "";
				showLayout();

				break;
			case R.id.rb_noserialvisible:

				isSerialNumberVisible = "No";
				LPGSupplier = "";
				showLayout();

				break;

			case R.id.rb_yespremise:

				isPremiseCorrect = "Yes";
				LPGSupplier = "";
				showLayout();

				break;

			case R.id.rb_nopremise:

				isPremiseCorrect = "No";
				LPGSupplier = "";
				showLayout();

				break;*/

			case R.id.btn_cancel:
				finish();
				break;
			/*
						case R.id.rb_bharatLPGSupplier:
							LPGSupplier = "Bharat";

							break;
						case R.id.rb_IndaneLPGSupplier:
							LPGSupplier = "Indane";

							break;
						case R.id.rb_HpLPGSupplier:
							LPGSupplier = "HPL";

							break;
						case R.id.rb_custLPGSupplier:
							LPGSupplier = "Customer did not share detail";
							//meterReadingBO.setIsLPGSupplier(LPGSupplier);
							break;*/

			case R.id._hindi_text_message:

				String[] codesForMessageList = {};
				msgListImplausibleHigh = MeterReaderMsg.msglist_106_implausibleYYY;
				msgListImplausibleLow = MeterReaderMsg.msglist_105_implausibleYNY;
				if (implausibleType.equalsIgnoreCase("HighConsumption"))
				{
					showCustomAlertDialog(codesForMessageList,
							MeterReadingNotesListsConstants.getImplausibleHigh(getApplicationContext()),
							msgListImplausibleHigh, "Message From Reader");
				}
				else
				{
					showCustomAlertDialog(codesForMessageList,
							MeterReadingNotesListsConstants.getImplausibleLow(getApplicationContext()),
							msgListImplausibleLow, "Message From Reader");
				}

				break;

			case R.id.ll_hindi_text_code:

				String[] codesForMessageList1 = {};
				String selectedItem = (String) codeSpinner.getSelectedItem();

				if (selectedItem.equalsIgnoreCase("Meter Normal 99"))
				{
					showCustomAlertDialog(MeterReadingNotesListsConstants.meternormalcode,
							MeterReadingNotesListsConstants.getMeterNormalcodeImplausible(getApplicationContext()),
							MeterReadingNotesListsConstants.meternormalreason, "Code");
				}
				break;

			case R.id.imageBharat:
				LPGSupplier = "Bharat";
				Bharat.setImageDrawable(this.getResources().getDrawable(R.drawable.selected_radio_button_40x40));
				HPL.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				Customer.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				Indane.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));

				isBharatSelected = true;
				isIndaneSelected = false;
				isCustomerSelected = false;
				isHPLselected = false;
				break;
			case R.id.imageIndane:
				LPGSupplier = "Indane";

				Indane.setImageDrawable(this.getResources().getDrawable(R.drawable.selected_radio_button_40x40));
				Bharat.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				HPL.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				Customer.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));

				isBharatSelected = false;
				isIndaneSelected = true;
				isCustomerSelected = false;
				isHPLselected = false;
				break;
			case R.id.imageHPL:
				LPGSupplier = "HP";
				Indane.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				Bharat.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				HPL.setImageDrawable(this.getResources().getDrawable(R.drawable.selected_radio_button_40x40));
				Customer.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));

				isBharatSelected = false;
				isIndaneSelected = false;
				isCustomerSelected = false;
				isHPLselected = true;
				break;
			case R.id.imageCustomer:
				LPGSupplier = "Customer did not share details";
				Indane.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				Bharat.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				HPL.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				Customer.setImageDrawable(this.getResources().getDrawable(R.drawable.selected_radio_button_40x40));

				isBharatSelected = false;
				isIndaneSelected = false;
				isCustomerSelected = true;
				isHPLselected = false;
				break;

			default:
				break;
		}
	}

	private void resetLPGSupplierRadioButton()
	{
		Indane.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
		Bharat.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
		HPL.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
		Customer.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
		
		isBharatSelected = false;
		isIndaneSelected = false;
		isCustomerSelected = false;
		isHPLselected = false;
	}

}
