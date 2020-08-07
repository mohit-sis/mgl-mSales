/**
 * 
 */
package com.mobicule.msales.mgl.client.updatecustomer;

import java.util.Vector;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.component.string.StringUtil;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.component.util.CoreMobiculeLogger;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.meterreading.DefaultMeterReading;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IMeterReadingInstance;
import com.mobicule.versioncontrol.VersionControl;

/**
 * @author nikita
 *
 */
public class DefaultUpdateCustomerFacade implements IUpdateCustomerFacade
{

	private final IApplicationFacade applicationFacade;

	private IUpdateCustomerPersistance updateCustomerPersistance;

	private final IUpdateCustomerCommunication updateCustomerCommunication;

	private UpdateCustomerRequestBuilder updateCustomerRequestBuilder;

	private static DefaultUpdateCustomerFacade instance;

	private IUpdateCustomer updateCustomer = null;
	
	private IMeterReadingInstance currentMeterReading;

	JSONParser jsonParser;
	
	private VersionControl versionControl;

	public DefaultUpdateCustomerFacade(IUpdateCustomerPersistance updateCustomerPersistance,
			IUpdateCustomerCommunication updateCustomerCommunication, IApplicationFacade applicationFacade,VersionControl versionControl)
	{
		initmarketsurveyCycle();
		this.updateCustomerPersistance = updateCustomerPersistance;
		this.updateCustomerCommunication = updateCustomerCommunication;
		this.applicationFacade = applicationFacade;
		jsonParser = JSONParser.getInstance();
		this.versionControl = versionControl;
	}

	public synchronized static IUpdateCustomerFacade getInstance(IUpdateCustomerPersistance updateCustomerPersistance,
			IUpdateCustomerCommunication updateCustomerCommunication, IApplicationFacade applicationFacade,VersionControl versionControl)
	{
		if (instance == null)
		{
			instance = new DefaultUpdateCustomerFacade(updateCustomerPersistance, updateCustomerCommunication,
					applicationFacade,versionControl);
		}
		return instance;
	}

	private synchronized void initMeterReadingCycle()
	{
		currentMeterReading = new DefaultMeterReading.DefaultMeterReadingInstance();
	}

	private synchronized void initmarketsurveyCycle()
	{
		updateCustomer = new DefaultUpdateCustomer();
	}

	public synchronized void initCustomerUpdateCycleWithSavedCustomerUpdate(String savedCustomerUpdate)
	{
		try
		{
			getCustomerUpdateBO().init();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	public synchronized IUpdateCustomer getCustomerUpdateBO()
	{
		if (updateCustomer == null)
		{
			initmarketsurveyCycle();
		}
		return updateCustomer;
	}

	public synchronized IMeterReadingInstance getCurrentMeterReadingBO()
	{
		if (currentMeterReading == null)
		{
			initMeterReadingCycle();
		}
		return currentMeterReading;
	}
	public synchronized Response updateOneCustomerDetails(String customerDetails)
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

				updateCustomerRequestBuilder = new UpdateCustomerRequestBuilder(jsonObject, savedJson);

				CoreMobiculeLogger.log("Default Meter Reading :  meterReadingRequestBuilder is:  " + updateCustomerRequestBuilder.build());
				
				JSONObject jsonObj = new JSONObject(updateCustomerRequestBuilder.build().toString());

				//versionControl = VersionControl.getInstance();
				JSONObject jsonUpdate = versionControl.attachVersionControlInfo(jsonObj);
				response = updateCustomerCommunication.submit(jsonUpdate);

				/*JSONObject jsonUpd=new JSONObject(response.getData().toString());
				
				String status=jsonUpd.getString(CoreConstants.USER_RESPONSE_STATUS);
				
				if (response.isSuccess() == false && status.equalsIgnoreCase(CoreConstants.VERSION_CHANGE))
				{
					return new Response(false, StringUtil.getMessage(response.getMessage()), response.getData());
				}
				else*/ if (response.isSuccess())
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

	/*
		public Response updateCustomerDetails()
		{
			Vector isSuccessSubmitV = null;
			try
			{
				Vector savedMeterReadings = fetchAllSavedCustomerUpdate();

				System.out.println("updateCustomerDetails: savedMeterReadings: " + savedMeterReadings.size());

				if (savedMeterReadings != null && savedMeterReadings.size() > 0)
				{
					Response response = applicationFacade.getUserDtail();
					JSONObject jsonObject = new JSONObject(response.getData().toString());

					isSuccessSubmitV = new Vector();
					
					for (int i = 0; i < savedMeterReadings.size(); i++)
					{
						JSONObject savedJson = new JSONObject(savedMeterReadings.elementAt(i).toString());
						getCustomerUpdateBO().init(savedJson);

						updateCustomerRequestBuilder = new UpdateCustomerRequestBuilder(jsonObject, updateCustomer);
						System.out.println("updateCustomerDetails :  meterReadingRequestBuilder is:  "
								+ updateCustomerRequestBuilder.build());
						response = updateCustomerCommunication.submit(updateCustomerRequestBuilder.build());

						if (response.isSuccess())
						{
							editSavedMeterReading(true);
							isSuccessSubmitV.addElement("true");
							if (i == savedMeterReadings.size() - 1)
							{
								return new Response(true, response.getMessage(), null);
							}
						}
						else
						{
							//break;
							isSuccessSubmitV.addElement("false");
						}
					}
				}

			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
			System.out.println("updateCustomerDetails: isSuccessSubmitV: " + isSuccessSubmitV);

			if(isSuccessSubmitV!=null && isSuccessSubmitV.size()>0)
			{
				if(isSuccessSubmitV.contains("false"))
				{
					return new Response(false, CoreConstants.EMPTY_STRING, null);
				}
				else
				{
					applicationFacade.dropTable(CoreConstants.TABLE_CUSTOMER_UPDATE);
					return new Response(true, CoreConstants.EMPTY_STRING, null);
				}
			}
			return new Response(false, CoreConstants.EMPTY_STRING, null);
		}
	*/

	public Response updateCustomerDetails()
	{
		Vector isSuccessSubmitV = null;
		try
		{
			Vector savedMeterReadings = fetchAllSavedCustomerUpdateMroNo();
			
			if (savedMeterReadings != null && savedMeterReadings.size() > 0)
			{
				Response response = applicationFacade.getUserDtail();
				JSONObject jsonObject = new JSONObject(response.getData().toString());

				isSuccessSubmitV = new Vector();

				for (int i = 0; i < savedMeterReadings.size(); i++)
				{
					String mroNo = savedMeterReadings.elementAt(i).toString();
				
					Vector savedRandomMeterReading = fetchSavedCustomerUpdate(mroNo);					
					
					JSONObject savedJson = new JSONObject(savedRandomMeterReading.elementAt(0).toString());
					//getCustomerUpdateBO().init(savedJson);

					updateCustomerRequestBuilder = new UpdateCustomerRequestBuilder(jsonObject, savedJson);
					
					CoreMobiculeLogger.log("updateCustomerDetails :  meterReadingRequestBuilder is :  "+ updateCustomerRequestBuilder.build());
					
					JSONObject jsonObj = new JSONObject(updateCustomerRequestBuilder.build().toString());

					//versionControl = VersionControl.getInstance();
					JSONObject jsonUpdate = versionControl.attachVersionControlInfo(jsonObj);
					
					response = updateCustomerCommunication.submit(jsonUpdate);
					
					 if (response.isSuccess())
					{
						editSavedMeterReading(true, savedJson);
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
						return new Response(false, StringUtil.getMessage(response.getMessage()), response.getData());
					}
				}
			}

		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		CoreMobiculeLogger.log("updateCustomerDetails: isSuccessSubmitV: " + isSuccessSubmitV);

		if (isSuccessSubmitV != null && isSuccessSubmitV.size() > 0)
		{
			if (isSuccessSubmitV.contains("false"))
			{
				return new Response(false, CoreConstants.EMPTY_STRING, null);
			}
			else
			{
				applicationFacade.dropTable(CoreConstants.TABLE_CUSTOMER_UPDATE);
				return new Response(true, CoreConstants.EMPTY_STRING, null);
			}
		}
		return new Response(false, CoreConstants.EMPTY_STRING, null);
	}

	private synchronized void editSavedMeterReading(boolean isReadingSubmittedToServer, JSONObject saveJsonObject)
	{
		try
		{
			if (isReadingSubmittedToServer)
			{
				saveJsonObject.put(CoreConstants.KEY_UPDATECUSTOMER_SUBMIT, "1");
				//getCustomerUpdateBO().setMrSubmitted("1");
			}
			else
			{
				saveJsonObject.put(CoreConstants.KEY_UPDATECUSTOMER_SUBMIT, "0");
				//getCustomerUpdateBO().setMrSubmitted("0");//saved by default
			}
			/*updateCustomerPersistance.saveUpdateCustomer(CoreConstants.TABLE_CUSTOMER_UPDATE, getCustomerUpdateBO()
					.toJSON().toString());*/
			updateCustomerPersistance
					.saveUpdateCustomer(CoreConstants.TABLE_CUSTOMER_UPDATE, saveJsonObject.toString());

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
			if (isReadingSubmittedToServer)
			{
				getCustomerUpdateBO().setMrSubmitted("1");
			}
			else
			{
				getCustomerUpdateBO().setMrSubmitted("0");//saved by default
			}
			updateCustomerPersistance.saveUpdateCustomer(CoreConstants.TABLE_CUSTOMER_UPDATE, getCustomerUpdateBO()
					.toJSON().toString());
			//applicationFacade.updateBookWalkStatus(randomMeterReading.toJsonArray().toString());
			//return new Response(true, "Random Meter Reading Saved Successfully", null);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public synchronized Vector fetchAllSavedCustomerUpdateMroNo()
	{
		return updateCustomerPersistance.fetchAllSavedUpdateCustomerMroNo(CoreConstants.TABLE_CUSTOMER_UPDATE);
	}

	public synchronized Response saveCustomerUpdate(boolean isReadingSubmittedToServer)
	{
		if (isReadingSubmittedToServer)
		{
			getCustomerUpdateBO().setMrSubmitted("1");
		}
		else
		{
			getCustomerUpdateBO().setMrSubmitted("0");//saved by default
		}
		
		CoreMobiculeLogger.log("saveMeterReading CurrentMeterBo" + getCustomerUpdateBO().toJSON().toString());
	
		updateCustomerPersistance.saveUpdateCustomer(CoreConstants.TABLE_CUSTOMER_UPDATE, getCustomerUpdateBO()
				.toJSON().toString());
		return new Response(true, "Customer Updated Successfully", null);
	}

	public synchronized Vector fetchSavedCustomerUpdate(String mroNo)
	{
		return updateCustomerPersistance.fetchSavedUpdateCustomer(CoreConstants.TABLE_CUSTOMER_UPDATE, mroNo);
	}

	public synchronized IUpdateCustomer getCustomerUpdate()
	{
		if (updateCustomer == null)
		{
			updateCustomer = new DefaultUpdateCustomer();
		}
		return updateCustomer;
	}

	public Response getOfflineUpdateCustomerDetail()
	{
		Vector data = updateCustomerPersistance.getOfflineUpdateCustomerDetail();
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}
}
