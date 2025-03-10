package com.mobicule.android.msales.mgl.updatecustomer.view;

import org.json.me.JSONObject;

import com.mahanagar.R;
import com.mahanagar.R.menu;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.meterreading.view.CustomerInformation;
import com.mobicule.android.msales.mgl.mybookwalk.view.MyBookWalk;
import com.mobicule.android.msales.mgl.onmplaning.view.OnMPlanningSummaryActivity;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.msales.mgl.client.common.Response;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class UpdateContactNoActivity extends DefaultUpdateCustomerActivity implements OnClickListener
{
	private TextView currentContactNoMobile, currentContactNoHome, currentContactNoOffice;

	private EditText newContactNoMobileValue, newContactNoHomeValue, newContactNoOfficeValue, editTextRemark;

	private String getUpdateCustomerJson, selectedCustomerJson;

	Button btn_update, btn_cancel;

	private Response response;

	private Menu menu;

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

		setContentView(R.layout.activity_contact_update);
		
		updateCustomerFacade.getCustomerUpdate().reset();
		
		initalization();
		
		initializeUpdateCustomerBO();
		
		if (getIntent().hasExtra("selectedCustomerJSON"))
		{
			Bundle extras = getIntent().getExtras();
			selectedCustomerJson = extras.getString("selectedCustomerJSON");
		}

		if (getIntent().hasExtra("updateCustomer"))
		{
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

	private boolean setValidations()
	{

		boolean isValidContactNumberMobile = true, isValidContactNoHome = true, isValidContactNumberOffice = true;
		if (!newContactNoMobileValue.getText().toString().equals(""))
		{
			if (newContactNoMobileValue.getText().toString().length() < 10)
			{
				Toast.makeText(getApplicationContext(), "Enter valid Mobile Number", Toast.LENGTH_SHORT).show();
				isValidContactNumberMobile = false;
			}
			else
			{
				isValidContactNumberMobile = true;
			}
		}

		if (!newContactNoHomeValue.getText().toString().equals(""))
		{
			if (newContactNoHomeValue.getText().toString().length() < 10)
			{
				Toast.makeText(getApplicationContext(), "Enter valid Telephone No. (Res.)", Toast.LENGTH_SHORT).show();
				isValidContactNoHome = false;
			}
			else
			{
				isValidContactNoHome = true;
			}
		}

		if (!newContactNoOfficeValue.getText().toString().equals(""))
		{
			if (newContactNoOfficeValue.getText().toString().length() < 10)
			{
				Toast.makeText(getApplicationContext(), "Enter valid Telephone No. (Office)", Toast.LENGTH_SHORT).show();
				isValidContactNumberOffice = false;
			}
			else
			{
				isValidContactNumberOffice = true;
			}
		}

		if (!isValidContactNumberMobile || !isValidContactNoHome || !isValidContactNumberOffice)
		{
			return false;
		}

		return true;

	}

	private boolean setValidationsOneofthefields()
	{
		if (!oneOfTheFieldsFilled())
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(UpdateContactNoActivity.this);
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

			return false;

		}
		return true;

	}

	private void setCustomerDataInBo()
	{
		// TODO Auto-generated method stub
		try
		{
			JSONObject updateCustomer = new JSONObject(getUpdateCustomerJson);
			MobiculeLogger.verbose("Update Customer json : " + updateCustomer.toString());
			
			String currentContactNumberMobile = updateCustomer.getValue(Constants.KEY_CUSTOMER_CONTACT_NUMBER)
					.toString();
			currentContactNoMobile.setText(currentContactNumberMobile);
			updateCustomerBO.setCurrContactNoMobile(currentContactNumberMobile);
			
			updateCustomerBO.setBPNumber(updateCustomer.getString(Constants.KEY_BP_NUMBER));

			String currentContactNumberHome = updateCustomer.getValue(Constants.KEY_CUSTOMER_LANDLINE_NUMBER)
					.toString();
			currentContactNoHome.setText(currentContactNumberHome);
			updateCustomerBO.setCurrContactNoHome(currentContactNumberHome);

			String currentContactNumberOffice = updateCustomer.getValue(Constants.KEY_CUSTOMER_OFFICE_NUMBER)
					.toString();
			currentContactNoOffice.setText(currentContactNumberOffice);
			updateCustomerBO.setCurrContactNoOffice(currentContactNumberOffice);
			
			//updateCustomerBO.setNewContactNoMobile(newContactNoMobileValue.getText().toString());
			//updateCustomerBO.setNewContactNoHome(newContactNoHomeValue.getText().toString());
			//updateCustomerBO.setNewContactNoOffice(newContactNoOfficeValue.getText().toString());
			updateCustomerBO.setMroNo(updateCustomer.getString(Constants.KEY_MRO_NUMBER));
			updateCustomerBO.setCustomerName(updateCustomer.getString(Constants.KEY_CUSTOMER_NAME));
			updateCustomerBO.setBuildName(updateCustomer.getValue(getString(R.string.key_field_buildingname)).toString());
			updateCustomerBO.setCurrentMeterNO(updateCustomer.getValue(Constants.FIELD_METER_NO).toString());
			updateCustomerBO.setWing(updateCustomer.getValue(getString(R.string.key_field_wing)).toString());
			updateCustomerBO.setPlot(updateCustomer.getValue(getString(R.string.key_field_plot)).toString());
			updateCustomerBO.setFloor(updateCustomer.getValue(getString(R.string.key_field_floor)).toString());
			updateCustomerBO.setFlatNumber(updateCustomer.getValue(Constants.FIELD_FLAT_NO).toString());
			
			MobiculeLogger.verbose("Upadte Bo----------" + updateCustomerBO.toString());
		}

		catch (Exception e)
		{

			handler.logCrashReport(e);
			e.printStackTrace();
		}
	}

	public boolean oneOfTheFieldsFilled()
	{

		if (newContactNoMobileValue.getText().toString().equals("")
				&& newContactNoHomeValue.getText().toString().equals("")
				&& newContactNoOfficeValue.getText().toString().equals(""))
		{
			return false;
		}
		else
		{
			return true;
		}
	}



	private void updateDataInBo()
	{

		updateCustomerBO.setNewContactNoMobile(newContactNoMobileValue.getText().toString().trim());
		updateCustomerBO.setNewContactNoHome(newContactNoHomeValue.getText().toString().trim());
		updateCustomerBO.setNewContactNoOffice(newContactNoOfficeValue.getText().toString().trim());
		updateCustomerBO.setRemark(editTextRemark.getText().toString().trim());

	}

	private void initalization()
	{
		// TODO Auto-generated method stub
		currentContactNoMobile = (TextView) findViewById(R.id.tv2_old_mob_number);
		newContactNoMobileValue = (EditText) findViewById(R.id.ed_new_mob_number);

		currentContactNoHome = (TextView) findViewById(R.id.tv2_old_res_number);
		newContactNoHomeValue = (EditText) findViewById(R.id.ed_new_res_number);

		currentContactNoOffice = (TextView) findViewById(R.id.tv2_old_office_number);
		newContactNoOfficeValue = (EditText) findViewById(R.id.tv2_new_office_number);

		editTextRemark = (EditText) findViewById(R.id.ed_remarks);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		btn_update = (Button) findViewById(R.id.btn_update);
		btn_update.setOnClickListener(this);

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
				
				InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm1.hideSoftInputFromWindow(newContactNoMobileValue.getWindowToken(), 0);
				InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm2.hideSoftInputFromWindow(newContactNoHomeValue.getWindowToken(), 0);
				InputMethodManager imm3 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm3.hideSoftInputFromWindow(newContactNoOfficeValue.getWindowToken(), 0);
				if (setValidations() && setValidationsOneofthefields())
				{

					updateDataInBo();
					updateCustomer();

				}

				break;
			default:
				break;
		}

	}

	private void updateCustomer()
	{

		new ApplicationAsk(UpdateContactNoActivity.this, new ApplicationService()
		{
			@Override
			public void postExecute()
			{

				AlertDialog.Builder saveDialog = new AlertDialog.Builder(UpdateContactNoActivity.this);
				saveDialog.setCancelable(false);
				saveDialog.setTitle("Update Contact Number Details");
				if(response.isSuccess())
				{
					saveDialog.setMessage("Contact Number Update request sent successfully");
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
						if (Constants.searchSelection == true)
						{
							startActivity(new Intent(UpdateContactNoActivity.this, MyBookWalk.class));
						}
						else
						{
							Intent intent = new Intent(UpdateContactNoActivity.this, UpdateCustomerInfoActivity.class);
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

				MobiculeLogger.verbose("save updated contact response : " + response);

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
	/*	@Override
		protected void onResume()
		{
			super.onResume();
		}

		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event)
		{
			if (keyCode == KeyEvent.KEYCODE_BACK)
			{
				startActivity(new Intent(this, UpdateCustomerInfoActivity.class));
				finish();
			}
			return super.onKeyDown(keyCode, event);
		}*/
}
