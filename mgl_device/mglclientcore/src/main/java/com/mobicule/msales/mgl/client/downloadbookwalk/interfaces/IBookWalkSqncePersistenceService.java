package com.mobicule.msales.mgl.client.downloadbookwalk.interfaces;

import java.util.Vector;

public interface IBookWalkSqncePersistenceService
{

	public static String BOOKWALK_TABLE = "BOOKWALK";

	public String getLastSyncDate(String string);

	public void add(String entity, String syncResponse);

	public void update(String entity, String syncResponse);

	public void delete(String entity, String syncResponse);

	public Vector load(String entity);

	public Vector load(String entity, String key, String value);

	public void dropTable(String entity);

	public void renameTableName(String oldName, String newName);

	public void updateUserData(String userDetailJesonobject, String data, IBookWalkSqnceRequestBuilder requestBuilder);

	public boolean checkForNewBookWalk(String data);
	
	public void syncInit(String entity);

}
