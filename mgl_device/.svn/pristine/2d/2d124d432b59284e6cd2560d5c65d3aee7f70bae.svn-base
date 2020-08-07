package com.mobicule.msales.mgl.client.downloadbookwalk.state;

import java.util.Vector;

import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncCommunicationService;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncePersistenceService;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqnceRequestBuilder;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.ISyncObserver;

public abstract class GenericSyncState implements ISyncState
{
	protected String entity;

	protected ISyncObserver observer;

	private Vector observers;

	protected IBookWalkSqncePersistenceService persistenceService;

	protected IBookWalkSqncCommunicationService communicationService;

	protected IBookWalkSqnceRequestBuilder requestBuilder;

	public GenericSyncState(String entity, IBookWalkSqncePersistenceService persistenceService,
			IBookWalkSqncCommunicationService communicationService, IBookWalkSqnceRequestBuilder requestBuilder)
	{
		this.entity = entity;
		this.observers = new Vector();

		this.persistenceService = persistenceService;
		this.communicationService = communicationService;
		this.requestBuilder = requestBuilder;
	}

	public void notifyObserver(String entity, int progressValue)
	{
		if (observers == null)
		{
			return;
		}
		for (int i = 0; i < observers.size(); i++)
		{
			((ISyncObserver) observers.elementAt(i)).update(entity, progressValue);
		}
	}

	public void addObserver(ISyncObserver syncObserver)
	{
		this.observer = syncObserver;

		observers.addElement(syncObserver);
	}

	public void sync()
	{
		clearValues();
		doSync();
	}

	public abstract void clearValues();

	public abstract void doSync();
}