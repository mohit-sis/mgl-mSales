package com.mobicule.android.msales.mgl.autotrigger;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.Utilities;
import com.mobicule.android.msales.mgl.utilities.Common_Methods;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IMeterReadingInstance;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingFacade;

public class AutotriggerBroadcastReceiver extends BroadcastReceiver
{
	private static AutotriggerBroadcastReceiver instance;

	public IApplicationFacade mApplicationFacade;

	protected IMeterReadingFacade mMeterReadingFacade;

	protected IMeterReadingInstance mMeterReadingBO;

	public static boolean isRegistered = false;  

	public AutotriggerBroadcastReceiver()
	{
		if (!isRegistered)
			isRegistered = true;
	}

	public static AutotriggerBroadcastReceiver getInstance()
	{
		if (instance == null)
		{
			instance = new AutotriggerBroadcastReceiver();
		}
		return instance;
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{

		if (Constants.END_DATE != null && !Constants.END_DATE.equals("") && !Constants.END_DATE.equals("0"))
		{
			
			MobiculeLogger.verbose("onReceive","Constants.IS_SYNC_RUNNING: "+Constants.IS_SYNC_RUNNING);

			if (!Constants.IS_SYNC_RUNNING)
			{

				if (System.currentTimeMillis() > Utilities.getDateinMillis(Constants.END_DATE))
				{

					Constants.isBroadcastRegistered = true;

					final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					formatter.setLenient(false);

					Common_Methods.autoTrigger(context, true);
				}
			}
		}

	}

}
