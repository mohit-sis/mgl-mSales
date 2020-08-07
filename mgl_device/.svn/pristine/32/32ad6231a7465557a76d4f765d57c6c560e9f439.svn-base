/**
******************************************************************************
* C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
* <p>
* Copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved. 
* This is proprietary information of Mobicule Technologies Pvt. Ltd.and is 
* subject to applicable licensing agreements. Unauthorized reproduction, 
* transmission or distribution of this file and its contents is a 
* violation of applicable laws.
******************************************************************************
*
* @project mSalesMgl
*/
package com.mobicule.android.msales.mgl.jointicketing.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import android.content.Context;
import android.database.Cursor;
import com.mobicule.android.component.db.Clause;
import com.mobicule.android.component.db.Operator;
import com.mobicule.android.component.db.QueryItem;
import com.mobicule.android.component.db.SQLiteDatabaseManager;
import com.mobicule.android.component.db.SearchCondition;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.component.string.StringUtil;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncePersistenceService;
import com.mobicule.msales.mgl.client.jointicketing.IJoinTicketingPersistance;

/**
* 
* <enter description here>
*
* @author Ashish Shukla
* @see 
*
* @createdOn 15-Apr-2014
* @modifiedOn
*
* @copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class DefaultJoinTicketingPersistenceService implements IJoinTicketingPersistance
{
    
	private Context context;

	private static DefaultJoinTicketingPersistenceService instance;

	private SQLiteDatabaseManager databaseManager;

	private static final String DATA = "data";
	
	private static final String KEY_MRO_NUMBER = "mroNo";

	private JSONParser jsonParser;

	private String readingElement;

	private String mroNo = "";

	private String date = "";

	private String mrCode = "";

	private String searchQuery;

	public DefaultJoinTicketingPersistenceService(Context context)
	{
		this.context = context;
		jsonParser = JSONParser.getInstance();
		databaseManager = (SQLiteDatabaseManager) SQLiteDatabaseManager.getInstance(context,Constants.DATABASE_NAME);
	}

	public static IJoinTicketingPersistance getPersistenceService(Context context)
	{
		if (instance == null)
		{
			instance = new DefaultJoinTicketingPersistenceService(context);
		}
		return instance;
	}

	
	@Override
	public boolean saveJoinTicketing(String entity, String data)
	{
		try
		{
			//jsonParser.setJson(data);

			//String bpNo = jsonParser.getValue(Constants.KEY_BP_NUMBER);
			
			JSONObject requestObj = new JSONObject(data);
			JSONObject dataObj = requestObj.getJSONObject("data");
			mroNo = dataObj.getString(CoreConstants.FIELD_MRO_NO);
			
			String entityObj = requestObj.getString("entity");
			
			
			//mroNo = jsonParser.getValue(CoreConstants.FIELD_MRO_NO);
			
			MobiculeLogger.verbose("saveJoinTicketing - entityObj: "+ entityObj + "mroNo: "+mroNo);
		
			Cursor cursor = null;

			if (StringUtil.isValid(mroNo) && (!(mroNo.equals("0"))))
			{
						try
						{
							cursor = databaseManager.search(entity, 
									new Clause(new QueryItem(CoreConstants.FIELD_MRO_NO, mroNo, SearchCondition.EQUALS), Operator.AND, 
											new Clause(new QueryItem("entity", entityObj, SearchCondition.EQUALS), null, null)));
							
							MobiculeLogger.verbose("MrO NuMbEr     " + mroNo + "   cursor.getCount()   "+ cursor.getCount());
							
							if (cursor.getCount() > 0)
							{
								cursor.moveToFirst();
								while (!cursor.isAfterLast())
								{
									MobiculeLogger.verbose("Cursor Id" + cursor.getInt(cursor.getColumnIndex("_id")) + "");
									
									databaseManager.update(entity, data, cursor.getInt(cursor.getColumnIndex("_id")));
									cursor.moveToNext();
								}
							}
							else
							{
								MobiculeLogger.verbose("saveJoinTicketing - inside add");
								
								databaseManager.add(entity, data);
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						finally
						{
							close(cursor);
						}
						return true;
					}

		}
		catch (Exception e1)
		{
			e1.printStackTrace();
			return false;
		}
		return false;
	}
  
	public void close(Cursor cursor)
	{
		if (cursor != null)
		{
			MobiculeLogger.verbose("Meter persistance - close cursor");
			cursor.close();
			cursor = null;
		}
	}

	public List<String> fetchAllSavedJoinTicketingsOnlyMroNums(String entity)
	{
		MobiculeLogger.verbose("fetchAllSavedJoinTicketingsOnlyMroNum - "+ entity);
		
		Cursor cursor = null;
		Set<String> savedJoinTicketing = new HashSet<String>();
		
		try
		{
			Clause clause = new Clause(new QueryItem(KEY_JT_SUBMIT, "0", SearchCondition.EQUALS), null, null);
			cursor = databaseManager.search(entity, clause);
			
			while(cursor.moveToNext())
			{
				/*jsonParser.setJson(cursor.getString(cursor.getColumnIndex(DATA)));
				String mroNumber = jsonParser.getValue(Constants.KEY_MRO_NUMBER);*/
				JSONObject reqObj = new JSONObject(cursor.getString(cursor.getColumnIndex(DATA)));
				JSONObject dataObj = reqObj.getJSONObject("data");
				savedJoinTicketing.add(dataObj.getString(Constants.KEY_MRO_NUMBER));
			}
			
			MobiculeLogger.verbose("fetchAllSavedJoinTicketingsOnlyMroNum - savedJoinTicketing: "+savedJoinTicketing);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(cursor != null)
			{
				cursor.close();
			}
		}
		
		return new ArrayList<String>(savedJoinTicketing);
		
		/*Vector<String> savedJoinTicketing = new Vector<String>();
		//check for saved meter readings and not the submitted ones.
		Clause joinTicketingClause = new Clause(new QueryItem(KEY_JT_SUBMIT, "0", SearchCondition.EQUALS), null, null);
		Cursor cursor = null;
		try 
		{ 
			if (getTableLists().contains(entity))
			{
				cursor = databaseManager.search(entity, joinTicketingClause);

				if (cursor != null) 
				{
					if (cursor.getCount() > 0)
					{
						while (cursor.moveToNext())
						{						
							jsonParser.setJson(cursor.getString(cursor.getColumnIndex(DATA)));
							String mroNumber = jsonParser.getValue(Constants.KEY_MRO_NUMBER);
							JSONObject jsonObj = new JSONObject();
							
							jsonObj.put(Constants.KEY_MRO_NUMBER, mroNumber);
							savedJoinTicketing.add(jsonObj.toString());
							
							savedJoinTicketing.add(cursor.getString(cursor.getColumnIndex(DATA)));
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Exception "+e.toString());
		}
		finally
		{
			close(cursor);
		}
		return savedJoinTicketing;*/
	}


	@Override
	public Vector fetchSavedJoinTicketing(String entity, String mroNO)
	{
		Vector savedJoinTicketing = new Vector();
		Clause clause = new Clause(new QueryItem(CoreConstants.FIELD_MRO_NO, mroNO, SearchCondition.EQUALS), null, null);
		Cursor cursor = null;
		try
		{
			cursor = databaseManager.search(entity, clause);

			if (cursor.getCount() > 0)
			{
				while (cursor.moveToNext())
				{
					savedJoinTicketing.add(cursor.getString(cursor.getColumnIndex(DATA)));
				}
			}
		}
		catch (NullPointerException e)
		{
			throw new RuntimeException("DATA NOT PRESENT");
		}
		finally
		{
			close(cursor);
		}
		return savedJoinTicketing;
	}
	
	@Override
	public boolean searchSaveJoinTicketingStatusByMro(String entity, String data)
	{
		Cursor cursor = null;
		try
		{
			jsonParser.setJson(data);
			mroNo = jsonParser.getValue(CoreConstants.FIELD_MRO_NO);

			if (StringUtil.isValid(mroNo) && (!(mroNo.equals("0"))))
			{
				cursor = databaseManager.search(entity, new Clause(new QueryItem(CoreConstants.FIELD_MRO_NO, mroNo,
						SearchCondition.CONTAINS), null, null));
				if (cursor.getCount() > 0)
				{
					return true;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			close(cursor);
		}
		return false;
	}

	

	public Vector<String> getTableLists()
	{
		Vector<String> tableNames = new Vector<String>();
		MobiculeLogger.verbose("APPLICATION PERSISTANCE - GET TABLE LIST");
		Cursor cursor = databaseManager.getTableNames();
		MobiculeLogger.verbose("NUMBER OF TABLES - COUNT = " + cursor.getCount());
		try
		{
			if (cursor.moveToFirst())
			{
				for (int i = 0; i < cursor.getCount(); i++)
				{
					tableNames.add(cursor.getString(cursor.getColumnIndex("name")));
					cursor.moveToNext();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		MobiculeLogger.verbose("tablesNames - "+ tableNames.toString());
		return tableNames;
	}

	@Override
	public Vector getSavedCustomerList(String connObj)
	{
		Vector data = new Vector();
		Cursor cursor = null;
		try
		{
			Clause joinTicketingClause = new Clause(new QueryItem(KEY_JT_SUBMIT, "0", SearchCondition.EQUALS), null,
					null);
			Clause connClause = new Clause(new QueryItem(Constants.FIELD_CONN_OBJ, connObj, SearchCondition.EQUALS),
					Operator.AND, joinTicketingClause);
			//following will give the no. of customers
			cursor = databaseManager.search(CoreConstants.TABLE_SAVED_JOIN_TICKETING, connClause);
			if (cursor.getCount() > 0)
			{
				//data = getRecordStore(data, cursor);
				data = getRecordStoreForCustomerList(data, cursor);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return data;
	}

	@Override
	public Vector<String> getSavedBuildingList()
	{
		Vector data = new Vector();
		Vector joinTicketingData = null;
		Vector connObjList = new Vector();
		Cursor cursor = null;
		try
		{
			//check for saved meter readings and not the submitted ones.
			Clause joinTicketingClause = new Clause(new QueryItem(KEY_JT_SUBMIT, "0", SearchCondition.EQUALS), null,
					null);
			
			cursor = databaseManager.search(CoreConstants.TABLE_SAVED_JOIN_TICKETING, joinTicketingClause);
			MobiculeLogger.verbose("*/**/*/SEARCH RESULT*/*/*/*/ " + cursor.getCount());
			if (cursor.getCount() > 0)
			{
				joinTicketingData = new Vector();
				//meterReadingData = getRecordStore(meterReadingData, cursor);
				joinTicketingData = getRecordStoreOnlyConnObj(joinTicketingData, cursor);
				MobiculeLogger.verbose("ONE READING - ",joinTicketingData.elementAt(0).toString());
				close(cursor);
				for (int i = 0; i < joinTicketingData.size(); i++)
				{
					jsonParser.setJson(joinTicketingData.elementAt(i).toString());
					String connObj = jsonParser.getValue(Constants.FIELD_CONN_OBJ);
					//close(cursor);
					if (!connObjList.contains(connObj))
					{
						connObjList.add(connObj);
						Clause connClause = new Clause(new QueryItem(Constants.FIELD_CONN_OBJ, connObj,
								SearchCondition.EQUALS), Operator.AND, joinTicketingClause);
						//following will give the no. of customers
						cursor = databaseManager.search(CoreConstants.TABLE_SAVED_JOIN_TICKETING, connClause);
						int customerCount = cursor.getCount();
						close(cursor);
						if (customerCount > 0)
						{
							Clause bookWalkClause = new Clause(new QueryItem(Constants.FIELD_CONN_OBJ, connObj,
									SearchCondition.EQUALS), null, null);
							//close(cursor);
							cursor = databaseManager.search(IBookWalkSqncePersistenceService.BOOKWALK_TABLE,
									bookWalkClause);
							if (cursor.getCount() > 0)
							{
								data = getRecordStore(data, cursor);
								JSONObject jsonObject = new JSONObject(data.lastElement().toString());
								jsonObject.put(Constants.FIELD_CUSTOMER_COUNT, customerCount);
								data.remove(data.lastElement());
								data.add(jsonObject.toString());
							}
							close(cursor);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return data;
	}

	
	private Vector<String> getRecordStoreForCustomerList(Vector<String> data, Cursor cursor)
	{
		while (cursor.moveToNext())
		{	
			try
			{
				jsonParser.setJson(cursor.getString(cursor.getColumnIndex("data")));
				String customerName = jsonParser.getValue(Constants.FIELD_CUSTOMER_NAME);
				String address = jsonParser.getValue(Constants.FIELD_ADDRESS);
				String bpNumber = jsonParser.getValue(Constants.FIELD_BP_NO);
				String mroNumber = jsonParser.getValue(Constants.KEY_MRO_NUMBER);
				
				JSONObject jsonObj = new JSONObject();
				jsonObj.put(Constants.FIELD_CUSTOMER_NAME, customerName);
				jsonObj.put(Constants.FIELD_ADDRESS, address);
				jsonObj.put(Constants.FIELD_BP_NO, bpNumber);
				jsonObj.put(Constants.KEY_MRO_NUMBER, mroNumber);
				
				data.add(jsonObj.toString());
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		//close(cursor);
		return data;
	}
	
	@Override
	public Vector<String> fetchAllSavedJoinTicketings(String entity)
	{
		Vector<String> savedJoinTicketing = new Vector<String>();
		//check for saved meter readings and not the submitted ones.
		Clause meterReadingClause = new Clause(new QueryItem(KEY_JT_SUBMIT, "0", SearchCondition.EQUALS), null, null);
		Cursor cursor = null;
		try
		{
			if (getTableLists().contains(entity))
			{
				cursor = databaseManager.search(entity, meterReadingClause);

				if (cursor != null)
				{
					if (cursor.getCount() > 0)
					{
						while (cursor.moveToNext())
						{
							savedJoinTicketing.add(cursor.getString(cursor.getColumnIndex(DATA)));
						}
					}
				}
			}
		}
		catch (NullPointerException e)
		{
			throw new RuntimeException("DATA NOT PRESENT");
		}
		finally
		{
			close(cursor);
		}
		return savedJoinTicketing;
	}
	
	private Vector<String> getRecordStoreOnlyConnObj(Vector<String> data, Cursor cursor)
	{
		while (cursor.moveToNext())
		{	
			try
			{
				jsonParser.setJson(cursor.getString(cursor.getColumnIndex("data")));
				String connObj = jsonParser.getValue(Constants.FIELD_CONN_OBJ);
				JSONObject jsonObj = new JSONObject();
				jsonObj.put(Constants.FIELD_CONN_OBJ, connObj);
				data.add(jsonObj.toString());
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		//close(cursor);
		return data;
	}
	
	private Vector<String> getRecordStore(Vector<String> data, Cursor cursor)
	{
		while (cursor.moveToNext())
		{
			data.add(cursor.getString(cursor.getColumnIndex("data")));
		}
		//close(cursor);
		return data;
	}

	@Override
	public List<String> getJoinTicketingDataByMro(String mroNo) 
	{
		Cursor cursor = null;
		List<String> data = new ArrayList<String>();
		
		try
		{
			Clause clause = new Clause(new QueryItem(KEY_MRO_NUMBER, mroNo, SearchCondition.EQUALS), Operator.AND, 
					new Clause(new QueryItem("jtSubmit", "0", SearchCondition.EQUALS), null, null));
			cursor = databaseManager.search(CoreConstants.TABLE_SAVED_JOIN_TICKETING, clause);
			
			while(cursor.moveToNext())
			{
				data.add(cursor.getString(cursor.getColumnIndex("data")));
			}
			
			MobiculeLogger.verbose("getJoinTicketingDataByMro - data.size(): "+data.size());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(cursor != null)
			{
				cursor.close();
			}
		}
		return data;
	}

	@Override
	public void addJoinTicketingImage(String imgJsonStr) 
	{
		MobiculeLogger.verbose("addJoinTicketingImage - inside addJoinTicketingImage");
		//databaseManager.add(CoreConstants.TABLE_JOIN_TICKETING_IMAGE, imgJsonStr);
		
		try
		{
		
		JSONObject requestObj = new JSONObject(imgJsonStr);
		JSONObject dataObj = requestObj.getJSONObject("data");
		String mroNo = dataObj.getString(CoreConstants.FIELD_MRO_NO);
		
		String entityObj = requestObj.getString("entity");		
		
		MobiculeLogger.verbose("addJoinTicketingImage - entityObj: "+ entityObj + "mroNo: "+mroNo);
	
		Cursor cursor = null;

		if (StringUtil.isValid(mroNo) && (!(mroNo.equals("0"))))
		{
					
						cursor = databaseManager.search(CoreConstants.TABLE_JOIN_TICKETING_IMAGE, 
								new Clause(new QueryItem(CoreConstants.FIELD_MRO_NO, mroNo, SearchCondition.EQUALS), Operator.AND, 
										new Clause(new QueryItem("entity", entityObj, SearchCondition.EQUALS), null, null)));
						
						MobiculeLogger.verbose("MrO NuMbEr     " + mroNo + "   cursor.getCount()   "+ cursor.getCount());
						
						try
						{
						
						if (cursor.getCount() > 0)
						{
							cursor.moveToFirst();
							while (!cursor.isAfterLast())
							{
								MobiculeLogger.verbose("Cursor Id" + cursor.getInt(cursor.getColumnIndex("_id")) + "");
								
								databaseManager.update(CoreConstants.TABLE_JOIN_TICKETING_IMAGE, imgJsonStr, cursor.getInt(cursor.getColumnIndex("_id")));
								cursor.moveToNext();
							}
						}
						else
						{
							MobiculeLogger.verbose("addJoinTicketingImage - inside add");
							
							databaseManager.add(CoreConstants.TABLE_JOIN_TICKETING_IMAGE, imgJsonStr);
						}
						
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						finally
						{
							if(cursor != null)
							{
								cursor.close();
							}
						}
			}
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void deleteFromJoinTicketingImage(String imgJsonStr)
	{
		Cursor cursor = null;
		
		try
		{
			if(imgJsonStr.equals(""))
			{
				MobiculeLogger.verbose("deleteFromJoinTicketingImage - inside deleteFromJoinTicketingImage");
				
				cursor = databaseManager.search(CoreConstants.TABLE_JOIN_TICKETING_IMAGE, null);
				
				MobiculeLogger.verbose("deleteFromJoinTicketingImage - cusor count: "+cursor.getCount());
				
				while(cursor.moveToNext())
				{
					int rowId = cursor.getInt(cursor.getColumnIndex("_id"));
					databaseManager.delete(CoreConstants.TABLE_JOIN_TICKETING_IMAGE, rowId);
				}				
			}
			else
			{
				JSONObject imgJson = new JSONObject(imgJsonStr);
				String entity = imgJson.getString("entity");
				JSONObject imgDataJson = imgJson.getJSONObject("data");
				String mroNo = imgDataJson.getString(KEY_MRO_NUMBER);
				
				MobiculeLogger.verbose("deleteFromJoinTicketingImage - inside deleteFromJoinTicketingImage");
				
				cursor = databaseManager.search(CoreConstants.TABLE_JOIN_TICKETING_IMAGE, 
						new Clause(new QueryItem("entity", entity, SearchCondition.EQUALS), Operator.AND, 
								new Clause(new QueryItem(KEY_MRO_NUMBER, mroNo, SearchCondition.EQUALS), null, null)));
				
				MobiculeLogger.verbose("deleteFromJoinTicketingImage - cusor count: "+cursor.getCount());
				
				while(cursor.moveToNext())
				{
					int rowId = cursor.getInt(cursor.getColumnIndex("_id"));
					databaseManager.delete(CoreConstants.TABLE_JOIN_TICKETING_IMAGE, rowId);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(cursor != null)
			{
				cursor.close();
			}
		}		
	}

	@Override
	public List<String> fetchAllJoinTicketingImages(String mro) 
	{
		Cursor cursor = null;
		List<String> allImagesList = new ArrayList<String>();
		
		MobiculeLogger.verbose("fetchAllJoinTicketingImages - inside fetchAllJoinTicketingImages");
		
		try
		{
			//String query = "select * from " + CoreConstants.TABLE_JOIN_TICKETING_IMAGE;
			Clause meterReadingClause = new Clause(new QueryItem(KEY_MRO_NUMBER,mro , SearchCondition.EQUALS), null, null);
			cursor= databaseManager.search(CoreConstants.TABLE_JOIN_TICKETING_IMAGE, null);
			
			MobiculeLogger.verbose("fetchAllJoinTicketingImages - cursor count: "+cursor.getCount());
			if(cursor.getCount()>0)
			{
			  while(cursor.moveToNext())
		      { 
				allImagesList.add(cursor.getString(cursor.getColumnIndex("data")));
			  }
		   }	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(cursor != null)
			{
				cursor.close();
			}
		}
		return allImagesList;
	}	
}
