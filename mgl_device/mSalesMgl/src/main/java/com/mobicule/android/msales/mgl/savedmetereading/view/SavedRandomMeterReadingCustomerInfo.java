/**
 * 
 */
package com.mobicule.android.msales.mgl.savedmetereading.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mahanagar.R;
import com.mobicule.android.component.db.SQLiteDatabaseManager;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.randommeterreading.view.DefaultRandomMeterReadingActivity;
import com.mobicule.android.msales.mgl.randommeterreading.view.RandomMeterReadingSummary;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.crashnotifier.IGenericExceptionHandler;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.versioncontrol.VersionControl;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author nikita
 *
 */
public class SavedRandomMeterReadingCustomerInfo extends DefaultRandomMeterReadingActivity implements OnClickListener
{
	private SimpleAdapter adapter;

	private ArrayList<HashMap<String, String>> customerArraylist;

	private ListView customerinfo;

	private String[] header_list = new String[] { "customer_name", "address", Constants.FIELD_BP_NO };

	private int[] resources = new int[] { R.id.txt_user_info_header, R.id.txt_user_info_detail,
			R.id.txt_user_info_flatno };

	public static final String TABLE_RANDOM_METER_READING = "RANDOM_METER_READING";

	private SQLiteDatabaseManager databaseManager;

	private Vector customerList;

	private String reading;

	private JSONObject meterreading;

	private String statusRMR;

	private Context context;

	private Button submitAll;

	private LinearLayout buttonLayout;
	
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

		setContentView(R.layout.customer_info);
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
					jsonParser.setJson(customerList.elementAt(position).toString());
					String customerName = jsonParser.getValue(Constants.FIELD_CUSTOMER_NAME);
					Vector savedRandomMeterReading = randomMeterReadingFacade
							.fetchSavedRandomMeterReading(customerName);
					if (savedRandomMeterReading != null && savedRandomMeterReading.size() > 0)
					{
						JSONObject jsonObject = new JSONObject(savedRandomMeterReading.elementAt(0).toString());
						randomMeterReadingFacade.initRandomMeterReadingCycleWithSavedRandomMeterReading(jsonObject
								.toString());
						meterreading = (JSONObject) jsonObject.get(Constants.KEY_READINGS);
						reading = meterreading.getValue(Constants.KEY_METER_READING).toString();
						statusRMR = randomMeterReadingBO.getSelectedStatus().toString();
						Intent intent = new Intent(SavedRandomMeterReadingCustomerInfo.this,
								RandomMeterReadingSummary.class);
						intent.putExtra("menu", false);
						intent.putExtra("reading", reading);
						intent.putExtra("status", statusRMR);
						intent.putExtra("TAG", "SavedRandomMeterReading");
						startActivity(intent);
					}
					else
					{
						AlertDialog.Builder savedSalesOrderDialog = new AlertDialog.Builder(
								SavedRandomMeterReadingCustomerInfo.this);
						savedSalesOrderDialog.setCancelable(false);
						savedSalesOrderDialog.setTitle("Saved Random Meter Reading");
						savedSalesOrderDialog.setMessage("No Saved Random Meter Reading Found.");
						savedSalesOrderDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
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
		
		buttonLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
		buttonLayout.setVisibility(View.VISIBLE);

		submitAll = (Button) findViewById(R.id.submit_all_button);
		submitAll.setOnClickListener(this);
	}

	private void initialise()
	{
		new ApplicationAsk(SavedRandomMeterReadingCustomerInfo.this, new ApplicationService()
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
				response = randomMeterReadingFacade.getOfflineRandomMeterReadingDetail();
			}
		}).execute();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
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
				private Response response = null;

				@Override
				public void postExecute()
				{

					if (response.isSuccess() == false && response.getData() != null)
					{

						JSONObject jsonResp;
						try
						{
							jsonResp = new JSONObject(response.getData().toString());

							String status = jsonResp.getString(CoreConstants.USER_RESPONSE_STATUS);

							if (status.equalsIgnoreCase(CoreConstants.VERSION_CHANGE))
							{
								String url = jsonResp.getJSONObject(CoreConstants.KEY_DATA).getString(
										VersionControl.KEY_BUILD_URL);

								VersionControl.downloadNewBuild(context, url, false);
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
					}
					else if (response.isSuccess())
					{
						databaseManager.dropTable(TABLE_RANDOM_METER_READING);
						finish();
					}
					else
					{

						finish();
					}
				}

				@Override
				public void execute()
				{
					//response = meterReadingFacade.submitMeterReading();
					response = randomMeterReadingFacade.submitOneRandomMeterReadingFromReceiver();
					//responseUpdate = updateCustomerFacade.updateCustomerDetails();
				}
			}).execute();
		}
		return super.onOptionsItemSelected(item);
	}
*/
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
				temp.put(header_list[0], "Customer Name: " + jsonParser.getValue(Constants.FIELD_CUSTOMER_NAME));
				temp.put(header_list[1],
						"Contact Number: " + jsonParser.getValue(Constants.KEY_CUSTOMER_CONTACT_NUMBER));
				customerArraylist.add(temp);
			}
		}
		else
		{
			AlertDialog.Builder customerInfoDialog = new AlertDialog.Builder(SavedRandomMeterReadingCustomerInfo.this);
			customerInfoDialog.setCancelable(false);
			customerInfoDialog.setTitle("Saved RandomMeter Reading");
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

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.submit_all_button)
		{

			new ApplicationAsk(this, new ApplicationService()
			{
				private Response response = null;

				@Override
				public void postExecute()
				{

					if (response.isSuccess() == false && response.getData() != null)
					{

						JSONObject jsonResp;
						try
						{
							jsonResp = new JSONObject(response.getData().toString());

							String status = jsonResp.getString(CoreConstants.USER_RESPONSE_STATUS);

							if (status.equalsIgnoreCase(CoreConstants.VERSION_CHANGE))
							{
								String url = jsonResp.getJSONObject(CoreConstants.KEY_DATA).getString(
										VersionControl.KEY_BUILD_URL);

								VersionControl.downloadNewBuild(context, url, false);
							}
						}
						catch (JSONException e)
						{
							handler.logCrashReport(e);
							e.printStackTrace();
						}
					}
					else if (response.isSuccess())
					{
						databaseManager.dropTable(TABLE_RANDOM_METER_READING);
						finish();
					}
					else
					{

						finish();
					}
				}

				@Override
				public void execute()
				{
					//response = meterReadingFacade.submitMeterReading();
					response = randomMeterReadingFacade.submitOneRandomMeterReadingFromReceiver();
					//responseUpdate = updateCustomerFacade.updateCustomerDetails();
				}
			}).execute();
//			}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
	}
}
