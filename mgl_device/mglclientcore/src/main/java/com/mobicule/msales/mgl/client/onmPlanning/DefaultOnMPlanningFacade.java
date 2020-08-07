package com.mobicule.msales.mgl.client.onmPlanning;

import java.util.ArrayList;
import java.util.Vector;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mobicule.component.string.StringUtil;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.component.util.CoreMobiculeLogger;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IMeterReadingInstance;
import com.mobicule.msales.mgl.client.updatecustomer.DefaultUpdateCustomerFacade;
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomer;
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomerCommunication;
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomerFacade;
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomerPersistance;
import com.mobicule.msales.mgl.client.updatecustomer.UpdateCustomerRequestBuilder;
import com.mobicule.versioncontrol.VersionControl;

public class DefaultOnMPlanningFacade implements IOnMFacade
{

	private final IApplicationFacade applicationFacade;

	private IOnMPersistance onMPersistance;

	private final IOnMCommunication onMCommunication;

	OnMPlanningRequestBuilder onMPlanningRequestBuilder;

	private static DefaultOnMPlanningFacade instance;

	IOnMPlanning iomPlanning = null;

	VersionControl versionControl;

	public DefaultOnMPlanningFacade(IOnMPersistance onmPersistance, IOnMCommunication onMCommunication,
			IApplicationFacade applicationFacade, VersionControl versionControl)
	{
		initmarketsurveyCycle();
		this.applicationFacade = applicationFacade;
		this.onMCommunication = onMCommunication;
		this.onMPersistance = onmPersistance;
		this.versionControl = versionControl;
	}

	public synchronized static IOnMFacade getInstance(IOnMPersistance onmPersistance,
			IOnMCommunication onMCommunication, IApplicationFacade applicationFacade, VersionControl versionControl)
	{
		if (instance == null)
		{
			instance = new DefaultOnMPlanningFacade(onmPersistance, onMCommunication, applicationFacade, versionControl);
		}
		return instance;
	}

	public void initmarketsurveyCycle()
	{
		iomPlanning = new DefaultOnMPlanning();
	}

	public synchronized IOnMPlanning resetOnMPlanningBO()
	{
		initmarketsurveyCycle();
		return iomPlanning;
	}

	@Override
	public IOnMPlanning getOnMPlanningBO()
	{
		if (iomPlanning == null)
		{
			initmarketsurveyCycle();
		}
		return iomPlanning;
	}

	@Override
	public Response OnMCustomerDetails()
	{
		Vector isSuccessSubmitV = null;
		try
		{
			Vector savedMeterReadings = fetchAllSavedOnMMroNo();

			if (savedMeterReadings != null && savedMeterReadings.size() > 0)
			{
				Response response = applicationFacade.getUserDtail();
				JSONObject jsonObject = new JSONObject(response.getData().toString());

				isSuccessSubmitV = new Vector();

				for (int i = 0; i < savedMeterReadings.size(); i++)
				{
					if (savedMeterReadings.elementAt(i) != null)
					{
						String mroNo = savedMeterReadings.elementAt(i).toString();

						CoreMobiculeLogger.log("updateCustomerDetails mroNo: " + mroNo);

						Vector savedRandomMeterReading = fetchSavedOnM(mroNo);

						JSONObject savedJson = new JSONObject(savedRandomMeterReading.elementAt(0).toString());
						//getCustomerUpdateBO().init(savedJson);

						onMPlanningRequestBuilder = new OnMPlanningRequestBuilder(jsonObject, savedJson);
						CoreMobiculeLogger.log("updateCustomerDetails :  meterReadingRequestBuilder is :  "+ onMPlanningRequestBuilder.build());

						JSONObject jsonObj = new JSONObject(onMPlanningRequestBuilder.build().toString());

						//versionControl = VersionControl.getInstance();
						JSONObject jsonUpdate = versionControl.attachVersionControlInfo(jsonObj);

						response = onMCommunication.submit(jsonUpdate);

						if (response.isSuccess())
						{
							editSavedOnMReading(true, savedJson);
							isSuccessSubmitV.addElement("true");
							/*if (i == savedMeterReadings.size() - 1)
							{
								return new Response(true, response.getMessage(), null);
							}*/
						}
						else
						{
							//break;
							isSuccessSubmitV.addElement("false");
							//return new Response(false, StringUtil.getMessage(response.getMessage()), response.getData());
						}
					}
				}
			}

		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		if (isSuccessSubmitV != null && isSuccessSubmitV.size() > 0)
		{
			if (isSuccessSubmitV.contains("false"))
			{
				return new Response(false, CoreConstants.EMPTY_STRING, null);
			}
			else
			{
				//applicationFacade.dropTable(CoreConstants.TABLE_OnM_PLANNING);
				return new Response(true, CoreConstants.EMPTY_STRING, null);
			}
		}
		return new Response(false, CoreConstants.EMPTY_STRING, null);

	}

	@Override
	public Response OnMOneCustomerDetails(String customerDetails)
	{
		JSONObject jsonObject;
		Response response;

		try
		{
			response = applicationFacade.getUserDtail();
			jsonObject = new JSONObject(response.getData().toString());

			if (customerDetails != null && customerDetails.length() > 0)
			{
				JSONObject savedJson = null;

				CoreMobiculeLogger.log("saved reading " + customerDetails.toString());
				
				savedJson = new JSONObject(customerDetails);
				//getCustomerUpdateBO().init(savedJson);

				onMPlanningRequestBuilder = new OnMPlanningRequestBuilder(jsonObject, savedJson);

				CoreMobiculeLogger.log("Default Meter Reading :  meterReadingRequestBuilder is:  "+ onMPlanningRequestBuilder.build());

				JSONObject jsonObj = new JSONObject(onMPlanningRequestBuilder.build().toString());

				//versionControl = VersionControl.getInstance();
				JSONObject jsonUpdate = versionControl.attachVersionControlInfo(jsonObj);
				response = onMCommunication.submit(jsonUpdate);

				/*JSONObject jsonUpd=new JSONObject(response.getData().toString());
				
				String status=jsonUpd.getString(CoreConstants.USER_RESPONSE_STATUS);
				
				if (response.isSuccess() == false && status.equalsIgnoreCase(CoreConstants.VERSION_CHANGE))
				{
					return new Response(false, StringUtil.getMessage(response.getMessage()), response.getData());
				}
				else*/if (response.isSuccess())
				{
					//editSavedMeterReading(true);
					return new Response(true, response.getMessage(), null);
				}
				else
				{
					//editSavedMeterReading(false);
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

	@Override
	public IUpdateCustomer getOnMPlanning()
	{
		return null;
	}

	@Override
	public Response saveOnMPlanning(boolean isReadingSubmittedToServer)
	{
		if (isReadingSubmittedToServer)
		{
			getOnMBO().setMrSubmit("1");
		}
		else
		{
			getOnMBO().setMrSubmit("0");//saved by default
		}
		
		CoreMobiculeLogger.log("saveOnMPlanning CurrentOnMBo" + getOnMPlanningBO().toJSON().toString());

		onMPersistance.saveOnMReading(CoreConstants.TABLE_OnM_PLANNING, getOnMPlanningBO().toJSON().toString()); // Change table name
		return new Response(true, "Other information submitted successfully", null);
	}

	@Override
	public Vector fetchSavedOnM(String mroNo)
	{
		return onMPersistance.fetchSavedOnMReading(CoreConstants.TABLE_OnM_PLANNING, mroNo); /// ----------- Change Table name 
	}

	@Override
	public Vector fetchAllSavedOnMMroNo()
	{
		return onMPersistance.fetchAllSavedOnMReadingMroNo(CoreConstants.TABLE_OnM_PLANNING); // ---------------- Change table name
	}

	@Override
	public Response getOfflineOnMPlanningDetail()
	{
		ArrayList<String> data = onMPersistance.getOfflineOnMReadingDetail();
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}

	@Override
	public void initCustomerCycleWithSavedOnMPlanning(String savedCustomerUpdate)
	{
		try
		{
			getOnMBO().init();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	public synchronized IOnMPlanning getOnMBO()
	{
		if (iomPlanning == null)
		{
			initmarketsurveyCycle();
		}
		return iomPlanning;
	}

	private synchronized void editSavedOnMReading(boolean isReadingSubmittedToServer, JSONObject saveJsonObject)
	{
		try
		{
			if (isReadingSubmittedToServer)
			{
				saveJsonObject.put(CoreConstants.KEY_MR_SUBMIT, "1"); // ------------ change key name
				//getCustomerUpdateBO().setMrSubmitted("1");
			}
			else
			{
				saveJsonObject.put(CoreConstants.KEY_MR_SUBMIT, "0"); // ------------ change key name
				//getCustomerUpdateBO().setMrSubmitted("0");//saved by default
			}
			/*updateCustomerPersistance.saveUpdateCustomer(CoreConstants.TABLE_CUSTOMER_UPDATE, getCustomerUpdateBO()
					.toJSON().toString());*/
			onMPersistance.saveOnMReading(CoreConstants.TABLE_OnM_PLANNING, saveJsonObject.toString()); // ------------- change table name

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
