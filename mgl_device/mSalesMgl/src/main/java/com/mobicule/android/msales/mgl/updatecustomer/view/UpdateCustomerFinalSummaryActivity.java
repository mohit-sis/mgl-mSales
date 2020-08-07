package com.mobicule.android.msales.mgl.updatecustomer.view;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahanagar.R;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.jointicketing.view.JoinTicketing;
import com.mobicule.android.msales.mgl.meterreading.view.CustomerSummary;
import com.mobicule.android.msales.mgl.meterreading.view.MeterReading;
import com.mobicule.android.msales.mgl.mybookwalk.view.MyBookWalk;
import com.mobicule.android.msales.mgl.util.Base64;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.android.msales.mgl.util.FileUtil;

import java.io.File;

public class UpdateCustomerFinalSummaryActivity extends DefaultUpdateCustomerActivity
{
	private TextView bpNo, mroNo, flatNo, floorNo, plotNo, wingNo, buildName, customerUpdateRemark, docType;

	private ImageView docImgOne, docImgTwo, docImgThree;
	
	private String selectedCustomerJson;

	private Button updateButton;

	private Button cancelButton;

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

		setContentView(R.layout.activity_update_customer_final_summary);

		if(getIntent().hasExtra("selectedCustomerJSON")){
			Bundle extras = getIntent().getExtras();
			selectedCustomerJson = extras.getString("selectedCustomerJSON");
		}

		
		initalization();

		setData();
	}

	private void initalization()
	{
		bpNo = (TextView) findViewById(R.id.bpNumber);
		mroNo = (TextView) findViewById(R.id.mroNumber);
		//customerName = (TextView) findViewById(R.id.customerName);
		//meterNo = (TextView) findViewById(R.id.meterNo);
		flatNo = (TextView) findViewById(R.id.flatNo);
		floorNo = (TextView) findViewById(R.id.floorNo);
		plotNo = (TextView) findViewById(R.id.plotNo);
		wingNo = (TextView) findViewById(R.id.wingNo);
		buildName = (TextView) findViewById(R.id.buildName);
		//contactNoMobile = (TextView) findViewById(R.id.contactMobileNo);
		//contactNoHome = (TextView) findViewById(R.id.contactHomeNo);
		//contactNoOffice = (TextView) findViewById(R.id.contactOfficeNo);
		customerUpdateRemark = (TextView) findViewById(R.id.customerUpdateRemark);
		docType = (TextView) findViewById(R.id.docType);

		docImgOne = (ImageView) findViewById(R.id.docImgOne);
		docImgTwo = (ImageView) findViewById(R.id.docImgTwo);
		docImgThree = (ImageView) findViewById(R.id.docImgThree);
		
		updateButton = (Button) findViewById(R.id.updateButton);
		updateButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				new ApplicationAsk(UpdateCustomerFinalSummaryActivity.this, new ApplicationService()
				{

					@Override
					public void postExecute()
					{
						AlertDialog.Builder updateDialog = new AlertDialog.Builder(UpdateCustomerFinalSummaryActivity.this);
						updateDialog.setCancelable(false);
						updateDialog.setTitle("Update Address Details");
						if(response.isSuccess())
						{
							updateDialog.setMessage("Address Update Request sent successfully");
						}
						else 
						{
							updateDialog.setMessage(response.getMessage());
						}
						
						updateDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
								Intent broadcastIntent = new Intent();
								broadcastIntent.setAction("com.mobicule.ACTION_MY_BOOK_WALK");
								sendBroadcast(broadcastIntent);
								JSONObject selectedCustomerJsonObject;
								String mrucode = null;
								try
								{
									selectedCustomerJsonObject = new JSONObject(selectedCustomerJson);
									 mrucode = selectedCustomerJsonObject.getString(Constants.KEY_MRU_CODE);
								}
								catch (JSONException e)
								{
									// TODO Auto-generated catch block

									handler.logCrashReport(e);
									e.printStackTrace();

									FileUtil.writeToFile("//UpdateCustomerFinalSummaryActivity : Exception :: " + e);

								}
								
								if ((mrucode.equalsIgnoreCase("COMBA003")) || (mrucode.equalsIgnoreCase("COMCA001"))
										|| (mrucode.equalsIgnoreCase("INDA001") || (mrucode.equalsIgnoreCase("COMAC003"))))
								{
									Intent intent = new Intent(UpdateCustomerFinalSummaryActivity.this, UpdateCustomerInfoActivity.class);
									intent.putExtra("selectedCustomerJSON", selectedCustomerJson);
									startActivity(intent);
									finish();
								
								}else{
									Intent intent = new Intent(UpdateCustomerFinalSummaryActivity.this, UpdateCustomerInfoActivity.class);
									intent.putExtra("updateCustomer", selectedCustomerJson);
									//intent.putExtra("selectedCustomerJSON", selectedCustomerJson);
									intent.putExtra("selectedCustomerJSON", selectedCustomerJson);
									startActivity(intent);
									finish();
								}
							}
						});
						updateDialog.show();
					}

					@Override
					public void execute()
					{
						response = updateCustomerFacade.saveCustomerUpdate(false);
					}
				}).execute();
				
			}
		});
		
		cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}

	private void setData()
	{
		bpNo.setText(updateCustomerBO.getBPNumber());
		mroNo.setText(updateCustomerBO.getMroNo());
		//customerName.setText(updateCustomerBO.getNewCustomerName());
		//meterNo.setText(updateCustomerBO.getNewMeterNO());
		flatNo.setText(updateCustomerBO.getNewFlatNumber());
		floorNo.setText(updateCustomerBO.getNewFloor());
		plotNo.setText(updateCustomerBO.getNewPlot());
		wingNo.setText(updateCustomerBO.getNewWing());
		buildName.setText(updateCustomerBO.getNewBuildName());
		//contactNoMobile.setText(updateCustomerBO.getNewContactNoMobile());
		//contactNoHome.setText(updateCustomerBO.getNewContactNoHome());
		//contactNoOffice.setText(updateCustomerBO.getNewContactNoOffice());
		customerUpdateRemark.setText(updateCustomerBO.getRemark());
		docType.setText(updateCustomerBO.getDocType());

		try
		{
			byte[] docImageByte = Base64.decode(updateCustomerBO.getDocImgOne());
			if (docImageByte != null && docImageByte.length > 0)
			{
				Bitmap bitmap = BitmapFactory.decodeByteArray(docImageByte, 0, docImageByte.length);
				docImgOne.setImageBitmap(bitmap);
			}

			byte[] docImageByte1 = Base64.decode(updateCustomerBO.getDocImgTwo());
			if (docImageByte1 != null && docImageByte1.length > 0)
			{
				findViewById(R.id.docImgTwoText).setVisibility(View.VISIBLE);
				docImgTwo.setVisibility(View.VISIBLE);
				
				Bitmap bitmap = BitmapFactory.decodeByteArray(docImageByte1, 0, docImageByte1.length);
				docImgTwo.setImageBitmap(bitmap);
			}

			byte[] docImageByte2 = Base64.decode(updateCustomerBO.getDocImgThree());
			if (docImageByte2 != null && docImageByte2.length > 0)
			{
				findViewById(R.id.docImgThreeText).setVisibility(View.VISIBLE);
				docImgThree.setVisibility(View.VISIBLE);
				
				Bitmap bitmap = BitmapFactory.decodeByteArray(docImageByte2, 0, docImageByte2.length);
				docImgThree.setImageBitmap(bitmap);
			}
		}
		catch (Exception e)
		{

			handler.logCrashReport(e);
			e.printStackTrace();
		}

	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add("Update");
		menu.add("Cancel");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getTitle().equals("Update"))
		{
			new ApplicationAsk(UpdateCustomerFinalSummaryActivity.this, new ApplicationService()
			{

				@Override
				public void postExecute()
				{
					AlertDialog.Builder updateDialog = new AlertDialog.Builder(UpdateCustomerFinalSummaryActivity.this);
					updateDialog.setCancelable(false);
					updateDialog.setTitle("Update Customer Details");
					updateDialog.setMessage(response.getMessage());
					updateDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
							Intent broadcastIntent = new Intent();
							broadcastIntent.setAction("com.mobicule.ACTION_MY_BOOK_WALK");
							sendBroadcast(broadcastIntent);

							Intent intent = new Intent(UpdateCustomerFinalSummaryActivity.this, MeterReading.class);
							intent.putExtra("selectedCustomerJSON", selectedCustomerJson);
							startActivity(intent);
							finish();
						}
					});
					updateDialog.show();
				}

				@Override
				public void execute()
				{
					response = updateCustomerFacade.saveCustomerUpdate(false);
				}
			}).execute();
		}
		else if (item.getTitle().equals("Cancel"))
		{
			finish();

		}
		return super.onOptionsItemSelected(item);
	}*/

}
