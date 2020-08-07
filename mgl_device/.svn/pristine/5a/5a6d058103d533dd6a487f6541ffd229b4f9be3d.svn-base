package com.mobicule.android.msales.mgl.autotrigger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import org.json.me.JSONObject;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.AppApplication;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.commons.model.IOCContainer;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.Utilities;
import com.mobicule.component.json.parser.IJSONParser;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.component.string.StringUtil;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.crashnotifier.GenericExceptionHandler;
import com.mobicule.crashnotifier.IGenericExceptionHandler;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.meterreading.DefaultMeterReading;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IMeterReadingInstance;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IReadings;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingFacade;

public class AutotriggerService extends Service
{
	public final static String ACTION = "AutotriggerServiceAction";

	public IApplicationFacade applicationFacade;

	protected IMeterReadingFacade meterReadingFacade;

	public final static int STOP_SERVICE = 0;

	private IJSONParser jsonParser;

	protected static Response response;

	private long endTime;

	protected IMeterReadingInstance meterReadingBO;

	private boolean autoTriggerOn = false;

	private Long interval;

	private static String TAG = "AutotriggerService";

	private PendingIntent pendingIntent = null;

	private SharedPreferences delayPreference;

	public AutotriggerService()
	{
		defaultInitialization();
	}

	private void defaultInitialization()
	{
		try
		{
			meterReadingFacade = (IMeterReadingFacade) IOCContainer.getInstance(AutotriggerService.this).getBean(
					"DefaultMeterReadingFacade");
			meterReadingBO = meterReadingFacade.getCurrentMeterReadingBO();
			applicationFacade = (IApplicationFacade) IOCContainer.getInstance(AutotriggerService.this).getBean(
					"ApplicationFacade");
			jsonParser = JSONParser.getInstance();
		}
		catch (Exception e)
		{
			/*IGenericExceptionHandler handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
			handler.setIsLogEnable(true);*/
			e.printStackTrace();
			
		}
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		autoTriggerOn = false;

		new ApplicationAsk(null, new ApplicationService()
		{
			@Override
			public void postExecute()
			{
				CoreConstants.setAutotriggerOn(false);
				stopSelf();
			}

			@Override
			public void execute()
			{
				while (!autoTriggerOn)
				{
					if (Constants.END_DATE != null && !Constants.END_DATE.equals("") && !Constants.END_DATE.equals("0"))
					{
						if (System.currentTimeMillis() > Utilities.getDateinMillis(Constants.END_DATE))
						{
							autoTriggerOn = true;
							CoreConstants.setAutotriggerOn(true);
							Toast.makeText(AutotriggerService.this, "Service converting to completed",
									Toast.LENGTH_LONG).show();

							if (applicationFacade.getIncompleteCount() > 0
									|| applicationFacade.getUnattemptedCount() > 0)
							{
								Intent intent = new Intent(Constants.AUTOTRIGGER_ACTION);
								intent.setClass(AppApplication.getInstance(), AutotriggerBroadcastReceiver.class);
								AutotriggerService.this.sendBroadcast(intent);

								updateMeterReadingOnAutoTrigger(CoreConstants.FIELD_UNATTEMPTED);
								updateMeterReadingOnAutoTrigger(Constants.FIELD_INCOMPLETE);
							}
						}
					}
				}
			}
		}).execute();
		return START_STICKY;
	}


	public void updateMeterReadingOnAutoTrigger(String status)
	{
		Response response = applicationFacade.getBuildingList(status);
		Vector data = new Vector();
		Vector customerData = new Vector();
		if (response.isSuccess())
		{
			data = (Vector) response.getData();
		}
		for (int i = 0; i < data.size(); i++)
		{
			jsonParser.setJson(data.elementAt(i).toString());
			String connObj = jsonParser.getValue(IMeterReading.KEY_CONN_OBJ);
			meterReadingBO.reset();

			meterReadingBO.setConnObj(connObj);
			if (status.equalsIgnoreCase(CoreConstants.FIELD_UNATTEMPTED))
			{
				response = applicationFacade.getCustomerListBasedOnConnObj(connObj, status);
			}
			else if (status.equalsIgnoreCase(Constants.FIELD_INCOMPLETE))
			{
				response = applicationFacade.getCustomerListBasedOnConnObjFromSavedMeterReading(connObj, status);
			}

			if (response.isSuccess())
			{
				customerData = (Vector) response.getData();
				for (int j = 0; j < customerData.size(); j++)
				{
					try
					{
						meterReadingBO.resetReadingList();
						jsonParser.setJson(customerData.elementAt(j).toString());
						MobiculeLogger.verbose("customerData", "customer	:" + j + "	:data	:" + customerData.elementAt(j).toString());

						meterReadingBO.setMroNumber(jsonParser.getValue(IMeterReading.KEY_MRO_NO));
						meterReadingBO.setBpNumber(jsonParser.getValue(IMeterReading.KEY_BP_NO));
						meterReadingBO.setMeterNumber(jsonParser.getValue(IMeterReading.KEY_METER_NO));
						meterReadingBO.setCustomerContactNo(jsonParser.getValue(IMeterReading.KEY_CUSTOMER_CONTACT));
						meterReadingBO.setCustomerName(jsonParser.getValue(IMeterReading.KEY_CUSTOMER_NAME));
						meterReadingBO.setCustomerAddress(jsonParser.getValue(IMeterReading.KEY_CUSTOMER_ADDRESS));
						meterReadingBO.setCaNo(jsonParser.getValue(Constants.KEY_CUSTOMER_CA_NUMBER));
						meterReadingBO.setCustLandNo(jsonParser.getValue(Constants.KEY_CUSTOMER_LANDLINE_NUMBER));
						meterReadingBO.setCustOfficeNo(jsonParser.getValue(Constants.KEY_CUSTOMER_OFFICE_NUMBER));

						meterReadingBO.setStatus(Constants.FIELD_COMPLETED);
						meterReadingBO.setImage("");
						meterReadingBO.setArea("");
						Vector meterReadingV = jsonParser.getArray(IMeterReading.KEY_READINGS);
						if (meterReadingV != null && !meterReadingV.isEmpty())
						{
							for (int k = 0; k < meterReadingV.size(); k++)
							{
								meterReadingBO.setReadings(new DefaultMeterReading.DefaultReadings(new JSONObject(
										meterReadingV.elementAt(k).toString())));
							}
						}

						if (status.equalsIgnoreCase(CoreConstants.FIELD_UNATTEMPTED))
						{
							meterReadingBO.setLock("1");
							meterReadingBO.setMrReason(CoreConstants.MSG_FAILED_TO_TAKE_MR);
							IReadings reading = new DefaultMeterReading.DefaultReadings();
							SimpleDateFormat dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);

							String readingTime = dateTime.format(new Date(System.currentTimeMillis()));
							String time = readingTime.substring(11);
							String date = readingTime.substring(0, 10);

							reading.setReadingTime(time);
							reading.setDate(date);
							reading.setMrCode(CoreConstants.FAILED_MR_CODE);
							reading.setMeterReading("");
							meterReadingBO.setReadings(reading);

							MobiculeLogger.verbose("My bookwalk *******   ", "No of Attempts   get method  *********in json  "
									+ jsonParser.getValue(CoreConstants.KEY_NO_OF_ATTEMPTS));

							int noOfAttempts;
							if (!StringUtil.isValid(jsonParser.getValue(CoreConstants.KEY_NO_OF_ATTEMPTS)))
							{
								noOfAttempts = 1;
								meterReadingBO.setNoOfAttempts(String.valueOf(noOfAttempts));
							}
							else
							{
								noOfAttempts = Integer.parseInt(jsonParser.getValue(CoreConstants.KEY_NO_OF_ATTEMPTS)) + 1;
								meterReadingBO.setNoOfAttempts(String.valueOf(noOfAttempts));
							}
						}
						else if (status.equals(Constants.FIELD_INCOMPLETE))
						{
							meterReadingBO.setLock("2");
							meterReadingBO.setMrReason(jsonParser.getValue(IMeterReading.KEY_MR_REASON));

							MobiculeLogger.verbose("No of Attempts   get method  *********in json  "
									+ jsonParser.getValue(CoreConstants.KEY_NO_OF_ATTEMPTS));

							int noOfAttempts;
							if (!StringUtil.isValid(jsonParser.getValue(CoreConstants.KEY_NO_OF_ATTEMPTS)))
							{
								noOfAttempts = 1;
								meterReadingBO.setNoOfAttempts(String.valueOf(noOfAttempts));
							}
							else
							{
								noOfAttempts = Integer.parseInt(jsonParser.getValue(CoreConstants.KEY_NO_OF_ATTEMPTS));
								meterReadingBO.setNoOfAttempts(String.valueOf(noOfAttempts));
							}
						}

						MobiculeLogger.verbose("No of Attempts   get method  *********  " + meterReadingBO.getNoOfAttempts());
						meterReadingFacade.saveMeterReading(meterReadingBO.toJSON().toString(), false);

					}
					catch (Exception e)
					{
						/*IGenericExceptionHandler handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
						handler.setIsLogEnable(true);*/
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void onDestroy()
	{
		stopSelf();
		super.onDestroy();
	}
}
