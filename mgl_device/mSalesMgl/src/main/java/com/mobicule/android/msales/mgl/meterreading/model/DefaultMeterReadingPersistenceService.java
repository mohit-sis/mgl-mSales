package com.mobicule.android.msales.mgl.meterreading.model;

import android.content.Context;
import android.database.Cursor;

import com.mobicule.android.component.db.Clause;
import com.mobicule.android.component.db.Operator;
import com.mobicule.android.component.db.QueryItem;
import com.mobicule.android.component.db.SQLiteDatabaseManager;
import com.mobicule.android.component.db.SearchCondition;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.FileUtilDate;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.component.string.StringUtil;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.IBookWalkSqncePersistenceService;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingPersistance;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.util.Vector;

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
public class DefaultMeterReadingPersistenceService implements IMeterReadingPersistance
{

	private Context context;

	private static DefaultMeterReadingPersistenceService instance;

	private SQLiteDatabaseManager databaseManager;

	private static final String DATA = "data";

	private JSONParser jsonParser;

	private String readingElement;

	private String mroNo = "";

	private String date = "";

	private String mrCode = "";

	private String searchQuery;

	public DefaultMeterReadingPersistenceService(Context context)
	{
		this.context = context;
		jsonParser = JSONParser.getInstance();
		databaseManager = (SQLiteDatabaseManager) SQLiteDatabaseManager.getInstance(context, Constants.DATABASE_NAME);
	}

	public static IMeterReadingPersistance getPersistenceService(Context context)
	{
		if (instance == null)
		{
			instance = new DefaultMeterReadingPersistenceService(context);
		}
		return instance;
	}

	public Vector<String> getTableLists()
	{
		Vector<String> tableNames = new Vector<String>();
		MobiculeLogger.verbose("APPLICATION PERSISTANCE - GET TABLE LIST");
		Cursor cursor = databaseManager.getTableNames();
		MobiculeLogger.verbose("NUMBER OF TABLES : COUNT - " + cursor.getCount());

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

	public void DeleteMethod(String entity)
	{
		String CREATE_DELETE_QUERY = "CREATE TABLE IF NOT EXISTS " + entity
				+ " (_id INTEGER primary key autoincrement, " + "data Text not null);";
		Cursor cursor = null;

		String search = "SELECT * FROM " + entity + " WHERE " + CoreConstants.TABLE_COLUMN_DATA + " LIKE '%\""
				+ CoreConstants.FEY_READING + "\":[]%';";
		cursor = databaseManager.executeRawQuery(entity, search);

		while (cursor.moveToNext())
		{
			int rowID = cursor.getInt(cursor.getColumnIndex("_id"));
			databaseManager.delete(entity, rowID);
		}
		close(cursor);
	}

	/*@Override
	public void saveMeterReading(String entity, String data)
	{
		jsonParser.setJson(data);
		//String bpNo = jsonParser.getValue(Constants.KEY_BP_NUMBER);
		String mroNo = jsonParser.getValue(CoreConstants.FIELD_MRO_NO);
		Cursor cursor = null;

		try
		{
			cursor = databaseManager.search(entity, new Clause(new QueryItem(CoreConstants.FIELD_MRO_NO, mroNo,
					SearchCondition.CONTAINS), null, null));
			if (cursor.getCount() > 0)
			{
				if (cursor.moveToFirst())
				{
					databaseManager.update(entity, data, cursor.getInt(cursor.getColumnIndex("_id")));
				}
			}
			else
			{
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
	}*/
	@Override
	public boolean searchSaveMeterReadingStatusByMro(String entity, String data)
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

	@Override
	public boolean saveMeterReading(String entity, String data)
	{
		Vector readingVector = null;
		try
		{
			jsonParser.setJson(data);
			readingVector = new Vector();
			//	Log.e("savemeterreadingJson", data);

			//String bpNo = jsonParser.getValue(Constants.KEY_BP_NUMBER);
			mroNo = jsonParser.getValue(CoreConstants.FIELD_MRO_NO);

			MobiculeLogger.verbose("DefaultMeterReading saveMeterReading() - mroNo : " + mroNo + " readingVector : " + readingVector
					+ " jsonParser value: " + data);
		
			FileUtilDate.writeToFile("**********DefaultMeterReadingPersistance :In saveMeterReading()----> : MRO No"+mroNo);
			
			FileUtilDate.writeToFile("**********DefaultMeterReadingPersistance :In saveMeterReading()----> : Entity"+entity);
			
			readingVector = jsonParser.getArray(CoreConstants.FEY_READING);
			for (int i = 0; i < readingVector.size(); i++)
			{
				readingElement = readingVector.elementAt(i).toString();
				jsonParser.setJson(readingElement);
				date = jsonParser.getValue(CoreConstants.FIELD_READING_DATE);
				mrCode = jsonParser.getValue(CoreConstants.FIELD_MR_CODE);
			}

			Cursor cursor = null;

			MobiculeLogger.verbose("MRO No" + mroNo + " data" + data);

			if (StringUtil.isValid(mroNo) && (!(mroNo.equals("0"))))
			{
				if (!(readingVector.isEmpty()))
				{
					if (StringUtil.isValid(date) && StringUtil.isValid(mrCode))
					{
						try
						{
							cursor = databaseManager.search(entity, new Clause(new QueryItem(
									CoreConstants.FIELD_MRO_NO, mroNo, SearchCondition.CONTAINS), null, null));
							
							MobiculeLogger.verbose("MrO NuMbEr     " + mroNo + "   cursor.getCount()   "+ cursor.getCount());
							
							if (cursor.getCount() > 0)
							{
								cursor.moveToFirst();
								while (!cursor.isAfterLast())
								{
									MobiculeLogger.verbose("Cursor Id" + cursor.getInt(cursor.getColumnIndex("_id")) + "");
									FileUtilDate.writeToFile("****DefaultMeterReadingPersistenceService : In saveMeterReading() : Cursor Id :---->"+ cursor.getInt(cursor.getColumnIndex("_id")));
									databaseManager.update(entity, data, cursor.getInt(cursor.getColumnIndex("_id")));
									cursor.moveToNext();
								}
							}
							else
							{
								databaseManager.add(entity, data);
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
						return true;
					}
				}
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
			MobiculeLogger.verbose("Meter persistance", "close cursor");
			cursor.close();
			cursor = null;
		}
	}

	@Override
	public Vector<String> getSavedBuildingList()
	{
		Vector data = new Vector();
		Vector meterReadingData = null;
		Vector connObjList = new Vector();
		Cursor cursor = null;
		try
		{
			//check for saved meter readings and not the submitted ones.
			Clause meterReadingClause = new Clause(new QueryItem(KEY_MR_SUBMIT, "0", SearchCondition.EQUALS), null,
					null);

			cursor = databaseManager.search(CoreConstants.TABLE_SAVED_METER_READING, meterReadingClause);
			MobiculeLogger.verbose("*/**/*/SEARCH RESULT*/*/*/*/ " + cursor.getCount());
			if (cursor.getCount() > 0)
			{
				meterReadingData = new Vector();
				//meterReadingData = getRecordStore(meterReadingData, cursor);
				meterReadingData = getRecordStoreOnlyConnObj(meterReadingData, cursor);
				MobiculeLogger.verbose("ONE READING - "+ meterReadingData.elementAt(0).toString());
				close(cursor);
				for (int i = 0; i < meterReadingData.size(); i++)
				{
					jsonParser.setJson(meterReadingData.elementAt(i).toString());
					String connObj = jsonParser.getValue(Constants.FIELD_CONN_OBJ);
					//close(cursor);
					if (!connObjList.contains(connObj))
					{
						connObjList.add(connObj);
						Clause connClause = new Clause(new QueryItem(Constants.FIELD_CONN_OBJ, connObj,
								SearchCondition.EQUALS), Operator.AND, meterReadingClause);
						//following will give the no. of customers
						cursor = databaseManager.search(CoreConstants.TABLE_SAVED_METER_READING, connClause);
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

	private Vector<String> getRecordStore(Vector<String> data, Cursor cursor)
	{
		while (cursor.moveToNext())
		{
			data.add(cursor.getString(cursor.getColumnIndex("data")));
		}
		//close(cursor);
		return data;
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
	public Vector fetchSavedMeterReading(String entity, String mroNO)
	{
		Vector savedMeterReading = new Vector();
		Clause clause = new Clause(new QueryItem(CoreConstants.FIELD_MRO_NO, mroNO, SearchCondition.EQUALS), null, null);
		Cursor cursor = null;
		try
		{
			cursor = databaseManager.search(entity, clause);

			if (cursor.getCount() > 0)
			{
				while (cursor.moveToNext())
				{
					savedMeterReading.add(cursor.getString(cursor.getColumnIndex(DATA)));
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
		return savedMeterReading;
	}

	@Override
	public Vector getSavedCustomerList(String connObj)
	{
		Vector data = new Vector();
		Cursor cursor = null;
		try
		{
			Clause meterReadingClause = new Clause(new QueryItem(KEY_MR_SUBMIT, "0", SearchCondition.EQUALS), null,
					null);
			Clause connClause = new Clause(new QueryItem(Constants.FIELD_CONN_OBJ, connObj, SearchCondition.EQUALS),
					Operator.AND, meterReadingClause);
			//following will give the no. of customers
			cursor = databaseManager.search(CoreConstants.TABLE_SAVED_METER_READING, connClause);
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
	public Vector<String> fetchAllSavedMeterReadings(String entity)
	{
		Vector<String> savedMeterReading = new Vector<String>();
		//check for saved meter readings and not the submitted ones.
		Clause meterReadingClause = new Clause(new QueryItem(KEY_MR_SUBMIT, "0", SearchCondition.EQUALS), null, null);
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
							savedMeterReading.add(cursor.getString(cursor.getColumnIndex(DATA)));
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
		//Log.d("fetch all saved metereaing data", savedMeterReading.toString());
		return savedMeterReading;
	}

	public Vector<String> fetchAllSavedMeterReadingsOnlyMroNums(String entity)
	{
		Vector<String> savedMeterReading = new Vector<String>();
		//check for saved meter readings and not the submitted ones.
		Clause meterReadingClause = new Clause(new QueryItem(KEY_MR_SUBMIT, "0", SearchCondition.EQUALS), null, null);
		Cursor cursor = null;
		try
		{
			MobiculeLogger.verbose("INSIDE  fetchAllSavedMeterReadingsOnlyMroNums() ------- ");
			if (getTableLists().contains(entity))
			{
				cursor = databaseManager.search(entity, meterReadingClause);

				MobiculeLogger.info("cursorFetchedData:",cursor.toString());

				if (cursor != null)
				{
					if (cursor.getCount() > 0)
					{
						while (cursor.moveToNext())
						{

							int index = cursor.getColumnIndex(DATA);
							if (index == -1) {
								// Column doesn't exist
								MobiculeLogger.info("Column doesn't exist");
							} else {
								MobiculeLogger.info("Column exist: ", cursor.getString(index));
								jsonParser.setJson(cursor.getString(index));
								String mroNumber = jsonParser.getValue(Constants.KEY_MRO_NUMBER);
								MobiculeLogger.info("mroNumber",mroNumber);
								JSONObject jsonObj = new JSONObject();
								jsonObj.put(Constants.KEY_MRO_NUMBER, mroNumber);
                                savedMeterReading.add(jsonObj.toString());
																																																																																																																																																										}
						}
					} else {
						MobiculeLogger.info("cursorcountiszero");
					}
				} else {
					MobiculeLogger.info("cursorisnull");
				}/////////////
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
		//Log.d("fetch all saved metereaing data", savedMeterReading.toString());
		return savedMeterReading;
	}

	//-----------------------------------

	@Override
	public boolean deleteMeterReading(String entity, String data)
	{
		Vector readingVector = null;
		try
		{
			jsonParser.setJson(data);
			readingVector = new Vector();
			//	Log.e("savemeterreadingJson", data);

			//String bpNo = jsonParser.getValue(Constants.KEY_BP_NUMBER);
			mroNo = jsonParser.getValue(CoreConstants.FIELD_MRO_NO);

			MobiculeLogger.verbose("DefaultMeterReading delete - mroNo : " + mroNo + " readingVector : " + readingVector
					+ " jsonParser value: " + data);

			FileUtilDate.writeToFile("**********DefaultMeterReadingPersistance :In deleteMeterReading()----> : MRO No"+mroNo);

			FileUtilDate.writeToFile("**********DefaultMeterReadingPersistance :In deleteMeterReading()----> : Entity"+entity);

			readingVector = jsonParser.getArray(CoreConstants.FEY_READING);
			for (int i = 0; i < readingVector.size(); i++)
			{
				readingElement = readingVector.elementAt(i).toString();
				jsonParser.setJson(readingElement);
				date = jsonParser.getValue(CoreConstants.FIELD_READING_DATE);
				mrCode = jsonParser.getValue(CoreConstants.FIELD_MR_CODE);
			}

			Cursor cursor = null;

			MobiculeLogger.verbose("MRO No" + mroNo + " data" + data);

			if (StringUtil.isValid(mroNo) && (!(mroNo.equals("0"))))
			{
				if (!(readingVector.isEmpty()))
				{
					if (StringUtil.isValid(date) && StringUtil.isValid(mrCode))
					{
						try
						{
							cursor = databaseManager.search(entity, new Clause(new QueryItem(
									CoreConstants.FIELD_MRO_NO, mroNo, SearchCondition.CONTAINS), null, null));

							MobiculeLogger.verbose("MrO NuMbEr     " + mroNo + "   cursor.getCount()   "+ cursor.getCount());

							if (cursor.getCount() > 0)
							{
								cursor.moveToFirst();
								while (!cursor.isAfterLast())
								{
									MobiculeLogger.verbose("Cursor Id" + cursor.getInt(cursor.getColumnIndex("_id")) + "");
									FileUtilDate.writeToFile("****DefaultMeterReadingPersistenceService : In deleteMeterReading() : Cursor Id :---->"+ cursor.getInt(cursor.getColumnIndex("_id")));
									databaseManager.delete(entity, cursor.getInt(cursor.getColumnIndex("_id")));
									cursor.moveToNext();
								}
							}
							else
							{
								databaseManager.add(entity, data);
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
						return true;
					}
				}
			}

		}
		catch (Exception e1)
		{
			e1.printStackTrace();
			return false;
		}
		return false;
	}



	//------------------------------------









}
