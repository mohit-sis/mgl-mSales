package com.mobicule.msales.mgl.client.downloadbookwalk.state;

public class SyncEnum
{

	public static final int BOOKWALK_SEQUENCE = 1;

	public static String getName(int constant)
	{
		return getDisplayName(constant).toUpperCase().replace(' ', '_');
	}

	public static String getDisplayName(int constant)
	{
		switch (constant)
		{
			case BOOKWALK_SEQUENCE:
				return "bookwalk";
			default:
				return "None";
		}
	}
}
