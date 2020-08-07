/**
******************************************************************************
* C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
* <p>
* Copyright ? 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved. 
* This is proprietary information of Mobicule Technologies Pvt. Ltd.and is 
* subject to applicable licensing agreements. Unauthorized reproduction, 
* transmission or distribution of this file and its contents is a 
* violation of applicable laws.
******************************************************************************
*
* @project mgl-client-core
*/
package com.mobicule.msales.mgl.client.randommeterreading;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.component.string.StringUtil;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.component.util.CoreMobiculeLogger;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingCommunication;
import com.mobicule.msales.mgl.client.meterreading.MeterReadingRequestBuilder;
import com.mobicule.msales.mgl.client.randommeterreading.IRandomMeterReading.IRandomMeterReadingInstance;
import com.mobicule.versioncontrol.VersionControl;

/**
* 
* <enter description here>
*
* @author nikita
* @see 
*
* @createdOn Mar 20, 2012
* @modifiedOn
*
* @copyright ? 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class DefaultRandomMeterReadingFacade implements IRandomMeterReadingFacade
{
	private final IApplicationFacade applicationFacade;

	private final IRandomMeterReadingPersistance randomMeterReadingPersistance;

	private final IMeterReadingCommunication meterReadingCommunication;

	private IRandomMeterReadingInstance currentRandomMeterReading;

	private static DefaultRandomMeterReadingFacade instance;

	private MeterReadingRequestBuilder meterReadingRequestBuilder;

	private IRandomMeterReading randomMeterReading = null;

	JSONParser jsonParser;

	VersionControl versionControl;

	private DefaultRandomMeterReadingFacade(IRandomMeterReadingPersistance randomMeterReadingPersistance,
			IMeterReadingCommunication meterReadingCommunication, IApplicationFacade applicationFacade,VersionControl versionControl)
	{
		initRandomMeterReadingCycle();
		this.randomMeterReadingPersistance = randomMeterReadingPersistance;
		this.meterReadingCommunication = meterReadingCommunication;
		this.applicationFacade = applicationFacade;
		jsonParser = JSONParser.getInstance();
		this.versionControl = versionControl;
	}

	public synchronized static IRandomMeterReadingFacade getInstance(
			IRandomMeterReadingPersistance randomMeterReadingPersistance,
			IMeterReadingCommunication meterReadingCommunication, IApplicationFacade applicationFacade,VersionControl versionControl)
	{
		if (instance == null)
		{
			instance = new DefaultRandomMeterReadingFacade(randomMeterReadingPersistance, meterReadingCommunication,
					applicationFacade,versionControl);
		}
		return instance;
	}

	private synchronized void initRandomMeterReadingCycle()
	{
		currentRandomMeterReading = new DefaultRandomMeterReading.DefaultRandomMeterReadingInstance();
	}

	public synchronized void initRandomMeterReadingCycleWithSavedRandomMeterReading(String savedRandomMeterReading)
	{
		try
		{
			getRandomMeterReadingBO().init(new JSONObject(savedRandomMeterReading));
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	public synchronized IRandomMeterReadingInstance getRandomMeterReadingBO()
	{
		if (currentRandomMeterReading == null)
		{
			initRandomMeterReadingCycle();
		}
		return currentRandomMeterReading;
	}

	public synchronized Response submitOneRandomMeterReading(String ramdomMeterReadingString)
	{
		JSONObject jsonObject;
		Response response = applicationFacade.getUserDtail();
		try
		{

			jsonObject = new JSONObject(response.getData().toString());

			Vector savedMeterReadings = fetchAllSavedMeterReadings();

			if (ramdomMeterReadingString != null && ramdomMeterReadingString.length() > 0)
			{
				JSONObject savedJson = null;

				savedJson = new JSONObject(ramdomMeterReadingString);
				getRandomMeterReadingBO().init(savedJson);
				setRandomMeterReadingInstance(getRandomMeterReadingBO());

				meterReadingRequestBuilder = new RandomMeterReadingRequestBuilder(jsonObject,
						randomMeterReading.toJsonArray());
				JSONObject jsonSubmit = new JSONObject(meterReadingRequestBuilder.build().toString());

				JSONObject jsonRequest = versionControl.attachVersionControlInfo(jsonSubmit);
				response = meterReadingCommunication.submit(jsonRequest);

				/*JSONObject jsonRMR = new JSONObject(response.getData().toString());

				String status = jsonRMR.getString(CoreConstants.USER_RESPONSE_STATUS);

				if (response.isSuccess() == false && status.equalsIgnoreCase(CoreConstants.VERSION_CHANGE))
				{
					return new Response(false, StringUtil.getMessage(response.getMessage()), response.getData());
				}
				else*/ if (response.isSuccess())
				{
					//editSavedMeterReading(true, ramdomMeterReadingString);
					//getRandomMeterReading().reset();
					return new Response(true, response.getMessage(), null);
				}
				else
				{
					//editSavedMeterReading(false, ramdomMeterReadingString);
					//getRandomMeterReading().reset();
					return new Response(false, response.getMessage(), null);
				}
			}

		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return new Response(false, CoreConstants.EMPTY_STRING, null);
	}

	public synchronized Response submitRandomMeterReading()
	{
		JSONObject jsonObject;
		Response response = applicationFacade.getUserDtail();
		try
		{

			jsonObject = new JSONObject(response.getData().toString());

			Vector savedMeterReadings = fetchAllSavedMeterReadings();

			if (savedMeterReadings != null && savedMeterReadings.size() > 0)
			{
				JSONObject savedJson = null;
				for (int i = 0; i < savedMeterReadings.size(); i++)
				{
					savedJson = new JSONObject(savedMeterReadings.elementAt(i).toString());
					getRandomMeterReadingBO().init(savedJson);
					setRandomMeterReadingInstance(getRandomMeterReadingBO());
				}
				meterReadingRequestBuilder = new RandomMeterReadingRequestBuilder(jsonObject,
						randomMeterReading.toJsonArray());
				JSONObject jsonSubmit = new JSONObject(meterReadingRequestBuilder.build().toString());

				JSONObject jsonRequest = versionControl.attachVersionControlInfo(jsonSubmit);
				response = meterReadingCommunication.submit(jsonRequest);

				 if (response.isSuccess())
				{
					editSavedMeterReading(true);
					getRandomMeterReading().reset();
					return new Response(true, response.getMessage(), null);
				}
				else
				{
					getRandomMeterReading().reset();
					return new Response(false, response.getMessage(), null);
				}
			}

		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return new Response(false, CoreConstants.EMPTY_STRING, null);
	}

	public Response submitOneRandomMeterReadingFromReceiver()
	{
		Vector isSuccessSubmitV = null;
		try
		{
			Vector savedMeterReadings = fetchAllSavedMeterReadings();

			//System.out.println("submitOneRandomMeterReadingFromReceiver: savedMeterReadings: "+ savedMeterReadings.size());

			if (savedMeterReadings != null && savedMeterReadings.size() > 0)
			{
				Response response = applicationFacade.getUserDtail();
				JSONObject jsonObject = new JSONObject(response.getData().toString());

				isSuccessSubmitV = new Vector();
				JSONArray reqestJsonArray = new JSONArray();
				for (int i = 0; i < savedMeterReadings.size(); i++)
				{
					String ramdomMeterReadingString = savedMeterReadings.elementAt(i).toString();
					JSONObject savedJson = new JSONObject(ramdomMeterReadingString);
					reqestJsonArray.add(savedJson);
					/*getRandomMeterReadingBO().init(savedJson);
					setRandomMeterReadingInstance(getRandomMeterReadingBO());*/

					//meterReadingRequestBuilder = new RandomMeterReadingRequestBuilder(jsonObject, randomMeterReading);
					meterReadingRequestBuilder = new RandomMeterReadingRequestBuilder(jsonObject, reqestJsonArray);
					JSONObject jsonSubmit = new JSONObject(meterReadingRequestBuilder.build().toString());

					JSONObject jsonRequest = versionControl.attachVersionControlInfo(jsonSubmit);
					response = meterReadingCommunication.submit(jsonRequest);

					if (response.isSuccess())
					{
						editSavedMeterReading(true, ramdomMeterReadingString);
						isSuccessSubmitV.addElement("true");
						getRandomMeterReading().reset();
					}
					else
					{
						editSavedMeterReading(false, ramdomMeterReadingString);
						isSuccessSubmitV.addElement("false");
						getRandomMeterReading().reset();
						//return new Response(false, StringUtil.getMessage(response.getMessage()), response.getData());
					}
					reqestJsonArray.remove(savedJson);
				}
			}

		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		CoreMobiculeLogger.log("submitOneRandomMeterReadingFromReceiver: isSuccessSubmitV: " + isSuccessSubmitV);

		if (isSuccessSubmitV != null && isSuccessSubmitV.size() > 0)
		{
			if (isSuccessSubmitV.contains("false"))
			{
				return new Response(false, CoreConstants.EMPTY_STRING, null);
			}
			else
			{
				applicationFacade.dropTable(CoreConstants.TABLE_RANDOM_METER_READING);
				return new Response(true, CoreConstants.EMPTY_STRING, null);
			}
		}
		return new Response(false, CoreConstants.EMPTY_STRING, null);
	}

	private synchronized void editSavedMeterReading(boolean isReadingSubmittedToServer, String meterReading)
	{
		try
		{
			JSONObject savedJson = new JSONObject(meterReading);
			//getRandomMeterReadingBO().init(new JSONObject(meterReading));

			if (isReadingSubmittedToServer)
			{
				savedJson.put(CoreConstants.KEY_RMR_SUBMIT, "1");
				//getRandomMeterReadingBO().setMrSubmitted("1");
			}
			else
			{
				savedJson.put(CoreConstants.KEY_RMR_SUBMIT, "0");
				//getRandomMeterReadingBO().setMrSubmitted("0");//saved by default
			}
			/*randomMeterReadingPersistance.saveRandomMeterReading(CoreConstants.TABLE_RANDOM_METER_READING,
					getRandomMeterReadingBO().toJSON().toString());*/
			String savedJsonValue = savedJson.toString();
			if (StringUtil.isValid(savedJsonValue))
			{
				randomMeterReadingPersistance.saveRandomMeterReading(CoreConstants.TABLE_RANDOM_METER_READING,
						savedJsonValue);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private synchronized void editSavedMeterReading(boolean isReadingSubmittedToServer)
	{
		try
		{
			JSONArray jsonArray = randomMeterReading.toJsonArray();
			JSONObject savedJson = null;
			for (int i = 0; i < jsonArray.length(); i++)
			{
				savedJson = jsonArray.getJSONObject(i);
				getRandomMeterReadingBO().init(savedJson);
				if (isReadingSubmittedToServer)
				{
					getRandomMeterReadingBO().setMrSubmitted("1");
				}
				else
				{
					getRandomMeterReadingBO().setMrSubmitted("0");//saved by default
				}
				randomMeterReadingPersistance.saveRandomMeterReading(CoreConstants.TABLE_RANDOM_METER_READING,
						getRandomMeterReadingBO().toJSON().toString());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public synchronized Vector fetchAllSavedMeterReadings()
	{
		return randomMeterReadingPersistance.fetchAllSavedRandomMeterReadings(CoreConstants.TABLE_RANDOM_METER_READING);
	}

	public synchronized Response saveRandomMeterReading(boolean isReadingSubmittedToServer)
	{
		if (isReadingSubmittedToServer)
		{
			getRandomMeterReadingBO().setMrSubmitted("1");
		}
		else
		{
			getRandomMeterReadingBO().setMrSubmitted("0");//saved by default
		}
		String mrNumber = getRandomMeterReadingBO().getMrSubmitted();
		
		//System.out.println("mr number is::::  ^^^  " + mrNumber);
		CoreMobiculeLogger.log("saveMeterReading CurrentMeterBo" + getRandomMeterReadingBO().toJSON().toString());
		
		randomMeterReadingPersistance.saveRandomMeterReading(CoreConstants.TABLE_RANDOM_METER_READING,
				getRandomMeterReadingBO().toJSON().toString());
		
		//applicationFacade.updateBookWalkStatus(getRandomMeterReadingBO().toJSON().toString());
		
		return new Response(true, "Random Meter reading saved successfully", null);
	}

	public synchronized Response saveRandomMeterReading()
	{
		randomMeterReadingPersistance.saveRandomMeterReading(CoreConstants.TABLE_RANDOM_METER_READING,
				randomMeterReading.toJsonArray().toString());
		//applicationFacade.updateBookWalkStatus(randomMeterReading.toJsonArray().toString());
		return new Response(true, "Random Meter Reading Saved Successfully", null);

	}

	public synchronized Vector fetchSavedRandomMeterReading(String customerName)
	{
		return randomMeterReadingPersistance.fetchSavedRandomMeterReading(CoreConstants.TABLE_RANDOM_METER_READING,
				customerName);
	}

	public synchronized IRandomMeterReading setRandomMeterReadingInstance(IRandomMeterReadingInstance instance)
	{
		getRandomMeterReading();
		randomMeterReading.setRandomMeterReadingInstance(instance);
		return randomMeterReading;
	}

	public synchronized IRandomMeterReading getRandomMeterReading()
	{
		if (randomMeterReading == null)
		{
			randomMeterReading = new DefaultRandomMeterReading();
		}
		return randomMeterReading;
	}

	public synchronized IRandomMeterReadingInstance resetrandomMeterReadingInstanceBO()
	{
		initRandomMeterReadingCycle();
		return currentRandomMeterReading;
	}

	public synchronized Response getOfflineRandomMeterReadingDetail()
	{
		Vector data = randomMeterReadingPersistance.getOfflineRandomMeterReadingDetail();
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}

}
