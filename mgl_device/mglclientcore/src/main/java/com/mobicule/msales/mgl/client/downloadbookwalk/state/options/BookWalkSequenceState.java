package com.mobicule.msales.mgl.client.downloadbookwalk.state.options;

import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncCommunicationService;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncePersistenceService;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqnceRequestBuilder;
import com.mobicule.msales.mgl.client.downloadbookwalk.state.EntitySyncState;
import com.mobicule.msales.mgl.client.downloadbookwalk.state.SyncEnum;
import com.mobicule.versioncontrol.VersionControl;

public class BookWalkSequenceState extends EntitySyncState
{
	public BookWalkSequenceState(IBookWalkSqncePersistenceService persistenceService,
			IBookWalkSqncCommunicationService communicationService, IBookWalkSqnceRequestBuilder requestBuilder,VersionControl versionControl)
	{
		super(SyncEnum.getDisplayName(SyncEnum.BOOKWALK_SEQUENCE), persistenceService, communicationService,
				requestBuilder,versionControl);
	}
}
