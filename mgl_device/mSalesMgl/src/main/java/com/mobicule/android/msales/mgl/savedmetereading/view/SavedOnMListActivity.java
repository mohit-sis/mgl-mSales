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
package com.mobicule.android.msales.mgl.savedmetereading.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mahanagar.R;
import com.mobicule.android.component.db.SQLiteDatabaseManager;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.menu.view.MainMenu;
import com.mobicule.android.msales.mgl.onmplaning.view.DefaultOnMPlanningActivity;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.crashnotifier.IGenericExceptionHandler;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.versioncontrol.VersionControl;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

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
public class SavedOnMListActivity extends DefaultOnMPlanningActivity implements OnClickListener
{

	private SimpleAdapter adapter;

	private ArrayList<HashMap<String, String>> customerArraylist;

	private ListView customerinfo;

	private JSONArray jsonarray;

	private String[] header_list = new String[] { "customer_name", "address", Constants.FIELD_BP_NO };

	private int[] resources = new int[] { R.id.txt_user_info_header, R.id.txt_user_info_detail,
			R.id.txt_user_info_flatno };

	public static final String TABLE_CUSTOMER_UPDATE = "CUSTOMER_UPDATE";

	private ArrayList<String> customerList;

	private SQLiteDatabaseManager databaseManager;

	private JSONObject savedcustomerupdatejsonObject;

	private Context context;
	
	private Button submitAll;

	private TextView tv_headerTitle;
	IGenericExceptionHandler handler;

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


		setContentView(R.layout.activity_saved_onmlist);
		Log.e("SavedOnMListActivity","SavedOnMListActivity");

		context = this;

		customerinfo = (ListView) findViewById(android.R.id.list);
		
		customerArraylist = new ArrayList<HashMap<String, String>>();

		adapter = new SimpleAdapter(this, customerArraylist, R.layout.userinfo_row, header_list, resources);

		databaseManager = (SQLiteDatabaseManager) SQLiteDatabaseManager.getInstance(getApplicationContext(),Constants.DATABASE_NAME);
		
		
		//handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
		
		initialise();

		customerinfo.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				try
				{
					jsonParser.setJson(customerList.get(position).toString());
					//String bpNo = jsonParser.getValue(Constants.FIELD_BP_NO);
					String mroNo = jsonParser.getValue(CoreConstants.FIELD_MRO_NO);
					MobiculeLogger.verbose("mroNo is ***  " + mroNo);
					Vector savedRandomMeterReading = onmFacade.fetchSavedOnM(mroNo);
					if (savedRandomMeterReading != null && savedRandomMeterReading.size() > 0)
					{
						savedcustomerupdatejsonObject = new JSONObject(savedRandomMeterReading.elementAt(0).toString());
						//response = applicationFacade.getCustomerListBasedOnBPNO(mroNo);
						onmFacade.initCustomerCycleWithSavedOnMPlanning(savedcustomerupdatejsonObject
										.toString());
						
						MobiculeLogger.verbose("---- savedcustomerupdatejsonObject : "+savedcustomerupdatejsonObject.toString());
						//String cust_name=savedcustomerupdatejsonObject.getValue(Constants.FIELD_CUSTOMER_NAME).toString();
						/*Bundle b = new Bundle();
						
						// Storing data into bundle
						b.putString("bpNo", savedcustomerupdatejsonObject.getValue(Constants.FIELD_BP_NO).toString());
						b.putString("mroNo", savedcustomerupdatejsonObject.getValue(Constants.KEY_MRO_NUMBER).toString());
						b.putString("time", savedcustomerupdatejsonObject.getValue(Constants.KEY_TIME).toString());
						b.putString("date", savedcustomerupdatejsonObject.getValue(Constants.KEY_DATE).toString());
						b.putString("noOfGasGeysers", savedcustomerupdatejsonObject.getValue(Constants.KEY_NO_OF_GEYSERS).toString());
						b.putString("noOfBurners", savedcustomerupdatejsonObject.getValue(Constants.KEY_NO_OF_BURNERS).toString());
						b.putString("msgRemarks", savedcustomerupdatejsonObject.getValue(Constants.KEY_MSG_REMARKS).toString());
						b.putString("msgFrmMr", savedcustomerupdatejsonObject.getValue(Constants.KEY_MSG_FROM_MR).toString());
						b.putString("image1", savedcustomerupdatejsonObject.getValue(Constants.KEY_IMAGE1).toString());
						b.putString("image2", savedcustomerupdatejsonObject.getValue(Constants.KEY_IMAGE2).toString());
						*/
						
						Intent intent = new Intent(SavedOnMListActivity.this, SavedOnMSummaryActivity.class);
						//intent.putExtra("SavedcustomerUpdate", savedcustomerupdatejsonObject.toString());
						intent.putExtra("mrno",mroNo);
						//intent.putExtras(b);
						startActivity(intent);
					}
					else
					{
						AlertDialog.Builder savedSalesOrderDialog = new AlertDialog.Builder(SavedOnMListActivity.this);
						savedSalesOrderDialog.setCancelable(false);
						savedSalesOrderDialog.setTitle("Saved OnM Planning");
						savedSalesOrderDialog.setMessage("No Saved Data Found.");
						savedSalesOrderDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
								startActivity(new Intent(SavedOnMListActivity.this, MainMenu.class));
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
		
		submitAll = (Button) findViewById(R.id.submit_all_button);
		submitAll.setOnClickListener(this);
	}

	private void initialise()
	{
		new ApplicationAsk(SavedOnMListActivity.this, new ApplicationService()
		{
			@Override
			public void postExecute()
			{
				populateList();
				customerinfo.setAdapter(adapter);
			}

			@Override
			public void execute()
			{
				response = onmFacade.getOfflineOnMPlanningDetail();
			}
		}).execute();
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add("Submit All");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getTitle().equals("Submit All"))
		{

			new ApplicationAsk(this, new ApplicationService()
			{
				Response response = null;

				@Override
				public void postExecute()
				{
					try
					{
						if (response.isSuccess() == false && response.getData() != null)
						{
							String status = new JSONObject(response.getData().toString())
									.getString(CoreConstants.USER_RESPONSE_STATUS);
							if (status.equalsIgnoreCase(CoreConstants.VERSION_CHANGE))
							{
								String url = new JSONObject(response.getData().toString()).getJSONObject(
										CoreConstants.KEY_DATA).getString("url");
								VersionControl.downloadNewBuild(context, url, false);
							}
						}
						else if (response.isSuccess())
						{
							databaseManager.dropTable(TABLE_CUSTOMER_UPDATE);
							finish();
						}
						else{
							finish();
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				//	finish();
				}

				@Override
				public void execute()
				{
					//response = meterReadingFacade.submitMeterReading();
					//responseRMR = randomMeterReadingFacade.submitRandomMeterReading();

					response = updateCustomerFacade.updateCustomerDetails();
				}
			}).execute();
		}
		return super.onOptionsItemSelected(item);
	}
*/
	@Override
	protected void onResume()
	{
		super.onResume();
	}

	private void populateList()
	{
		try
		{
			if (response != null && response.getData() != null)
			{
				jsonarray = new JSONArray(response.getData().toString());
				HashMap<String, String> temp;
				customerArraylist.clear();
				customerList = (ArrayList) response.getData();
				MobiculeLogger.verbose("customerList     " + customerList.toString());
				for (int i = 0; i < customerList.size(); i++)
				{
					MobiculeLogger.verbose("using parser " + jsonParser.toString());
					jsonParser.setJson(customerList.get(i).toString());
					temp = new HashMap<String, String>();
					temp.put(header_list[0], "Customer Name: " + jsonParser.getValue(Constants.FIELD_CUSTOMER_NAME));
					temp.put(header_list[1], "Contact BP Number: " + jsonParser.getValue(Constants.KEY_BP_NUMBER));
					temp.put(header_list[2], "Customer MRO Number: " + jsonParser.getValue(Constants.KEY_MRO_NUMBER));
					customerArraylist.add(temp);
				}
			}
			else
			{
				AlertDialog.Builder customerInfoDialog = new AlertDialog.Builder(SavedOnMListActivity.this);
				customerInfoDialog.setTitle("Saved Customer Updates");
				customerInfoDialog.setCancelable(false);
				customerInfoDialog.setMessage("Sorry! No data found.");
				customerInfoDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						finish();
					}
				});
				customerInfoDialog.show();
			}
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			handler.logCrashReport(e);
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed()
	{
		finish();
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.submit_all_button)
		{
			new ApplicationAsk(this, new ApplicationService() {
				Response response = null;

				@Override
				public void postExecute() {
					try {
						if (response.isSuccess() == false && response.getData() != null) {
							String status = new JSONObject(response.getData().toString())
									.getString(CoreConstants.USER_RESPONSE_STATUS);
							if (status.equalsIgnoreCase(CoreConstants.VERSION_CHANGE)) {
								String url = new JSONObject(response.getData().toString()).getJSONObject(
										CoreConstants.KEY_DATA).getString("url");
								VersionControl.downloadNewBuild(context, url, false);
							}
						} else if (response.isSuccess()) {
							//databaseManager.dropTable(TABLE_CUSTOMER_UPDATE);
							finish();
						} else {
							finish();
						}
					} catch (Exception e) {
						handler.logCrashReport(e);
						e.printStackTrace();
					}
					//	finish();
				}

				@Override
				public void execute() {
					//response = meterReadingFacade.submitMeterReading();
					//responseRMR = randomMeterReadingFacade.submitRandomMeterReading();

					MobiculeLogger.verbose("execute()");
					response = onmFacade.OnMCustomerDetails();
				}
			}).execute();
//			}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		
	}


}
