package com.mobicule.android.component.db;

import android.content.ContentValues;
import android.database.Cursor;

public interface IDatabaseManager
{
	public int add(String entity, String data);

	public boolean update(String entity, String data, int rowId);
	
	public boolean update(String entity, ContentValues data, int rowId);

	public boolean delete(String entity, int rowId);

	public Cursor search(String entity, Clause clause);
	
	public Cursor executeRawQuery(String entity,String query);

	public String[] getColumnNames(String entity);

	public int getRowCount(String entity);

	public void dropTable(String entity);

	public String getId(String entity, String data);

	public String getData(String entity, int rowId);

	public Cursor getTableNames();

	public void createTable(String entity);
	
	public void renameTable(String oldName,String newName);
	
	public int add(String entity, String[] columnarray,String[] valuearray, String data);
	
	public int add(String entity, ContentValues data);

	boolean update(String entity, String[] columnarray, String[] valuearray,
			String data, int rowId);
	
	public boolean updateLeadStatus(String entity, String leadStatus, int rowId);
	
	public boolean updateFIDataSubmissionStatus(String entity, String dataSubmissionStatus, int rowId) ;
	
	public boolean updateFIImageSubmissionStatus(String entity, String imageSubmissionStatus, int rowId) ;
	
	public boolean updateFIFormStatus(String entity, String formStatus, int rowId) ;
	
	public boolean updateFIData(String entity, String formStatus, int rowId)  ;
	
	public boolean updateFISubmissionDate(String entity, String submissionDate, int rowId);
	
	public boolean updateSyncTable(int rowId, String lastSyncDate);

}