package com.mobicule.android.msales.mgl.jointicketing.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.meterreading.view.LocationSensor;
import com.mobicule.android.msales.mgl.meterreading.view.MeterReadingFeild;
import com.mobicule.android.msales.mgl.util.Base64;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;

import org.json.me.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JoinTicketing extends DefaultJoinTicketingActivity
{
	private final String TAG = "JoinTicketing";
	
	private static final int ACTION_TAKE_PHOTO_S = 2;

	private static final int PERMISSION_REQUEST_CODE = 7;

	private ImageView mImageView;

	private Button save, cancel;

	private Bitmap mImageBitmap;

	private byte[] bitmapdata;

	private static Uri imageUri;

	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";

	private static final String BITMAP_STORAGE_KEY = "viewbitmap";

	private EditText editTextTurbineMeterReading, editTextEvcCorrected, editTextEvcUnCorrected, editTextOutletPre,
			editTextCorrectionFac, editTextTemperature, editTextRemarks;

	private Button btnOK,cropImage;

	private Dialog dialog;

	private LinearLayout linearTurbine, linearEvc, linearOutlet;

	private String selectedCustomerJson;
	
	private LinearLayout remarksLayout;
	
	private EditText remarksEditText;

	ArrayList<Bitmap>listOfImages;

	private static final int CROP_FROM_CAMERA = 5;

	private String mCurrentPhotoPath;

	File photoFile;

	String path;

	String replacedStringPath = "";

	Uri uri = null;

	@Override
	protected void onStart()
	{
		super.onStart();
		Constants.broadcastDialogContext = this;
		MobiculeLogger.verbose("onStart()");
		
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

		MobiculeLogger.verbose("onCreate()");
		
		setContentView(R.layout.join_ticketing);
		Log.e("JoinTicketing","JoinTicketing");
		mImageView = (ImageView) findViewById(R.id.imageview);
		save = (Button) findViewById(R.id.saveButton);
		cancel = (Button) findViewById(R.id.cancelButton);
		remarksLayout = (LinearLayout) findViewById(R.id.remarksLayout);
		remarksEditText = (EditText) findViewById(R.id.remarkText);
		cropImage = (Button)findViewById(R.id.btncrop);

		mImageBitmap = null;
		MeterReadingFeild.init();
		Constants.threePicsCapCnt = 0;

		new LocationLooperThread().start();

		joinTicketingCommonDialog("Take MRS Image");

		setBtnListenerOrDisable(save, saveOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
		setBtnListenerOrDisable(cancel, cancelOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
		setBtnListenerOrDisable(cropImage,cropImageListener,MediaStore.ACTION_IMAGE_CAPTURE);
		String extraStr_customerSummary = "selectedCustomerJSON";
		Bundle bundle = getIntent().getExtras();

		if (getIntent().hasExtra(extraStr_customerSummary))
		{
			selectedCustomerJson = bundle.getString(extraStr_customerSummary);
			MobiculeLogger.verbose("selectedCustomerJson from customer Summary:" + selectedCustomerJson.toString());
		}
		fetchMroNumber();

	}

	Button.OnClickListener saveOnClickListener = new Button.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if(remarksLayout.getVisibility()==View.VISIBLE)
			{
				View view = JoinTicketing.this.getCurrentFocus();
				if (view != null) {  
				    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				    MobiculeLogger.verbose("Keyboard is hidden","hiddennnnnnnnn");
				}
			}
			ByteArrayOutputStream blob = new ByteArrayOutputStream();

			if (mImageBitmap == null)
			{
				//Toast.makeText(JoinTicketing.this, "Please take picture", Toast.LENGTH_LONG).show();
				return;
			}
			mImageBitmap.compress(CompressFormat.JPEG, 100 /* ignored for PNG */, blob);
			bitmapdata = blob.toByteArray();

			if (bitmapdata == null || (bitmapdata != null && bitmapdata.length == 0))
			{
				Toast.makeText(JoinTicketing.this, "Please take picture", Toast.LENGTH_LONG).show();
				return;
			}
			String imageStr = Base64.encodeBytes(bitmapdata);

			if (Constants.threePicsCapCnt <= 9)
			{
				if (Constants.threePicsCapCnt == 0)
				{
					joinTicketingBO.setMrsImage(imageStr);
					MobiculeLogger.verbose("Save First Image to BO");

					mImageView.setImageBitmap(null);
					mImageBitmap = null;

					buildImageJsonAndSaveInDb("MRS_IMAGE_1_PATH", imageStr, "");

					Toast.makeText(JoinTicketing.this, "MRS image has been saved successfully", Toast.LENGTH_SHORT)
							.show();

					Constants.threePicsCapCnt = 1;
					MobiculeLogger.verbose("saveOnClickListener - JoinTicketing------------------------");
					joinTicketingCommonDialog("Take Turbine Image");
					Toast.makeText(JoinTicketing.this, "Take Turbine image", Toast.LENGTH_SHORT).show();
					//dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
				}
				else if (Constants.threePicsCapCnt == 1)
				{
					joinTicketingBO.setTurbineImage(imageStr);
					buildImageJsonAndSaveInDb("TURBINE_IMAGE_PATH", imageStr, "");

					MobiculeLogger.verbose("Save Second Image to BO");
					Constants.threePicsCapCnt = 2;

					mImageView.setImageBitmap(null);
					mImageBitmap = null;

					Toast.makeText(JoinTicketing.this, "Turbine image has been saved successfully", Toast.LENGTH_SHORT)
							.show();
					showCustomDialog(linearTurbine);
					linearTurbine.setVisibility(View.VISIBLE);
					linearEvc.setVisibility(View.GONE);
					linearOutlet.setVisibility(View.GONE);
					btnOK.setOnClickListener(new View.OnClickListener()
					{

						@Override
						public void onClick(View v)
						{
							MobiculeLogger.verbose("editTextTurbineMeterReading:" + editTextTurbineMeterReading.getText().toString());
							
							String val = editTextTurbineMeterReading.getText().toString().trim();
							/*
							if (!val.equals("") || !val.equals("."))
							{
							View view = dialog.getCurrentFocus();//getCurrentFocus();
							if (view != null) {  
							    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
							    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
							 //   Toast.makeText(JoinTicketing.this, "Key board is hidden", Toast.LENGTH_SHORT).show();
							}
							}*/
							
							if (val.equals("") || val.equals("."))
							{
								Toast.makeText(getApplicationContext(), "Enter Turbine Meter Reading",
										Toast.LENGTH_SHORT).show();
							}
							else
							{	
								View view = dialog.getCurrentFocus();//getCurrentFocus();
								if (view != null) 
								{  
									InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
									imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
								}
								if (validateTenDigit(val))
								{
									joinTicketingBO.setTurbineMeterReading(editTextTurbineMeterReading.getText()
											.toString());
									dialog.dismiss();
									save.setVisibility(View.VISIBLE);
									cancel.setVisibility(View.VISIBLE);
									Toast.makeText(getApplicationContext(), " Turbine Meter Reading Saved ",
											Toast.LENGTH_SHORT).show();
									joinTicketingCommonDialog("Take 1st Image Of EVC ");
									//dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
								}
								else
								{
									Toast.makeText(getApplicationContext(),
											"Enter max ten digits before decimal and max three digits after decimal",
											Toast.LENGTH_LONG).show();
								}

							}

						}
					});

					MobiculeLogger.verbose("Open the Turbine Pop Up Reading.");
				}
				else if (Constants.threePicsCapCnt == 2)
				{
					joinTicketingBO.setEvcImage1(imageStr);

					buildImageJsonAndSaveInDb("EVC_IMAGE_1_PATH", imageStr, "");

					MobiculeLogger.verbose("Save Third Image to BO");
					Constants.threePicsCapCnt = 3;

					mImageView.setImageBitmap(null);
					mImageBitmap = null;

					Toast.makeText(JoinTicketing.this, "1st Image of EVC has been saved successfully",
							Toast.LENGTH_SHORT).show();
					//Toast.makeText(JoinTicketing.this, "Take 1st image of EVC", Toast.LENGTH_SHORT).show();
					//joinTicketingCommonDialog("Take 1st Image Of EVC ");
					//dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
					MobiculeLogger.verbose("Open the Evc Corrected and UnCorrecred Pop Up Reading.");
					showCustomDialog(linearEvc);
					linearTurbine.setVisibility(View.GONE);
					linearEvc.setVisibility(View.VISIBLE);
					linearOutlet.setVisibility(View.GONE);
					btnOK.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							MobiculeLogger.verbose("editTextEvcCorrected:" + editTextEvcCorrected.getText().toString());
							MobiculeLogger.verbose("editTextEvcUnCorrected:" + editTextEvcUnCorrected.getText().toString());

							String val1 = editTextEvcCorrected.getText().toString().trim();
							String val2 = editTextEvcUnCorrected.getText().toString().trim();
							
							
							if (val1.equals("") || val2.equals("") || val1.equals(".") || val2.equals("."))
							{
								Toast.makeText(getApplicationContext(), "All fields Compulsary", Toast.LENGTH_SHORT)
										.show();
							}
							
							else
							{
								View view = dialog.getCurrentFocus();//getCurrentFocus();
								if (view != null) 
								{  
									InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
									imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
								}
								
								if (validateTenDigit(val1))
								{
									if (validateTenDigit(val2))
									{
										joinTicketingBO.setCorrectedValue(editTextEvcCorrected.getText().toString());
										joinTicketingBO
												.setUnCorrectedValue(editTextEvcUnCorrected.getText().toString());
										dialog.dismiss();
										save.setVisibility(View.VISIBLE);
										cancel.setVisibility(View.VISIBLE);
										Toast.makeText(getApplicationContext(),
												"Corrected And UnCorrected Values Saved", Toast.LENGTH_SHORT).show();
										joinTicketingCommonDialog("Take 2nd Image Of EVC");
										//dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
									}
									else
									{
										/*Toast.makeText(
												getApplicationContext(),
												"Enter max ten digits before decimal and max three digits after decimal in Evc UnCorrected field",
												10000).show();*/
										
										final Toast toast = Toast.makeText(getApplicationContext(), "Enter max ten digits before decimal and max three digits after decimal in Evc UnCorrected field", Toast.LENGTH_LONG);
									    toast.show();

									    Handler handler = new Handler();
									        handler.postDelayed(new Runnable() {
									           @Override
									           public void run() {
									               toast.cancel(); 
									           }
									    }, 5000);
									}

								}
								else
								{
									/*Toast.makeText(
											getApplicationContext(),
											"Enter max ten digits before decimal and max three digits after decimal in Evc Corrected field",
											10000).show();*/
									
									final Toast toast = Toast.makeText(getApplicationContext(), "Enter max ten digits before decimal and max three digits after decimal in Evc Corrected field", Toast.LENGTH_LONG);
								    toast.show();

								    Handler handler = new Handler();
								        handler.postDelayed(new Runnable() {
								           @Override
								           public void run() {
								               toast.cancel(); 
								           }
								    }, 5000);
								}
							}
							
						}
					});

				}
				else if (Constants.threePicsCapCnt == 3)
				{
					joinTicketingBO.setEvcImage2(imageStr);

					buildImageJsonAndSaveInDb("EVC_IMAGE_2_PATH", imageStr, "");

					MobiculeLogger.verbose("Save Fourth Image to BO");
					Constants.threePicsCapCnt = 4;

					mImageView.setImageBitmap(null);
					mImageBitmap = null;

					Toast.makeText(JoinTicketing.this, "2nd image of EVC has been saved successfully",
							Toast.LENGTH_SHORT).show();
					MobiculeLogger.verbose("Open OutLetPressure Pop Up Reading.");
					showCustomDialog(linearOutlet);
					linearTurbine.setVisibility(View.GONE);
					linearEvc.setVisibility(View.GONE);
					linearOutlet.setVisibility(View.VISIBLE);
					btnOK.setOnClickListener(new View.OnClickListener()
					{

						@Override
						public void onClick(View v)
						{
							MobiculeLogger.verbose("editTextOutletPre:" + editTextOutletPre.getText().toString());
							MobiculeLogger.verbose("editTextCorrectionFac:" + editTextCorrectionFac.getText().toString());
							MobiculeLogger.verbose("editTextTemperature:" + editTextTemperature.getText().toString());
							MobiculeLogger.verbose("editTextRemarks:" + editTextRemarks.getText().toString());

							String val1, val2, val3, val4;
							MobiculeLogger.verbose("JoinTicketing-val1");
							val1 = editTextOutletPre.getText().toString().trim();
							MobiculeLogger.verbose("JoinTicketing-val2");
							val2 = editTextTemperature.getText().toString().trim();
							MobiculeLogger.verbose("JoinTicketing-val3");
							val3 = editTextCorrectionFac.getText().toString().trim();
							MobiculeLogger.verbose("JoinTicketing-val4");
							val4 = editTextRemarks.getText().toString().trim();

							if (val3.equals(""))
							{
								Toast.makeText(getApplicationContext(), "Correction Factor is Compulsary",
										Toast.LENGTH_SHORT).show();
							}
							else
							{
								try
								{
									double read1 = 0, read2 = 0, read3 = 0;
									if (!val1.equals("") && validateSixDigit(val1))
									{
										read1 = Double.parseDouble(val1);
									}
									if (!val2.equals("") && validateSixDigit(val2))
									{
										read2 = Double.parseDouble(val2);
									}
									if (!val3.equals("") && validateSixDigit(val3))
									{
										read3 = Double.parseDouble(val3);
									}
									
									if (read1 < 16)
									{
										if (read2 >= 5 && read2 < 51)
										{
											if (read3 < 16)
											{
											/*	View view = dialog.getCurrentFocus();//getCurrentFocus();
												if (view != null) 
												{  
													InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
													imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
												}
											*/	
												joinTicketingBO.setOutletPressure(val1);
												joinTicketingBO.setTemperature(val2);
												joinTicketingBO.setCorrectionFactor(val3);
												joinTicketingBO.setJoinRemarks(val4);

												dialog.dismiss();
												save.setVisibility(View.VISIBLE);
												cancel.setVisibility(View.VISIBLE);
												Toast.makeText(getApplicationContext(), "Values Saved",
														Toast.LENGTH_SHORT).show();
												joinTicketingCommonDialog("Take Turbine Seal Image");
												//dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
											}
											
											else
											{
												Toast.makeText(getApplicationContext(),"Enter Correction Factor value less than 16 in format XX.XXX",
														Toast.LENGTH_LONG).show();
											}
										}
										else
										{
											Toast.makeText(getApplicationContext(),"Enter Temperature value between 5 to 50.999 in XX.XXX format",
													Toast.LENGTH_LONG).show();
										}
									}
									else
									{
										Toast.makeText(getApplicationContext(),"Enter Pressure value less than 16 in XX.XXX format",
												Toast.LENGTH_LONG).show();
									}
								}
								catch (Exception e)
								{
									Toast.makeText(getApplicationContext(), "Enter valid data", Toast.LENGTH_SHORT).show();
								}

							}

						}
					});
				}
				else if (Constants.threePicsCapCnt == 4)
				{
					joinTicketingBO.setTurbineSealImage(imageStr);

					buildImageJsonAndSaveInDb("TURBINE_SEAL_IMAGE_PATH", imageStr, "");

					MobiculeLogger.verbose("Save Fifth Image to BO");
					Constants.threePicsCapCnt = 5;

					mImageView.setImageBitmap(null);
					mImageBitmap = null;

					Toast.makeText(JoinTicketing.this, "Turbine seal image has been saved successfully",
							Toast.LENGTH_SHORT).show();
					MobiculeLogger.verbose("Open OutLetPressure Pop Up Reading.");
					joinTicketingCommonDialog("Take EVC Seal Image");
					
				}
				else if (Constants.threePicsCapCnt == 5)
				{
					joinTicketingBO.setEvcSealImage(imageStr);

					buildImageJsonAndSaveInDb("EVC_SEAL_IMAGE_PATH", imageStr, "");

					MobiculeLogger.verbose("Save Sixth Image to BO");
					Constants.threePicsCapCnt = 6;

					mImageView.setImageBitmap(null);
					mImageBitmap = null;

					Toast.makeText(JoinTicketing.this, "EVC seal image has been saved successfully", Toast.LENGTH_SHORT)
							.show();
					//Toast.makeText(JoinTicketing.this, "Take MRS Locked image", Toast.LENGTH_SHORT).show();
					joinTicketingCommonDialog("Take MRS Locked Image");
					//dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);

				}
				else if (Constants.threePicsCapCnt == 6)
				{
					joinTicketingBO.setMrsLockedImage(imageStr);

					buildImageJsonAndSaveInDb("MRS_LOCKED_IMAGE_PATH", imageStr, "");

					MobiculeLogger.verbose("Save Seventh Image to BO");
					Constants.threePicsCapCnt = 7;

					mImageView.setImageBitmap(null);
					mImageBitmap = null;

					Toast.makeText(JoinTicketing.this, "MRS Locked image has been Saved Successfully",
							Toast.LENGTH_SHORT).show();
					MobiculeLogger.verbose("Draw 2 Signatures" + joinTicketingBO.toJSON());
					
					joinTicketingCommonDialog("Take image of left side of the meter box.");//new
					
				}
				else if (Constants.threePicsCapCnt == 7)
				{
					joinTicketingBO.setMeterBoxLeftSideImage(imageStr);
					joinTicketingBO.setMeterBoxLeftSideImageRemark(remarksEditText.getText().toString());

					buildImageJsonAndSaveInDb("METER_LEFT_SIDE_IMAGE", imageStr, remarksEditText.getText().toString());

					MobiculeLogger.verbose("Save eighth Image to BO");
					Constants.threePicsCapCnt = 8;

					mImageView.setImageBitmap(null);
					mImageBitmap = null;
					remarksEditText.setText("");

					Toast.makeText(JoinTicketing.this, "Meter left side image has been saved Successfully",
							Toast.LENGTH_SHORT).show();
					
					joinTicketingCommonDialog("Take image of rear side of the meter box.");
					
					
					
				}
				else if (Constants.threePicsCapCnt == 8)
				{
					joinTicketingBO.setMeterBoxRearSideImage(imageStr);
					joinTicketingBO.setMeterBoxRearSideImageRemark(remarksEditText.getText().toString());

					buildImageJsonAndSaveInDb("METER_REAR_SIDE_IMAGE", imageStr, remarksEditText.getText().toString());

					MobiculeLogger.verbose("Save ninth Image to BO");
					Constants.threePicsCapCnt = 9;

					mImageView.setImageBitmap(null);
					mImageBitmap = null;
					remarksEditText.setText("");

					Toast.makeText(JoinTicketing.this, "Meter rear side image has been saved Successfully",
							Toast.LENGTH_SHORT).show();
					
					joinTicketingCommonDialog("Take image of right side of the meter box.");
					
				}
				else if (Constants.threePicsCapCnt == 9)
				{
					joinTicketingBO.setMeterBoxRightSideImage(imageStr);
					joinTicketingBO.setMeterBoxRightSideImageRemark(remarksEditText.getText().toString());

					buildImageJsonAndSaveInDb("METER_RIGHT_SIDE_IMAGE", imageStr, remarksEditText.getText().toString());

					MobiculeLogger.verbose("Save tenth Image to BO");
					Constants.threePicsCapCnt = 10;

					mImageView.setImageBitmap(null);
					mImageBitmap = null;
					remarksEditText.setText("");

					Toast.makeText(JoinTicketing.this, "Meter right side image has been saved Successfully",
							Toast.LENGTH_SHORT).show();
					MobiculeLogger.verbose("Draw 2 Signatures" + joinTicketingBO.toJSON());

					Intent intent = new Intent(JoinTicketing.this, JoinTicketingSummary.class);
					intent.putExtra("selectedCustomerJSON", selectedCustomerJson);
					startActivity(intent);
					finish();
				}

			}

		}
	};

	Button.OnClickListener cancelOnClickListener = new Button.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			finish();
		}
	};


	Button.OnClickListener cropImageListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {

			if (mImageBitmap == null)
			{
				Toast.makeText(JoinTicketing.this, "Please take picture", Toast.LENGTH_SHORT).show();
				return;
			}

			else {

//				new cropImageAsyncTask(JoinTicketing.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new cropImageAsyncTask(JoinTicketing.this).execute();
			}


		}
	};

	private void setBtnListenerOrDisable(Button btn, Button.OnClickListener onClickListener, String intentName)
	{
		if (isIntentAvailable(this, intentName))
		{
			btn.setOnClickListener(onClickListener);
		}
		else
		{
			btn.setText(getText(R.string.cannot).toString() + " " + btn.getText());
			btn.setClickable(false);
		}
	}

	public void joinTicketingCommonDialog(String msg)
	{
		MobiculeLogger.verbose("joinTicketingCommonDialog");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		builder.setInverseBackgroundForced(true);
		builder.setMessage(msg);
		builder.setCancelable(false);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{ 
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(((Dialog) dialog).getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
	       
	            MobiculeLogger.verbose("IntojoinTicketingCommonDialog - msg222222222222222222");
//				dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);


				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

					if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

						requestPermissions(new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
					} else {
						dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
					}

				} else {
					dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
				}


				MobiculeLogger.verbose("dispatchTakePictureIntent - msg7777777777777777777");
				dialog.dismiss();
			}

		});
		
		if(Constants.threePicsCapCnt == 7 || Constants.threePicsCapCnt == 8 || Constants.threePicsCapCnt == 9)
		{
			remarksLayout.setVisibility(View.VISIBLE);
		}
		else
		{
			remarksLayout.setVisibility(View.GONE);
		}

		AlertDialog alert = builder.create();
		MobiculeLogger.verbose("IntojoinTicketingCommonDialog - msg3333333333333333");
		alert.show();
	}

	public static boolean isIntentAvailable(Context context, String action)
	{
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	private void dispatchTakePictureIntent(int actionCode)
	{
		MobiculeLogger.verbose("dispatchTakePictureIntent");
		/*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(takePictureIntent, actionCode);*/

		/*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, "NewPicture");
		MobiculeLogger.verbose("dispatchTakePictureIntent - msg55555555555555");
		imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		MobiculeLogger.verbose("imageUri - "+imageUri);
		MobiculeLogger.verbose("imageUri - "+imageUri.toString());
		takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		MobiculeLogger.verbose("dispatchTakePictureIntent - msg666666666666");
		startActivityForResult(takePictureIntent, actionCode);*/

		if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {


			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

				try {

					photoFile = createPhotoFile();

					if (photoFile != null ) {
						imageUri = FileProvider.getUriForFile(this,"com.mahanagar",photoFile);
						takePictureIntent.putExtra("return-data",true);
						takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
						takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//						takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,imageUri);
						Log.e("EXTRA_OUTPUT_greater",""+imageUri);
						startActivityForResult(takePictureIntent, actionCode);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}


		} else {

			try
			{

				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//File temFile = new File(Environment.getDownloadCacheDirectory() + "/msales_images/" + System.currentTimeMillis() + ".jpg");

				File temFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + System.currentTimeMillis() + ".jpg");
				if (!temFile.exists())
				{
					temFile.createNewFile();
				}
				imageUri = Uri.fromFile(temFile);
				takePictureIntent.putExtra("return-data",true);
				takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//				takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,imageUri);
				Log.e("EXTRA_OUTPUT_less",""+imageUri);
				startActivityForResult(takePictureIntent, actionCode);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
	}

	private File createPhotoFile() throws IOException{
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "MGL_" + timeStamp + "_";
		File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = image.getAbsolutePath();
		galleryAddPic();
		return image;

	}

	// need to check for image quality and confirm


	private void galleryAddPic() {

		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(String.valueOf(photoFile));
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null));
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
		mImageView.setImageBitmap(mImageBitmap);
		mImageView.setVisibility(savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ? ImageView.VISIBLE
				: ImageView.INVISIBLE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		MobiculeLogger.verbose("onActivityResult");

		switch (requestCode)
		{
			case ACTION_TAKE_PHOTO_S:
			{
				if (resultCode == RESULT_OK)
				{
					MobiculeLogger.verbose("onActivityResult - RESULT_OK");
					handleSmallCameraPhoto(data);
					deleteingCapturedImage();
				}
				else
				{
					MobiculeLogger.verbose("onActivityResult - cancel from camera");

					joinTicketingCommonDialog("Image was not clicked. Please take again!");
				}
				break;
			} // ACTION_TAKE_PHOTO_S

			case CROP_FROM_CAMERA:
			{
				if (resultCode == RESULT_OK)
				{
					try {
//						Uri imageUri = data.getData();
//						mImageBitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//						mImageView.setImageBitmap(mImageBitmap);
//						mImageView.setVisibility(View.VISIBLE);


						if(data.getData()==null){
							mImageBitmap = (Bitmap)data.getExtras().get("data");
						}else{
							mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
						}

						mImageView.setImageBitmap(mImageBitmap);
						mImageView.setVisibility(View.VISIBLE);

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		} // switch
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);


		if (requestCode == PERMISSION_REQUEST_CODE) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Now user should be able to use camera

				dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
			}
			else {
				// Your app will not have this permission. Turn off all functions
				// that require this permission or it will force close like your
				// original question

				Toast.makeText(this, "Permission need to be granted for access camera.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void handleSmallCameraPhoto(Intent intent)
	{

		try
		{

			/*mImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
			mImageBitmap = Bitmap.createScaledBitmap(mImageBitmap, 390, 280, false);*/

			//Uri imgUri = intent.getData();
		/*	BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, o);

			//The new size we want to scale to
			final int REQUIRED_SIZE = 140;

			//Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			//Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			mImageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, o2);
*/

			if (imageUri != null) {

				MobiculeLogger.verbose("imageUri - "+imageUri.toString());

				String path = imageUri.getPath();
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					replacedStringPath = "/storage/emulated/0" + path.replaceAll("/shared_files", "");
					//replacedStringPath = (Environment.getDownloadCacheDirectory() + "/msales_images/");

					path = replacedStringPath;
					uri = Uri.parse(replacedStringPath);
					mImageBitmap = BitmapFactory.decodeFile(replacedStringPath, null);
				} else {
					uri = Uri.parse(path);
					mImageBitmap = BitmapFactory.decodeFile(path, null);
				}
                FileOutputStream out = null;
                String filename = replacedStringPath;



				try
				{
                    out = new FileOutputStream(filename);

                    int maxSize = 140;
					int outWidth;
					int outHeight;
					int inWidth = mImageBitmap.getWidth();
					int inHeight = mImageBitmap.getHeight();
					if (inWidth > inHeight)
					{
						outWidth = maxSize;
						outHeight = (inHeight * maxSize) / inWidth;
					}
					else
					{
						outHeight = maxSize;
						outWidth = (inWidth * maxSize) / inHeight;
					}

					mImageBitmap = Bitmap.createScaledBitmap(mImageBitmap, outWidth, outHeight, false);
                   // mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                }
				catch (Exception e)
				{
					e.printStackTrace();
				}

				mImageBitmap = rotateImage(mImageBitmap);
				mImageView.setImageBitmap(mImageBitmap);
				mImageView.setVisibility(View.VISIBLE);
				save.setEnabled(true);

			} else {
				Toast.makeText(getApplicationContext(), "Couldn't display image", Toast.LENGTH_LONG).show();
				return;
			}

		}
		catch (Exception e)
		{
			/*IGenericExceptionHandler handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
			handler.setIsLogEnable(true);*/
			e.printStackTrace();
		}

	}

	//private void dialogFor

	/*public void deleteingCapturedImage()
	{

		try 
		{
			getContentResolver().delete(imageUri, null, null);
			MobiculeLogger.verbose("Photo Deleted : " + imageUri.getPath());
		}
		catch (Exception e)
		{
			*//*IGenericExceptionHandler handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
			handler.setIsLogEnable(true);*//*
			MobiculeLogger.verbose("deleteCaptureImage() - " + e.toString());
			e.printStackTrace();
		}

		String[] projection = { MediaStore.Images.ImageColumns.SIZE, MediaStore.Images.ImageColumns.DISPLAY_NAME,
				MediaStore.Images.ImageColumns.DATA, BaseColumns._ID, };

		Cursor c = null;
		Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		MobiculeLogger.verbose("on activityresult Uri u " + u.toString());

		try
		{
			if (u != null)
			{
				c = managedQuery(u, projection, null, null, null);
			}
			if ((c != null) && (c.moveToLast()))
			{
				MobiculeLogger.verbose("c.getString(0) " + c.getString(0));
				MobiculeLogger.verbose("c.getString(1) " + c.getString(1));
				MobiculeLogger.verbose("c.getString(2) " + c.getString(2));
				MobiculeLogger.verbose("c.getString(3) " + c.getString(3));

				ContentResolver cr = getContentResolver();
				int i = cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, BaseColumns._ID + "=" + c.getString(3),
						null);

				MobiculeLogger.verbose("Number of column deleted : " + i);

			}
		}
		catch (Exception e)
		{
			*//*IGenericExceptionHandler handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
			handler.setIsLogEnable(true);*//*
			e.printStackTrace();
		}
	}*/


	public void deleteingCapturedImage()
	{
		try
		{
            if ( uri != null) {

                MobiculeLogger.error(TAG, "URI path - " + uri.getPath());
                // getContentResolver().delete(imageUri, null, null);
                File file = new File(uri.getPath());
                if (file.exists()) {
                    file.delete();
                }
                MobiculeLogger.error(TAG, "Photo Deleted - " + uri.getPath());
            }
		}
		catch (Exception e)
		{
			MobiculeLogger.error(TAG, "deleteCaptureImage() - " + e.toString());
			e.printStackTrace();
		}

	}

	protected void showCustomDialog(LinearLayout linearLayout)
	{
		dialog = new Dialog(JoinTicketing.this);
		//dialog.setCancelable(false);
		/*dialog.setOnKeyListener(new DialogInterface.OnKeyListener()
		{
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
			{
				onBackPressed();
				return true;
			}
		});*/

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog_join_ticketing);
		save.setVisibility(View.GONE);
		cancel.setVisibility(View.GONE);
		editTextTurbineMeterReading = (EditText) dialog.findViewById(R.id.edittext_turbineMeterreading);

		editTextEvcCorrected = (EditText) dialog.findViewById(R.id.edittext_EvcCorrected);
		editTextEvcUnCorrected = (EditText) dialog.findViewById(R.id.edittext_EvcUnCorrected);

		editTextOutletPre = (EditText) dialog.findViewById(R.id.edittext_OutLet_Pressure);
		editTextCorrectionFac = (EditText) dialog.findViewById(R.id.edittext_Correction_Factor);
		editTextTemperature = (EditText) dialog.findViewById(R.id.edittext_Temperature);
		editTextRemarks = (EditText) dialog.findViewById(R.id.edittext_Remarks);

		linearTurbine = (LinearLayout) dialog.findViewById(R.id.linear_turbine);
		linearEvc = (LinearLayout) dialog.findViewById(R.id.linear_Evc);
		linearOutlet = (LinearLayout) dialog.findViewById(R.id.linear_Pressure);
		btnOK = (Button) dialog.findViewById(R.id.btn_Ok);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				finish();
			}
		});

		dialog.show();
	}

	@Override
	public void onBackPressed()
	{
	}

	private Bitmap rotateImage(Bitmap bitmap)
	{
		Bitmap newBitmap = null;
		try
		{
//			ExifInterface ei = new ExifInterface(getRealPathFromURI(imageUri));
			ExifInterface ei = new ExifInterface(uri.getPath());
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

			Matrix mat = new Matrix();

			switch (orientation)
			{
				case ExifInterface.ORIENTATION_ROTATE_90:
					mat.postRotate(90);
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					mat.postRotate(90);
					break;
			}

			newBitmap = Bitmap.createBitmap(mImageBitmap, 0, 0, mImageBitmap.getWidth(), mImageBitmap.getHeight(), mat,
					true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return newBitmap;
	}

	public String getRealPathFromURI(Uri contentUri)
	{
		try
		{
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}

	private boolean validateTenDigit(String val)
	{
		if (val.contains("."))
		{
			String[] strSplit = val.split("\\.");

			if(strSplit.length == 2)
			{
				if (strSplit[0].length() <= 10 && strSplit[1].length() <= 3)
				{
					return true;
				}
			}
		}
		else if (val.length() <= 10)
		{
			return true;
		}
		return false;
	}

	private boolean validateSixDigit(String val)
	{
		if (val.contains("."))
		{
			String[] strSplit = val.split("\\.");

			if (strSplit.length == 2)
			{
				if (strSplit[1].length() <= 3)
				{
					return true;
				}
			}
		}
		else
		{
			return true;
		}
		return false;
	}

	private void fetchMroNumber()
	{
		try
		{

			JSONObject jointicket = new JSONObject(selectedCustomerJson);

			joinTicketingBO.setMroNo(jointicket.getValue("mroNo").toString());
			MobiculeLogger.verbose("joinTicketingBO.setMroNumber - " + jointicket.getValue("mroNo").toString());

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	class LocationLooperThread extends Thread
	{
		public Handler mHandler;

		@Override
		public void run()
		{
			Looper.prepare();
			Message msg = new Message();
			msg.setTarget(mHandler);
			mHandler = new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					new LocationSensor(JoinTicketing.this, null);
				}
			};
			mHandler.sendMessage(msg);
			Looper.loop();
		}
	}

	public class cropImageAsyncTask extends AsyncTask<Void, Void, Void>
	{
		private Context context;

		private ProgressDialog progressDialog;

		public cropImageAsyncTask(Context _context)
		{
			context = _context;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			progressDialog = new ProgressDialog(context);
			this.progressDialog.setMessage("Loading...");
			this.progressDialog.setCancelable(false);
			this.progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setType("image/*");

			List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

			int size = list.size();

			if (size == 0)
			{
				//TODO TOAST
				// CustomToast.getInstance(MITCImageCapture.this).showCustomToast("Cannot find image crop app");

			}
			else
			{
//				Bitmap bitmap = getBitmapFromCaptureImageView();
//				listOfImages = new ArrayList<Bitmap>();
//				listOfImages.add(bitmap);

				Uri mImageCaptureUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
						getBitmapFromCaptureImageView(), "some Title", "some_Description"));

				intent.setData(mImageCaptureUri);

				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 4);
				intent.putExtra("aspectY", 3);
				intent.putExtra("outputX", 400);
				intent.putExtra("outputY", 300);
				intent.putExtra("scale", true);

				intent.putExtra("return-data", true);

				MobiculeLogger.info("CROP ", "if Size == 0");
				//flagForGps = false;
				startActivityForResult(intent, CROP_FROM_CAMERA);

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			progressDialog.dismiss();
		}

		public  Bitmap getBitmapFromCaptureImageView()
		{
			Drawable drawable = mImageView.getDrawable();
			BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
			Bitmap bitmap = bitmapDrawable.getBitmap();

			return bitmap;
		}

	}


}
