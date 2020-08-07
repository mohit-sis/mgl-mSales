package com.mobicule.msales.mgl.client.downloadbookwalk.state;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mobicule.component.json.parser.IJSONParser;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.component.util.CoreMessages;
import com.mobicule.component.util.CoreMobiculeLogger;
import com.mobicule.component.util.LogFile;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncCommunicationService;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncePersistenceService;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqnceRequestBuilder;
import com.mobicule.versioncontrol.VersionControl;

import sun.rmi.runtime.Log;

public class EntitySyncState extends GenericSyncState
{
	private static final int ADD = 0;

	private static final int MODIFY = 1;

	private static final int DELETE = 2;

	private static final int INIT = 3;

	private int totalPageCount;

	protected int currentPageNumber;

	private String lastSyncDate;

	private int[] pageCountArray;

	private int progressValue;

	protected IJSONParser jsonParser;

	private int currentEntityState;

	private VersionControl versionControl;

	private String zero_string = "0";

	public EntitySyncState(String entity, IBookWalkSqncePersistenceService persistenceService,
			IBookWalkSqncCommunicationService communicationService, IBookWalkSqnceRequestBuilder requestBuilder,
			VersionControl versionControl)
	{
		super(entity, persistenceService, communicationService, requestBuilder);
		jsonParser = JSONParser.getInstance();
		this.versionControl = versionControl;
	}

	public void doSync()
	{

		initSync();
		syncEntityState(DELETE);
		syncEntityState(MODIFY);
		syncEntityState(ADD);
		completeSync();
	}

	public void clearValues()
	{
		totalPageCount = 0;
		currentPageNumber = 0;
		lastSyncDate = zero_string;
		pageCountArray = null;
		progressValue = 0;
		currentEntityState = ADD;
	}

	private void initSync()
	{
		String syncInitResponse = null;
		
		try
		{
			if (isInitRequired())
			{
				currentEntityState = INIT;
				//			lastSyncDate = persistenceService.getLastSyncDate(entity.toUpperCase().replace(' ', '_'));
				//			String syncInitRequest = requestBuilder.createSyncInitRequest(replaceAll(entity, " ", "").toLowerCase(),
				//					lastSyncDate);
				lastSyncDate = persistenceService.getLastSyncDate(getPersistanceEntityName(entity));
				String syncInitRequest = requestBuilder.createSyncInitRequest(getRequestEntityName(entity),
						lastSyncDate);
				
				CoreMobiculeLogger.log("INSIDE initSync()  syncInitRequest - " + syncInitRequest);
				
				JSONObject jsonObject = new JSONObject(syncInitRequest);

				//versionControl = VersionControl.getInstance();
				JSONObject jsonInit = versionControl.attachVersionControlInfo(jsonObject);
				syncInitResponse = communicationService.sendSyncRequest(jsonInit.toString());
				
				CoreMobiculeLogger.log("INSIDE initSync()   syncInitResponse   ----------" + syncInitResponse);
				//jsonParser.setJson(syncInitResponse);
				
				JSONObject jsonResp = new JSONObject(syncInitResponse);

				String version = jsonResp.getString(CoreConstants.USER_RESPONSE_STATUS);

				boolean isSuccess = CoreConstants.STATUS_RESPONSE_SUCCESS.equalsIgnoreCase(jsonResp
						.getString(CoreConstants.USER_RESPONSE_STATUS));

				if (isSuccess == false && version.equalsIgnoreCase("CHANGE"))
				{
					CoreConstants.SYNC_URL = jsonResp.getJSONObject(CoreConstants.KEY_DATA).getString(
							VersionControl.KEY_BUILD_URL);
					throw new RuntimeException(CoreConstants.VERSION_CHANGE);
				}
				else if (!isSuccess)
				{

					CoreMobiculeLogger.log("INSIDE initSync()   KEY_DATA   ----------"+jsonResp.getString(CoreConstants.KEY_DATA));
					CoreMessages.SYNC_FAILED = jsonResp.getString(CoreConstants.USER_RESPONSE_MESSAGE);
					throw new RuntimeException(CoreMessages.SYNC_FAILED);
				}
				else
				{
					
					//CoreMobiculeLogger.log("INSIDE initSync()   KEY_DATA   ----------"+jsonResp.getString(CoreConstants.KEY_DATA));
					persistenceService.syncInit(entity);
					saveSyncData(jsonResp.toString());
				}
			}

			pageCountArray = getPageCountArray(syncInitResponse);

			if(pageCountArray.length==0){
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	protected boolean isInitRequired()
	{
		return true;
	}

	private void syncEntityState(int currentEntityState)
	{
		totalPageCount = pageCountArray[currentEntityState];//Error

		this.currentEntityState = currentEntityState;
		for (currentPageNumber = 0; currentPageNumber < totalPageCount; currentPageNumber++)
		{
			syncCurrentPage();
			updateProgressValue();
			notifyObserver(entity, progressValue);
		}
	}

	/*	protected void syncCurrentPage()
		{
	//		String syncPageRequest = requestBuilder.createSyncPageRequest(replaceAll(entity, " ", "").toLowerCase(),
	//				getName(currentEntityState).toLowerCase(), currentPageNumber, lastSyncDate);
			String syncPageRequest = requestBuilder.createSyncPageRequest(getRequestEntityName(entity),
					getName(currentEntityState).toLowerCase(), currentPageNumber, lastSyncDate);

			String syncPageResponse = communicationService.sendSyncRequest(syncPageRequest);

			jsonParser.setJson(syncPageResponse);
			String status = jsonParser.getValue(CoreConstants.USER_RESPONSE_STATUS);
			if (status.equalsIgnoreCase(CoreConstants.STATUS_RESPONSE_SUCCESS))
			{
				//JSONObject jsonObj = new JSONObject(syncPageResponse);
				//JSONArray dataJSonArray = jsonObj.getJSONArray(DATA_KEY);
				if (!(jsonParser.getValue(CoreConstants.KEY_DATA)).equals("[]"))
				{
					Vector dataV = jsonParser.getArray(CoreConstants.KEY_DATA);

					for (int i = 0; i < dataV.size(); i++) //jsonParser.getArray(DATA_KEY).size()
					{
						saveSyncData(dataV.elementAt(i).toString()); //dataJSonArray.getJSONObject(i).toString()
					}
				}
			}
			else
			{
				CoreMessages.SYNC_FAILED = jsonParser.getValue(CoreConstants.USER_RESPONSE_MESSAGE);
				throw new RuntimeException(CoreMessages.SYNC_FAILED);
			}
		}
	*/
	protected void syncCurrentPage()
	{
		String syncPageRequest = requestBuilder.createSyncPageRequest(getRequestEntityName(entity),
				getName(currentEntityState).toLowerCase(), currentPageNumber, lastSyncDate);
		try
		{

			JSONObject jsonObject = new JSONObject(syncPageRequest);

			//versionControl = VersionControl.getInstance();
			JSONObject jsonInit = versionControl.attachVersionControlInfo(jsonObject);

			String syncPageResponse = communicationService.sendSyncRequest(jsonInit.toString());

			JSONObject syncPageJsonObject = new JSONObject(syncPageResponse);
			String status = null;

			if (syncPageJsonObject.has(CoreConstants.USER_RESPONSE_STATUS))
			{
				status = syncPageJsonObject.getString(CoreConstants.USER_RESPONSE_STATUS);
				if (status.equalsIgnoreCase(CoreConstants.STATUS_RESPONSE_SUCCESS))
				{
					if ((syncPageJsonObject.has(CoreConstants.KEY_DATA))
							&& (syncPageJsonObject.get(CoreConstants.KEY_DATA)) instanceof JSONArray)
					{
						JSONArray dataV = syncPageJsonObject.getJSONArray(CoreConstants.KEY_DATA);

						for (int i = 0; i < dataV.length(); i++) //jsonParser.getArray(DATA_KEY).size()
						{
							saveSyncData(dataV.getJSONObject(i).toString()); //dataJSonArray.getJSONObject(i).toString()
						}
					}
				}
			}
			else if (syncPageJsonObject.has(CoreConstants.USER_RESPONSE_STATUS)
					&& syncPageJsonObject.getJSONObject(CoreConstants.KEY_DATA) != null)
			{
				String respStatus = syncPageJsonObject.getString(CoreConstants.USER_RESPONSE_STATUS);
				if (respStatus.equalsIgnoreCase("CHANGE"))
				{
					CoreConstants.SYNC_URL = syncPageJsonObject.getJSONObject(CoreConstants.KEY_DATA).getString("url");
					throw new RuntimeException("CHANGE");
				}

			}
			else
			{
				CoreMessages.SYNC_FAILED = syncPageJsonObject.getString(CoreConstants.USER_RESPONSE_MESSAGE);
				throw new RuntimeException(CoreMessages.SYNC_FAILED);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void saveSyncData(String data)
	{
		switch (currentEntityState)
		{
			case ADD:
				persistenceService.add(getPersistanceEntityName(entity), data);//persistenceService.add(entity.toUpperCase().replace(' ', '_'), data); //jsonParser.getArray(DATA_KEY).elementAt(i).toString()
				break;

			case MODIFY:
				persistenceService.update(getPersistanceEntityName(entity), data); //persistenceService.update(entity.toUpperCase().replace(' ', '_'), data);
				break;

			case DELETE:
				deleteData(data);
				break;
			case INIT:
				persistenceService.checkForNewBookWalk(data);
			default:
				break;
		}
	}

	protected void deleteData(String data)
	{
		persistenceService.delete(getPersistanceEntityName(entity), data);//persistenceService.delete(entity.toUpperCase().replace(' ', '_'), data);
	}

	protected void completeSync()
	{
		String syncCompleteRequest = requestBuilder.createSyncCompletionRequest(getRequestEntityName(entity),
				lastSyncDate);

		JSONObject jsonObject;
		try
		{
			jsonObject = new JSONObject(syncCompleteRequest);

			//versionControl = VersionControl.getInstance();
			JSONObject jsonInit = versionControl.attachVersionControlInfo(jsonObject);

			String syncCompletionResponse = communicationService.sendSyncRequest(jsonInit.toString());

			CoreMobiculeLogger.log("//completeSync : syncCompletionResponse :: " + syncCompletionResponse);

			LogFile.writeToFile("**** EntitySyncState : In completeSync() : syncCompletionResponse  "
					+ syncCompletionResponse);

			jsonParser.setJson(syncCompletionResponse);
			JSONObject syncPageJsonObject = new JSONObject(syncCompletionResponse);
			String statusStr = jsonParser.getValue(CoreConstants.USER_RESPONSE_STATUS);

			if (statusStr.equalsIgnoreCase(CoreConstants.STATUS_RESPONSE_SUCCESS))
			{
				progressValue = 100;
				notifyObserver(entity, progressValue);
				persistenceService.updateUserData(CoreConstants.USER_DETAIL_JESONOBJECT,
						jsonParser.getValue(CoreConstants.KEY_DATA), requestBuilder);
			}
			else if (statusStr.equalsIgnoreCase(CoreConstants.VERSION_CHANGE))
			{
				CoreConstants.SYNC_URL = syncPageJsonObject.getJSONObject(CoreConstants.KEY_DATA).getString("url");
				throw new RuntimeException("CHANGE");

			}
			else
			{
				CoreMessages.SYNC_FAILED = jsonParser.getValue(CoreConstants.USER_RESPONSE_MESSAGE);
				throw new RuntimeException(CoreMessages.SYNC_FAILED);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			LogFile.exceptionToFile();
		}
	}

	protected int[] getPageCountArray(String syncResponse)
	{
		int addPages, modifyPages, deletePages;

		if (syncResponse != null && !syncResponse.equals(CoreConstants.EMPTY_STRING) && syncResponse.startsWith("{")
				&& syncResponse.endsWith("}"))
		{
			jsonParser.setJson(syncResponse);
			String statusStr = jsonParser.getValue(CoreConstants.USER_RESPONSE_STATUS);
			if (statusStr.equalsIgnoreCase(CoreConstants.STATUS_RESPONSE_SUCCESS))
			{
				addPages = Integer.parseInt(jsonParser.getValue("add"));
				if (addPages == 0)
				{
					currentEntityState = ADD;
					saveSyncData("");
				}
				//addPages = i;
				modifyPages = Integer.parseInt(jsonParser.getValue("modify"));
				deletePages = Integer.parseInt(jsonParser.getValue("delete"));
				return new int[] { addPages, modifyPages, deletePages };
			}
		}
		return new int[] { 0, 0, 0 };
	}

	private void updateProgressValue()
	{
		if (totalPageCount > 0)
		{
			progressValue = progressValue + (int) ((float) (1) / (totalPageCount * pageCountArray.length) * 100);
		}
	}

	private String getName(int entityState)
	{
		switch (entityState)
		{
			case ADD:
				return "add";
			case MODIFY:
				return "modify";
			case DELETE:
				return "delete";
			default:
				return "";
		}
	}

	public static String replaceAll(String text, String searchString, String replacementString)
	{
		StringBuffer sBuffer = new StringBuffer();
		int pos = 0;
		while ((pos = text.indexOf(searchString)) != -1)
		{
			sBuffer.append(text.substring(0, pos) + replacementString);
			text = text.substring(pos + searchString.length());
		}
		sBuffer.append(text);
		return sBuffer.toString();
	}

	protected String getRequestEntityName(String entity)
	{

		String _entity = entity;
		_entity = replaceAll(_entity, " ", "");
		int i = _entity.indexOf('_');
		if (i == -1)
		{
			return _entity.toLowerCase();
		}
		else
		{
			String entityType = (_entity.substring(0, (i))).toLowerCase();
			String entityCode = _entity.substring((i + 1), _entity.length());
			return entityType.concat("_" + entityCode);

		}

	}

	protected String getPersistanceEntityName(String entity)
	{
		String _entity = entity;
		int i = _entity.indexOf(' ');
		if (i != -1)
		{
			return _entity.toUpperCase().replace(' ', '_');
		}
		else
		{
			i = _entity.indexOf('_');
			if (i != -1)
			{
				String entityType = (_entity.substring(0, (i))).toUpperCase();
				String entityCode = _entity.substring((i + 1), _entity.length());
				return entityType.concat("_" + entityCode);
			}
			else
			{
				return _entity.toUpperCase();
			}

		}

	}
}
