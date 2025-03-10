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
package com.mobicule.android.msales.mgl.search.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.meterreading.view.BuildingListActivity;
import com.mobicule.android.msales.mgl.mybookwalk.view.MyBookWalk;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.component.string.StringUtil;
import com.mobicule.msales.mgl.client.common.Response;

import java.io.File;

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
public class SearchInputActivity extends DefaultSearchActivity implements OnClickListener
{
	private Spinner mysearch;

	private TextView search;

	private String selectedItem;

	private EditText searchInput;

	private Button searchButton;

	private String[] searchType = { "BP Number", "Building Name", "Meter Number", "Contact Number", "Customer Name",
			"Flat Number" };

	private static int SEARCH_DIALOG = 0;

	private final static int SEARCH_DIALOG_BUILDING_NAME = 0;

	private final static int SEARCH_DIALOG_BP_NO = 1;

	private final static int SEARCH_DIALOG_METER_N0 = 2;

	private final static int SEARCH_DIALOG_CONTACT_NO = 3;

	private final static int SEARCH_DIALOG_CUSTOMER_NM = 4;

	private final static int SEARCH_DIALOG_CUSTOMER_FLAT_NO = 5;

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

		setContentView(R.layout.search);

		mysearch = (Spinner) findViewById(R.id.spinner);
		search = (TextView) findViewById(R.id.searchTextview);
		searchInput = (EditText) findViewById(R.id.searchedittext);

		searchButton = (Button) findViewById(R.id.search_button);
		searchButton.setOnClickListener(this);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, searchType);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mysearch.setAdapter(adapter);

		mysearch.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3)
			{

				selectedItem = (String) parent.getItemAtPosition(position);
				search.setVisibility(View.GONE);
				search.setText("");
				searchInput.setVisibility(View.GONE);
				searchInput.setText("");
				if (selectedItem.equals(searchType[0]))//BP No
				{
					search.setVisibility(View.VISIBLE);
					search.setText("Enter BP Number");
					searchInput.setVisibility(View.VISIBLE);
					SEARCH_DIALOG = SEARCH_DIALOG_BP_NO;
				}
				else if (selectedItem.equals(searchType[1]))//Building Nm
				{
					search.setVisibility(View.VISIBLE);
					search.setText("Enter Building Name");
					searchInput.setVisibility(View.VISIBLE);
					SEARCH_DIALOG = SEARCH_DIALOG_BUILDING_NAME;
				}
				else if (selectedItem.equals(searchType[2]))//Meter No
				{
					search.setVisibility(View.VISIBLE);
					search.setText("Enter Meter Number");
					searchInput.setVisibility(View.VISIBLE);
					SEARCH_DIALOG = SEARCH_DIALOG_METER_N0;
				}
				else if (selectedItem.equals(searchType[3]))//Contact No
				{
					search.setVisibility(View.VISIBLE);
					search.setText("Enter Contact Number");
					searchInput.setVisibility(View.VISIBLE);
					SEARCH_DIALOG = SEARCH_DIALOG_CONTACT_NO;
				}
				else if (selectedItem.equals(searchType[4]))//Customer Nm
				{
					search.setVisibility(View.VISIBLE);
					search.setText("Enter Customer Name");
					searchInput.setVisibility(View.VISIBLE);
					SEARCH_DIALOG = SEARCH_DIALOG_CUSTOMER_NM;
				}
				else if (selectedItem.equals(searchType[5]))//Customer flat no
				{
					search.setVisibility(View.VISIBLE);
					search.setText("Enter Customer's Flat Number");
					searchInput.setVisibility(View.VISIBLE);
					SEARCH_DIALOG = SEARCH_DIALOG_CUSTOMER_FLAT_NO;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{

			}

		});

	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add("Search");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getTitle().equals("Search"))
		{
			showDialog(SEARCH_DIALOG);
		}
		return super.onOptionsItemSelected(item);
	}*/

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
			case SEARCH_DIALOG_BUILDING_NAME:
				new ApplicationAsk(SearchInputActivity.this, new ApplicationService()
				{
					@Override
					public void postExecute()
					{
						if (response.isSuccess())
						{
							startActivity(new Intent(SearchInputActivity.this, SearchOutput.class));
						}
						else if(response.getMessage().equals("Please Enter Something."))
						{
							Toast.makeText(SearchInputActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
						}
						else
						{
							Toast.makeText(SearchInputActivity.this, "No Results found", Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void execute()
					{
						if (StringUtil.isValid(searchInput.getText().toString()))
						{
							response = applicationFacade.searchCustomerListBasedOnBuildingName(searchInput.getText()
									.toString());
						}
						else
						{
							response = new Response(false, "Please Enter Something.", null);
						}

					}
				}).execute();
				break;
			case SEARCH_DIALOG_BP_NO:
				new ApplicationAsk(SearchInputActivity.this, new ApplicationService()
				{
					@Override
					public void postExecute()
					{
						if (response.isSuccess())
						{
							startActivity(new Intent(SearchInputActivity.this, SearchOutput.class));
						}
						else if(response.getMessage().equals("Please Enter Something."))
						{
							Toast.makeText(SearchInputActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
						}
						else
						{
							Toast.makeText(SearchInputActivity.this, "No Results found", Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void execute()
					{
						if (StringUtil.isValid(searchInput.getText().toString()))
						{
							response = applicationFacade.getCustomerListBasedOnBPNO(searchInput.getText().toString());
						}
						else
						{
							response = new Response(false, "Please Enter Something.", null);
						}
					}
				}).execute();
				break;
			case SEARCH_DIALOG_CONTACT_NO:
				new ApplicationAsk(SearchInputActivity.this, new ApplicationService()
				{
					@Override
					public void postExecute()
					{
						if (response.isSuccess())
						{
							startActivity(new Intent(SearchInputActivity.this, SearchOutput.class));
						}
						else if(response.getMessage().equals("Please Enter Something."))
						{
							Toast.makeText(SearchInputActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
						}
						else
						{
							Toast.makeText(SearchInputActivity.this, "No Results found", Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void execute()
					{
						if (StringUtil.isValid(searchInput.getText().toString()))
						{
							response = applicationFacade.getCustomerListBasedOnContactNo(searchInput.getText()
									.toString());
						}
						else
						{
							response = new Response(false, "Please Enter Something.", null);
						}
					}
				}).execute();
				break;
			case SEARCH_DIALOG_CUSTOMER_FLAT_NO:
				new ApplicationAsk(SearchInputActivity.this, new ApplicationService()
				{
					@Override
					public void postExecute()
					{
						if (response.isSuccess())
						{
							startActivity(new Intent(SearchInputActivity.this, SearchOutput.class));
						}
						else if(response.getMessage().equals("Please Enter Something."))
						{
							Toast.makeText(SearchInputActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
						}
						else
						{
							Toast.makeText(SearchInputActivity.this, "No Results found", Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void execute()
					{
						if (StringUtil.isValid(searchInput.getText().toString()))
						{
							response = applicationFacade.getCustomerListBasedOnCustomerFlatNo(searchInput.getText()
									.toString());
						}
						else
						{
							response = new Response(false, "Please Enter Something.", null);
						}
					}
				}).execute();
				break;
			case SEARCH_DIALOG_METER_N0:
				new ApplicationAsk(SearchInputActivity.this, new ApplicationService()
				{
					@Override
					public void postExecute()
					{
						if (response.isSuccess())
						{
							startActivity(new Intent(SearchInputActivity.this, SearchOutput.class));
						}
						else if(response.getMessage().equals("Please Enter Something."))
						{
							Toast.makeText(SearchInputActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
						}
						else
						{
							Toast.makeText(SearchInputActivity.this, "No Results found", Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void execute()
					{
						if (StringUtil.isValid(searchInput.getText().toString()))
						{
							response = applicationFacade
									.getCustomerListBasedOnMeterNo(searchInput.getText().toString());
						}
						else
						{
							response = new Response(false, "Please Enter Something.", null);
						}
					}
				}).execute();
				break;
			case SEARCH_DIALOG_CUSTOMER_NM:
				new ApplicationAsk(SearchInputActivity.this, new ApplicationService()
				{
					@Override
					public void postExecute()
					{
						if (response.isSuccess())
						{
							startActivity(new Intent(SearchInputActivity.this, SearchOutput.class));
						}
						else if(response.getMessage().equals("Please Enter Something."))
						{
							Toast.makeText(SearchInputActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
						}
						else
						{
							Toast.makeText(SearchInputActivity.this, "No Results found", Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void execute()
					{
						if (StringUtil.isValid(searchInput.getText().toString()))
						{
							response = applicationFacade.getCustomerListBasedOnCustomerName(searchInput.getText()
									.toString());
						}
						else
						{
							response = new Response(false, "Please Enter Something.", null);
						}
					}
				}).execute();
				break;
			default:
				break;
		}
		return super.onCreateDialog(id);
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.search_button)
		{

			showDialog(SEARCH_DIALOG);
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
}
