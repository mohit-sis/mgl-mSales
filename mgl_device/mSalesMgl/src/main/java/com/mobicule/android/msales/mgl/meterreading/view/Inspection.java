package com.mobicule.android.msales.mgl.meterreading.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.msales.mgl.client.meterreading.DefaultMeterReading;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IInspection;

import java.io.File;

/**
 * 
 * <enter description here>
 * 
 * @author Prashant
 * @see
 * 
 * @createdOn 19-Mar-2013
 * @modifiedOn
 * 
 * @copyright © 2010-2011 Mobicule Technologies Pvt. Ltd. All rights reserved.
 */

public class Inspection extends DefaultMeterReadingActivity implements OnItemSelectedListener, OnClickListener
{
	private ArrayAdapter<String> meterProtectionSealAdapter;

	private RadioButton radioButtonMeterTamperedYes, radioButtonMeterByPassYes;

	private RadioButton radioButtonMeterTamperedNo, radioButtonMeterByPassNo;
	
	private TextView textViewCurrentSealNo;

	private EditText editTextRemarkMeterTampered;

	private EditText editTextRemarkMeterProtectionSeal;

	private EditText editTextRemarkMeterByPass;

	private EditText editTextInputSealNo;

	private Spinner spinnerMeterProtectionSeal;

	private String[] meterProtectionSealItems = { "Installed", "Not installed", "Missing" };

	private String selectedItem;
	
	IInspection inspection;

	private Button nextButton;
	private String activityFromIntent;

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

		setContentView(R.layout.inspection);
		initializeControls();
		try{
			activityFromIntent = getIntent().getStringExtra("activity");

			if(meterReadingBO.getInspection() == null)
		{
			inspection = new DefaultMeterReading.DefaultInspection();
			inspection.setCurrentSealNo("");
		}
		else
		{
			inspection = new DefaultMeterReading.DefaultInspection(meterReadingBO.getInspection());
		}
		textViewCurrentSealNo.setText(inspection.getCurrentSealNo());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	

	private void initializeControls()
	{
		try
		{
			radioButtonMeterTamperedYes = (RadioButton) findViewById(R.id.inspect_RadioButtonTamperedYes);
			radioButtonMeterByPassYes = (RadioButton) findViewById(R.id.inspect_RadioButtonByPassYes);
			radioButtonMeterTamperedNo = (RadioButton) findViewById(R.id.inspect_RadioButtonTamperedNo);
			radioButtonMeterByPassNo = (RadioButton) findViewById(R.id.inspect_RadioButtonByPassNo);

			editTextRemarkMeterTampered = (EditText) findViewById(R.id.inspect_EditTextRemarkTampered);
			editTextRemarkMeterProtectionSeal = (EditText) findViewById(R.id.inspect_EditTextRemarkMeterProtectionSeal);
			editTextRemarkMeterByPass = (EditText) findViewById(R.id.inspect_EditTextRemarkByPass);

			textViewCurrentSealNo = (TextView)findViewById(R.id.inspect_TextViewCurrentSealNo);
			
			editTextInputSealNo = (EditText) findViewById(R.id.inspect_EditTextInputSealNo);

			spinnerMeterProtectionSeal = (Spinner) findViewById(R.id.inspect_SpinnerMeterProtectionSeal);

			meterProtectionSealAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
					meterProtectionSealItems);
			meterProtectionSealAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerMeterProtectionSeal.setAdapter(meterProtectionSealAdapter);

			radioButtonMeterTamperedYes.setOnClickListener(this);
			radioButtonMeterByPassYes.setOnClickListener(this);
			radioButtonMeterTamperedNo.setOnClickListener(this);
			radioButtonMeterByPassNo.setOnClickListener(this);
			spinnerMeterProtectionSeal.setOnItemSelectedListener(this);

			enableEditText(editTextRemarkMeterTampered, false);
			enableEditText(editTextRemarkMeterByPass, false);
			
			nextButton = (Button) findViewById(R.id.next_button);
			nextButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					if (isFormDataValid())
					{ 
						Constants.threePicsCapCnt = 0;
						Constants.isThreePicsSelected = true;

						
						if(radioButtonMeterTamperedYes.isChecked())
						{
							inspection.setIsMeterIndexTampered("Yes");
						}
						else
						{
							inspection.setIsMeterIndexTampered("No");
						}						
						inspection.setMeterIndexTamperedRemark(editTextRemarkMeterTampered.getText().toString().trim());
						inspection.setMeterProtectionSeal(spinnerMeterProtectionSeal.getSelectedItem().toString().trim());
						inspection.setInputSealNo(editTextInputSealNo.getText().toString().trim());
						inspection.setMeterProtectionSealRemark(editTextRemarkMeterProtectionSeal.getText().toString().trim());
						if(radioButtonMeterByPassYes.isChecked())
						{
							inspection.setIsMeterByPass("Yes");
						}
						else
						{
							inspection.setIsMeterByPass("No");
						}
						inspection.setMeterByPassRemark(editTextRemarkMeterByPass.getText().toString().trim());

						meterReadingBO.setInspection(inspection);
						MobiculeLogger.verbose("METER READING BO - "+ meterReadingBO.toJSON().toString());

						Intent intent = new Intent(Inspection.this, PhotoIntentActivity.class);
						intent.putExtra("activity", activityFromIntent);

						startActivity(intent);
					}
				}
			});
		}
		catch (Exception e)
		{
			MobiculeLogger.verbose("Inspection.initializeControls() - "+ e.toString());
		}
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		Constants.broadcastDialogContext = this;
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add("Next");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		try
		{
			if (item.getTitle().equals("Next"))
			{
				if (isFormDataValid())
				{ 
					Constants.threePicsCapCnt = 0;
					Constants.isThreePicsSelected = true;

					
					if(radioButtonMeterTamperedYes.isChecked())
					{
						inspection.setIsMeterIndexTampered("Yes");
					}
					else
					{
						inspection.setIsMeterIndexTampered("No");
					}						
					inspection.setMeterIndexTamperedRemark(editTextRemarkMeterTampered.getText().toString().trim());
					inspection.setMeterProtectionSeal(spinnerMeterProtectionSeal.getSelectedItem().toString().trim());
					inspection.setInputSealNo(editTextInputSealNo.getText().toString().trim());
					inspection.setMeterProtectionSealRemark(editTextRemarkMeterProtectionSeal.getText().toString().trim());
					if(radioButtonMeterByPassYes.isChecked())
					{
						inspection.setIsMeterByPass("Yes");
					}
					else
					{
						inspection.setIsMeterByPass("No");
					}
					inspection.setMeterByPassRemark(editTextRemarkMeterByPass.getText().toString().trim());

					meterReadingBO.setInspection(inspection);
					Log.i("METER READING BO", meterReadingBO.toJSON().toString());

					Intent intent = new Intent(Inspection.this, PhotoIntentActivity.class);
					startActivity(intent);
				}
			}
		}
		catch (Exception e)
		{
			Log.e("Inspection.next", e.toString());
		}
		return super.onOptionsItemSelected(item);
	}*/

	private boolean isFormDataValid()
	{
		//if meter is tampered
		if ((radioButtonMeterTamperedYes.isChecked())
				&& (editTextRemarkMeterTampered.getText().toString().trim().equals("")))
		{
			Toast.makeText(this, getString(R.string.meter_tampered_remark), Toast.LENGTH_SHORT).show();
			editTextRemarkMeterTampered.requestFocus();
			return false;
		}
		//if meter protection seal is INSTALLED
		if ((selectedItem.equals(meterProtectionSealItems[0]))
				&& (editTextInputSealNo.getText().toString().trim().equals("")))
		{
			Toast.makeText(this, getString(R.string.meter_input_seal_no), Toast.LENGTH_SHORT).show();
			editTextInputSealNo.requestFocus();
			return false;
		}
		//if meter protection seal is MISSING
		if ((selectedItem.equals(meterProtectionSealItems[2]))
				&& (editTextRemarkMeterProtectionSeal.getText().toString().trim().equals("")))
		{
			Toast.makeText(this, getString(R.string.meter_protection_remark), Toast.LENGTH_SHORT).show();
			editTextRemarkMeterProtectionSeal.requestFocus();
			return false;
		}
		//if meter is bypass 
		if ((radioButtonMeterByPassYes.isChecked())
				&& (editTextRemarkMeterByPass.getText().toString().trim().equals("")))
		{
			Toast.makeText(this, getString(R.string.meter_bypass_remark), Toast.LENGTH_SHORT).show();
			editTextRemarkMeterByPass.requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int position, long id)
	{
		selectedItem = spinnerMeterProtectionSeal.getSelectedItem().toString();

		// if meter protection seal is INSTALLED
		if (selectedItem.equals(meterProtectionSealItems[0]))
		{
			enableEditText(editTextInputSealNo, true);
			enableEditText(editTextRemarkMeterProtectionSeal, true);
		}
		// if meter protection seal is NOT INSTALLED
		if (selectedItem.equals(meterProtectionSealItems[1]))
		{
			enableEditText(editTextInputSealNo, false);
			enableEditText(editTextRemarkMeterProtectionSeal, false);
		}
		// if meter protection seal is MISSING
		if (selectedItem.equals(meterProtectionSealItems[2]))
		{
			enableEditText(editTextInputSealNo, false);
			enableEditText(editTextRemarkMeterProtectionSeal, true);
		}
	}

	@Override
	public void onClick(View v)
	{
		if (v.equals(radioButtonMeterTamperedYes))
		{
			enableEditText(editTextRemarkMeterTampered, true);
		}
		if (v.equals(radioButtonMeterTamperedNo))
		{
			enableEditText(editTextRemarkMeterTampered, false);
		}
		if (v.equals(radioButtonMeterByPassYes))
		{
			enableEditText(editTextRemarkMeterByPass, true);
		}
		if (v.equals(radioButtonMeterByPassNo))
		{
			enableEditText(editTextRemarkMeterByPass, false);
		}
	}

	private void enableEditText(EditText editText, boolean enable)
	{
		editText.setEnabled(enable);
		editText.setFocusable(enable);
		editText.setFocusableInTouchMode(enable);
		if (!enable)
		{
			editText.setText("");
			editText.setInputType(0);
		}
		else
		{
			if (editText.equals(editTextInputSealNo))
			{
				editText.setInputType(InputType.TYPE_CLASS_NUMBER);
			}
			else
			{
				editText.setInputType(InputType.TYPE_CLASS_TEXT);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			meterReadingBO.removeInspection();
		}
		return super.onKeyDown(keyCode, event);
	}

}
