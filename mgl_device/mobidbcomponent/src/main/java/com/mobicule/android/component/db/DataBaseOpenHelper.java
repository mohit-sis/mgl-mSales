package com.mobicule.android.component.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseOpenHelper extends SQLiteOpenHelper
{
	Context context;

	public DataBaseOpenHelper(Context context, String dataBaseName, int version)
	{
		super(context, dataBaseName, null, version);
		this.context = context;

	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		onCreate(db);
	}

}
