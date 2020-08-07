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
* @project mSales_GGCL_Core
*/
package com.mobicule.android.component.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

/**
* 
* <enter description here>
*
* @author sneha <enter lastname>
* @see 
*
* @createdOn 17-Apr-2015
* @modifiedOn
*
* @copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class FileUtil
{
	public static void writeToFile(String data)
	{
		File path = Environment.getExternalStorageDirectory();

		File syncDir = new File(path + "/MGL_LOGS");

		if (!syncDir.exists())
		{
			syncDir.mkdir();
		}

		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");

			String date = sdf.format(new Date());

			FileWriter f;
			f = new FileWriter(Environment.getExternalStorageDirectory() + "/MGL_LOGS/logs" + ".txt", true);
			f.write(date + " - " + data + "\n");
			f.flush();
			f.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static PrintStream exceptionToFile()
	{
		File path = Environment.getExternalStorageDirectory();

		File syncDir = new File(path + "/MGL_LOGS");

		if (!syncDir.exists())
		{
			syncDir.mkdir();
		}

		FileOutputStream fos = null;
		PrintStream ps = null;
		try
		{
			fos = new FileOutputStream(
					new File(Environment.getExternalStorageDirectory() + "/MGL_LOGS/exception.txt"), true);

			ps = new PrintStream(fos);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return ps;
	}

	/*public static void logsToFile()
	{
		File path = Environment.getExternalStorageDirectory();

		File syncDir = new File(path + "/GGCL_LOGS");

		StringBuilder log = new StringBuilder();

		if (!syncDir.exists())
		{
			syncDir.mkdir();			
		}

		try
		{
			File ftemp = new File(syncDir, "temp.txt");
						
			try
			{
				//Runtime.getRuntime().exec(new String[] { "logcat", "-f", ftemp.getAbsolutePath(), "ggcl.com:V", "*:S" });
				
				Runtime.getRuntime().exec("logcat -f"+ftemp.getAbsolutePath());

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			try
			{
				FileInputStream fileInputStream = new FileInputStream(ftemp.getAbsolutePath());

				DataInputStream dataInputStream = new DataInputStream(fileInputStream);

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));

				String fileLineRead;

				while ((fileLineRead = (bufferedReader.readLine())) != null)
				{
					log.append(fileLineRead);
				}

				dataInputStream.close();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			FileWriter f;
			f = new FileWriter(Environment.getExternalStorageDirectory() + "/GGCL_LOGS/LogsTracing" + ".txt", true);
			f.write(log.toString() + "\n");
			f.flush();
			f.close();
			
			Runtime.getRuntime().exec("logcat -c");

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}*/
}
