package com.mobicule.android.msales.mgl.savedmetereading.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.commons.model.IOCContainer;
import com.mobicule.android.msales.mgl.menu.view.MainMenu;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.component.util.CoreMobiculeLogger;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.meterreading.DefaultMeterReadingFacade;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingFacade;
import com.mobicule.msales.mgl.client.meterreading.StringMeterReadingRequestBuilder;
import com.mobicule.msales.mgl.client.meterreading.StringScheduledMeterReadingRequestBuilder;
import com.mobicule.versioncontrol.VersionControl;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import static com.mobicule.msales.mgl.client.meterreading.DefaultMeterReadingFacade.meterReadingCommunication;

/**
 * <enter description here>
 *
 * @author Sathya Sheela
 * @createdOn April 6, 2012
 * @modifiedOn
 * @copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
 * @see
 */
public class SavedBuildingListActivity extends DefaultSavedMeterReadingActivity implements OnClickListener {
    private ListView bookwalkList;

    private static SimpleAdapter adapter;

    private static ArrayList<HashMap<String, String>> list;

    private String[] headerList = new String[]{"con_name", "con_address", "con_location"};

    private int[] resource = new int[]{R.id.txt_user_info_header, R.id.txt_user_info_detail,
            R.id.txt_user_info_flatno};

    private String connObj = "";

    private Context context;

    private VersionControl versionControl;

    private Vector buildingList;

    private Button submitAll;

    protected IMeterReadingFacade meterReadingFacade;

    private LinearLayout buttonLayout;

    ProgressDialog progressDialog;

    //-----------------------------
    private StringMeterReadingRequestBuilder meterReadingRequestBuilder;
    JSONObject jsonRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        setContentView(R.layout.bookwalksequence);
        context = this;
        //VersionControl.init(context);

        meterReadingFacade = (IMeterReadingFacade) IOCContainer.getInstance(getApplicationContext()).getBean(
                "DefaultMeterReadingFacade");
        versionControl = (VersionControl) IOCContainer.getInstance(this).getBean(IOCContainer.VERSION_CONTROL);

        meterReadingBO.reset();

        meterReadingBO.resetReadingList();

        meterReadingFacade.getMeterReading().reset();

        list = new ArrayList<HashMap<String, String>>();

        adapter = new SimpleAdapter(this, list, R.layout.userinfo_row, headerList, resource);

        bookwalkList = (ListView) findViewById(android.R.id.list);

        bookwalkList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
                new ApplicationAsk(SavedBuildingListActivity.this, new ApplicationService() {

                    @Override
                    public void postExecute() {
                        Intent intent = new Intent(SavedBuildingListActivity.this, SavedCustomerInformation.class);
                        startActivity(intent);
                    }

                    @Override
                    public void execute() {
                        jsonParser.setJson(buildingList.elementAt(position).toString());

                        connObj = jsonParser.getValue(Constants.FIELD_CONN_OBJ);
                        MobiculeLogger.verbose("" + buildingList.elementAt(position).toString());
                        meterReadingBO.setConnObj(connObj);
                        response = meterReadingFacade.getSavedCustomerList(connObj);
                        MobiculeLogger.verbose("RESPONSE " + response);
                    }
                }).execute();
            }
        });
        initialise();

        buttonLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
        buttonLayout.setVisibility(View.VISIBLE);

        submitAll = (Button) findViewById(R.id.submit_all_button);
        submitAll.setOnClickListener(this);


    }

    private void initialise() {
        new ApplicationAsk(SavedBuildingListActivity.this, new ApplicationService() {
            @Override
            public void postExecute() {
                populateList();
                bookwalkList.setAdapter(adapter);
            }

            @Override
            public void execute() {
                response = meterReadingFacade.getSavedBuildingList();
            }
        }).execute();
    }

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add("Submit All");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getTitle().equals("Submit All"))
		{
			// startService(new Intent(new SavedBuildingListActivity(),
			// BackgroundService.class));

			new ApplicationAsk(this, new ApplicationService()
			{
				@Override
				public void postExecute()
				{
					try
					{
						if (response.isSuccess() == false && response.getData() != null)
						{
							String status = new JSONObject(response.getData().toString())
									.getString(CoreConstants.USER_RESPONSE_STATUS);
							if (status.equalsIgnoreCase(CoreConstants.VERSION_CHANGE))
							{
								String url = new JSONObject(response.getData().toString()).getJSONObject(
										CoreConstants.KEY_DATA).getString("url");
								VersionControl.downloadNewBuild(context, url, false);
							}
							
						}
						else{
							
							finish();
						}
						
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
					
					 * if (responseRMR.isSuccess()) {
					 * databaseManager.dropTable(TABLE_RANDOM_METER_READING); }
					 * if (responseUpdate.isSuccess()) {
					 * databaseManager.dropTable(TABLE_CUSTOMER_UPDATE); }
					 
				//	finish();
				}

				@Override
				public void execute()
				{
					response = meterReadingFacade.submitOneMeterReadingFromRecevier();
					// responseRMR =
					// randomMeterReadingFacade.submitRandomMeterReading();
					// responseUpdate =
					// updateCustomerFacade.updateCustomerDetails();
				}
			}).execute();
		}
		return super.onOptionsItemSelected(item);
	}*/

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Constants.broadcastDialogContext = this;
    }

    private void populateList() {
        if (response != null && response.getData() != null) {
            HashMap<String, String> temp;
            list.clear();
            buildingList = (Vector) response.getData();
            for (int i = 0; i < buildingList.size(); i++) {
                jsonParser.setJson(buildingList.elementAt(i).toString());
                temp = new HashMap<String, String>();
                temp.put(
                        headerList[0],
                        jsonParser.getValue(Constants.KEY_CONN_NAME) + " [ "
                                + jsonParser.getValue(Constants.FIELD_CUSTOMER_COUNT) + " ] ");
                temp.put(headerList[1], jsonParser.getValue(Constants.KEY_CONN_ADDRESS));
                temp.put(headerList[2], jsonParser.getValue(Constants.KEY_LOCATION));
                temp.put(Constants.FIELD_CONN_OBJ, jsonParser.getValue(Constants.FIELD_CONN_OBJ));
                list.add(temp);
            }
        } else {
            AlertDialog.Builder buildingListDialog = new AlertDialog.Builder(SavedBuildingListActivity.this);
            buildingListDialog.setCancelable(false);
            buildingListDialog.setTitle("Saved Meter Reading");
            buildingListDialog.setMessage("Sorry! No data found.");
            buildingListDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            buildingListDialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_all_button) {

            // startService(new Intent(new SavedBuildingListActivity(),
            // BackgroundService.class));
            progressDialog = ProgressDialog.show(this, "", "Loading...", false, false);

            new ApplicationAsk(this, new ApplicationService() {
                @Override
                public void postExecute() {
                    try {
                        if (response.isSuccess() == false && response.getData() != null) {
                            String status = new JSONObject(response.getData().toString())
                                    .getString(CoreConstants.USER_RESPONSE_STATUS);
                            if (status.equalsIgnoreCase(CoreConstants.VERSION_CHANGE)) {
                                String url = new JSONObject(response.getData().toString()).getJSONObject(
                                        CoreConstants.KEY_DATA).getString("url");
                                VersionControl.downloadNewBuild(context, url, false);
                            }

                        } else {

//                            finish();
                        }

                    } catch (JSONException e) {
                        handler.logCrashReport(e);
                        e.printStackTrace();
                    }
                    /*
                     * if (responseRMR.isSuccess()) {
                     * databaseManager.dropTable(TABLE_RANDOM_METER_READING); }
                     * if (responseUpdate.isSuccess()) {
                     * databaseManager.dropTable(TABLE_CUSTOMER_UPDATE); }
                     */
                    //	finish();
                }

                @Override
                public void execute() {

                    submitdataMultipleThread();
//                    response = meterReadingFacade.submitOneMeterReadingFromRecevier();
                    // responseRMR =
                    // randomMeterReadingFacade.submitRandomMeterReading();
                    // responseUpdate =
                    // updateCustomerFacade.updateCustomerDetails();

                }
            }, false).execute();
//			}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }


    //------------------------------------
    private Response submitdataMultipleThread() {

        {

            Vector isSuccessSubmitV = null;

            try {
                if (CoreConstants.isAutotriggerOn()) {
                    return new Response(false, CoreConstants.EMPTY_STRING, null);
                }
                //Vector savedMeterReadings = fetchAllSavedMeterReadings();
                Vector savedMeterReadings = meterReadingFacade.fetchAllSavedMeterReadingsOnlyMroNums();

                CoreMobiculeLogger.log("submitOneMeterReadingFromRecevier() : savedMeterReadings : " + savedMeterReadings.size());

                if (savedMeterReadings != null && savedMeterReadings.size() > 0) {
                    Response response = applicationFacade.getUserDtail();
                    JSONObject userJson = new JSONObject(response.getData().toString());

                    isSuccessSubmitV = new Vector();
                    StringBuffer meterReadingBuffer = new StringBuffer();

                    for (int i = 0; i < savedMeterReadings.size(); i++) {

                        //String oneMeterReading = savedMeterReadings.elementAt(i).toString();

                        jsonParser.setJson(savedMeterReadings.elementAt(i).toString());
                        String mroNumber = jsonParser.getValue("mroNo");

                        CoreMobiculeLogger.log("MRO NUMBER " + mroNumber);

                        String oneMeterReading = "";
                        Vector oneMeterReadingVector = meterReadingFacade.fetchSavedMeterReading(mroNumber);

                        CoreMobiculeLogger.log("oneMeterReadingVector :: " + oneMeterReadingVector.toString());

                        if (oneMeterReadingVector != null && oneMeterReadingVector.size() > 0) {
                            if (meterReadingFacade.fetchSavedMeterReading(mroNumber).size() == 0) {
                                CoreMobiculeLogger.log("No Data Found in vector");
                            } else {
                                oneMeterReading = meterReadingFacade.fetchSavedMeterReading(mroNumber).elementAt(0).toString();
                            }
                        } else {
                            CoreMobiculeLogger.log("NOT FOUND - MRO NO : " + mroNumber + " |  UNABLE TO FETCH METER READING | submitOneMeterReadingFromRecevier()");
                            continue;
                        }

                        CoreMobiculeLogger.log(i + "     One meter reading Json before submit    : " + oneMeterReading);

                        meterReadingBuffer.append('[');
                        meterReadingBuffer.append(oneMeterReading);
//                        meterReadingBuffer.append(',');
//                        meterReadingBuffer.append();
                        meterReadingBuffer.append(']');

                        CoreMobiculeLogger.log("submitOneMeterReadingFromRecevier" + meterReadingBuffer.toString());

                        meterReadingRequestBuilder = new StringScheduledMeterReadingRequestBuilder(userJson, meterReadingBuffer.toString());

                        CoreMobiculeLogger.log("submitOneMeterReadingFromRecevier	:	meterReadingRequestBuilder:" + meterReadingRequestBuilder.build().toString());

                        JSONObject jsonSubmit = new JSONObject(meterReadingRequestBuilder.build());

                        JSONObject jsonRequest = versionControl.attachVersionControlInfo(jsonSubmit);

                        CoreMobiculeLogger.log("JSON REQUEST " + jsonRequest);

                        new TaskMeterReading(i, jsonRequest, isSuccessSubmitV, oneMeterReading, meterReadingBuffer,savedMeterReadings.size()).execute();
                        meterReadingBuffer.setLength(0);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (isSuccessSubmitV != null && isSuccessSubmitV.size() > 0) {
                if (isSuccessSubmitV.contains("false")) {
                    return new Response(false, CoreConstants.EMPTY_STRING, null);
                } else {
                    return new Response(true, CoreConstants.EMPTY_STRING, null);
                }
            }

            return new Response(false, CoreConstants.EMPTY_STRING, null);
        }
    }


    public void editSavedMeterReading(boolean isReadingSubmittedToServer, String meterReading) {
        try {
            JSONObject jsonObject = new JSONObject(meterReading);

            CoreMobiculeLogger.log("METER READING SUBMITTED TO SERVER : " + isReadingSubmittedToServer);

            if (isReadingSubmittedToServer) {
                jsonObject.put(CoreConstants.KEY_MR_SUBMIT, "1");
//				meterReadingPersistance.deleteMeterReading(CoreConstants.TABLE_SAVED_METER_READING, jsonObject.toString());
            } else {
                jsonObject.put(CoreConstants.KEY_MR_SUBMIT, "0");
//				meterReadingPersistance.saveMeterReading(CoreConstants.TABLE_SAVED_METER_READING, jsonObject.toString());
            }
            meterReadingPersistance.saveMeterReading(CoreConstants.TABLE_SAVED_METER_READING, jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class TaskMeterReading extends AsyncTask {

        int i,size;
        JSONObject jsonRequest;
        Vector isSuccessSubmitV;
        String oneMeterReading;
        StringBuffer meterReadingBuffer;

        public TaskMeterReading(int i, JSONObject jsonRequest, Vector isSuccessSubmitV, String oneMeterReading, StringBuffer meterReadingBuffer,int size) {
            this.i = i;
            this.jsonRequest = jsonRequest;
            this.isSuccessSubmitV = isSuccessSubmitV;
            this.oneMeterReading = oneMeterReading;
            this.meterReadingBuffer = meterReadingBuffer;
            this.size = size;
        }


        @Override
        protected Response doInBackground(Object[] objects) {

            Response response = meterReadingCommunication.submitString(jsonRequest.toString());

            CoreMobiculeLogger.log("*****Meter reading response after submit    : " + response);

            if (response.isSuccess()) {

                CoreMobiculeLogger.log("*****submitOneMeterReadingFromRecevier : success : " + response.isSuccess());

                isSuccessSubmitV.addElement("true");
                editSavedMeterReading(true, oneMeterReading);
                if (progressDialog != null) {
                    if (i == (size-1)) {
                        progressDialog.dismiss();
                        finish();
                    }
                }
            } else {
                CoreMobiculeLogger.log("*****submitOneMeterReadingFromRecevier : error message : " + response.getMessage() + " error data : " + response.getData());

                isSuccessSubmitV.addElement("false");
                editSavedMeterReading(false, oneMeterReading);

                //return new Response(false, StringUtil.getMessage(response.getMessage()), response.getData());
            }
            if (progressDialog != null) {
                if (i == (size-1)) {
                    progressDialog.dismiss();
                }

            }

            CoreMobiculeLogger.log(i + " One meter reading Json after submit    : " + oneMeterReading);

            if (CoreConstants.isAutotriggerOn()) {
                onStop();
            }
//            if (i % 100 == 0) {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            meterReadingBuffer.setLength(0);
            //meterReadingBuffer.delete(0, meterReadingBuffer.length());

//            if (isSuccessSubmitV != null && isSuccessSubmitV.size() > 0) {
//                if (isSuccessSubmitV.contains("false")) {
//                    return new Response(false, CoreConstants.EMPTY_STRING, null);
//                } else {
//                    return new Response(true, CoreConstants.EMPTY_STRING, null);
//                }
//            }

            return null;
        }
    }


}
