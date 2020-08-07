package com.mobicule.msales.mgl.client.downloadbookwalk.implementation;

import java.util.Vector;

import com.mobicule.component.util.CoreConstants;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncCommunicationService;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncePersistenceService;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqnceRequestBuilder;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.ISyncFacade;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.ISyncObserver;
import com.mobicule.msales.mgl.client.downloadbookwalk.state.options.BookWalkSequenceState;
import com.mobicule.versioncontrol.VersionControl;

public class DefaultBookWalkSqnceFacade implements ISyncFacade
{
	private IBookWalkSqncePersistenceService persistenceService;

	private IBookWalkSqncCommunicationService communicationService;

	public IBookWalkSqnceRequestBuilder requestBuilder;

	protected ISyncObserver syncObserver;

	private Vector observers;

	private IApplicationFacade applicationFacade;
	
	private VersionControl versionControl;

	public DefaultBookWalkSqnceFacade(Vector observers, IBookWalkSqncePersistenceService persistenceService,
			IBookWalkSqncCommunicationService communicationService, String userJson,
			IApplicationFacade applicationFacade,VersionControl versionControl)
	{
		//this.syncObserver = syncObserver;
		this.observers = observers;
		this.applicationFacade = applicationFacade;
		this.persistenceService = persistenceService;
		this.communicationService = communicationService;
		this.versionControl = versionControl;
		String pageSize = CoreConstants.PAGE_SIZE;

		requestBuilder = DefaultSyncRequestBuilder.getInstance(userJson, new Integer(Integer.parseInt(pageSize)));
		//set userJson again since the userjson is respected to change.
		requestBuilder.setUserJson(userJson);
	}

	public void syncBookWalkSequence()
	{
		BookWalkSequenceState bookwalkState = new BookWalkSequenceState(persistenceService, communicationService,
				requestBuilder,versionControl);

		for (int i = 0; i < observers.size(); i++)
		{
			bookwalkState.addObserver((ISyncObserver) observers.elementAt(i));
		}

		bookwalkState.sync();
	}
}
