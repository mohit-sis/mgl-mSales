package com.mobicule.android.component.db;

import java.util.List;

import org.json.me.JSONArray;

import android.database.Cursor;
import android.database.SQLException;

public interface IDatabaseAdapter
{
	public abstract IDatabaseAdapter open(String entity) throws SQLException;

	public void drop(String entity);

	public abstract void close();

	public abstract long insert(String data);
	
	public long insert(String entity, String data);

	public abstract boolean update(long rowId, String data);

	public abstract boolean delete(long rowId);
	
	public boolean delete(String entity ,long rowId);

	public abstract Cursor search(Clause clause);
	
	public Cursor search(String entity, Clause clause);

	public abstract String[] fetchColumnNames();

	public abstract int getSize(String table);

	public abstract Cursor fetchRow(long rowId, SearchCondition searchCondition);

	public abstract Cursor fetchRow(String data, SearchCondition searchCondition);

	public abstract Cursor getAllTableNames();
	
	public abstract void alterTableName(String oldName , String newName);

	public Cursor getDistinctTabs(String productName);
	
	public Cursor getDistinctFormTypeTabs(String productName, String formType);
	
	public Cursor getTabsDetails(String inRangeString);
	
	public Cursor getFieldsForTabs(String productName, String tabCode);
	
	public Cursor getFieldsForTBS013Tabs(String productName, String tabCode, String customerSegment);
	
	public Cursor getAllFieldsForAllTabs(String productName, String inRangeString);
	
	public Cursor getLeadIdList();
	
	public Cursor getLocalLeadIdList();
	
	public long insert(String entity, String[] columnarray,String[] valuearray, String data);
	
	public boolean update(String entity, String[] columnarray ,String[] valuearray , String data, long rowId);
	
	public boolean updateLeadStatus(int rowId, String leadStatus);
	
	public boolean updateLeadStatus(String entity ,int rowId, String leadStatus);
	
	public boolean updateFIDataSubmissionStatus(int rowId, String status) ;
	
	public boolean updateFIDataSubmissionStatus(String entity ,int rowId, String status);
	
	public boolean updateFIImageSubmissionStatus(int rowId, String status) ;
	
	public boolean updateFIImageSubmissionStatus(String entity, int rowId, String status) ;
	
	public boolean updateFIFormStatus(int rowId, String status) ;
	
	public boolean updateFIFormStatus(String entity, int rowId, String status) ;
	
	public boolean updateFIData(int rowId, String fiData) ;
	
	public boolean updateFIData(String entity, int rowId, String fiData) ;
	
	public Cursor executeRawQuery(String entity,String query);
	
	public boolean updateFiSubmissionDate(int rowId, String submissionDate) ;
	
	public boolean updateFiSubmissionDate(String entity, int rowId, String submissionDate) ;
	
	public void insertInBulk(String tableName, JSONArray data);
	
	public boolean updateSyncTable(int rowId, String lastSyncDate);
	
	public boolean updateSyncTable(String entity, int rowId, String lastSyncDate);

}