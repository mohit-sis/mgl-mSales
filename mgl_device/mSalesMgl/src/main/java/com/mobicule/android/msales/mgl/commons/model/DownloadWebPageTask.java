package com.mobicule.android.msales.mgl.commons.model;


import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;

public class DownloadWebPageTask extends AsyncTask<String, Void, Integer>
{
	private ProgressDialog progressDialog;

	private ImageView internet, server;

	private String which;

	public static String getresponce;

	public DownloadWebPageTask(Context context, View view1, View view2, String which)
	{
		progressDialog = new ProgressDialog(context);
		this.internet = (ImageView) view1;
		this.server = (ImageView) view2;
		this.which = which;
	}

	@Override
	protected Integer doInBackground(String... urls)
	{
		int response = 0;
		for (String url : urls)
		{
			MobiculeLogger.verbose("Connecting.... " + url);
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is established.
			// The default value is zero, that means the timeout is not used. 
			int timeoutConnection = 10000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT) 
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = 10000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			HttpGet httpGet = new HttpGet(url);
			try
			{
				HttpResponse execute = client.execute(httpGet);
				response = execute.getStatusLine().getStatusCode();
				MobiculeLogger.verbose("Response : " + response);
				if (response == 200)
				{
					return response;
				}
				InputStream content = execute.getEntity().getContent();

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return response;
	}

	@Override
	protected void onPostExecute(Integer result)
	{
		progressDialog.cancel();
		super.onPostExecute(result);
		getresponce = "" + result;
		if (result == 200)
		{
			if (which.equals("internet"))
			{
				internet.setImageResource(R.drawable.radio_button_green);
			}
			else
			{
				server.setImageResource(R.drawable.radio_button_green);
			}
		}
		else
		{
			if (which.equals("internet"))
			{
				internet.setImageResource(R.drawable.radio_select);
			}
			else
			{
				server.setImageResource(R.drawable.radio_select);
			}
		}
	}

	@Override
	protected void onPreExecute()
	{
		progressDialog.setMessage("Processing....");
		progressDialog.show();
		super.onPreExecute();
	}

}
