package com.mobicule.android.msales.mgl.commons.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.sax.StartElementListener;
import android.webkit.WebView;
import android.widget.Toast;

import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.menu.view.MainMenu;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.component.util.CoreConstants;
import com.mobicule.crashnotifier.GenericExceptionHandler;
import com.mobicule.crashnotifier.IGenericExceptionHandler;
import com.mobicule.msales.mgl.client.common.IParser;
import com.mobicule.msales.mgl.client.common.Parser;
import com.mobicule.versioncontrol.VersionControl;

public class ApplicationAsk extends AsyncTask<Integer, Object, Boolean> {
    private Activity activity;

    private ApplicationService applicationService;

    private static final String FIELD_VERSION_CHECK = "versionCheck";

    private static String NETWORK_ERROR = "There seems to be a Network Issue. Please try after some time";

    IGenericExceptionHandler handler;

    /**
     * @return the applicationService
     */
    public ApplicationService getApplicationService() {
        return applicationService;
    }

    /**
     * @param applicationService the applicationService to set
     */
    public void setApplicationService(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    protected ProgressDialog progressDialog;

    private String message;

    private static IParser parserInstance;

    private boolean showLoader = true;

    public ApplicationAsk(Activity activity, ApplicationService applicationService) {
        this.activity = activity;
        this.applicationService = applicationService;

    }

    public ApplicationAsk(Activity activity, ApplicationService applicationService, boolean showLoader) {
        this.activity = activity;
        this.applicationService = applicationService;
        this.showLoader = showLoader;
    }

    public ApplicationAsk(Activity context) {
        this.activity = context;
        IGenericExceptionHandler handler = GenericExceptionHandler.sharedInstance(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (activity != null) {
            /*
             * progressDialog = new ProgressDialog(activity);
             * this.progressDialog.setMessage("Loading...");
             * this.progressDialog.show();
             */
            if (showLoader) {
                progressDialog = ProgressDialog.show(activity, "", "Loading...", false, false);
            }
        }
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        MobiculeLogger.verbose("app ask onPostExecute ... s", "result : " + result);

        Constants.IS_SYNC_RUNNING = false;

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        if (message != null && message.equalsIgnoreCase("CHANGE")) {
            showVersionDialog();
        } else {

            if (parserResponse()) {
                if (result) {
                    applicationService.postExecute();
                } else {
                    if (activity == null) {
                        return;
                    }
                    if (message == null || message.equals("")) {
                        message = NETWORK_ERROR;
                    }
                    showErrorDialog(message);
                }
            }
        }
    }

    protected Boolean doInBackground(Integer... params) {
        try {
            applicationService.execute();

        } catch (Throwable e) {
            MobiculeLogger.verbose("ApplicationAsk", "catch from applicationAsk layer");
            MobiculeLogger.verbose("doInBackground", e.getMessage() + " return " + false);
            message = e.getMessage();
            //handler.logCrashReport(e);
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public void showErrorDialog(final String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_dialog_alert);
        if (message.equals(NETWORK_ERROR)) {
            builder.setTitle("Network Error");
            builder.setMessage(msg);

        } else if (msg.equalsIgnoreCase(Constants.ALERT_NOT_ASSIGNED_BOOKWALK)) {
            builder.setTitle("Bookwalk Sequence Status");
            builder.setMessage(msg);

        } else if (message.equalsIgnoreCase("CHANGE")) {
            builder.setTitle("");
            builder.setMessage(msg);

        } else if (message.equalsIgnoreCase("Attempt to read from null array")) {
            builder.setTitle("Data Not Assigned");
            builder.setMessage("Kindly Contact For Data Allocation");
        }else if(message.equalsIgnoreCase("Attempt to invoke virtual method 'int java.lang.String.length()' on a null object reference")){
            builder.setTitle("Data Not Assigned");
            builder.setMessage("Kindly Contact For Data Allocation");
        }
        else {
            builder.setTitle("Error Occured");
            builder.setMessage(msg);

        }

        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean parserResponse() {
        boolean flag = true;
        try {
            if (activity == null) {
                return true;
            }
            /**
             * We set new parser instance every time a new request is sent to
             * server where the response from the server is set. Here, we get
             * previous instance of parser to retrieve the response.
             */
            parserInstance = Parser.getPreviousInstance();
            // " parserInstance.getResponse().getMessage() "+parserInstance.getResponse().getMessage());
            if (parserInstance != null && parserInstance.getResponseStatus().toUpperCase().indexOf("MINOR") != -1) {
                flag = false;
                AlertDialog.Builder submitCompleteDialog = new AlertDialog.Builder(activity);
                submitCompleteDialog.setTitle("Upgrade Minor");
                submitCompleteDialog.setCancelable(false);
                setPositiveButton("Yes", submitCompleteDialog);
                submitCompleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetParserInstance();
                        dialog.dismiss();
                    }
                });
                submitCompleteDialog.show();
            } else if (parserInstance != null && parserInstance.getResponseStatus().toUpperCase().indexOf("MAJOR") != -1) {
                flag = false;
                AlertDialog.Builder submitCompleteDialog = new AlertDialog.Builder(activity);
                submitCompleteDialog.setCancelable(false);
                submitCompleteDialog.setTitle("Upgrade Major");
                setPositiveButton("Ok", submitCompleteDialog);
                submitCompleteDialog.show();
            } else if (parserInstance != null
                    && parserInstance.getResponseStatus().indexOf(IParser.AUTHENTICATION_FAILURE) != -1) {

                if (parserInstance.getResponse().getMessage().equalsIgnoreCase(Constants.ALERT_MSG_BOOKWALK_SYNC_AGAIN)) {
                    Constants.isbookwalkSyncAgain = true;
                    Toast.makeText(activity, Constants.ALERT_MSG_BOOKWALK_SYNC_AGAIN, Toast.LENGTH_LONG).show();
                } else {

                    flag = false;
                    AlertDialog.Builder submitCompleteDialog = new AlertDialog.Builder(activity);
                    submitCompleteDialog.setCancelable(false);
                    submitCompleteDialog.setTitle("Failure");
                    submitCompleteDialog.setMessage(parserInstance.getResponse().getMessage());
                    submitCompleteDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resetParserInstance();
                            dialog.dismiss();
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction("com.mobicule.ACTION_LOGOUT");
                            activity.sendBroadcast(broadcastIntent);
                        }
                    });
                    submitCompleteDialog.show();
                }
            } else {
                resetParserInstance();
            }

        } catch (Exception e) {
            handler.logCrashReport(e);
            flag = true;
            e.printStackTrace();
        }
        MobiculeLogger.verbose("app ask...s ", "flag ... " + flag);
        return flag;

    }

    private void resetParserInstance() {
        parserInstance = null;
        Parser.setPreviousInstance(null);
    }

    private void setPositiveButton(String positiveButton, AlertDialog.Builder submitCompleteDialog) {
        if (message.equalsIgnoreCase("CHANGE")) {
            submitCompleteDialog
                    .setMessage("There is a new Version of the Application available. Kindly click isInternetOn OK to download the same.");
        } else {
            submitCompleteDialog.setMessage(parserInstance.getResponse().getMessage());
        }
        submitCompleteDialog.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                dialog.dismiss();

                if (message.equalsIgnoreCase("CHANGE")) {
                    openBrowser();
                    message = "";
                    resetParserInstance();
                } else {
                    MobiculeLogger.verbose("setPositiveButton   ", "DATA---====" + parserInstance.getResponse().getData().toString());
                    openBrowser(parserInstance.getResponse().getData().toString());
                }
                resetParserInstance();
            }
        });
    }

    public void openBrowser() {
        if (CoreConstants.SYNC_URL != null && !CoreConstants.SYNC_URL.equals("")) {

            ((Activity) activity).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CoreConstants.SYNC_URL)));

        }
    }

    private void showVersionDialog() {
        //flag = false;
        AlertDialog.Builder submitCompleteDialog = new AlertDialog.Builder(activity);
        submitCompleteDialog.setCancelable(false);
        submitCompleteDialog.setTitle("Version Update");
        setPositiveButton("Ok", submitCompleteDialog);
        submitCompleteDialog.show();
    }

    public void openBrowser(String url) {
        /*
         * String url; try {
         */
        /*
         * url = new JSONObject(data).getJSONObject("data").getString("url");
         *
         * ((Activity) activity).startActivity(new Intent(Intent.ACTION_VIEW,
         * Uri.parse(url)));
         */

        WebView webView = new WebView(activity);
        webView.getSettings().setJavaScriptEnabled(true);
        MobiculeLogger.verbose("openbrowser with url", url);
        webView.loadUrl(url);
        activity.setContentView(webView);

        /*
         * } catch (JSONException e) {
         *
         * e.printStackTrace(); }
         */
    }
}
