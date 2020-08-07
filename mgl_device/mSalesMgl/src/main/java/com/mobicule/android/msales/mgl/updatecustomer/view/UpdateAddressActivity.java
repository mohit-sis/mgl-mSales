package com.mobicule.android.msales.mgl.updatecustomer.view;

import org.json.me.JSONObject;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.commons.model.ApplicationAsk;
import com.mobicule.android.msales.mgl.commons.model.ApplicationService;
import com.mobicule.android.msales.mgl.meterreading.view.CustomerInformation;
import com.mobicule.android.msales.mgl.mybookwalk.view.MyBookWalk;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class UpdateAddressActivity extends DefaultUpdateCustomerActivity implements OnClickListener
{

	private TextView flatNo, floorNo, plotNo, wingNo, buildName, remark;

	private EditText editTextRemark, newFlatValue, newFloorValue, newPlotValue, newWingValue, newBuildingNameValue;

	private String getUpdateCustomerJson, selectedCustomerJson;

	private Menu menu;

	private Spinner documentList;

	Button btn_take_picture, btn_cancel;

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

		setContentView(R.layout.update_address);
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
			documentList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
			{
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
				{
					MobiculeLogger.verbose("documentList: " + documentList.getItemAtPosition(arg2));

					if (documentList.getItemAtPosition(arg2).toString().equalsIgnoreCase("Select Document Type"))
					{
						/*Toast.makeText(getApplicationContext(), "Select Document Type", Toast.LENGTH_LONG).show();*/
					}
					else if (arg2 != 0)
					{
						updateCustomerBO.setDocType(documentList.getItemAtPosition(arg2).toString());
						Intent intent = new Intent();
						intent.putExtra("updateCustomer", getUpdateCustomerJson);
						intent.putExtra("selectedCustomerJSON", selectedCustomerJson);

					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0)
				{

				}
			});
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

	public boolean setValidations()
	{

		if (!oneOfTheFieldsFilled())
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAddressActivity.this);
			builder.setCancelable(true);

			builder.setMessage("Please enter atleast one of the field other than remark."
					+ "\r\n"
					+ UpdateAddressActivity.this
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
		else if(documentList.getSelectedItem().toString().equalsIgnoreCase("Select Document Type"))
		{
			Toast.makeText(UpdateAddressActivity.this, "Select Document Type", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	public boolean oneOfTheFieldsFilled()
	{

		if (newFlatValue.getText().toString().equals("") && newFloorValue.getText().toString().equals("")
				&& newPlotValue.getText().toString().equals("") && newWingValue.getText().toString().equals("")
				&& newBuildingNameValue.getText().toString().equals(""))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	private void setCustomerDataInBo()
	{
		try
		{
			JSONObject updateCustomer = new JSONObject(getUpdateCustomerJson);
			
			MobiculeLogger.verbose("UpdateCustomer :: "+updateCustomer.toString());
			
			String currentContactNumberMobile = updateCustomer.getValue(Constants.KEY_CUSTOMER_CONTACT_NUMBER)
					.toString();
			updateCustomerBO.setCurrContactNoMobile(currentContactNumberMobile);
			
			updateCustomerBO.setBPNumber(updateCustomer.getString(Constants.KEY_BP_NUMBER));

			String currentContactNumberHome = updateCustomer.getValue(Constants.KEY_CUSTOMER_LANDLINE_NUMBER)
					.toString();
			updateCustomerBO.setCurrContactNoHome(currentContactNumberHome);

			String currentContactNumberOffice = updateCustomer.getValue(Constants.KEY_CUSTOMER_OFFICE_NUMBER)
					.toString();
			updateCustomerBO.setCurrContactNoOffice(currentContactNumberOffice);
			
			updateCustomerBO.setMroNo(updateCustomer.getString(Constants.KEY_MRO_NUMBER));
			updateCustomerBO.setCustomerName(updateCustomer.getString(Constants.KEY_CUSTOMER_NAME));
			updateCustomerBO.setBuildName(updateCustomer.getValue(getString(R.string.key_field_buildingname)).toString());
			updateCustomerBO.setCurrentMeterNO(updateCustomer.getValue(Constants.FIELD_METER_NO).toString());
			updateCustomerBO.setWing(updateCustomer.getValue(getString(R.string.key_field_wing)).toString());
			updateCustomerBO.setPlot(updateCustomer.getValue(getString(R.string.key_field_plot)).toString());
			updateCustomerBO.setFloor(updateCustomer.getValue(getString(R.string.key_field_floor)).toString());
			updateCustomerBO.setFlatNumber(updateCustomer.getValue(Constants.FIELD_FLAT_NO).toString());
			updateCustomerBO.setBPNumber(updateCustomer.getString(Constants.FIELD_BP_NO));
			updateCustomerBO.setMroNo(updateCustomer.getString(Constants.KEY_MRO_NUMBER));
			updateCustomerBO.setCustomerName(updateCustomer.getString(Constants.KEY_CUSTOMER_NAME));
			
			flatNo.setText(updateCustomer.getValue(Constants.FIELD_FLAT_NO).toString());
			buildName.setText(updateCustomer.getValue(getString(R.string.key_field_buildingname)).toString());
			wingNo.setText(updateCustomer.getValue(getString(R.string.key_field_wing)).toString());
			floorNo.setText(updateCustomer.getValue(getString(R.string.key_field_floor)).toString());
			plotNo.setText(updateCustomer.getValue(getString(R.string.key_field_plot)).toString());
			

		}
		catch (Exception e)
		{

			handler.logCrashReport(e);
			e.printStackTrace();
		}

	}

	private void initalization()
	{
		// TODO Auto-generated method stub
		flatNo = (TextView) findViewById(R.id.tv2_old_flat);
		newFlatValue = (EditText) findViewById(R.id.ed_new_flat);

		floorNo = (TextView) findViewById(R.id.tv2_old_floor);
		newFloorValue = (EditText) findViewById(R.id.ed_new_floor);

		plotNo = (TextView) findViewById(R.id.tv2_old_plot);
		newPlotValue = (EditText) findViewById(R.id.ed_new_plot);

		wingNo = (TextView) findViewById(R.id.tv2_old_wing);
		newWingValue = (EditText) findViewById(R.id.ed_new_wing);

		buildName = (TextView) findViewById(R.id.tv2_old_blgName);
		newBuildingNameValue = (EditText) findViewById(R.id.ed_new_blgName);

		remark = (TextView) findViewById(R.id.tv_remarks);
		editTextRemark = (EditText) findViewById(R.id.ed_remarks);

		documentList = (Spinner) findViewById(R.id.sp_DoctType);

		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		btn_take_picture = (Button) findViewById(R.id.btn_take_picture);
		btn_take_picture.setOnClickListener(this);

	}

	private void updateDataInBo()
	{
		updateCustomerBO.setNewFlatNumber(newFlatValue.getText().toString().trim());
		updateCustomerBO.setNewFloor(newFloorValue.getText().toString().trim());
		updateCustomerBO.setNewBuildName(newBuildingNameValue.getText().toString().trim());
		updateCustomerBO.setNewWing(newWingValue.getText().toString().trim());
		updateCustomerBO.setNewPlot(newPlotValue.getText().toString().trim());
		updateCustomerBO.setRemark(editTextRemark.getText().toString().trim());

	}

	private void updateAddress()
	{

		new ApplicationAsk(UpdateAddressActivity.this, new ApplicationService()
		{
			@Override
			public void postExecute()
			{

				AlertDialog.Builder saveDialog = new AlertDialog.Builder(UpdateAddressActivity.this);
				saveDialog.setCancelable(false);
				saveDialog.setTitle("Update Address Details");
				if(response.isSuccess())
				{
					saveDialog.setMessage("Address Update Request sent successfully");
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
							startActivity(new Intent(UpdateAddressActivity.this, MyBookWalk.class));
						}
						else
						{

							Intent intent = new Intent(UpdateAddressActivity.this, UpdateCustomerInfoActivity.class);
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

				MobiculeLogger.verbose("save address update response : " + response);

				//meterReadingFacade.resetMeterReadingInstanceBO();// tushar // ----------------- changed by me

			}
		}).execute();

	}

	private void showAlert()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAddressActivity.this);
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

	@Override
	protected void onStart()
	{
		super.onStart();
		Constants.broadcastDialogContext = this;
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
			case R.id.btn_take_picture:
				
				InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm1.hideSoftInputFromWindow(newFlatValue.getWindowToken(), 0);
				InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm2.hideSoftInputFromWindow(newFloorValue.getWindowToken(), 0);
				InputMethodManager imm3 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm3.hideSoftInputFromWindow(newPlotValue.getWindowToken(), 0);
				InputMethodManager imm4 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm4.hideSoftInputFromWindow(newWingValue.getWindowToken(), 0);
				InputMethodManager imm5 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm5.hideSoftInputFromWindow(newBuildingNameValue.getWindowToken(), 0);
				InputMethodManager imm6 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm6.hideSoftInputFromWindow(editTextRemark.getWindowToken(), 0);
				if (setValidations())
				{
					updateDataInBo();
					Intent intent = new Intent(UpdateAddressActivity.this, DocumentImageActivity.class);
					intent.putExtra("updateCustomer", getUpdateCustomerJson);
					intent.putExtra("selectedCustomerJSON", selectedCustomerJson);
					startActivity(intent);
				}
				break;
			case R.id.btn_cancel:
				finish();
				break;
			default:
				break;
		}

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
