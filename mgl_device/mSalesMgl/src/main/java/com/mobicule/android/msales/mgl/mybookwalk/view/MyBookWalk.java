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
package com.mobicule.android.msales.mgl.mybookwalk.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.menu.view.MainMenu;
import com.mobicule.android.msales.mgl.meterreading.view.BuildingListActivity;
import com.mobicule.android.msales.mgl.meterreading.view.DefaultMeterReadingActivity;
import com.mobicule.android.msales.mgl.search.view.SearchInputActivity;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.component.string.StringUtil;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.meterreading.DefaultMeterReading;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IReadings;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

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
public class MyBookWalk extends DefaultMeterReadingActivity implements OnItemClickListener
{

	private ListView bookwalkListMenu;

	private TextView listText;

	protected ArrayList<String> numberOfCount;

	private static MyBookWalk instance;

	private static boolean autoTriggerOn;

	public static MyBookWalk getInstance()
	{
		if (instance == null)
		{
			instance = new MyBookWalk();
		}
		return instance;
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		Constants.broadcastDialogContext = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		try
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

			setContentView(R.layout.mybookwalk);
			if (this.getIntent().hasExtra("AutoTrigger"))
			{
				autoTriggerOn = this.getIntent().getExtras().getBoolean("AutoTrigger");
			}
			initialise();
		}
		catch (Exception e)
		{
			MobiculeLogger.verbose("MyBookWalk.onCreate"+ e.toString());
		}

	}

	private synchronized void initialise()
	{
		//new MainMenu().startAutotriggerService();
		new ApplicationAsk(MyBookWalk.this, new ApplicationService()
		{
			@Override
			public void postExecute()
			{
				bookwalkListMenu = (ListView) findViewById(android.R.id.list);

				bookwalkListMenu.setOnItemClickListener(MyBookWalk.this);

				bookwalkListMenu.setAdapter(new EfficientAdapter());

			}

			@Override
			public void execute()
			{
				if (autoTriggerOn)
				{
					MobiculeLogger.verbose("Mybookwalk - autotriggerOn");
					//updateMeterReadingOnAutoTrigger(CoreConstants.FIELD_UNATTEMPTED);
					//updateMeterReadingOnAutoTrigger(Constants.FIELD_INCOMPLETE);
				}
				numberOfCount = new ArrayList<String>();
				numberOfCount.add("Total" + " [ " + applicationFacade.calculateBookWalkCountBasedOnStatus() + " ]");
				numberOfCount.add("Unattempted" + " [ " + applicationFacade.getUnattemptedCount() + " ]");
				numberOfCount.add("Completed" + " [ " + applicationFacade.getCompletedCount() + " ]");
				numberOfCount.add("Incomplete" + " [ " + applicationFacade.getIncompleteCount() + " ]");
				numberOfCount.add("Search");
			}
		}).execute();
	}

	private class EfficientAdapter extends ArrayAdapter
	{
		public EfficientAdapter()
		{
			super(MyBookWalk.this, R.layout.listview, R.id.TextView01, numberOfCount);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View rows = super.getView(position, convertView, parent);
			listText = (TextView) rows.findViewById(R.id.TextView01);
			listText.setTextSize(18);
			listText.setHeight(50);
			listText.setSingleLine(false);
			return rows;
		}
	}

	public void goHome(View v)
	{
		Intent intent = new Intent(this, MainMenu.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3)
	{
		startActivity(numberOfCount.get(position).toString(), MyBookWalk.this);
	}

	public void startActivity(String option, Context context)
	{
		if (option.contains("Unattempted"))
		{
			BuildingListActivity.STATUS = Constants.FIELD_UNATTEMPTED;
			startActivity(new Intent(context, BuildingListActivity.class));
		}
		else if (option.contains("Completed"))
		{
			BuildingListActivity.STATUS = Constants.FIELD_COMPLETED;
			Constants.COMPLTED_BOOKWALK = true;
			startActivity(new Intent(context, BuildingListActivity.class));
		}
		else if (option.contains("Incomplete"))
		{
			BuildingListActivity.STATUS = Constants.FIELD_INCOMPLETE;
			startActivity(new Intent(context, BuildingListActivity.class));
		}
		else if (option.contains("Search"))
		{
			startActivity(new Intent(context, SearchInputActivity.class));
		}
	}

	public void updateMeterReadingOnAutoTrigger(String status)
	{
		Response response = applicationFacade.getBuildingList(status);
		Vector<String> data = new Vector<String>();
		Vector<String> customerData = new Vector<String>();
		if (response.isSuccess())
		{
			data = (Vector<String>) response.getData();
		}
		for (int i = 0; i < data.size(); i++)
		{
			jsonParser.setJson(data.elementAt(i).toString());
			String connObj = jsonParser.getValue(IMeterReading.KEY_CONN_OBJ);
			meterReadingBO.reset();

			meterReadingBO.setConnObj(connObj);
			if (status.equalsIgnoreCase(CoreConstants.FIELD_UNATTEMPTED))
			{
				response = applicationFacade.getCustomerListBasedOnConnObj(connObj, status);
			}
			else if (status.equalsIgnoreCase(Constants.FIELD_INCOMPLETE))
			{
				response = applicationFacade.getCustomerListBasedOnConnObjFromSavedMeterReading(connObj, status);
			}

			if (response.isSuccess())
			{
				customerData = (Vector<String>) response.getData();
				for (int j = 0; j < customerData.size(); j++)
				{
					jsonParser.setJson(customerData.elementAt(j).toString());
					MobiculeLogger.verbose("customer	:" + j + "	:data	:" + customerData.elementAt(j).toString());
					meterReadingBO.setMroNumber(jsonParser.getValue(IMeterReading.KEY_MRO_NO));
					meterReadingBO.setBpNumber(jsonParser.getValue(IMeterReading.KEY_BP_NO));
					meterReadingBO.setMeterNumber(jsonParser.getValue(IMeterReading.KEY_METER_NO));
					meterReadingBO.setCustomerContactNo(jsonParser.getValue(IMeterReading.KEY_CUSTOMER_CONTACT));
					meterReadingBO.setCustomerName(jsonParser.getValue(IMeterReading.KEY_CUSTOMER_NAME));
					meterReadingBO.setCustomerAddress(jsonParser.getValue(IMeterReading.KEY_CUSTOMER_ADDRESS));
					meterReadingBO.setCaNo(jsonParser.getValue(Constants.KEY_CUSTOMER_CA_NUMBER));
					meterReadingBO.setMrReason(CoreConstants.MSG_FAILED_TO_TAKE_MR);
					meterReadingBO.setStatus(Constants.FIELD_COMPLETED);
					meterReadingBO.setLock("1");
					meterReadingBO.setImage("");
					meterReadingBO.setArea("");
					IReadings reading = new DefaultMeterReading.DefaultReadings();
					SimpleDateFormat dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);

					String readingTime = dateTime.format(new Date(System.currentTimeMillis()));
					String time = readingTime.substring(11);
					String date = readingTime.substring(0, 10);

					reading.setReadingTime(time);
					reading.setDate(date);
					reading.setMrCode(CoreConstants.FAILED_MR_CODE);
					reading.setMeterReading("");
					meterReadingBO.setReadings(reading);

					//int noOfAttempts = Integer.parseInt(meterReadingBO.getNoOfAttempts()) + 1;
					MobiculeLogger.verbose("My bookwalk *******   "+
							"No of Attempts   get method  *********in json  "
									+ jsonParser.getValue(CoreConstants.KEY_NO_OF_ATTEMPTS));
					int noOfAttempts;
					if (!StringUtil.isValid(jsonParser.getValue(CoreConstants.KEY_NO_OF_ATTEMPTS)))
					{
						noOfAttempts = 1;
						meterReadingBO.setNoOfAttempts(String.valueOf(noOfAttempts));
					}
					else
					{
						noOfAttempts = Integer.parseInt(jsonParser.getValue(CoreConstants.KEY_NO_OF_ATTEMPTS)) + 1;
						meterReadingBO.setNoOfAttempts(String.valueOf(noOfAttempts));
					}
					//int noOfAttempts = Integer.parseInt(meterReadingBO.getNoOfAttempts()) + 1;
					//meterReadingBO.setNoOfAttempts(String.valueOf(noOfAttempts));
					MobiculeLogger.verbose("My bookwalk *******   "+
							"No of Attempts   get method  *********  " + meterReadingBO.getNoOfAttempts());
					//Log.e("My bookwalk", "json	:"+meterReadingBO.toJSON().toString())
					meterReadingFacade.saveMeterReading(false);
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			startActivity(new Intent(this, MainMenu.class));
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
	}
}
