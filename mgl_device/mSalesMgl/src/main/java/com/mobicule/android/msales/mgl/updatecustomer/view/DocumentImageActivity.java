package com.mobicule.android.msales.mgl.updatecustomer.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.util.Base64;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DocumentImageActivity extends DefaultUpdateCustomerActivity
{
	private static final int ACTION_TAKE_PHOTO_S = 2;

	//private Uri imageUri;

	private String TAG = "DocumentImageActivity";

	private static final int PERMISSION_REQUEST_CODE = 7;

	private Bitmap mImageBitmap;

	private ImageView mImageView;

	private Button save, cancel, takePhoto;

	private int imgCount;

	private String getUpdateCustomerJson, selectedCustomerJson, getUpdatedListNoImage;

	private Uri backupImageUri;

	private String mCurrentPhotoPath;

	File photoFile;

	String path;

	File mediaDir;

	String replacedStringPath = "";

	Uri uri = null;

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

		setContentView(R.layout.document_image);

		try
		{
			mediaDir = new File(Environment.getExternalStorageDirectory().getPath() + "/images/");
			mediaDir.mkdirs();

		}
		catch (Exception e)
		{
			handler.logCrashReport(e);
			e.printStackTrace();
		}

		if (getIntent().hasExtra("selectedCustomerJSON"))
		{
			Bundle extras = getIntent().getExtras();
			selectedCustomerJson = extras.getString("selectedCustomerJSON");

		}

		MobiculeLogger.verbose("selectedCustomerJSON: " + selectedCustomerJson);

		Bundle extras = getIntent().getExtras();
		getUpdateCustomerJson = extras.getString("updateCustomer");
		getUpdatedListNoImage = extras.getString("updateCustomer");

		mImageView = (ImageView) findViewById(R.id.docimageview);
		save = (Button) findViewById(R.id.docsaveButton);
		cancel = (Button) findViewById(R.id.doccancelButton);
		takePhoto = (Button) findViewById(R.id.docImgButton);
		mImageBitmap = null;

		imgCount = 0;

		updateCustomerBO.setDocImgOne("");
		updateCustomerBO.setDocImgTwo("");
		updateCustomerBO.setDocImgThree("");

		save.setOnClickListener(saveOnClickListener);

		cancel.setOnClickListener(cancelOnClickListener);

		takePhoto.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//joinTicketingCommonDialog("Take image of document");
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
			}
		});
	}

	Button.OnClickListener cancelOnClickListener = new Button.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			//finish();
			Button tempButton = (Button) v;
			String str = tempButton.getText().toString();
			MobiculeLogger.verbose("tempButton.getText(): " + str);

			if (str.equals("Next"))
			{
				Intent intent = new Intent(DocumentImageActivity.this, UpdateCustomerFinalSummaryActivity.class);
				intent.putExtra("updateCustomer", getUpdateCustomerJson);
				intent.putExtra("selectedCustomerJSON", selectedCustomerJson);

				startActivity(intent);
				finish();
			}
			else
			{
				finish();
			}
		}
	};

	Button.OnClickListener saveOnClickListener = new Button.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			ByteArrayOutputStream blob = new ByteArrayOutputStream();

			if (mImageBitmap == null)
			{
				Toast.makeText(DocumentImageActivity.this, "Please take image", Toast.LENGTH_LONG).show();
				return;
			}

			mImageBitmap.compress(CompressFormat.PNG, 0, blob);

			byte[] bitmapdata = blob.toByteArray();

			if (bitmapdata == null || (bitmapdata != null && bitmapdata.length == 0))
			{
				Toast.makeText(DocumentImageActivity.this, "Please take image", Toast.LENGTH_LONG).show();
				return;
			}
			String imageStr = Base64.encodeBytes(bitmapdata);

			if (imgCount == 0)
			{
				updateCustomerBO.setDocImgOne(imageStr);

				mImageView.setImageBitmap(null);
				mImageBitmap = null;

				Toast.makeText(DocumentImageActivity.this, "1st image of document saved successfully",
						Toast.LENGTH_SHORT).show();

				imgCount = 1;

				//cancel.setText("Next");

				//joinTicketingCommonDialog("Take 2nd image of document");
			}
			else if (imgCount == 1)
			{
				updateCustomerBO.setDocImgTwo(imageStr);

				mImageView.setImageBitmap(null);
				mImageBitmap = null;

				Toast.makeText(DocumentImageActivity.this, "2nd image of document saved successfully",
						Toast.LENGTH_SHORT).show();

				imgCount = 2;

				//joinTicketingCommonDialog("Take 3rd image of document");
			}
			else if (imgCount == 2)
			{
				updateCustomerBO.setDocImgThree(imageStr);

				mImageView.setImageBitmap(null);
				mImageBitmap = null;

				Toast.makeText(DocumentImageActivity.this, "3rd image of document saved successfully",
						Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(DocumentImageActivity.this, UpdateCustomerFinalSummaryActivity.class);
				intent.putExtra("updateCustomer", getUpdateCustomerJson);
				intent.putExtra("selectedCustomerJSON", selectedCustomerJson);
				startActivity(intent);
				finish();
			}
		}
	};

	private void joinTicketingCommonDialog(String msg)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg);
		builder.setCancelable(false);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
				dialog.dismiss();
			}

		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public static Uri getImageContentUri(Context context, File imageFile)
	{
		String filePath = imageFile.getAbsolutePath();
		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
				new String[] { filePath }, null);
		if (cursor != null && cursor.moveToFirst())
		{
			int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
			cursor.close();//// first cursor closed
			return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
		}
		else
		{
			if (imageFile.exists())
			{
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.DATA, filePath);
				return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			}
			else
			{
				return null;
			}
		}
	}

	private void dispatchTakePictureIntent(int actionCode)
	{
//		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//		ContentValues values = new ContentValues();
//		values.put(MediaStore.Images.Media.TITLE, "NewPicture");
//		backupImageUri = getImageContentUri(this, mediaDir);
//		backupImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//		//BackupImageUri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//		takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		//takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//
//		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, backupImageUri);
//
//		startActivityForResult(takePictureIntent, actionCode);


		if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {


			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

				try {

					photoFile = createPhotoFile();

					if (photoFile != null ) {
						backupImageUri = FileProvider.getUriForFile(this,"com.mahanagar",photoFile);
						takePictureIntent.putExtra("return-data",true);
						takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
						takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, backupImageUri);
//						takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,imageUri);
						Log.e("EXTRA_OUTPUT_greater",""+backupImageUri);
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
				backupImageUri = Uri.fromFile(temFile);
				takePictureIntent.putExtra("return-data",true);
				takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, backupImageUri);
//				takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,imageUri);
				Log.e("EXTRA_OUTPUT_less",""+backupImageUri);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		MobiculeLogger.verbose("onActivityResult - camera");

		switch (requestCode)
		{
			case ACTION_TAKE_PHOTO_S:
			{
				if (resultCode == RESULT_OK)
				{

					/*if(data==null)
					{
						Toast.makeText(DocumentImageActivity.this,"Error in capturing the image please try again",Toast.LENGTH_SHORT).show();
					}*/
					//else
					//{
					handleSmallCameraPhoto(data);
					deleteingCapturedImage();
					//}
				}
				else
				{
					MobiculeLogger.verbose("onActivityResult - cancel from camera");

					joinTicketingCommonDialog("Image was not clicked. Please take again!");
				}
				break;
			} // ACTION_TAKE_PHOTO_S

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

			if (backupImageUri != null) {
				String path = backupImageUri.getPath();
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

				try
				{
					FileOutputStream out = null;
					String filename = replacedStringPath;
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

				//mImageBitmap = rotateImage(mImageBitmap);
				mImageView.setImageBitmap(mImageBitmap);
				mImageView.setVisibility(View.VISIBLE);
				save.setEnabled(true);


			} else {
				Toast.makeText(getApplicationContext(), "Couldn't display image", Toast.LENGTH_LONG).show();
				return;
			}


			/*BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			//	BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, o);
			*//*FileInputStream inputStream  = new FileInputStream(BackupImageUri.toString());
			FileInputStream inputStreamm  =BackupImageUri.openStream();
			*//*
			BitmapFactory.decodeStream(getContentResolver().openInputStream(backupImageUri), null, o);

			//The new size we want to scale to
			final int REQUIRED_SIZE = 140;

			//Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			//Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			mImageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(backupImageUri), null, o2);
			//mImageBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
			//mImageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, o2);
			if (mImageBitmap == null)
			{
				MobiculeLogger.verbose("ImageBitmap is");
			}*/



		}
		catch (Exception e)
		{

			handler.logCrashReport(e);
			e.printStackTrace();
		}
	}

	private Bitmap rotateImage(Bitmap bitmap)
	{
		Bitmap newBitmap = null;
		try
		{
			//ExifInterface ei = new ExifInterface(getRealPathFromURI(imageUri));
//			ExifInterface ei = new ExifInterface(getRealPathFromURI(backupImageUri));

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

			handler.logCrashReport(e);
			e.printStackTrace();
		}

		return newBitmap;
	}

	public String getRealPathFromURI(Uri contentUri)
	{
		Cursor cursor = null;
		try
		{
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = managedQuery(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();

			return cursor.getString(column_index);

		}
		catch (Exception e)
		{

			handler.logCrashReport(e);
			e.printStackTrace();
			return "";

		}

	}

	/*public void deleteingCapturedImage()
	{
		try
		{
			getContentResolver().delete(backupImageUri, null, null);
			MobiculeLogger.verbose("deleteingCapturedImage - Photo Deleted : " + backupImageUri.getPath());
		}
		catch (Exception e)
		{

			handler.logCrashReport(e);
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

			handler.logCrashReport(e);
			e.printStackTrace();
		}

	}*/


	public void deleteingCapturedImage()
	{
		try
		{
            if (uri != null) {
                MobiculeLogger.error(TAG, "URI path - " + uri.getPath());
                // getContentResolver().delete(imageUri, null, null);
                File file = new File(uri.getPath());
                if (file.exists()) {
                    file.delete();
                }
                MobiculeLogger.error(TAG, "Photo Deleted - " + backupImageUri.getPath());
            }
		}
		catch (Exception e)
		{
			MobiculeLogger.error(TAG, "deleteCaptureImage() - " + e.toString());
			e.printStackTrace();
		}

	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add("Cancel");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getTitle().equals("Cancel"))
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}*/
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		if (backupImageUri != null)
		{
			outState.putString("cameraImageUri", backupImageUri.toString());
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.containsKey("cameraImageUri"))
		{
			backupImageUri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
		}
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();

	}
}
