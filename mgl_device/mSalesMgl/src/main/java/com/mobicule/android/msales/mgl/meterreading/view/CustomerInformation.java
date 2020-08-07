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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.component.util.CoreConstants;

import org.json.me.JSONException;
import org.json.me.JSONObject;

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
public class CustomerInformation extends DefaultMeterReadingActivity implements OnClickListener
{
	//private SimpleAdapter adapter;

	private ArrayList<HashMap<String, String>> customerArraylist, searchedCustomerList;

	private ListView customerinfo;

	private String[] header_list = new String[] { "customer_name", "address", Constants.FIELD_BP_NO,
			Constants.KEY_CUSTOMER_METER_NUMBER, Constants.KEY_MESSAGE_TO_MR };

	private int[] resources = new int[] { R.id.txt_user_info_header, R.id.txt_user_info_detail,
			R.id.txt_user_info_flatno, R.id.txt_user_info_status, R.id.txt_user_info_messagetometer };

	private Vector customerList, customerListInEachLoad, searchedCustomers;

	private ArrayList<String> searchedList;

	private static String connObj;

	private EditText et_search;

	private ImageView iv_search;

	private CustomCustomerListAdapter customCustomerListAdapter;

	//Pagination
	boolean loadingMore = false;

	boolean allDataFetched = false;

	boolean isSearchOn = false;

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

		setContentView(R.layout.customer_info);

		customerinfo = (ListView) findViewById(android.R.id.list);

		iv_search = (ImageView) findViewById(R.id.iv_search);
		iv_search.setOnClickListener(CustomerInformation.this);

		customerArraylist = new ArrayList<HashMap<String, String>>();

		searchedCustomerList = new ArrayList<HashMap<String, String>>();

		customerList = new Vector<Object>();

		searchedCustomers = new Vector<Object>();

		searchedList = new ArrayList<String>();

		//adapter = new SimpleAdapter(this, customerArraylist, R.layout.userinfo_row_search, header_list, resources);

		meterReadingBO.reset();

		meterReadingBO.resetReadingList();

		meterReadingFacade.getMeterReading().reset();

		Constants.searchSelection = false;

		Constants.searchUpdate = false;

		if (getIntent().hasExtra("connObj"))
		{
			connObj = getIntent().getExtras().getString("connObj");
		}

		if (connObj != null && connObj.equals(""))
		{
			MobiculeLogger.verbose("Pagination - if ");
		}
		else
		{
			MobiculeLogger.verbose("Pagination - else ");

			customerinfo.setOnScrollListener(new OnScrollListener()
			{

				public void onScrollStateChanged(AbsListView view, int scrollState)
				{
					MobiculeLogger.debug("onScrollStateChanged");
				}

				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
				{

					MobiculeLogger.debug("onScroll()");
					int lastInScreen = firstVisibleItem + visibleItemCount;

					MobiculeLogger.debug("onScroll() - lastInScreen: " + lastInScreen + " totalItemCount: "
							+ totalItemCount + " visibleItemCount: " + visibleItemCount);

					if ((lastInScreen == totalItemCount) && !(loadingMore))
					{
						MobiculeLogger.debug("onScroll() : In If : last == total");
						if (!allDataFetched)
						{
							MobiculeLogger.debug("onScroll() : In If : !allDataFetched");
							if (!isSearchOn)
							{
								MobiculeLogger.debug("onScroll() : In If : !isSearchOn()");
								loadingMore = true;
								initialise(totalItemCount);
							}
						}

					}
				}
			});
		}

		setOnItemClickListener();

		//initialise();

		et_search = (EditText) findViewById(R.id.et_search);
		et_search.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				//CustomerInformation.this.adapter.getFilter().filter(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				//getSearchCustomerOnData(s.toString());
				MobiculeLogger.verbose("afterTextChanged...");
				getSearchedCustomerDataFromDB(s.toString());
			}
		});

		/*adapter.setViewBinder(new SimpleAdapter.ViewBinder() {


			@Override
			public boolean setViewValue(View view, Object data, String textRepresentation)
			{

				JSONObject json = new JSONObject(data.toString());
		               String description = json.getString(Constants.FIELD_CUSTOMER_NAME);
		               int startPos = description.indexOf(textRepresentation);
		               int endPos = startPos + textRepresentation.length();
		               if (startPos != -1) // This should always be true, just a sanity check
		               {boolean isSboolean isSearhOnboolean isSearhOnearhOn
		                   Spannable spannable = new SpannableString(description);
		                   ColorStateList blueColor = new ColorStateList(new int[][] { new int[] {}}, new int[] { Color.BLUE });
		                   TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
		                   spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		                   view
		                   return true;
		               }
		               else
		               {
		                   ((TextView) view).setText(description);
		                   return true;
		               }boolean isSboolean isSearhOnearhOn

		           return false;
			}
		});*/

	}

	private void initialise(int count)
	{

		final int noOfRecordsFrom;

		if (count == 0)
		{
			noOfRecordsFrom = 0;
		}
		else
		{
			noOfRecordsFrom = count;
		}

		final int noOfRecordsTo = noOfRecordsFrom + 5;

		/*new ApplicationAsk(CustomerInformation.this, new ApplicationService()
		{


			@Override
			public void postExecute()
			{

				MobiculeLogger.debug("noOfRecordsFrom before notify " + noOfRecordsFrom);

				populateList();

				*//*customerinfo.setAdapter(customCustomerListAdapter);
				customCustomerListAdapter.notifyDataSetChanged();*//*

				MobiculeLogger.debug("noOfRecordsFrom after notify " + noOfRecordsFrom);

				if ((noOfRecordsFrom - 2) >= 0)
				{
					customerinfo.setSelection(noOfRecordsFrom - 2);
				}

				loadingMore = false;
			}

			@Override
			public void execute()
			{

				meterReadingBO.setConnObj(connObj);
				Constants.JoinConnObj = connObj;
				response = applicationFacade.getCustomerListBasedOnConnObj(connObj, BuildingListActivity.STATUS,
						noOfRecordsFrom, noOfRecordsTo);

			}
		}).execute();*/


				/*new Thread(new Runnable() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {*/
		new ApplicationAsk(CustomerInformation.this, new ApplicationService()
		{


			@Override
			public void postExecute()
			{

				MobiculeLogger.debug("noOfRecordsFrom before notify " + noOfRecordsFrom);

				populateList();

				/*customerinfo.setAdapter(customCustomerListAdapter);
				customCustomerListAdapter.notifyDataSetChanged();*/

				MobiculeLogger.debug("noOfRecordsFrom after notify " + noOfRecordsFrom);

				if ((noOfRecordsFrom - 2) >= 0)
				{
					customerinfo.setSelection(noOfRecordsFrom - 2);
				}

				loadingMore = false;
			}

			@Override
			public void execute()
			{

				meterReadingBO.setConnObj(connObj);
				Constants.JoinConnObj = connObj;
				response = applicationFacade.getCustomerListBasedOnConnObj(connObj, BuildingListActivity.STATUS,
						noOfRecordsFrom, noOfRecordsTo);

			}
		}).execute();
							/*}
						});
					}

				}).start();*/

	}

	private void getSearchedCustomerDataFromDB(final String searchKey)
	{
		MobiculeLogger.verbose("getSearchedCustomerDataFromDB : searchKey -> "+searchKey);
		if(searchKey.equals(""))
		{
			MobiculeLogger.verbose("getSearchedCustomerDataFromDB : In If ");
			isSearchOn = false;
		}
		else
		{
			MobiculeLogger.verbose("getSearchedCustomerDataFromDB : In Else ");
			isSearchOn = true;
		}
		new ApplicationAsk(CustomerInformation.this, new ApplicationService()
		{


			@Override
			public void postExecute()
			{

				if(isSearchOn)
				{
					MobiculeLogger.verbose("getSearchedCustomerDataFromDB : In If search ON");
					populateSearchedDataList(searchKey);
				}
				else
				{
					MobiculeLogger.verbose("getSearchedCustomerDataFromDB : In Else");
					customCustomerListAdapter = new CustomCustomerListAdapter(CustomerInformation.this, customerArraylist, "");
					customerinfo.setAdapter(customCustomerListAdapter);
				}

			}

			@Override
			public void execute()
			{

				meterReadingBO.setConnObj(connObj);
				Constants.JoinConnObj = connObj;
				response = applicationFacade.getSearchedCustomerDataFrmDB(connObj, searchKey, BuildingListActivity.STATUS);
				MobiculeLogger.verbose("getSearchedCustomerDataFromDB : response -> "+response.toString());

			}
		}).execute();

	}

	private void getSearchCustomerOnData(String searchKey)
	{
		try
		{
			if(searchKey.equals(""))
			{
				isSearchOn = false;
			}
			else
			{
				isSearchOn = true;
			}
			MobiculeLogger.verbose("//SearchKey :: " + searchKey);

			int startPos, endPos;

			customerArraylist.clear();
			searchedList.clear();

			MobiculeLogger.verbose("getSearchCustomerOnData : customerArraylist length -> " + customerArraylist.size());
			MobiculeLogger.verbose("getSearchCustomerOnData : searchedList length -> " + searchedList.size());

			for (int i = 0; i < customerList.size(); i++)
			{

				JSONObject json = new JSONObject(customerList.elementAt(i).toString());

				if (json.getString(Constants.FIELD_CUSTOMER_NAME).toLowerCase().contains(searchKey.toLowerCase())
						|| json.getString(Constants.KEY_BP_NUMBER).toLowerCase().contains(searchKey.toLowerCase())
						|| json.getString(Constants.FIELD_METER_NO).toLowerCase().contains(searchKey.toLowerCase())
						|| json.getString(Constants.FIELD_FLAT_NO).toLowerCase().contains(searchKey.toLowerCase())
						|| json.getString(Constants.FIELD_BUILDING_NAME).toLowerCase()
						.contains(searchKey.toLowerCase())
						|| json.getString(Constants.KEY_CUSTOMER_CONTACT_NUMBER).toLowerCase()
						.contains(searchKey.toLowerCase())
						|| json.getString(Constants.KEY_CUSTOMER_LANDLINE_NUMBER).toLowerCase()
						.contains(searchKey.toLowerCase())
						|| json.getString(Constants.KEY_CUSTOMER_OFFICE_NUMBER).toLowerCase()
						.contains(searchKey.toLowerCase()))
				{

					HashMap<String, String> temp = new HashMap<String, String>();
					temp.put(header_list[0], json.getString(Constants.FIELD_CUSTOMER_NAME));
					temp.put(header_list[1], json.getString(Constants.FIELD_ADDRESS));
					temp.put(header_list[2], "BP Number: " + json.getValue(Constants.FIELD_BP_NO));
					temp.put(header_list[3], "Meter Number : "
							+ json.getValue(Constants.KEY_CUSTOMER_METER_NUMBER).toString());

					if (json.getValue(Constants.KEY_MESSAGE_TO_MR) != null)
					{
						temp.put(header_list[4],
								"Message to Meter Reader: " + json.getValue(Constants.KEY_MESSAGE_TO_MR).toString());
					}
					else
					{
						temp.put(header_list[4], "Message to Meter Reader: ");
					}

					MobiculeLogger.verbose("CustomerList" + customerArraylist);

					customerArraylist.add(temp);
					searchedList.add(customerList.elementAt(i).toString());

				}

			}

			if (customerArraylist.size() == 0)
			{
				Toast.makeText(CustomerInformation.this, "No Result Found", Toast.LENGTH_SHORT).show();
			}

			MobiculeLogger.verbose("getSearchCustomerOnData : customerArraylist length -> " + customerArraylist.size());
			MobiculeLogger.verbose("getSearchCustomerOnData : searchedList length -> " + searchedList.size());

			customCustomerListAdapter = new CustomCustomerListAdapter(CustomerInformation.this, customerArraylist,
					searchKey);
			customerinfo.setAdapter(customCustomerListAdapter);
			customCustomerListAdapter.notifyDataSetChanged();
			//adapter.notifyDataSetChanged();

		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	private void initialise()
	{
		new ApplicationAsk(CustomerInformation.this, new ApplicationService()
		{

			@Override
			public void postExecute()
			{
				//customerinfo.setAdapter(adapter);
				populateList();
			}

			@Override
			public void execute()
			{
				meterReadingBO.setConnObj(connObj);
				Constants.JoinConnObj = connObj;
				response = applicationFacade.getCustomerListBasedOnConnObj(connObj, BuildingListActivity.STATUS);
			}
		}).execute();
	}

	private void setOnItemClickListener()
	{
		customerinfo.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				try
				{
					if (BuildingListActivity.STATUS.equals(Constants.FIELD_UNATTEMPTED))
					{
						/*Intent intent = new Intent(CustomerInformation.this, CustomerSummary.class);
						intent.putExtra("selectedCustomerJSON", customerList.elementAt(position).toString());
						startActivity(intent);*/

						if(isSearchOn)
						{
							Intent intent = new Intent(CustomerInformation.this, CustomerSummary.class);
							intent.putExtra("selectedCustomerJSON", searchedList.get(position).toString());
							startActivity(intent);
						}
						else
						{
							Intent intent = new Intent(CustomerInformation.this, CustomerSummary.class);
							intent.putExtra("selectedCustomerJSON", customerList.elementAt(position).toString());
							startActivity(intent);
						}
					}
					else if (BuildingListActivity.STATUS.equals(Constants.FIELD_INCOMPLETE))
					{
						//jsonParser.setJson(customerList.elementAt(position).toString());
						if(isSearchOn)
						{
							jsonParser.setJson(searchedList.get(position).toString());
						}
						else
						{
							jsonParser.setJson(customerList.elementAt(position).toString());
						}

						//String bpNo = jsonParser.getValue(Constants.FIELD_BP_NO);
						String mroNo = jsonParser.getValue(CoreConstants.FIELD_MRO_NO);
						Vector savedMeterReading = meterReadingFacade.fetchSavedMeterReading(mroNo);
						if (savedMeterReading != null && savedMeterReading.size() > 0)
						{
							JSONObject jsonObject = new JSONObject(savedMeterReading.elementAt(0).toString());
							meterReadingFacade.initMeterReadingCycleWithSavedMeterReading(jsonObject.toString());
							/*if (BuildingListActivity.STATUS.equals(Constants.FIELD_COMPLETED))
							{
								//								Intent intent = new Intent(CustomerInformation.this, Summary.class);
								//								startActivity(intent);
							}
							else
							{*/
							if(isSearchOn)
							{
								Intent intent = new Intent(CustomerInformation.this, CustomerSummary.class);
								intent.putExtra("selectedCustomerJSON", searchedList.get(position).toString());
								startActivity(intent);
							}
							else
							{
								Intent intent = new Intent(CustomerInformation.this, CustomerSummary.class);
								intent.putExtra("selectedCustomerJSON", customerList.elementAt(position).toString());
								startActivity(intent);
							}
							//							}
						}
						else
						{
							AlertDialog.Builder savedSalesOrderDialog = new AlertDialog.Builder(
									CustomerInformation.this);
							savedSalesOrderDialog.setTitle("Saved Meter Reading");
							savedSalesOrderDialog.setMessage("No Saved Meter Reading Found.");
							savedSalesOrderDialog.setCancelable(false);
							savedSalesOrderDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									dialog.dismiss();
									startActivity(new Intent(CustomerInformation.this, BuildingListActivity.class));
									finish();
								}
							});
							savedSalesOrderDialog.show();
						}
					}
					else if (BuildingListActivity.STATUS.equals(Constants.FIELD_COMPLETED))
					{
						if(isSearchOn)
						{
							Intent intent = new Intent(CustomerInformation.this, CustomerSummary.class);
							intent.putExtra("updateCompletedCustomerBySearch", searchedList.get(position).toString());
							startActivity(intent);
						}
						else
						{
							Intent intent = new Intent(CustomerInformation.this, CustomerSummary.class);
							intent.putExtra("updateCompletedCustomerBySearch", customerList.elementAt(position).toString());
							startActivity(intent);
						}
					}

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	private void populateSearchedDataList(String searchKey)
	{
		searchedCustomers.clear();
		searchedList.clear();
		searchedCustomerList.clear();


		if (response != null && response.getData() != null)
		{
			MobiculeLogger.verbose("populateSearchedDataList : In If");
			MobiculeLogger.verbose("Response------------------------" + response.toString());
			HashMap<String, String> temp;

			searchedCustomers.clear();
			searchedList.clear();
			searchedCustomerList.clear();
			searchedCustomers = (Vector) response.getData();

			MobiculeLogger.verbose("populateSearchedDataList : searchedCustomers.size() -> "+searchedCustomers.size());
			//searchedDataList = new ArrayList<HashMap<String, String>>();
			for (int i = 0; i < searchedCustomers.size(); i++)
			{
				//customerList.add(searchedCustomers.elementAt(i).toString());
				searchedList.add(searchedCustomers.elementAt(i).toString());
				MobiculeLogger.verbose("customerListInEachLoad-------------" + searchedCustomers.toString());
				jsonParser.setJson(searchedCustomers.elementAt(i).toString());
				temp = new HashMap<String, String>();
				temp.put(header_list[0], jsonParser.getValue(Constants.FIELD_CUSTOMER_NAME));
				temp.put(header_list[1], jsonParser.getValue(Constants.FIELD_ADDRESS));
				temp.put(header_list[2], "BP Number: " + jsonParser.getValue(Constants.FIELD_BP_NO));

				temp.put(header_list[3], "Meter Number : "
						+ jsonParser.getValue(Constants.KEY_CUSTOMER_METER_NUMBER).toString());
				if (jsonParser.getValue(Constants.KEY_MESSAGE_TO_MR) != null)
				{
					temp.put(header_list[4],
							"Message to Meter Reader: " + jsonParser.getValue(Constants.KEY_MESSAGE_TO_MR).toString());
				}
				else
				{
					temp.put(header_list[4], "Message to Meter Reader: ");
				}

				//customerArraylist.add(temp);
				searchedCustomerList.add(temp);
				//```````````searchedList.add(customerList.get(i).toString());

				customCustomerListAdapter = new CustomCustomerListAdapter(CustomerInformation.this, searchedCustomerList,
						searchKey);
				customerinfo.setAdapter(customCustomerListAdapter);

				MobiculeLogger.verbose("populateList : customerArraylist length -> " + customerArraylist.size());
				MobiculeLogger.verbose("populateList : searchedList length -> " + searchedList.size());
			}
			//searchedDataList.addAll(customerArraylist);

		}
		else
		{
			MobiculeLogger.verbose("populateSearchedDataList : In Else");
			if(!isSearchOn)
			{
				if (!(customerArraylist.size() > 0) || !(searchedList.size() > 0))
				{
					startActivity(new Intent(CustomerInformation.this, BuildingListActivity.class));
					finish();
				}
			}
			else
			{
				if(searchedCustomerList.size() == 0)
				{
					Toast.makeText(CustomerInformation.this, "No result found", Toast.LENGTH_SHORT).show();
					customCustomerListAdapter = new CustomCustomerListAdapter(CustomerInformation.this, searchedCustomerList,
							searchKey);
					customerinfo.setAdapter(customCustomerListAdapter);
				}
			}
			/*AlertDialog.Builder customerInfoDialog = new AlertDialog.Builder(CustomerInformation.this);
			customerInfoDialog.setTitle("Customer Details");
			customerInfoDialog.setMessage("Sorry! No data found.");
			customerInfoDialog.setCancelable(false);
			customerInfoDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
					startActivity(new Intent(CustomerInformation.this, BuildingListActivity.class));
					finish();
				}
			});
			customerInfoDialog.show();*/
			if(!isSearchOn)
			{
				allDataFetched = true;
			}
		}

	}

	private void populateList()
	{
		if (response != null && response.getData() != null)
		{
			MobiculeLogger.verbose("Response------------------------" + response.toString());
			HashMap<String, String> temp;
			//customerArraylist.clear();
			//searchedList.clear();
			customerListInEachLoad = (Vector) response.getData();

			//searchedDataList = new ArrayList<HashMap<String, String>>();
			for (int i = 0; i < customerListInEachLoad.size(); i++)
			{
				customerList.add(customerListInEachLoad.elementAt(i).toString());
				MobiculeLogger.verbose("customerListInEachLoad-------------" + customerListInEachLoad.toString());
				jsonParser.setJson(customerListInEachLoad.elementAt(i).toString());
				temp = new HashMap<String, String>();
				temp.put(header_list[0], jsonParser.getValue(Constants.FIELD_CUSTOMER_NAME));
				temp.put(header_list[1], jsonParser.getValue(Constants.FIELD_ADDRESS));
				temp.put(header_list[2], "BP Number: " + jsonParser.getValue(Constants.FIELD_BP_NO));

				temp.put(header_list[3], "Meter Number : "
						+ jsonParser.getValue(Constants.KEY_CUSTOMER_METER_NUMBER).toString());
				if (jsonParser.getValue(Constants.KEY_MESSAGE_TO_MR) != null)
				{
					temp.put(header_list[4],
							"Message to Meter Reader: " + jsonParser.getValue(Constants.KEY_MESSAGE_TO_MR).toString());
				}
				else
				{
					temp.put(header_list[4], "Message to Meter Reader: ");
				}

				customerArraylist.add(temp);
				//searchedDataList.add(temp);
				//searchedList.add(customerList.get(i).toString());

				customCustomerListAdapter = new CustomCustomerListAdapter(CustomerInformation.this, customerArraylist,
						"");
				customerinfo.setAdapter(customCustomerListAdapter);

				MobiculeLogger.verbose("populateList : customerArraylist length -> " + customerArraylist.size());
				MobiculeLogger.verbose("populateList : searchedList length -> " + searchedList.size());
			}
			//searchedDataList.addAll(customerArraylist);

		}
		else
		{
			if (!(customerArraylist.size() > 0))
			{
				startActivity(new Intent(CustomerInformation.this, BuildingListActivity.class));
				finish();
			}
			/*AlertDialog.Builder customerInfoDialog = new AlertDialog.Builder(CustomerInformation.this);
			customerInfoDialog.setTitle("Customer Details");
			customerInfoDialog.setMessage("Sorry! No data found.");
			customerInfoDialog.setCancelable(false);
			customerInfoDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
					startActivity(new Intent(CustomerInformation.this, BuildingListActivity.class));
					finish();
				}
			});
			customerInfoDialog.show();*/
			allDataFetched = true;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			startActivity(new Intent(this, BuildingListActivity.class));
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.iv_search:

				et_search.setText("");
//				populateList();
				isSearchOn = false;
				//adapter.notifyDataSetChanged();
				customCustomerListAdapter.notifyDataSetChanged();

				break;
		}
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
	}
}
