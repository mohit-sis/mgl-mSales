package com.mobicule.android.msales.mgl.downloadbookwalk.model;

import android.app.Activity;
import android.app.ProgressDialog;

import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.msales.mgl.client.downloadbookwalk.interfaces.ISyncObserver;

/**
* 
* <enter description here>
*
* @author Sathya Sheela
* @see 
*
* @createdOn Mar 6, 2012
* @modifiedOn
*
* @copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class SyncTask extends ApplicationAsk implements ISyncObserver
{

	private Activity context;



	public SyncTask(Activity context, ApplicationService applicationService)
	{
		super(context, applicationService);
		this.context = context;
	}

	@Override
	protected void onProgressUpdate(Object... values)
	{
		super.onProgressUpdate(values);
		final String title = (String) values[0];
		((Activity) context).runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				progressDialog.setTitle(title.toUpperCase());
			}
		});

		progressDialog.setProgress((Integer) values[1]);

	}

	@Override
	protected void onPreExecute()
	{
		progressDialog = new ProgressDialog(context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMax(100);
		progressDialog.setCancelable(false);
		progressDialog.setTitle("Downloading");
		progressDialog.show();
	}

	@Override
	public void update(String entity, int progress)
	{
		onProgressUpdate(new Object[] { entity, progress });
	}
}