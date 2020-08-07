package com.mobicule.android.msales.mgl.commons.model;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;

import com.mobicule.msales.mgl.client.jointicketing.IJoinTicketingFacade;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingFacade;
import com.mobicule.msales.mgl.client.onmPlanning.IOnMFacade;
import com.mobicule.msales.mgl.client.randommeterreading.IRandomMeterReadingFacade;
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomerFacade;

public class BackgroundDataSendTask extends AsyncTask<Void, Void, Void>
{
	private IMeterReadingFacade meterReadingFacade;

	private IRandomMeterReadingFacade randomMeterReadingFacade;

	private IUpdateCustomerFacade updateCustomerFacade;
	
	private IJoinTicketingFacade joinTicketingFacade; 
	
	private IOnMFacade onMFacade;

	private PowerManager powerManager;

	private PowerManager.WakeLock wakeLock;

	public BackgroundDataSendTask(Context context)
	{
		meterReadingFacade = (IMeterReadingFacade) IOCContainer.getInstance(context).getBean(
				"DefaultMeterReadingFacade");
		updateCustomerFacade = (IUpdateCustomerFacade) IOCContainer.getInstance(context).getBean(
				"DefaultUpdateCustomerFacade");
		randomMeterReadingFacade = (IRandomMeterReadingFacade) IOCContainer.getInstance(context).getBean(
				"DefaultRandomMeterReadingFacade");
		joinTicketingFacade=(IJoinTicketingFacade)IOCContainer.getInstance(context).getBean(
				"DefaultJoinTicketingFacade");
		onMFacade = (IOnMFacade) IOCContainer.getInstance(context).getBean("DefaultOnMPlanningFacade");
		powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MGL:BackgroundDataSendTask");
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		wakeLock.acquire();
	}

	@Override
	protected void onPostExecute(Void result)
	{
		super.onPostExecute(result);
		BackgroundBroadcastReceiver.isBackgroundDataSendTaskRunning = false;
		wakeLock.release();
	}

	@Override
	protected Void doInBackground(Void... arg0)
	{
		BackgroundBroadcastReceiver.isBackgroundDataSendTaskRunning = true;
		meterReadingFacade.submitOneMeterReadingFromRecevier();
		randomMeterReadingFacade.submitOneRandomMeterReadingFromReceiver();
		updateCustomerFacade.updateCustomerDetails();
		joinTicketingFacade.submitOneJoinTicketingFromRecevier();
		onMFacade.OnMCustomerDetails();
		return null;
	}
}
