package com.mobicule.android.msales.mgl.commons.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AlarmReceiver extends BroadcastReceiver
{
	public static final String ACTION_BOOKWALK_ALARM = "com.msales.mgl.ACTION_BOOKWALK_ALARM";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Intent notifyIntent = new Intent(context, NotifyService.class);
		context.startService(notifyIntent);
	}
}