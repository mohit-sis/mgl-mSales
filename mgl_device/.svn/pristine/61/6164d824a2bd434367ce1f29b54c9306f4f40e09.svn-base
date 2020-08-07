package com.mobicule.android.msales.mgl.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.mobicule.android.component.logging.MobiculeLogger;

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
public class Utilities
{

	public static long calculateTimeDifference(Map<String, String> deviceDateTimeMap)
	{
		long timeDifference = 0;

		Timestamp dateTimeClientCombination = generateTimestamp(Constants.DATE_TIME_FORMAT_CLIENT,
				((deviceDateTimeMap.get("date")) + " " + (deviceDateTimeMap.get("time"))));

		long millisecondsClient = (dateTimeClientCombination.getTime())
				+ ((dateTimeClientCombination.getNanos()) / 1000000);

		Timestamp dateTimeServerCombination = generateTimestamp(Constants.DATE_TIME_FORMAT_CLIENT);

		long millisecondsServer = (dateTimeServerCombination.getTime())
				+ ((dateTimeServerCombination.getNanos()) / 1000000);

		timeDifference = millisecondsClient - millisecondsServer;

		if (timeDifference < 0)
		{
			timeDifference = timeDifference * (-1);
		}

		return timeDifference;
	}

	public static Timestamp generateTimestamp(String format, String dateString)
	{
		Timestamp timestamp = null;

		try
		{
			Date date = getDate(format, dateString);

			timestamp = new Timestamp(date.getTime());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return timestamp;
	}

	public static Date getDate(String format, String dateString) throws ParseException
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);

		Date date = dateFormat.parse(dateString);
		MobiculeLogger.verbose("date"+ date.toGMTString());
		return date;
	}

	public static Timestamp generateTimestamp(String format)
	{
		Timestamp timestamp = null;

		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);

			Date date = dateFormat.parse(generateDate(format));

			timestamp = new Timestamp(date.getTime());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return timestamp;
	}

	public static String generateDate(String format)
	{
		Date date = Calendar.getInstance().getTime();

		DateFormat dateFormat = new SimpleDateFormat(format);

		return (dateFormat.format(date));
	}

	public static String formatMilliseconds(long timeDifference)
	{
		String timeDifferenceFormatted = "";

		long days;
		long hours;
		long minutes;

		minutes = timeDifference / 60000;

		if (minutes > 59)
		{
			hours = minutes / 60;
			minutes = minutes % 60;

			if (hours > 23)
			{
				days = hours / 24;
				hours = hours % 24;

				timeDifferenceFormatted = (String.valueOf(days)) + " Days, " + (String.valueOf(hours)) + " Hours, "
						+ (String.valueOf(minutes)) + " Minutes";
			}
			else
			{
				timeDifferenceFormatted = (String.valueOf(hours)) + " Hours, " + (String.valueOf(minutes)) + " Minutes";
			}
		}
		else
		{
			timeDifferenceFormatted = (String.valueOf(minutes)) + " Minutes";
		}

		return timeDifferenceFormatted;
	}

	public static long getDateinMillis(String dateStr)
	{
		Calendar c = Calendar.getInstance();
		int year = Integer.parseInt(dateStr.substring(0, 4));
		int month = Integer.parseInt(dateStr.substring(5, 7)) - 1;
		int day = Integer.parseInt(dateStr.substring(8, 10));
		int hour = Integer.parseInt(dateStr.substring(11, 13));
		int minutes = Integer.parseInt(dateStr.substring(14, 16));
		int seconds = Integer.parseInt(dateStr.substring(17, 19));
		c.set(year, month, day, hour, minutes, seconds);
		long dateInMillis = c.getTimeInMillis();
		return dateInMillis;
	}

	public static String getDifference(Date startTime, Date endTime)
	{
		String timeDiff;
		if (startTime == null)
			return "[corrupted]";
		Calendar startDateTime = Calendar.getInstance();
		startDateTime.setTime(startTime);
		Calendar endDateTime = Calendar.getInstance();
		endDateTime.setTime(endTime);
		long milliseconds1 = startDateTime.getTimeInMillis();
		long milliseconds2 = endDateTime.getTimeInMillis();
		long diff = milliseconds2 - milliseconds1;
		long hours = diff / (60 * 60 * 1000);
		long minutes = diff / (60 * 1000);
		minutes = minutes - 60 * hours;
		long seconds = diff / (1000);

		timeDiff = hours + ":" + minutes;
		return timeDiff;
	}

}
