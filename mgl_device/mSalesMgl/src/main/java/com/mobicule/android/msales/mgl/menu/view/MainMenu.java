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
package com.mobicule.android.msales.mgl.menu.view;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.autotrigger.AutotriggerBroadcastReceiver;
import com.mobicule.android.msales.mgl.commons.model.AlarmReceiver;
import com.mobicule.android.msales.mgl.commons.model.AppApplication;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.commons.model.BackgroundBroadcastReceiver;
import com.mobicule.android.msales.mgl.commons.model.BackgroundService;
import com.mobicule.android.msales.mgl.commons.model.IOCContainer;
import com.mobicule.android.msales.mgl.commons.model.SessionData;
import com.mobicule.android.msales.mgl.commons.view.DialogDisplay;
import com.mobicule.android.msales.mgl.downloadbookwalk.model.DefaultBookWalkSqnceCommunicationService;
import com.mobicule.android.msales.mgl.downloadbookwalk.model.DefaultBookWalkSqncePersistenceService;
import com.mobicule.android.msales.mgl.downloadbookwalk.model.SyncTask;
import com.mobicule.android.msales.mgl.login.view.Login;
import com.mobicule.android.msales.mgl.meterreading.model.DefaultMeterReadingPersistenceService;
import com.mobicule.android.msales.mgl.mybookwalk.view.MyBookWalk;
import com.mobicule.android.msales.mgl.randommeterreading.view.RandomMeterReadingActivity;
import com.mobicule.android.msales.mgl.savedmetereading.view.SavedMainActivity;
import com.mobicule.android.msales.mgl.timevariation.MtService;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.android.msales.mgl.util.Utilities;
import com.mobicule.android.msales.mgl.utilities.Common_Methods;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.downloadbookwalk.implementation.DefaultBookWalkSqnceFacade;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncCommunicationService;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncePersistenceService;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.ISyncFacade;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.ISyncObserver;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginPersistenceService;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IMeterReadingInstance;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingFacade;
import com.mobicule.versioncontrol.VersionControl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

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
public class MainMenu extends Activity implements OnClickListener
{

	private final String TAG = "MainMenu";

	public IApplicationFacade mApplicationFacade;

	private Menu menu;

	private String inProgess = "inProgess", complete = "complete", logout = "Logout", diagnostics = "Diagnostics",
			backUp = "BackUp";

	protected IMeterReadingFacade mMeterReadingFacade;

	protected IMeterReadingInstance mMeterReadingBO;

	private Button myBookWalk;

	private Button randomMeterReading;

	private Button savedReading;

	private Button download_seq;

	private IBookWalkSqncePersistenceService persistenceService;

	private IBookWalkSqncCommunicationService communicationService;

	private static final String timeVariationService = "com.mobicule.android.msales.mgl.timevariation.MtService";

	private IApplicationFacade applicationFacade;

	protected Vector observers = new Vector();

	protected ISyncObserver syncObserver;

	protected ISyncFacade syncFacade;

	private SharedPreferences isSync;

	public static boolean syncDone;

	private Response response;

	private boolean keycode = true;

	private JSONParser jsonParser;

	private long alrmTime, endTime, startTime, diffLong;

	private AlarmManager mAlarms;

	private AlarmReceiver alarmReceiver;

	private static PendingIntent mAlarmIntent;

	private static boolean autoTriggerOn = false;

	private static PendingIntent backgroundIntent;

	private PendingIntent pendingIntent = null;

	public boolean imeiCheckes;

//	private PowerManager.WakeLock wakeLock;

//	private PowerManager powerManager;

	private TextView alertText;

	private DefaultMeterReadingPersistenceService meterReadingPersistance;

	private String manulAutotrigger = "Manual Autotrigger";

	private Context context;

	private SharedPreferences delayPreference;

	private SetSavedCountThread savedCountThread;

	private VersionControl versionControl;

	private ImageButton menuButton;

	View quickMenuView;

	QuickActionBar settings_popup;

	private LinearLayout diagnosticsTextLayout, dbBackupTextLayout, logoutTextLayout;

	@Override
	protected void onStart()
	{
		super.onStart();
		Constants.broadcastDialogContext = this;

	}

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

		setContentView(R.layout.mainmenu);
		Constants.broadcastDialogContext = this;

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		context = this;

		defaultInit();

		this.logOutReceiver();

		myBookWalk.setOnClickListener(this);
		randomMeterReading.setOnClickListener(this);
		savedReading.setOnClickListener(this);
		download_seq.setOnClickListener(this);
		checkStatus(false);

		meterReadingPersistance = new DefaultMeterReadingPersistenceService(this);

		savedCountThread = new SetSavedCountThread();

		imeiCheckes = getIntent().getBooleanExtra("imeiKey", true);
		MobiculeLogger.verbose("Main menu : imeichecks - " + imeiCheckes);
		delayPreference = this.getSharedPreferences(Constants.MSALES_MGL_SHARED_PREFERENCE, Context.MODE_PRIVATE);
		Log.e("delayPreference","delayPreference"+delayPreference);
		registerBackgroundReceiver();
		startTimeVariationService();
		startAutotriggerReceiver();
		batteryOptimization();
	}

	private void batteryOptimization(){

		if (Build.VERSION.SDK_INT >= 26)
		{
			final Intent intent = new Intent();
			final String packageName = getApplicationContext().getPackageName();
			PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
			if (pm.isIgnoringBatteryOptimizations(packageName))
			{
				intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
				intent.setData(Uri.parse("package:" + packageName));
				startActivity(intent);
			}
			else
			{

				AlertDialog.Builder submitDialog = new AlertDialog.Builder(MainMenu.this);
				submitDialog.setCancelable(false);
				submitDialog.setTitle("Battery Optimization");
				submitDialog.setMessage("Please remove mSales From Battery Optimization.");
				submitDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();

					}
				});

				submitDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
								intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
								startActivity(intent);
							}
						}
				);

				AlertDialog alertDialog = submitDialog.create();
				alertDialog.show();

			}

		}


	}

	@Override
	protected void onResume()
	{
		super.onResume();
		failureAlertText.setVisibility(View.GONE);
		if (Constants.isbookwalkSyncAgain)
		{
			failureAlertText.setVisibility(View.VISIBLE);
			failureAlertText.setText(Constants.ALERT_MSG_BOOKWALK_SYNC_AGAIN);
		}
		MobiculeLogger.verbose("End Date - "+ Constants.END_DATE + "**********");

		if (!savedCountThread.isRunning())
		{
			savedCountThread = new SetSavedCountThread();
			savedCountThread.start();
		}
	}

	protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			MobiculeLogger.verbose("onReceive : Logout in progress --> Mainmenu");
			MainMenu.this.finish();
		}
	};

	private TextView failureAlertText;

	private void logOutReceiver()
	{
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.mobicule.ACTION_LOGOUT");
		registerReceiver(broadcastReceiver, intentFilter);
	}

	private void checkStatus(boolean isAlarmEnabled)
	{
		try
		{
			jsonParser = JSONParser.getInstance();
			applicationFacade = (IApplicationFacade) IOCContainer.getInstance(this).getBean("ApplicationFacade");
			response = applicationFacade.getBookWalkSyncData();

			Log.e("response","=========="+response.toString());
			if (response.isSuccess())
			{
				jsonParser.setJson(response.getData().toString());
				Constants.START_DATE = jsonParser.getValue(Constants.FIELD_START_DATE);
				Constants.END_DATE = jsonParser.getValue(Constants.FIELD_END_DATE);
				Constants.SYNC_STATUS = jsonParser.getValue(Constants.FIELD_SYNC_STATUS);

				Constants.BOOKWALK_ALARM_TIME = jsonParser.getValue(Constants.FIELD_BLKWLK_ALARM_TIME);

				MobiculeLogger.verbose("BOOKWALK_ALARM_TIME", Constants.BOOKWALK_ALARM_TIME);
				endTime = Utilities.getDateinMillis(Constants.END_DATE);
				startTime = System.currentTimeMillis();
				diffLong = endTime - startTime;
				alrmTime = endTime - 60000 * Integer.parseInt(Constants.BOOKWALK_ALARM_TIME);
				if (endTime >= startTime && endTime >= alrmTime)
				{
					autoTriggerOn = false;

					MobiculeLogger.verbose("alrmTime > startTime" + (alrmTime > startTime) + " " + alrmTime);
					
					if (startTime >= alrmTime)
					{
						alertText.setVisibility(View.VISIBLE);
						alertText.setText("Kindly note that your Bookwalk Sequence is ending on " + Constants.END_DATE);
						Constants.autoAlarmNotification = true;
					}
					if (isAlarmEnabled)
					{
						setAlarmForNotification();
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void setAlarmForNotification()
	{
		mAlarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmReceiver = new AlarmReceiver();
		Constants.ALARM_NOTIFICATION_TEXT = Utilities.formatMilliseconds(diffLong)
				+ " to complete the Bookwalk sequence.";
		jsonParser.setJson(SessionData.userInfo);
		String userName = jsonParser.getValue(ILoginPersistenceService.USER_DETAIL_USERNAME);
		Constants.ALARM_NOTIFICATION_TITLE = "Hi! " + userName;
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AlarmReceiver.ACTION_BOOKWALK_ALARM);
		registerReceiver(alarmReceiver, intentFilter);

		Intent intentToFire = new Intent(AlarmReceiver.ACTION_BOOKWALK_ALARM);
		mAlarmIntent = PendingIntent.getBroadcast(this, 0, intentToFire, 0);

		mAlarms.set(AlarmManager.RTC_WAKEUP, alrmTime, mAlarmIntent);
	}

	public static PendingIntent getSyncPendingIntent(Context context)
	{
		Intent i = new Intent(context, BackgroundService.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
		return pi;
	}

	public void startAutotriggerReceiver()
	{
		if (!Constants.isBroadcastRegistered)
		{
			if (Constants.END_DATE != null && !Constants.END_DATE.equals("") && !Constants.END_DATE.equals("0"))
			{
				Date oldDate;
				long autotriggertime = 0;
				final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				formatter.setLenient(false);

				MobiculeLogger.verbose("Current Time  :" + formatter.format(new Date()) + "***");
				
				try
				{
					oldDate = formatter.parse(Constants.END_DATE);
					autotriggertime = oldDate.getTime();
					MobiculeLogger.verbose("End Date - "+ Constants.END_DATE + "");
				}
				catch (ParseException e)
				{
					e.printStackTrace();
				}

				Intent myIntent = new Intent(getApplicationContext(), AutotriggerBroadcastReceiver.class);
				myIntent.setAction(Constants.AUTOTRIGGER_ACTION);
				pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 154654654, myIntent, 0);
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				try
				{
					alarmManager.set(AlarmManager.RTC_WAKEUP, autotriggertime, pendingIntent);
				}
				catch (NumberFormatException e)
				{
					e.printStackTrace();
				}
			}
		}
	}


	private void startTimeVariationService()
	{
		if (isServiceRunning(timeVariationService))
		{
			MobiculeLogger.verbose("time service is running " + isServiceRunning(timeVariationService));
		}
		else
		{
			MobiculeLogger.verbose("time service is not running  " + isServiceRunning(timeVariationService));
			startService(new Intent(MainMenu.this, MtService.class));
		}
	}

	public boolean isServiceRunning(String serviceName)
	{
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
		{
			if (serviceName.equals(service.service.getClassName()))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void onDestroy()
	{
		if (alarmReceiver != null)
		{
			this.unregisterReceiver(alarmReceiver);
		}
		if (broadcastReceiver != null)
		{
			this.unregisterReceiver(broadcastReceiver);
		}
		super.onDestroy();
	}

	public void defaultInit()
	{
		myBookWalk = (Button) findViewById(R.id.home_btn_feature1);
		randomMeterReading = (Button) findViewById(R.id.home_btn_feature6);
		savedReading = (Button) findViewById(R.id.home_btn_feature3);
		download_seq = (Button) findViewById(R.id.home_btn_feature5);
		alertText = (TextView) findViewById(R.id.alertTextview);
		failureAlertText = (TextView) findViewById(R.id.alertTextview2);
		menuButton = (ImageButton) findViewById(R.id.menu_button);

		menuButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				quickMenuView = getLayoutInflater().inflate(R.layout.layout_popup_menu, null);

				Display mDisplay = getWindowManager().getDefaultDisplay();

				int width = mDisplay.getWidth();

				settings_popup = new QuickActionBar(v, quickMenuView, 2, false);

				settings_popup.setAnimationStyle(QuickActionBar.GROW_FROM_LEFT);

				settings_popup.show();

				diagnosticsTextLayout = (LinearLayout) quickMenuView.findViewById(R.id.layout_diagnostics);
				diagnosticsTextLayout.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						settings_popup.dismiss();
						DialogDisplay.diagnosticDialog(MainMenu.this);

					}
				});
				dbBackupTextLayout = (LinearLayout) quickMenuView.findViewById(R.id.layout_db_backup);
				dbBackupTextLayout.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						settings_popup.dismiss();
						checkOutDBFile();

					}
				});
				logoutTextLayout = (LinearLayout) quickMenuView.findViewById(R.id.layout_logout);
				logoutTextLayout.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{

						settings_popup.dismiss();
						AlertDialog.Builder logoutBuilder = new AlertDialog.Builder(MainMenu.this);
						final AlertDialog logoutDialog = logoutBuilder.create();
						logoutDialog.setTitle("Application Logout");
						logoutDialog.setMessage(Constants.ALERT_MSG_LOGOUT_APPLICATION);
						logoutDialog.setButton("Yes", new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface arg0, int arg1)
							{
								Intent broadcastIntent = new Intent();
								broadcastIntent.setAction("com.mobicule.ACTION_LOGOUT");
								sendBroadcast(broadcastIntent);
								startActivity(new Intent(MainMenu.this, Login.class));
							}
						});

						logoutDialog.setButton2("No", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface arg0, int arg1)
							{
								logoutDialog.dismiss();
							}
						});
						logoutDialog.show();

					}
				});

				//-----------------------------------------------------------------------------------------
				/*//Creating the instance of PopupMenu  
				PopupMenu popup = new PopupMenu(MainMenu.this, menuButton);
				//Inflating the Popup using xml file  
				popup.getMenuInflater().inflate(R.menu.home_screen_menu, popup.getMenu());

				//registering popup with OnMenuItemClickListener  
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
				{
					public boolean onMenuItemClick(MenuItem item)
					{
						Toast.makeText(MainMenu.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
						return true;
					}
				});

				popup.show();//showing popup menu*/
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		try
		{
			if (imeiCheckes)
			{
				MobiculeLogger.verbose("on click - if case");

				if (!CoreConstants.isAutotriggerOn())
				{
					if (v == download_seq)
					{
						Constants.IS_SYNC_RUNNING = true;
						
						if(isInternetOn()){
//						powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//						wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MGL:BOOKWALK SYNC");
//						wakeLock.acquire();
						downLoadBookWalkSequence();
						}

					}
					else if (Constants.SYNC_STATUS != null && Constants.SYNC_STATUS.equalsIgnoreCase(complete))
					{
						if (v == myBookWalk)
						{
							Intent intent = new Intent(MainMenu.this, MyBookWalk.class);
							intent.putExtra("AutoTrigger", autoTriggerOn);
							startActivity(intent);
							meterReadingPersistance.DeleteMethod(Constants.TABLE_SAVED_METER_READING);
						}
						else if (v == randomMeterReading)
						{
							Intent intent = new Intent(MainMenu.this, RandomMeterReadingActivity.class);
							startActivity(intent);
						}
						else if (v == savedReading)
						{
							startActivity(new Intent(MainMenu.this, SavedMainActivity.class));
						}
					}
					else if (Constants.SYNC_STATUS != null && Constants.SYNC_STATUS.equals("")
							&& (v == randomMeterReading))
					{
						Intent intent = new Intent(MainMenu.this, RandomMeterReadingActivity.class);
						startActivity(intent);
					}
					else
					{
						Common_Methods.displayErrorDialog(MainMenu.this, "Bookwalk Sequence Sync",
								"Kindly download the Bookwalk Sequence again.", android.R.drawable.ic_dialog_alert);
					}
				}
				else
				{
					Common_Methods.displayDialog(MainMenu.this, "Auto Trigger", "Auto trigger is in progress");
				}
			}
			else
			{
				if (v == myBookWalk)
				{
					Toast.makeText(MainMenu.this, "Invalid user, Access is Denied", Toast.LENGTH_LONG).show();
				}
				else if (v == randomMeterReading)
				{
					Toast.makeText(MainMenu.this, "Invalid user, Access is Denied", Toast.LENGTH_LONG).show();
				}
				else if (v == savedReading)
				{
					startActivity(new Intent(MainMenu.this, SavedMainActivity.class));
				}
				else if (v == download_seq)
				{
					Toast.makeText(MainMenu.this, "Invalid user, Access is Denied", Toast.LENGTH_LONG).show();
				}

			}
		}
		catch (Exception e)
		{
			MobiculeLogger.verbose("MainMenu.onClick - ", e.toString());
		}

	}
	
	 public final boolean isInternetOn() {
         
	        // get Connectivity Manager object to check connection
	        ConnectivityManager connec = 
	                       (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
	         
	           // Check for network connections
	            if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
	                 connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
	                 connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
	                 connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
	               
	                return true;
	                 
	            } else if (
	              connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
	              connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
	               
	                Toast.makeText(this, " No Internet Connection ", Toast.LENGTH_SHORT).show();
	                return false;
	            }
	          return false;
	        }

	private void downLoadBookWalkSequence()
	{
		persistenceService = DefaultBookWalkSqncePersistenceService.getPersistenceService(this);
		communicationService = DefaultBookWalkSqnceCommunicationService.getDefaultSyncCommunicationService();
		applicationFacade = (IApplicationFacade) IOCContainer.getInstance(MainMenu.this).getBean("ApplicationFacade");
		versionControl = (VersionControl) IOCContainer.getInstance(MainMenu.this).getBean(IOCContainer.VERSION_CONTROL);

		SyncTask syncTask = new SyncTask(MainMenu.this, new ApplicationService()
		{

			@Override
			public void postExecute()
			{
                MobiculeLogger.verbose("result -----"+response.toString());
				try{
//				wakeLock.release();
				AlertDialog.Builder synCompleteDialog = new AlertDialog.Builder(context);
				synCompleteDialog.setTitle("BookWalk Sequence Download");
				synCompleteDialog.setMessage("Downloading BookWalk Sequence Completed Successfully");
				synCompleteDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						checkStatus(true);
						startAutotriggerReceiver();

						Constants.IS_SYNC_RUNNING = false;

						Constants.isbookwalkSyncAgain = false;
						MainMenu.this.failureAlertText.setVisibility(View.GONE);
					}
				});
				synCompleteDialog.show();
				keycode = true;
				
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}

			}

			@Override
			public void execute()
			{
				keycode = false;
				syncFacade.syncBookWalkSequence();
			}
		});

		observers.add(syncTask);
		syncFacade = new DefaultBookWalkSqnceFacade(observers, persistenceService, communicationService,
				SessionData.userInfo, applicationFacade, versionControl);
		syncTask.execute();

	}

	//Previous Code----------------------------------------------------------------------------------------

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home_screen_menu, menu);

		menu.add(logout);
		menu.add(diagnostics);
		menu.add(backUp);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu monClickenu)
	{
		this.menu = monClickenu;
		if (!keycode)
		{
			keycode = true;
			return false;
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getTitle().equals(logout))
		{
			AlertDialog.Builder logoutBuilder = new AlertDialog.Builder(MainMenu.this);
			final AlertDialog logoutDialog = logoutBuilder.create();
			logoutDialog.setTitle("Application Logout");
			logoutDialog.setMessage(Constants.ALERT_MSG_LOGOUT_APPLICATION);
			logoutDialog.setButton("Yes", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					Intent broadcastIntent = new Intent();
					broadcastIntent.setAction("com.mobicule.ACTION_LOGOUT");
					sendBroadcast(broadcastIntent);
					startActivity(new Intent(MainMenu.this, Login.class));
				}
			});

			logoutDialog.setButton2("No", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					logoutDialog.dismiss();
				}
			});
			logoutDialog.show();
		}
		else if (item.getTitle().equals("Clear Data"))
		{
			AlertDialog.Builder clearDataBuilder = new AlertDialog.Builder(MainMenu.this);
			final AlertDialog clearDataDialog = clearDataBuilder.create();
			clearDataDialog.setTitle("Clear Data");
			clearDataDialog.setMessage("Are you sure you want to clear the data?");
			clearDataDialog.setButton("Yes", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					unregisterBackgroundReceiver();
					clearAllData();
				}
			});

			clearDataDialog.setButton2("No", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					clearDataDialog.dismiss();
				}
			});
			clearDataDialog.show();
		}
		else if (item.getTitle().equals(diagnostics))
		{
			DialogDisplay.diagnosticDialog(MainMenu.this);
		}
		else if (item.getTitle().toString().equals(backUp))
		{
			checkOutDBFile();
		}
		else if (item.getTitle().equals(manulAutotrigger))
		{
			String title = "Kindly enter the following code to confirm the Auto Trigger operation.";

			final Dialog autotriggerDialog = new Dialog(MainMenu.this);
			autotriggerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			autotriggerDialog.setContentView(R.layout.autotrigger);

			TextView txtrndtitle = (TextView) autotriggerDialog.findViewById(R.id.txtrndtitle);
			txtrndtitle.setText(title);

			final EditText editrndom = (EditText) autotriggerDialog.findViewById(R.id.editrndnum);
			TextView txtrndno = (TextView) autotriggerDialog.findViewById(R.id.txtrndno);

			final Integer randomnum = new Double(Math.random() * 1000000000).intValue();
			txtrndno.setText("Code	:	" + String.valueOf(randomnum));

			Button btnOk = (Button) autotriggerDialog.findViewById(R.id.btnAutoOk);
			btnOk.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (editrndom.getText().toString().equalsIgnoreCase(String.valueOf(randomnum)))
					{
						Common_Methods.autoTrigger(MainMenu.this, true);
						autotriggerDialog.dismiss();
					}
					else
					{
						Toast.makeText(MainMenu.this, "Enter correct code", Toast.LENGTH_LONG).show();
					}

				}
			});

			Button btnCancel = (Button) autotriggerDialog.findViewById(R.id.btnAutoCancel);
			btnCancel.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					autotriggerDialog.dismiss();
				}
			});
			autotriggerDialog.show();

		}
		return super.onOptionsItemSelected(item);
	}*/
	//--------------------------------------------------------------------------------------------------------

	private void clearAllData()
	{
		new ApplicationAsk(MainMenu.this, new ApplicationService()
		{
			@Override
			public void postExecute()
			{
				AlertDialog.Builder clearDataBuilder = new AlertDialog.Builder(context);
				final AlertDialog clearDataDialog = clearDataBuilder.create();
				clearDataDialog.setTitle("Clear Data");
				clearDataDialog.setMessage("All the data has been successfully cleared.");
				clearDataDialog.setCancelable(false);
				clearDataDialog.setButton("Ok", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface arg0, int arg1)
					{
						Intent broadcastIntent = new Intent();
						broadcastIntent.setAction("com.mobicule.ACTION_LOGOUT");
						sendBroadcast(broadcastIntent);
					}
				});

				clearDataDialog.show();
			}

			@Override
			public void execute()
			{
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager.cancel(mAlarmIntent);

				AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager1.cancel(backgroundIntent);

				Intent timeVariation = new Intent();
				timeVariation.setAction(MtService.ACTION);
				timeVariation.putExtra("TVS", MtService.STOP_SERVICE);
				sendBroadcast(timeVariation);
				applicationFacade.clearAllData();
			}
		}).execute();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			AlertDialog.Builder exitBuilder = new AlertDialog.Builder(MainMenu.this);
			final AlertDialog exit = exitBuilder.create();
			exit.setTitle("Application Exit");
			exit.setMessage(Constants.ALERT_MSG_EXIT_APPLICATION);
			exit.setButton("Yes", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					Intent broadcastIntent = new Intent();
					broadcastIntent.setAction("com.mobicule.ACTION_LOGOUT");
					sendBroadcast(broadcastIntent);
					moveTaskToBack(true);
				}
			});

			exit.setButton2("No", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					exit.dismiss();
				}
			});
			exit.show();

		}
		return super.onKeyDown(keyCode, event);
	}

	private void registerBackgroundReceiver()
	{
		MobiculeLogger.verbose("registerBackgroundReceiver - " + Constants.BACKGROUND_ACTIVITY_DELAY);

		if (!Constants.isRegistered)
		{
			Constants.isRegistered = true;

			Intent myIntent = new Intent(AppApplication.getInstance(), BackgroundBroadcastReceiver.class);
			myIntent.setAction(Constants.BACKGROUND_ACTION);

			pendingIntent = PendingIntent.getBroadcast(AppApplication.getInstance(), 234324243, myIntent, 0);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.SECOND, 60);

			long backgroundTriggertime = 0;
			if ((null != Constants.BACKGROUND_ACTIVITY_DELAY) && (Constants.BACKGROUND_ACTIVITY_DELAY != ""))
			{
				backgroundTriggertime = Integer.parseInt(Constants.BACKGROUND_ACTIVITY_DELAY) * 60 * 1000;
				MobiculeLogger.verbose("Background Time *********   " + backgroundTriggertime);
			}
			else
			{
				Constants.BACKGROUND_ACTIVITY_DELAY = delayPreference.getString(Constants.bgActivityKey, "0");
				backgroundTriggertime = Integer.parseInt(Constants.BACKGROUND_ACTIVITY_DELAY) * 60 * 1000;
			}

			try
			{
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), backgroundTriggertime,
						pendingIntent);
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void checkOutDBFile()
	{
		/*
		 * You have to give permission "android.permission.WRITE_EXTERNAL_STORAGE" in your application
		 */
		try
		{
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			File mgldb_Dir = new File(sd, "/MGLDB/");
			mgldb_Dir.mkdirs();

			if (sd.canWrite())
			{
				String currentDBPath = "//data//" + getPackageName() + "//databases//MOBICULE_DB";
				String backupDBPath = "MGL";

				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(mgldb_Dir, backupDBPath);

				if (currentDB.exists())
				{
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());

					src.close();
					dst.close();
					Toast.makeText(getBaseContext(), "MGL BackUp file is created on sdcard.", Toast.LENGTH_LONG).show();
				}
			}
		}
		catch (Exception e)
		{

		}
	}

	private void unregisterBackgroundReceiver()
	{
		try
		{
			MobiculeLogger.verbose("unregisterBackgroundReceiver : ");

			if (pendingIntent != null)
			{
				Constants.isRegistered = false;
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager.cancel(pendingIntent);
			}
		}
		catch (IllegalArgumentException e)
		{
			// If receiver is not registered & we try to unRegistered it thn it
			// will through this exception.
			e.printStackTrace();
		}
	}

	private int getSavedCount()
	{
		try
		{
			applicationFacade = (IApplicationFacade) IOCContainer.getInstance(this).getBean("ApplicationFacade");
			int savedCount = 0;
			savedCount += applicationFacade.getSavedCount(CoreConstants.TABLE_SAVED_METER_READING);
			savedCount += applicationFacade.getSavedCount(CoreConstants.TABLE_RANDOM_METER_READING);
			savedCount += applicationFacade.getSavedCount(CoreConstants.TABLE_CUSTOMER_UPDATE);
			savedCount += applicationFacade.getSavedCount(CoreConstants.TABLE_SAVED_JOIN_TICKETING);
			savedCount += applicationFacade.getSavedCount(CoreConstants.TABLE_OnM_PLANNING);
			return savedCount;
		}
		catch (Exception e)
		{
			MobiculeLogger.verbose("getSavedCount() - " + e.toString());
			return 0;
		}
	}

	private class SetSavedCountThread extends Thread
	{
		private int count;

		private boolean isRunning;

		public SetSavedCountThread()
		{
			MobiculeLogger.verbose(TAG, "SetSavedCountThread started . . .");
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
				LoadingTextThread loadingThread = new LoadingTextThread();
				loadingThread.start();
				//Thread.sleep(7000);
				count = getSavedCount();
				loadingThread.stopLoading();
				while (!loadingThread.isLoadingStop())
				{

				}
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						savedReading.setText("Saved[" + count + "]");
					}
				});

			}
			catch (Exception e)
			{
				MobiculeLogger.verbose(TAG, "SetSavedCountThread : run() - " + e.toString());
			}
			MobiculeLogger.verbose(TAG, "SetSavedCountThread stoped ...");
			isRunning = false;
		}
	}

	private class LoadingTextThread extends Thread
	{

		private boolean isStop = false;

		private boolean isLoadingStop = false;

		String[] strPatterns = new String[] { " ..", ". .", ".. " };

		private int count = 0;

		public LoadingTextThread()
		{
			MobiculeLogger.verbose("LoadingTextThread started . . .");
		}

		@Override
		public void run()
		{
			super.run();

			try
			{

				while (!isStop)
				{
					count++;
					if (count > 2)
					{
						count = 0;
					}
					runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							savedReading.setText("Saved[" + strPatterns[count] + "]");
						}
					});
					Thread.sleep(200);
				}
			}
			catch (Exception e)
			{
				MobiculeLogger.verbose("LoadingTextThread - run() - " + e.toString());
			}
			isLoadingStop = true;
			MobiculeLogger.verbose("LoadingTextThread stoped . . .");
		}

		public void stopLoading()
		{
			isStop = true;
			MobiculeLogger.verbose("LoadingTextThread stopping . . .");
		}

		public boolean isLoadingStop()
		{
			return isLoadingStop;
		}
	}

}
