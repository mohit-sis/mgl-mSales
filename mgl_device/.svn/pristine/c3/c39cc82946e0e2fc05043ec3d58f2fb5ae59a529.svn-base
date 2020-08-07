package com.mobicule.component.util;

import java.util.Vector;

public class CoreMobiculeLogger
{
	public static boolean showlog = false;

	public static boolean showExceptionlog = false;

	private static Vector trackLogV = new Vector();

	private static Vector trackExceptionLogV = new Vector();

	public static Vector getTrackExceptionLogV()
	{
		return trackExceptionLogV;
	}

	public static Vector getTrackLogV()
	{
		return trackLogV;
	}

	public static void trackLogMsgs(Object message)
	{
		
	
		
		String logMsg = "";
		if (message != null)
		{
			logMsg = message.toString();
		}
		else
		{
			logMsg = (String) message;
		}
		log(logMsg);
		trackLogV.addElement(logMsg + "\n");
	}

	public static void trackExceptionLogV(Object message, Throwable t)
	{
		String logMsg = "";
		if (message != null)
		{
			logMsg = message.toString();
		}
		else
		{
			logMsg = (String) message;
		}
		log(logMsg, t);
		trackExceptionLogV.addElement(logMsg + "\t" + "Exception toString= " + t.toString() + "\t"
				+ "Exception message= " + t.getMessage() + "\n");
	}

	public static void clearTrackExceptionLogV()
	{
		if ((trackExceptionLogV != null) && (!trackExceptionLogV.isEmpty()))
		{
			trackExceptionLogV.removeAllElements();
		}
	}

	public static void clearTrackLogMsgs()
	{
		if ((trackLogV != null) && (!trackLogV.isEmpty()))
		{
			trackLogV.removeAllElements();
		}
	}

	public static void log(String message, Throwable t)
	{
		if (showExceptionlog)
		{
			/*log("||-");
			log(message);
			log("Exception toString= " + t.toString());
			log("Exception message= " + t.getMessage());
			log("-||");*/
		}
	}

	public static void log(Object message)
	{
		if (message != null)
		{
			log(message.toString());
		}
		else
		{
			log((String) message);
		}
	}

	public static void log(String message)
	{
		if (showlog)
		{
//			System.out.println(message);
		}
	}

	public static void showLogs(boolean show)
	{
		showlog = show;
	}

	public static void showExceptionLogs(boolean show)
	{
		showExceptionlog = show;
	}
}
