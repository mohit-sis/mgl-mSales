package com.mobicule.android.msales.mgl.savedmetereading.view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.json.me.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mahanagar.R;
import com.mobicule.android.msales.mgl.meterreading.view.BuildingListActivity;
import com.mobicule.android.msales.mgl.meterreading.view.Summary;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.android.msales.mgl.util.FileUtil;
import com.mobicule.component.util.CoreConstants;

/**
* 
* <enter description here>
*
* @author Sathya Sheela
* @see 
*
* @createdOn April 6, 2012
* @modifiedOn
*
* @copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class SavedCustomerInformation extends DefaultSavedMeterReadingActivity
{
	private SimpleAdapter adapter;

	private ArrayList<HashMap<String, String>> customerArraylist;

	private ListView customerinfo;

	private String[] header_list = new String[] { "customer_name", "address", Constants.FIELD_BP_NO,
			Constants.KEY_MRO_NUMBER };

	private int[] resources = new int[] { R.id.txt_user_info_header, R.id.txt_user_info_detail,
			R.id.txt_user_info_flatno, R.id.txt_user_info_status };

	private Vector customerList;

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

		setContentView(R.layout.customer_info);

		customerinfo = (ListView) findViewById(android.R.id.list);

		customerArraylist = new ArrayList<HashMap<String, String>>();
		adapter = new SimpleAdapter(this, customerArraylist, R.layout.userinfo_row_search, header_list, resources);

		customerinfo.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				try
				{
					jsonParser.setJson(customerList.elementAt(position).toString());
					String mroNo = jsonParser.getValue(CoreConstants.FIELD_MRO_NO);
					Vector savedMeterReading = meterReadingFacade.fetchSavedMeterReading(mroNo);
					if (savedMeterReading != null && savedMeterReading.size() > 0)
					{
						JSONObject jsonObject = new JSONObject(savedMeterReading.elementAt(0).toString());
						meterReadingFacade.initMeterReadingCycleWithSavedMeterReading(jsonObject.toString());
						Intent intent = new Intent(SavedCustomerInformation.this, Summary.class);
						FileUtil.writeToFile("//SavedCustomerInformation to start summary : meterReadingBO :: " + meterReadingBO.toJSON().toString());

						intent.putExtra("menu", false);
						intent.putExtra("TAG", "SavedMeterReading");
						startActivity(intent);
					}
					else
					{
						AlertDialog.Builder savedSalesOrderDialog = new AlertDialog.Builder(
								SavedCustomerInformation.this);
						savedSalesOrderDialog.setCancelable(false);
						savedSalesOrderDialog.setTitle("Saved Meter Reading");
						savedSalesOrderDialog.setMessage("No Saved Meter Reading Found.");
						savedSalesOrderDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
								startActivity(new Intent(SavedCustomerInformation.this, SavedBuildingListActivity.class));
								finish();
							}
						});
						savedSalesOrderDialog.show();
					}
				}
				catch (Exception e)
				{
					handler.logCrashReport(e);
					e.printStackTrace();
				}
			}
		});

		customerinfo.setAdapter(adapter);
		populateList();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	private void populateList()
	{
		if (response != null && response.getData() != null)
		{
			HashMap<String, String> temp;
			customerArraylist.clear();
			customerList = (Vector) response.getData();
			for (int i = 0; i < customerList.size(); i++)
			{
				jsonParser.setJson(customerList.elementAt(i).toString());
				temp = new HashMap<String, String>();
				temp.put(header_list[0], jsonParser.getValue(Constants.FIELD_CUSTOMER_NAME));
				temp.put(header_list[1], jsonParser.getValue(Constants.FIELD_ADDRESS));
				temp.put(header_list[2], "BP Number: " + jsonParser.getValue(Constants.FIELD_BP_NO));
				temp.put(header_list[3], "MRO Number: " + jsonParser.getValue(Constants.KEY_MRO_NUMBER));
				customerArraylist.add(temp);
			}
		}
		else
		{
			AlertDialog.Builder customerInfoDialog = new AlertDialog.Builder(SavedCustomerInformation.this);
			customerInfoDialog.setCancelable(false);
			customerInfoDialog.setTitle("Customer Details");
			customerInfoDialog.setMessage("Sorry! No data found.");
			customerInfoDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
					startActivity(new Intent(SavedCustomerInformation.this, BuildingListActivity.class));
					finish();
				}
			});
			customerInfoDialog.show();
		}
	}
}
