/**
 ******************************************************************************
 * C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
 * <p>
 * Copyright © 2011-2012 Mobicule Technologies Pvt. Ltd. All rights reserved.
 * This is proprietary information of Mobicule Technologies Pvt. Ltd.and is
 * subject to applicable licensing agreements. Unauthorized reproduction,
 * transmission or distribution of this file and its contents is a
 * violation of applicable laws.
 ******************************************************************************
 *
 * @project Mahanagar_Gas_Limited
 */
package com.mobicule.android.msales.mgl.meterreading.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.android.msales.mgl.util.CustomSpinnerAdapter;
import com.mobicule.component.string.StringUtil;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
public class MeterReading extends DefaultMeterReadingActivity implements OnClickListener
{
    // private Button summary;

    // private Button cancel;

    // private JSONObject jsonoCustomerDetails;

    private final String TAG = "MeterReading";

    String relocation;

    private EditText meterreadingEdittext;

    private Spinner spinner, spinnerMsg, groupSpinner;

    private String selectedItem, selectedMessageItem;

    private TextView meterReading_Remarks;

    private EditText meterReading_remarksvalue;

    private CheckBox final_attempt;

    private ArrayAdapter<String> adapter, adapterMsg;

    String remarksValue;

    private String selectedCustomerJson;

    private String concateArray[];

    private TextView meterReadingText;

    private JSONObject jsonoCustomerDetails;

    private String avgConsuption;

    public static int zeroConsumption = 0;

    ImageView yesrelocation,norelocation;
    Boolean isYesrelocation,isNorelocation;

    private String previousDate, activityFromIntent;

    private Date convertedPreviousDate;

    private MeterReaderMsg meterReaderMsg;

    private long currentTimeInMills, previousTimeInMills;

    private long numOfDays;

    private double avgCon, numDays;

    private String previousReading;

    private double xHigh, yHigh, yLow;

    private double previousReadingValue;

    private TextView meterReading_TextViewMsg;

    private int diffInDays;

    private Button cancelButton, nextButton;

    private RelativeLayout messageRelativeLayout;

    private LinearLayout groupInfoImageLayout;

    private LinearLayout codeInfoImageLayout,relocationRadiobuttonLayout;

    private LinearLayout messageInfoImageLayout;

    private LinearLayout ll_relocation;

    RadioButton rb_yes, rb_no;

    String[] groupSpinnerReasonAndCodeList;

    private View v_spaceView;


    private String intentVal="";
    private SharedPreferences sharedpreferences;

    @Override
    protected void onStart()
    {
        super.onStart();
        Constants.broadcastDialogContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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

        setContentView(R.layout.meter_reading);
        zeroConsumption = 0;

        MeterReadingFeild.init();
        meterreadingEdittext = (EditText) findViewById(R.id.meterreading_edittext);
        spinner = (Spinner) findViewById(R.id.unabletometerreadingSpinner);
        spinnerMsg = (Spinner) findViewById(R.id.meterReading_SpinnerMsg);

        groupSpinner = (Spinner) findViewById(R.id.groupOfMeterReadingsSpinner);

        meterReading_Remarks = (TextView) findViewById(R.id.meterReading_Remarks);
        meterReading_remarksvalue = (EditText) findViewById(R.id.meterReading_remarksvalue);
        meterReadingText = (TextView) findViewById(R.id.meterReading_TextView);
        meterReading_TextViewMsg = (TextView) findViewById(R.id.meterReading_TextViewMsg);

        yesrelocation=(ImageView)findViewById(R.id.imageyesrelocation);
        norelocation=(ImageView)findViewById(R.id.imagenorelocation);

        v_spaceView = (View) findViewById(R.id.v_spaceView);

        messageRelativeLayout = (RelativeLayout) findViewById(R.id.messageRelativeLayout);
        relocationRadiobuttonLayout=(LinearLayout)findViewById(R.id.ll_relocationRadiobuttonLayout);
        groupInfoImageLayout = (LinearLayout) findViewById(R.id.groupInfoImageLayout);
        codeInfoImageLayout = (LinearLayout) findViewById(R.id.codeInfoImageLayout);
        messageInfoImageLayout = (LinearLayout) findViewById(R.id.messageInfoImageLayout);
        ll_relocation = (LinearLayout) findViewById(R.id.ll_relocation);
		/*rb_yes = (RadioButton) findViewById(R.id.rb_yes);
		rb_no = (RadioButton) findViewById(R.id.rb_no);
		rb_yes.setOnClickListener(this);
		rb_no.setOnClickListener(this);*/
        yesrelocation.setOnClickListener(this);
        norelocation.setOnClickListener(this);
        isYesrelocation=false;
        isNorelocation=false;

        groupInfoImageLayout.setOnClickListener(this);
        codeInfoImageLayout.setOnClickListener(this);
        messageInfoImageLayout.setOnClickListener(this);


        meterReadingBO.setMsgRemarks("");
        Constants.isThreePicsSelected = false;
        Constants.threePicsCapCnt = 0;

        cancelButton = (Button) findViewById(R.id.cancel_button);
        nextButton = (Button) findViewById(R.id.next_button);

        cancelButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        int allReasonsListLength = MeterReadingNotesListsConstants.allReasonsList.length;

        concateArray = new String[allReasonsListLength];

        activityFromIntent = getIntent().getStringExtra("activity");
        Log.e("IntentVal",""+activityFromIntent);
        MobiculeLogger.verbose("activityFromIntent :: " + activityFromIntent);
        intentVal=activityFromIntent;
        sharedpreferences = getSharedPreferences("MGL", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("activity", intentVal);
        editor.commit();
        for (int i = 0; i < concateArray.length; i++)
        {
            concateArray[i] = MeterReadingNotesListsConstants.allReasonsList[i] + " "
                    + MeterReadingNotesListsConstants.allCodesList[i];

        }

        MobiculeLogger.verbose("allReasonsListLength : " + allReasonsListLength);
        MobiculeLogger.verbose("concateArray length : " + concateArray.length);

        // adapter = new ArrayAdapter<String>(this,
        // android.R.layout.simple_spinner_item, concateArray);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // List<String> meterCodeList = new
        // ArrayList<String>(Arrays.asList(concateArray));

		/*groupSpinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
				MeterReadingNotesListsConstants.groupList, 1));*/
        groupSpinnerReasonAndCodeList = getListOfMRCodes(MeterReadingNotesListsConstants.groupList,
                MeterReadingNotesListsConstants.groupCodeList);
        groupSpinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
                groupSpinnerReasonAndCodeList, 1));

		/*groupSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				String selectedGroupFromSpinner = groupSpinner.getSelectedItem().toString();

				Log.d("MeterReading - onItemSelected()",
						"selectedGroupFromSpinner" + selectedGroupFromSpinner.toString());

				if (selectedGroupFromSpinner.equals(MeterReadingNotesListsConstants.groupList[0]))
				{
					spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
							getListOfMRCodes(MeterReadingNotesListsConstants.meterNormalReasonsList,
									MeterReadingNotesListsConstants.meterNormalCodeList), 1));
				}
				else if (selectedGroupFromSpinner.equals(MeterReadingNotesListsConstants.groupList[1]))
				{
					spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
							getListOfMRCodes(MeterReadingNotesListsConstants.readingNotPossibleReasonsList,
									MeterReadingNotesListsConstants.readingNotPossibleCodeList), 1));
				}
				else if (selectedGroupFromSpinner.equals(MeterReadingNotesListsConstants.groupList[2]))
				{
					spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
							getListOfMRCodes(MeterReadingNotesListsConstants.premisesLockedReasonList,
									MeterReadingNotesListsConstants.premisesLockedCodeList), 1));
				}
				else if (selectedGroupFromSpinner.equals(MeterReadingNotesListsConstants.groupList[3]))
				{
					spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
							getListOfMRCodes(MeterReadingNotesListsConstants.permissionNotObtainedReasonList,
									MeterReadingNotesListsConstants.permissionNotObtainedCodeList), 1));
				}
				else if (selectedGroupFromSpinner.equals(MeterReadingNotesListsConstants.groupList[4]))
				{
					spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
							getListOfMRCodes(MeterReadingNotesListsConstants.buildingUnderConstructionReasonList,
									MeterReadingNotesListsConstants.buildingUnderConstructionCodeList), 1));
				}
				else if (selectedGroupFromSpinner.equals(MeterReadingNotesListsConstants.groupList[5]))
				{
					spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
							getListOfMRCodes(MeterReadingNotesListsConstants.buildingDemolishedReasonList,
									MeterReadingNotesListsConstants.buildingDemolishedCodeList), 1));
				}
				else if (selectedGroupFromSpinner.equals(MeterReadingNotesListsConstants.groupList[6]))
				{
					spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
							getListOfMRCodes(MeterReadingNotesListsConstants.meterAddnotFoundReasonList,
									MeterReadingNotesListsConstants.meterAddNotFoundCodeList), 1));
				}
				else if (selectedGroupFromSpinner.equals(MeterReadingNotesListsConstants.groupList[7]))
				{
					spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
							getListOfMRCodes(MeterReadingNotesListsConstants.letterToCustomerReasonList,
									MeterReadingNotesListsConstants.letterToCustomerCodeList), 1));
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}

		});*/

        groupSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedGroupFromSpinner = groupSpinner.getSelectedItem().toString();

                MobiculeLogger.verbose("selectedGroupFromSpinner" + selectedGroupFromSpinner.toString());
                relocation = "";

                if (selectedGroupFromSpinner.equals(groupSpinnerReasonAndCodeList[0]))
                {
                    spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
                            getListOfMRCodes(MeterReadingNotesListsConstants.meterAddnotFoundReasonList,
                                    MeterReadingNotesListsConstants.meterAddNotFoundCodeList), 1));
                }
                else if (selectedGroupFromSpinner.equals(groupSpinnerReasonAndCodeList[1]))
                {
                    spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
                            getListOfMRCodes(MeterReadingNotesListsConstants.buildingDemolishedReasonList,
                                    MeterReadingNotesListsConstants.buildingDemolishedCodeList), 1));
                }
                else if (selectedGroupFromSpinner.equals(groupSpinnerReasonAndCodeList[2]))
                {
                    spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
                            getListOfMRCodes(MeterReadingNotesListsConstants.buildingUnderConstructionReasonList,
                                    MeterReadingNotesListsConstants.buildingUnderConstructionCodeList), 1));
                }
                else if (selectedGroupFromSpinner.equals(groupSpinnerReasonAndCodeList[3]))
                {
                    spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
                            getListOfMRCodes(MeterReadingNotesListsConstants.permissionNotObtainedReasonList,
                                    MeterReadingNotesListsConstants.permissionNotObtainedCodeList), 1));
                }
                else if (selectedGroupFromSpinner.equals(groupSpinnerReasonAndCodeList[4]))
                {
                    spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
                            getListOfMRCodes(MeterReadingNotesListsConstants.premisesLockedReasonList,
                                    MeterReadingNotesListsConstants.premisesLockedCodeList), 1));
                }
                else if (selectedGroupFromSpinner.equals(groupSpinnerReasonAndCodeList[5]))
                {
                    spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
                            getListOfMRCodes(MeterReadingNotesListsConstants.readingNotPossibleReasonsList,
                                    MeterReadingNotesListsConstants.readingNotPossibleCodeList), 1));
                }
                else if (selectedGroupFromSpinner.equals(groupSpinnerReasonAndCodeList[6]))
                {
                    spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
                            getListOfMRCodes(MeterReadingNotesListsConstants.meterPossibleReasonList,
                                    MeterReadingNotesListsConstants.meterPossibleCodeList), 1));
                }
                else if (selectedGroupFromSpinner.equals(groupSpinnerReasonAndCodeList[7]))
                {
                    spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner,
                            getListOfMRCodes(MeterReadingNotesListsConstants.letterToCustomerReasonList,
                                    MeterReadingNotesListsConstants.letterToCustomerCodeList), 1));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }

        });

        //spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner, concateArray, 1));

        /*
         * try{
         *
         * spinner.removeViewAt(10); spinner.removeViewAt(17); }
         * catch(UnsupportedOperationException e){ e.printStackTrace(); }
         */
        // spinner.setAdapter(adapter);

        spinner.setVisibility(View.VISIBLE);
        final_attempt = (CheckBox) findViewById(R.id.checkbox);
        String extraStr_customerSummary = "selectedCustomerJSON";
        Bundle bundle = getIntent().getExtras();

        if (getIntent().hasExtra(extraStr_customerSummary))
        {
            selectedCustomerJson = bundle.getString(extraStr_customerSummary);
        }
        try
        {
            if (selectedCustomerJson != null)
            {
                jsonoCustomerDetails = new JSONObject(selectedCustomerJson);
                MobiculeLogger.verbose("JsonoCustomerDetails::" + jsonoCustomerDetails);
                avgConsuption = jsonoCustomerDetails.getValue(Constants.KEY_AVERAGE_CONSUMPTION).toString();
                previousReading = jsonoCustomerDetails.getValue(Constants.KEY_CUSTOMER_PREVIOUS_READING).toString();
                previousReadingValue = Double.parseDouble(previousReading);
                MobiculeLogger.verbose("previousReadingValue::" + previousReadingValue);
                avgCon = Double.parseDouble(avgConsuption);

                previousDate = jsonoCustomerDetails.getValue(Constants.KEY_PREVIOUS_READING_DATE).toString();
                MobiculeLogger.verbose("avgConsuption::" + avgConsuption);
                MobiculeLogger.verbose("previousDate::" + previousDate);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                convertedPreviousDate = new Date();
                try
                {
                    convertedPreviousDate = dateFormat.parse(previousDate);
                    previousTimeInMills = convertedPreviousDate.getTime();

                    currentTimeInMills = System.currentTimeMillis();

                    Date date = new Date(currentTimeInMills);

                    MobiculeLogger.verbose("Current Date:" + dateFormat.format(date));

                    Date currentDate = dateFormat.parse(dateFormat.format(date));

                    MobiculeLogger.verbose("Current Time in Millis old:" + currentTimeInMills + " Current date time in millis:"
                            + currentDate.getTime());

                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }

                diffInDays = (int) ((currentTimeInMills - previousTimeInMills) / (1000 * 60 * 60 * 24));
                MobiculeLogger.verbose("Days Difference:" + diffInDays);

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        MobiculeLogger.verbose("currentTimeInMills " + currentTimeInMills);
        MobiculeLogger.verbose("previousTimeInMills " + previousTimeInMills);
        // numOfDays = currentTimeInMills - previousTimeInMills;
        // numDays = new Long(numOfDays).doubleValue();
        // Log.d("numOfDays ", " numOfDays in long value " + numOfDays);
        MobiculeLogger.verbose("avgCon  in double" + avgCon);
        MobiculeLogger.verbose("diffInDayse" + diffInDays);
        // Log.d(" ", "Number of days  in double" + numDays);

        // xHigh = avgCon * numDays;
        xHigh = avgCon * diffInDays;
        yHigh = 2 * xHigh;
        yLow = 0.25 * xHigh;
        MobiculeLogger.verbose("Value of x High" + xHigh);
        MobiculeLogger.verbose("Value of y High" + yHigh);
        MobiculeLogger.verbose("Value of yLow" + yLow);
        meterReaderMsg = new MeterReaderMsg();

        spinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3)
            {
                meterReadingBO.setIsLPGSupplier("");
                meterReadingBO.setIsRelocation("");
                meterReadingBO.setMsgRemarksPrimary("");
                meterReadingBO.setIsSerailCorrect("");
                meterReadingBO.setIsPremiselCorrect("");
                meterReadingBO.setIsSerailVisible("");
                meterReadingBO.setIsReEnterMeterReading("");
                selectedItem = (String) parent.getItemAtPosition(position);

                String selectedCode = selectedItem.replaceAll("[^0-9]", "");

                String spinnerselectedItemReason = selectedItem.replaceAll("[^a-zA-Z ]+$", "");

                MobiculeLogger.verbose("MeterReading - selectedCode :" + selectedCode + " spinnerselectedItemReason :"
                        + spinnerselectedItemReason);

                ll_relocation.setVisibility(View.GONE);
                relocation = "";

                if (zeroConsumption != 1)
                {
                    Constants.KEY_METER_READING_TYPE = Constants.KEY_INCOMPLETE;
                    MobiculeLogger.verbose("Code spinner selected position : " + position);

                    changeMessageList(selectedCode);

                    MobiculeLogger.verbose("Selected Item of meter code - selectedItem :" + selectedItem);
                    MobiculeLogger.verbose("Meter Reading - concateArray[24] : " + concateArray[24]);
                    final_attempt.setVisibility(View.INVISIBLE);
                    if (selectedItem.equals(concateArray[0]))
                    {

                        meterreadingEdittext.setText("");
                        final_attempt.setVisibility(View.INVISIBLE);
                        meterReadingText.setVisibility(View.INVISIBLE);
                        meterreadingEdittext.setVisibility(View.INVISIBLE);
                        spinnerMsg.setVisibility(View.VISIBLE);
                        messageRelativeLayout.setVisibility(View.VISIBLE);
                        meterReading_Remarks.setVisibility(View.VISIBLE);//changed
                        meterReading_remarksvalue.setVisibility(View.VISIBLE);//changed
                        //ll_relocation.setVisibility(View.GONE);//changed

                    }
                    else if (selectedItem.equals(concateArray[1])
                            || selectedItem.equals(concateArray[24])
                            || selectedItem
                            .equals(concateArray[MeterReadingNotesListsConstants.allReasonsList.length - 1]))
                    {
                        MobiculeLogger.verbose("In else if : Meter Normal");
                        final_attempt.setVisibility(View.GONE);
                        meterReadingText.setVisibility(View.INVISIBLE);
                        meterreadingEdittext.setVisibility(View.INVISIBLE);
                        if (selectedItem.equals(concateArray[24]))
                        {
                            MobiculeLogger.verbose("In if : Using for commercial purpose");
                            meterReadingText.setVisibility(View.VISIBLE);
                            meterreadingEdittext.setVisibility(View.VISIBLE);
                            spinnerMsg.setVisibility(View.VISIBLE);
                            messageRelativeLayout.setVisibility(View.VISIBLE);
                            meterReading_Remarks.setVisibility(View.VISIBLE);//changed
                            meterReading_remarksvalue.setVisibility(View.VISIBLE);
                            final_attempt.setVisibility(View.GONE);//changed
                            //ll_relocation.setVisibility(View.GONE);//changed
                        }
                        else if(selectedItem.equals(concateArray[1]))
                        {
                            MobiculeLogger.verbose("In else : Meter Normal");

                            meterReadingText.setVisibility(View.VISIBLE);
                            meterreadingEdittext.setVisibility(View.VISIBLE);
                            spinnerMsg.setVisibility(View.GONE);
                            messageRelativeLayout.setVisibility(View.GONE);
                            meterReading_Remarks.setVisibility(View.GONE);
                            meterReading_remarksvalue.setVisibility(View.GONE);

                        }
                    }

					/*else if (selectedItem.equals(concateArray[24])
							|| selectedItem
									.equals(concateArray[MeterReadingNotesListsConstants.allReasonsList.length - 1]))
					{
						final_attempt.setVisibility(View.VISIBLE);
						meterReadingText.setVisibility(View.INVISIBLE);
						meterreadingEdittext.setVisibility(View.INVISIBLE);
						if (selectedItem.equals(concateArray[24]))
						{
							meterReadingText.setVisibility(View.GONE);
							meterreadingEdittext.setVisibility(View.GONE);
							spinnerMsg.setVisibility(View.VISIBLE);
							messageRelativeLayout.setVisibility(View.VISIBLE);
							meterReading_Remarks.setVisibility(View.VISIBLE);//changed
							meterReading_remarksvalue.setVisibility(View.VISIBLE);
							final_attempt.setVisibility(View.VISIBLE);//changed
							//ll_relocation.setVisibility(View.GONE);//changed
						}

					}*/

                    else if (selectedItem.equals(concateArray[15]) || (selectedItem.equals(concateArray[17]))
                            || (selectedItem.equals(concateArray[19])))
                    {
                        meterreadingEdittext.setText("");
                        final_attempt.setVisibility(View.VISIBLE);
                        meterreadingEdittext.setVisibility(View.INVISIBLE);
                        meterReadingText.setVisibility(View.INVISIBLE);
                        spinnerMsg.setVisibility(View.VISIBLE);
                        messageRelativeLayout.setVisibility(View.VISIBLE);
                        meterReading_Remarks.setVisibility(View.VISIBLE);//changed
                        meterReading_remarksvalue.setVisibility(View.VISIBLE);//changed
                        ll_relocation.setVisibility(View.GONE);//changed
                    }
                    else if (selectedItem.equals(concateArray[12]))
                    {

                        meterreadingEdittext.setText("");
                        final_attempt.setVisibility(View.VISIBLE);
                        meterReadingText.setVisibility(View.VISIBLE);
                        meterreadingEdittext.setVisibility(View.VISIBLE);
                        spinnerMsg.setVisibility(View.VISIBLE);
                        messageRelativeLayout.setVisibility(View.VISIBLE);
                        meterReading_Remarks.setVisibility(View.VISIBLE);//changed
                        meterReading_remarksvalue.setVisibility(View.VISIBLE);//changed
                        //ll_relocation.setVisibility(View.VISIBLE);//changed

                    }

                    else if (selectedItem.equals(concateArray[13]))
                    {
                        meterreadingEdittext.setText("");
                        final_attempt.setVisibility(View.VISIBLE);
                        meterReadingText.setVisibility(View.VISIBLE);
                        meterreadingEdittext.setVisibility(View.VISIBLE);
                        spinnerMsg.setVisibility(View.VISIBLE);
                        messageRelativeLayout.setVisibility(View.VISIBLE);
                        meterReading_Remarks.setVisibility(View.VISIBLE);//changed
                        meterReading_remarksvalue.setVisibility(View.VISIBLE);//changed

                        //ll_relocation.setVisibility(View.VISIBLE);//changed
                    }
                    else if (selectedItem.equals(concateArray[3]))
                    {
                        //finish();

                        meterreadingEdittext.setText("");
                        final_attempt.setVisibility(View.VISIBLE);
                        meterreadingEdittext.setVisibility(View.VISIBLE);
                        meterReadingText.setVisibility(View.VISIBLE);
                        spinnerMsg.setVisibility(View.VISIBLE);
                        messageRelativeLayout.setVisibility(View.VISIBLE);
                        meterReading_Remarks.setVisibility(View.VISIBLE);//changed
                        meterReading_remarksvalue.setVisibility(View.VISIBLE);//changed
                        //ll_relocation.setVisibility(View.GONE);//changed
                    }
                    //else if (selectedItem.equals(concateArray[10]) || (selectedItem.equals(concateArray[17])))
                    else if (selectedItem.equals(concateArray[17]))
                    {

                        finish();

                        Intent i = new Intent(MeterReading.this, MeterReading.class);
                        startActivity(i);

                    }
                    else
                    {

                        meterreadingEdittext.setText("");
                        final_attempt.setVisibility(View.VISIBLE);
                        meterreadingEdittext.setVisibility(View.INVISIBLE);
                        meterReadingText.setVisibility(View.INVISIBLE);
                        spinnerMsg.setVisibility(View.VISIBLE);
                        messageRelativeLayout.setVisibility(View.VISIBLE);
                        //ll_relocation.setVisibility(View.GONE);//changed

                    }

                }
                else
                {
                    if (zeroConsumption == 1)
                    {
                        final_attempt.setVisibility(View.VISIBLE);
                        spinnerMsg.setVisibility(View.VISIBLE);
                        messageRelativeLayout.setVisibility(View.VISIBLE);
                        //meterReadingText.setText("Previous Reading : ");
                        // changeMessageList(position);
                        selectedItem = spinner.getSelectedItem().toString();
                        MobiculeLogger.verbose("selected Item Zero Consumption   " + selectedItem);
                        meterreadingEdittext.setEnabled(false);
                        meterreadingEdittext.setText(previousReading);
                        meterreadingEdittext.setFocusable(false);

                        if (selectedItem.equals("Counter Defective    8"))
                        {
                            changeMessageList("8");
                        }
                        else if (selectedItem.equals("Not Using Gas     52"))
                        {
                            changeMessageList("52");
                        }
                        else if (selectedItem.equals("Temporary Disconnection     1"))
                        {
                            changeMessageList("1");
                        }
                        else if (selectedItem.equals("Meter Testing not possible  69"))
                        {
                            changeMessageList("69");
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                selectedItem = (String) parent.getItemAtPosition(0);
                MobiculeLogger.verbose(" ON Nothng Selected  Code...." + selectedItem);
                meterReadingText.setVisibility(View.VISIBLE);
                meterreadingEdittext.setVisibility(View.VISIBLE);
            }
        });

        spinnerMsg.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3)
            {

                selectedMessageItem = (String) parent.getItemAtPosition(position);
                MobiculeLogger.verbose("Message Selected" + selectedMessageItem);

                relocation = "";

                if (selectedItem.replaceAll("[^0-9]", "").equalsIgnoreCase("14")
                        || selectedItem.replaceAll("[^0-9]", "").equalsIgnoreCase("13"))
                {
                    String msgList[] = meterReaderMsg.getMessageList(selectedItem.replaceAll("[^0-9]", ""));
					/*if (selectedMessageItem.equalsIgnoreCase(msgList[3]))
					{
						ll_relocation.setVisibility(View.VISIBLE);
					}
					else
					{
						ll_relocation.setVisibility(View.GONE);
					}*/
                }
				/*if (selectedMessageItem.equals("Other(Write)"))
				{
					Log.d("", "Show the remarks field");
					meterReading_Remarks.setVisibility(View.VISIBLE);
					meterReading_remarksvalue.setVisibility(View.VISIBLE);
				}
				else
				{
					Log.d("", "Hide the remarks field");
					meterReading_Remarks.setVisibility(View.GONE);
					meterReading_remarksvalue.setVisibility(View.GONE);
				}*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                selectedMessageItem = (String) parent.getItemAtPosition(0);
                MobiculeLogger.verbose("ON Nothng Selected Msg ...." + selectedMessageItem);
            }
        });

    }

    private String[] getListOfMRCodes(String[] reasonsList, String[] codesList)
    {
        //MeterReadingNotesListsConstants.allCodesList = new String[0];
        //MeterReadingNotesListsConstants.allReasonsList = new String[0];

        String[] concatResonsAndCodes = new String[reasonsList.length];
        for (int i = 0; i < concatResonsAndCodes.length; i++)
        {
            concatResonsAndCodes[i] = reasonsList[i] + " " + codesList[i];
        }

        //MeterReadingNotesListsConstants.allCodesList = codesList;
        //MeterReadingNotesListsConstants.allReasonsList = reasonsList;

        return concatResonsAndCodes;
    }

    private void changeMessageList(String code)
    {
        try
        {
            String[] msgList = meterReaderMsg.getMessageList(code);
            if (msgList == null)
            {
                msgList = new String[] { "Select", "NA" };
            }

            // adapterMsg = new ArrayAdapter<String>(this,
            // android.R.layout.simple_spinner_item, msgList);
            // adapterMsg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // spinnerMsg.setAdapter(adapterMsg);
            // List<String> meterMessageList = new
            // ArrayList<String>(Arrays.asList(msgList));
            spinnerMsg.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner, msgList, 0));
            // adapterMsg.notifyDataSetChanged();
        }
        catch (Exception e)
        {
            MobiculeLogger.verbose(TAG, "setMessageList() - " + e.toString());
        }
    }

    private void changeList()
    {
        try
        {
            spinner.setAdapter(null);
            String[] msgList = new String[] { "Select", "Counter Defective    8", "Not Using Gas     52",
                    "Temporary Disconnection     1", "Meter Testing not possible 69" };
            zeroConsumption = 1;
            spinner.setAdapter(new CustomSpinnerAdapter(MeterReading.this, R.layout.customspinner, msgList, 0));

        }
        catch (Exception e)
        {
            MobiculeLogger.verbose(TAG, "setMessageList() - " + e.toString());
        }

    }

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add("Next");
		menu.add("Cancel");

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getTitle().equals("Next"))
		{
			// ************* For setting BO **********************
			String messageSelected = spinnerMsg.getSelectedItem().toString();
			Log.d("", "messageSelected:" + messageSelected);
			if (selectedItem.equals(concateArray[0]))
			{
				Toast.makeText(getApplicationContext(), "Please Select Code", Toast.LENGTH_LONG).show();
			}
			else if (messageSelected.equals("Select") && !selectedItem.equals(concateArray[1]))
			{
				Toast.makeText(getApplicationContext(), "Please Select Message", Toast.LENGTH_LONG).show();
			}
			else
			{
				remarksValue = meterReading_remarksvalue.getText().toString();
				if (messageSelected.equals("other") && remarksValue.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please Enter Remarks", Toast.LENGTH_LONG).show();

				}
				else
				{
					if (!remarksValue.equals(""))
					{
						// Toast.makeText(getApplicationContext(),
						// "Set Value to BO", Toast.LENGTH_LONG).show();
						meterReadingBO.setMsgRemarks(remarksValue);
					}
					Log.d("", "All data is filled up");

					String spinnerselectedItemCode = selectedItem.replaceAll("[^0-9]", "");

					String spinnerselectedItemReason = selectedItem.replaceAll("[^a-zA-Z ]+$", "");

					String spinnerselectedItemCode = MeterReadingNotesListsConstants.allCodesList[spinner
							.getSelectedItemPosition()];

					String spinnerselectedItemReason = MeterReadingNotesListsConstants.allReasonsList[spinner
							.getSelectedItemPosition()];

					Log.d(TAG, "CODE " + spinnerselectedItemCode + " REASON " + spinnerselectedItemReason);
					String meterReading;
					meterReadingBO.setMrReason(spinnerselectedItemReason);
					meterReadingBO.setMsgFromMr(spinnerMsg.getSelectedItem().toString());

					if (meterreadingEdittext.getVisibility() == View.VISIBLE)
					{
						meterReading = meterreadingEdittext.getText().toString();
					}
					else
					{
						meterReading = "";
					}

					SimpleDateFormat dateTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.US);

					String readingTime = dateTime.format(new Date(System.currentTimeMillis()));
					String time = readingTime.substring(11);
					String date = readingTime.substring(0, 10);

					MeterReadingFeild.readingTime = time;
					MeterReadingFeild.date = date;
					MeterReadingFeild.mrCode = spinnerselectedItemCode;

					if (zeroConsumption == 1)
					{

						if (selectedItem.equals("Counter Defective    8"))
						{
							MeterReadingFeild.mrCode = "8";
							meterReadingBO.setMrReason(selectedItem);
							meterReadingBO.setMsgFromMr(selectedMessageItem);
						}
						else if (selectedItem.equals("Not Using Gas     52"))
						{
							MeterReadingFeild.mrCode = "52";
							meterReadingBO.setMrReason(selectedItem);
							meterReadingBO.setMsgFromMr(selectedMessageItem);
						}
						else if (selectedItem.equals("Temporary Disconnection     1"))
						{
							MeterReadingFeild.mrCode = "1";
							meterReadingBO.setMrReason(selectedItem);
							meterReadingBO.setMsgFromMr(selectedMessageItem);
						}
						Log.e("Zero Consumption :", "Code " + MeterReadingFeild.mrCode + "  Reason " + selectedItem
								+ "  Message " + selectedMessageItem);

					}

					if (selectedItem.equals(concateArray[1]))
					{
						if (!StringUtil.isValid(meterReading))
						{
							Toast.makeText(MeterReading.this, "Please enter Meter Reading", Toast.LENGTH_LONG).show();
							return false;
						}
						else if (meterReading.length() < 3)
						{
							Toast.makeText(MeterReading.this, "Please enter atleast 3 digit Meter Reading",
									Toast.LENGTH_LONG).show();
							return false;
						}
					}
					if (!StringUtil.isValid(meterReading))
					{

						MeterReadingFeild.meterReading = "";
						if (!final_attempt.isChecked())
						{
							meterReadingBO.setStatus(Constants.FIELD_INCOMPLETE);
							meterReadingBO.setLock("0");// false
						}
						else
						{
							meterReadingBO.setStatus(Constants.FIELD_COMPLETED);
							meterReadingBO.setLock("1");// true
						}
					}
					else
					{
						MeterReadingFeild.meterReading = meterReading;
						meterReadingBO.setStatus(Constants.FIELD_COMPLETED);
						meterReadingBO.setLock("1");// true
					}

					if (zeroConsumption == 1)
					{
						if (!final_attempt.isChecked())
						{
							meterReadingBO.setStatus(Constants.FIELD_INCOMPLETE);
							meterReadingBO.setLock("0");// false
						}
						else
						{
							meterReadingBO.setStatus(Constants.FIELD_COMPLETED);
							meterReadingBO.setLock("1");// true
						}
					}

					if (spinner.getSelectedItem().equals(concateArray[21]))
					{
						Log.i("meterReading", "concateArray[21]");
						meterReadingBO.setStatus(Constants.FIELD_COMPLETED);
						meterReadingBO.setLock("1");
					}
					Log.e("meter readingg ", "Code is::  *******  " + spinnerselectedItemCode);

					Log.e("meter readingg ", "item is::  *******  " + spinner.getSelectedItem());

					// ************* setting BO over **********************

					// Log.i("mruCode : ", meterReadingBO.getMruCode());
					if (meterReadingFacade.isCommercialCustomer(meterReadingBO)
							&& (selectedItem.equals(concateArray[1])))
					{
						Intent intent = new Intent(MeterReading.this,

	.class);
						startActivity(intent);
					}


					 * else if
					 * (spinner.getSelectedItem().equals(concateArray[21])) {
					 * Log.i("", "Meter Reading BO:" + meterReadingBO.toJSON());
					 * Intent intent = new Intent(MeterReading.this,
					 * Summary.class); startActivity(intent);
					 * meterReadingBO.setImage(""); }

					else
					{
						if (spinner.getSelectedItem().equals(concateArray[1]))
						{
							double newMeterReading = Double.parseDouble(meterReading);
							double newDiffMeterReading = newMeterReading - previousReadingValue;
							Log.e("consumption---", "prevRead :" + previousReadingValue + " New Read : "
									+ newMeterReading);

							long xH = Math.round(xHigh);
							long Yh = Math.round(yHigh);
							long yL = Math.round(yLow);
							Log.d(" ", "xL" + xH);
							Log.d(" ", "Yh" + Yh);
							Log.d(" ", "yL" + yL);
							Log.d(" ", "newDiffMeterReading" + newDiffMeterReading);

							if (previousReadingValue == newMeterReading)
							{
								Log.d("Zero consumption", "Zero consumption");

								AlertDialog.Builder builder = new AlertDialog.Builder(MeterReading.this);
								builder.setCancelable(true);

								builder.setMessage("Please check Meter / Reading once again and select 'Not using gas code(52)' or 'Counter defective code(8)' or 'Temporary Disconnection code(01)' ");
								builder.setInverseBackgroundForced(true);
								builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										dialog.dismiss();
										Intent intent = new Intent(MeterReading.this, VerifyMeterReadingActivity.class);
										intent.putExtra(
												"CODE_AND_REASONS_LIST",
												getListOfMRCodes(
														MeterReadingNotesListsConstants.meterNormalReasonsList,
														MeterReadingNotesListsConstants.meterNormalCodeList));
										intent.putExtra("CODESLIST",
												MeterReadingNotesListsConstants.meterNormalCodeList);
										intent.putExtra("ZEROCONSUMPTION", true);
										startActivity(intent);
										//changeList();
									}
								});
								builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										dialog.dismiss();
									}
								});
								AlertDialog alert = builder.create();
								alert.show();

								// goToPhotoActivity();

							}
							else if (previousReadingValue > newMeterReading)
							{
								Log.d("Negative consumption", "Negative consumption");
								commonConfimrationDialog("Please reverify Meter number and Enter proper Meter reading");
							}
							else if (newDiffMeterReading > yHigh)
							{
								Log.d("High consumption", "High consumption");
								commonConfimrationDialog("Please reverify Meter number and Enter proper Meter reading");
							}
							else if (newDiffMeterReading < yLow)
							{
								Log.d("Low consumption", "Low consumption");
								commonConfimrationDialog("Please reverify Meter number and Enter proper Meter reading");
							}
							else
							{
								goToPhotoActivity();
							}
						}
						else
						{
							goToPhotoActivity();
						}

					}
				}
			}
		}
		else if (item.getTitle().equals("Cancel"))
		{
			Intent intent = new Intent(MeterReading.this, CustomerInformation.class);
			startActivity(intent);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}*/

    private void goToPhotoActivity()
    {
        Constants.threePicsCapCnt = 0;

        if ((spinner.getSelectedItem().equals(concateArray[2])) || (spinner.getSelectedItem().equals(concateArray[13]))
                || (spinner.getSelectedItem().equals(concateArray[12]))
                || (spinner.getSelectedItem().equals(concateArray[14]))
                || (spinner.getSelectedItem().equals(concateArray[21])))
        {

            Constants.isThreePicsSelected = true;
            Constants.isTwoPicsSelected = false;
        }
        else if ((spinner.getSelectedItem().equals(concateArray[20])
                || (spinner.getSelectedItem().equals(concateArray[18]))
                || (spinner.getSelectedItem().equals(concateArray[17]))
                || (spinner.getSelectedItem().equals(concateArray[16]))
                || (spinner.getSelectedItem().equals(concateArray[15]))
                || (spinner.getSelectedItem().equals(concateArray[10])) || (spinner.getSelectedItem()
                .equals(concateArray[3]))))
        {
            Constants.isTwoPicsSelected = true;
            Constants.isThreePicsSelected = false;
        }

        else
        {
            Constants.isThreePicsSelected = false;
            Constants.isTwoPicsSelected = false;
        }
        intentVal = sharedpreferences.getString("activity", "");

        Log.e("IntentVal111",""+intentVal);

        Intent intent = new Intent(MeterReading.this, PhotoIntentActivity.class);
        intent.putExtra("activity", intentVal);
        MobiculeLogger.verbose("activityFromIntent ::: " + intentVal);

        if (spinner.getSelectedItem().equals(concateArray[2]))
        {
            intent.putExtra(getString(R.string.isdoorlockedpopup), true);


        }
        //	activityFromIntent = getIntent().getStringExtra("activity");

        //intent.putExtra("activity", activityFromIntent);
        //	activityFromIntent = getIntent().getStringExtra("activity");


        startActivity(intent);
    }

    private void commonConfimrationDialog(String message)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(MeterReading.this);
        builder.setCancelable(true);
        builder.setMessage(message);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                //goToPhotoActivity();
                Intent intent = new Intent(MeterReading.this, VerifyMeterReadingActivity.class);
                intent.putExtra(
                        "CODE_AND_REASONS_LIST",
                        getListOfMRCodes(MeterReadingNotesListsConstants.meterNormalReasonsList,
                                MeterReadingNotesListsConstants.meterNormalCodeList));
                intent.putExtra("CODESLIST", MeterReadingNotesListsConstants.meterNormalCodeList);
                intent.putExtra("ZEROCONSUMPTION", false);
                intent.putExtra("activity", activityFromIntent);

                startActivity(intent);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void goToVerifyActivity()
    {
        Intent intent = new Intent(MeterReading.this, VerifyMeterReadingActivity.class);
        intent.putExtra(
                "CODE_AND_REASONS_LIST",
                getListOfMRCodes(MeterReadingNotesListsConstants.meterNormalReasonsList,
                        MeterReadingNotesListsConstants.meterNormalCodeList));
        intent.putExtra("CODESLIST", MeterReadingNotesListsConstants.meterNormalCodeList);
        intent.putExtra("ZEROCONSUMPTION", false);
        intent.putExtra("activity", activityFromIntent);
        startActivity(intent);
    }

    private void commonConfimrationDialog1(String message, String implausibleType)
    {
        v_spaceView.setVisibility(View.VISIBLE);
        final String _implausibleType = implausibleType;
        AlertDialog.Builder builder = new AlertDialog.Builder(MeterReading.this);
        builder.setCancelable(true);

        builder.setMessage(message);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                v_spaceView.setVisibility(View.GONE);
                //goToPhotoActivity();
                Intent intent = new Intent(MeterReading.this, ImplausibleReading.class);
                intent.putExtra(
                        "CODE_AND_REASONS_LIST",
                        getListOfMRCodes(MeterReadingNotesListsConstants.meterNormalReasonsList,
                                MeterReadingNotesListsConstants.meterNormalCodeList));
                intent.putExtra("CODESLIST", MeterReadingNotesListsConstants.meterNormalCodeList);
                intent.putExtra("ZEROCONSUMPTION", false);
                intent.putExtra("activity", activityFromIntent);
                intent.putExtra("implausibleType", _implausibleType);

                startActivity(intent);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                dialog.dismiss();
                v_spaceView.setVisibility(View.GONE);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

        alert.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    arg0.dismiss();
                    v_spaceView.setVisibility(View.GONE);
                }
                return true;
            }
        });

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
                v_spaceView.setVisibility(View.GONE);
            }
        });

    }

    private void showCustomAlertDialog(String[] codesList, String[] alertMessageHindiList,
                                       String[] alertMessageEnglishiList, String title)
    {
        // custom dialog
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);

        //dialog.setTitle(title);

        dialog.setContentView(R.layout.alert_dialog_custom);

        TextView titleText = (TextView) dialog.findViewById(R.id.alertDialogTitleText);

        titleText.setText(title);

        // set the custom dialog components - text, image and button
        ListView dialogList = (ListView) dialog.findViewById(R.id.list);

        CustomListAdapter adapter = new CustomListAdapter(this, R.layout.alert_dialog_list_item, codesList,
                alertMessageHindiList, alertMessageEnglishiList);

        adapter.notifyDataSetChanged();

        dialogList.setAdapter(adapter);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed()
    {
        v_spaceView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.next_button:

                // ************* For setting BO **********************
                String messageSelected = spinnerMsg.getSelectedItem().toString();
                MobiculeLogger.verbose("messageSelected:" + messageSelected);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(meterreadingEdittext.getWindowToken(), 0);
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(meterReading_remarksvalue.getWindowToken(), 0);
                if (selectedItem.equals(concateArray[0]))
                {
                    Toast.makeText(getApplicationContext(), "Please Select Code", Toast.LENGTH_LONG).show();
                }
                else if (messageSelected.equals("Select") && !selectedItem.equals(concateArray[1]))
                {
                    Toast.makeText(getApplicationContext(), "Please Select Message", Toast.LENGTH_LONG).show();
                }
			/*	else if (ll_relocation.getVisibility() == View.VISIBLE &&!isYesrelocation&& !isNorelocation)
				{
					Toast.makeText(getApplicationContext(), "Please Select Customer interested for relocation?",
							Toast.LENGTH_LONG).show();
				}*/
                else
                {
                    remarksValue = meterReading_remarksvalue.getText().toString();
                    if (messageSelected.equals("other") && remarksValue.equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "Please Enter Remarks", Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        if (!remarksValue.equals(""))
                        {
                            // Toast.makeText(getApplicationContext(),
                            // "Set Value to BO", Toast.LENGTH_LONG).show();
                            meterReadingBO.setMsgRemarks(remarksValue);

                        }
                        meterReadingBO.setIsRelocation(relocation);
                        MobiculeLogger.verbose("All data is filled up");

                        String spinnerselectedItemCode = selectedItem.replaceAll("[^0-9]", "");

                        String spinnerselectedItemReason = selectedItem.replaceAll("[^a-zA-Z ]+$", "").trim();

						/*String spinnerselectedItemCode = MeterReadingNotesListsConstants.allCodesList[spinner
								.getSelectedItemPosition()];
						String spinnerselectedItemReason = MeterReadingNotesListsConstants.allReasonsList[spinner
								.getSelectedItemPosition()];*/

                        MobiculeLogger.verbose("CODE " + spinnerselectedItemCode + " REASON " + spinnerselectedItemReason);
                        String meterReading;
                        meterReadingBO.setMrReason(spinnerselectedItemReason);
                        meterReadingBO.setMsgFromMr(spinnerMsg.getSelectedItem().toString());

                        if (meterreadingEdittext.getVisibility() == View.VISIBLE)
                        {
                            meterReading = meterreadingEdittext.getText().toString();
                        }
                        else
                        {
                            meterReading = "";
                        }

                        SimpleDateFormat dateTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.US);

                        String readingTime = dateTime.format(new Date(System.currentTimeMillis()));
                        String time = readingTime.substring(11);
                        String date = readingTime.substring(0, 10);

                        MeterReadingFeild.readingTime = time;
                        MeterReadingFeild.date = date;
                        MeterReadingFeild.mrCode = spinnerselectedItemCode;

                        if (zeroConsumption == 1)
                        {

                            if (selectedItem.equals("Counter Defective    8"))
                            {
                                MeterReadingFeild.mrCode = "8";
                                meterReadingBO.setMrReason(selectedItem);
                                meterReadingBO.setMsgFromMr(selectedMessageItem);
                            }
                            else if (selectedItem.equals("Not Using Gas     52"))
                            {
                                MeterReadingFeild.mrCode = "52";
                                meterReadingBO.setMrReason(selectedItem);
                                meterReadingBO.setMsgFromMr(selectedMessageItem);
                            }
                            else if (selectedItem.equals("Temporary Disconnection     1"))
                            {
                                MeterReadingFeild.mrCode = "1";
                                meterReadingBO.setMrReason(selectedItem);
                                meterReadingBO.setMsgFromMr(selectedMessageItem);
                            }

                            else if (selectedItem.equals("Meter Testing not possible  69"))
                            {
                                MeterReadingFeild.mrCode = "69";
                                meterReadingBO.setMrReason(selectedItem);
                                meterReadingBO.setMsgFromMr(selectedMessageItem);
                            }
                            MobiculeLogger.verbose("Zero Consumption :"+ "Code " + MeterReadingFeild.mrCode + "  Reason " + selectedItem
                                    + "  Message " + selectedMessageItem);

                        }

                        if (selectedItem.equals(concateArray[1]) || selectedItem.equals(concateArray[24]))
                        {
                            if (!StringUtil.isValid(meterReading))
                            {
                                Toast.makeText(MeterReading.this, "Please enter Meter Reading", Toast.LENGTH_LONG)
                                        .show();
                                break;
                            }
                            else if (meterReading.length() < 3)
                            {
                                Toast.makeText(MeterReading.this, "Please enter atleast 3 digit Meter Reading",
                                        Toast.LENGTH_LONG).show();
                                break;
                            }
                        }
                        if (!StringUtil.isValid(meterReading))
                        {

                            MeterReadingFeild.meterReading = "";
                            if (!final_attempt.isChecked())
                            {
                                meterReadingBO.setStatus(Constants.FIELD_INCOMPLETE);
                                meterReadingBO.setLock("0");// false
                            }
                            else
                            {
                                meterReadingBO.setStatus(Constants.FIELD_COMPLETED);
                                meterReadingBO.setLock("1");// true
                            }
                        }
                        else
                        {
                            MeterReadingFeild.meterReading = meterReading;
                            meterReadingBO.setStatus(Constants.FIELD_COMPLETED);
                            meterReadingBO.setLock("1");// true
                        }

                        if (zeroConsumption == 1)
                        {
                            if (!final_attempt.isChecked())
                            {
                                meterReadingBO.setStatus(Constants.FIELD_INCOMPLETE);
                                meterReadingBO.setLock("0");// false
                            }
                            else
                            {
                                meterReadingBO.setStatus(Constants.FIELD_COMPLETED);
                                meterReadingBO.setLock("1");// true
                            }
                        }

                        if (spinner.getSelectedItem().equals(concateArray[21]))
                        {
                            MobiculeLogger.verbose("meterReading - concateArray[21]");
                            meterReadingBO.setStatus(Constants.FIELD_COMPLETED);
                            meterReadingBO.setLock("1");
                        }

                        // ************* setting BO over **********************

                        MobiculeLogger.verbose("mruCode : "+ meterReadingBO.getMruCode());
                        if (meterReadingFacade.isCommercialCustomer(meterReadingBO)
                                && (selectedItem.equals(concateArray[1])) || (selectedItem.equals(concateArray[24])))
                        {
                            Intent intent = new Intent(MeterReading.this, Inspection.class);
                            intent.putExtra("activity", intentVal);

                            startActivity(intent);
                        }

                        /*
                         * else if
                         * (spinner.getSelectedItem().equals(concateArray[21])) {
                         * Log.i("", "Meter Reading BO:" + meterReadingBO.toJSON());
                         * Intent intent = new Intent(MeterReading.this,
                         * Summary.class); startActivity(intent);
                         * meterReadingBO.setImage(""); }
                         */
                        else
                        {
                            if (spinner.getSelectedItem().equals(concateArray[1]))
                            {
                                double newMeterReading = Double.parseDouble(meterReading);
                                double newDiffMeterReading = newMeterReading - previousReadingValue;
                                MobiculeLogger.verbose("consumption---"+ "prevRead :" + previousReadingValue + " New Read : "
                                        + newMeterReading);

                                long xH = Math.round(xHigh);
                                long Yh = Math.round(yHigh);
                                long yL = Math.round(yLow);
                                MobiculeLogger.verbose("xH" + xH);
                                MobiculeLogger.verbose("Yh" + Yh);
                                MobiculeLogger.verbose("yL" + yL);
                                MobiculeLogger.verbose("newDiffMeterReading" + newDiffMeterReading);

                                if (previousReadingValue == newMeterReading)
                                {
                                    v_spaceView.setVisibility(View.VISIBLE);
                                    Constants.KEY_METER_READING_TYPE = Constants.KEY_IMPLAUSIBLE;
                                    MobiculeLogger.verbose("Zero consumption");

                                    AlertDialog.Builder builder = new AlertDialog.Builder(MeterReading.this);
                                    builder.setCancelable(true);

                                    builder.setMessage("Please check Meter / Reading once again and select 'Not using gas code(52)' or 'Meter/Counter defective code(18)' or 'Temporary Disconnection code(01)' "
                                            + "\r\n"
                                            + this.getString(R.string.please_check_meter_reading_once_again_for_zero_consumption));
                                    builder.setInverseBackgroundForced(true);

                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            dialog.dismiss();
                                            v_spaceView.setVisibility(View.GONE);
                                            Intent intent = new Intent(MeterReading.this,
                                                    VerifyMeterReadingActivity.class);
                                            intent.putExtra(
                                                    "CODE_AND_REASONS_LIST",
                                                    getListOfMRCodes(
                                                            MeterReadingNotesListsConstants.meterNormalReasonsList,
                                                            MeterReadingNotesListsConstants.meterNormalCodeList));
                                            intent.putExtra("CODESLIST",
                                                    MeterReadingNotesListsConstants.meterNormalCodeList);
                                            intent.putExtra("ZEROCONSUMPTION", true);
                                            intent.putExtra("activity", activityFromIntent);
                                            startActivity(intent);
                                            //changeList();
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            dialog.dismiss();
                                            v_spaceView.setVisibility(View.GONE);
                                        }
                                    });


                                    AlertDialog alert = builder.create();
                                    alert.show();

                                    alert.setOnKeyListener(new Dialog.OnKeyListener() {

                                        @Override
                                        public boolean onKey(DialogInterface arg0, int keyCode,
                                                             KeyEvent event) {
                                            // TODO Auto-generated method stub
                                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                                arg0.dismiss();
                                                v_spaceView.setVisibility(View.GONE);
                                            }
                                            return true;
                                        }
                                    });

                                    alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            dialog.dismiss();
                                            v_spaceView.setVisibility(View.GONE);
                                        }
                                    });

                                    v_spaceView.setVisibility(View.VISIBLE);
									/*Window window = alert.getWindow();
									WindowManager.LayoutParams wlp = window.getAttributes();

									wlp.gravity = Gravity.TOP;
									wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
									window.setAttributes(wlp);
*/
                                    // goToPhotoActivity();

                                }
                                else if (previousReadingValue > newMeterReading)
                                {
                                    Constants.KEY_METER_READING_TYPE = Constants.KEY_IMPLAUSIBLE;
                                    MobiculeLogger.verbose("Negative consumption");
                                    commonConfimrationDialog1(
                                            "Please tap 'OK' for next step or 'Cancel' to re-verify meter reading."
                                                    + "\r\n"
                                                    + this.getString(R.string.please_tap_OK_for_next_step_or_Cancel_to_re_verify_meter_reading),
                                            "NegativeConsumption");

									/*	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
										imm.hideSoftInputFromWindow(meterreadingEdittext.getWindowToken(), 0);*/

                                }
                                else if (newDiffMeterReading > yHigh)
                                {
                                    Constants.KEY_METER_READING_TYPE = Constants.KEY_IMPLAUSIBLE;
                                    MobiculeLogger.verbose("High consumption");
                                    commonConfimrationDialog1(
                                            "Please tap 'OK' for next step or 'Cancel' to re-verify meter reading."
                                                    + "\r\n"
                                                    + this.getString(R.string.please_tap_OK_for_next_step_or_Cancel_to_re_verify_meter_reading),
                                            "HighConsumption");
                                }//Please reverify meter number and Enter proper meter reading
                                else if (newDiffMeterReading < yLow)
                                {
                                    Constants.KEY_METER_READING_TYPE = Constants.KEY_IMPLAUSIBLE;
                                    MobiculeLogger.verbose("Low consumption");
                                    commonConfimrationDialog1(
                                            "Please tap 'OK' for next step or 'Cancel' to re-verify meter reading."
                                                    + "\r\n"
                                                    + this.getString(R.string.please_tap_OK_for_next_step_or_Cancel_to_re_verify_meter_reading),
                                            "LowConsumption");
                                }
                                else
                                {

                                    Constants.KEY_METER_READING_TYPE = Constants.KEY_PLAUSIBLE;

                                    goToPhotoActivity();

                                    // Removed Pop Up
									/*AlertDialog.Builder builder = new AlertDialog.Builder(MeterReading.this);
															builder.setCancelable(true);

															builder.setMessage("Please tap 'OK' for next step or 'Cancel' to re-verify meter reading"
															+ "\r\n"
															+ this.getString(R.string.please_tap_OK_for_next_step_or_Cancel_to_re_verify_meter_reading));
															builder.setInverseBackgroundForced(true);
															builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
															{
															@Override
															public void onClick(DialogInterface dialog, int which)
															{

															goToPhotoActivity();

															}
															});
															builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
															{
															@Override
															public void onClick(DialogInterface dialog, int which)
															{
															dialog.dismiss();
															}
															});
															AlertDialog alert = builder.create();
															alert.show();

															for (int i = 0; i < concateArray.length; i++)
															{
															Log.d("MeterReading", "concateArray :" + concateArray[i]);
															}

															*/
                                }
                            }
                            else
                            {
                                for (int i = 0; i < concateArray.length; i++)
                                {
                                    MobiculeLogger.verbose("MeterReading - concateArray :" + concateArray[i]);
                                }

                                goToPhotoActivity();
                            }

                        }
                    }
                }

                break;

            case R.id.cancel_button:

				/*Intent intent = new Intent(MeterReading.this, CustomerInformation.class);
				startActivity(intent);*/
                finish();

                break;

            case R.id.groupInfoImageLayout:

                String[] codesList = {};

				/*showCustomAlertDialog(codesList,
						MeterReadingNotesListsConstants.getGroupInHindi(getApplicationContext()),
						MeterReadingNotesListsConstants.groupList, "Group");*/
                showCustomAlertDialog(MeterReadingNotesListsConstants.groupCodeList,
                        MeterReadingNotesListsConstants.getGroupInHindi(this),
                        MeterReadingNotesListsConstants.groupList, "Group");

                break;

            case R.id.codeInfoImageLayout:

                String selectedGroupFromSpinner = groupSpinner.getSelectedItem().toString();

				/*if (selectedGroupFromSpinner.equals(MeterReadingNotesListsConstants.groupList[0]))
				{
					showCustomAlertDialog(MeterReadingNotesListsConstants.meterNormalCodeList,
							MeterReadingNotesListsConstants.getMeterNormalCodeInHindi(getApplicationContext()),
							MeterReadingNotesListsConstants.meterNormalReasonsList, "Code");
				}
				else if (selectedGroupFromSpinner.equals(MeterReadingNotesListsConstants.groupList[1]))
				{
					showCustomAlertDialog(MeterReadingNotesListsConstants.readingNotPossibleCodeList,
							MeterReadingNotesListsConstants.getReadingNotPossibleCodeInHindi(this),
							MeterReadingNotesListsConstants.readingNotPossibleReasonsList, "Code");
				}
				else if (selectedGroupFromSpinner.equals(MeterReadingNotesListsConstants.groupList[2]))
				{
					showCustomAlertDialog(MeterReadingNotesListsConstants.premisesLockedCodeList,
							MeterReadingNotesListsConstants.getPremisesLockedCodeInHindi(this),
							MeterReadingNotesListsConstants.premisesLockedReasonList, "Code");
				}
				else if (selectedGroupFromSpinner.equals(MeterReadingNotesListsConstants.groupList[3]))
				{
					showCustomAlertDialog(MeterReadingNotesListsConstants.permissionNotObtainedCodeList,
							MeterReadingNotesListsConstants.getPermissionNotObtainedCodeInHindi(this),
							MeterReadingNotesListsConstants.permissionNotObtainedReasonList, "Code");
				}
				else if (selectedGroupFromSpinner.equals(MeterReadingNotesListsConstants.groupList[4]))
				{
					showCustomAlertDialog(MeterReadingNotesListsConstants.buildingUnderConstructionCodeList,
							MeterReadingNotesListsConstants.getbuildingUnderConstructionCodeInHindi(this),
							MeterReadingNotesListsConstants.buildingUnderConstructionReasonList, "Code");
				}
				else if (selectedGroupFromSpinner.equals(MeterReadingNotesListsConstants.groupList[5]))
				{
					showCustomAlertDialog(MeterReadingNotesListsConstants.buildingDemolishedCodeList,
							MeterReadingNotesListsConstants.getBuildingDemolishedCodeInHindi(this),
							MeterReadingNotesListsConstants.buildingDemolishedReasonList, "Code");
				}
				else if (selectedGroupFromSpinner.equals(MeterReadingNotesListsConstants.groupList[6]))
				{
					showCustomAlertDialog(MeterReadingNotesListsConstants.meterAddNotFoundCodeList,
							MeterReadingNotesListsConstants.getAdderessNotFoundCodeInHindi(this),
							MeterReadingNotesListsConstants.meterAddnotFoundReasonList, "Code");
				}
				else if (selectedGroupFromSpinner.equals(MeterReadingNotesListsConstants.groupList[7]))
				{
					showCustomAlertDialog(MeterReadingNotesListsConstants.letterToCustomerCodeList,
							MeterReadingNotesListsConstants.getLetterGivenCodeInHindi(this),
							MeterReadingNotesListsConstants.letterToCustomerReasonList, "Code");
				}*/

                if (selectedGroupFromSpinner.equals(groupSpinnerReasonAndCodeList[0]))
                {
                    showCustomAlertDialog(MeterReadingNotesListsConstants.meterAddNotFoundCodeList,
                            MeterReadingNotesListsConstants.getAdderessNotFoundCodeInHindi(this),
                            MeterReadingNotesListsConstants.meterAddnotFoundReasonList, "Code");
                }
                else if (selectedGroupFromSpinner.equals(groupSpinnerReasonAndCodeList[1]))
                {
                    showCustomAlertDialog(MeterReadingNotesListsConstants.buildingDemolishedCodeList,
                            MeterReadingNotesListsConstants.getBuildingDemolishedCodeInHindi(this),
                            MeterReadingNotesListsConstants.buildingDemolishedReasonList, "Code");
                }
                else if (selectedGroupFromSpinner.equals(groupSpinnerReasonAndCodeList[2]))
                {
                    showCustomAlertDialog(MeterReadingNotesListsConstants.buildingUnderConstructionCodeList,
                            MeterReadingNotesListsConstants.getbuildingUnderConstructionCodeInHindi(this),
                            MeterReadingNotesListsConstants.buildingUnderConstructionReasonList, "Code");
                }
                else if (selectedGroupFromSpinner.equals(groupSpinnerReasonAndCodeList[3]))
                {
                    showCustomAlertDialog(MeterReadingNotesListsConstants.permissionNotObtainedCodeList,
                            MeterReadingNotesListsConstants.getPermissionNotObtainedCodeInHindi(this),
                            MeterReadingNotesListsConstants.permissionNotObtainedReasonList, "Code");
                }
                else if (selectedGroupFromSpinner.equals(groupSpinnerReasonAndCodeList[4]))
                {
                    showCustomAlertDialog(MeterReadingNotesListsConstants.premisesLockedCodeList,
                            MeterReadingNotesListsConstants.getPremisesLockedCodeInHindi(this),
                            MeterReadingNotesListsConstants.premisesLockedReasonList, "Code");
                }
                else if (selectedGroupFromSpinner.equals(groupSpinnerReasonAndCodeList[5]))
                {
                    showCustomAlertDialog(MeterReadingNotesListsConstants.readingNotPossibleCodeList,
                            MeterReadingNotesListsConstants.getReadingNotPossibleCodeInHindi(this),
                            MeterReadingNotesListsConstants.readingNotPossibleReasonsList, "Code");
                }
                else if (selectedGroupFromSpinner.equals(groupSpinnerReasonAndCodeList[6]))
                {
                    showCustomAlertDialog(MeterReadingNotesListsConstants.meterPossibleCodeList,
                            MeterReadingNotesListsConstants.getMeterPossibleCodeInHindi(getApplicationContext()),
                            MeterReadingNotesListsConstants.meterPossibleReasonList, "Code");
                }
                else if (selectedGroupFromSpinner.equals(groupSpinnerReasonAndCodeList[7]))
                {
                    showCustomAlertDialog(MeterReadingNotesListsConstants.letterToCustomerCodeList,
                            MeterReadingNotesListsConstants.getLetterGivenCodeInHindi(this),
                            MeterReadingNotesListsConstants.letterToCustomerReasonList, "Code");
                }

                break;

            case R.id.messageInfoImageLayout:

                String selectedCodeFromSpinner = spinner.getSelectedItem().toString();

                String selectedCode = selectedCodeFromSpinner.replaceAll("[^0-9]", "");

                String[] msgList = meterReaderMsg.getMessageList(selectedCode);

                String[] codesForMessageList = new String[] { "" };

                MobiculeLogger.verbose("MeterReading - selectedCode : " + selectedCode);

                for (int i = 0; i < msgList.length; i++)
                {
                    MobiculeLogger.verbose("msgList :" + msgList[i]);
                }

                MobiculeLogger.verbose("MessageInHindiList : "+ MeterReadingNotesListsConstants.getMessageInHindiList(getApplicationContext(),selectedCode));

                showCustomAlertDialog(codesForMessageList,
                        MeterReadingNotesListsConstants.getMessageInHindiList(getApplicationContext(), selectedCode),
                        msgList, "Message");

                break;
/*
			case R.id.rb_yes:
				relocation = "Yes";
				//meterReadingBO.setIsRelocation(relocation);
				break;

			case R.id.rb_no:
				relocation = "No";
				//meterReadingBO.setIsRelocation(relocation);
				break;*/

			/*case R.id.imageyesrelocation:
				relocation = "Yes";
				yesrelocation.setImageDrawable(this.getResources().getDrawable(R.drawable.selected_radio_button_40x40));
				norelocation.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				isYesrelocation=true;
				isNorelocation=false;
				break;

			case R.id.imagenorelocation:
				relocation = "No";

				yesrelocation.setImageDrawable(this.getResources().getDrawable(R.drawable.unselected_radio_button_40x40));
				norelocation.setImageDrawable(this.getResources().getDrawable(R.drawable.selected_radio_button_40x40));

				isYesrelocation=false;
				isNorelocation=true;
				break;*/
        }
    }



    @Override
    protected void onResume()
    {
        super.onResume();
        MobiculeLogger.info("OnResume... MeterReading...");
    }
}