/**
 * 
 */
package com.mobicule.msales.mgl.client.login.implementation;

import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginBusinessService;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginCommunicationService;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginFacade;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginPersistenceService;
import com.mobicule.versioncontrol.VersionControl;

/**
 * @author nikita
 * 
 */
public class DefaultLoginFacade implements ILoginFacade
{
	public DefaultLoginFacade(ILoginCommunicationService communicationService,
			ILoginPersistenceService persistenceService,VersionControl versionControl)
	{
		businessService = DefaultLoginBusinessService.getBusinessService(communicationService, persistenceService,versionControl);
	}

	private ILoginBusinessService businessService;

	public Response isOffineLoginMode()
	{
		return businessService.loginInOfflineMode();
	}

	public void setOnlineLoginFlag(boolean isOnlineLogin)
	{
		businessService.setOnlineLoginFlag(isOnlineLogin);

	}

	public Response signIn(String username, String password, String imeinumber)
	{

		return businessService.signIn(username, password, imeinumber);
	}
}
