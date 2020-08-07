package com.mobicule.android.msales.mgl.sign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.jointicketing.view.DefaultJoinTicketingActivity;
import com.mobicule.android.msales.mgl.jointicketing.view.JoinTicketingFinalSummary;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.android.msales.mgl.util.FileUtil;
import com.mobicule.crashnotifier.GenericExceptionHandler;
import com.mobicule.crashnotifier.IGenericExceptionHandler;

import java.io.File;

public class SignatureActivity extends DefaultJoinTicketingActivity
{

	GraphicView graphicView;

	public static int width;

	public static int height;

	private int signCount = 0;

	private String selectedCustomerJson;
	
	private Button saveButton, refreshButton;
	
	LinearLayout signatureViewLayout;
	
	IGenericExceptionHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		try
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

			String extraStr_customerSummary = "selectedCustomerJSON";
			Bundle bundle = getIntent().getExtras();

			if (getIntent().hasExtra(extraStr_customerSummary))
			{
				selectedCustomerJson = bundle.getString(extraStr_customerSummary);
				MobiculeLogger.verbose("selectedCustomerJson from customer Summary:" + selectedCustomerJson.toString());
			}
			
			setContentView(R.layout.signature_layout);
			
			signatureViewLayout = (LinearLayout) findViewById(R.id.signatureLayoutView);

			Display display = getWindowManager().getDefaultDisplay();

			width = display.getWidth();
			height = display.getHeight();
			joinTicketingCommonDialog("Take Meter Reader's Signature On Screen");

			graphicView = new GraphicView(this, true);
			
			signatureViewLayout.addView(graphicView);
			
			//handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
			
			saveButton = (Button) findViewById(R.id.save_button);
			saveButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					if (graphicView.save())
					{
						if (signCount == 0)
						{
							joinTicketingBO.setMeterReaderSign(graphicView.getSignatureImage());

							buildImageJsonAndSaveInDb("METER_READER_SINGNATURE", graphicView.getSignatureImage(), "");

							graphicView.refreshView();
							signCount = 1;
							Toast.makeText(SignatureActivity.this, "Meter Reader's Signature Saved", Toast.LENGTH_SHORT).show();
							joinTicketingCommonDialog("Take Customer's Signature On Screen");
						}
						else if (signCount == 1)
						{
							joinTicketingBO.setClientSign(graphicView.getSignatureImage());

							buildImageJsonAndSaveInDb("CLIENT_SIGNATURE_PATH", graphicView.getSignatureImage(), "");

							Toast.makeText(SignatureActivity.this, "Client's Signature Saved", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(SignatureActivity.this, JoinTicketingFinalSummary.class);
							intent.putExtra("selectedCustomerJSON", selectedCustomerJson);
							startActivity(intent);
							finish();
						}
					}
					
				}
			});
			
			
			refreshButton = (Button) findViewById(R.id.refresh_button);
			refreshButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					graphicView.refreshView();
				}
			});

			
		}
		catch (Exception e)
		{
			FileUtil.writeToFile("//SignatureActivity : Exception :: " + e);

			handler.logCrashReport(e);
			e.printStackTrace();
		}
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add("Refresh");
		menu.add("Save");

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		if (item.getTitle().toString().equals("Refresh"))
		{
			graphicView.refreshView();
		}
		else if (item.getTitle().toString().equals("Save"))
		{
			if (graphicView.save())
			{
				if (signCount == 0)
				{
					joinTicketingBO.setMeterReaderSign(graphicView.getSignatureImage());

					buildImageJsonAndSaveInDb("METER_READER_SINGNATURE", graphicView.getSignatureImage(), "");

					graphicView.refreshView();
					signCount = 1;
					Toast.makeText(this, "Meter Reader's Signature Saved", Toast.LENGTH_SHORT).show();
					joinTicketingCommonDialog("Take Customer's Signature On Screen");
				}
				else if (signCount == 1)
				{
					joinTicketingBO.setClientSign(graphicView.getSignatureImage());

					buildImageJsonAndSaveInDb("CLIENT_SIGNATURE_PATH", graphicView.getSignatureImage(), "");

					Toast.makeText(this, "Client's Signature Saved", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(this, JoinTicketingFinalSummary.class);
					intent.putExtra("selectedCustomerJSON", selectedCustomerJson);
					startActivity(intent);
					finish();
				}
			}
		}
		return super.onOptionsItemSelected(item);
	}*/

	public void joinTicketingCommonDialog(final String msg)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setInverseBackgroundForced(true);
		builder.setMessage(msg);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
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

	@Override
	public void onBackPressed()
	{
	}

}
