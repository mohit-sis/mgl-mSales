package com.mobicule.android.msales.mgl.commons.model;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.component.util.CoreMobiculeLogger;
import com.mobicule.crashnotifier.GenericExceptionHandler;
import com.mobicule.crashnotifier.IGenericExceptionHandler;

public class AppApplication extends Application
{
	public static final String CHANNEL_ID = "ForegroundServiceChannel";

	private static AppApplication singleton;

	public static AppApplication getInstance()
	{
		return singleton;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		singleton = this;
		
//		MobiculeLogger.disableLogging();
//		CoreMobiculeLogger.showlog = false;

		MobiculeLogger.enableLogging(Log.VERBOSE);
		CoreMobiculeLogger.showlog = true;

//		createNotificationChannel();

		IGenericExceptionHandler handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
		handler.setIsLogEnable(true);
		String[] toRecepient = new String[] { "mglsupport@mobicule.com" };
		handler.setRecipientsEmail(toRecepient);
		handler.enableBackgroundMailTriggering(true);
		handler.registerForCapturingGenericException();
		
	}

	private void createNotificationChannel() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel serviceChannel = new NotificationChannel(
					CHANNEL_ID,
					"Foreground Service Channel",
					NotificationManager.IMPORTANCE_DEFAULT
			);
			serviceChannel.setDescription("ChannelDescription");
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(serviceChannel);
		}
	}
}
