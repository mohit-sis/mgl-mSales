package com.mobicule.component.util;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;



public class LogFile
{

public static void writeToFile(String data) {
	 boolean isOff = false;
	if (isOff == true ) {

		String fpath = "/sdcard/";

		File syncDir = new File(fpath + "/MR_LOGS");
		File logDirectory = new File(syncDir + "/log");

		if (!syncDir.exists()) {
			syncDir.mkdir();
		}
		if (!logDirectory.exists()) {
			logDirectory.mkdir();
		}
		/*FileWriter  fw;
		try
		{
		fw = new FileWriter(logDirectory.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(data);
		bw.close();

		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
*/

		try {
			FileWriter f;

			String timestamp = new SimpleDateFormat("dd-MMM-yyyy")
					.format(new Date());// date wise
			String dateNTime = new SimpleDateFormat("dd-MMM-yyyy_hh:mm:ss").format(new Date());


			File logFile = new File(logDirectory, timestamp + ".txt");

			f = new FileWriter(logFile, true);

			f.write(dateNTime + data + "\n");
			f.flush();
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	}

public static PrintStream exceptionToFile()
{
	
	String path = "/sdcard/";
	File syncDir = new File(path + "/MR_LOGS");
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
		File file = new File(logDirectory,timestamp + ".txt");
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
