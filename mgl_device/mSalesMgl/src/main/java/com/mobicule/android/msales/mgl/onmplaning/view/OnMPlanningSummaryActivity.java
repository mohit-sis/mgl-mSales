/**
 * *****************************************************************************
 * C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
 * <p>
 * Copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
 * This is proprietary information of Mobicule Technologies Pvt. Ltd.and is
 * subject to applicable licensing agreements. Unauthorized reproduction,
 * transmission or distribution of this file and its contents is a
 * violation of applicable laws.
 * *****************************************************************************
 *
 * @project mSalesMgl
 */
package com.mobicule.android.msales.mgl.onmplaning.view;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.commons.model.IOCContainer;
import com.mobicule.android.msales.mgl.meterreading.view.CustomerInformation;
import com.mobicule.android.msales.mgl.meterreading.view.MeterReadingFeild;
import com.mobicule.android.msales.mgl.meterreading.view.Summary;
import com.mobicule.android.msales.mgl.mybookwalk.view.MyBookWalk;
import com.mobicule.android.msales.mgl.onmplaning.model.DefaultOnMPlanningPersistanceService;
import com.mobicule.android.msales.mgl.util.Base64;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.android.msales.mgl.util.FileUtil;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.crashnotifier.GenericExceptionHandler;
import com.mobicule.crashnotifier.IGenericExceptionHandler;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.meterreading.DefaultMeterReading;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IReadings;
import com.mobicule.msales.mgl.client.onmPlanning.DefaultOnMPlanningFacade;

/**
 *
 * <enter description here>
 *
 * @author namrata <enter lastname>
 * @see
 *
 * @createdOn 20-Apr-2017
 * @modifiedOn
 *
 * @copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
 */
public class OnMPlanningSummaryActivity extends DefaultOnMPlanningActivity implements OnClickListener {

    JSONObject jsonObject;

    TextView tv_burners, tv_geysers, tv_bpNumber, tv_mroNumber, tv_remark, tv_message, tv_dateNTime, tv_msgFrmMRLabel,
            tv_photo1, tv_photo2;

    ImageView iv_photo1, iv_photo2;

    Bitmap image;

    String date_Time;

    private Button b_cancel, b_submit;

    private byte[] imageByte;

    String strDate;

    private String strHoseAvailable, currentDateTimeString;

    //private DefaultOnMPlanningFacade onMPlanningFacade;

    private Response response;

    Date myDate = null;

    String reportDate;

    private String strTime;
    IGenericExceptionHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        setContentView(R.layout.activity_onm_summary);

        initializecomponents();

        strHoseAvailable = getIntent().getStringExtra("strHoseAvailable");
        //Date cal = (Date) Calendar.getInstance().getTime();
        //date_Time = cal.toLocaleString();

		/*Date test = new Date();
		
		
		try
		{
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss") ;
			df.parse("test");
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        strDate = sdf.format(date);

        sdf = new SimpleDateFormat("hh:mm:ss");
        strTime = sdf.format(date);

        MobiculeLogger.verbose("", "// strDate : " + strDate);
        MobiculeLogger.verbose("", "// strTime : " + strTime);

        display();

        MobiculeLogger.verbose("// OnMSummaryActivity : onMPlanningBO : " + onMPlanningBO.toJSON().toString());

    }

    public void initializecomponents() {

        tv_bpNumber = (TextView) findViewById(R.id.tv_bpno2);
        tv_mroNumber = (TextView) findViewById(R.id.tv_code2);
        tv_remark = (TextView) findViewById(R.id.tv_remark2);
        tv_msgFrmMRLabel = (TextView) findViewById(R.id.tv_message1);
        tv_message = (TextView) findViewById(R.id.tv_message2);
        tv_dateNTime = (TextView) findViewById(R.id.tv_date2);
        //tv_readerNAgencyName = (TextView) findViewById(R.id.tv_name2);
        tv_burners = (TextView) findViewById(R.id.tv_burner2);
        tv_geysers = (TextView) findViewById(R.id.tv_geyser2);

        tv_photo1 = (TextView) findViewById(R.id.tv_photo1);
        tv_photo2 = (TextView) findViewById(R.id.tv_photo2);
        iv_photo1 = (ImageView) findViewById(R.id.iv_photo1);
        iv_photo2 = (ImageView) findViewById(R.id.iv_photo2);

        b_cancel = (Button) findViewById(R.id.btn_cancel);
        b_cancel.setOnClickListener(this);

        b_submit = (Button) findViewById(R.id.btn_submit);
        b_submit.setOnClickListener(this);

        //handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());

    }

    public void display() {
        try {

            tv_bpNumber.setText(onMPlanningBO.getBPNumber());

            tv_mroNumber.setText(onMPlanningBO.getMroNo());

            tv_burners.setText(onMPlanningBO.getNoOfBurners());

            tv_geysers.setText(onMPlanningBO.getNoOfGasGeysers());

            tv_dateNTime.setText(strDate + " " + strTime);

            tv_remark.setText(onMPlanningBO.getMsgRemarks());

            onMPlanningBO.setTime(strTime);

            onMPlanningBO.setDate(strDate);

            onMPlanningBO.setPlot("");

            if (strHoseAvailable.equalsIgnoreCase("Yes")) {
                FileUtil.writeToFile("//OnMPlanningSummaryActivity Yes Hose Available : : onMPlanningBO " + onMPlanningBO.toJSON().toString());

                tv_message.setVisibility(View.GONE);
                tv_msgFrmMRLabel.setVisibility(View.GONE);

                tv_photo1.setVisibility(View.VISIBLE);
                tv_photo2.setVisibility(View.VISIBLE);

                String image1 = onMPlanningBO.getImage();
                imageByte = Base64.decode(image1);

                Bitmap image1Bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                iv_photo1.setImageBitmap(image1Bitmap);

                FileUtil.writeToFile("//OnMPlanningSummaryActivity First Image Set: : onMPlanningBO " + onMPlanningBO.toJSON().toString());

                imageByte = Base64.decode(onMPlanningBO.getImage2());
                Bitmap image2Bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                iv_photo2.setImageBitmap(image2Bitmap);

                FileUtil.writeToFile("// OnMPlanningSummaryActivity Second Image Set : : onMPlanningBO " + onMPlanningBO.toJSON().toString());

            } else if (strHoseAvailable.equalsIgnoreCase("No")) {
                tv_message.setVisibility(View.VISIBLE);
                tv_msgFrmMRLabel.setVisibility(View.VISIBLE);
                tv_message.setText(onMPlanningBO.getMsgFrmMr());

                tv_photo1.setVisibility(View.GONE);
                tv_photo2.setVisibility(View.GONE);

                iv_photo1.setVisibility(View.GONE);
                iv_photo2.setVisibility(View.GONE);
            }

            tv_remark.setText(onMPlanningBO.getMsgRemarks());

            //tv_readerNAgencyName.setText("");

        } catch (IOException e) {
            //IGenericExceptionHandler handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
            //handler.setIsLogEnable(true);
            handler.logCrashReport(e);
            e.printStackTrace();
            e.printStackTrace(FileUtil.exceptionToFile());

        }

		/*String jsonValue = getIntent().getStringExtra("Summary");
		try {
			JSONObject profileJSON = new JSONObject(jsonValue);
			if (profileJSON != null) {
				tv_burners.setText(profileJSON.getString("BCount"));
				tv_geysers.setText(profileJSON.getString("Gcount"));
				
			}				

			
		} catch (JSONException e) {

			e.printStackTrace();
		}
		

		*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                finish();
                break;

            case R.id.btn_submit:

                try {
                    FileUtil.writeToFile("//OnMPlanningSummaryActivity Submit : : onMPlanningBO " + onMPlanningBO.toJSON().toString());

                    confirmationDialog("Gas geyser/Burners/PNG hose details are taken correctly and confirmed" + "\r\n"
                            + this.getString(R.string.gas_geysers_burners_png_details_taken_correctly_confirmed));
                } catch (Exception e) {
                    // TODO: handle exception
					/*IGenericExceptionHandler handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
					handler.setIsLogEnable(true);*/
                    handler.logCrashReport(e);
                    e.printStackTrace();
                    e.printStackTrace(FileUtil.exceptionToFile());
                }

                //submitDataToDB();
                break;

            default:
                break;
        }
    }

    private void confirmationDialog(String message) {
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(OnMPlanningSummaryActivity.this);
        saveDialog.setCancelable(false);
        saveDialog.setTitle("Submit Other Information");
        saveDialog.setMessage(message);
        saveDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitDataToDB();
            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        saveDialog.show();
    }

    private void submitDataToDB() {

        new ApplicationAsk(OnMPlanningSummaryActivity.this, new ApplicationService() {
            @Override
            public void postExecute() {
                if (response.isSuccess()) {

                    AlertDialog.Builder saveDialog = new AlertDialog.Builder(OnMPlanningSummaryActivity.this);
                    saveDialog.setCancelable(false);
                    saveDialog.setTitle("Submit Other Information");
                    saveDialog.setMessage("Other information submitted successfully");
                    saveDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            meterReadingFacade.resetMeterReadingInstanceBO(); // ---------- reset BO
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction("com.mobicule.ACTION_MY_BOOK_WALK");
                            sendBroadcast(broadcastIntent);
                            if (Constants.searchSelection == true) {
                                startActivity(new Intent(OnMPlanningSummaryActivity.this, MyBookWalk.class));
                            } else {
                                startActivity(new Intent(OnMPlanningSummaryActivity.this, CustomerInformation.class));
                            }
                        }

                    });
                    saveDialog.show();
                }
           else {
                    Toast.makeText(OnMPlanningSummaryActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void execute() {

                response = onmFacade.saveOnMPlanning(false);

                MobiculeLogger.verbose("Summary submit() execute() - save meter reading response : " + response);

                //meterReadingFacade.resetMeterReadingInstanceBO();// tushar // ----------------- changed by me

            }
        }).execute();

    }

}
