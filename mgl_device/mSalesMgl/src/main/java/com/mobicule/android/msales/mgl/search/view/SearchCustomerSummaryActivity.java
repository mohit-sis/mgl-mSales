package com.mobicule.android.msales.mgl.search.view;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.jointicketing.view.JoinTicketing;
import com.mobicule.android.msales.mgl.meterreading.view.BuildingListActivity;
import com.mobicule.android.msales.mgl.meterreading.view.CustomerInformation;
import com.mobicule.android.msales.mgl.meterreading.view.CustomerSummary;
import com.mobicule.android.msales.mgl.meterreading.view.DefaultMeterReadingActivity;
import com.mobicule.android.msales.mgl.meterreading.view.MeterReading;
import com.mobicule.android.msales.mgl.updatecustomer.view.UpdateCustomerInfoActivity;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.android.msales.mgl.util.FileUtil;
import com.mobicule.component.string.StringUtil;
import com.mobicule.crashnotifier.GenericExceptionHandler;
import com.mobicule.crashnotifier.IGenericExceptionHandler;
import com.mobicule.msales.mgl.client.meterreading.DefaultMeterReading;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IInspection;

import java.io.File;

public class SearchCustomerSummaryActivity extends DefaultMeterReadingActivity implements OnClickListener
{
	private String selectedCustomerJson, landlineNo, officeNo;

	private JSONObject jsonoCustomerDetails;

	private TextView bpNumberText, meterNumberTextView, customerNameText, contactNumberTextView, addressText,
			caNumberTextView, mroNumberText, custlandLineNo, custOfficeNo;

	private TextView textViewMruCode;

	//private TextView msgtomrSummary;

	private TextView flatTextView;

	private TextView floorTextView;

	private TextView plotTextView;

	private TextView wingTextView;

	private String sealNumForInspection;

	protected String meterReadingType = "Take Meter Reading";

	protected String updateCustomerDataType = "Update Customer";

	private Button takeMeterReadingButton, jointTicketingButton, updateCustomerButton;

	private LinearLayout space1, space2;
	
	IGenericExceptionHandler handler;

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

		setContentView(R.layout.customer_summary);

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

		takeMeterReadingButton = (Button) findViewById(R.id.takemeter_button);

		jointTicketingButton = (Button) findViewById(R.id.joint_ticketing_button);

		updateCustomerButton = (Button) findViewById(R.id.update_customer_button);
		
		//handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());

		takeMeterReadingButton.setOnClickListener(this);
		jointTicketingButton.setOnClickListener(this);
		updateCustomerButton.setOnClickListener(this);

		space1 = (LinearLayout) findViewById(R.id.space1);

		space2 = (LinearLayout) findViewById(R.id.space2);

		//msgtomrSummary = (TextView) findViewById(R.id.msgtomrSummary);

		String extraStr_updateCustomerSummary = "updateCompletedCustomerBySearch";
		String extraStr_customerSummary = "selectedCustomerJSON";
		Bundle bundle = getIntent().getExtras();

		if (getIntent().hasExtra(extraStr_updateCustomerSummary))
		{
			selectedCustomerJson = bundle.getString(extraStr_updateCustomerSummary);
		}
		else if (getIntent().hasExtra(extraStr_customerSummary))
		{
			selectedCustomerJson = bundle.getString(extraStr_customerSummary);
		}
		if (StringUtil.isValid(selectedCustomerJson))
		{
			try
			{
				MobiculeLogger.verbose("selectedCustomerJson" + selectedCustomerJson);
				jsonoCustomerDetails = new JSONObject(selectedCustomerJson);
				String mroNumber = jsonoCustomerDetails.getValue(Constants.KEY_MRO_NUMBER).toString();
				meterReadingBO.setMroNumber(mroNumber);
				if (jsonoCustomerDetails.has(Constants.FIELD_CONN_OBJ))
				{
					meterReadingBO.setConnObj(jsonoCustomerDetails.get(Constants.FIELD_CONN_OBJ).toString());
				}
				bpNumberText.setText(jsonoCustomerDetails.get(Constants.KEY_BP_NUMBER).toString());
				mroNumberText.setText(jsonoCustomerDetails.get(Constants.KEY_MRO_NUMBER).toString());
				meterNumberTextView.setText(jsonoCustomerDetails.getValue(Constants.KEY_CUSTOMER_METER_NUMBER)
						.toString());
				customerNameText.setText(jsonoCustomerDetails.getValue(Constants.KEY_CUSTOMER_NAME).toString());
				contactNumberTextView.setText(jsonoCustomerDetails.getValue(Constants.KEY_CUSTOMER_CONTACT_NUMBER)
						.toString());
				landlineNo = jsonoCustomerDetails.getValue(Constants.KEY_CUSTOMER_LANDLINE_NUMBER).toString();
				officeNo = jsonoCustomerDetails.getValue(Constants.KEY_CUSTOMER_OFFICE_NUMBER).toString();

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
				//msgtomrSummary.setText(jsonoCustomerDetails.getValue(
				//getString(R.string.key_field_messToMR)).toString());
			}
			catch (JSONException e)
			{
				FileUtil.writeToFile("//SearchCustomerSummaryActivity : Exception :: " + e);

				handler.logCrashReport(e);
				MobiculeLogger.verbose("CustomerSummery,onCreate"+ e.toString());
			}
		}

		getButtonsVisibilty();

	}

	private void getButtonsVisibilty()
	{
		MobiculeLogger.verbose("CustomerSummery - getButtonsVisibilty() - inside getButtonsVisibility()");
		
		if (BuildingListActivity.STATUS.equals(Constants.FIELD_COMPLETED))
		{
			if (getIntent().hasExtra("updateCompletedCustomerBySearch"))
			{
				updateCustomerButton.setVisibility(View.VISIBLE);
				MobiculeLogger.verbose("CustomerSummery - getButtonsVisibilty() - UpdateButton Visible");

			}
		}
		if (BuildingListActivity.STATUS.equals(Constants.FIELD_INCOMPLETE)
				|| (BuildingListActivity.STATUS.equals(Constants.FIELD_UNATTEMPTED)))
		{
			String mrucode = jsonoCustomerDetails.getValue(Constants.KEY_MRU_CODE).toString();
			MobiculeLogger.verbose("OnPrepareOptionsSelectedMenu - mrucode" + mrucode);
			if ((mrucode.equalsIgnoreCase("COMBA003")) || (mrucode.equalsIgnoreCase("COMCA001"))
					|| (mrucode.equalsIgnoreCase("INDA001") || (mrucode.equalsIgnoreCase("COMAC003"))))
			{
				jointTicketingButton.setVisibility(View.VISIBLE);
				space1.setVisibility(View.VISIBLE);
				updateCustomerButton.setVisibility(View.VISIBLE);
				
				MobiculeLogger.verbose("CustomerSummery - getButtonsVisibilty() - Joint ticketing Button & Update Button  Visible");

			}
			else
			{
				if (getIntent().hasExtra("selectedCustomerJSON"))
				{
					takeMeterReadingButton.setVisibility(View.VISIBLE);
					space2.setVisibility(View.VISIBLE);
					updateCustomerButton.setVisibility(View.VISIBLE);
					
					MobiculeLogger.verbose("CustomerSummery - getButtonsVisibilty() - Take Meter Reading Button & Update Button  Visible");

				}
			}
		}
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.mymenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{

		if (BuildingListActivity.STATUS.equals(Constants.FIELD_COMPLETED))
		{
			if (getIntent().hasExtra("updateCompletedCustomerBySearch"))
			{
				menu.findItem(R.id.updatecustomer).setVisible(true);

			}
		}
		if (BuildingListActivity.STATUS.equals(Constants.FIELD_INCOMPLETE)
				|| (BuildingListActivity.STATUS.equals(Constants.FIELD_UNATTEMPTED)))
		{
			String mrucode = jsonoCustomerDetails.getValue(Constants.KEY_MRU_CODE).toString();
			if ((mrucode.equalsIgnoreCase("COMBA003")) || (mrucode.equalsIgnoreCase("COMCA001"))
					|| (mrucode.equalsIgnoreCase("INDA001") || (mrucode.equalsIgnoreCase("COMAC003"))))
			{
				menu.findItem(R.id.jointicketing).setVisible(true);
				menu.findItem(R.id.updatecustomer).setVisible(true);

			}
			else
			{
				if (getIntent().hasExtra("selectedCustomerJSON"))
				{
					menu.findItem(R.id.takemeterreading).setVisible(true);
					menu.findItem(R.id.updatecustomer).setVisible(true);

				}
			}
		}

		return super.onPrepareOptionsMenu(menu);
	}*/

	private void commonConfimrationDialog(final String type)
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(SearchCustomerSummaryActivity.this);
		builder.setCancelable(true);

		builder.setMessage("All details have been verified and updated wherever applicable."
				+ "\r\n"
				+ this.getString(R.string.all_details_have_been_verified_and_updated_wherever_applicable));
		
		builder.setInverseBackgroundForced(true);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (type.equalsIgnoreCase(meterReadingType))
				{

					// *********** Setting BO ********************
					setMeterReadingBO();

					// *********** Setting up BO Over ********************
					Intent intent = new Intent(SearchCustomerSummaryActivity.this, MeterReading.class);
					intent.putExtra("selectedCustomerJSON", selectedCustomerJson);
					intent.putExtra("activity", "SearchCustomerSummaryActivity");
					startActivity(intent);
					dialog.dismiss();

				}
				/*else if (type.equalsIgnoreCase(updateCustomerDataType))
				{

					Intent intent = new Intent(CustomerSummary.this, UpdateCustomerSummaryActivity.class);
					intent.putExtra("selectedCustomerJSON", selectedCustomerJson);
					if (jsonoCustomerDetails != (null))
					{
						intent.putExtra("updateCustomer", jsonoCustomerDetails.toString());

					}

					startActivity(intent);

				}*/

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

	@Override
	public void onBackPressed()
	{
		Intent intent =new Intent(this,SearchOutput.class);
		startActivity(intent);
		super.onBackPressed();
	}

	private void setMeterReadingBO()
	{
		MobiculeLogger.verbose("customer summary - in update customer menu  ");
		// *********** Setting BO ********************
		String bpNumber = bpNumberText.getText().toString();
		String mroNumber = mroNumberText.getText().toString();
		String meterNumber = meterNumberTextView.getText().toString();
		String contactNumber = contactNumberTextView.getText().toString();
		String customerName = customerNameText.getText().toString();
		String address = addressText.getText().toString();

		String flat = flatTextView.getText().toString();
		String floor = floorTextView.getText().toString();
		String plot = plotTextView.getText().toString();
		String wing = wingTextView.getText().toString();

		String caNo = caNumberTextView.getText().toString();
		//String messToMR = msgtomrSummary.getText().toString();
		String landLineNumber = custlandLineNo.getText().toString();
		String officeNumber = custOfficeNo.getText().toString();
		String mruCode = textViewMruCode.getText().toString();

		meterReadingBO.setBpNumber(bpNumber);
		meterReadingBO.setMroNumber(mroNumber);
		meterReadingBO.setMeterNumber(meterNumber);
		meterReadingBO.setCustomerContactNo(contactNumber);
		meterReadingBO.setCustLandNo(landLineNumber);
		meterReadingBO.setCustOfficeNo(officeNumber);
		meterReadingBO.setCustomerName(customerName);
		meterReadingBO.setCustomerAddress(address);
		meterReadingBO.setCaNo(caNo);
		meterReadingBO.setMruCode(mruCode);
		meterReadingBO.setFlat(flat);
		meterReadingBO.setFloor(floor);
		meterReadingBO.setPlot(plot);
		meterReadingBO.setWing(wing);
		meterReadingBO.setMsgToMr(jsonoCustomerDetails.getValue(Constants.KEY_MESSAGE_TO_MR).toString());
		meterReadingBO.setImage("");
		meterReadingBO.setImage2("");
		meterReadingBO.setImage3("");
		MobiculeLogger.verbose("CustomerSummary BO - Meter Reading BO" + meterReadingBO.toJSON());
		if (jsonoCustomerDetails.has(Constants.KEY_CURRENT_SEAL_NO))
		{
			sealNumForInspection = jsonoCustomerDetails.getValue(Constants.KEY_CURRENT_SEAL_NO).toString();
			if (sealNumForInspection != null && !sealNumForInspection.trim().equals(""))
			{
				IInspection inspection = new DefaultMeterReading.DefaultInspection();
				inspection.setCurrentSealNo(sealNumForInspection);
				meterReadingBO.setInspection(inspection);
				MobiculeLogger.verbose("Current Seal No"+ sealNumForInspection);
			}
			else
			{
				MobiculeLogger.verbose("Current Seal No - NULL or Empty");
			}
		}
		else
		{
			MobiculeLogger.verbose("Current Seal No - Not Applicable");
		}

		// *********** Setting up BO Over ********************
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == (R.id.takemeterreading))
		{
			commonConfimrationDialog(meterReadingType);
		}
		else if (item.getItemId() == (R.id.updatecustomer))
		{
			//commonConfimrationDialog(updateCustomerDataType);
			setMeterReadingBO();
			Intent intent = new Intent(SearchCustomerSummaryActivity.this, UpdateCustomerInfoActivity.class);
			if (jsonoCustomerDetails != (null))
			{
				MobiculeLogger.verbose("Cusromer Summary - jsonoCustomerDetails     ");
				intent.putExtra("updateCustomer", jsonoCustomerDetails.toString());
				intent.putExtra("selectedCustomerJSON", selectedCustomerJson);
			}

			startActivity(intent);
		}
		else if (item.getItemId() == (R.id.jointicketing))
		{
			Intent joinTicket = new Intent(SearchCustomerSummaryActivity.this, JoinTicketing.class);
			joinTicket.putExtra("selectedCustomerJSON", selectedCustomerJson);
			startActivity(joinTicket);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v)
	{

		switch (v.getId())
		{
			case R.id.takemeter_button:

				commonConfimrationDialog(meterReadingType);

				break;

			case R.id.update_customer_button:

				//commonConfimrationDialog(updateCustomerDataType);
				setMeterReadingBO();
				Intent intent = new Intent(SearchCustomerSummaryActivity.this, UpdateCustomerInfoActivity.class);
				if (jsonoCustomerDetails != (null))
				{
					MobiculeLogger.verbose("Cusromer Summary - jsonoCustomerDetails     ");
					intent.putExtra("updateCustomer", jsonoCustomerDetails.toString());
					intent.putExtra("selectedCustomerJSON", selectedCustomerJson);
				}

				startActivity(intent);

				break;

			case R.id.joint_ticketing_button:

				Intent joinTicket = new Intent(SearchCustomerSummaryActivity.this, JoinTicketing.class);
				joinTicket.putExtra("selectedCustomerJSON", selectedCustomerJson);
				startActivity(joinTicket);

				break;

		}

	}
}
