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

import java.io.File;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.util.Base64;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.component.string.StringUtil;
import com.mobicule.msales.mgl.client.randommeterreading.DefaultRandomMeterReading;
import com.mobicule.msales.mgl.client.randommeterreading.DefaultRandomMeterReading.DefaultReadings;

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
public class RandomMeterReadingSummary extends DefaultRandomMeterReadingActivity implements OnClickListener
{
	private TextView status;

	private TextView statusValue;

	private TextView customerNameValue;

	private TextView contactNoValue;

	private TextView meterreadingText;

	private EditText meterReading;

	private byte[] bitmapdata;

	private ImageView imageView;

	private boolean menuOptionEnabled;

	private DefaultReadings reading;

	private String readingValue;

	private String stautsRMR;
	
	private Button submitButton;

	@Override
	protected void onStart()
	{
		super.onStart();
		Constants.broadcastDialogContext = this;
	}

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

		setContentView(R.layout.random_meter_reading_new);

		initalization();
		
		if(getIntent().getStringExtra("TAG").equalsIgnoreCase("SavedRandomMeterReading")){
			
			submitButton.setVisibility(View.GONE);
			
		}else{
			
			submitButton.setVisibility(View.VISIBLE);
			
		}

		try
		{
			//if(randomMeterReadingBO.getBpNumber())
			String imageStr = randomMeterReadingBO.getImage();
			if (StringUtil.isValid(imageStr))
			{
				bitmapdata = Base64.decode(imageStr);
				if (bitmapdata == null)
				{
					imageView.setVisibility(View.GONE);
				}
				else if (bitmapdata != null && bitmapdata.length > 0)
				{
					imageView.setVisibility(View.VISIBLE);
					Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
					imageView.setImageBitmap(bitmap);
				}
			}
		}
		catch (Exception e)
		{
			handler.logCrashReport(e);
			e.printStackTrace();
		}

		menuOptionEnabled = true;
		if (getIntent().hasExtra("menu"))
		{
			menuOptionEnabled = getIntent().getExtras().getBoolean("menu");
		}
		if (getIntent().hasExtra("reading") && (getIntent().hasExtra("status")))
		{
			meterReading.setVisibility(View.GONE);
			readingValue = getIntent().getExtras().getString("reading");
			stautsRMR = getIntent().getExtras().getString("status");
			meterreadingText.setText(readingValue);
			status.setText(stautsRMR+":");

		}

		String statusSelected = randomMeterReadingBO.getSelectedStatus().toString();

		String selectedValue = randomMeterReadingBO.getStatusValue();
		if (statusSelected.equalsIgnoreCase("BP Number"))
		{
			randomMeterReadingBO.setApplicationFormNo("");
			randomMeterReadingBO.setCANo("");
			randomMeterReadingBO.setMeterNumber("");
			randomMeterReadingBO.setBpNumber(selectedValue);
		}
		else if (statusSelected.equalsIgnoreCase("Application Form Number"))
		{
			randomMeterReadingBO.setBpNumber("");
			randomMeterReadingBO.setCANo("");
			randomMeterReadingBO.setMeterNumber("");
			randomMeterReadingBO.setApplicationFormNo(selectedValue);
		}
		else if (statusSelected.equalsIgnoreCase("Meter Number"))
		{
			randomMeterReadingBO.setBpNumber("");
			randomMeterReadingBO.setCANo("");
			randomMeterReadingBO.setApplicationFormNo("");
			randomMeterReadingBO.setMeterNumber(selectedValue);
		}
		else if (statusSelected.equalsIgnoreCase("CA Number"))
		{
			randomMeterReadingBO.setBpNumber("");
			randomMeterReadingBO.setMeterNumber("");
			randomMeterReadingBO.setApplicationFormNo("");
			randomMeterReadingBO.setCANo(selectedValue);
		}

		status.setText(randomMeterReadingBO.getSelectedStatus().toString()+":");

		statusValue.setText(randomMeterReadingBO.getStatusValue().toString());

		customerNameValue.setText(randomMeterReadingBO.getCustomerName().toString());

		contactNoValue.setText(randomMeterReadingBO.getCustomerContactNo().toString());

	}

	private void initalization()
	{
		status = (TextView) findViewById(R.id.statusText);
		statusValue = (TextView) findViewById(R.id.statusvalue);
		customerNameValue = (TextView) findViewById(R.id.custname_value);
		contactNoValue = (TextView) findViewById(R.id.custContactNo_value);
		meterReading = (EditText) findViewById(R.id.meterreading_edittext);
		imageView = (ImageView) findViewById(R.id.ramdomimage);
		meterreadingText = (TextView) findViewById(R.id.meterreading);
		
		submitButton = (Button) findViewById(R.id.submit_button);
		submitButton.setOnClickListener(this);
	}

	//Previous Code-------------------------------------------------------------------------------------------
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if (menuOptionEnabled)
		{
			menu.add(Constants.SUBMIT);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getTitle().equals(Constants.SUBMIT))
		{
			randomMeterReadingBO.resetReadingList();
			reading = new DefaultRandomMeterReading.DefaultReadings();

			SimpleDateFormat dateTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.US);

			String readingTime = dateTime.format(new Date(System.currentTimeMillis()));
			String time = readingTime.substring(11);
			String date = readingTime.substring(0, 10);
			reading.setDate(date);
			reading.setReadingTime(time);
			reading.setMeterReading(meterReading.getText().toString());

			randomMeterReadingBO.setReadings(reading);

			if (meterReading.getText().toString().equals(""))
			{
				Toast.makeText(RandomMeterReadingSummary.this, "Please Enter Meter Reading", Toast.LENGTH_LONG).show();
			}
			else if (meterReading.getText().toString().length() < 3)
			{
				Toast.makeText(RandomMeterReadingSummary.this, "Please Enter atleast 3 digit Meter Reading",
						Toast.LENGTH_LONG).show();
			}
			else
			{
				new ApplicationAsk(RandomMeterReadingSummary.this, new ApplicationService()
				{

					@Override
					public void postExecute()
					{
						AlertDialog.Builder submitDialog = new AlertDialog.Builder(RandomMeterReadingSummary.this);
						submitDialog.setCancelable(false);
						submitDialog.setTitle("Save Random Meter Reading");
						submitDialog.setMessage(response.getMessage());
						submitDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
								Intent broadcastIntent = new Intent();
								broadcastIntent.setAction("com.mobicule.ACTION_MY_BOOK_WALK");
								sendBroadcast(broadcastIntent);
								startActivity(new Intent(RandomMeterReadingSummary.this,
										RandomMeterReadingActivity.class));
							}
						});
						submitDialog.show();
					}

					@Override
					public void execute()
					{
						Response randomResponse = randomMeterReadingFacade.submitOneRandomMeterReading(randomMeterReadingBO.toJSON().toString());
						if(randomResponse.isSuccess())
						{
							//response = randomMeterReadingFacade.saveRandomMeterReading(true);
							response = randomResponse;
						}
						else
						{
							response = randomMeterReadingFacade.saveRandomMeterReading(false);
						}
						response = randomMeterReadingFacade.saveRandomMeterReading(false);
					}
				}).execute();
			}

		}

		return super.onOptionsItemSelected(item);
	}*/

	@Override
	public void onClick(View v)
	{
		if(v.getId() == R.id.submit_button)
		{

			randomMeterReadingBO.resetReadingList();
			reading = new DefaultRandomMeterReading.DefaultReadings();

			SimpleDateFormat dateTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.US);

			String readingTime = dateTime.format(new Date(System.currentTimeMillis()));
			String time = readingTime.substring(11);
			String date = readingTime.substring(0, 10);
			reading.setDate(date);
			reading.setReadingTime(time);
			
			if(getIntent().hasExtra("reading") && (getIntent().hasExtra("status")))
			{
				reading.setMeterReading(meterreadingText.getText().toString());
				randomMeterReadingBO.setReadings(reading);
				
				new ApplicationAsk(RandomMeterReadingSummary.this, new ApplicationService()
				{

					@Override
					public void postExecute()
					{
						AlertDialog.Builder submitDialog = new AlertDialog.Builder(RandomMeterReadingSummary.this);
						submitDialog.setCancelable(false);
						submitDialog.setTitle("Save Random Meter Reading");
						submitDialog.setMessage(response.getMessage());
						submitDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
								Intent broadcastIntent = new Intent();
								broadcastIntent.setAction("com.mobicule.ACTION_MY_BOOK_WALK");
								sendBroadcast(broadcastIntent);
								startActivity(new Intent(RandomMeterReadingSummary.this, RandomMeterReadingActivity.class));
							}
						});
						submitDialog.show();
					}

					@Override
					public void execute()
					{
						/*Response randomResponse = randomMeterReadingFacade.submitOneRandomMeterReading(randomMeterReadingBO.toJSON().toString());
						if(randomResponse.isSuccess())
						{
							//response = randomMeterReadingFacade.saveRandomMeterReading(true);
							response = randomResponse;
						}
						else
						{
							response = randomMeterReadingFacade.saveRandomMeterReading(false);
						}*/
						response = randomMeterReadingFacade.saveRandomMeterReading(false);
					}
				}).execute();
			}
			else
			{
				reading.setMeterReading(meterReading.getText().toString());

				randomMeterReadingBO.setReadings(reading);
				
				if (meterReading.getText().toString().equals(""))
				{
					Toast.makeText(RandomMeterReadingSummary.this, "Please Enter Meter Reading", Toast.LENGTH_LONG).show();
				}
				else if (meterReading.getText().toString().length() < 3)
				{
					Toast.makeText(RandomMeterReadingSummary.this, "Please Enter atleast 3 digit Meter Reading",
							Toast.LENGTH_LONG).show();
				}
				else
				{
					new ApplicationAsk(RandomMeterReadingSummary.this, new ApplicationService()
					{

						@Override
						public void postExecute()
						{
							AlertDialog.Builder submitDialog = new AlertDialog.Builder(RandomMeterReadingSummary.this);
							submitDialog.setCancelable(false);
							submitDialog.setTitle("Save Random Meter Reading");
							submitDialog.setMessage(response.getMessage());
							submitDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									dialog.dismiss();
									Intent broadcastIntent = new Intent();
									broadcastIntent.setAction("com.mobicule.ACTION_MY_BOOK_WALK");
									sendBroadcast(broadcastIntent);
									startActivity(new Intent(RandomMeterReadingSummary.this, RandomMeterReadingActivity.class));
								}
							});
							submitDialog.show();
						}

						@Override
						public void execute()
						{
							/*Response randomResponse = randomMeterReadingFacade.submitOneRandomMeterReading(randomMeterReadingBO.toJSON().toString());
							if(randomResponse.isSuccess())
							{
								//response = randomMeterReadingFacade.saveRandomMeterReading(true);
								response = randomResponse;
							}
							else
							{
								response = randomMeterReadingFacade.saveRandomMeterReading(false);
							}*/
							response = randomMeterReadingFacade.saveRandomMeterReading(false);
						}
					}).execute();
				}
			}
			
		}
		
	}

	@Override
	public void onBackPressed()
	{
		if(getIntent().getStringExtra("TAG").equalsIgnoreCase("SavedRandomMeterReading")){
			finish();
		}else{
			MobiculeLogger.verbose("back button pressed");
		        
		        Intent intent = new Intent(RandomMeterReadingSummary.this, RandomMeterReadingActivity.class);
		        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(intent);
		}
		super.onBackPressed();
	}
	
	
	
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	   
	    if(getIntent().getStringExtra("TAG").equalsIgnoreCase("SavedRandomMeterReading")){
			finish();
		}else{
		        
		        Intent intent = new Intent(RandomMeterReadingSummary.this, RandomMeterReadingActivity.class);
		        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(intent);
		}
	    return super.onKeyDown(keyCode, event);
	}
*/
	
	
	
	
}
