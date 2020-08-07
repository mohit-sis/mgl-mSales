package com.mobicule.android.component.db;

import java.util.HashMap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbConnectionManager
{

	private int DATABASE_VERSION = 1;

	private String _dbName;

	private String _dbPassword;

	private static HashMap<String, DataBaseOpenHelper> hashmap = new HashMap<String, DataBaseOpenHelper>();

	int dbOpenCloseCount;

	private static DbConnectionManager instance;

	private SQLiteDatabase database;

	private Context context;

	private DataBaseOpenHelper dbHelper;

	public DbConnectionManager(Context context, String dbName)
	{

		this.context = context;

		this._dbName = dbName;
	}

	public synchronized static DbConnectionManager getInstance(Context context, String dbName)
	{

		if (instance == null)
		{
			instance = new DbConnectionManager(context, dbName);
		}
		return instance;
	}

	public void setDbConfiguration(String dbName, String dbPassword)
	{
		this._dbName = dbName;
		this._dbPassword = dbPassword;
		dbOpenCloseCount = 0;
	}

	public SQLiteDatabase openDatabase()
	{
		if (dbOpenCloseCount == 0)
		{
			dbHelper = new DataBaseOpenHelper(context, _dbName, DATABASE_VERSION);
			database = dbHelper.getWritableDatabase();

		}
		dbOpenCloseCount++;
		return database;
	}

	public void closeDatabase()
	{
		/*dbOpenCloseCount--;
		if (dbOpenCloseCount == 0)
		{
			database.close();
		}*/
		// 1.info("Database Close Count : ", "" + dbOpenCloseCount);
	}
}
