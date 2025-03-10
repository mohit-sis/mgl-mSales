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
package com.mobicule.android.msales.mgl.randommeterreading.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.component.string.StringUtil;

import java.io.File;

/**
 * 
 * <enter description here>
 *
 * @author nikita 
 * @see 
 *
 * @createdOn 21-Feb-2012
 * @modifiedOn
 *
 * @copyright © 2010-2011 Mobicule Technologies Pvt. Ltd. All rights reserved.
 */
public class RandomMeterReadingActivity extends DefaultRandomMeterReadingActivity implements OnClickListener
{
	private String[] searchBy = { "BP Number", "Application Form Number", "Meter Number", "CA Number" };

	private Spinner myRandomsearch;

	private TextView searchText;

	private EditText searchInput;

	private String selectedItem;

	private EditText customerNameInput;

	private EditText customerContactNoInput;

	private Button nextButton, cancelButton;

	private TextView searchTextMandatory;

	@Override
	protected void onStart()
	{
		super.onStart();
		Constants.broadcastDialogContext = this;
	}
	
	

	@Override
	protected void onResume()
	{
		
		super.onResume();
		searchInput.setText("");
		searchInput.requestFocus();
		customerNameInput.setText("");
		customerContactNoInput.setText("");
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

		setContentView(R.layout.random_reading_form);

		randomMeterReadingFacade.getRandomMeterReading().reset();

		MobiculeLogger.verbose("onCreate");
		initalization();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, searchBy);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		myRandomsearch.setAdapter(adapter);

		myRandomsearch.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3)
			{

				selectedItem = (String) parent.getItemAtPosition(position);
				searchText.setVisibility(View.GONE);
				searchTextMandatory.setVisibility(View.GONE);
				searchText.setText("");
				customerNameInput.setText("");
				customerContactNoInput.setText("");
				searchInput.setVisibility(View.GONE);
				searchInput.setText("");
				if (selectedItem.equals(searchBy[0]))
				{
					searchText.setVisibility(View.VISIBLE);
					searchTextMandatory.setVisibility(View.VISIBLE);
					searchText.setText("Enter BP Number:");
					searchInput.setVisibility(View.VISIBLE);
					searchInput.setSelected(false);
					int maxLength = 10;    
					searchInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
				}
				else if (selectedItem.equals(searchBy[1]))
				{
					searchText.setVisibility(View.VISIBLE);
					searchTextMandatory.setVisibility(View.VISIBLE);
					searchText.setText("Enter Application Form Number:");
					searchInput.setVisibility(View.VISIBLE);
					searchInput.setSelected(false);
					int maxLength = 20;
					searchInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
				}
				else if (selectedItem.equals(searchBy[2]))
				{
					searchText.setVisibility(View.VISIBLE);
					searchTextMandatory.setVisibility(View.VISIBLE);
					searchText.setText("Enter Meter Number:");
					searchInput.setVisibility(View.VISIBLE);
					searchInput.setSelected(false);
					int maxLength = 20;
					searchInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
				}
				else if (selectedItem.equals(searchBy[3]))
				{
					searchText.setVisibility(View.VISIBLE);
					searchTextMandatory.setVisibility(View.VISIBLE);
					searchText.setText("Enter CA Number:");
					searchInput.setVisibility(View.VISIBLE);
					searchInput.setSelected(false);
					int maxLength = 12;    
					searchInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{

			}

		});

	}

	public void initalization()
	{
		myRandomsearch = (Spinner) findViewById(R.id.spinner);
		searchText = (TextView) findViewById(R.id.rmrTextview);
		searchTextMandatory = (TextView) findViewById(R.id.rmrTextviewmandatory);
		searchInput = (EditText) findViewById(R.id.rmredittext);
		customerNameInput = (EditText) findViewById(R.id.custname_Text);
		customerNameInput.setSelected(false);
		
		customerContactNoInput = (EditText) findViewById(R.id.custcontactNo);
		customerContactNoInput.setSelected(false);
		int maxLength = 10;
		customerContactNoInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

		nextButton = (Button) findViewById(R.id.next_button);
		nextButton.setOnClickListener(this);

		cancelButton = (Button) findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(this);
	}

	//Previous code ------------------------------------------------------------------------------------------

	/*public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add("Next");
		menu.add("Cancel");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		if (item.getTitle().equals("Cancel"))
		{
			finish();
		}
		else if (item.getTitle().equals("Next"))
		{
			if (randomMeterReadingvalidation())
			{
				randomMeterReadingBO.setSelectedStatus(myRandomsearch.getSelectedItem().toString());
				randomMeterReadingBO.setStatusValue(searchInput.getText().toString());
				randomMeterReadingBO.setCustomerName(customerNameInput.getText().toString());
				randomMeterReadingBO.setCustomerContactNo(customerContactNoInput.getText().toString());
				Intent intent = new Intent(RandomMeterReadingActivity.this, TakePicture.class);
				startActivity(intent);
			}

		}
		return super.onOptionsItemSelected(item);
	}*/

	boolean randomMeterReadingvalidation()
	{
		boolean flagcustomerName = false, flagcustomerContactNoInput = false, flagsearchInput = false, flagContactNo = false;
		String toastMessage = null;

		if (customerNameInput.getText().toString().equalsIgnoreCase(""))
			flagcustomerName = false;
		else
			flagcustomerName = true;
		if (customerContactNoInput.getText().toString().equalsIgnoreCase("")){
			flagcustomerContactNoInput = false;
		}
		else{
			flagcustomerContactNoInput = true;
		}
		if(customerContactNoInput.getText().toString().length() < 8)
		{
			flagContactNo = false;
		}else{
			flagContactNo = true;
		}
		if (searchInput.getText().toString().equalsIgnoreCase(""))
		{
			flagsearchInput = false;
		}
		else
		{
			flagsearchInput = true;
		}

		if (!flagcustomerName && !flagcustomerContactNoInput && !flagsearchInput)
			toastMessage = "Please enter mandatory fields";
		else if (!flagcustomerName)
			toastMessage = "Enter Customer Name";
		else if (!flagcustomerContactNoInput)
			toastMessage = "Enter Customer Contact Number";
		else if (!flagsearchInput)
			toastMessage = "Enter "+selectedItem;
		else if (!flagContactNo)
			toastMessage = "Please enter valid Contact Number"; 

		if (!flagcustomerName || !flagcustomerContactNoInput || !flagsearchInput || !flagContactNo)
		{
			Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	@Override
	public void onBackPressed()
	{
		finish();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.next_button:

				if (randomMeterReadingvalidation())
				{
					randomMeterReadingBO.setSelectedStatus(myRandomsearch.getSelectedItem().toString());

					if (selectedItem.equals(searchBy[0]))
					{
						if (StringUtil.isValidBpNo(searchInput.getText().toString()))
						{
							randomMeterReadingBO.setStatusValue(searchInput.getText().toString());
						}
						else
						{
							Toast.makeText(this, "Please Enter Correct BP Number.", Toast.LENGTH_LONG).show();
							break;
						}
					}
					else if (selectedItem.equals(searchBy[1]))
					{
						randomMeterReadingBO.setStatusValue(searchInput.getText().toString());
					}
					else if (selectedItem.equals(searchBy[2]))
					{
						randomMeterReadingBO.setStatusValue(searchInput.getText().toString());
					}
					else if (selectedItem.equals(searchBy[3]))
					{
						randomMeterReadingBO.setStatusValue(searchInput.getText().toString());
						if (StringUtil.isValidCaNo(searchInput.getText().toString()))
						{
							randomMeterReadingBO.setStatusValue(searchInput.getText().toString());
						}
						else
						{
							Toast.makeText(this, "Please Enter Correct CA Number.", Toast.LENGTH_LONG).show();
							break;
						}
						
					}

					randomMeterReadingBO.setCustomerName(customerNameInput.getText().toString());
					randomMeterReadingBO.setCustomerContactNo(customerContactNoInput.getText().toString());
					Intent intent = new Intent(RandomMeterReadingActivity.this, TakePicture.class);
					startActivity(intent);
				}

				break;

			case R.id.cancel_button:

				finish();

				break;

			default:
				break;
		}

	}


}
