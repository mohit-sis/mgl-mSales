/**
******************************************************************************
* C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
* <p>
* Copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved. 
* This is proprietary information of Mobicule Technologies Pvt. Ltd.and is 
* subject to applicable licensing agreements. Unauthorized reproduction, 
* transmission or distribution of this file and its contents is a 
* violation of applicable laws.
******************************************************************************
*
* @project mgl-client-core
*/
package com.mobicule.msales.mgl.client.jointicketing;

import java.util.List;
import java.util.Vector;

import org.json.me.JSONObject;

import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.component.string.StringUtil;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.component.util.CoreMobiculeLogger;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.versioncontrol.VersionControl;

/**
* 
* <enter description here>
*
* @author Ashish Shukla
* @see 
*
* @createdOn 15-Apr-2014
* @modifiedOn
*
* @copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class DefaultJoinTicketingFacade implements IJoinTicketingFacade
{
	private final IApplicationFacade applicationFacade;

	private final IJoinTicketingPersistance joinTicketingPersistance;

	private final IJoinTicketingCommunication joinTicketingCommunication;

	private JoinTicketingRequestBuilder joinTicketingRequestBuilder;

	private IJoinTicketing currentJoinTicketing;

	private static DefaultJoinTicketingFacade instance;

	private IJoinTicketing joinTicketing = null;

	JSONParser jsonParser;

	private VersionControl versionControl;

	private DefaultJoinTicketingFacade(IJoinTicketingPersistance joinTicketingPersistance,
			IJoinTicketingCommunication joinTicketingCommunication, IApplicationFacade applicationFacade,VersionControl versionControl)
	{
		initJoinTicketingCycle();
		this.joinTicketingPersistance = joinTicketingPersistance;
		this.joinTicketingCommunication = joinTicketingCommunication;
		this.applicationFacade = applicationFacade;
		jsonParser = JSONParser.getInstance();
		this.versionControl = versionControl;
	}

	private synchronized void initJoinTicketingCycle()
	{
		currentJoinTicketing = new DefaultJoinTicketing().getInstance();
	}

	public synchronized static IJoinTicketingFacade getInstance(IJoinTicketingPersistance joinTicketingPersistance,
			IJoinTicketingCommunication joinTicketingCommunication, IApplicationFacade applicationFacade,VersionControl versionControl)
	{
		if (instance == null)
		{
			instance = new DefaultJoinTicketingFacade(joinTicketingPersistance, joinTicketingCommunication,
					applicationFacade,versionControl);
		}
		return instance;
	}

	public synchronized void initJoinTicketingCycleWithSavedJoinTicketing(String savedJoinTicketing)
	{
		try
		{
			getCurrentJoinTicketingBO().init(new JSONObject(savedJoinTicketing));
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	public synchronized IJoinTicketing getCurrentJoinTicketingBO()
	{
		if (currentJoinTicketing == null)
		{
			initJoinTicketingCycle();
		}
		return currentJoinTicketing;
	}

	public synchronized IJoinTicketing setJoinTicketingInstance(IJoinTicketing instance)
	{
		getJoinTicketing().setJoinTicketingInstance(instance);
		return joinTicketing;
	}

	public synchronized Response getSavedBuildingList()
	{
		Vector data = joinTicketingPersistance.getSavedBuildingList();
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}

	public synchronized Response getSavedCustomerList(String connObj)
	{
		Vector data = joinTicketingPersistance.getSavedCustomerList(connObj);
		if (data != null && data.size() > 0)
		{
			return new Response(true, "", data);
		}
		return new Response(false, "", null);
	}

	public Response submitOneJoinTicketingFromRecevier()
	{
		try
		{
			//versionControl = VersionControl.getInstance();
			if (CoreConstants.isAutotriggerOn())
			{
				return new Response(false, CoreConstants.EMPTY_STRING, null);
			}

			List<String> savedMroNo = fetchAllSavedJoinTicketingsOnlyMroNums();
			if (savedMroNo.size() > 0)
			{
				CoreMobiculeLogger.log("submitOneJoinTicketingFromRecevier: savedMroNo: " + savedMroNo.size());

				for (int i = 0; i < savedMroNo.size(); i++)
				{
					List<String> data = getJoinTicketingDataByMro(savedMroNo.get(i).toString());
					CoreMobiculeLogger.log("submitOneJoinTicketingFromRecevier()" + savedMroNo.get(i).toString()+ "data..." + data.size());

					for (int j = 0; j < data.size(); j++)
					{

						JSONObject json = new JSONObject(data.get(j).toString());
						JSONObject jsonJT = versionControl.attachVersionControlInfo(json);
						Response response = joinTicketingCommunication.submitString(jsonJT.toString());

						CoreMobiculeLogger.log("Join Ticketing response after submit    : " + response);

						
						 if (response.isSuccess())
						{

							editSavedJoinTicketing(true, data.get(j).toString());
						}
						else
						{
							if(response.getMessage().equalsIgnoreCase("Data Already Submitted"))
							{
								editSavedJoinTicketing(true, data.get(j).toString());
							}
							else
							{
							    editSavedJoinTicketing(false, data.get(j).toString());
							}
							//return new Response(false, StringUtil.getMessage(response.getMessage()), response.getData());
						}

						 CoreMobiculeLogger.log(i + "     One join Ticketing Json after submit    : " + data.get(j));

						if (CoreConstants.isAutotriggerOn())
						{
							return new Response(false, CoreConstants.EMPTY_STRING, null);
						}
					}

					if (CoreConstants.isAutotriggerOn())
					{
						return new Response(false, CoreConstants.EMPTY_STRING, null);
					}
				}
			}

			/*
			 * Vector isSuccessSubmitV = null;
			 * 
			 * if (CoreConstants.isAutotriggerOn())
			{
				return new Response(false, CoreConstants.EMPTY_STRING, null);
			}
			//Vector savedMeterReadings = fetchAllSavedMeterReadings();
			Vector savedJoinTicketings = fetchAllSavedJoinTicketingsOnlyMroNums();

			System.out.println("submitOneJoinTicketingFromRecevier: savedJoinTicketings: " + savedJoinTicketings.size());
			
			if (savedJoinTicketings != null && savedJoinTicketings.size() > 0)
			{
				Response response = applicationFacade.getUserDtail();
				JSONObject userJson = new JSONObject(response.getData().toString());

				isSuccessSubmitV = new Vector();
				StringBuffer joinTicketingBuffer = new StringBuffer();
				for (int i = 0; i < savedJoinTicketings.size(); i++)
				{
					
					//String oneMeterReading = savedMeterReadings.elementAt(i).toString();
					
					jsonParser.setJson(savedJoinTicketings.elementAt(i).toString());
					String mroNumber = jsonParser.getValue("mroNo");
					System.out.println("MRO NUMBER " + mroNumber);
					String oneJoinTicketing = "";
					Vector oneJoinTicketingVector = fetchSavedJoinTicketing(mroNumber);
					if(oneJoinTicketingVector != null && oneJoinTicketingVector.size() > 0)
					{
						oneJoinTicketing = fetchSavedJoinTicketing(mroNumber).elementAt(0).toString();	
					}
					else
					{
						System.out.println("NOT FOUND - MRO NO : "+mroNumber+" |  UNABLE TO FETCH JOIN TICKETING | submitOneJoinTicketingFromRecevier()");
						continue;
					}
					
					System.out.println(i + "     One join Ticketing Json before submit    : " + oneJoinTicketing);

					//joinTicketingBuffer.append('[');
					joinTicketingBuffer.append(oneJoinTicketing);
					//joinTicketingBuffer.append(']');

					System.out.println("submitOneJoinTicketingFromRecevier" + joinTicketingBuffer.toString());
					joinTicketingRequestBuilder = new JoinTicketingRequestBuilder(userJson,
							"jointTicket",new JSONObject(oneJoinTicketing));

					System.out.println("submitOneJoinTicketingFromRecevier	:	joinTicketingRequestBuilder:"
							+ joinTicketingRequestBuilder.build().toString());
					response = joinTicketingCommunication.submitString(joinTicketingRequestBuilder.build().toString());

					System.out.println("Join Ticketing response after submit    : " + response);

					if (response.isSuccess())
					{
						
						isSuccessSubmitV.addElement("true");
						editSavedJoinTicketing(true, oneJoinTicketing);
					}
					else
					{
						isSuccessSubmitV.addElement("false");
						editSavedJoinTicketing(false, oneJoinTicketing);
					}

					System.out.println(i + "     One join Ticketing Json after submit    : " + oneJoinTicketing);

					if (CoreConstants.isAutotriggerOn())
					{
						break;
					}
					if (i % 100 == 0)
					{
						try
						{
							Thread.sleep(2000);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
					joinTicketingBuffer.setLength(0);
					//meterReadingBuffer.delete(0, meterReadingBuffer.length());
				}
			}*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return new Response(true, CoreConstants.EMPTY_STRING, null);
	}

	private synchronized void editSavedJoinTicketing(boolean isReadingSubmittedToServer)
	{
		try
		{
			/*//Vector savedMeterReadings = fetchAllSavedMeterReadings();
			Vector savedJoinTicketings = new Vector(); //= fetchAllSavedJoinTicketingsOnlyMroNums();
			
			JSONObject savedJson = null;
			for (int i = 0; i < savedJoinTicketings.size(); i++)
			{
				//savedJson = new JSONObject(savedMeterReadings.elementAt(i).toString());
				
				jsonParser.setJson(savedJoinTicketings.elementAt(i).toString());
				String mroNumber = jsonParser.getValue("mroNo");
				String oneJoinTicketing = "";
				Vector oneJoinTicketingVector = fetchSavedJoinTicketing(mroNumber);
				if(oneJoinTicketingVector != null && oneJoinTicketingVector.size() > 0)
				{
					oneJoinTicketing = fetchSavedJoinTicketing(mroNumber).elementAt(0).toString();	
				}
				else
				{
					System.out.println("NOT FOUND - MRO NO : "+mroNumber+" |  UNABLE TO FETCH JOIN TICKETING | editSavedJoinTicketing(boolean)");
					continue;
				}
				savedJson = new JSONObject(oneJoinTicketing);
				
				getCurrentJoinTicketingBO().init(savedJson);
				if (isReadingSubmittedToServer)
				{
					getCurrentJoinTicketingBO().setJtSubmitted("1");
				}
				else
				{
					getCurrentJoinTicketingBO().setJtSubmitted("0");//saved by default
				}
				joinTicketingPersistance.saveJoinTicketing(CoreConstants.TABLE_SAVED_JOIN_TICKETING,
						getCurrentJoinTicketingBO().toJSON().toString());
			}*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private synchronized void editSavedJoinTicketing(boolean isReadingSubmittedToServer, String joinTicketing)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(joinTicketing);
			JSONObject dataObj = jsonObject.getJSONObject("data");
			
			CoreMobiculeLogger.log("Join Ticketing SUBMITTED TO SERVER : " + isReadingSubmittedToServer);
			
			if (isReadingSubmittedToServer)
			{
				dataObj.put(CoreConstants.KEY_JT_SUBMIT, "1");
			}
			else
			{
				dataObj.put(CoreConstants.KEY_JT_SUBMIT, "0");
			}

			jsonObject.put("data", dataObj);

			joinTicketingPersistance.saveJoinTicketing(CoreConstants.TABLE_SAVED_JOIN_TICKETING, jsonObject.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public synchronized IJoinTicketing getJoinTicketing()
	{
		if (joinTicketing == null)
		{
			joinTicketing = new DefaultJoinTicketing();
		}
		return joinTicketing;
	}

	public synchronized IJoinTicketing resetJoinTicketingBO()
	{
		initJoinTicketingCycle();
		return currentJoinTicketing;
	}

	public synchronized Vector fetchAllSavedJoinTicketings()
	{
		return joinTicketingPersistance.fetchAllSavedJoinTicketings(CoreConstants.TABLE_SAVED_JOIN_TICKETING);
	}

	public synchronized List<String> fetchAllSavedJoinTicketingsOnlyMroNums()

	{
		return joinTicketingPersistance
				.fetchAllSavedJoinTicketingsOnlyMroNums(CoreConstants.TABLE_SAVED_JOIN_TICKETING);
	}

	public synchronized Vector fetchSavedJoinTicketing(String mroNO)
	{
		return joinTicketingPersistance.fetchSavedJoinTicketing(CoreConstants.TABLE_SAVED_JOIN_TICKETING, mroNO);
	}

	public synchronized Response saveJoinTicketing(boolean isReadingSubmittedToServer)
	{
		if (isReadingSubmittedToServer)
		{
			getCurrentJoinTicketingBO().setJtSubmitted("1");
		}
		else
		{
			getCurrentJoinTicketingBO().setJtSubmitted("0");//saved by default
		}

		String saveJoinTicketingJsonValue = getCurrentJoinTicketingBO().toJSON().toString();

		String dataJson = setJoinTicketingDataJson(saveJoinTicketingJsonValue);

		if (StringUtil.isValid(saveJoinTicketingJsonValue))
		{
			boolean saveJoinTicketingStatus = joinTicketingPersistance.saveJoinTicketing(
					CoreConstants.TABLE_SAVED_JOIN_TICKETING, dataJson);
			if (saveJoinTicketingStatus)
			{

				applicationFacade.updateBookWalkStatus(saveJoinTicketingJsonValue);

				/*if (joinTicketingPersistance.searchSaveJoinTicketingStatusByMro(CoreConstants.TABLE_SAVED_JOIN_TICKETING,
						saveJoinTicketingJsonValue))
				{
					 System.out.println("if (joinTicketingPersistance.searchSaveJoinT)");
				  applicationFacade.updateBookWalkStatus(saveJoinTicketingJsonValue);
				}*/
			}
		}

		return new Response(true, "Join Ticketing saved successfully", null);
	}

	private String setJoinTicketingDataJson(String saveJoinTicketingJsonValue)
	{
		JSONObject dataRequestJson = new JSONObject();

		try
		{
			Response response = applicationFacade.getUserDtail();

			dataRequestJson.put("type", "jointTicket");
			dataRequestJson.put("entity", "jointTicket");
			dataRequestJson.put("action", "submit");
			dataRequestJson.put("user", new JSONObject(response.getData().toString()));
			dataRequestJson.put("data", new JSONObject(saveJoinTicketingJsonValue));

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return dataRequestJson.toString();
	}

	/*public synchronized Response saveJoinTicketing(String joinTicketing, boolean isReadingSubmittedToServer)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(joinTicketing);
			if (isReadingSubmittedToServer)
			{
				jsonObject.put(CoreConstants.KEY_JT_SUBMIT, "1");
			}
			else
			{
				jsonObject.put(CoreConstants.KEY_JT_SUBMIT, "0");
			}
			String saveJoinTicketingJsonValue = jsonObject.toString();
			System.out.println("saveJoinTicketing :" + saveJoinTicketingJsonValue);

			if (StringUtil.isValid(saveJoinTicketingJsonValue))
			{
				boolean saveJoinTicketingStatus = joinTicketingPersistance.saveJoinTicketing(
						CoreConstants.TABLE_SAVED_JOIN_TICKETING, saveJoinTicketingJsonValue);
				if (saveJoinTicketingStatus)
				{
					applicationFacade.updateBookWalkStatus(saveJoinTicketingJsonValue);
				}
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	
		return new Response(true, "Join Ticketing saved successfully", null);
	}*/

	@Override
	public boolean isCommercialCustomer(IJoinTicketing joinTicketingBO)
	{

		if (null == joinTicketingBO)
		{
			return false;
		}
		String mruCodePrefix = joinTicketingBO.getMruCode();

		if (null == mruCodePrefix || mruCodePrefix.trim().equals(CoreConstants.EMPTY_STRING))
		{
			return false;
		}

		mruCodePrefix = mruCodePrefix.substring(0, 3);
		if (mruCodePrefix.equalsIgnoreCase(CoreConstants.COM_MRU_CODE))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public List<String> getJoinTicketingDataByMro(String mroNo)
	{
		return joinTicketingPersistance.getJoinTicketingDataByMro(mroNo);
	}

	@Override
	public synchronized void saveJoinTicketingImages(String mro)
	{
		List<String> allImageList = joinTicketingPersistance.fetchAllJoinTicketingImages(mro);

		for (int i = 0; i < allImageList.size(); i++)
		{
			boolean saveJoinTicketingStatus = joinTicketingPersistance.saveJoinTicketing(
					CoreConstants.TABLE_SAVED_JOIN_TICKETING, allImageList.get(i));

			if (saveJoinTicketingStatus)
			{
				deleteFromJoinTicketingImage(allImageList.get(i));
			}
		}
	}

	@Override
	public void addJoinTicketingImage(String imgJsonStr)
	{
		joinTicketingPersistance.addJoinTicketingImage(imgJsonStr);
	}

	@Override
	public void deleteFromJoinTicketingImage(String imgJsonStr)
	{
		joinTicketingPersistance.deleteFromJoinTicketingImage(imgJsonStr);
	}
}
