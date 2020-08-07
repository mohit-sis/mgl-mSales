
package com.mobicule.msales.mgl.client.login.implementation;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mobicule.component.string.StringUtil;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.component.util.CoreMessages;
import com.mobicule.msales.mgl.client.common.LoginRequestBuilder;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginBusinessService;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginCommunicationService;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginPersistenceService;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginValidationService;
import com.mobicule.versioncontrol.VersionControl;

/**
 * @author nikita
 *
 */
public class DefaultLoginBusinessService implements ILoginBusinessService
{
	private static DefaultLoginBusinessService instance = null;

	private ILoginValidationService validationService;

	private ILoginPersistenceService persistenceService;

	private ILoginCommunicationService loginCommunicationService;

	private boolean isOnlineLogin = false;

	JSONObject responseJSON=null;
	
	JSONObject jsonRequest=null;
	
	JSONObject finalJSON=null;
	
	private VersionControl versionControl;
	
	//private Context context;

	private DefaultLoginBusinessService(ILoginCommunicationService communicationService,
			ILoginPersistenceService persistenceService,VersionControl versionControl)
	{
		validationService = DefaultLoginValidationService.getInstance();
		loginCommunicationService = communicationService;
		this.persistenceService = persistenceService;
		this.versionControl = versionControl;
	}

	public static ILoginBusinessService getBusinessService(ILoginCommunicationService communicationService,
			ILoginPersistenceService persistenceService,VersionControl versionControl)
	{
		if (instance == null)
		{
			instance = new DefaultLoginBusinessService(communicationService, persistenceService,versionControl);
		}
		return instance;
	}

	public Response loginInOfflineMode()
	{
		Response response = persistenceService.getOfflineLoginDetail();
		if (response.isSuccess())
		{
			JSONObject offlineJsonObj = (JSONObject) response.getData();
			return new Response(true, CoreMessages.LOGIN_OFFLINE, offlineJsonObj);
		}
		return new Response(false, null, null);
	}

	public Response setOffineLoginMode(JSONObject userDetail)
	{
		Response response = persistenceService.setOffineLoginMode(userDetail);
		return response;
	}

	public Response isOffineLoginMode()
	{
		boolean isOffineLoginMode = persistenceService.isOffineLoginMode();
		return new Response(isOffineLoginMode, null, null);
	}

	public boolean isOnlineLogin()
	{
		return isOnlineLogin;
	}

	public void setOnlineLoginFlag(boolean isOnlineLogin)
	{
		this.isOnlineLogin = isOnlineLogin;
	}

	public Response setOnlineLoginMode(JSONObject jsonObject)
	{
		Response networkResponse = null;
		try
		{

			networkResponse = loginCommunicationService.submit(jsonObject);

			if (networkResponse.isSuccess())
			{
				if (networkResponse.getData() != null)
				{
				    responseJSON = (JSONObject) networkResponse.getData();

					if (responseJSON.getString(CoreConstants.USER_RESPONSE_STATUS).equalsIgnoreCase(
							CoreConstants.STATUS_RESPONSE_SUCCESS))
					{
						isOnlineLogin = false;
						Object responseData = responseJSON.get(CoreConstants.USER_RESPONSE_DATA);
						if (responseData != null && responseData instanceof JSONObject)
						{
							JSONObject serverResponseJson = (JSONObject) responseData;
							JSONObject dataJson = jsonObject.getJSONObject(CoreConstants.KEY_DATA);
							dataJson.put(CoreConstants.FIELD_BWK_NAME, "");
							dataJson.put(CoreConstants.FIELD_VERSION_NO, "");

							storeUserDetails(dataJson);
							jsonObject.put(ILoginPersistenceService.DELAY, serverResponseJson);
							return new Response(true, StringUtil.getMessage(networkResponse.getMessage()),  jsonObject);
						}
					}
				}
			} 
			else if(networkResponse.isSuccess()==false && networkResponse.getData() != null){ 
				
				responseJSON = (JSONObject) networkResponse.getData();
		  		
				if(responseJSON.getString(CoreConstants.USER_RESPONSE_STATUS).equalsIgnoreCase(CoreConstants.VERSION_CHANGE))
	 		   {
		
				return new Response(false, StringUtil.getMessage(networkResponse.getMessage()),
						networkResponse.getData());
		    	}
			}
			else{
				
				return new Response(false, StringUtil.getMessage(networkResponse.getMessage()),
						networkResponse.getData());
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return new Response(false, StringUtil.getMessage(networkResponse.getMessage()), networkResponse.getData());

	}

	public Response signIn(String username, String password, String imeinumber)
	{
		Response loginDetailsValidationResponse;
		loginDetailsValidationResponse = validateInput(username, password, imeinumber);
		if (loginDetailsValidationResponse.isSuccess())
		{
			// jsonRequest=new JSONObject();
			 jsonRequest=new LoginRequestBuilder(username, password, imeinumber).build();
		     //versionControl=VersionControl.getInstance();
			// finalJSON=new JSONObject();
			 finalJSON=versionControl.attachVersionControlInfo(jsonRequest);
			return submit(finalJSON);
		}
		return loginDetailsValidationResponse;
	}

	public Response storeUserDetails(JSONObject jsonObject)
	{
		return persistenceService.storeUserDetails(jsonObject);
	}

	public Response submit(JSONObject givenJsonObject)
	{
		try
		{
			Response isOfflineLoginAvailable = isOffineLoginMode();
			if (isOfflineLoginAvailable.isSuccess())
			{
				isOfflineLoginAvailable = setOffineLoginMode(givenJsonObject);
				if (isOfflineLoginAvailable.isSuccess())
				{
					//implement following to update the userName entered different from what is stored in db
					Response response = persistenceService.getOfflineLoginDetail();
					JSONObject offlineData = (JSONObject) response.getData();
					JSONObject dataJson = givenJsonObject.getJSONObject(CoreConstants.KEY_DATA);
					String userNameEntered = dataJson.getString(ILoginPersistenceService.USER_DETAIL_USERNAME);
					
					if (offlineData != null)
					{
						String userNameStored = offlineData.getString(ILoginPersistenceService.USER_DETAIL_USERNAME);
						
						if (!userNameEntered.equals(userNameStored))
						{
							offlineData.put(ILoginPersistenceService.USER_DETAIL_USERNAME, userNameEntered);
							storeUserDetails(offlineData);
							return new Response(response.isSuccess(), response.getMessage(), offlineData);
						}
					}
					return response;
				}
				else
				{
					return setOnlineLoginMode(givenJsonObject);
				}
			}
			else
			{
				return setOnlineLoginMode(givenJsonObject);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new Response(false, NETWORK_FAILED, null);
	}

	public Response validateInput(String username, String password, String imeinumber)
	{
		return validationService.validateInput(username, password, imeinumber);
	}
}
