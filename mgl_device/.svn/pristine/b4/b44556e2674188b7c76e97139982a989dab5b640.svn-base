/**
 * 
 */
package com.mobicule.msales.mgl.client.timevariation;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.component.util.CoreMobiculeLogger;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingCommunication;

/**
 * @author nikita
 *
 */
public class DefaultTimeVariationFacade implements ITimeVariationFacade
{
	private final IApplicationFacade applicationFacade;

	private final IMeterReadingCommunication meterReadingCommunication;

	private TimeVariationRequestBuilder timevariationRequesrbuilder;

	private static DefaultTimeVariationFacade instance;

	private ITimeVariation timeVariation = null;

	JSONParser jsonParser;

	public DefaultTimeVariationFacade(IMeterReadingCommunication meterReadingCommunication,
			IApplicationFacade applicationFacade)
	{
		initmarketsurveyCycle();
		this.meterReadingCommunication = meterReadingCommunication;
		this.applicationFacade = applicationFacade;
		jsonParser = JSONParser.getInstance();
	}

	public synchronized static ITimeVariationFacade getInstance(IMeterReadingCommunication meterReadingCommunication,
			IApplicationFacade applicationFacade)
	{
		if (instance == null)
		{
			instance = new DefaultTimeVariationFacade(meterReadingCommunication, applicationFacade);
		}
		return instance;
	}

	private void initmarketsurveyCycle()
	{
		timeVariation = new DefaultTimeVariation();
	}

	public ITimeVariation getTimeVariationBO()
	{
		if (timeVariation == null)
		{
			initmarketsurveyCycle();
		}
		return timeVariation;
	}

	public Response submitTimeVariation()
	{
		JSONArray jsonarray;
		JSONObject jsonObject;
		Response response = applicationFacade.getUserDtail();
		try
		{

			jsonObject = new JSONObject(response.getData().toString());

			timevariationRequesrbuilder = new TimeVariationRequestBuilder(jsonObject, timeVariation);
			jsonarray = new JSONArray();

			CoreMobiculeLogger.log("Default Time variation  " + response);

			response = meterReadingCommunication.submit(timevariationRequesrbuilder.build());

			if (response.isSuccess())
			{
				return new Response(true, response.getMessage(), null);
			}
			else
			{
				return new Response(false, response.getMessage(), null);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return new Response(false, CoreConstants.EMPTY_STRING, null);
	}

	public ITimeVariation getTimeVariation()
	{
		if (timeVariation == null)
		{
			timeVariation = new DefaultTimeVariation();
		}
		return timeVariation;
	}

}
