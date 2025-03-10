package com.mobicule.android.msales.mgl.jointicketing.view;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.meterreading.view.MeterReadingFeild;
import com.mobicule.android.msales.mgl.sign.SignatureActivity;
import com.mobicule.android.msales.mgl.util.Base64;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;

public class JoinTicketingSummary extends DefaultJoinTicketingActivity
{
	private ImageView imgMRS1, imgTurbine, imgSeal1, imgEvc1, imgEvc2, imgSeal2, imgMRS2, imgLeftSide, imgRightSide, imgRearSide;

	private TextView txtTurbineMeterRea, txtCorrVol, txtUnCorrVol, txtOutletPressure, txtCorrectedFactor, txtTemp,
			txtRemarks;

	private byte[] bitmapdataMRS1, bitmapdataTurbine, bitmapdataSeal1, bitmapdataEvc1, bitmapdataEvc2, bitmapdataSeal2,
			bitmapdataMRS2, bitmapdataLeftSideMeter, bitmapdataRightSideMeter, bitmapdataRearSideMeter;

	private String selectedCustomerJson;

	private TextView showRightSideImageRemarkText;

	private TextView showRearSideImageRemarkText;

	private TextView showLeftSideImageRemarkText;

	private Button signatureButton;
	
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

		setContentView(R.layout.join_ticketing_summary);
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
		imgMRS1 = (ImageView) findViewById(R.id.imgViewMRS1);
		imgTurbine = (ImageView) findViewById(R.id.imgViewTurbine);
		imgSeal1 = (ImageView) findViewById(R.id.imgViewSeal1);
		imgEvc1 = (ImageView) findViewById(R.id.imgViewEvc1);
		imgEvc2 = (ImageView) findViewById(R.id.imgViewEvc2);
		imgSeal2 = (ImageView) findViewById(R.id.imgViewSeal2);
		imgMRS2 = (ImageView) findViewById(R.id.imgViewMrs2);
		txtTurbineMeterRea = (TextView) findViewById(R.id.txt_turbineMeterRead);
		txtCorrVol = (TextView) findViewById(R.id.txt_CorrectedVol);
		txtUnCorrVol = (TextView) findViewById(R.id.txt_UnCorrectedVol);
		txtOutletPressure = (TextView) findViewById(R.id.txt_OutletPressure);
		txtTemp = (TextView) findViewById(R.id.txt_Temperature);
		txtCorrectedFactor = (TextView) findViewById(R.id.txt_CorrectionFac);
		txtRemarks = (TextView) findViewById(R.id.txt_Remarks);
		imgLeftSide = (ImageView) findViewById(R.id.imgViewMeterLeftSide);
		imgRightSide = (ImageView) findViewById(R.id.imgViewMeterRightSide);
		imgRearSide = (ImageView) findViewById(R.id.imgViewMeterRearSide);
		showRightSideImageRemarkText = (TextView) findViewById(R.id.showRightSideImageRemarkText);
		showRearSideImageRemarkText = (TextView) findViewById(R.id.showRearSideImageRemarkText);
		showLeftSideImageRemarkText = (TextView) findViewById(R.id.showLeftSideImageRemarkText);
		
		signatureButton = (Button) findViewById(R.id.signatureButton);
		signatureButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				joinTicketingBO.setMrsImage("");
				joinTicketingBO.setTurbineImage("");
				joinTicketingBO.setTurbineSealImage("");
				joinTicketingBO.setEvcImage1("");
				joinTicketingBO.setEvcImage2("");
				joinTicketingBO.setEvcSealImage("");
				joinTicketingBO.setMrsLockedImage("");

				Intent intent = new Intent(JoinTicketingSummary.this, SignatureActivity.class);
				intent.putExtra("selectedCustomerJSON", selectedCustomerJson);
				startActivity(intent);
				finish();
				
			}
		});
	}

	private void setData()
	{
		txtTurbineMeterRea.setText("Turbine Meter Reading : " + joinTicketingBO.getTurbineMeterReading());
		txtCorrVol.setText("Corrected Volume :" + joinTicketingBO.getCorrectedValue());
		txtUnCorrVol.setText("UnCorrected Volume :" + joinTicketingBO.getUnCorrectedValue());
		txtOutletPressure.setText("Outlet Pressure :" + joinTicketingBO.getOutletPressure());
		txtTemp.setText("Temperature: " + joinTicketingBO.getTemperature());
		txtCorrectedFactor.setText("Corrected Factor :" + joinTicketingBO.getCorrectionFactor());
		txtRemarks.setText("Remarks: " + joinTicketingBO.getJoinRemarks());

		try
		{
			bitmapdataMRS1 = Base64.decode(joinTicketingBO.getMrsImage());
			if (bitmapdataMRS1 != null && bitmapdataMRS1.length > 0)
			{

				Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdataMRS1, 0, bitmapdataMRS1.length);
				imgMRS1.setImageBitmap(bitmap);
			}
			bitmapdataTurbine = Base64.decode(joinTicketingBO.getTurbineImage());
			if (bitmapdataTurbine != null && bitmapdataTurbine.length > 0)
			{

				Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdataTurbine, 0, bitmapdataTurbine.length);
				imgTurbine.setImageBitmap(bitmap);
			}
			bitmapdataSeal1 = Base64.decode(joinTicketingBO.getTurbineSealImage());
			if (bitmapdataSeal1 != null && bitmapdataSeal1.length > 0)
			{

				Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdataSeal1, 0, bitmapdataSeal1.length);
				imgSeal1.setImageBitmap(bitmap);
			}
			bitmapdataEvc1 = Base64.decode(joinTicketingBO.getEvcImage1());
			if (bitmapdataEvc1 != null && bitmapdataEvc1.length > 0)
			{

				Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdataEvc1, 0, bitmapdataEvc1.length);
				imgEvc1.setImageBitmap(bitmap);
			}
			bitmapdataEvc2 = Base64.decode(joinTicketingBO.getEvcImage2());
			if (bitmapdataEvc2 != null && bitmapdataEvc2.length > 0)
			{

				Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdataEvc2, 0, bitmapdataEvc2.length);
				imgEvc2.setImageBitmap(bitmap);
			}
			bitmapdataSeal2 = Base64.decode(joinTicketingBO.getEvcSealImage());
			if (bitmapdataSeal2 != null && bitmapdataSeal2.length > 0)
			{

				Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdataSeal2, 0, bitmapdataSeal2.length);
				imgSeal2.setImageBitmap(bitmap);
			}
			bitmapdataMRS2 = Base64.decode(joinTicketingBO.getMrsLockedImage());
			if (bitmapdataMRS2 != null && bitmapdataMRS2.length > 0)
			{

				Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdataMRS2, 0, bitmapdataMRS2.length);
				imgMRS2.setImageBitmap(bitmap);
			}
			
			bitmapdataLeftSideMeter = Base64.decode(joinTicketingBO.getMeterBoxLeftSideImage());
			if (bitmapdataLeftSideMeter != null && bitmapdataLeftSideMeter.length > 0)
			{

				Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdataLeftSideMeter, 0, bitmapdataLeftSideMeter.length);
				imgLeftSide.setImageBitmap(bitmap);
				showLeftSideImageRemarkText.setText(joinTicketingBO.getMeterBoxLeftSideImageRemark());
			}
			
			bitmapdataRightSideMeter = Base64.decode(joinTicketingBO.getMeterBoxRightSideImage());
			if (bitmapdataRightSideMeter != null && bitmapdataRightSideMeter.length > 0)
			{

				Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdataRightSideMeter, 0, bitmapdataRightSideMeter.length);
				imgRightSide.setImageBitmap(bitmap);
				showRightSideImageRemarkText.setText(joinTicketingBO.getMeterBoxRightSideImageRemark());
			}
			
			bitmapdataRearSideMeter = Base64.decode(joinTicketingBO.getMeterBoxRearSideImage());
			if (bitmapdataRearSideMeter != null && bitmapdataRearSideMeter.length > 0)
			{

				Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdataRearSideMeter, 0, bitmapdataRearSideMeter.length);
				imgRearSide.setImageBitmap(bitmap);
				showRearSideImageRemarkText.setText(joinTicketingBO.getMeterBoxRearSideImageRemark());
			}
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.jointicketingmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == (R.id.sign))
		{
			joinTicketingBO.setMrsImage("");
			joinTicketingBO.setTurbineImage("");
			joinTicketingBO.setTurbineSealImage("");
			joinTicketingBO.setEvcImage1("");
			joinTicketingBO.setEvcImage2("");
			joinTicketingBO.setEvcSealImage("");
			joinTicketingBO.setMrsLockedImage("");

			Intent intent = new Intent(JoinTicketingSummary.this, SignatureActivity.class);
			intent.putExtra("selectedCustomerJSON", selectedCustomerJson);
			startActivity(intent);
			finish();
		}

		return super.onOptionsItemSelected(item);
	}*/

	@Override
	public void onBackPressed()
	{
	}
}
