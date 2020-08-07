package com.mobicule.android.msales.mgl.util;

import android.os.Environment;
import android.util.Log;

import com.mobicule.android.component.logging.MobiculeLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {

	public static File logFile;

	public static void writeToFile(String data) {

		if (Constants.IS_FileLog == true) {

			File path = Environment.getExternalStorageDirectory();

			File syncDir = new File(path + "/MGL_LOGS");
			File logDirectory = new File(syncDir + "/log");

			if (!syncDir.exists()) {
				syncDir.mkdir();
			}
			if (!logDirectory.exists()) {
				logDirectory.mkdir();
			}
			try {
				FileWriter f;

				String timestamp = new SimpleDateFormat("dd-MMM-yyyy")
						.format(new Date());// date wise
				String dateNTime = new SimpleDateFormat("dd-MMM-yyyy_hh:mm:ss").format(new Date());

				// File logFile=new File(Environment.getExternalStorageDirectory() +
				// "/MGL_LOGS/Logs/" ,"logs" + timestamp + ".txt");
				 logFile = new File(logDirectory, "logs" + timestamp + ".txt");
				MobiculeLogger.verbose("FileUtil ", "FileUtil : logFile " + logFile.getAbsolutePath());
				if (!logFile.exists()) {
					MobiculeLogger.verbose("FileUtil ", "FileUtil : logFile not exists");
					// boolean fileCreated = logFile.mkdir();

					// MobiculeLogger.showVerboesLog("FileUtil ",
					// "FileUtil : fileCreated : "+fileCreated);


				}
				f = new FileWriter(logFile, true);

				f.write(dateNTime + data + "\n");
				f.flush();
				f.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {

		}
	}
	public static void writeToFilePrintDB(String data) {

		//if (Constants.IS_FileLog == true) {

			File path = Environment.getExternalStorageDirectory();

			File syncDir = new File(path + "/MGLS_LOGSDB");
			File logDirectory = new File(syncDir + "/logs");

			if (!syncDir.exists()) {
				syncDir.mkdir();
			}
			if (!logDirectory.exists()) {
				logDirectory.mkdir();
			}
			try {
				FileWriter f;

				String timestamp = new SimpleDateFormat("dd-MMM-yyyy")
						.format(new Date());// date wise
				String dateNTime = new SimpleDateFormat("dd-MMM-yyyy_hh:mm:ss").format(new Date());

				// File logFile=new File(Environment.getExternalStorageDirectory() +
				// "/MGL_LOGS/Logs/" ,"logs" + timestamp + ".txt");
				File logFile = new File(logDirectory, "logs" + timestamp + ".txt");
				MobiculeLogger.verbose("FileUtil ", "FileUtil : logFile " + logFile.getAbsolutePath());
				if (!logFile.exists()) {
					MobiculeLogger.verbose("FileUtil ", "FileUtil : logFile not exists");
					// boolean fileCreated = logFile.mkdir();

					// MobiculeLogger.showVerboesLog("FileUtil ",
					// "FileUtil : fileCreated : "+fileCreated);


				}
				Log.e("ResponseData",""+data);
				f = new FileWriter(logFile, true);

				f.write( data + "\n");
				//f.flush();
				f.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
//		}
//		else {
//
//		}
	}



	public static PrintStream exceptionToFile()
	{
		File path = Environment.getExternalStorageDirectory();

		File syncDir = new File(path + "/MGL_LOGS");
		File logDirectory = new File(syncDir + "/log");

		if (!syncDir.exists()) {
			syncDir.mkdir();
		}
		if (!logDirectory.exists()) {
			logDirectory.mkdir();
		}

		String timestamp = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());// date wise
		String dateNTime = new SimpleDateFormat("dd-MMM-yyyy_hh:mm:ss").format(new Date());
		FileOutputStream fos = null;
		PrintStream ps = null;
		try
		{
			File file = new File(logDirectory, "logs" + timestamp + ".txt");
			fos = new FileOutputStream(file, true);
			ps = new PrintStream(fos);			
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		/*catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		return ps;
		
	}
}
