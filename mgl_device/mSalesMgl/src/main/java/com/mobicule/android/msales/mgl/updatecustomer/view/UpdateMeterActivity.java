package com.mobicule.android.msales.mgl.updatecustomer.view;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.meterreading.view.CustomerInformation;
import com.mobicule.android.msales.mgl.mybookwalk.view.MyBookWalk;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.msales.mgl.client.common.Response;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class UpdateMeterActivity extends DefaultUpdateCustomerActivity implements OnClickListener
{

	private TextView currentMeterNo;

	private Menu menu;

	private EditText newMeterNoValue, editTextRemark;

	private String getUpdateCustomerJson, selectedCustomerJson;

	private Button b_meterUpdate, cancel_button;

	private Response response;

	String newMeterNumber = "";

	@Override
	protected void onStart()
	{
		super.onStart();
		Constants.broadcastDialogContext = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
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

		setContentView(R.layout.meterupadte);
		updateCustomerFacade.getCustomerUpdate().reset();
		
		initalization();
		
		initializeUpdateCustomerBO();

		if (getIntent().hasExtra("selectedCustomerJSON"))
		{
			Bundle extras = getIntent().getExtras();
			selectedCustomerJson = extras.getString("selectedCustomerJSON");
			MobiculeLogger.verbose("selectedCustomerJSON: " + selectedCustomerJson);
		}

		if (getIntent().hasExtra("updateCustomer"))
		{
			MobiculeLogger.verbose("updateCustomer");
			Bundle extras = getIntent().getExtras();
			getUpdateCustomerJson = extras.getString("updateCustomer");

			setCustomerDataInBo();
		}
	}
	
	private void initializeUpdateCustomerBO()
	{
		updateCustomerBO.setNewBuildName("");
		updateCustomerBO.setNewContactNoHome("");
		updateCustomerBO.setNewContactNoMobile("");
		updateCustomerBO.setNewContactNoOffice("");
		updateCustomerBO.setNewCustomerName("");
		updateCustomerBO.setNewFlatNumber("");
		updateCustomerBO.setNewFloor("");
		updateCustomerBO.setNewMeterNO("");
		updateCustomerBO.setNewPlot("");
		updateCustomerBO.setNewWing("");
		
		updateCustomerBO.setBPNumber("");
		updateCustomerBO.setBuildName("");
		updateCustomerBO.setCurrContactNoHome("");
		updateCustomerBO.setCurrContactNoMobile("");
		updateCustomerBO.setCurrContactNoOffice("");
		updateCustomerBO.setCurrentMeterNO("");
		updateCustomerBO.setCustomerName("");
		updateCustomerBO.setDocImgOne("");
		updateCustomerBO.setDocImgThree("");
		updateCustomerBO.setDocImgTwo("");
		updateCustomerBO.setDocType("");
		updateCustomerBO.setFlatNumber("");
		updateCustomerBO.setFloor("");
		updateCustomerBO.setMroNo("");
		updateCustomerBO.setMrSubmitted("");
		updateCustomerBO.setPlot("");
		updateCustomerBO.setRemark("");
		updateCustomerBO.setWing("");
	}






	private void setCustomerDataInBo()
	{
		try
		{

			JSONObject updateCustomer = new JSONObject(getUpdateCustomerJson);
			String currentMeterNumber = updateCustomer.getValue(Constants.FIELD_METER_NO).toString();
			currentMeterNo.setText(currentMeterNumber);
			
			updateCustomerBO.setCurrentMeterNO(currentMeterNumber);
			
			String currentContactNumberMobile = updateCustomer.getValue(Constants.KEY_CUSTOMER_CONTACT_NUMBER)
					.toString();
			updateCustomerBO.setCurrContactNoMobile(currentContactNumberMobile);
			
			updateCustomerBO.setBPNumber(updateCustomer.getString(Constants.FIELD_BP_NO));
			
			updateCustomerBO.setMroNo(updateCustomer.getString(Constants.KEY_MRO_NUMBER));
			
			updateCustomerBO.setCustomerName(updateCustomer.getString(Constants.KEY_CUSTOMER_NAME));
			
			String currentContactNumberHome = updateCustomer.getValue(Constants.KEY_CUSTOMER_LANDLINE_NUMBER)
					.toString();
			updateCustomerBO.setCurrContactNoHome(currentContactNumberHome);

			String currentContactNumberOffice = updateCustomer.getValue(Constants.KEY_CUSTOMER_OFFICE_NUMBER)
					.toString();
			updateCustomerBO.setCurrContactNoOffice(currentContactNumberOffice);
			
			updateCustomerBO.setBuildName(updateCustomer.getValue(getString(R.string.key_field_buildingname)).toString());
			
			updateCustomerBO.setCurrentMeterNO(updateCustomer.getValue(Constants.FIELD_METER_NO).toString());
			
			updateCustomerBO.setWing(updateCustomer.getValue(getString(R.string.key_field_wing)).toString());
			
			updateCustomerBO.setPlot(updateCustomer.getValue(getString(R.string.key_field_plot)).toString());
			
			updateCustomerBO.setFloor(updateCustomer.getValue(getString(R.string.key_field_floor)).toString());
			
			updateCustomerBO.setFlatNumber(updateCustomer.getValue(Constants.FIELD_FLAT_NO).toString());

			MobiculeLogger.verbose("Upadte Bo----------" + updateCustomerBO.toJSON().toString());

		}
		catch (JSONException e)
		{

			handler.logCrashReport(e);
			e.printStackTrace();
		}

	}

	private void showAlert()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMeterActivity.this);
		builder.setCancelable(true);

		builder.setMessage("Please enter atleast one of the field other than remark." + "\r\n"
				+ this.getString(R.string.please_enter_atleast_one_field));
		builder.setInverseBackgroundForced(true);
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

	public boolean oneOfTheFieldsFilled()
	{

		if (newMeterNoValue.getText().toString().equals(""))
		{
			return false;
		}
		else
		{
			return true;
		}

	}

	public boolean setValidations()
	{

		if (!oneOfTheFieldsFilled())
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMeterActivity.this);
			builder.setCancelable(true);

			builder.setMessage("Please enter atleast one of the field other than remark."
					+ "\r\n"
					+ UpdateMeterActivity.this
							.getString(R.string.please_enter_atleast_one_field));
			builder.setInverseBackgroundForced(true);
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
			return false;

		}

		return true;
	}

	private void initalization()
	{
		// TODO Auto-generated method stub
		editTextRemark = (EditText) findViewById(R.id.ed_remarks);

		currentMeterNo = (TextView) findViewById(R.id.tv2_old_meter_update);
		newMeterNoValue = (EditText) findViewById(R.id.ed_new_meter_update);

		b_meterUpdate = (Button) findViewById(R.id.btn_update);
		b_meterUpdate.setOnClickListener(this);

		cancel_button = (Button) findViewById(R.id.btn_cancel);
		cancel_button.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{

		switch (v.getId())
		{
			case R.id.btn_cancel:
				finish();
				break;
			case R.id.btn_update:

				if (setValidations())
				{
					updateDataInBo();

					updateMeterReading();
				}

				break;

			default:
				break;
		}

	}

	private void updateDataInBo()
	{

		updateCustomerBO.setNewMeterNO(newMeterNoValue.getText().toString().trim());
		updateCustomerBO.setRemark(editTextRemark.getText().toString().trim());

	}

	/*private boolean setValidations()
	{
		if (newMeterNoValue.getText().toString().equals(""))
		{
			Toast.makeText(UpdateMeterActivity.this, "Enter valid Meter Number", Toast.LENGTH_SHORT).show();
			return false;
		}

		else if (newMeterNoValue.getText().toString().length() != 50)
		{

			Toast.makeText(UpdateMeterActivity.this, "Enter valid Meter Number", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}*/

	private void updateMeterReading()
	{

		new ApplicationAsk(UpdateMeterActivity.this, new ApplicationService()
		{
			@Override
			public void postExecute()
			{

				AlertDialog.Builder saveDialog = new AlertDialog.Builder(UpdateMeterActivity.this);
				saveDialog.setCancelable(false);
				saveDialog.setTitle("Update Meter Number Details");
				if(response.isSuccess())
				{
				saveDialog.setMessage("Meter Number Update request sent successfully");
				}
				else
				{
					saveDialog.setMessage(response.getMessage());
				}
				saveDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{

						//onmFacade.resetMeterReadingInstanceBO(); // ---------- reset BO
						Intent broadcastIntent = new Intent();
						broadcastIntent.setAction("com.mobicule.ACTION_MY_BOOK_WALK");
						sendBroadcast(broadcastIntent);
						MobiculeLogger.verbose("Summary  is ::::    ", "flag is   " + Constants.searchSelection);

						if (Constants.searchSelection == true)
						{
							startActivity(new Intent(UpdateMeterActivity.this, MyBookWalk.class));

						}
						else
						{

							Intent intent = new Intent(UpdateMeterActivity.this, UpdateCustomerInfoActivity.class);
							intent.putExtra("updateCustomer", getUpdateCustomerJson);
							intent.putExtra("selectedCustomerJSON", selectedCustomerJson);
							startActivity(intent);

						}
					}

				});
				saveDialog.show();
			}

			@Override
			public void execute()
			{

				response = updateCustomerFacade.saveCustomerUpdate(false);

				MobiculeLogger.verbose("save meter reading update response : " + response);

				//meterReadingFacade.resetMeterReadingInstanceBO();// tushar // ----------------- changed by me

			}
		}).execute();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			onBackPressed();
		}
		return true;
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		finish();

	}

}
