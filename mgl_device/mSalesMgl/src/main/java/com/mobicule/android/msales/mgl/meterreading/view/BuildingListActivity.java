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
package com.mobicule.android.msales.mgl.meterreading.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mahanagar.R;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.mybookwalk.view.MyBookWalk;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
* 
* <enter description here>
*
* @author Sathya Sheela
* @see 
*
* @createdOn Mar 6, 2012
* @modifiedOn
*
* @copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class BuildingListActivity extends DefaultMeterReadingActivity
{
	private ListView bookwalkList;

	private static SimpleAdapter adapter;

	private static ArrayList<HashMap<String, String>> list;

	private String[] headerList = new String[] { "con_name", "con_address", "con_location" };

	private int[] resource = new int[] { R.id.txt_user_info_header, R.id.txt_user_info_detail,
			R.id.txt_user_info_flatno };

	private String connObj = Constants.FIELD_CONN_OBJ;

	public static String STATUS = Constants.FIELD_UNATTEMPTED;

	private Vector buildingList;

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

		list = new ArrayList<HashMap<String, String>>();

		adapter = new SimpleAdapter(this, list, R.layout.userinfo_row, headerList, resource);

		bookwalkList = (ListView) findViewById(android.R.id.list);

		/*bookwalkList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3)
			{
				new ApplicationAsk(BuildingListActivity.this, new ApplicationService()
				{

					@Override
					public void postExecute()
					{
						Intent intent = new Intent(BuildingListActivity.this, CustomerInformation.class);
						startActivity(intent);
					}

					@Override
					public void execute()
					{
						jsonParser.setJson(buildingList.elementAt(position).toString());
						String connObj = jsonParser.getValue(Constants.FIELD_CONN_OBJ);
						meterReadingBO.setConnObj(connObj);

						response = applicationFacade.getCustomerListBasedOnConnObj(connObj, STATUS);
					}
				}).execute();
			}
		});
		initialise();
		}*/

		bookwalkList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3)
			{
				jsonParser.setJson(buildingList.elementAt(position).toString());
				String connObj = jsonParser.getValue(Constants.FIELD_CONN_OBJ);

				Intent intent = new Intent(BuildingListActivity.this, CustomerInformation.class);
				intent.putExtra("connObj", connObj);
				startActivity(intent);
			}
		});
		initialise();
	}

	private void initialise()
	{
		new ApplicationAsk(BuildingListActivity.this, new ApplicationService()
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
				response = applicationFacade.getBuildingList(STATUS);
			}
		}).execute();
	}

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
				temp.put(connObj, jsonParser.getValue(Constants.FIELD_CONN_OBJ));
				list.add(temp);
			}
		}
		else
		{
			AlertDialog.Builder buildingListDialog = new AlertDialog.Builder(BuildingListActivity.this);
			buildingListDialog.setTitle("Connection Details");
			buildingListDialog.setMessage("Sorry! No data found.");
			buildingListDialog.setCancelable(false);
			buildingListDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
					startActivity(new Intent(BuildingListActivity.this, MyBookWalk.class));
					finish();
				}
			});
			buildingListDialog.show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			startActivity(new Intent(this, MyBookWalk.class));
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
	}
}
