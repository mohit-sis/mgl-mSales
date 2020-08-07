package com.mobicule.android.msales.mgl.downloadbookwalk.model;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.mobicule.android.component.db.Clause;
import com.mobicule.android.component.db.Operator;
import com.mobicule.android.component.db.QueryItem;
import com.mobicule.android.component.db.SQLiteDatabaseManager;
import com.mobicule.android.component.db.SearchCondition;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.SessionData;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.FileUtilDate;
import com.mobicule.component.json.JSONUtil;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.component.string.StringUtil;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncePersistenceService;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqnceRequestBuilder;

/**
* 
* <enter description here>
*
* @author Sathya Sheela
* @see 
*
* @createdOn Mar 6, 2012
* @modifiedOn
*
* @copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class DefaultBookWalkSqncePersistenceService implements IBookWalkSqncePersistenceService
{

	private static final String CODE = "cd";

	private static final String NAME = "nm";

	private static IBookWalkSqncePersistenceService instance;

	private SQLiteDatabaseManager databaseManager;

	private JSONParser jsonParser;

	private String SYNC_TABLE_ENTITY = "entity";

	private String searchQuery;

	private String searchQueryForDeleteUpdateTable;

	private DefaultBookWalkSqncePersistenceService(Context context)
	{
		databaseManager = (SQLiteDatabaseManager) SQLiteDatabaseManager.getInstance(context,Constants.DATABASE_NAME);
		databaseManager.createTable(CoreConstants.BOOKWALK_TABLE);
		databaseManager.createTable(CoreConstants.TABLE_CUSTOMER_INFO);
	}

	public static synchronized IBookWalkSqncePersistenceService getPersistenceService(Context context)
	{
		if (instance == null)
		{
			instance = new DefaultBookWalkSqncePersistenceService(context);
		}

		return instance;
	}

	public void insertOrUpdateBookwalk(String entity, String syncResponse, Clause clause)
	{
		Cursor cursor = null;
		cursor = databaseManager.search(entity, clause);
		this.insertOrUpdate(entity, syncResponse, cursor);
	}

	/*	public void add(String entity, String syncResponse)
		{
			try
			{
				if (StringUtil.isValid(syncResponse))
				{
					jsonParser.setJson(syncResponse);
					String connObj = jsonParser.getValue(CoreConstants.FIELD_CONN_OBJ);
					if (StringUtil.isValid(connObj))
					{
						//following is to maintain a different table for buiding/Conn details
						JSONObject jsonObject = new JSONObject(syncResponse);
						if (JSONUtil.isValid(jsonObject) && jsonObject.has(CoreConstants.FIELD_CUSTOMER_LIST))
						{
							jsonObject.remove(CoreConstants.FIELD_CUSTOMER_LIST);
							insertOrUpdateBookwalk(entity, jsonObject.toString(), new Clause(new QueryItem(CoreConstants.FIELD_CONN_OBJ,
									connObj, SearchCondition.EQUALS), null, null));
						}
						
						int bookwalk_id = find_idInDataBase(CoreConstants.BOOKWALK_TABLE, connObj);
						
						//to maintain only customer details based on the connObj
						Vector customerList = jsonParser.getArray(CoreConstants.FIELD_CUSTOMER_LIST);
						if (customerList != null)
						{
							for (int i = 0; i < customerList.size(); i++)
							{
								JSONObject customerJson = new JSONObject(customerList.elementAt(i).toString());
								customerJson.put(Constants.FIELD_STATUS, Constants.FIELD_UNATTEMPTED);
								
								ContentValues contentValues = new ContentValues();
								contentValues.put("data", customerJson.toString());
								contentValues.put("bookwalk_id", bookwalk_id);
								
								//databaseManager.add(CoreConstants.TABLE_CUSTOMER_INFO, CREATE_CUSTOMER_INFO, contentValues);
								searchQuery = "SELECT * FROM "+ CoreConstants.TABLE_CUSTOMER_INFO +" WHERE "+CoreConstants.TABLE_COLUMN_BOOKWALK_ID+"='"+bookwalk_id+"'"
										+" AND "+CoreConstants.TABLE_COLUMN_DATA+" LIKE '%\""+CoreConstants.FIELD_MRO_NO+"\":\""+ customerJson.getString(CoreConstants.FIELD_MRO_NO)+"\"%';";
								
								Cursor cursor = databaseManager.executeSearchQuary(CoreConstants.TABLE_CUSTOMER_INFO, CoreConstants.CREATE_CUSTOMER_INFO, searchQuery);
								try
								{
	//								if (cursor.getCount() == 0)
	//								{
	//									databaseManager.add(CoreConstants.TABLE_CUSTOMER_INFO, CoreConstants.CREATE_CUSTOMER_INFO, contentValues);
	//								}
	//								else
	//								{
	//									if (cursor.moveToFirst())
	//									{
	//										for (int j = 0; j < cursor.getCount(); j++)
	//										{
	//											int rowid = cursor.getInt(cursor.getColumnIndex("_id"));
	//											databaseManager.update(CoreConstants.TABLE_CUSTOMER_INFO, CoreConstants.CREATE_CUSTOMER_INFO, contentValues, rowid);
	//											cursor.moveToNext();
	//										}
	//									}
	//								}
									if (!(cursor.getCount() > 0))
									{
										databaseManager.add(CoreConstants.TABLE_CUSTOMER_INFO, CoreConstants.CREATE_CUSTOMER_INFO, contentValues);
									}
									else
									{
										while(cursor.moveToNext())
										{
											int rowid = cursor.getInt(cursor.getColumnIndex("_id"));
											databaseManager.update(CoreConstants.TABLE_CUSTOMER_INFO, CoreConstants.CREATE_CUSTOMER_INFO, contentValues, rowid);
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
								
								//insertOrUpdateBookwalk(CoreConstants.TABLE_CUSTOMER_INFO, customerJson.toString(), new Clause(new QueryItem(FIELD_BP_NO, customerJson.getString(FIELD_BP_NO),SearchCondition.EQUALS), null, null));
							}
						}
					}
					else
					{
						databaseManager.add(entity, syncResponse);
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	*/
	public void add(String entity, String syncResponse)
	{
		try
		{
			if (StringUtil.isValid(syncResponse))
			{
				JSONObject sysJsonObj = new JSONObject(syncResponse);
				String connObj = sysJsonObj.getString(CoreConstants.FIELD_CONN_OBJ);
				if (StringUtil.isValid(connObj))
				{
					//following is to maintain a different table for buiding/Conn details
					JSONObject jsonObject = new JSONObject(syncResponse);
					if (JSONUtil.isValid(jsonObject) && jsonObject.has(CoreConstants.FIELD_CUSTOMER_LIST))
					{
						jsonObject.remove(CoreConstants.FIELD_CUSTOMER_LIST);
						insertOrUpdateBookwalk(entity, jsonObject.toString(), new Clause(new QueryItem(
								CoreConstants.FIELD_CONN_OBJ, connObj, SearchCondition.EQUALS), null, null));
					}

					int bookwalk_id = find_idInDataBase(CoreConstants.BOOKWALK_TABLE, connObj);
					MobiculeLogger.verbose("bookwalk_id - " + bookwalk_id);

					//to maintain only customer details based on the connObj
					JSONArray customerList = sysJsonObj.getJSONArray(CoreConstants.FIELD_CUSTOMER_LIST);
					MobiculeLogger.verbose("cusotmer list"+ customerList.toString());
					if (customerList != null)
					{
						for (int i = 0; i < customerList.length(); i++)
						{
							JSONObject customerJson = customerList.getJSONObject(i);
							customerJson.put(Constants.FIELD_STATUS, Constants.FIELD_UNATTEMPTED);

							ContentValues contentValues = new ContentValues();
							contentValues.put("data", customerJson.toString());
							contentValues.put("bpNumber", customerJson.getString(CoreConstants.FIELD_BP_NO));
							contentValues.put("meterNo", customerJson.getString("meterNo"));
							contentValues.put("contactNo", customerJson.getString("contactNo"));
							contentValues.put("customerName", customerJson.getString(Constants.KEY_CUSTOMER_NAME));
							contentValues.put("flatNo", customerJson.getString(Constants.FIELD_FLAT_NO));
							contentValues.put("bookwalk_id", bookwalk_id);
							contentValues.put("buildingName", customerJson.getString("buildName"));
							contentValues.put("contactNoOffice", customerJson.getString("offcNo"));
							contentValues.put("contactNoRes", customerJson.getString("resNo"));

							//databaseManager.add(CoreConstants.TABLE_CUSTOMER_INFO, CREATE_CUSTOMER_INFO, contentValues);
							searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
									+ CoreConstants.TABLE_COLUMN_BOOKWALK_ID + "='" + bookwalk_id + "'" + " AND "
									+ CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\"" + CoreConstants.FIELD_MRO_NO
									+ "\":\"" + customerJson.getString(CoreConstants.FIELD_MRO_NO) + "\"%';";

							Cursor cursor = databaseManager.executeRawQuery(CoreConstants.TABLE_CUSTOMER_INFO, searchQuery);
							try
							{
								//								if (cursor.getCount() == 0)
								//								{
								//									databaseManager.add(CoreConstants.TABLE_CUSTOMER_INFO, CoreConstants.CREATE_CUSTOMER_INFO, contentValues);
								//								}
								//								else
								//								{
								//									if (cursor.moveToFirst())
								//									{
								//										for (int j = 0; j < cursor.getCount(); j++)
								//										{
								//											int rowid = cursor.getInt(cursor.getColumnIndex("_id"));
								//											databaseManager.update(CoreConstants.TABLE_CUSTOMER_INFO, CoreConstants.CREATE_CUSTOMER_INFO, contentValues, rowid);
								//											cursor.moveToNext();
								//										}
								//									}
								//								}
								if (!(cursor.getCount() > 0))
								{
									MobiculeLogger.verbose("add in to table*** "
											+ customerJson.toString());
									databaseManager.add(CoreConstants.TABLE_CUSTOMER_INFO, contentValues);
								}
								else
								{
									while (cursor.moveToNext())
									{
										int rowid = cursor.getInt(cursor.getColumnIndex("_id"));
										MobiculeLogger.verbose("update in to CustomerInfo table" + customerJson.toString());
										databaseManager.update(CoreConstants.TABLE_CUSTOMER_INFO, contentValues, rowid);
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

							//insertOrUpdateBookwalk(CoreConstants.TABLE_CUSTOMER_INFO, customerJson.toString(), new Clause(new QueryItem(FIELD_BP_NO, customerJson.getString(FIELD_BP_NO),SearchCondition.EQUALS), null, null));
						}
					}
				}
				else
				{
					databaseManager.add(entity, syncResponse);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*		
		public void delete(String entity, String syncResponse)
		{
			try
			{
				Cursor cursor;
				int rowId;
				jsonParser.setJson(syncResponse);
				if (entity.equals(CoreConstants.TABLE_SYNC))
				{
					cursor = databaseManager.search(entity,
							new Clause(new QueryItem(SYNC_TABLE_ENTITY, jsonParser.getValue(SYNC_TABLE_ENTITY),
									SearchCondition.EQUALS), null, null));
					if (cursor.moveToFirst())
					{
						rowId = cursor.getInt(cursor.getColumnIndex("_id"));
						databaseManager.delete(entity, rowId);
					}
					close(cursor);
				}
				else if (entity.equals(BOOKWALK_TABLE))
				{
					try
					{
						if (StringUtil.isValid(syncResponse))
						{
	//						jsonParser.setJson(syncResponse);
	//						String connObj = jsonParser.getValue(FIELD_CONN_OBJ);
	//						if (StringUtil.isValid(connObj))
	//						{
	//							
	//							//to maintain only customer details based on the connObj
	//							Vector customerList = jsonParser.getArray(FIELD_CUSTOMER_LIST);
	//							if (customerList != null)
	//							{
	//								for (int i = 0; i < customerList.size(); i++)
	//								{
	//									jsonParser.setJson(customerList.elementAt(i).toString());
	//									cursor = databaseManager.search(entity + "_" + connObj, new Clause(new QueryItem(
	//											FIELD_BP_NO, jsonParser.getValue(FIELD_BP_NO), SearchCondition.EQUALS),
	//											null, null));
	//									if (cursor.getCount() > 0)
	//									{
	//										if (cursor.moveToFirst())
	//										{
	//											rowId = cursor.getInt(cursor.getColumnIndex("_id"));
	//											databaseManager.delete(entity + "_" + connObj, rowId);
	//										}
	//									}
	//									close(cursor);
	//								}
	//							}
	//							//if all customer info is deleted, delete the building info from Bookwalk table
	//							cursor = databaseManager.search(entity + "_" + connObj, null);
	//							int count = cursor.getCount();
	//							close(cursor);
	//							if (count == 0)
	//							{
	//								cursor = databaseManager.search(entity, new Clause(new QueryItem(FIELD_CONN_OBJ,
	//										connObj, SearchCondition.EQUALS), null, null));
	//								if (cursor.getCount() > 0)
	//								{
	//									if (cursor.moveToFirst())
	//									{
	//										rowId = cursor.getInt(cursor.getColumnIndex("_id"));
	//										databaseManager.delete(entity, rowId);
	//									}
	//								}
	//								close(cursor);
	//							}
	//						}
							jsonParser.setJson(syncResponse);
							String connObj = jsonParser.getValue(CoreConstants.FIELD_CONN_OBJ);
							
							if (StringUtil.isValid(connObj))
							{
								int bookwalk_id = find_idInDataBase(BOOKWALK_TABLE, connObj);
								//to maintain only customer details based on the connObj
								Vector customerList = jsonParser.getArray(CoreConstants.FIELD_CUSTOMER_LIST);
								if (customerList != null)
								{
									for (int i = 0; i < customerList.size(); i++)
									{
										jsonParser.setJson(customerList.elementAt(i).toString());
										//SELECT * FROM CUSTOMER_INFO where bookwalk_id='2' AND data like '%"bpNo":"1100186436"%';
										searchQuery = "SELECT * FROM "+ CoreConstants.TABLE_CUSTOMER_INFO +" WHERE "+CoreConstants.TABLE_COLUMN_BOOKWALK_ID+"='"+bookwalk_id+"'"
												+" AND "+CoreConstants.TABLE_COLUMN_DATA+ " LIKE '%\""+CoreConstants.FIELD_MRO_NO+"\":\""+ jsonParser.getValue(CoreConstants.FIELD_MRO_NO)+"\"%';";
										cursor = databaseManager.executeSearchQuary(CoreConstants.TABLE_CUSTOMER_INFO, CoreConstants.CREATE_CUSTOMER_INFO, searchQuery);
												
										if (cursor.getCount() > 0)
										{
											if (cursor.moveToFirst())
											{
												rowId = cursor.getInt(cursor.getColumnIndex("_id"));
												databaseManager.delete(CoreConstants.TABLE_CUSTOMER_INFO, rowId);
											}
										}
										close(cursor);
									}
								}
								//if all customer info is deleted, delete the building info from Bookwalk table
								searchQuery = "SELECT * FROM "+ CoreConstants.TABLE_CUSTOMER_INFO +" WHERE "+CoreConstants.TABLE_COLUMN_BOOKWALK_ID+"='"+bookwalk_id+"';";
								cursor = databaseManager.executeSearchQuary(CoreConstants.TABLE_CUSTOMER_INFO, CoreConstants.CREATE_CUSTOMER_INFO, searchQuery);
								int count = cursor.getCount();
								close(cursor);
								if (count == 0)
								{
									cursor = databaseManager.search(entity, new Clause(new QueryItem(CoreConstants.FIELD_CONN_OBJ,
											connObj, SearchCondition.EQUALS), null, null));
									if (cursor.getCount() > 0)
									{
										if (cursor.moveToFirst())
										{
											rowId = cursor.getInt(cursor.getColumnIndex("_id"));
											databaseManager.delete(entity, rowId);
										}
									}
									close(cursor);
								}
							}
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}

				}
				else
				{
					String name = jsonParser.getValue(NAME);
					String code = jsonParser.getValue(CODE);
					cursor = databaseManager.search(entity, new Clause(new QueryItem(NAME, name, SearchCondition.EQUALS),
							Operator.AND, new Clause(new QueryItem(CODE, code, SearchCondition.EQUALS), null, null)));
					try
					{
						if (cursor.moveToFirst())
						{
							rowId = cursor.getInt(cursor.getColumnIndex("_id"));
							databaseManager.delete(entity, rowId);
						}
						close(cursor);
					}
					catch (Exception e)
					{
						throw new RuntimeException("DATA IS NOT PRESENT IN YOUR LOCAL DATABASE. ");
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	*/

	public void delete(String entity, String syncResponse)
	{
		try
		{
			Cursor cursor;
			Cursor cursorUpdate;
			int rowId;
			JSONObject sysJsonObject = new JSONObject(syncResponse);

			if (entity.equals(CoreConstants.TABLE_SYNC))
			{
				cursor = databaseManager.search(entity,
						new Clause(new QueryItem(SYNC_TABLE_ENTITY, sysJsonObject.getString(SYNC_TABLE_ENTITY),
								SearchCondition.EQUALS), null, null));
				if (cursor.moveToFirst())
				{
					rowId = cursor.getInt(cursor.getColumnIndex("_id"));
					MobiculeLogger.verbose("delete - " + rowId);
					databaseManager.delete(entity, rowId);
				}
				close(cursor);
			}
			else if (entity.equals(BOOKWALK_TABLE))
			{
				try
				{
					if (StringUtil.isValid(syncResponse))
					{
						//jsonParser.setJson(syncResponse);
						String connObj = sysJsonObject.getString(CoreConstants.FIELD_CONN_OBJ);
						if (StringUtil.isValid(connObj))
						{
							int bookwalk_id = find_idInDataBase(BOOKWALK_TABLE, connObj);
							//to maintain only customer details based on the connObj
							JSONArray customerList = sysJsonObject.getJSONArray(CoreConstants.FIELD_CUSTOMER_LIST);
							if (customerList != null)
							{
								for (int i = 0; i < customerList.length(); i++)
								{
									//jsonParser.setJson(customerList.elementAt(i).toString());
									JSONObject customerJsonObj = customerList.getJSONObject(i);
									//SELECT * FROM CUSTOMER_INFO where bookwalk_id='2' AND data like '%"bpNo":"1100186436"%';
									searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
											+ CoreConstants.TABLE_COLUMN_BOOKWALK_ID + "='" + bookwalk_id + "'"
											+ " AND " + CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\""
											+ CoreConstants.FIELD_MRO_NO + "\":\""
											+ customerJsonObj.getString(CoreConstants.FIELD_MRO_NO) + "\"%';";
									cursor = databaseManager.executeRawQuery(CoreConstants.TABLE_CUSTOMER_INFO, searchQuery);

									if (cursor.getCount() > 0)
									{
										if (cursor.moveToFirst())
										{
											rowId = cursor.getInt(cursor.getColumnIndex("_id"));
											MobiculeLogger.verbose("delete - " + rowId);
											databaseManager.delete(CoreConstants.TABLE_CUSTOMER_INFO, rowId);
										}
									}
									close(cursor);
								}
							}
							//if all customer info is deleted, delete the building info from Bookwalk table
							searchQuery = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_INFO + " WHERE "
									+ CoreConstants.TABLE_COLUMN_BOOKWALK_ID + "='" + bookwalk_id + "';";
							cursor = databaseManager.executeRawQuery(CoreConstants.TABLE_CUSTOMER_INFO, searchQuery);
							int count = cursor.getCount();
							close(cursor);
							if (count == 0)
							{
								cursor = databaseManager.search(entity, new Clause(new QueryItem(
										CoreConstants.FIELD_CONN_OBJ, connObj, SearchCondition.EQUALS), null, null));
								if (cursor.getCount() > 0)
								{
									if (cursor.moveToFirst())
									{
										rowId = cursor.getInt(cursor.getColumnIndex("_id"));
										MobiculeLogger.verbose("delete - " + rowId);
										databaseManager.delete(entity, rowId);
									}
								}
								close(cursor);
							}
						}
					}

					JSONArray customerList1 = sysJsonObject.getJSONArray(CoreConstants.FIELD_CUSTOMER_LIST);
					if (customerList1 != null)
					{
						for (int i = 0; i < customerList1.length(); i++)
						{
							JSONObject customerJsonObj = customerList1.getJSONObject(i);
							String mroNo = customerJsonObj.getString(Constants.KEY_MRO_NUMBER);
							/*searchQuery = "SELECT * FROM " + CoreConstants.TABLE_SAVED_METER_READING + " WHERE "
									+ CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\"" + Constants.KEY_MRO_NUMBER
									+ "\":\"" + mroNo + "%';";*/
							searchQuery = "SELECT * FROM " + CoreConstants.TABLE_SAVED_METER_READING + " WHERE "
									+ CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\"" + Constants.KEY_MRO_NUMBER
									+ "\":\"" + mroNo + "\"%';";//new
							cursor = databaseManager.executeRawQuery(CoreConstants.TABLE_SAVED_METER_READING, searchQuery);

							if (cursor.getCount() > 0)
							{
								if (cursor.moveToFirst())
								{
									rowId = cursor.getInt(cursor.getColumnIndex("_id"));
									MobiculeLogger.verbose("delete - " + rowId);
									databaseManager.delete(CoreConstants.TABLE_SAVED_METER_READING, rowId);
								}
							}

							searchQueryForDeleteUpdateTable = "SELECT * FROM " + CoreConstants.TABLE_CUSTOMER_UPDATE
									+ " WHERE " + CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\""
									+ Constants.KEY_MRO_NUMBER + "\":\"" + mroNo + "\"%';";//new
							cursorUpdate = databaseManager.executeRawQuery(CoreConstants.TABLE_CUSTOMER_UPDATE, searchQueryForDeleteUpdateTable);

							if (cursorUpdate.getCount() > 0)
							{
								if (cursorUpdate.moveToFirst())
								{
									rowId = cursorUpdate.getInt(cursorUpdate.getColumnIndex("_id"));
									MobiculeLogger.verbose("delete - " + rowId);
									databaseManager.delete(CoreConstants.TABLE_CUSTOMER_UPDATE, rowId);
								}
							}
							close(cursorUpdate);
							close(cursor);
						}
					}

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
			else
			{
				jsonParser.setJson(syncResponse);
				String name = jsonParser.getValue(NAME);
				String code = jsonParser.getValue(CODE);
				cursor = databaseManager.search(entity, new Clause(new QueryItem(NAME, name, SearchCondition.EQUALS),
						Operator.AND, new Clause(new QueryItem(CODE, code, SearchCondition.EQUALS), null, null)));
				try
				{
					if (cursor.moveToFirst())
					{
						rowId = cursor.getInt(cursor.getColumnIndex("_id"));
						MobiculeLogger.verbose("delete - " + rowId);
						databaseManager.delete(entity, rowId);
					}
					close(cursor);
				}
				catch (Exception e)
				{
					throw new RuntimeException("DATA IS NOT PRESENT IN YOUR LOCAL DATABASE. ");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void close(Cursor cursor)
	{
		if (cursor != null)
		{
			cursor.close();
		}
	}

	public String getLastSyncDate(String entity)
	{
		jsonParser = JSONParser.getInstance();
		String lastSyncDate = "0";
		String value = entity;
		String data = null;
		Cursor cursor = databaseManager.search(CoreConstants.TABLE_SYNC, new Clause(new QueryItem(SYNC_TABLE_ENTITY,
				value, SearchCondition.ENDS_WITH), null, null));
		try
		{
			if (cursor.moveToFirst())
			{
				data = cursor.getString(cursor.getColumnIndex("data"));
				jsonParser.setJson(data);
				MobiculeLogger.verbose("lastsyncDate"+ data);
				String syncDate = jsonParser.getValue(Constants.FIELD_LAST_SYNC_DATE);
				if (!StringUtil.isValid(syncDate))
				{
					syncDate = "0";
				}
				lastSyncDate = syncDate;
			}
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(cursor);
		}

		return lastSyncDate;
	}

	private String getRecord(Cursor cursor)
	{
		MobiculeLogger.verbose("Bookwalk persistance - getRecord");
		String data = "";
		while (cursor.moveToNext())
		{
			data = cursor.getString(cursor.getColumnIndex("data"));
		}
		return data;
	}

	public int find_idInDataBase(String entity, String conObj)
	{
		Cursor cursor = null;
		try
		{
			if (entity.equals(CoreConstants.BOOKWALK_TABLE))
			{
				cursor = databaseManager.search(entity, new Clause(new QueryItem(CoreConstants.FIELD_CONN_OBJ, conObj,
						SearchCondition.EQUALS), null, null));
				MobiculeLogger.verbose("find_idInDataBase - 1");
				if (cursor.getCount() == 0)
				{
					MobiculeLogger.verbose("find_idInDataBase - 2");
					MobiculeLogger.verbose("add - "+ entity + " data - " + conObj);
					return -1;
				}
				else
				{
					if (cursor.moveToFirst())
					{
						MobiculeLogger.verbose("find_idInDataBase - 3");
						MobiculeLogger.verbose("update - "+ entity + " data - " + conObj);
						return cursor.getInt(cursor.getColumnIndex("_id"));
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
		return -1;
	}

	public void update(String entity, String syncResponse)
	{
		Cursor cursor = null;
		jsonParser = JSONParser.getInstance();
		jsonParser.setJson(syncResponse);
		JSONObject dataJson = null;
		FileUtilDate.writeToFile("****DefaultBookwalkPersistenceService : In upadte() : syncresponse--->"+syncResponse);
		try
		{
			if (entity.equals(CoreConstants.TABLE_SYNC))
			{
				cursor = databaseManager.search(entity,
						new Clause(new QueryItem(SYNC_TABLE_ENTITY, jsonParser.getValue(SYNC_TABLE_ENTITY),
								SearchCondition.EQUALS), null, null));
				if (cursor.getCount() > 0)
				{
					String data = getRecord(cursor);
					if (StringUtil.isValid(data))
					{
						dataJson = new JSONObject(data);
						dataJson.put(Constants.FIELD_LAST_SYNC_DATE,
								jsonParser.getValue(Constants.FIELD_LAST_SYNC_DATE));
					}
					if (JSONUtil.isValid(dataJson))
					{
						FileUtilDate.writeToFile("****DefaultBookwalkPersistenceService : In upadte() : dataJsonR--->"+ dataJson.toString());
						this.insertOrUpdate(entity, dataJson.toString(), cursor);
					}
				}
				else
				{
					this.insertOrUpdate(entity, syncResponse, cursor);
				}
			}
			else
			{
				jsonParser.setJson(syncResponse);
				String code = jsonParser.getValue(CODE);
				cursor = databaseManager.search(entity, new Clause(new QueryItem(CODE, code, SearchCondition.EQUALS),
						null, null));
				this.insertOrUpdate(entity, syncResponse, cursor);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			FileUtilDate.exceptionToFile();
		}
		finally
		{
			close(cursor);
		}
	}

	private void insertOrUpdate(String entity, String syncResponse, Cursor cursor)
	{
		try
		{
			if (cursor.getCount() == 0)
			{
				MobiculeLogger.verbose("add - "+ entity + " data - " + syncResponse);
				databaseManager.add(entity, syncResponse);
			}
			else
			{
				if (cursor.moveToFirst())
				{
					MobiculeLogger.verbose("update - "+ entity + " data - " + syncResponse);
					for (int i = 0; i < cursor.getCount(); i++)
					{
						int rowid = cursor.getInt(cursor.getColumnIndex("_id"));
						databaseManager.update(entity, syncResponse, rowid);
						cursor.moveToNext();
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
	}

	@Override
	public Vector load(String entity)
	{
		String str;
		Vector<String> row = new Vector<String>();
		Cursor cursor = databaseManager.search(entity, null);
		try
		{
			if (cursor.moveToFirst())
			{
				for (int i = 0; i < cursor.getCount(); i++)
				{
					str = cursor.getString(cursor.getColumnIndex("data"));
					if ((str != null) && (str.length() != 0))
					{
						row.add(str);
					}
					else
					{
						delete(entity, str);
					}
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
		return row;
	}

	public Vector load(String entity, String key, String value)
	{
		Vector<String> row = new Vector<String>();
		Cursor cursor = databaseManager.search(entity, new Clause(new QueryItem(key, value, SearchCondition.EQUALS),
				null, null));
		try
		{
			if (cursor.moveToFirst())
			{
				for (int i = 0; i < cursor.getCount(); i++)
				{
					row.add(cursor.getString(cursor.getColumnIndex("data")));
					MobiculeLogger.verbose("Persistance for table names DefaultSyncPersistenceService - "+ cursor.getString(cursor.getColumnIndex("data")));
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
		return row;
	}

	public void dropTable(String tableName)
	{
		MobiculeLogger.verbose("dropping table - ", tableName);
		databaseManager.dropTable(tableName);
	}

	public void renameTableName(String arg0, String arg1)
	{
		databaseManager.renameTable(arg0, arg1);
	}

	@Override
	public void updateUserData(String entity, String response, IBookWalkSqnceRequestBuilder requestBuilder)
	{
		Cursor cursor = null;
		jsonParser = JSONParser.getInstance();
		jsonParser.setJson(response);
		JSONObject dataJson = null;
		
		FileUtilDate.writeToFile("****DefaultBookwalkPersistenceService : In updateUserData : response--->"+response);
		
		if (entity.equals(CoreConstants.USER_DETAIL_JESONOBJECT))
		{
			try
			{
				cursor = databaseManager.search(entity, null);  
				if (cursor.getCount() > 0)
				{
					String data = getRecord(cursor);
					if (StringUtil.isValid(data))
					{
						dataJson = new JSONObject(data);
						String versioinNo = jsonParser.getValue(Constants.FIELD_VERSION_NO);
						String bwkName = jsonParser.getValue(Constants.FIELD_BWK_NAME);
                        dataJson.put(Constants.FIELD_VERSION_NO, versioinNo);
                        dataJson.put(Constants.FIELD_BWK_NAME, bwkName);
					}
					if (JSONUtil.isValid(dataJson))
					{
						this.insertOrUpdate(entity, dataJson.toString(), cursor);
						SessionData.userInfo = dataJson.toString();
						requestBuilder.setUserJson(SessionData.userInfo);
					}
				}
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
				FileUtilDate.exceptionToFile();
			}
			finally
			{
				close(cursor);
			}
			updateSyncData();
		}
	}

	public Vector<String> getTableLists()
	{
		Cursor cursor = null;
		Vector<String> tableNames = new Vector<String>();
		MobiculeLogger.verbose("PERSISTANCE - GET TABLE LIST");
		cursor = databaseManager.getTableNames();
		MobiculeLogger.verbose("NUMBER OF TABLES - COUNT : " + cursor.getCount());
		try
		{
			if (cursor.moveToFirst())
			{
				while (cursor.moveToNext())
				{
					tableNames.add(cursor.getString(cursor.getColumnIndex("name")));
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
		return tableNames;
	}

	private void updateSyncData()
	{
		Cursor sync_cursor = null;
		JSONObject dataJson = new JSONObject();
		//update start date and sync date and sync table
		try
		{
			sync_cursor = databaseManager.search(CoreConstants.TABLE_SYNC, null);

			String lastSyncDate = jsonParser.getValue(Constants.FIELD_LAST_SYNC_DATE);
			String startDate = jsonParser.getValue(Constants.FIELD_START_DATE);
			String endDate = jsonParser.getValue(Constants.FIELD_END_DATE);
			String bookWalkAlarmTime = jsonParser.getValue(Constants.FIELD_BLKWLK_ALARM_TIME);

			dataJson.put(Constants.FIELD_START_DATE, startDate);
			dataJson.put(Constants.FIELD_END_DATE, endDate);
			dataJson.put(Constants.FIELD_BLKWLK_ALARM_TIME, bookWalkAlarmTime);
			dataJson.put(Constants.FIELD_LAST_SYNC_DATE, lastSyncDate);
			dataJson.put(Constants.FIELD_SYNC_STATUS, "complete");
			dataJson.put(SYNC_TABLE_ENTITY, IBookWalkSqncePersistenceService.BOOKWALK_TABLE);

			if (JSONUtil.isValid(dataJson))
			{
				this.insertOrUpdate(CoreConstants.TABLE_SYNC, dataJson.toString(), sync_cursor);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(sync_cursor);
		}
	}

	@Override
	public boolean checkForNewBookWalk(String response)
	{
		Cursor cursor = null;
		jsonParser = JSONParser.getInstance();
		jsonParser.setJson(response);
		JSONObject dataJson = null;
		boolean flag = false;
		try
		{
			cursor = databaseManager.search(CoreConstants.USER_DETAIL_JESONOBJECT, null);
			if (cursor.getCount() > 0)
			{
				String data = getRecord(cursor);
				if (StringUtil.isValid(data))
				{
					dataJson = new JSONObject(data);
					String bwkName = jsonParser.getValue(Constants.FIELD_BWK_NAME);
					String oldBwkName = dataJson.getString(Constants.FIELD_BWK_NAME);
					// on getting a different bwkname.. clear the db.
					if (StringUtil.isValid(oldBwkName) && !oldBwkName.equals(bwkName))
					{
						Vector<String> tableNames = getTableLists();
						for (int i = 0; i < tableNames.size(); i++)
						{
							String tableName = tableNames.elementAt(i).toString();
							if (tableName.indexOf(IBookWalkSqncePersistenceService.BOOKWALK_TABLE) != -1)
							{
								dropTable(tableName);
							}
							if (tableName.equals(CoreConstants.TABLE_CUSTOMER_INFO))
							{
								dropTable(tableName);
							}
							if (tableName.equals(CoreConstants.TABLE_SAVED_METER_READING))
							{
								dropTable(tableName);
							}
						}
						flag = true;
					}
				}
			}
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		finally
		{
			close(cursor);
		}
		return flag;
	}

	@Override
	public void syncInit(String entity)
	{

		Cursor sync_cursor = null;
		JSONObject dataJson = null;
		try
		{
			sync_cursor = databaseManager.search(CoreConstants.TABLE_SYNC, null);

			if (sync_cursor != null && sync_cursor.getCount() > 0)
			{
				String data = getRecord(sync_cursor);
				if (StringUtil.isValid(data))
				{
					dataJson = new JSONObject(data);
					dataJson.put(Constants.FIELD_SYNC_STATUS, "inprogess");
				}
			}
			else
			{
				dataJson = new JSONObject();
				dataJson.put(Constants.FIELD_SYNC_STATUS, "inprogess");
			}

			if (JSONUtil.isValid(dataJson))
			{
				this.insertOrUpdate(CoreConstants.TABLE_SYNC, dataJson.toString(), sync_cursor);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}