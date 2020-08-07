package com.mobicule.android.msales.mgl.commons.model;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import com.mobicule.android.component.db.SQLiteDatabaseManager;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.meterreading.model.DefaultMeterReadingPersistenceService;
import com.mobicule.android.msales.mgl.onmplaning.model.DefaultOnMPlanningPersistanceService;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IMeterReadingInstance;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingFacade;
import com.mobicule.msales.mgl.client.onmPlanning.IOnMFacade;
import com.mobicule.msales.mgl.client.onmPlanning.IOnMPlanning;
import com.mobicule.msales.mgl.client.randommeterreading.IRandomMeterReading.IRandomMeterReadingInstance;
import com.mobicule.msales.mgl.client.randommeterreading.IRandomMeterReadingFacade;
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomer;
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomerFacade;

public class BackgroundService extends Service
{

	public final static String ACTION = "BackGroundServiceAction";

	public final static int STOP_SERVICE = 0;

	protected IMeterReadingFacade meterReadingFacade;

	protected IMeterReadingInstance meterReadingBO;

	protected DefaultMeterReadingPersistenceService meterReadingPersistance;
	
	protected IOnMFacade onMFacade;
	
	protected IOnMPlanning onMPlanningBO;
	
	protected DefaultOnMPlanningPersistanceService onMPlanningPersistance;

	protected static Response response;

	protected static Response responseRMR;

	protected static Response responseUpdate;
	
	protected static Response responseOnM;

	protected IRandomMeterReadingFacade randomMeterReadingFacade;

	protected IRandomMeterReadingInstance randomMeterReadingBO;

	private BackgroundServiceReceiver backgroundServiceReceiver;

	protected IUpdateCustomerFacade updateCustomerFacade;

	protected IUpdateCustomer updateCustomerBO;

	private SQLiteDatabaseManager databaseManager;

	public static final String TABLE_CUSTOMER_UPDATE = "CUSTOMER_UPDATE";

	public static final String TABLE_RANDOM_METER_READING = "RANDOM_METER_READING";

	@Override
	public void onCreate()
	{
		MobiculeLogger.verbose("Background oncreate()");
		backgroundServiceReceiver = new BackgroundServiceReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION);
		registerReceiver(backgroundServiceReceiver, intentFilter);

		meterReadingFacade = (IMeterReadingFacade) IOCContainer.getInstance(getApplicationContext()).getBean(
				"DefaultMeterReadingFacade");
		meterReadingBO = meterReadingFacade.getCurrentMeterReadingBO();
		meterReadingPersistance = (DefaultMeterReadingPersistenceService) IOCContainer.getInstance(
				getApplicationContext()).getBean("DefaultMeterReadingPersistanceService");
		randomMeterReadingFacade = (IRandomMeterReadingFacade) IOCContainer.getInstance(getApplicationContext())
				.getBean("DefaultRandomMeterReadingFacade");
		randomMeterReadingBO = randomMeterReadingFacade.getRandomMeterReadingBO();
		updateCustomerFacade = (IUpdateCustomerFacade) IOCContainer.getInstance(getApplicationContext()).getBean(
				"DefaultUpdateCustomerFacade");
		updateCustomerBO = updateCustomerFacade.getCustomerUpdateBO();
		
		onMFacade = (IOnMFacade) IOCContainer.getInstance(getApplicationContext()).getBean("DefaultOnMPlanningFacade");
		onMPlanningBO = onMFacade.getOnMPlanningBO();
		onMPlanningPersistance = (DefaultOnMPlanningPersistanceService) IOCContainer.getInstance(getApplicationContext()).getBean("DefaultOnMPlanningPersistanceService");
		databaseManager = (SQLiteDatabaseManager) SQLiteDatabaseManager.getInstance(getApplicationContext(),Constants.DATABASE_NAME);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onDestroy()
	{
		this.unregisterReceiver(backgroundServiceReceiver);
		MobiculeLogger.verbose("BackGround", "destroy");
		stopSelf();
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		MobiculeLogger.verbose("onStart backgroundActivity");

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				response = meterReadingFacade.submitMeterReading();
				responseRMR = randomMeterReadingFacade.submitRandomMeterReading();
				responseUpdate = updateCustomerFacade.updateCustomerDetails();
				responseOnM = onMFacade.OnMCustomerDetails();
				
				MobiculeLogger.verbose("//BackgroundService : OnM Reasponse :: "+responseOnM.toString());
				MobiculeLogger.verbose("BackgroundService.... s ", "responce message : " + response.getMessage());
				
				if (responseRMR.isSuccess())
				{
					databaseManager.dropTable(TABLE_RANDOM_METER_READING);
				}
				if (responseUpdate.isSuccess())
				{
					databaseManager.dropTable(TABLE_CUSTOMER_UPDATE);
				}
			}
		}).start();

		super.onStart(intent, startId);
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		return super.onUnbind(intent);
	}

	public class BackgroundServiceReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context arg0, Intent arg1)
		{
			int bgs = arg1.getIntExtra("BGS", 0);
			if (bgs == STOP_SERVICE)
			{
				stopSelf();
			}
		}
	}

}