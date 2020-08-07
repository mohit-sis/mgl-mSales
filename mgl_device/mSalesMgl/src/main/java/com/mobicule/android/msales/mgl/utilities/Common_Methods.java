package com.mobicule.android.msales.mgl.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.commons.model.IOCContainer;
import com.mobicule.android.msales.mgl.menu.view.MainMenu;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.component.json.parser.IJSONParser;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.component.string.StringUtil;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.meterreading.DefaultMeterReading;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IMeterReadingInstance;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IReadings;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingFacade;

public class Common_Methods
{

	public static IApplicationFacade applicationFacade;

	public static IMeterReadingFacade meterReadingFacade;

	public static IMeterReadingInstance meterReadingBO;

	public static IJSONParser jsonParser;

	public static JSONObject jsonObject;

	public static boolean dialog_AutoTrigger = false;

	public static void autoTrigger(Context context, boolean dialogAutotrigger)
	{
		dialog_AutoTrigger = dialogAutotrigger;
		defaultInitialization(context);
		applicationTask();
	}

	public static void defaultInitialization(Context context)
	{
		try
		{
			meterReadingFacade = (IMeterReadingFacade) IOCContainer.getInstance(context).getBean(
					"DefaultMeterReadingFacade");
			meterReadingBO = meterReadingFacade.getCurrentMeterReadingBO();
			applicationFacade = (IApplicationFacade) IOCContainer.getInstance(context).getBean("ApplicationFacade");
			jsonParser = JSONParser.getInstance();
			applicationFacade.calculateBookWalkCountBasedOnStatus();
			if (applicationFacade.getIncompleteCount() > 0 || applicationFacade.getUnattemptedCount() > 0)
			{
				if (dialog_AutoTrigger)
				{
					if(!CoreConstants.isAutotriggerOn())
					{
						Common_Methods.displayBroadcastDialog(Constants.broadcastDialogContext, "Auto trigger",
							Constants.AUTO_TRIGGER);
					}
					else
					{
						Common_Methods.displayBroadcastDialog(Constants.broadcastDialogContext, "Auto trigger", "AutoTrigger is in progress");
					}
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void applicationTask()
	{
		new ApplicationAsk(null, new ApplicationService()
		{
			@Override
			public void postExecute()
			{
				CoreConstants.setAutotriggerOn(false);
				Constants.isBroadcastRegistered = false;
			}

			@Override
			public void execute()
			{
				CoreConstants.setAutotriggerOn(true);
				// Toast.makeText(asyncContext,
				// "Service converting to completed", Toast.LENGTH_LONG).show();

				MobiculeLogger.verbose("Broadcast server Executed Incomplete" + applicationFacade.getIncompleteCount()+ "UnAttempted"
						+ applicationFacade.getUnattemptedCount());
				if (applicationFacade.getIncompleteCount() > 0)
				{
					updateMeterReadingOnAutoTrigger(Constants.FIELD_INCOMPLETE, applicationFacade, meterReadingFacade,
							meterReadingBO, jsonParser);// ,jsonObject);
				}
				if (applicationFacade.getUnattemptedCount() > 0)
				{
					updateMeterReadingOnAutoTrigger(CoreConstants.FIELD_UNATTEMPTED, applicationFacade,
							meterReadingFacade, meterReadingBO, jsonParser);// ,jsonObject);

				}
			}
		}).execute();
	}

	public static void displayBroadcastDialog(final Context context, String title, String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		final AlertDialog alertDialog = builder.create();
		alertDialog.setMessage(message);
		alertDialog.setTitle(title);
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				MobiculeLogger.verbose(""+ context + "");
				Intent myintent = new Intent(context, MainMenu.class);
				myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				myintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//myintent.putExtra("isBroadcastStarted", true);
				context.startActivity(myintent);
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

	public static void updateMeterReadingOnAutoTrigger(String status, IApplicationFacade mApplicationFacade,
			IMeterReadingFacade mMeterReadingFacade, IMeterReadingInstance mMeterReadingBO, IJSONParser mJsonParser)
	{
		Response response = applicationFacade.getBuildingList(status);

		MobiculeLogger.verbose("Common class - EEEEEEEEEEEEEEEEEEEEEEEEe   " + meterReadingBO);
		Vector data = new Vector();
		Vector customerData = new Vector();
		if (response.isSuccess())
		{
			data = (Vector) response.getData();
		}
		for (int i = 0; i < data.size(); i++)
		{
			jsonParser.setJson(data.elementAt(i).toString());

			try
			{
				jsonObject = new JSONObject(data.elementAt(i).toString());
			}
			catch (JSONException e1)
			{

				e1.printStackTrace();
			}
			String connObj = jsonObject.getValue(IMeterReading.KEY_CONN_OBJ).toString(); // jsonParser.getValue(IMeterReading.KEY_CONN_OBJ);
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
				MobiculeLogger.verbose("Response"+ response.getData().toString());

				MobiculeLogger.verbose("Common - AAAAAAAAAAAAAAAAAA  " + meterReadingBO.toJSON().toString());
				customerData = (Vector) response.getData();
				for (int j = 0; j < customerData.size(); j++)
				{
					try
					{
						meterReadingBO.resetReadingList();

						jsonObject = new JSONObject(customerData.elementAt(j).toString());
						MobiculeLogger.verbose("customer	:" + j + "	:data	:" + customerData.elementAt(j).toString());

						// meterReadingBO.init(new
						// JSONObject(customerData.elementAt(j).toString()));

						meterReadingBO.setMroNumber(jsonObject.getStringValue(IMeterReading.KEY_MRO_NO));
						meterReadingBO.setMruCode(jsonObject.getStringValue(IMeterReading.KEY_MRU_CODE));
						meterReadingBO.setBpNumber(jsonObject.getStringValue(IMeterReading.KEY_BP_NO));
						meterReadingBO.setMeterNumber(jsonObject.getStringValue(IMeterReading.KEY_METER_NO));
						meterReadingBO.setCustomerContactNo(jsonObject
								.getStringValue(IMeterReading.KEY_CUSTOMER_CONTACT));
						meterReadingBO.setCustomerName(jsonObject.getStringValue(IMeterReading.KEY_CUSTOMER_NAME));
						meterReadingBO
								.setCustomerAddress(jsonObject.getStringValue(IMeterReading.KEY_CUSTOMER_ADDRESS));
						meterReadingBO.setCaNo(jsonObject.getStringValue(Constants.KEY_CUSTOMER_CA_NUMBER));
						meterReadingBO.setCustLandNo(jsonObject.getStringValue(Constants.KEY_CUSTOMER_LANDLINE_NUMBER));
						meterReadingBO.setCustOfficeNo(jsonObject.getStringValue(Constants.KEY_CUSTOMER_OFFICE_NUMBER));

						// meterReadingBO.setMrReason(CoreConstants.MSG_FAILED_TO_TAKE_MR);
						meterReadingBO.setStatus(Constants.FIELD_COMPLETED);
						// meterReadingBO.setLock("1");
						if (jsonObject.has(IMeterReading.KEY_IMAGES))
						{
							meterReadingBO.setImage(jsonObject.getString(IMeterReading.KEY_IMAGES));
						}
						else
						{
							meterReadingBO.setImage("");
						}
						if (jsonObject.has(IMeterReading.KEY_IMAGES2))
						{
							meterReadingBO.setImage2(jsonObject.getString(IMeterReading.KEY_IMAGES2));
						}
						else
						{
							meterReadingBO.setImage2("");
						}
						if (jsonObject.has(IMeterReading.KEY_IMAGES3))
						{
							meterReadingBO.setImage3(jsonObject.getString(IMeterReading.KEY_IMAGES3));
						}
						else
						{
							meterReadingBO.setImage3("");
						}

						if (jsonObject.has(IMeterReading.KEY_AREA))
						{
							meterReadingBO.setArea(jsonObject.getString(IMeterReading.KEY_AREA));
						}
						else
						{
							meterReadingBO.setArea("");
						}
						/*
						 * Vector meterReadingV =
						 * jsonParser.getArray(IMeterReading.KEY_READINGS);
						 */

						/*
						 * 	// jsonParser.setJson(customerData.elementAt(j).toString());
						 * meterReadingBO.setMroNumber(jsonObject.getStringValue(
						 * IMeterReading.KEY_MRO_NO));
						 * meterReadingBO.setBpNumber
						 * (jsonParser.getValue(IMeterReading.KEY_BP_NO));
						 * meterReadingBO
						 * .setMeterNumber(jsonParser.getValue(IMeterReading
						 * .KEY_METER_NO));
						 * meterReadingBO.setCustomerContactNo(jsonParser
						 * .getValue(IMeterReading.KEY_CUSTOMER_CONTACT));
						 * meterReadingBO
						 * .setCustomerName(jsonParser.getValue(IMeterReading
						 * .KEY_CUSTOMER_NAME));
						 * meterReadingBO.setCustomerAddress
						 * (jsonParser.getValue(
						 * IMeterReading.KEY_CUSTOMER_ADDRESS));
						 * meterReadingBO.setCaNo
						 * (jsonParser.getValue(Constants.KEY_CUSTOMER_CA_NUMBER
						 * )); meterReadingBO.setCustLandNo(jsonParser.getValue(
						 * Constants.KEY_CUSTOMER_LANDLINE_NUMBER));
						 * meterReadingBO
						 * .setCustOfficeNo(jsonParser.getValue(Constants
						 * .KEY_CUSTOMER_OFFICE_NUMBER));
						 */
						/*	Vector meterReadingV = (Vector) jsonObject.get(IMeterReading.KEY_READINGS);
						
						//Vector meterReadingV = jsonParser.getArray(IMeterReading.KEY_READINGS);

							if (meterReadingV != null && !meterReadingV.isEmpty())
							{
								for (int k = 0; k < meterReadingV.size(); k++)
								{
									meterReadingBO.setReadings(new DefaultMeterReading.DefaultReadings(new JSONObject(
											meterReadingV.elementAt(k).toString())));
								}
							}*/

						MobiculeLogger.verbose("common Method " + jsonObject);
						if (jsonObject.has(IMeterReading.KEY_READINGS))
						{
							JSONArray meterReadingV = jsonObject.getJSONArray(IMeterReading.KEY_READINGS);
							MobiculeLogger.verbose("Meter Reading " + meterReadingV);
							//Vector meterReadingV = jsonParser.getArray(IMeterReading.KEY_READINGS);

							if (meterReadingV != null && !meterReadingV.isEmpty())
							{
								for (int k = 0; k < meterReadingV.length(); k++)
								{
									MobiculeLogger.verbose("CCCCCCCCCCCCCCCCCCC   " + meterReadingV.get(k).toString());
									meterReadingBO.setReadings(new DefaultMeterReading.DefaultReadings(new JSONObject(
											meterReadingV.get(k).toString())));
								}
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

							int noOfAttempts;
							/*
							 * if (!StringUtil.isValid(jsonParser.getValue(
							 * CoreConstants.KEY_NO_OF_ATTEMPTS))) {
							 * noOfAttempts = 1;
							 * meterReadingBO.setNoOfAttempts(String
							 * .valueOf(noOfAttempts)); } else { noOfAttempts =
							 * Integer
							 * .parseInt(jsonParser.getValue(CoreConstants
							 * .KEY_NO_OF_ATTEMPTS)) + 1;
							 * meterReadingBO.setNoOfAttempts
							 * (String.valueOf(noOfAttempts)); }
							 */

							if (!StringUtil.isValid(jsonObject.getStringValue(CoreConstants.KEY_NO_OF_ATTEMPTS)))
							{
								noOfAttempts = 1;
								meterReadingBO.setNoOfAttempts(String.valueOf(noOfAttempts));
							}
							else
							{
								noOfAttempts = Integer.parseInt(jsonObject
										.getStringValue(CoreConstants.KEY_NO_OF_ATTEMPTS)) + 1;
								meterReadingBO.setNoOfAttempts(String.valueOf(noOfAttempts));
							}
						}
						else if (status.equals(Constants.FIELD_INCOMPLETE))
						// else
						{
							meterReadingBO.setLock("2");
							meterReadingBO.setMrReason(jsonObject.getStringValue(IMeterReading.KEY_MR_REASON));
							// int noOfAttempts =
							// Integer.parseInt(meterReadingBO.getNoOfAttempts())
							// + 1;
							MobiculeLogger.verbose("No of Attempts   get method  *********in json  "
									+ jsonObject.getStringValue(CoreConstants.KEY_NO_OF_ATTEMPTS));
							/*
							 * int noOfAttempts =
							 * Integer.parseInt(meterReadingBO
							 * .getNoOfAttempts()) + 1;
							 * meterReadingBO.setNoOfAttempts
							 * (String.valueOf(noOfAttempts));
							 */
							int noOfAttempts;
							if (!StringUtil.isValid(jsonObject.getStringValue(CoreConstants.KEY_NO_OF_ATTEMPTS)))
							{
								noOfAttempts = 1;
								meterReadingBO.setNoOfAttempts(String.valueOf(noOfAttempts));
							}
							else
							{
								noOfAttempts = Integer.parseInt(jsonObject
										.getStringValue(CoreConstants.KEY_NO_OF_ATTEMPTS));
								meterReadingBO.setNoOfAttempts(String.valueOf(noOfAttempts));
							}

							/*
							 * meterReadingBO.setLock("2");
							 * meterReadingBO.setMrReason
							 * (jsonParser.getValue(IMeterReading
							 * .KEY_MR_REASON)); //int noOfAttempts =
							 * Integer.parseInt
							 * (meterReadingBO.getNoOfAttempts()) + 1;
							 * "No of Attempts   get method  *********in json  "
							 * +
							 * jsonParser.getValue(CoreConstants.KEY_NO_OF_ATTEMPTS
							 * )); int noOfAttempts =
							 * Integer.parseInt(meterReadingBO
							 * .getNoOfAttempts()) + 1;
							 * meterReadingBO.setNoOfAttempts
							 * (String.valueOf(noOfAttempts)); int noOfAttempts;
							 * if (!StringUtil.isValid(jsonParser.getValue(
							 * CoreConstants.KEY_NO_OF_ATTEMPTS))) {
							 * noOfAttempts = 1;
							 * meterReadingBO.setNoOfAttempts(String
							 * .valueOf(noOfAttempts)); } else { noOfAttempts =
							 * Integer
							 * .parseInt(jsonParser.getValue(CoreConstants
							 * .KEY_NO_OF_ATTEMPTS));
							 * meterReadingBO.setNoOfAttempts
							 * (String.valueOf(noOfAttempts)); }
							 */
						}

						MobiculeLogger.verbose("No of Attempts   get method  *********  " + meterReadingBO.getNoOfAttempts());
						meterReadingFacade.saveMeterReading(meterReadingBO.toJSON().toString(), false);

					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void displayErrorDialog(Context context, String title, String message, int drawableicon)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);
		builder.setIcon(drawableicon);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setInverseBackgroundForced(true);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void displayDialog(Context context, String title, String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		final AlertDialog alertDialog = builder.create();
		alertDialog.setMessage(message);
		alertDialog.setTitle(title);
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}
}
