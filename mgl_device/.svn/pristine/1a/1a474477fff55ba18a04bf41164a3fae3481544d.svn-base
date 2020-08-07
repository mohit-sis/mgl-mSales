package com.mobicule.component.string;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.mobicule.component.util.CoreConstants;
import com.mobicule.component.util.CoreMessages;
import com.mobicule.component.util.CoreMobiculeLogger;
import com.mobicule.msales.mgl.client.common.Response;

public class StringUtil
{
	public static Vector<String> createVectorFromCommaSeparatedValues(String commaSeparatedValue)
	{
		CoreMobiculeLogger.log("....StringUtil: createVectorFromCommaSeparatedValues: commaSeparatedValue: "
				+ commaSeparatedValue);

		Vector<String> vector = new Vector<String>();

		String remainingString = commaSeparatedValue;
		int startIndex = 0;
		int endIndex = remainingString.indexOf(',');

		while (startIndex <= endIndex)
		{
			if (!(remainingString.substring(startIndex, endIndex).trim().equalsIgnoreCase(CoreConstants.EMPTY_STRING)))
			{
				vector.addElement(remainingString.substring(startIndex, endIndex).trim());
			}

			remainingString = remainingString.substring(endIndex + 1, remainingString.length()).trim();
			endIndex = remainingString.indexOf(',');
		}
		if (!remainingString.trim().equals(CoreConstants.EMPTY_STRING))
		{
			vector.addElement(remainingString.trim());
		}
		return vector;
	}

	public static Vector<String> getSubStringsSepratedByDashV(String data)
	{
		Vector<String> subStringsV = new Vector<String>();

		String remainingString = data;
		int startIndex = 0;
		int endIndex = remainingString.indexOf('-');

		while (startIndex < endIndex)
		{
			subStringsV.addElement(remainingString.substring(startIndex, endIndex));

			remainingString = remainingString.substring(endIndex + 1, remainingString.length());
			endIndex = remainingString.indexOf('-');
		}
		subStringsV.addElement(remainingString);
		return subStringsV;
	}

	public static String getDateString(long date)
	{
		String[] listOfMonth = new String[] { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT",
				"NOV", "DEC" };

		Calendar c = Calendar.getInstance();
		c.setTime(new Date(date));
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH) + 1;
		int d = c.get(Calendar.DATE);

		String monthStr = listOfMonth[m - 1];

		String dateStr = (d < 10 ? "0" : "") + d + "/" + monthStr + (y < 10 ? "0" : "") + "/" + y;
		return dateStr;
	}

	public static Response isValidDate(Date toDate, Date fromDate, int validation_Days)
	{
		Date today = new Date(System.currentTimeMillis());
		Date validation_Date = addDays(toDate, validation_Days);
		if (toDate.getTime() > today.getTime())
		{
			return new Response(false, CoreMessages.TO_DATE_GREATER_THAN_CURRENT_DATE, null);
		}
		else if (fromDate.getTime() > today.getTime())
		{
			return new Response(false, CoreMessages.FROM_DATE_GREATER_THAN_CURRENT_DATE, null);
		}
		else if (fromDate.getTime() > toDate.getTime())
		{
			return new Response(false, CoreMessages.FROM_DATE_GREATER_THAN_TO_DATE, null);
		}
		else if (fromDate.getTime() < validation_Date.getTime())
		{
			return new Response(false, CoreMessages.DIFFERENCE_BETWEEN_DATES, null);
		}
		return new Response(true, CoreConstants.EMPTY_STRING, null);
	}

	public static Date addDays(Date date, int days)
	{
		long MILLISECONDS_IN_SECOND = 1000l, SECONDS_IN_MINUTE = 60l, MINUTES_IN_HOUR = 60l, HOURS_IN_DAY = 24l;

		long MILLISECONDS_IN_DAY = MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY;
		return new Date(date.getTime() + (days * MILLISECONDS_IN_DAY));
	}

	public static Date getDateFromString(String date)
	{
		Calendar calendar = Calendar.getInstance();
		try
		{
			String[] listOfMonth = new String[] { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT",
					"NOV", "DEC" };

			Vector<String> vector = new Vector<String>();

			String remainingString = date;
			int startIndex = 0;
			int endIndex = remainingString.indexOf('/');

			while (startIndex < endIndex)
			{
				vector.addElement(remainingString.substring(startIndex, endIndex));

				remainingString = remainingString.substring(endIndex + 1, remainingString.length());
				endIndex = remainingString.indexOf('/');
			}
			vector.addElement(remainingString);

			calendar.set(Calendar.DATE, Integer.parseInt(vector.elementAt(0).toString()));
			calendar.set(Calendar.YEAR, Integer.parseInt(vector.elementAt(2).toString()));

			for (int i = 0; i < listOfMonth.length; i++)
			{
				if (listOfMonth[i].equals(vector.elementAt(1).toString()))
				{
					calendar.set(Calendar.MONTH, i);
				}
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		return calendar.getTime();
	}

	public static boolean isValid(String str)
	{
		return (str != null) && !str.equals(CoreConstants.EMPTY_STRING);
	}

	public static String getMessage(String dataStr)
	{
		int endIndex = dataStr.indexOf(':');
		String message = dataStr;

		if (endIndex != -1)
		{
			message = dataStr.substring(endIndex + 1, dataStr.length());
		}
		return message;
	}

	public static float Round(double number)
	{
		number = number * 100;
		float tmp = (long) Math.floor(number + 0.5d);
		return tmp / 100;
	}

	public static String replaceAll(String src, String token) throws Throwable
	{
		StringBuffer data = new StringBuffer();
		src = src.trim();

		Vector<String> v = new Vector<String>();
		if (src.indexOf(token) == -1)
		{
			return src;
		}
		int index = src.indexOf(token);
		while (index != -1)
		{
			v.addElement(src.substring(0, index).trim());

			src = src.substring(index + token.length());
			index = src.indexOf(token);
		}
		v.addElement(src);
		for (int i = 0; i < v.size(); i++)
			data.append(v.elementAt(i));
		return data.toString();
	}

	public static String replaceStr(String src, String token, String replaceStr) throws Throwable
	{
		StringBuffer data = new StringBuffer();
		src = src.trim();

		Vector<String> v = new Vector<String>();
		if (src.indexOf(token) == -1)
		{
			return src;
		}
		int index = src.indexOf(token);
		while (index != -1)
		{

			v.addElement(src.substring(0, index).trim() + replaceStr.trim());

			src = src.substring(index + token.length());
			index = src.indexOf(token);
		}
		v.addElement(src);
		for (int i = 0; i < v.size(); i++)
			data.append(v.elementAt(i));
		return data.toString();
	}
	
	public static boolean isValidBpNo(String bpNo)
	{
		if(bpNo != null && !bpNo.equals(CoreConstants.EMPTY_STRING))
		{
			if(bpNo.matches("^[0-9][0-9]{9}"))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isValidCaNo(String caNo)
	{
		if(caNo != null && !caNo.equals(CoreConstants.EMPTY_STRING))
		{
			if(caNo.matches("^[0-9][0-9]{11}"))
			{
				return true;
			}
		}
		
		return false;
	}
}
