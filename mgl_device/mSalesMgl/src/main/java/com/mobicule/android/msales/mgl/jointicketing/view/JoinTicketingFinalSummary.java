package com.mobicule.android.msales.mgl.jointicketing.view;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.me.JSONObject;

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
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.meterreading.view.CustomerInformation;
import com.mobicule.android.msales.mgl.meterreading.view.LocationSensor;
import com.mobicule.android.msales.mgl.meterreading.view.MeterReadingFeild;
import com.mobicule.android.msales.mgl.mybookwalk.view.MyBookWalk;
import com.mobicule.android.msales.mgl.util.Base64;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.component.string.StringUtil;

public class JoinTicketingFinalSummary extends DefaultJoinTicketingActivity
{
	private final String TAG = "JoinTicketingFinalSummary";

	private ImageView imgReaderSign, imgClientSign;

	private TextView txtTurbineMeterRea, txtCorrVol, txtUnCorrVol, txtOutletPressure, txtCorrectedFactor, txtTemp,
			txtRemarks, bpNumberText, meterNumberTextView, customerNameText, contactNumberTextView, addressText,
			caNumberTextView, mroNumberText, custlandLineNo, custOfficeNo;

	private TextView textViewMruCode;

	private TextView flatTextView;

	private TextView floorTextView;

	private TextView plotTextView;

	private TextView wingTextView;

	private byte[] bitmapdataReaderSign, bitmapdataClientSign;

	private String selectedCustomerJson;

	private Button submitButton;

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

		setContentView(R.layout.join_ticketing_final_summary);
		MeterReadingFeild.init();
		String extraStr_customerSummary = "selectedCustomerJSON";
		Bundle bundle = getIntent().getExtras();

		if (getIntent().hasExtra(extraStr_customerSummary))
		{
			selectedCustomerJson = bundle.getString(extraStr_customerSummary);
			MobiculeLogger.verbose("selectedCustomerJson from customer Summary:" + selectedCustomerJson.toString());
		}
		init();
		setData();
	}

	private void init()
	{
		imgReaderSign = (ImageView) findViewById(R.id.imgViewReaderSign);
		imgClientSign = (ImageView) findViewById(R.id.imgViewClientSign);
		txtTurbineMeterRea = (TextView) findViewById(R.id.summaryTurbineMeterRead);
		txtCorrVol = (TextView) findViewById(R.id.summaryCorrectedVol);
		txtUnCorrVol = (TextView) findViewById(R.id.summaryUnCorrectedVol);
		txtOutletPressure = (TextView) findViewById(R.id.summaryOutletPressure);
		txtTemp = (TextView) findViewById(R.id.summaryTemperature);
		txtCorrectedFactor = (TextView) findViewById(R.id.summaryCorrectionFac);
		txtRemarks = (TextView) findViewById(R.id.summaryRemarks);
		bpNumberText = (TextView) findViewById(R.id.bpNumSummary);

		meterNumberTextView = (TextView) findViewById(R.id.meterNumSummary);

		customerNameText = (TextView) findViewById(R.id.custNameSummary);

		contactNumberTextView = (TextView) findViewById(R.id.custContactNumber);

		caNumberTextView = (TextView) findViewById(R.id.custcanumber);

		addressText = (TextView) findViewById(R.id.custAddressSummary);

		flatTextView = (TextView) findViewById(R.id.custflatSummary);

		floorTextView = (TextView) findViewById(R.id.custfloorSummary);

		plotTextView = (TextView) findViewById(R.id.custplotSummary);

		wingTextView = (TextView) findViewById(R.id.custwingSummary);

		mroNumberText = (TextView) findViewById(R.id.mroNumSummary);

		custlandLineNo = (TextView) findViewById(R.id.cust_landline_Summary);

		custOfficeNo = (TextView) findViewById(R.id.cust_office_Summary);

		textViewMruCode = (TextView) findViewById(R.id.mruNumSummary);
		
		submitButton = (Button) findViewById(R.id.submit_button); 
		submitButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				submit();
			}
		});
	}

	private void setData()
	{
		try
		{

			txtTurbineMeterRea.setText(joinTicketingBO.getTurbineMeterReading());
			txtCorrVol.setText(joinTicketingBO.getCorrectedValue());
			txtUnCorrVol.setText(joinTicketingBO.getUnCorrectedValue());
			txtOutletPressure.setText(joinTicketingBO.getOutletPressure());
			txtTemp.setText(joinTicketingBO.getTemperature());
			txtCorrectedFactor.setText(joinTicketingBO.getCorrectionFactor());
			txtRemarks.setText(joinTicketingBO.getJoinRemarks());

			JSONObject jsonoCustomerDetails = new JSONObject(selectedCustomerJson);
			MobiculeLogger.verbose("jsonoCustomerDetails" + selectedCustomerJson);
			String mroNumber = jsonoCustomerDetails.getValue(Constants.KEY_MRO_NUMBER).toString();
			joinTicketingBO.setMroNo(mroNumber);
			if (jsonoCustomerDetails.has(Constants.FIELD_CONN_OBJ))
			{
				joinTicketingBO.setConnObj(jsonoCustomerDetails.get(Constants.FIELD_CONN_OBJ).toString());
			}
			bpNumberText.setText(jsonoCustomerDetails.get(Constants.KEY_BP_NUMBER).toString());
			mroNumberText.setText(jsonoCustomerDetails.get(Constants.KEY_MRO_NUMBER).toString());
			meterNumberTextView.setText(jsonoCustomerDetails.getValue(Constants.KEY_CUSTOMER_METER_NUMBER).toString());
			customerNameText.setText(jsonoCustomerDetails.getValue(Constants.KEY_CUSTOMER_NAME).toString());
			contactNumberTextView.setText(jsonoCustomerDetails.getValue(Constants.KEY_CUSTOMER_CONTACT_NUMBER)
					.toString());
			String landlineNo = jsonoCustomerDetails.getValue(Constants.KEY_CUSTOMER_LANDLINE_NUMBER).toString();
			String officeNo = jsonoCustomerDetails.getValue(Constants.KEY_CUSTOMER_OFFICE_NUMBER).toString();

			textViewMruCode.setText(jsonoCustomerDetails.getValue(Constants.KEY_MRU_CODE).toString());

			if (StringUtil.isValid(landlineNo))
			{
				custlandLineNo.setText(landlineNo);
			}
			else
			{
				custlandLineNo.setText("");
			}
			if (StringUtil.isValid(officeNo))
			{
				custOfficeNo.setText(officeNo);
			}
			else
			{
				custOfficeNo.setText("");
			}

			addressText.setText(jsonoCustomerDetails.getValue(Constants.KEY_CUSTOMER_ADDRESS).toString());
			caNumberTextView.setText(jsonoCustomerDetails.getValue(Constants.KEY_CUSTOMER_CA_NUMBER).toString());
			flatTextView.setText(jsonoCustomerDetails.getValue(getString(R.string.key_field_flat)).toString());
			floorTextView.setText(jsonoCustomerDetails.getValue(getString(R.string.key_field_floor)).toString());
			plotTextView.setText(jsonoCustomerDetails.getValue(getString(R.string.key_field_plot)).toString());
			wingTextView.setText(jsonoCustomerDetails.getValue(getString(R.string.key_field_wing)).toString());

			bitmapdataReaderSign = Base64.decode(joinTicketingBO.getMeterReaderSign());
			if (bitmapdataReaderSign != null && bitmapdataReaderSign.length > 0)
			{

				Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdataReaderSign, 0, bitmapdataReaderSign.length);
				imgReaderSign.setImageBitmap(bitmap);
			}
			bitmapdataClientSign = Base64.decode(joinTicketingBO.getClientSign());
			if (bitmapdataClientSign != null && bitmapdataClientSign.length > 0)
			{

				Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdataClientSign, 0, bitmapdataClientSign.length);
				imgClientSign.setImageBitmap(bitmap);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(Constants.SUBMIT);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getTitle().equals(Constants.SUBMIT))
		{
			submit();
		}

		return super.onOptionsItemSelected(item);
	}*/

	private void submit()
	{
		joinTicketingBO.setMeterReaderSign("");
		joinTicketingBO.setClientSign("");

		new ApplicationAsk(JoinTicketingFinalSummary.this, new ApplicationService()
		{
			@Override
			public void postExecute()
			{
				MobiculeLogger.verbose("post execute async task");
				AlertDialog.Builder saveDialog = new AlertDialog.Builder(JoinTicketingFinalSummary.this);
				saveDialog.setCancelable(false);
				saveDialog.setTitle("Submit Join Ticketing");
				saveDialog.setMessage(response.getMessage());
				saveDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						Intent broadcastIntent = new Intent();
						broadcastIntent.setAction("com.mobicule.ACTION_JOIN_TICKETING");
						sendBroadcast(broadcastIntent);
						MobiculeLogger.verbose("flag is   " + Constants.searchSelection);
						if (Constants.searchSelection == true)
						{
							startActivity(new Intent(JoinTicketingFinalSummary.this, MyBookWalk.class));
							finish();
						}
						else
						{
							startActivity(new Intent(JoinTicketingFinalSummary.this, CustomerInformation.class));
							finish();
						}
					}
				});
				saveDialog.show();
			}

			@Override
			public void execute()
			{
				MobiculeLogger.verbose("execute async task");
				MobiculeLogger.verbose("JOIN TICKETING BO: "+ joinTicketingBO.toJSON().toString());

				joinTicketingBO.setBpNo(bpNumberText.getText().toString().trim());
				joinTicketingBO.setMroNo(mroNumberText.getText().toString().trim());
				joinTicketingBO.setMeterNumber(meterNumberTextView.getText().toString().trim());
				joinTicketingBO.setStatus(Constants.FIELD_COMPLETED);
				joinTicketingBO.setConnObj(Constants.JoinConnObj);
				joinTicketingBO.setArea(LocationSensor.getArea());

				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
				String deviceDate = format1.format(new Date());
				String deviceTime = format2.format(new Date());

				joinTicketingBO.setDeviceDate(deviceDate);
				joinTicketingBO.setDeviceTime(deviceTime);

				response = joinTicketingFacade.saveJoinTicketing(false);
				joinTicketingFacade.saveJoinTicketingImages(joinTicketingBO.getMroNo().toString());

				joinTicketingFacade.resetJoinTicketingBO();// tushar

			}
		}).execute();
	}

	@Override
	public void onBackPressed()
	{
		/*super.onBackPressed();		

		joinTicketingFacade.deleteFromJoinTicketingImage("");		
		
		Intent i = new Intent(JoinTicketingFinalSummary.this, CustomerSummary.class);
		startActivity(i);*/
	}
}
