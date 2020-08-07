package com.mobicule.android.msales.mgl.commons.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.IBinder;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.menu.view.MainMenu;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;

import java.io.File;

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
public class NotifyService extends Service
{

	private final static String ACTION = "NotifyServiceAction";

	private final static int RQS_STOP_SERVICE = 1;

	private NotifyServiceReceiver notifyServiceReceiver;

	private NotificationManager notificationManager;

	private Notification myNotification;

	public static final String TAG = "NOTIFY SERVICE";

	@Override
	public void onCreate()
	{
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

		notifyServiceReceiver = new NotifyServiceReceiver();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{

		MobiculeLogger.verbose("onstartCommand");
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION);
		registerReceiver(notifyServiceReceiver, intentFilter);
		// Send Notification
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		myNotification = new Notification(R.drawable.msalesnew, Constants.ALARM_TICKER_TEXT, System.currentTimeMillis());

		myNotification.defaults |= Notification.DEFAULT_SOUND;
		myNotification.flags |= Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;
		Context context = getApplicationContext();
		Intent notificationIntent = new Intent(this, MainMenu.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		Notification.Builder builder = new Notification.Builder(context)
				.setSmallIcon(R.drawable.msalesnew)
				.setContentTitle(Constants.ALARM_NOTIFICATION_TITLE)
				.setContentText(Constants.ALARM_NOTIFICATION_TEXT)
				.setContentIntent(pendingIntent);
		Notification notification = builder.build();
		notificationManager.notify(R.id.listview, notification);
		stopSelf();






		/*MobiculeLogger.verbose("onstartCommand");
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION);
		registerReceiver(notifyServiceReceiver, intentFilter);
		// Send Notification
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		myNotification = new Notification(R.drawable.msalesnew, Constants.ALARM_TICKER_TEXT, System.currentTimeMillis());
		Context context = getApplicationContext();
		Intent notificationIntent = new Intent(this, MainMenu.class);
		String notificationTitle = Constants.ALARM_NOTIFICATION_TITLE;
		String notificationText = Constants.ALARM_NOTIFICATION_TEXT;
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		myNotification.defaults |= Notification.DEFAULT_SOUND;
		myNotification.flags |= Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;
		myNotification.setLatestEventInfo(context, notificationTitle, notificationText, pendingIntent);
		notificationManager.notify(R.id.listview, myNotification);
		stopSelf();*/
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{
		this.unregisterReceiver(notifyServiceReceiver);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

	public class NotifyServiceReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context arg0, Intent arg1)
		{
			int rqs = arg1.getIntExtra("RQS", 0);
			if (rqs == RQS_STOP_SERVICE)
			{
				stopSelf();
			}
		}
	}

}
