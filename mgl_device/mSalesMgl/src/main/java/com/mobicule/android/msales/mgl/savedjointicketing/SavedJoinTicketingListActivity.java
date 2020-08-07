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
 * @project mSalesMgl_SVN
 */
package com.mobicule.android.msales.mgl.savedjointicketing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.commons.model.IOCContainer;
import com.mobicule.android.msales.mgl.jointicketing.view.DefaultJoinTicketingActivity;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.crashnotifier.IGenericExceptionHandler;
import com.mobicule.msales.mgl.client.jointicketing.IJoinTicketingFacade;
import com.mobicule.versioncontrol.VersionControl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * 
 * <enter description here>
 * 
 * @author Ashish Shukla
 * @see
 * 
 * @createdOn 18-Apr-2014
 * @modifiedOn
 * 
 * @copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
 */
public class SavedJoinTicketingListActivity extends DefaultJoinTicketingActivity implements OnClickListener
{

	private ListView bookwalkList;

	private static SimpleAdapter adapter;

	private static ArrayList<HashMap<String, String>> list;

	private String[] headerList = new String[] { "con_name", "con_address", "con_location" };

	private int[] resource = new int[] { R.id.txt_user_info_header, R.id.txt_user_info_detail,
			R.id.txt_user_info_flatno };

	private String connObj = "";

	private Vector buildingList;

	private Context context;

	protected IJoinTicketingFacade joinTicketingFacade;
	
	private Button submitAll;

	private LinearLayout buttonLayout;
	IGenericExceptionHandler handler;

	@Override
	public void onCreate(Bundle savedInstanceState)
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

		setContentView(R.layout.bookwalksequence);
		context = this;

		joinTicketingFacade = (IJoinTicketingFacade) IOCContainer.getInstance(getApplicationContext()).getBean(
				"DefaultJoinTicketingFacade");


		joinTicketingBO.reset();

		joinTicketingBO.resetReadingList();

		joinTicketingFacade.getJoinTicketing().reset();

		list = new ArrayList<HashMap<String, String>>();

		adapter = new SimpleAdapter(this, list, R.layout.userinfo_row, headerList, resource);
		
		bookwalkList = (ListView) findViewById(android.R.id.list);

		/*
		 * bookwalkList.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> arg0, View arg1,
		 * final int position, long arg3) { new
		 * ApplicationAsk(SavedJoinTicketingListActivity.this, new
		 * ApplicationService() {
		 * 
		 * @Override public void postExecute() { Intent intent = new
		 * Intent(SavedJoinTicketingListActivity.this,
		 * SavedJoinTicketingInformation.class); startActivity(intent); }
		 * 
		 * @Override public void execute() {
		 * jsonParser.setJson(buildingList.elementAt(position).toString());
		 * connObj = jsonParser.getValue(Constants.FIELD_CONN_OBJ);
		 * joinTicketingBO.setConnObj(connObj); response =
		 * joinTicketingFacade.getSavedCustomerList(connObj); } }).execute(); }
		 * });
		 */
		initialise();
		
		buttonLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
		buttonLayout.setVisibility(View.VISIBLE);
		
		submitAll = (Button) findViewById(R.id.submit_all_button);
		submitAll.setOnClickListener(this);
		// handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
	}

	private void initialise()
	{
		new ApplicationAsk(SavedJoinTicketingListActivity.this, new ApplicationService()
		{
			@Override
			public void postExecute()
			{
				populateList();
				bookwalkList.setAdapter(adapter);
			}

			@Override
			public void execute()
			{
				response = joinTicketingFacade.getSavedBuildingList();
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
			// startService(new Intent(new SavedBuildingListActivity(),
			// BackgroundService.class));

			new ApplicationAsk(this, new ApplicationService()
			{
				@Override
				public void postExecute()
				{
					try
					{
						if (response.isSuccess() == false && response.getData() != null)
						{

							JSONObject jsonResp = new JSONObject(response.getData().toString());
							String status = jsonResp.getString(CoreConstants.USER_RESPONSE_STATUS);

							if (status.equalsIgnoreCase(CoreConstants.VERSION_CHANGE))
							{
								String url = jsonResp.getJSONObject(CoreConstants.KEY_DATA).getString(
										VersionControl.KEY_BUILD_URL);

								VersionControl.downloadNewBuild(context, url, false);
							}
						}
						else{
							finish();
						}
					}
					catch (JSONException e)
					{

						e.printStackTrace();
					}

					
					 * if (responseRMR.isSuccess()) {
					 * databaseManager.dropTable(TABLE_RANDOM_METER_READING); }
					 * if (responseUpdate.isSuccess()) {
					 * databaseManager.dropTable(TABLE_CUSTOMER_UPDATE); }
					 

					//finish();
				}

				@Override
				public void execute()
				{
					response = joinTicketingFacade.submitOneJoinTicketingFromRecevier();
					// responseRMR =
					// randomMeterReadingFacade.submitRandomMeterReading();
					// responseUpdate =
					// updateCustomerFacade.updateCustomerDetails();
				}
			}).execute();
		}
		return super.onOptionsItemSelected(item);
	}*/

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		Constants.broadcastDialogContext = this;
	}

	private void populateList()
	{
		if (response != null && response.getData() != null)
		{
			HashMap<String, String> temp;
			list.clear();
			buildingList = (Vector) response.getData();
			for (int i = 0; i < buildingList.size(); i++)
			{
				jsonParser.setJson(buildingList.elementAt(i).toString());
				temp = new HashMap<String, String>();
				temp.put(
						headerList[0],
						jsonParser.getValue(Constants.KEY_CONN_NAME) + " [ "
								+ jsonParser.getValue(Constants.FIELD_CUSTOMER_COUNT) + " ] ");
				temp.put(headerList[1], jsonParser.getValue(Constants.KEY_CONN_ADDRESS));
				temp.put(headerList[2], jsonParser.getValue(Constants.KEY_LOCATION));
				temp.put(Constants.FIELD_CONN_OBJ, jsonParser.getValue(Constants.FIELD_CONN_OBJ));
				list.add(temp);
			}
		}
		else
		{
			AlertDialog.Builder buildingListDialog = new AlertDialog.Builder(SavedJoinTicketingListActivity.this);
			buildingListDialog.setCancelable(false);
			buildingListDialog.setTitle("Saved Join Ticketing");
			buildingListDialog.setMessage("Sorry! No data found.");
			buildingListDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
					finish();
				}
			});
			buildingListDialog.show();
		}
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.submit_all_button)
		{
			// startService(new Intent(new SavedBuildingListActivity(),
						// BackgroundService.class));

			MobiculeLogger.verbose("JoinTicketing: submit all");

						new ApplicationAsk(this, new ApplicationService() {
							@Override
							public void postExecute() {
								try {
									if (response.isSuccess() == false && response.getData() != null) {

										JSONObject jsonResp = new JSONObject(response.getData().toString());
										String status = jsonResp.getString(CoreConstants.USER_RESPONSE_STATUS);

										if (status.equalsIgnoreCase(CoreConstants.VERSION_CHANGE)) {
											String url = jsonResp.getJSONObject(CoreConstants.KEY_DATA).getString(
													VersionControl.KEY_BUILD_URL);

											VersionControl.downloadNewBuild(context, url, false);
										}
									} else {
										finish();
									}
								} catch (JSONException e) {
									handler.logCrashReport(e);
									e.printStackTrace();
								}

								/*
								 * if (responseRMR.isSuccess()) {
								 * databaseManager.dropTable(TABLE_RANDOM_METER_READING); }
								 * if (responseUpdate.isSuccess()) {
								 * databaseManager.dropTable(TABLE_CUSTOMER_UPDATE); }
								 */

								MobiculeLogger.verbose("JoinTicketing: in postExecute");

								//finish();
							}

							@Override
							public void execute() {
								MobiculeLogger.verbose("JoinTicketing: in execute");

								response = joinTicketingFacade.submitOneJoinTicketingFromRecevier();
								// responseRMR =
								// randomMeterReadingFacade.submitRandomMeterReading();
								// responseUpdate =
								// updateCustomerFacade.updateCustomerDetails();
							}
						}).execute();
//						}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		
	}
}
