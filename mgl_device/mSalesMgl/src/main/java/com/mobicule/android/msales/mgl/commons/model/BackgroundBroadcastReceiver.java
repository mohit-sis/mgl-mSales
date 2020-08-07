package com.mobicule.android.msales.mgl.commons.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.util.Constants;

public class BackgroundBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG = "BackgroundBroadcastReceiver";
	//private static BackgroundBroadcastReceiver instance;

	//public static boolean isRegistered = false;

	public static boolean isBackgroundDataSendTaskRunning = false;

	/*public BackgroundBroadcastReceiver()
	{
		if (!isRegistered)
			isRegistered = true;
	}*/

	/*public static BackgroundBroadcastReceiver getInstance()
	{
		if (instance == null)
		{
			instance = new BackgroundBroadcastReceiver();
		}
		return instance;
	}*/


	@Override
	public void onReceive(Context context, Intent intent)
	{

		if (intent.getAction().equals(Constants.BACKGROUND_ACTION))
		{
			MobiculeLogger.verbose("onReceive","Constants.IS_SYNC_RUNNING: "+Constants.IS_SYNC_RUNNING);
			MobiculeLogger.verbose("onReceive","isBackgroundDataSendTaskRunning: "+isBackgroundDataSendTaskRunning);

			if (!isBackgroundDataSendTaskRunning && !Constants.IS_SYNC_RUNNING)
			{

				MobiculeLogger.verbose("****BackgroundBroadcastReceiver: onReceive", "");

				//new BackgroundDataSendTask(context).execute(); // 1

				ConnectivityManager cm = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = cm.getActiveNetworkInfo();

				if (info != null) {
					if (info.isConnected()) {
						// you got a connection! tell your user!

							if (context != null) {

								Log.e(TAG,"BackgroundDataSendTaskStarted");

								//new BackgroundDataSendTask(context).execute();
								new BackgroundDataSendTask(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

							}
					}
				}
			}
		}
	}

}
