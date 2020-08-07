/**
 * *****************************************************************************
 * C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
 * <p>
 * Copyright © 2011-2012 Mobicule Technologies Pvt. Ltd. All rights reserved.
 * This is proprietary information of Mobicule Technologies Pvt. Ltd.and is
 * subject to applicable licensing agreements. Unauthorized reproduction,
 * transmission or distribution of this file and its contents is a
 * violation of applicable laws.
 * *****************************************************************************
 *
 * @project MahanagarGasLimitedNew
 */
package com.mobicule.android.msales.mgl.meterreading.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.mybookwalk.view.MyBookWalk;
import com.mobicule.android.msales.mgl.onmplaning.view.OnMPlanningActivity;
import com.mobicule.android.msales.mgl.search.view.SearchCustomerSummaryActivity;
import com.mobicule.android.msales.mgl.util.Base64;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.android.msales.mgl.util.FileUtil;
import com.mobicule.component.string.StringUtil;
import com.mobicule.component.util.LogFile;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.meterreading.DefaultMeterReading;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IReadings;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.me.JSONArray;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * <enter description here>
 *
 * @author nikita
 * @see
 *
 * @createdOn 20-Feb-2012
 * @modifiedOn
 *
 * @copyright © 2010-2011 Mobicule Technologies Pvt. Ltd. All rights reserved.
 */
public class Summary extends DefaultMeterReadingActivity implements OnClickListener {

    private TextView bpnumber, meterNumber, customerName, contactNumber, address, meterReadingText, meterReading,
            imageText, unableReasonText, unableReason, caNoTextView, mroNumber, custLandLineNum, custOfficeNumber,
            imageText3, imageText2, textViewMsgFromMr, textViewLPG, textViewRelocation, currentLpg, currentRelocation,
            textviewPrimaryMessage, currentPrimaryMessage, textViewremarks, currentremarks;

    private TextView textViewMruCode;

    private Response response;

    private ImageView imageView;

    private ImageView imageView2;

    private ImageView imageView3;

    private byte[] bitmapdata;

    private byte[] bitmapdata2;

    private byte[] bitmapdata3;

    private boolean menuOptionEnabled;

    private Button submitButton, cancelButton;

    private TextView textViewMsgFromMrField;

    private String activityFromIntent;

    private LinearLayout imagesetlayout;

    @Override
    protected void onStart() {
        super.onStart();
        Constants.broadcastDialogContext = this;
    }

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

        setContentView(R.layout.summary);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        try {

            initalizedData();

            activityFromIntent = getIntent().getStringExtra("activity");
            MobiculeLogger.verbose("activityFromIntent :: " + activityFromIntent);

            MobiculeLogger.verbose("meterReadingBO : Summary :: " + meterReadingBO.toJSON().toString());
            // FileUtil.writeToFilePrintDB(meterReadingBO.toJSON().toString());
            FileUtil.writeToFile("//SummayFile : meterReadingBO :: " + meterReadingBO.toJSON().toString());
            if (meterReadingBO != null || (meterReadingBO.getBpNumber() != null ||
                    meterReadingBO.getBpNumber().trim().equalsIgnoreCase(""))) {

         /*   final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setTitle("Name");
            // set the custom layout
            final View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_layout, null);

            TextView BONoTxt = customLayout.findViewById(R.id.BONoTxt);
            TextView DateTimetxt = customLayout.findViewById(R.id.DateTime);
            TextView version = customLayout.findViewById(R.id.version);
            TextView response = customLayout.findViewById(R.id.response);
                Log.e("aaaa", "aaaaa");
                BONoTxt.setText("BP Number:" + meterReadingBO.getBpNumber().toString());
                DateTimeFormatter dtf = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    DateTimetxt.setText("Date&Time " + dtf.format(now));
                }
                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String versionName = pInfo.versionName;
                    version.setText("Version :" + versionName);

                } catch (PackageManager.NameNotFoundException e) {

                    e.printStackTrace();

                }
                response.setText(meterReadingBO.toJSON().toString());
                builder.setView(customLayout);
                // add a button
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // send data from the AlertDialog to the Activity
                        dialog.dismiss();

                    }
                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
*/

                //test
                //  meterReadingBO.setBpNumber("");


//                        if (activityFromIntent.equals("CustomerSummary")) {
//                            Intent intent = new Intent(Summary.this, CustomerInformation.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                            startActivity(intent);
//                            finish();
//                        }
                        /*} else if (activityFromIntent.equals("SearchCustomerSummaryActivity")) {
                            Intent intent = new Intent(Summary.this, SearchCustomerSummaryActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                            finish();
                        }*/
//            } else if (meterReadingBO == null) {
////test
//            }
            bpnumber.setText(meterReadingBO.getBpNumber().toString());

            textViewMruCode.setText(meterReadingBO.getMruCode().toString());

            mroNumber.setText(meterReadingBO.getMroNumber().toString());

            meterNumber.setText(meterReadingBO.getMeterNumber().toString());

            customerName.setText(meterReadingBO.getCustomerName().toString());

            address.setText(meterReadingBO.getCustomerAddress().toString());
                if(MeterReadingFeild.mrCode.equalsIgnoreCase("00")){
                    unableReasonText.setVisibility(View.VISIBLE);
                    unableReason.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    imageText.setVisibility(View.GONE);

                    unableReason.setText(meterReadingBO.getMrReason().toString());
                }
            if (meterReadingBO.getMsgFromMr() == "Other(Write)") {
                textViewMsgFromMr.setText(meterReadingBO.getMsgRemarks());
            } else if (meterReadingBO.getMsgFromMr() == "Select") {
                textViewMsgFromMr.setVisibility(View.GONE);
                textViewMsgFromMrField.setVisibility(View.GONE);
            } else {
                textViewMsgFromMr.setText(meterReadingBO.getMsgFromMr());
            }if(meterReadingBO.getMruCode()=="00"){
              //  cc test
                }

            /*if (getIntent().getStringExtra("type").equalsIgnoreCase("plausible")||getIntent().getStringExtra("type").equalsIgnoreCase("implausible"))*/
            if (Constants.KEY_METER_READING_TYPE.equalsIgnoreCase(Constants.KEY_PLAUSIBLE)) {
                textviewPrimaryMessage.setVisibility(View.GONE);
                currentPrimaryMessage.setVisibility(View.GONE);

                MobiculeLogger.verbose("Plausible vlaue" + Constants.KEY_PLAUSIBLE);

            } else if (Constants.KEY_METER_READING_TYPE.equalsIgnoreCase(Constants.KEY_IMPLAUSIBLE)) {
                textviewPrimaryMessage.setVisibility(View.VISIBLE);
                currentPrimaryMessage.setVisibility(View.VISIBLE);
                textviewPrimaryMessage.setText(meterReadingBO.getMsgRemarksPrimary());
            } else if (Constants.KEY_METER_READING_TYPE.equalsIgnoreCase(Constants.KEY_INCOMPLETE)) {
                textviewPrimaryMessage.setVisibility(View.GONE);
                currentPrimaryMessage.setVisibility(View.GONE);
            } else {
                textviewPrimaryMessage.setVisibility(View.VISIBLE);
                currentPrimaryMessage.setVisibility(View.VISIBLE);
                textviewPrimaryMessage.setText(meterReadingBO.getMsgRemarksPrimary());
            }

                /*if(meterReadingBO.getIsRelocation().equalsIgnoreCase("YES"))
                {
                    textViewRelocation.setText(meterReadingBO.getIsRelocation());
                }
                else if(meterReadingBO.getIsRelocation().equalsIgnoreCase("NO"))
                        {
                    textViewRelocation.setText(meterReadingBO.getIsRelocation());
                        }*/

            MobiculeLogger.verbose("Plausible vlaue" + Constants.KEY_PLAUSIBLE);

            submitButton = (Button) findViewById(R.id.submit_button);

            submitButton.setOnClickListener(this);

            cancelButton = (Button) findViewById(R.id.cancel_button);

            cancelButton.setOnClickListener(this);

            String intentValue = getIntent().getExtras().getString("TAG");

            if (getIntent().getStringExtra("TAG").equalsIgnoreCase("SavedMeterReading")) {

                submitButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);

            } else {

                submitButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);

            }

            if (StringUtil.isValid(meterReadingBO.getCustomerContactNo())) {
                contactNumber.setText(meterReadingBO.getCustomerContactNo().toString());
            }

            if (StringUtil.isValid(meterReadingBO.getCustLandNo())) {
                custLandLineNum.setText(meterReadingBO.getCustLandNo().toString());
            }

            if (StringUtil.isValid(meterReadingBO.getCustOfficeNo())) {
                custOfficeNumber.setText(meterReadingBO.getCustOfficeNo().toString());
            }

            if (StringUtil.isValid(meterReadingBO.getIsRelocation())) {
                textViewRelocation.setText(meterReadingBO.getIsRelocation().toString());
            }
            MobiculeLogger.verbose("Summary - LPGSupplier :: " + meterReadingBO.getLPGSupplier().toString());
            if (StringUtil.isValid(meterReadingBO.getLPGSupplier())) {
                currentLpg.setVisibility(View.VISIBLE);
                textViewLPG.setVisibility(View.VISIBLE);
                textViewLPG.setText(meterReadingBO.getLPGSupplier().toString());
            } else {
                currentLpg.setVisibility(View.GONE);
                textViewLPG.setVisibility(View.GONE);
            }

            if (StringUtil.isValid(meterReadingBO.getMsgRemarksPrimary())) {
                textviewPrimaryMessage.setText(meterReadingBO.getMsgRemarksPrimary().toString());
            }
            if (StringUtil.isValid(meterReadingBO.getMsgRemarks())) {
                textViewremarks.setText(meterReadingBO.getMsgRemarks().toString());
            }
                /*if (MeterReadingFeild.mrCode.equals("14") || MeterReadingFeild.mrCode.equals("13"))
                {
                    currentRelocation.setVisibility(View.VISIBLE);
                    textViewRelocation.setVisibility(View.VISIBLE);
                }
                else
                {
                    currentRelocation.setVisibility(View.GONE);
                    textViewRelocation.setVisibility(View.GONE);
                }*/

            if (MeterReadingFeild.mrCode.toString().equals("99")) {
                MobiculeLogger.verbose("Meter Normal 99 LPG - in if");
                    /*String reading1 = meterReadingBO.getReEnterMeterReading();
                    System.out.print("Reading 1" + reading1);*/

                    /*if (!meterReadingBO.getLPGSupplier().equals(""))
                    {
                        currentLpg.setVisibility(View.VISIBLE);
                        textViewLPG.setVisibility(View.VISIBLE);

                    }
                    else
                    {
                        currentLpg.setVisibility(View.GONE);
                        textViewLPG.setVisibility(View.GONE);
                        textViewremarks.setVisibility(View.GONE);
                        currentremarks.setVisibility(View.GONE);
                    }
                    */
                if (/*!meterReadingBO.getMsgRemarks().equals("") ||*/meterReadingBO.getMsgRemarks().trim().length() != 0) {
                    textViewremarks.setVisibility(View.VISIBLE);
                    currentremarks.setVisibility(View.VISIBLE);
                } else {
                    textViewremarks.setVisibility(View.GONE);
                    currentremarks.setVisibility(View.GONE);
                }

            }

            if (/*!meterReadingBO.getMsgRemarks().equals("") ||*/meterReadingBO.getMsgRemarks().trim().length() != 0) {
                textViewremarks.setVisibility(View.VISIBLE);
                currentremarks.setVisibility(View.VISIBLE);
            } else {
                textViewremarks.setVisibility(View.GONE);
                currentremarks.setVisibility(View.GONE);
            }

            MobiculeLogger.verbose("Summary - MeterReadingFeild.mrCode : " + MeterReadingFeild.mrCode);

                /*if(MeterReadingFeild.mrCode.equals("52") || MeterReadingFeild.mrCode.equals("1"))
                {
                    currentLpg.setVisibility(View.VISIBLE);
                    textViewLPG.setVisibility(View.VISIBLE);
                }
                else
                {
                    currentLpg.setVisibility(View.GONE);
                    textViewLPG.setVisibility(View.GONE);
                }*/

            menuOptionEnabled = true;
            if (getIntent().hasExtra("menu")) {
                menuOptionEnabled = getIntent().getExtras().getBoolean("menu");
            }
            //MobiculeLogger.verbose("Reason Code ********" + meterReadingBO.getMrReason().toString() + "*******");
            caNoTextView.setText(meterReadingBO.getCaNo());

            String mrReasonCode = meterReadingBO.getMrReason().toString();
            MobiculeLogger.verbose("SummaryActivity - mrReasonCode :: " + mrReasonCode);

            try {
                String imageStr2 = "";

                String imageStr3 = "";

                JSONArray iamgearrayget = new JSONArray();

                if ((mrReasonCode.equalsIgnoreCase("Not Using Gas"))
                        || (mrReasonCode.equalsIgnoreCase("Temporary Disconnection"))

                        || (mrReasonCode.equalsIgnoreCase("Premises Locked"))
                        || (mrReasonCode.equalsIgnoreCase("Customer Shifted"))
                        || (mrReasonCode.equalsIgnoreCase("Customer Not Staying"))
                        || (mrReasonCode.equalsIgnoreCase("NO PNG connection"))
                        || (mrReasonCode.equalsIgnoreCase("Meter Fitted At Awkward Place/Level"))
                        || (mrReasonCode.equalsIgnoreCase("Meter Inaccessible"))
                        || (mrReasonCode.equalsIgnoreCase("Room Under Repair/Renovation"))
                        || (mrReasonCode.equalsIgnoreCase("Customer Refuse To Meter Reading"))
                        || ((MeterReadingFeild.mrCode.equals("99")) && (meterReadingFacade
                        .isCommercialCustomer(meterReadingBO)))
                        || ((MeterReadingFeild.mrCode.equals("63")) && (meterReadingFacade
                        .isCommercialCustomer(meterReadingBO)))) {

                    imageStr3 = meterReadingBO.getImage3();


                    if (StringUtil.isValid(imageStr3)) {
                        bitmapdata3 = Base64.decode(imageStr3);

                        MobiculeLogger.verbose("Image 3 " + (bitmapdata3 != null));


                        if (bitmapdata3 != null && bitmapdata3.length > 0) {
                            imageText3.setVisibility(View.VISIBLE);
                            imageView3.setVisibility(View.VISIBLE);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata3, 0, bitmapdata3.length);
                            imageView3.setImageBitmap(bitmap);
                        } else {
                            meterReadingBO.setImage3("");
                            imageText3.setVisibility(View.GONE);
                            imageView3.setVisibility(View.GONE);

                        }
                    }

                    imageStr2 = meterReadingBO.getImage2();

                    if (StringUtil.isValid(imageStr2)) {
                        bitmapdata2 = Base64.decode(imageStr2);

                        MobiculeLogger.verbose("Image 2 " + (bitmapdata2 != null));

                        if (bitmapdata2 != null && bitmapdata2.length > 0) {
                            imageText2.setVisibility(View.VISIBLE);
                            imageView2.setVisibility(View.VISIBLE);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata2, 0, bitmapdata2.length);
                            imageView2.setImageBitmap(bitmap);
                        } else {
                            meterReadingBO.setImage2("");
                            imageText2.setVisibility(View.GONE);
                            imageView2.setVisibility(View.GONE);
                        }
                    }
                }

                if (/*(mrReasonCode.equalsIgnoreCase("Not Using Gas"))
                            ||*/(mrReasonCode.equalsIgnoreCase("Meter/Counter Defective"))
                        /*	|| *//*(mrReasonCode.equalsIgnoreCase("Temporary Disconnection"))*/
                        || (mrReasonCode.equalsIgnoreCase("Meter Normal") && !(meterReadingFacade.isCommercialCustomer(meterReadingBO)))
                        || (mrReasonCode.equalsIgnoreCase("Meter Testing Not Possible"))
                        || (mrReasonCode.equalsIgnoreCase("Meter Glass Smoky/Opaque"))
                        || (mrReasonCode.equalsIgnoreCase("Permission Refused By Customer"))
                        || (mrReasonCode.equalsIgnoreCase("Permission Refused By Secretary"))
                        || (mrReasonCode.equalsIgnoreCase("Permission Refused By Security"))) {
                    imageStr2 = meterReadingBO.getImage2();

                    if (StringUtil.isValid(imageStr2)) {
                        bitmapdata2 = Base64.decode(imageStr2);

                        if (bitmapdata2 != null && bitmapdata2.length > 0) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata2, 0, bitmapdata2.length);
                            imageView2.setImageBitmap(bitmap);

                            imageText2.setVisibility(View.VISIBLE);
                            imageView2.setVisibility(View.VISIBLE);

                            meterReadingBO.setImage3("");
                            imageText3.setVisibility(View.GONE);
                            imageView3.setVisibility(View.GONE);

                            MobiculeLogger.verbose("Image 2 " + (bitmapdata2 != null) + "imageView2.getVisibility()" + imageView2.getVisibility());
                        } else {
                            meterReadingBO.setImage2("");
                            imageText2.setVisibility(View.GONE);
                            imageView2.setVisibility(View.GONE);
                        }
                    }
                }
                    /*else
                    {
                        imageText2.setVisibility(View.GONE);
                        imageView2.setVisibility(View.GONE);

                        imageText3.setVisibility(View.GONE);
                        imageView3.setVisibility(View.GONE);
                    }*/

                ///////

                String imageStr = meterReadingBO.getImage();

                if (StringUtil.isValid(imageStr)) {
                    bitmapdata = Base64.decode(imageStr);

                    MobiculeLogger.verbose("Image 1 " + (bitmapdata != null));

                    if (bitmapdata != null && bitmapdata.length > 0) {
                        imageText.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                        imageView.setImageBitmap(bitmap);
                    } else {
                        meterReadingBO.setImage("");
                        imageText.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                    }
                } else {
                    meterReadingBO.setImage("");
                    meterReadingBO.setImage2("");
                    meterReadingBO.setImage3("");
                    imageText.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    imageText2.setVisibility(View.GONE);
                    imageView2.setVisibility(View.GONE);
                    imageText3.setVisibility(View.GONE);
                    imageView3.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                meterReadingBO.setImage("");
                e.printStackTrace();
            }
            String reading = MeterReadingFeild.meterReading;
            //String reading1 = meterReadingBO.getReEnterMeterReading();//changed
            meterReadingText.setVisibility(View.GONE);
            meterReading.setVisibility(View.GONE);
            unableReasonText.setVisibility(View.GONE);
            unableReason.setVisibility(View.GONE);

            if (!StringUtil.isValid(reading)) {
                unableReasonText.setVisibility(View.VISIBLE);
                unableReason.setVisibility(View.VISIBLE);
                unableReason.setText(meterReadingBO.getMrReason().toString());
            } else {
                meterReadingText.setVisibility(View.VISIBLE);
                meterReading.setVisibility(View.VISIBLE);
                meterReading.setText(reading);
            }

            if (Constants.KEY_METER_READING_TYPE.equalsIgnoreCase(Constants.KEY_RE_ENTER_METER_READING)) {
                meterReading.setText(meterReadingBO.getReEnterMeterReading());
                MeterReadingFeild.meterReading = meterReading.getText().toString();
            } else {
                meterReading.setText(reading);
            }
            ////////////
            new LocationSensor(Summary.this, meterReadingBO);

            MobiculeLogger.verbose("METER READING BO before submitting" + meterReadingBO.toJSON().toString());

        }
                    else if(meterReadingBO == null ){

            }
        } catch (Exception e) {
            e.printStackTrace();
            FileUtil.writeToFile("//Summay : Exception :: " + e);

        }
    }

    public void initalizedData() {
        meterReadingText = (TextView) findViewById(R.id.current_meterreading_TextView);
        meterReading = (TextView) findViewById(R.id.current_meterreading);
        imageText = (TextView) findViewById(R.id.meterimage_TextView);
        imageText2 = (TextView) findViewById(R.id.meterimage2_TextView);
        imageText3 = (TextView) findViewById(R.id.meterimage3_TextView);
        unableReasonText = (TextView) findViewById(R.id.unable_reason_text);
        unableReason = (TextView) findViewById(R.id.unable_reason);
        imageView = (ImageView) findViewById(R.id.imageview);

        imageView2 = (ImageView) findViewById(R.id.imageview2);
        imageView3 = (ImageView) findViewById(R.id.imageview3);
        bpnumber = (TextView) findViewById(R.id.bpnumsummary_Text);
        mroNumber = (TextView) findViewById(R.id.mronumsummary_Text);
        meterNumber = (TextView) findViewById(R.id.custMeterSummary_Text);
        customerName = (TextView) findViewById(R.id.custNameSummary_Text);
        contactNumber = (TextView) findViewById(R.id.custContactNum_Text);
        address = (TextView) findViewById(R.id.custAddress_Text);
        caNoTextView = (TextView) findViewById(R.id.custcanumber);
        custLandLineNum = (TextView) findViewById(R.id.custlandlinenum_Text);
        custOfficeNumber = (TextView) findViewById(R.id.custofficenum_Text);
        textViewMruCode = (TextView) findViewById(R.id.mruCodeSummary_Text);
        textViewMsgFromMr = (TextView) findViewById(R.id.summary_TextViewMsgFromMr_value);
        textViewMsgFromMrField = (TextView) findViewById(R.id.summary_TextViewMsgFromMr_field);
        textViewLPG = (TextView) findViewById(R.id.LPGSupplier);
        textViewRelocation = (TextView) findViewById(R.id.Relocation);
        currentLpg = (TextView) findViewById(R.id.current_LPGSupplier);
        currentRelocation = (TextView) findViewById(R.id.current_Relocation);
        textviewPrimaryMessage = (TextView) findViewById(R.id.PrimaryMsg_Text);
        currentPrimaryMessage = (TextView) findViewById(R.id.PrimaryMsg_TextView);
        textViewremarks = (TextView) findViewById(R.id.remarks);
        currentremarks = (TextView) findViewById(R.id.current_remarks);


    }

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if (menuOptionEnabled)
		{
			menu.add(Constants.SUBMIT);

			if ((meterReadingFacade.isCommercialCustomer(meterReadingBO)) && (MeterReadingFeild.mrCode.equals("99")))
			{
				menu.add(Constants.INSPECTION_DETAILS);
			}
		}
		return super.onCreateOptionsMenu(menu);
	}*/

    private void commonConfimrationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Summary.this);
        builder.setCancelable(true);

        builder.setMessage("I hereby certify that Meter reading and Master data update obtained for correct BP."
                + "\r\n"
                + this.getString(R.string.i_hereby_certify_that_meter_reading_and_master_data_update_obtained_for_correct_bp));
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submit();
                MobiculeLogger.verbose(" else Summary - onm_functionality : ");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void submit() {
        new ApplicationAsk(Summary.this, new ApplicationService() {
            @Override
            public void postExecute() {
                if (response.isSuccess()) {

                    Constants.isThreePicsSelected = false;
                    Constants.threePicsCapCnt = 0;

                    AlertDialog.Builder saveDialog = new AlertDialog.Builder(Summary.this);
                    saveDialog.setCancelable(false);
                    saveDialog.setTitle("Submit Meter Reading");
                    saveDialog.setMessage(response.getMessage());
                    saveDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();


                            if (((MeterReadingFeild.mrCode.equals("99")) && (!meterReadingFacade.isCommercialCustomer(meterReadingBO))) ||
                                    (MeterReadingFeild.mrCode.equals("52"))
                                    || (MeterReadingFeild.mrCode.equals("18"))
                                    || (MeterReadingFeild.mrCode.equals("1"))
                                    || (MeterReadingFeild.mrCode.equals("69"))
                                    || (MeterReadingFeild.mrCode.equals("63")) && (!meterReadingFacade.isCommercialCustomer(meterReadingBO))) {
                                MobiculeLogger.verbose("meter reading field value is...if not commercial " + meterReadingFacade.isCommercialCustomer(meterReadingBO));

                                MobiculeLogger.verbose("meter reading field value is...if " + MeterReadingFeild.mrCode);
                                MobiculeLogger.verbose("meter reading meterReadingBO...if not commercial ", meterReadingBO.toJSON().toString());
                                //dialogForOnMPlanning();
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.setAction("com.mobicule.ACTION_MY_BOOK_WALK");
                                sendBroadcast(broadcastIntent);
                                Intent intent = new Intent(Summary.this, OnMPlanningActivity.class);
                                startActivity(intent);
                            } else {
                                MobiculeLogger.verbose("meter reading field value is...else if not commercial " + meterReadingFacade.isCommercialCustomer(meterReadingBO));
                                MobiculeLogger.verbose("meter reading field value is...else " + MeterReadingFeild.mrCode);
                                meterReadingFacade.resetMeterReadingInstanceBO();
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.setAction("com.mobicule.ACTION_MY_BOOK_WALK");
                                sendBroadcast(broadcastIntent);
                                MobiculeLogger.verbose("Summary  is :::: flag is " + Constants.searchSelection);
                                if (Constants.searchSelection == true) {
                                    startActivity(new Intent(Summary.this, MyBookWalk.class));
                                } else {
                                    startActivity(new Intent(Summary.this, CustomerInformation.class));
                                }
                            }

                        }
                    });
                    saveDialog.show();
                } else {
                    Toast.makeText(Summary.this, "Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void execute() {
                IReadings oneMeterReading = new DefaultMeterReading.DefaultReadings();
                String realTime = "";
                String realdate = "";

                String real_time = getTime();

                if (!real_time.toString().isEmpty() || !real_time.toString().equalsIgnoreCase("")) {

                    String str[] = real_time.split(" ");

                    if (str.length == 2) {
                        oneMeterReading.setReadingTime(str[1]);
                        realTime = str[1];
                        oneMeterReading.setDate(str[0]);
                        realdate = str[0];
                    } else {

                        realTime = MeterReadingFeild.readingTime;
                        realdate = MeterReadingFeild.date;
                    }

                } else {

                    realTime = MeterReadingFeild.readingTime;
                    realdate = MeterReadingFeild.date;
                }

                oneMeterReading.setReadingTime(realTime);
                oneMeterReading.setDate(realdate);
                oneMeterReading.setMrCode(MeterReadingFeild.mrCode);
                oneMeterReading.setMeterReading(MeterReadingFeild.meterReading);
                meterReadingBO.setReadings(oneMeterReading);

                int noOfAttempts = Integer.parseInt(meterReadingBO.getNoOfAttempts()) + 1;
                meterReadingBO.setNoOfAttempts(String.valueOf(noOfAttempts));

                MobiculeLogger.verbose("/////////In Summary : submit : METER READING BO -> ", meterReadingBO.toJSON()
                        .toString());

                response = meterReadingFacade.saveMeterReading(false);

                MobiculeLogger.verbose("Summary submit() execute() - save meter reading response : " + response);

                //meterReadingFacade.resetMeterReadingInstanceBO();// tushar // ----------------- changed by me

                MobiculeLogger.verbose("////////In Summary : submit : METER READING BO after reset -> ", meterReadingBO
                        .toJSON().toString());

            }
        }).execute();
    }

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getTitle().equals(Constants.SUBMIT))
		{
			if (MeterReadingFeild.mrCode.equals("99"))
			{
				commonConfimrationDialog();
			}
			else
			{
				submit();
			}

		}
		if (item.getTitle().equals(Constants.INSPECTION_DETAILS))
		{
			JSONObject inJsonObject = meterReadingBO.getInspection();
			IInspection inspection = new DefaultMeterReading.DefaultInspection(inJsonObject);
			InspectionDetails inspectionDetailsDialog = new InspectionDetails(this, inspection);
			inspectionDetailsDialog.showDialog();
		}
		return super.onOptionsItemSelected(item);
	}*/

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_button) {
            MobiculeLogger.verbose("Summary - //MeterReadingFeild.mrCode : " + MeterReadingFeild.mrCode);
            if (MeterReadingFeild.mrCode.equals("99")) {
                commonConfimrationDialog();
                MobiculeLogger.verbose(" if  Summary - onm_functionality : ");
                //dialogForOnMPlanning();
            } else {
                submit();
                MobiculeLogger.verbose(" else Summary - onm_functionality : ");

            }
        } else if (v.getId() == R.id.cancel_button) {
            if (activityFromIntent.equals("CustomerSummary")) {
                Intent intent = new Intent(Summary.this, CustomerSummary.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            } else if (activityFromIntent.equals("SearchCustomerSummaryActivity")) {
                Intent intent = new Intent(Summary.this, SearchCustomerSummaryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (getIntent().getStringExtra("TAG").equalsIgnoreCase("SavedMeterReading")) {
            finish();
        }
    }

    private void dialogForOnMPlanning() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Summary.this);
        builder.setCancelable(true);
        builder.setTitle("O&M Planning");
        builder.setMessage("Do you want to continue to O&M Planning reading?");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.mobicule.ACTION_MY_BOOK_WALK");
                sendBroadcast(broadcastIntent);
                Intent intent = new Intent(Summary.this, OnMPlanningActivity.class);
                intent.putExtra("meterReadingBO", meterReadingBO.toJSON().toString());
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                meterReadingFacade.resetMeterReadingInstanceBO();
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.mobicule.ACTION_MY_BOOK_WALK");
                sendBroadcast(broadcastIntent);
                dialog.dismiss();
                MobiculeLogger.verbose("Summary  is :::: flag is   " + Constants.searchSelection);
                if (Constants.searchSelection == true) {
                    startActivity(new Intent(Summary.this, MyBookWalk.class));
                } else {
                    startActivity(new Intent(Summary.this, CustomerInformation.class));
                }
                submit();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private String getTime() {

        String output = "";


        try {
            DateFormat utcFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet("http://google.com/"));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                String dateStr = response.getFirstHeader("Date").getValue();
                //Here I do something with the Date String
                System.out.println(dateStr);
                MobiculeLogger.info("time and date :: " + dateStr);


                //convert gmt to ist

                Date date = new Date(dateStr);

                String currentDay = utcFormat.format(date);


                utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                DateFormat indianFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                utcFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
                Date timestamp = utcFormat.parse(currentDay);
                output = indianFormat.format(timestamp);

                MobiculeLogger.info("GMT TO IST :: " + output);
                //
				/*DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				Date date = sdf.parse("2013-01-09T19:32:49.103+05:30");
				sdf.setTimeZone(TimeZone.getTimeZone("IST"));
				System.out.println(sdf.format(date));*/


            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            Log.d("Response", e.getMessage());
            MobiculeLogger.info("get time response :::" + e.getMessage());
        } catch (IOException e) {
            Log.d("Response", e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return output;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

}
