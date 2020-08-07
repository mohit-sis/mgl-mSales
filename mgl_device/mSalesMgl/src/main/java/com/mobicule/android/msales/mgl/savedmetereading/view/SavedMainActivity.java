/**
 * 
 */
package com.mobicule.android.msales.mgl.savedmetereading.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.IOCContainer;
import com.mobicule.android.msales.mgl.savedjointicketing.SavedJoinTicketingListActivity;
import com.mobicule.android.msales.mgl.updatecustomer.view.SavedUpdateCustomer;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

/**
 * @author nikita
 *
 */
public class SavedMainActivity extends DefaultSavedMeterReadingActivity implements OnItemClickListener
{
	private ListView saved_details;

	private String selectedItem;

	private static SimpleAdapter adapter;

	private static ArrayList<HashMap<String, String>> list;

	private IApplicationFacade applicationFacade;

	private SetIndividualCountThread savedIndividualCountThread;

	private static int savedMeterCount;

	private static int savedRandomCount;

	private static int savedCustomerUpdateCount;
	
	private static int savedJoinCount;
	
	private static int savedOnMCount;

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

		setContentView(R.layout.saved_reading);
		applicationFacade = (IApplicationFacade) IOCContainer.getInstance(this).getBean("ApplicationFacade");
		savedIndividualCountThread = new SetIndividualCountThread();
		saved_details = (ListView) findViewById(android.R.id.list);

		list = new ArrayList<HashMap<String, String>>();
		adapter = new SimpleAdapter(this, list, R.layout.userinfo_rownew, new String[] { "header", "detail",
				"headerCount" }, new int[] { R.id.txt_user_info_header, R.id.txt_user_info_detail,
				R.id.txt_user_info_header_count });

		saved_details.setOnItemClickListener(this);
		saved_details.setAdapter(adapter);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if (!savedIndividualCountThread.isRunning())
		{
			savedIndividualCountThread = new SetIndividualCountThread();
			savedIndividualCountThread.start();
		}
	}

    @Override
    protected void onRestart() {
        super.onRestart();

		final Handler ha=new Handler();
		ha.postDelayed(new Runnable() {

			@Override
			public void run() {
				savedIndividualCountThread = new SetIndividualCountThread();
				savedIndividualCountThread.start();
				ha.postDelayed(this, 1000);
			}
		}, 1000);
    }


    private class SetIndividualCountThread extends Thread
	{
		private int count;

		private boolean isRunning;

		public SetIndividualCountThread()
		{
			MobiculeLogger.verbose("SetSavedCountThread started . . .");
		}

		public boolean isRunning()
		{
			return isRunning;
		}

		@Override
		public void run()
		{
			super.run();
			try
			{
				isRunning = true;

				savedMeterCount = applicationFacade.getSavedCount(CoreConstants.TABLE_SAVED_METER_READING);
				savedRandomCount = applicationFacade.getSavedCount(CoreConstants.TABLE_RANDOM_METER_READING);
				savedCustomerUpdateCount = applicationFacade.getSavedCount(CoreConstants.TABLE_CUSTOMER_UPDATE);
				savedJoinCount = applicationFacade.getSavedCount(CoreConstants.TABLE_SAVED_JOIN_TICKETING);
				savedOnMCount = applicationFacade.getSavedCount(CoreConstants.TABLE_OnM_PLANNING);
				
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						populateList();
					}
				});

			}
			catch (Exception e)
			{
				handler.logCrashReport(e);
				MobiculeLogger.verbose("SetSavedCountThread - run() - " + e.toString());
			}
			MobiculeLogger.verbose("SetSavedCountThread stoped . . .");
			isRunning = false;
		}
	}

	private static void populateList()
	{
		HashMap<String, String> temp = new HashMap<String, String>();
		list.clear();
		temp.put("header", "Meter Reading");
		temp.put("detail", "");
		temp.put("headerCount", "[" + savedMeterCount + "]");
		list.add(temp);

		HashMap<String, String> temp1 = new HashMap<String, String>();
		temp1.put("header", "Random Meter Reading");
		temp1.put("detail", "");
		temp1.put("headerCount", "[" + savedRandomCount + "]");
		list.add(temp1);

		HashMap<String, String> temp2 = new HashMap<String, String>();
		temp2.put("header", "Customer Update");
		temp2.put("detail", "");
		temp2.put("headerCount", "[" + savedCustomerUpdateCount + "]");
		list.add(temp2);
		
		HashMap<String, String> temp3 = new HashMap<String, String>();
		temp3.put("header", "Join Ticketing");
		temp3.put("detail", "");
		temp3.put("headerCount", "[" + savedJoinCount + "]");
		list.add(temp3);
		
		HashMap<String, String> temp4 = new HashMap<String, String>();
		temp4.put("header", "O&M Planning");
		temp4.put("detail", "");
		temp4.put("headerCount", "[" + savedOnMCount + "]");
		list.add(temp4);
		
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long arg3)
	{
		HashMap<String, String> obj = (HashMap<String, String>) parent.getItemAtPosition(position);
		if (obj.get("header").equalsIgnoreCase("Meter Reading"))
		{
			Intent intent = new Intent(SavedMainActivity.this, SavedBuildingListActivity.class);
			startActivity(intent);
		}
		else if (obj.get("header").equalsIgnoreCase("Random Meter Reading"))
		{
			Intent intent = new Intent(SavedMainActivity.this, SavedRandomMeterReadingCustomerInfo.class);
			startActivity(intent);
		}
		else if (obj.get("header").equalsIgnoreCase("Customer Update"))
		{
			Intent intent = new Intent(SavedMainActivity.this, SavedUpdateCustomer.class);
			startActivity(intent);
		}
		else if (obj.get("header").equalsIgnoreCase("Join Ticketing"))
		{
			Intent intent = new Intent(SavedMainActivity.this, SavedJoinTicketingListActivity.class);
			startActivity(intent);
		}
		else if (obj.get("header").equalsIgnoreCase("O&M Planning"))
		{
			Intent intent = new Intent(SavedMainActivity.this, SavedOnMListActivity.class);
			startActivity(intent);
		}
	}

}
