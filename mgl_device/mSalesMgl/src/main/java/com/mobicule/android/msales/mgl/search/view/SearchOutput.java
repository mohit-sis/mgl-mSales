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
 * @project mSales
 */
package com.mobicule.android.msales.mgl.search.view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.meterreading.view.BuildingListActivity;
import com.mobicule.android.msales.mgl.meterreading.view.CustomerSummary;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.crashnotifier.GenericExceptionHandler;
import com.mobicule.crashnotifier.IGenericExceptionHandler;

/**
 * 
 * <enter description here>
 * 
 * @author nikita
 * @see
 * 
 * @createdOn 22-Feb-2012
 * @modifiedOn
 * 
 * @copyright © 2010-2011 Mobicule Technologies Pvt. Ltd. All rights reserved.
 */
public class SearchOutput extends DefaultSearchActivity
{
	private ListView search_customer;

	static MyAdapter adapter;

	private TextView listText;

	private HashMap<String, String> statusinfo;

	private ArrayList<HashMap<String, String>> customerlist;

	private Vector<String> customerList;

	private JSONObject customerInfoBySearch;

	private String[] header_list = new String[] { "customer_name", "address", Constants.FIELD_BP_NO, "status" };

	private int[] resources = new int[] { R.id.txt_user_info_header, R.id.txt_user_info_detail,
			R.id.txt_user_info_flatno, R.id.txt_user_info_status };
	
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

		setContentView(R.layout.searchoutput);

		search_customer = (ListView) findViewById(R.id.searchList);

		customerlist = new ArrayList<HashMap<String, String>>();

		adapter = new MyAdapter(this, customerlist, R.layout.userinfo_row_search, header_list, resources);

		Constants.searchSelection = true;

		Constants.searchUpdate = true;
		
	//	handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());

		search_customer.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3)
			{
				statusinfo = (HashMap<String, String>) parent.getItemAtPosition(position);
				
				MobiculeLogger.verbose("Search output statusinfo - statusinfo:  " + statusinfo.toString());
				
				try
				{
					customerInfoBySearch = new JSONObject(customerList.elementAt(position).toString());
					MobiculeLogger.verbose("Search output - json is:  " + customerInfoBySearch);
					String str = statusinfo.get(Constants.FIELD_STATUS);
					
					BuildingListActivity.STATUS = str;
					
					MobiculeLogger.verbose("Search output status - Status:  " + str);
					
					if (str.equalsIgnoreCase(Constants.FIELD_INCOMPLETE)
							|| str.equalsIgnoreCase(Constants.FIELD_UNATTEMPTED))
					{
						
						MobiculeLogger.verbose("Search output intent - selectedCustomerJSON");
						
						Intent intent = new Intent(SearchOutput.this, SearchCustomerSummaryActivity.class);
						intent.putExtra("selectedCustomerJSON", customerInfoBySearch.toString());
						//intent.putExtra("connObj", customerInfoBySearch.getString(Constants.FIELD_CONN_OBJ));
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
					else
					{
						MobiculeLogger.verbose("Search output intent - updateCompletedCustomerBySearch");
						
						Intent intent = new Intent(SearchOutput.this, SearchCustomerSummaryActivity.class);
						intent.putExtra("updateCompletedCustomerBySearch", customerInfoBySearch.toString());
						//intent.putExtra("connObj", customerInfoBySearch.getString(Constants.FIELD_CONN_OBJ));
						startActivity(intent);
					}
				}
				catch (JSONException e)
				{
					handler.logCrashReport(e);
					e.printStackTrace();
				}

			}

		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		search_customer.setAdapter(adapter);
		populateList();
	}

	public class MyAdapter extends SimpleAdapter
	{

		Context context;

		public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
		{
			super(context, data, resource, from, to);
			this.context = context;
		}

		@Override
		public View getView(final int position, View view, ViewGroup parent)
		{
			view = super.getView(position, view, parent);
			if (null == view)
			{
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.userinfo_row_search, null);
			}
			RelativeLayout relBorder = (RelativeLayout) view.findViewById(R.id.relativeLayout);
			TextView name = (TextView) view.findViewById(R.id.txt_user_info_header);
			TextView address = (TextView) view.findViewById(R.id.txt_user_info_detail);
			TextView bpNumber = (TextView) view.findViewById(R.id.txt_user_info_flatno);
			TextView status = (TextView) view.findViewById(R.id.txt_user_info_status);

			name.setVisibility(View.VISIBLE);
			address.setVisibility(View.VISIBLE);
			bpNumber.setVisibility(View.VISIBLE);
			status.setVisibility(View.VISIBLE);

			String statusType = (String) customerlist.get(position).get("status");
			//String str1=statusinfo.get("status");
			MobiculeLogger.verbose("search output - getview  status type is::::    " + statusType);
			//status.setText(statusType);
			name.setTextColor(Color.BLACK);
			address.setTextColor(Color.BLACK);
			bpNumber.setTextColor(Color.BLACK);
			status.setTextColor(Color.BLACK);
			if (statusType.equalsIgnoreCase(Constants.FIELD_INCOMPLETE))
			{
				MobiculeLogger.verbose("Search output - FIELD_INCOMPLETE ");
				name.setTextColor(getResources().getColor(R.color.incompletedField));
				address.setTextColor(getResources().getColor(R.color.incompletedField));
				bpNumber.setTextColor(getResources().getColor(R.color.incompletedField));
				status.setTextColor(getResources().getColor(R.color.incompletedField));
			}
			else if (statusType.equalsIgnoreCase(Constants.FIELD_COMPLETED))
			{
				MobiculeLogger.verbose("Search output - FIELD_COMPLETED ");
				name.setTextColor(Color.RED);
				address.setTextColor(Color.RED);
				bpNumber.setTextColor(Color.RED);
				status.setTextColor(Color.RED);
			}
			/*else if(statusType.equalsIgnoreCase(Constants.FIELD_UNATTEMPTED))
			{
				name.setTextColor(Color.MAGENTA);
				address.setTextColor(Color.MAGENTA);
				bpNumber.setTextColor(Color.MAGENTA);
				status.setTextColor(Color.MAGENTA);
			}*/

			return view;
		}

	}

	private void populateList()
	{
		if (response != null && response.getData() != null)
		{
			HashMap<String, String> temp;
			customerlist.clear();
			customerList = (Vector<String>) response.getData();
			for (int i = 0; i < customerList.size(); i++)
			{
				jsonParser.setJson(customerList.elementAt(i).toString());
				temp = new HashMap<String, String>();
				temp.put(header_list[0], jsonParser.getValue(Constants.FIELD_CUSTOMER_NAME));
				temp.put(header_list[1], jsonParser.getValue(Constants.FIELD_ADDRESS));
				temp.put(header_list[2], "BP Number: " + jsonParser.getValue(Constants.FIELD_BP_NO));
				temp.put(header_list[3], jsonParser.getValue("status"));
				customerlist.add(temp);
			}
		}
		else
		{
			Toast.makeText(SearchOutput.this, "No Results Found", Toast.LENGTH_LONG).show();
		}
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			startActivity(new Intent(this, SearchInputActivity.class));
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
