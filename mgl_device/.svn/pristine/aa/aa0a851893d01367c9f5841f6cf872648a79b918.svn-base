/**
 * 
 */
package com.mobicule.android.msales.mgl.timevariation;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.IOCContainer;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.component.json.parser.IJSONParser;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.crashnotifier.IGenericExceptionHandler;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.timevariation.ITimeVariation;
import com.mobicule.msales.mgl.client.timevariation.ITimeVariationFacade;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author nikita
 * 
 */
public class MtService extends Service {
	public final static String ACTION = "TimeVariationServiceAction";

	public IApplicationFacade applicationFacade;

	public final static int STOP_SERVICE = 0;

	private IJSONParser jsonParser;

	protected static Response response;

	protected ITimeVariationFacade timeVariationFacade;

	protected ITimeVariation timeVariationBO;

	private SharedPreferences delayPreference;

	private TimeVariationServiceReceiver timeVariationServiceReceiver;
	
	IGenericExceptionHandler handler;

	public MtService() {
		defaultInitialization();
	}

	private void defaultInitialization() {
		try {
			timeVariationFacade = (ITimeVariationFacade) IOCContainer
					.getInstance(MtService.this).getBean(
							"DefaultTimeVarationFacade");
			timeVariationBO = timeVariationFacade.getTimeVariationBO();
			applicationFacade = (IApplicationFacade) IOCContainer.getInstance(
					this).getBean("ApplicationFacade");
			jsonParser = JSONParser.getInstance();
			//handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
		} catch (Exception e) {
			//handler.logCrashReport(e);
			e.printStackTrace();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		timeVariationServiceReceiver = new TimeVariationServiceReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION);
		registerReceiver(timeVariationServiceReceiver, intentFilter);

		delayPreference = MtService.this.getSharedPreferences(
				Constants.MSALES_MGL_SHARED_PREFERENCE, Context.MODE_PRIVATE);

		Constants.TIME_VARIATION_SUBMISSION_DELAY = delayPreference.getString(
				Constants.timeVariableKey, "0");
	
		if (Constants.TIME_VARIATION_SUBMISSION_DELAY != null && !Constants.TIME_VARIATION_SUBMISSION_DELAY.equals("0") ) {
			Long interval = Long
					.valueOf(Constants.TIME_VARIATION_SUBMISSION_DELAY);

			interval = interval * 60000;

			MobiculeLogger.verbose("MTSERVICE ====---+++   TVSD  "
					+ Constants.TIME_VARIATION_SUBMISSION_DELAY
		  			+ "  interval  " + interval);

		
			SimpleTimerTask myTimerTask = new SimpleTimerTask();

			Timer myTimer = new Timer();
  
			myTimer.schedule(myTimerTask, 0, interval);
		}
		return START_STICKY;
	}

	private class SimpleTimerTask extends TimerTask {
		@Override
		public void run() {
			MobiculeLogger.verbose("Constants.IS_SYNC_RUNNING: "
					+ Constants.IS_SYNC_RUNNING);

			if (!Constants.IS_SYNC_RUNNING) {
				timeVariationFacade.getTimeVariation().reset();

				SimpleDateFormat dateTime = new SimpleDateFormat(
						"dd/MM/yyyy HH:mm:ss", Locale.US);
				String readingTime = dateTime.format(new Date(System
						.currentTimeMillis()));
				String time = readingTime.substring(11);
				String date = readingTime.substring(0, 10);

				timeVariationBO.setDeviceTime(time);
				timeVariationBO.setDeviceDate(date);

				response = timeVariationFacade.submitTimeVariation();
			}
		}
	}

	@Override
	public void onDestroy() {
		/*this.unregisterReceiver(timeVariationServiceReceiver);
		stopSelf();*/
		super.onDestroy();
	}

	public class TimeVariationServiceReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			int rqs = arg1.getIntExtra("TVS", 0);
			if (rqs == STOP_SERVICE) {
				stopSelf();
			}
		}
	}

}
