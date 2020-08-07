/**
 *
 */
package com.mobicule.android.msales.mgl.login.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.commons.model.IOCContainer;
import com.mobicule.android.msales.mgl.commons.model.SessionData;
import com.mobicule.android.msales.mgl.commons.view.DialogDisplay;
import com.mobicule.android.msales.mgl.menu.view.MainMenu;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.android.msales.mgl.util.FileUtil;
import com.mobicule.component.json.parser.IJSONParser;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.component.string.StringUtil;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.crashnotifier.IGenericExceptionHandler;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginFacade;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginPersistenceService;
import com.mobicule.versioncontrol.VersionControl;

import org.json.me.JSONObject;

import java.io.File;

/**
 * <enter description here>
 *
 * @author nikita
 * @createdOn Mar 1, 2012
 * @modifiedOn
 * @copyright � 2010-2011 Mobicule Technologies Pvt. Ltd. All rights reserved.
 * @see
 */

public class Login extends Activity implements OnClickListener {
    public EditText userName;

    public EditText passWord;

    protected Response response;

    protected Context context;

    public String imeinumber, responseStatus, responseURL;

    public ILoginFacade iLoginFacade;

    private TelephonyManager mTelephonyMgr;

    private IJSONParser jsonParser;

    private SharedPreferences delayPreference;

    private String imeiSharedPrefNumber = "";

    private VersionControl versionControl;

    private Button diagnosticsButton, loginButton;
    IGenericExceptionHandler handler;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
//For CrashFile
        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {


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


        Constants.broadcastDialogContext = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        context = this;

        userName = (EditText) findViewById(R.id.login_editText);
        passWord = (EditText) findViewById(R.id.password_editText);
        diagnosticsButton = (Button) findViewById(R.id.diagnostics_button);
        loginButton = (Button) findViewById(R.id.login_button);


        //	handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());

        diagnosticsButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
       /* try {

            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei;

            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imei =  telephonyManager.getImei();
                } else {
                    imei = telephonyManager.getDeviceId();
                }

                Log.e("IMEI","IMEI==="+imei);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
      /*  String imei;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (PermissionChecker.checkSelfPermission(Login.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei =  telephonyManager.getImei();
            } else {
                imei = telephonyManager.getDeviceId();
            }

            Log.e("IMEI","IMEI==="+imei);        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    ) {

                requestPermissions(new String[] {Manifest.permission.READ_PHONE_STATE}, 101);
            }else {
                mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            }

        } else {
            mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        }
        //imeinumber = "864089040289116";
        // imeinumber = "357872109510041"; // hardcoded uat sd01 user
        //imeinumber = "357872109271966"; //Live nc2 user
        //imeinumber ="353350106108816";
        //imeinumber ="353350106108826";
        //meinumber = "357490081431340";
     //  imeinumber = "865115043085919";//123test


        // uncomment for live and uat
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               imeinumber = mTelephonyMgr.getImei();
                Log.e("imeinumber", "" + imeinumber);

            } else {
                imeinumber = mTelephonyMgr.getDeviceId();
                Log.e("imeinumber111", "" + imeinumber);

            }

          /*  if (null == imeinumber || 0 == imeinumber.length()) {
                imeinumber = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.e("imeinumber111222", "" + imeinumber);

            }*/
        }catch (Exception e){
            e.printStackTrace();
        }
        // uncomment for live and uat
        MobiculeLogger.verbose("Login : imei number - " + imeinumber);

        jsonParser = JSONParser.getInstance();

        // VersionControl.init(context);
        //VersionControl.getInstance();

        iLoginFacade = (ILoginFacade) IOCContainer.getInstance(this).getBean("loginFacade");

        delayPreference = this.getSharedPreferences(Constants.MSALES_MGL_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        Constants.TIME_VARIATION_SUBMISSION_DELAY = delayPreference.getString(Constants.timeVariableKey, "0");
        Constants.BACKGROUND_ACTIVITY_DELAY = delayPreference.getString(Constants.bgActivityKey, "0");

        MobiculeLogger.verbose("BACKGROUND_ACTIVITY_DELAY - " + Constants.BACKGROUND_ACTIVITY_DELAY);
        MobiculeLogger.verbose("TIME_VARIATION_SUBMISSION_DELAY - " + Constants.TIME_VARIATION_SUBMISSION_DELAY);

        Constants.LOGIN_URL = Constants.FIRST_URL;
        Response offlineDetail = iLoginFacade.isOffineLoginMode();
        MobiculeLogger.verbose("Login - Offline details  " + offlineDetail);

        if (offlineDetail.isSuccess()) {
            if (!(offlineDetail.getData() instanceof JSONObject)) {
                return;
            }
            SessionData.userInfo = offlineDetail.getData().toString();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        userName.setText("");
        passWord.setText("");
        // System.runFinalizersOnExit(true);
        // System.exit(0);
    }

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add("Login");
		menu.add("Diagnostics");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getTitle().equals("Login"))
		{

			loginExecute();

		}
		else if (item.getTitle().equals("Diagnostics"))
		{

			DialogDisplay.diagnosticDialog(context);

		}
		return super.onOptionsItemSelected(item);
	}*/

    public void loginExecute() {
        new ApplicationAsk(Login.this, new ApplicationService() {
            @Override
            public void postExecute() {
                try {

                    if (response.isSuccess() && response.getData() != null) {
                        setUserData();

                        Intent intent = new Intent(Login.this, MainMenu.class);

                        if (delayPreference.contains("imei")) {
                            MobiculeLogger.verbose("In ifelayPreference.contains - " + imeiSharedPrefNumber);
                            imeiSharedPrefNumber = delayPreference.getString("imei", "0");
                            MobiculeLogger.verbose("In ifelayPreference.contains - " + imeiSharedPrefNumber);
                        }

                        startActivity(intent);
                        checkImei(intent);
                    } else if (response.getData() != null && response.isSuccess() == false) {

                        JSONObject jsonResponseObject = new JSONObject(response.getData().toString());

                        JSONObject jsonObject = new JSONObject(jsonResponseObject.getJSONObject(
                                CoreConstants.USER_RESPONSE_DATA).toString());

                        MobiculeLogger.verbose("DATA - " + response.getData().toString());

                        responseStatus = jsonResponseObject.getString(CoreConstants.USER_RESPONSE_STATUS);
                        if (responseStatus.equalsIgnoreCase("CHANGE")) {

                            responseURL = jsonObject.getString(VersionControl.KEY_BUILD_URL);

                            VersionControl.downloadNewBuild(context, responseURL, false);
                        }

                        if (responseStatus.equalsIgnoreCase("ERROR")) {
                            Toast.makeText(Login.this, response.getMessage(), Toast.LENGTH_LONG).show();
                            MobiculeLogger.verbose("Login - ", response.getMessage());
                        }

                    } else {
                        Toast.makeText(Login.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                        MobiculeLogger.verbose("Login - ", response.getMessage());
                    }
                } catch (Exception e) {
                    FileUtil.writeToFile("//Login : Exception :: " + e);

                    //handler.setIsLogEnable(true);
                    handler.logCrashReport(e);
                    e.printStackTrace();
                }
            }


            @Override
            public void execute() {
                try {
                    //imeinumber = "352306051462369";
                    //imeinumber = "353285060210507";
                    //imeinumber = "865622027840037";
                    //imeinumber = "352210086252003";
                    //imeinumber = "213456378913456";//successfully running 027 mgl
                    //imeinumber = "357138056611855";//last
                    //imeinumber = "357138056611855";
                    //imeinumber = "123456789111111";
                    //imeinumber = "357490082344369"; // J01 mgl - UAT
                    //imeinumber = "869071026292251";  //100 mgl - UAT


                    // Log.d("LOGIN EXECUTE  ", "Version Control JSON value :"
                    // + versionControlJSON.toString());

                    response = iLoginFacade.signIn(userName.getText().toString(), passWord.getText().toString(),
                            imeinumber);

                } catch (Exception ex) {
                    handler.logCrashReport(ex);
                    MobiculeLogger.verbose("" + ex.toString());
                }
            }
        }).execute();
    }

    private void checkImei(Intent intent) {
        if (imeiSharedPrefNumber.equalsIgnoreCase("0")) {
            delayPreference.edit().putString("imei", imeinumber).commit();
            MobiculeLogger.verbose("Login - In if - imeiSharedPrefNumber.equalsIgnoreCase)");
        } else {
            if (imeinumber.equals(imeiSharedPrefNumber)) {
                intent.putExtra("imeiKey", true);
            } else {
                intent.putExtra("imeiKey", false);
            }
        }
    }
    private void setUserData() {

        JSONObject responseJson = (JSONObject) response.getData();
        jsonParser.setJson(responseJson.toString());
        SessionData.userInfo = jsonParser.getValue(CoreConstants.KEY_DATA);
        if (!StringUtil.isValid(SessionData.userInfo))// offline login
        {
            SessionData.userInfo = response.getData().toString();
        } else
        // online login
        {
            String delay = jsonParser.getValue(ILoginPersistenceService.DELAY);
            if (StringUtil.isValid(delay)) {
                jsonParser.setJson(delay);
                Constants.BACKGROUND_ACTIVITY_DELAY = jsonParser.getValue(Constants.KEY_BACKGROUND_ACTIVITY_DELAY);
                Constants.TIME_VARIATION_SUBMISSION_DELAY = jsonParser
                        .getValue(Constants.KEY_TIME_VARIATION_SUBMISSION_DELAY);
                delayPreference.edit().putString(Constants.timeVariableKey, Constants.TIME_VARIATION_SUBMISSION_DELAY)
                        .commit();
                delayPreference.edit().putString(Constants.bgActivityKey, Constants.BACKGROUND_ACTIVITY_DELAY).commit();
                MobiculeLogger.verbose("BACKGROUND_ACTIVITY_DELAY - " + Constants.BACKGROUND_ACTIVITY_DELAY);
                MobiculeLogger.verbose("TIME_VARIATION_SUBMISSION_DELAY - " + Constants.TIME_VARIATION_SUBMISSION_DELAY);
            }
        }

        MobiculeLogger.verbose("Login  response.getdatais:::::::: - Response is:  " + response.getData().toString());
        MobiculeLogger.verbose("Login : SessionData.userInfo - " + SessionData.userInfo);
        Constants.LOGIN_PARAM = (ILoginPersistenceService.USER_DETAIL_USERNAME + "=" + userName.getText().toString()
                + "&" + ILoginPersistenceService.USER_DETAIL_PASSWORD + "=" + passWord.getText().toString()).trim()
                + "&" + ILoginPersistenceService.USER_DETAIL_IMEINUMBER + "=" + imeinumber;
        MobiculeLogger.verbose("Constants.LOGIN_PARAM - " + Constants.LOGIN_PARAM);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.diagnostics_button:
                DialogDisplay.diagnosticDialog(context);
                break;

            case R.id.login_button:
                loginExecute();
                break;

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera

            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question

                Toast.makeText(this, "Permission need to be granted for access camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
