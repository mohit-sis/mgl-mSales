/**
 * 
 */
package com.mobicule.android.msales.mgl.commons.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.msales.mgl.login.view.Login;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.android.msales.mgl.util.FileUtil;
import com.mobicule.versioncontrol.VersionControl;

import java.io.File;

import static com.mobicule.android.msales.mgl.commons.model.NotifyService.TAG;

//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;


/**
 * 
 * <enter description here>
 * 
 * @author nikita
 * @see
 * 
 * @createdOn  Feb 14, 2012
 * @modifiedOn
 * 
 * @copyright � 2010-2011 Mobicule Technologies Pvt. Ltd. All rights reserved.
 */



public class SplashScreen extends Activity
{
	
	private Context context;
	private int REQUEST_CODE = 200;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{    
		super.onCreate(savedInstanceState);
		
	           // setContentView(R.layout.activity_main);
	           // init();
//Runtime Permission given by priyanka.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


//For CrashFile
		if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {


			File path = Environment.getExternalStorageDirectory();

			File syncDir = new File(path + "/MGL_LOGS");
			File logDirectory = new File(syncDir + "/log");

			if (!syncDir.exists()) {
				syncDir.mkdir();
			}
			if (!logDirectory.exists()) {
				logDirectory.mkdir();
			}

			Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(

					logDirectory.getPath(), "https://crash.ezycom.co.in/upload.php"));

		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
					&& checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission( Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
					&& checkSelfPermission(Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED
					&& checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
					&& checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED
					&& checkSelfPermission(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS) == PackageManager.PERMISSION_GRANTED) {

				setContentView(R.layout.splash);
				Constants.broadcastDialogContext = this;
				VersionControl.init(this);
				context = getApplicationContext();
				showSplash();

            }
            else
			{
				setPermission();
			}
		}
		else
		{
			setContentView(R.layout.splash);
			Constants.broadcastDialogContext = this;
			VersionControl.init(this);
			context = getApplicationContext();
			showSplash();
		}
		//FirebaseCrash.log("Activity created");
		//FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");
		//Throwable ex;
		//FirebaseCrash.report(ex);
	}


	private void showSplash()

	{

		Thread mSplashThread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					synchronized (this)
					{
						wait(3000);
					}
				}
				catch (InterruptedException ex)
				{

				}
				Intent intent = new Intent(SplashScreen.this, Login.class);
				startActivity(intent);

			}
		};

		mSplashThread.start();

	}

	@Override
	protected void onRestart()
	{
		finish();
		super.onRestart();
	}




	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			android.os.Process.killProcess(android.os.Process.myPid());
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_CODE) {
			switch (requestCode) {
				case 200:

					if (grantResults.length > 0) {
						if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
								if (checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                                        &&checkSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                        && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                        && checkSelfPermission( Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                                        && checkSelfPermission( Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                        && checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
										&& checkSelfPermission(Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED
										&& checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
										&& checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED
										&& checkSelfPermission(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS) == PackageManager.PERMISSION_GRANTED) {
                                    setContentView(R.layout.splash);
                                    showSplash();
                                    //init();
                                    try {
                                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //init();

                                } else {
                                    Toast.makeText(SplashScreen.this, "Please Grant All Permissions", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
							}
							else
							{
								setContentView(R.layout.splash);
								Constants.broadcastDialogContext = this;
								VersionControl.init(this);
								context = getApplicationContext();
								showSplash();
							}

						} else {
							Toast.makeText(SplashScreen.this, "Please Grant All Permissions", Toast.LENGTH_SHORT).show();
							finish();

						}
					}
			}
		}
	}



	@TargetApi(Build.VERSION_CODES.M)
	private void setPermission() {

		String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE",
				"android.permission.INTERNET","android.permission.CAMERA", "android.permission.READ_PHONE_STATE",
				"android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION",
				"android.permission.WAKE_LOCK","android.permission.ACCESS_NETWORK_STATE",
				"android.permission.RECEIVE_BOOT_COMPLETED","android.permission.ACCESS_LOCATION_EXTRA_COMMANDS"};

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(permissions, REQUEST_CODE);
		}
	}

}
