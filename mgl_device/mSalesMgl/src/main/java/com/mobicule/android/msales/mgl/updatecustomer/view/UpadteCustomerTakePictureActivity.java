/**
******************************************************************************
* C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
* <p>
* Copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved. 
* This is proprietary information of Mobicule Technologies Pvt. Ltd.and is 
* subject to applicable licensing agreements. Unauthorized reproduction, 
* transmission or distribution of this file and its contents is a 
* violation of applicable laws.
******************************************************************************
*
* @project mSalesMgl
*/
package com.mobicule.android.msales.mgl.updatecustomer.view;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.msales.mgl.menu.view.MainMenu;
import com.mobicule.android.msales.mgl.meterreading.view.PhotoIntentActivity;
import com.mobicule.android.msales.mgl.meterreading.view.Summary;
import com.mobicule.android.msales.mgl.util.Base64;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;

/**
* 
* <enter description here>
*
* @author namrata <enter lastname>
* @see 
*
* @createdOn 21-Apr-2017
* @modifiedOn
*
* @copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class UpadteCustomerTakePictureActivity extends DefaultUpdateCustomerActivity implements OnClickListener
{

	public static final String EXTRA_IMG = "IMG";

	private Button b_onmSavePhoto, b_onmCancel, b_onmTakePicture;

	private ImageView iv_onmCapturedImage;

	private String Takephoto;

	private Bitmap thumbnail;

	private int REQUEST_CAMERA = 100;

	String strJson;

	Intent intent;

	private int imageCount = 0;

	private Bitmap mImageBitmap;

	private byte[] bitmapdata;

	private String strHoseAvailable;

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

		setContentView(R.layout.activity_onm_takepicture);

		initializecomponents();

		strHoseAvailable = getIntent().getStringExtra("strHoseAvailable");

		//strJson=getIntent().getStringExtra("Summary");
	}

	private void initializecomponents()
	{

		b_onmSavePhoto = (Button) findViewById(R.id.b_onmSavePhoto);
		b_onmCancel = (Button) findViewById(R.id.b_onmCancel);
		iv_onmCapturedImage = (ImageView) findViewById(R.id.iv_onmCapturedImage);
		b_onmTakePicture = (Button) findViewById(R.id.b_onmTakePicture);

		b_onmSavePhoto.setOnClickListener(this);
		b_onmCancel.setOnClickListener(this);
		b_onmTakePicture.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{

		switch (v.getId())
		{
			case R.id.b_onmSavePhoto:

				imageCount++;
				ByteArrayOutputStream blob = new ByteArrayOutputStream();
				if (mImageBitmap == null)
				{
					Toast.makeText(UpadteCustomerTakePictureActivity.this, "Please take picture", Toast.LENGTH_LONG)
							.show();
					return;
				}
				mImageBitmap.compress(CompressFormat.JPEG, 100 /* ignored for PNG */, blob);
				bitmapdata = blob.toByteArray();

				if (bitmapdata == null || (bitmapdata != null && bitmapdata.length == 0))
				{
					Toast.makeText(UpadteCustomerTakePictureActivity.this, "Please take picture", Toast.LENGTH_LONG)
							.show();
					return;
				}
				String imageStr = Base64.encodeBytes(bitmapdata);

				if (imageCount == 1)
				{
					updateCustomerBO.setDocImgOne(imageStr);

					iv_onmCapturedImage.setImageBitmap(null);
					mImageBitmap = null;
					Toast.makeText(UpadteCustomerTakePictureActivity.this, "Image 1 has been Saved Successfully",
							Toast.LENGTH_SHORT).show();
				}
				else if (imageCount == 2)
				{
					updateCustomerBO.setDocImgTwo(imageStr);
					iv_onmCapturedImage.setImageBitmap(null);
					mImageBitmap = null;

					Toast.makeText(UpadteCustomerTakePictureActivity.this, "Image 2 has been Saved Successfully",
							Toast.LENGTH_SHORT).show();

					Intent intent = new Intent(UpadteCustomerTakePictureActivity.this, SavedUpdateCustomerSummary.class);
					intent.putExtra("strHoseAvailable", strHoseAvailable);
					startActivity(intent);
					finish();
				}

				break;

			case R.id.b_onmCancel:

				Intent intentCancel = new Intent(UpadteCustomerTakePictureActivity.this, MainMenu.class);
				startActivity(intentCancel);
				break;
			case R.id.b_onmTakePicture:

				if (imageCount <= 2)
				{

					Intent cam_intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(cam_intent, REQUEST_CAMERA);

				}

			default:
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK)
		{

			Bundle extras = data.getExtras();
			mImageBitmap = (Bitmap) extras.get("data");

			iv_onmCapturedImage.setImageBitmap(mImageBitmap);
			iv_onmCapturedImage.setVisibility(View.VISIBLE);

			/*Bitmap mphoto = (Bitmap) data.getExtras().get("data");

			ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
			mphoto.compress(Bitmap.CompressFormat.PNG, 100, stream2);
			byte[] imageInByte = stream2.toByteArray();

			Bitmap bitmap = BitmapFactory.decodeByteArray(imageInByte, 0, imageInByte.length);
			iv_onmCapturedImage.setImageBitmap(mphoto);
			
			if(imageCount == 1)
			{
				onMPlanningBO.setImage(imageInByte.toString());
			}
			else if(imageCount == 2)
			{
				onMPlanningBO.setImage2(imageInByte.toString());
			}
			else if(imageCount == 3)
			{
				onMPlanningBO.setImage3(imageInByte.toString());
			}*/

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
