package com.mobicule.msales.mgl.client.downloadbookwalk.state;

import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncCommunicationService;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncePersistenceService;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqnceRequestBuilder;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.ISyncObserver;

public class ModuleSyncState extends GenericSyncState
{
	private ISyncState[] syncSubStates;

	private int currentEntity;

	private ISyncObserver entitySyncObserver;

	private int progressValue;

	public ModuleSyncState(String entity, ISyncState[] syncSubStates,
			IBookWalkSqncePersistenceService persistenceService,
			IBookWalkSqncCommunicationService communicationService, IBookWalkSqnceRequestBuilder requestBuilder)
	{
		super(entity, persistenceService, communicationService, requestBuilder);

		this.syncSubStates = syncSubStates;

		entitySyncObserver = new ISyncObserver()
		{
			public void update(String entity, int progress)
			{
				updateProgressValue(progress);
				notifyObserver(entity, progressValue);
			}
		};
	}

	public void setSyncSubStates(ISyncState[] syncSubStates)
	{
		this.syncSubStates = syncSubStates;
	}

	public void clearValues()
	{
		currentEntity = 0;
		progressValue = 0;
	}

	public void doSync()
	{
		if (syncSubStates != null)
		{
			for (currentEntity = 0; currentEntity < syncSubStates.length; currentEntity++)
			{
				ISyncState currentSyncSubState = syncSubStates[currentEntity];

				currentSyncSubState.addObserver(entitySyncObserver);

				currentSyncSubState.sync();
			}
		}
	}

	private void updateProgressValue(int currentSubProgressValue)
	{
		if (currentSubProgressValue == 100)
		{
			progressValue = (int) ((float) (currentEntity + 1) / syncSubStates.length * 100);

		}
		else
		{
			progressValue = (int) ((float) (currentEntity) / syncSubStates.length * 100)
					+ (int) (((float) (currentSubProgressValue) / syncSubStates.length));

		}
	}
}
