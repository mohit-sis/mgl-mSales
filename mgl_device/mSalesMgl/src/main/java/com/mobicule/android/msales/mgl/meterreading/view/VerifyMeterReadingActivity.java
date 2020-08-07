package com.mobicule.android.msales.mgl.meterreading.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.android.msales.mgl.util.CustomSpinnerAdapter;

import java.io.File;

public class VerifyMeterReadingActivity extends DefaultMeterReadingActivity implements OnClickListener

{
	private Spinner messageFromReaderSpinner, codeSpinner;

	private ImageView yesRadioButton1, noRadioButton1,HPL,Indane,Bharat,Customer;

	private ImageView yesRadioButton2, noRadioButton2;

	private LinearLayout codeAndMessageFromReaderLayout, burnerOperatedLayout, meterCheckingLayout,
			codeInfoImageLayout, messageInfoImageLayout, conditionInfoImageLayout, ll_LPGSupplierLayout,ll_radioButtonsLayoutLPG;

	private RelativeLayout conditionLayout;

	RadioButton rb_HPL, rb_Indane, rb_Bharat, rb_Customer;

	private TextView conditionDescriptionTextView;

	private EditText meterReading_remarksvalue;

	private String[] codesAndReasonsList, codeList, messageFromReadersList;

	private MeterReaderMsg meterReaderMsg;

	private Boolean isYesRadioButton1Selected, isNoRadioButton1Selected, isYesRadioButton2Selected,
			isNoRadioButton2Selected, isHPLselected, isBharatSelected, isIndaneSelected, isCustomerSelected;

	private Button nextButton, cancelButton;

	private String activityFromIntent;

	String LPGsupplier;

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

		setContentView(R.layout.verify_meter_reading);

		initializeFields();

		activityFromIntent = getIntent().getStringExtra("activity");
		Log.d("TAG", "activityFromIntent :: " + activityFromIntent);

		Intent intent = getIntent();
		Boolean value = intent.getBooleanExtra("ZEROCONSUMPTION", false);
	}

	private void initializeFields()
	{
		isYesRadioButton1Selected = false;
		isNoRadioButton1Selected = false;
		isYesRadioButton2Selected = false;
		isNoRadioButton2Selected = false;
		isHPLselected = false;
		isBharatSelected = false;
		isIndaneSelected = false;
		isCustomerSelected = false;
		
		
		codesAndReasonsList = getIntent().getStringArrayExtra("CODE_AND_REASONS_LIST");
		codeList = getIntent().getStringArrayExtra("CODESLIST");

		for (int i = 0; i < codesAndReasonsList.length; i++)
		{
			Log.d("VerifyMeterReadingActivity - initializeFields()",
					"codesAndReasonsList : " + codesAndReasonsList[i].toString() + " codeList : "
							+ codeList[i].toString());
		}

		conditionLayout = (RelativeLayout) findViewById(R.id.conditionLayout);
		codeAndMessageFromReaderLayout = (LinearLayout) findViewById(R.id.codeAndMessageFromReaderLayout);
		meterCheckingLayout = (LinearLayout) findViewById(R.id.meterCheckingLayout);
		burnerOperatedLayout = (LinearLayout) findViewById(R.id.burnerOperatedLayout);
		ll_radioButtonsLayoutLPG=(LinearLayout)findViewById(R.id.ll_radioButtonsLayoutLPG);
		/*rb_HPL = (RadioButton) findViewById(R.id.rb_HpLPGSupplier);
		rb_Bharat = (RadioButton) findViewById(R.id.rb_bharatLPGSupplier);
		rb_Indane = (RadioButton) findViewById(R.id.rb_IndaneLPGSupplier);
		rb_Customer = (RadioButton) findViewById(R.id.rb_custLPGSupplier);*/

		/*rb_HPL.setOnClickListener(this);
		rb_Bharat.setOnClickListener(this);
		rb_Indane.setOnClickListener(this);
		rb_Customer.setOnClickListener(this);
*/
		conditionDescriptionTextView = (TextView) findViewById(R.id.conditionDescriptionTextView);

		meterReading_remarksvalue = (EditText) findViewById(R.id.meterReading_remarksvalue);

		codeSpinner = (Spinner) findViewById(R.id.meterReadingCodeSpinner);

		/*ll_LPGSupplierLayout = (LinearLayout) findViewById(R.id.ll_LPGSupplier);*/

		codeSpinner.setAdapter(new CustomSpinnerAdapter(VerifyMeterReadingActivity.this, R.layout.customspinner,
				codesAndReasonsList, 1));

		codeSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				String selectedItem = codeSpinner.getSelectedItem().toString();

				String spinnerselectedItemCode = selectedItem.replaceAll("[^0-9]", "");

				LPGsupplier = "";

				if (isNoRadioButton1Selected && codeSpinner.getItemAtPosition(position).equals("Not Using Gas 52"))
				{
					messageFromReadersList = MeterReaderMsg.msgListForNotUsingGas;
				}

				/*	else if (isNoRadioButton1Selected && codeSpinner.getItemAtPosition(position).equals("Meter Testing not Possible"))
					{
						messageFromReadersList = MeterReaderMsg.msglist_101_MeterTestingNotPossible;
					}*/
				else

				{
					messageFromReadersList = meterReaderMsg.getMessageList(spinnerselectedItemCode);
				}

				Log.d("VerifyMeterReadingActivity - getLayoutVisibility()",
						"" + meterReaderMsg.getMessageList(spinnerselectedItemCode));

				for (int i = 0; i < messageFromReadersList.length; i++)
				{
					Log.d("VerifyMeterReadingActivity - getLayoutVisibility()", messageFromReadersList[i].toString());
				}

				messageFromReaderSpinner.setAdapter(new CustomSpinnerAdapter(VerifyMeterReadingActivity.this,
						R.layout.customspinner, messageFromReadersList, 1));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}

		});

		if (getIntent().getBooleanExtra("ZEROCONSUMPTION", false))
		{
			burnerOperatedLayout.setVisibility(View.VISIBLE);
			meterCheckingLayout.setVisibility(View.VISIBLE);
		}
		else
		{
			meterCheckingLayout.setVisibility(View.VISIBLE);
			burnerOperatedLayout.setVisibility(View.GONE);

		}

		messageFromReaderSpinner = (Spinner) findViewById(R.id.meterReadingMessageSpinner);
		messageFromReaderSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				String selectedMessageItem = (String) parent.getItemAtPosition(position);
				Log.d("", "Message Selected" + selectedMessageItem);
				LPGsupplier = "";
				resetLPGSupplierRadioButton();
				if (selectedMessageItem.equalsIgnoreCase("Using LPG cylinder,Meter read"))
				{
					ll_radioButtonsLayoutLPG.setVisibility(View.VISIBLE);
				}
				else
				{
					ll_radioButtonsLayoutLPG.setVisibility(View.GONE);
				}
				/*if (selectedMessageItem.equals("Other(Write)"))
				{
					Log.d("", "Show the remarks field");
					meterReading_remarksvalue.setVisibility(View.VISIBLE);
				}
				else
				{
					Log.d("", "Hide the remarks field");
					meterReading_remarksvalue.setVisibility(View.GONE);
				}*/
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});

		yesRadioButton1 = (ImageView) findViewById(R.id.yesRadioButton1);
		noRadioButton1 = (ImageView) findViewById(R.id.noRadioButton1);
		yesRadioButton2 = (ImageView) findViewById(R.id.yesRadioButton2);
		noRadioButton2 = (ImageView) findViewById(R.id.noRadioButton2);
		HPL=(ImageView)findViewById(R.id.imageHPL);
		Indane=(ImageView)findViewById(R.id.imageIndane);
		Customer=(ImageView)findViewById(R.id.imageCustomer);
		Bharat=(ImageView)findViewById(R.id.imageBharat);
		
		
		

		yesRadioButton1.setOnClickListener(this);
		noRadioButton1.setOnClickListener(this);
		yesRadioButton2.setOnClickListener(this);
		noRadioButton2.setOnClickListener(this);
		HPL.setOnClickListener(this);
		Indane.setOnClickListener(this);
		Customer.setOnClickListener(this);
		Bharat.setOnClickListener(this);

		nextButton = (Button) findViewById(R.id.next_button);
		cancelButton = (Button) findViewById(R.id.cancel_button);

		nextButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

		codeInfoImageLayout = (LinearLayout) findViewById(R.id.codeInfoImageLayout);
		messageInfoImageLayout = (LinearLayout) findViewById(R.id.messageInfoImageLayout);
		conditionInfoImageLayout = (LinearLayout) findViewById(R.id.conditionInfoImageLayout);

		codeInfoImageLayout.setOnClickListener(this);
		messageInfoImageLayout.setOnClickListener(this);
		conditionInfoImageLayout.setOnClickListener(this);

	}

	private void showLayout()
	{
		if (isYesRadioButton1Selected && isYesRadioButton2Selected)
		{
			codeSpinner.setAdapter(new CustomSpinnerAdapter(VerifyMeterReadingActivity.this, R.layout.customspinner,
					codesAndReasonsList, 1));

			getLayoutVisibility("PNG not being used. Burner operated, meter checked, OK", 3);
		}
		if (isNoRadioButton1Selected)
		{
			/*codeSpinner.setAdapter(new CustomSpinnerAdapter(VerifyMeterReadingActivity.this, R.layout.customspinner,
					new String[] { "Temporary Disconnection 1", "Not Using Gas 52" }, 1));*/

			if (!getIntent().getBooleanExtra("ZEROCONSUMPTION", false))
			{
				/*codeSpinner.setAdapter(new CustomSpinnerAdapter(VerifyMeterReadingActivity.this,
						R.layout.customspinner, new String[] { "Meter Normal 99" }, 1));
				*/
				getLayoutVisibility("All details verified.", 0);

			}
			else
			{
				codeSpinner.setAdapter(new CustomSpinnerAdapter(VerifyMeterReadingActivity.this,
						R.layout.customspinner, new String[] { "Temporary Disconnection 1", "Not Using Gas 52",
								"Meter Testing not Possible 69" }, 1));
				getLayoutVisibility("PNG not being used. Meter checking not possible.", 0);

			}

		}
		if (isYesRadioButton1Selected && isNoRadioButton2Selected)
		{
			codeSpinner.setAdapter(new CustomSpinnerAdapter(VerifyMeterReadingActivity.this, R.layout.customspinner,
					codesAndReasonsList, 1));

			getLayoutVisibility("Burner Operated, Meter not running, Replace Meter.", 2);
		}

	}

	private String getMrCode()
	{

		if (isNoRadioButton1Selected)
		{

			if (getIntent().getBooleanExtra("ZEROCONSUMPTION", true))
			{
				if (codeSpinner.getSelectedItemPosition() == 0)
				{
					return codeList[1];
				}
				if (codeSpinner.getSelectedItemPosition() == 1)
				{
					return codeList[3];
				}
				if (codeSpinner.getSelectedItemPosition() == 2)
				{
					return codeList[4];
				}
			}
			else
			{

				if (codeSpinner.getSelectedItemPosition() == 0)
				{
					return codeList[0];
				}
			}
		}

		return codeList[codeSpinner.getSelectedItemPosition()];
	}

	private void getLayoutVisibility(String conditionMessage, int position)
	{
		try
		{
			conditionLayout.setVisibility(View.VISIBLE);
			codeAndMessageFromReaderLayout.setVisibility(View.VISIBLE);
			conditionDescriptionTextView.setText(conditionMessage);
			codeSpinner.setSelection(position);
			meterReaderMsg = new MeterReaderMsg();

			String selectedItem = codeSpinner.getSelectedItem().toString();

			String spinnerselectedItemCode = selectedItem.replaceAll("[^0-9]", "");

			messageFromReadersList = meterReaderMsg.getMessageList(spinnerselectedItemCode);

			if (isNoRadioButton1Selected && getIntent().getBooleanExtra("ZEROCONSUMPTION", true))
			{
				codeSpinner.setEnabled(true);
			}
			else
			{
				codeSpinner.setEnabled(false);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private void goToPhotoActivity()
	{
		Constants.threePicsCapCnt = 0;

		Log.i("In Verify Meter Reading ", "goToPhotoActivity");
		Log.i("In Verify Meter Reading ", "goToPhotoActivity : codesAndReasonsList[4]" + codesAndReasonsList[4]);

		if (/*(codeSpinner.getSelectedItem().equals(codesAndReasonsList[1]))
				||*/(codeSpinner.getSelectedItem().equals(codesAndReasonsList[2])))
		{

			Constants.isTwoPicsSelected = true;
			//Constants.isOnePicsSelected = false;

		}

		else if (codeSpinner.getSelectedItem().equals(codesAndReasonsList[3]))
		{
			Log.i("In Verify Meter Reading ", "goToPhotoActivity : Not using Gas 52");
			Log.i("In Verify Meter Reading ", "goToPhotoActivity : MsgFrmMR [6] :: " + messageFromReadersList[6]);
			Log.i("In Verify Meter Reading ", "goToPhotoActivity : MsgFrmMR [5] :: " + messageFromReadersList[5]);

			if (isYesRadioButton1Selected)
			{
				if (messageFromReaderSpinner.getSelectedItem().toString().equalsIgnoreCase(messageFromReadersList[6]))
				{
					Constants.isTwoPicsSelected = true;
					Constants.isThreePicsSelected = false;
					Constants.isOnePicsSelected = false;
				}
				else
				{
					Constants.isTwoPicsSelected = false;
					Constants.isThreePicsSelected = false;
					Constants.isOnePicsSelected = true;
				}
			}

			if (isNoRadioButton1Selected)
			{
				if (messageFromReaderSpinner.getSelectedItem().toString().equalsIgnoreCase(messageFromReadersList[7]))
				{
					Constants.isTwoPicsSelected = false;
					Constants.isThreePicsSelected = false;
					Constants.isOnePicsSelected = true;
				}
				else
				{
					Constants.isTwoPicsSelected = true;
					Constants.isThreePicsSelected = false;
					Constants.isOnePicsSelected = false;
				}
			}

		}

		else if (codeSpinner.getSelectedItem().equals(codesAndReasonsList[1]))
		{
			Log.i("In Verify Meter Reading ", "goToPhotoActivity : temperory Disconnection");
			Log.i("In Verify Meter Reading ", "goToPhotoActivity : MsgFrmMR [1] :: " + messageFromReadersList[1]);
			Log.i("In Verify Meter Reading ", "goToPhotoActivity : MsgFrmMR [2] :: " + messageFromReadersList[2]);

			if (isNoRadioButton1Selected)
			{
				if (messageFromReaderSpinner.getSelectedItem().toString().equalsIgnoreCase(messageFromReadersList[1])/*||messageFromReaderSpinner.getSelectedItem().toString()
																														.equalsIgnoreCase(messageFromReadersList[2])*/)
				{
					Log.i("In Verify Meter Reading ", "goToPhotoActivity : Constants.isThreePicsSelected");

					Constants.isOnePicsSelected = false;
					Constants.isTwoPicsSelected = false;
					Constants.isThreePicsSelected = true;

				}
				else if (messageFromReaderSpinner.getSelectedItem().toString()
						.equalsIgnoreCase(messageFromReadersList[2]))
				{
					Constants.isThreePicsSelected = true;
					Constants.isTwoPicsSelected = false;
					Constants.isOnePicsSelected = false;
				}

			}

		}
		else if (codeSpinner.getSelectedItem().toString().equalsIgnoreCase(codesAndReasonsList[4]))
		{
			Log.i("In Verify Meter Reading ", "goToPhotoActivity : Meter Testing not possible");
			Log.i("In Verify Meter Reading ", "goToPhotoActivity : MsgFrmMR [1] :: " + messageFromReadersList[1]);
			Log.i("In Verify Meter Reading ", "goToPhotoActivity : MsgFrmMR [2] :: " + messageFromReadersList[2]);

			if (isNoRadioButton1Selected)
			{
				if (messageFromReaderSpinner.getSelectedItem().toString().equalsIgnoreCase(messageFromReadersList[1])
						|| messageFromReaderSpinner.getSelectedItem().toString()
								.equalsIgnoreCase(messageFromReadersList[2]))
				{
					Constants.isTwoPicsSelected = true;
					Constants.isThreePicsSelected = false;
					Constants.isOnePicsSelected = false;
				}
				/*else if (messageFromReaderSpinner.getSelectedItem().toString()
						.equalsIgnoreCase(messageFromReadersList[2]))
				{
					Constants.isTwoPicsSelected = true;
					Constants.isThreePicsSelected = false;
					Constants.isOnePicsSelected = false;
				}
				*/
			}

		}

		else
		{
			Constants.isTwoPicsSelected = false;
		}

		Log.d("VerifyMeterReadingActivity - goToPhotoActivity()", "Constants.isThreePicsSelected :"
				+ Constants.isThreePicsSelected);
		Log.d("VerifyMeterReadingActivity - goToPhotoActivity()", "Constants.isTwoPicsSelected :"
				+ Constants.isTwoPicsSelected);

		Intent intent = new Intent(VerifyMeterReadingActivity.this, PhotoIntentActivity.class);
		intent.putExtra("activity", activityFromIntent);
		startActivity(intent);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	public void onBackPressed()
	{

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

	@Override
	public void onClick(View v)
	{
		switch (v.getId())

		{
			case R.id.yesRadioButton1:

				yesRadioButton1.setImageDrawable(this.getResources()
						.getDrawable(R.drawable.selected_radio_button_40x40));
				noRadioButton1.setImageDrawable(this.getResources().getDrawable(
						R.drawable.unselected_radio_button_40x40));

				isYesRadioButton1Selected = true;
				isNoRadioButton1Selected = false;

				burnerOperatedLayout.setVisibility(View.VISIBLE);
				conditionLayout.setVisibility(View.GONE);
				codeAndMessageFromReaderLayout.setVisibility(View.GONE);

				if (!getIntent().getBooleanExtra("ZEROCONSUMPTION", false))
				{
					burnerOperatedLayout.setVisibility(View.GONE);
					getLayoutVisibility("All details verified. Actual consumption abnormal.", 0);

				}
				LPGsupplier = "";

				showLayout();
				resetLPGSupplierRadioButton();

				break;

			case R.id.noRadioButton1:

				noRadioButton1
						.setImageDrawable(this.getResources().getDrawable(R.drawable.selected_radio_button_40x40));
				yesRadioButton1.setImageDrawable(this.getResources().getDrawable(
						R.drawable.unselected_radio_button_40x40));

				isNoRadioButton1Selected = true;
				isYesRadioButton1Selected = false;

				burnerOperatedLayout.setVisibility(View.GONE);

				if (!getIntent().getBooleanExtra("ZEROCONSUMPTION", false))
				{
					getLayoutVisibility("All details verified.", 0);

				}
				LPGsupplier = "";

				showLayout();
				resetLPGSupplierRadioButton();

				break;

			case R.id.yesRadioButton2:

				yesRadioButton2.setImageDrawable(this.getResources()
						.getDrawable(R.drawable.selected_radio_button_40x40));
				noRadioButton2.setImageDrawable(this.getResources().getDrawable(
						R.drawable.unselected_radio_button_40x40));

				isYesRadioButton2Selected = true;
				isNoRadioButton2Selected = false;

				LPGsupplier = "";
				showLayout();
				resetLPGSupplierRadioButton();

				break;

			case R.id.noRadioButton2:

				noRadioButton2
						.setImageDrawable(this.getResources().getDrawable(R.drawable.selected_radio_button_40x40));
				yesRadioButton2.setImageDrawable(this.getResources().getDrawable(
						R.drawable.unselected_radio_button_40x40));

				isNoRadioButton2Selected = true;
				isYesRadioButton2Selected = false;

				LPGsupplier = "";
				showLayout();
				resetLPGSupplierRadioButton();

				break;

			case R.id.next_button:

				if (!isYesRadioButton1Selected && !isNoRadioButton1Selected)
				{
					Toast.makeText(getApplicationContext(), "Nothing is selected, Please Select ", Toast.LENGTH_LONG)
							.show();
					break;
				}

				if (getIntent().getBooleanExtra("ZEROCONSUMPTION", true))
				{

					if (isYesRadioButton1Selected && (!isYesRadioButton2Selected && !isNoRadioButton2Selected))
					{
						Toast.makeText(getApplicationContext(), "Please Select MGL gas burnt, Meter Status.",
								Toast.LENGTH_LONG).show();
						break;
					}
				}

				String codeSelected = codeSpinner.getSelectedItem().toString();
				String messageSelected = messageFromReaderSpinner.getSelectedItem().toString();

				Log.d("VerifyMeterReadingActivity - onClick()", "" + codeSpinner.getSelectedItem().toString() + "  "
						+ messageFromReaderSpinner.getSelectedItem().toString());

				if (messageSelected.equals("Select"))
				{
					Toast.makeText(getApplicationContext(), "Please Select Message", Toast.LENGTH_LONG).show();
					break;
				}
				else
				{

					if (ll_radioButtonsLayoutLPG.getVisibility() == View.VISIBLE && !isHPLselected
							&& !isIndaneSelected && !isBharatSelected && !isCustomerSelected)
					{
						
							Toast.makeText(getApplicationContext(), "Please Select LPG Supplier", Toast.LENGTH_LONG)
									.show();
							break;
						

					}

					String remarksValue = meterReading_remarksvalue.getText().toString();
					if (messageSelected.equals("other") && remarksValue.equals(""))
					{
						Toast.makeText(getApplicationContext(), "Please Enter Remarks", Toast.LENGTH_LONG).show();
						break;
					}
					else
					{
						if (!remarksValue.equals(""))
						{
							meterReadingBO.setMsgRemarks(remarksValue);
						}

						Log.d("VerifyMeterReadingActivity - onClick()", " MrCode : " + getMrCode());

						if (getMrCode() != "")
						{
							MeterReadingFeild.mrCode = getMrCode();
						}

						String spinnerselectedItemCode = codeSelected.replaceAll("[^0-9]", "");

						String spinnerselectedItemReason = codeSelected.replaceAll("[^a-zA-Z ]+$", "").trim();

						meterReadingBO.setMrReason(spinnerselectedItemReason);
						Log.d("VerifyMeterReading", "meterReadingBO.getMrReason :: "
								+ meterReadingBO.toJSON().toString());

					}

				}
				meterReadingBO.setIsLPGSupplier(LPGsupplier);
				meterReadingBO.setMsgRemarksPrimary(messageSelected);

				goToPhotoActivity();

				break;

			case R.id.cancel_button:

				finish();

				break;

			case R.id.codeInfoImageLayout:

				if (getIntent().getBooleanExtra("ZEROCONSUMPTION", false))
				{

					if (isYesRadioButton1Selected && isYesRadioButton2Selected)
					{

						showCustomAlertDialog(MeterReadingNotesListsConstants.yesYesCodeList,
								MeterReadingNotesListsConstants.getYesYesCodeInHindi(getApplicationContext()),
								MeterReadingNotesListsConstants.yesYesReasonList, "Code");

					}
					else if (isYesRadioButton1Selected && isNoRadioButton2Selected)
					{

						showCustomAlertDialog(MeterReadingNotesListsConstants.yesNoCodeList,
								MeterReadingNotesListsConstants.getYesNoInHindi(getApplicationContext()),
								MeterReadingNotesListsConstants.yesNoReasonList, "Code");

					}
					else if (isNoRadioButton1Selected)
					{

						showCustomAlertDialog(MeterReadingNotesListsConstants.noCodeList,
								MeterReadingNotesListsConstants.getNoCodeInHindi(getApplicationContext()),
								MeterReadingNotesListsConstants.noReasonList, "Code");

					}
				}
				else
				{
					showCustomAlertDialog(MeterReadingNotesListsConstants.meterPossibleCodeList,
							MeterReadingNotesListsConstants.getMeterPossibleCodeInHindi(getApplicationContext()),
							MeterReadingNotesListsConstants.meterPossibleReasonList, "Code");

				}
				/*				showCustomAlertDialog(MeterReadingNotesListsConstants.meterNormalCodeList,
										MeterReadingNotesListsConstants.getMeterNormalCodeInHindi(getApplicationContext()),
										MeterReadingNotesListsConstants.meterNormalReasonsList, "Code");
				*/
				break;

			case R.id.messageInfoImageLayout:

				//String selectedCodeFromSpinner = spinner.getSelectedItem().toString();

				String[] msgList = null;

				String[] msgList1 = null;

				String selectedItem = (String) codeSpinner.getSelectedItem();
				String[] codesForMessageList = {};

				Log.d("VerifyMeterReading", "Code spinner selecteditem : " + selectedItem);

				String selectedCode = selectedItem.replaceAll("[^0-9]", "");

				Log.d("VerifyMeterReading", "Code spinner selected code : " + selectedCode);

				/*if (isNoRadioButton1Selected && selectedItem.equals("Not Using Gas 52"))
				{
					msgList = MeterReaderMsg.msgListForNotUsingGas;
				}

				else
				{

					msgList = meterReaderMsg.getMessageList(selectedCode);
				}
				String[] codesForMessageList = {};

				if (isNoRadioButton1Selected && selectedItem.equals("Not Using Gas 52"))
				{
					showCustomAlertDialog(codesForMessageList,
							MeterReadingNotesListsConstants.getNotUsingGasMessageInHindi(getApplicationContext()),
							msgList, "Message From Reader");
				}

				else
				{
					showCustomAlertDialog(codesForMessageList, MeterReadingNotesListsConstants.getMessageInHindiList(
							getApplicationContext(), selectedCode), msgList, "Message From Reader");
				}
				
				if(isYesRadioButton1Selected && isYesRadioButton2Selected && selectedItem.equals("Not Using Gas 52"))
				{
					msgList1 = MeterReaderMsg.msgList_52_NotUsingGas;
				}
				else
				{
					msgList = meterReaderMsg.getMessageList(selectedCode);
				}
				if(isYesRadioButton1Selected && isYesRadioButton2Selected && selectedItem.equals("Not Using Gas 52"))
				{
					showCustomAlertDialog(codesForMessageList,
							MeterReadingNotesListsConstants.getNotusingGasYY(getApplicationContext()),
							msgList, "Message From Reader");
				}
				else
				{
					showCustomAlertDialog(codesForMessageList, MeterReadingNotesListsConstants.getMessageInHindiList(
							getApplicationContext(), selectedCode), msgList, "Message From Reader");
				}
				*/
				if (isNoRadioButton1Selected && selectedItem.equals("Not Using Gas 52"))
				{
					msgList = MeterReaderMsg.msgListForNotUsingGas;
					showCustomAlertDialog(codesForMessageList,
							MeterReadingNotesListsConstants.getNotUsingGasMessageInHindi(getApplicationContext()),
							msgList, "Message From Reader");
				}

				else if (isYesRadioButton1Selected && isYesRadioButton2Selected
						&& selectedItem.equals("Not Using Gas 52"))
				{
					msgList1 = MeterReaderMsg.msgList_52_NotUsingGas;
					showCustomAlertDialog(codesForMessageList,
							MeterReadingNotesListsConstants.getNotusingGasYY(getApplicationContext()), msgList1,
							"Message From Reader");
				}
				else
				{
					msgList = meterReaderMsg.getMessageList(selectedCode);
					showCustomAlertDialog(codesForMessageList, MeterReadingNotesListsConstants.getMessageInHindiList(
							getApplicationContext(), selectedCode), msgList, "Message From Reader");
				}

				break;

			case R.id.conditionInfoImageLayout:

				String[] codesForConditionList = {};

				String selectedCondition = conditionDescriptionTextView.getText().toString();

				String[] conditionsInHindi = MeterReadingNotesListsConstants.getConditionsInHindiList(
						getApplicationContext(), selectedCondition);

				showCustomAlertDialog(codesForConditionList, MeterReadingNotesListsConstants.getConditionsInHindiList(
						getApplicationContext(), selectedCondition), new String[] { selectedCondition }, "Condition");
				break;

			/*case R.id.rb_bharatLPGSupplier:
				LPGsupplier = "Bharat";
				//meterReadingBO.setIsLPGSupplier(LPGsupplier);
				break;
			case R.id.rb_IndaneLPGSupplier:
				LPGsupplier = "Indane";
				//meterReadingBO.setIsLPGSupplier(LPGsupplier);
				break;
			case R.id.rb_HpLPGSupplier:
				LPGsupplier = "HPL";
				//meterReadingBO.setIsLPGSupplier(LPGsupplier);
				break;
			case R.id.rb_custLPGSupplier:
				LPGsupplier = "Customer did not share detail";
				//meterReadingBO.setIsLPGSupplier(LPGsupplier);
				break;
				
				
*/
			case R.id.imageBharat:	
				LPGsupplier = "Bharat";
				Bharat.setImageDrawable(this.getResources().getDrawable(R.drawable.selected_radio_button_40x40));
			    HPL.setImageDrawable(this.getResources().getDrawable(
						R.drawable.unselected_radio_button_40x40));
			    Customer.setImageDrawable(this.getResources().getDrawable(
						R.drawable.unselected_radio_button_40x40));
			    Indane. setImageDrawable(this.getResources().getDrawable(
						R.drawable.unselected_radio_button_40x40));
			    
			    isBharatSelected=true;
			    isIndaneSelected=false;
			    isCustomerSelected=false;
			    isHPLselected=false;
				break;
			case R.id.imageIndane:	
				LPGsupplier = "Indane";
				
				Indane.setImageDrawable(this.getResources().getDrawable(R.drawable.selected_radio_button_40x40));
				Bharat.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
			    HPL.setImageDrawable(this.getResources().getDrawable(
						R.drawable.unselected_radio_button_40x40));
			    Customer.setImageDrawable(this.getResources().getDrawable(
						R.drawable.unselected_radio_button_40x40));
			    
			    isBharatSelected=false;
			    isIndaneSelected=true;
			    isCustomerSelected=false;
			    isHPLselected=false;
				break;
			case R.id.imageHPL:
				LPGsupplier = "HP";
				Indane.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				Bharat.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
			    HPL.setImageDrawable(this.getResources().getDrawable(
						R.drawable.selected_radio_button_40x40));
			    Customer.setImageDrawable(this.getResources().getDrawable(
						R.drawable.unselected_radio_button_40x40));
			    
			    isBharatSelected=false;
			    isIndaneSelected=false;
			    isCustomerSelected=false;
			    isHPLselected=true;
				break;
			case R.id.imageCustomer:
				LPGsupplier = "Customer did not share detail";
				Indane.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				Bharat.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
			    HPL.setImageDrawable(this.getResources().getDrawable(
						R.drawable.unselected_radio_button_40x40));
			    Customer.setImageDrawable(this.getResources().getDrawable(
						R.drawable.selected_radio_button_40x40));
			    
			    isBharatSelected=false;
			    isIndaneSelected=false;
			    isCustomerSelected=true;
			    isHPLselected=false;
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
