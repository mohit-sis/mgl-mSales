/**
 *
 */
package com.mobicule.msales.mgl.client.meterreading;

import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;


import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

//import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.component.json.parser.JSONParser;
import com.mobicule.component.string.StringUtil;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.component.util.CoreMobiculeLogger;
import com.mobicule.component.util.LogFile;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.common.StringHeaderRequestBuilder;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IMeterReadingInstance;
import com.mobicule.versioncontrol.VersionControl;

/**
 * @author nikita
 */
public class DefaultMeterReadingFacade implements IMeterReadingFacade {

    private final IApplicationFacade applicationFacade;

    private final IMeterReadingPersistance meterReadingPersistance;

    public static  IMeterReadingCommunication meterReadingCommunication;

    private StringMeterReadingRequestBuilder meterReadingRequestBuilder;

    private IMeterReadingInstance currentMeterReading;

    private static DefaultMeterReadingFacade instance;

    private IMeterReading meterReading = null;

    private VersionControl versionControl;

    JSONParser jsonParser;

    private DefaultMeterReadingFacade(IMeterReadingPersistance meterReadingPersistance,
                                      IMeterReadingCommunication meterReadingCommunication, IApplicationFacade applicationFacade,
                                      VersionControl versionControl) {
        initMeterReadingCycle();
        this.meterReadingPersistance = meterReadingPersistance;
        this.meterReadingCommunication = meterReadingCommunication;
        this.applicationFacade = applicationFacade;
        jsonParser = JSONParser.getInstance();
        this.versionControl = versionControl;

    }

    public synchronized static IMeterReadingFacade getInstance(IMeterReadingPersistance meterReadingPersistance,
                                                               IMeterReadingCommunication meterReadingCommunication, IApplicationFacade applicationFacade,
                                                               VersionControl versionControl) {
        if (instance == null) {
            instance = new DefaultMeterReadingFacade(meterReadingPersistance, meterReadingCommunication,
                    applicationFacade, versionControl);
        }
        return instance;
    }

    private synchronized void initMeterReadingCycle() {
        currentMeterReading = new DefaultMeterReading.DefaultMeterReadingInstance();
    }

    public synchronized void initMeterReadingCycleWithSavedMeterReading(String savedMeterReading) {
        try {
            getCurrentMeterReadingBO().init(new JSONObject(savedMeterReading));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public synchronized IMeterReadingInstance getCurrentMeterReadingBO() {
        if (currentMeterReading == null) {
            initMeterReadingCycle();
        }
        return currentMeterReading;
    }

	/*public synchronized Response submitMeterReading()
	{
		JSONObject jsonObject;
		System.out.println("Submit meter reading");
		Response response = applicationFacade.getUserDtail();
		try
		{
			jsonObject = new JSONObject(response.getData().toString());

			Vector savedMeterReadings = fetchAllSavedMeterReadings();
			if (savedMeterReadings != null && savedMeterReadings.size() > 0)
			{
				JSONObject savedJson = null;
				for (int i = 0; i < savedMeterReadings.size(); i++)
				{
					savedJson = new JSONObject(savedMeterReadings.elementAt(i).toString());
					getCurrentMeterReadingBO().init(savedJson);
					setMeterReadingInstance(getCurrentMeterReadingBO());
				}
				meterReadingRequestBuilder = new ScheduledMeterReadingRequestBuilder(jsonObject, meterReading);
				response = meterReadingCommunication.submit(meterReadingRequestBuilder.build());

				if (response.isSuccess())
				{
					editSavedMeterReading(true);
					getMeterReading().reset();
					return new Response(true, response.getMessage(), null);
				}
				else
				{
					getMeterReading().reset();
					return new Response(false, response.getMessage(), null);
				}
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return new Response(false, CoreConstants.EMPTY_STRING, null);

	}
	*/

    public synchronized Response submitOneMeterReading(String meterReading) {
        JSONObject jsonObject;
        Response response = applicationFacade.getUserDtail();
        try {
            jsonObject = new JSONObject(response.getData().toString());
            if (meterReading != null && meterReading.length() > 0) {
                StringBuffer meterReadingBuffer = new StringBuffer();

                meterReadingBuffer.append('[');
                meterReadingBuffer.append(meterReading);
                //meterReadingBuffer.append(',');
                meterReadingBuffer.append(']');

                CoreMobiculeLogger.log("submitMeterReading	:	StringBuffer:" + meterReading.toString());

                meterReadingRequestBuilder = new StringScheduledMeterReadingRequestBuilder(jsonObject,
                        meterReadingBuffer.toString());
                CoreMobiculeLogger.log("submitMeterReading	:	meterReadingRequestBuilder:" + meterReadingRequestBuilder.build().toString());

                JSONObject jsonSubmit = new JSONObject(meterReadingRequestBuilder.build());

                JSONObject jsonRequest = versionControl.attachVersionControlInfo(jsonSubmit);
                response = meterReadingCommunication.submitString(jsonRequest.toString());

				/*JSONObject jsonMR = new JSONObject(response.getData().toString());

				String status = jsonMR.getString(CoreConstants.USER_RESPONSE_STATUS);

				if (response.isSuccess() == false && status.equalsIgnoreCase(CoreConstants.VERSION_CHANGE))
				{
					return new Response(false, StringUtil.getMessage(response.getMessage()), response.getData());
				}
				else*/
                if (response.isSuccess()) {
                    //editSavedMeterReading(true, meterReading);
                    //getMeterReading().reset();
                    return new Response(true, response.getMessage(), null);
                } else {
                    return new Response(false, response.getMessage(), null);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //return new Response(false, CoreConstants.EMPTY_STRING, null);
        return new Response(true, CoreConstants.EMPTY_STRING, null);

    }

    public synchronized Response submitMeterReading() {
        JSONObject jsonObject;
        Response response = applicationFacade.getUserDtail();
        //String testJson = new FileReader().read(getClass().getResourceAsStream("/testJson.txt"));
        try {
            jsonObject = new JSONObject(response.getData().toString());
            //Vector savedMeterReadings = fetchAllSavedMeterReadings();
            Vector savedMeterReadings = fetchAllSavedMeterReadingsOnlyMroNums();

            if (savedMeterReadings != null && savedMeterReadings.size() > 0) {
                StringBuffer meterReading = new StringBuffer();
                meterReading.append('[');
                for (int i = 0; i < savedMeterReadings.size(); i++) {
                    jsonParser.setJson(savedMeterReadings.elementAt(i).toString());
                    String mroNumber = jsonParser.getValue("mroNo");
                    String oneMeterReading = "";
                    Vector oneMeterReadingVector = fetchSavedMeterReading(mroNumber);
                    if (oneMeterReadingVector != null && oneMeterReadingVector.size() > 0) {
                        oneMeterReading = fetchSavedMeterReading(mroNumber).elementAt(0).toString();
                    } else {
                        CoreMobiculeLogger.log("NOT FOUND - MRO NO : " + mroNumber + " | UNABLE TO FETCH METER READING | submitMeterReading()");
                        continue;
                    }
                    meterReading.append(oneMeterReading);

                    //meterReading.append(savedMeterReadings.elementAt(i).toString());
                    if (i != (savedMeterReadings.size() - 1)) {
                        meterReading.append(',');
                    }
                }
                meterReading.append(']');

                CoreMobiculeLogger.log("submitMeterReading	:	StringBuffer:" + meterReading.toString());

                meterReadingRequestBuilder = new StringScheduledMeterReadingRequestBuilder(jsonObject,
                        meterReading.toString());

                CoreMobiculeLogger.log("submitMeterReading	:	meterReadingRequestBuilder:" + meterReadingRequestBuilder.build().toString());

                JSONObject jsonSubmit = new JSONObject(meterReadingRequestBuilder.build());

                CoreMobiculeLogger.log("JSON SUBMIT " + jsonSubmit.toString());

                JSONObject jsonRequest = versionControl.attachVersionControlInfo(jsonSubmit);

                response = meterReadingCommunication.submitString(jsonRequest.toString());

                CoreMobiculeLogger.log("response VALUE  " + response);

                if (response.isSuccess()) {
                    editSavedMeterReading(true);
                    getMeterReading().reset();
                    return new Response(true, response.getMessage(), null);
                } else {
                    getMeterReading().reset();
                    return new Response(false, response.getMessage(), null);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //return new Response(false, CoreConstants.EMPTY_STRING, null);
        return new Response(true, CoreConstants.EMPTY_STRING, null);

    }

    public Response submitOneMeterReadingFromRecevier() {
        Vector isSuccessSubmitV = null;

        try {
            if (CoreConstants.isAutotriggerOn()) {
                return new Response(false, CoreConstants.EMPTY_STRING, null);
            }
            //Vector savedMeterReadings = fetchAllSavedMeterReadings();
            Vector savedMeterReadings = fetchAllSavedMeterReadingsOnlyMroNums();

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
                    Vector oneMeterReadingVector = fetchSavedMeterReading(mroNumber);

                    CoreMobiculeLogger.log("oneMeterReadingVector :: " + oneMeterReadingVector.toString());

                    if (oneMeterReadingVector != null && oneMeterReadingVector.size() > 0) {
                        if (fetchSavedMeterReading(mroNumber).size() == 0) {
                            CoreMobiculeLogger.log("No Data Found in vector");
                        } else {
                            oneMeterReading = fetchSavedMeterReading(mroNumber).elementAt(0).toString();
                        }
                    } else {
                        CoreMobiculeLogger.log("NOT FOUND - MRO NO : " + mroNumber + " |  UNABLE TO FETCH METER READING | submitOneMeterReadingFromRecevier()");
                        continue;
                    }

                    CoreMobiculeLogger.log(i + "     One meter reading Json before submit    : " + oneMeterReading);

                    meterReadingBuffer.append('[');
                    meterReadingBuffer.append(oneMeterReading);
                    //meterReadingBuffer.append(',');
                    //meterReadingBuffer.append();
                    meterReadingBuffer.append(']');

                    CoreMobiculeLogger.log("submitOneMeterReadingFromRecevier" + meterReadingBuffer.toString());

                    meterReadingRequestBuilder = new StringScheduledMeterReadingRequestBuilder(userJson, meterReadingBuffer.toString());

                    CoreMobiculeLogger.log("submitOneMeterReadingFromRecevier	:	meterReadingRequestBuilder:" + meterReadingRequestBuilder.build().toString());

                    JSONObject jsonSubmit = new JSONObject(meterReadingRequestBuilder.build());

                    JSONObject jsonRequest = versionControl.attachVersionControlInfo(jsonSubmit);

                    CoreMobiculeLogger.log("JSON REQUEST " + jsonRequest);


                    response = meterReadingCommunication.submitString(jsonRequest.toString());


                    CoreMobiculeLogger.log("*****Meter reading response after submit    : " + response);

                    if (response.isSuccess()) {
                        CoreMobiculeLogger.log("*****submitOneMeterReadingFromRecevier : success : " + response.isSuccess());

                        isSuccessSubmitV.addElement("true");
                        editSavedMeterReading(true, oneMeterReading);
                    } else {
                        CoreMobiculeLogger.log("*****submitOneMeterReadingFromRecevier : error message : " + response.getMessage() + " error data : " + response.getData());

                        isSuccessSubmitV.addElement("false");
                        editSavedMeterReading(false, oneMeterReading);

                        //return new Response(false, StringUtil.getMessage(response.getMessage()), response.getData());
                    }

                    CoreMobiculeLogger.log(i + " One meter reading Json after submit    : " + oneMeterReading);

                    if (CoreConstants.isAutotriggerOn()) {
                        break;
                    }
					if (i % 100 == 0)
					{
						try
						{
							Thread.sleep(2000);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
                    meterReadingBuffer.setLength(0);
                    //meterReadingBuffer.delete(0, meterReadingBuffer.length());
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

    private synchronized void editSavedMeterReading(boolean isReadingSubmittedToServer) {
        try {
            //Vector savedMeterReadings = fetchAllSavedMeterReadings();
            Vector savedMeterReadings = fetchAllSavedMeterReadingsOnlyMroNums();

            JSONObject savedJson = null;
            for (int i = 0; i < savedMeterReadings.size(); i++) {
                //savedJson = new JSONObject(savedMeterReadings.elementAt(i).toString());

                jsonParser.setJson(savedMeterReadings.elementAt(i).toString());
                String mroNumber = jsonParser.getValue("mroNo");
                String oneMeterReading = "";
                Vector oneMeterReadingVector = fetchSavedMeterReading(mroNumber);
                if (oneMeterReadingVector != null && oneMeterReadingVector.size() > 0) {
                    oneMeterReading = fetchSavedMeterReading(mroNumber).elementAt(0).toString();
                } else {
                    CoreMobiculeLogger.log("NOT FOUND - MRO NO : " + mroNumber + " |  UNABLE TO FETCH METER READING | editSavedMeterReading(boolean)");
                    continue;
                }
                savedJson = new JSONObject(oneMeterReading);

                getCurrentMeterReadingBO().init(savedJson);
                if (isReadingSubmittedToServer) {
                    getCurrentMeterReadingBO().setMrSubmitted("1");
                    //-----------------
                    meterReadingPersistance.deleteMeterReading(CoreConstants.TABLE_SAVED_METER_READING,
                            getCurrentMeterReadingBO().toJSON().toString());
                    //------------------
                } else {
                    getCurrentMeterReadingBO().setMrSubmitted("0");//saved by default performed
                    meterReadingPersistance.saveMeterReading(CoreConstants.TABLE_SAVED_METER_READING,
                            getCurrentMeterReadingBO().toJSON().toString());

                }

//				meterReadingPersistance.saveMeterReading(CoreConstants.TABLE_SAVED_METER_READING,
//						getCurrentMeterReadingBO().toJSON().toString());
//
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void editSavedMeterReading(boolean isReadingSubmittedToServer, String meterReading) {
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

    public synchronized Vector fetchAllSavedMeterReadings() {
        return meterReadingPersistance.fetchAllSavedMeterReadings(CoreConstants.TABLE_SAVED_METER_READING);
    }

    public synchronized Vector fetchAllSavedMeterReadingsOnlyMroNums() {
        return meterReadingPersistance.fetchAllSavedMeterReadingsOnlyMroNums(CoreConstants.TABLE_SAVED_METER_READING);
    }

    public synchronized Response saveMeterReading(boolean isReadingSubmittedToServer) {
        if (isReadingSubmittedToServer) {
            getCurrentMeterReadingBO().setMrSubmitted("1");
        } else {
            getCurrentMeterReadingBO().setMrSubmitted("0");//saved by default
        }
        String saveMeterReadingJsonValue = getCurrentMeterReadingBO().toJSON().toString();

        CoreMobiculeLogger.log("saveMeterReading CurrentMeterBo    " + saveMeterReadingJsonValue);

        if (StringUtil.isValid(saveMeterReadingJsonValue)) {
            boolean saveMeterReadingStatus = meterReadingPersistance.saveMeterReading(
                    CoreConstants.TABLE_SAVED_METER_READING, saveMeterReadingJsonValue);

            CoreMobiculeLogger.log("saveMeterReading saveMeterReadingStatus    " + saveMeterReadingStatus);

            if (saveMeterReadingStatus) {
                if (meterReadingPersistance.searchSaveMeterReadingStatusByMro(CoreConstants.TABLE_SAVED_METER_READING,
                        saveMeterReadingJsonValue)) {
                    applicationFacade.updateBookWalkStatus(saveMeterReadingJsonValue);
                }
            }
        }

        return new Response(true, "Meter reading saved successfully", null);
    }

    public synchronized Response saveMeterReading(String meterReading, boolean isReadingSubmittedToServer) {
        try {
            JSONObject jsonObject = new JSONObject(meterReading);
            if (isReadingSubmittedToServer) {
                jsonObject.put(CoreConstants.KEY_MR_SUBMIT, "1");
            } else {
                jsonObject.put(CoreConstants.KEY_MR_SUBMIT, "0");
            }
            String saveMeterReadingJsonValue = jsonObject.toString();

            CoreMobiculeLogger.log("saveMeterReading meterReading:" + saveMeterReadingJsonValue);

            LogFile.writeToFile("**** DefaultMeterReadingFacade : In SavedMeterReading() : saveMeterReadingJsonValue -> " + saveMeterReadingJsonValue);


            if (StringUtil.isValid(saveMeterReadingJsonValue)) {
                boolean saveMeterReadingStatus = meterReadingPersistance.saveMeterReading(
                        CoreConstants.TABLE_SAVED_METER_READING, saveMeterReadingJsonValue);

                LogFile.writeToFile("**** DefaultMeterReadingFacade : In savedMeterReading() : MeterReadingPersistence -> " + meterReadingPersistance);
                LogFile.writeToFile("**** DefaultMeterReadingFacade : In savedMeterReading() : saveMeterReadingStatus -> " + saveMeterReadingStatus);

                if (saveMeterReadingStatus) {
                    applicationFacade.updateBookWalkStatus(saveMeterReadingJsonValue);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogFile.exceptionToFile();
        }
        return new Response(true, "Meter reading saved successfully", null);
    }

    public synchronized Vector fetchSavedMeterReading(String mroNO) {
        return meterReadingPersistance.fetchSavedMeterReading(CoreConstants.TABLE_SAVED_METER_READING, mroNO);
    }

    //This method is for getting total number of customer counts from persistance
    public synchronized String getCustomerCount(JSONArray jsonCustomerCount) {
        return null;
    }

    public synchronized IMeterReading setMeterReadingInstance(IMeterReadingInstance instance) {
        getMeterReading().setMeterReadingInstance(instance);
        return meterReading;
    }

    public synchronized IMeterReading getMeterReading() {
        if (meterReading == null) {
            meterReading = new DefaultMeterReading();
        }
        return meterReading;
    }

    public synchronized IMeterReadingInstance resetMeterReadingInstanceBO() {
        initMeterReadingCycle();
        return currentMeterReading;
    }

    public synchronized Response getSavedBuildingList() {
        Vector data = meterReadingPersistance.getSavedBuildingList();
        if (data != null && data.size() > 0) {
            return new Response(true, "", data);
        }
        return new Response(false, "", null);
    }

    public synchronized Response getSavedCustomerList(String connObj) {
        Vector data = meterReadingPersistance.getSavedCustomerList(connObj);
        if (data != null && data.size() > 0) {
            return new Response(true, "", data);
        }
        return new Response(false, "", null);
    }

    @Override
    public boolean isCommercialCustomer(IMeterReadingInstance meterReadingBO) {
        if (null == meterReadingBO) {
            return false;
        }
        String mruCodePrefix = meterReadingBO.getMruCode();
        CoreMobiculeLogger.log("String mruCodePrefix  :::::" + mruCodePrefix);

        if (null == mruCodePrefix || mruCodePrefix.trim().equals(CoreConstants.EMPTY_STRING)) {
            return false;
            //CoreMobiculeLogger.log("String mruCodePrefix  :::::Fasle ::"+mruCodePrefix);
        }

        mruCodePrefix = mruCodePrefix.substring(0, 3);
        if (mruCodePrefix.equalsIgnoreCase(CoreConstants.COM_MRU_CODE)) {
            return true;
            //CoreMobiculeLogger.log("String mruCodePrefix  :::::COM_MRU_CODE "+mruCodePrefix);
        } else {
            return false;
        }
    }

}
