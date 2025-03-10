/**
 * 
 */
package com.mobicule.android.msales.mgl.randommeterreading.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
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
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.android.msales.mgl.util.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author nikita
 * 
 */
public class TakePicture extends DefaultRandomMeterReadingActivity
{
	private static final int ACTION_TAKE_PHOTO_S = 2;

	private static final int CROP_FROM_CAMERA = 5;

	private static final int PERMISSION_REQUEST_CODE = 7;

	private static final String BITMAP_STORAGE_KEY = "viewbitmap";

	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";

	private ImageView mImageView;

	private Button save, cancel,cropImage;

	private Bitmap mImageBitmap;

	private byte[] bitmapdata;

	private String TAG = "TakePicture";

	ArrayList<Bitmap>listOfImages;

	File photoFile;

	String replacedStringPath = "";

	Uri uri = null;

	private String mCurrentPhotoPath;

	private Uri imageUri;

	@Override
	protected void onStart()
	{
		super.onStart();
		Constants.broadcastDialogContext = this;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
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

//		Icepick.restoreInstanceState(this, savedInstanceState);
		setContentView(R.layout.saveimage);
Log.e("take Picture","TakePicture");
		mImageView = (ImageView) findViewById(R.id.imageview);
		save = (Button) findViewById(R.id.saveButton);
		cancel = (Button) findViewById(R.id.cancelButton);
		cropImage = (Button)findViewById(R.id.btncropImage);

		mImageBitmap = null;

		Button picSBtn = (Button) findViewById(R.id.btnIntendS);
		setBtnListenerOrDisable(picSBtn, mTakePicSOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
		setBtnListenerOrDisable(save, saveOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
		setBtnListenerOrDisable(cancel, cancelOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
		setBtnListenerOrDisable(cropImage,cropImageListener,MediaStore.ACTION_IMAGE_CAPTURE);

	}

	private void dispatchTakePictureIntent(int actionCode)
	{
//		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		startActivityForResult(takePictureIntent, actionCode);

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

	private void galleryAddPic() {

		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(String.valueOf(photoFile));
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	private void handleSmallCameraPhoto(Intent intent)
	{
//		Bundle extras = intent.getExtras();
//		mImageBitmap = (Bitmap) extras.get("data");
//		mImageView.setImageBitmap(mImageBitmap);
//		mImageView.setVisibility(View.VISIBLE);

		if (imageUri != null) {
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

                int maxSize = 600;
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


	private Bitmap rotateImage(Bitmap bitmap)
	{
		Bitmap newBitmap = null;
		try
		{
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

			newBitmap = Bitmap.createBitmap(mImageBitmap, 0, 0, mImageBitmap.getWidth(), mImageBitmap.getHeight(), mat, true);
		}
		catch (Exception e)
		{
			MobiculeLogger.error(TAG, "rotateImage() - " + e.toString());
		}

		return newBitmap;
	}

	Button.OnClickListener mTakePicSOnClickListener = new Button.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{

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
	};

	Button.OnClickListener saveOnClickListener = new Button.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			ByteArrayOutputStream blob = new ByteArrayOutputStream();
			if (mImageBitmap == null)
			{
				Toast.makeText(TakePicture.this, "Please take picture", Toast.LENGTH_LONG).show();
				return;
			}
			else {

//				new saveImageAsyncTask(TakePicture.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new saveImageAsyncTask(TakePicture.this).execute();
			}



//			mImageBitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, blob);
//			bitmapdata = blob.toByteArray();
//
//			if (bitmapdata == null || (bitmapdata != null && bitmapdata.length == 0))
//			{
//				Toast.makeText(TakePicture.this, "Please take picture", Toast.LENGTH_LONG).show();
//				return;
//			}
//			String imageStr = Base64.encodeBytes(bitmapdata);
//			randomMeterReadingBO.setImage(imageStr);
//			Intent intent = new Intent(TakePicture.this, RandomMeterReadingSummary.class);
//			intent.putExtra("TAG", "RandomMeterReading");
//			startActivity(intent);
		}
	};


    public class saveImageAsyncTask extends AsyncTask<Void, Void, Void> {
        private Context context;

        private ProgressDialog progressDialog;

        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        String imageStr;

        public saveImageAsyncTask(Context _context) {
            context = _context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(context);
            this.progressDialog.setMessage("Wait while Image is getting Saved...");
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100 /* ignored for PNG */, blob);
			    bitmapdata = blob.toByteArray();
				imageStr = Base64.encodeBytes(bitmapdata);

            } catch (Exception e) {
                e.printStackTrace();
                e.printStackTrace(FileUtil.exceptionToFile());
            }
            return null;
        }

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);

			if (bitmapdata == null || (bitmapdata != null && bitmapdata.length == 0))
			{
				Toast.makeText(TakePicture.this, "Please take picture", Toast.LENGTH_LONG).show();
				return ;
			}

			randomMeterReadingBO.setImage(imageStr);
			Toast.makeText(TakePicture.this, "Image has been Saved Successfully", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(TakePicture.this, RandomMeterReadingSummary.class);
			intent.putExtra("TAG", "RandomMeterReading");
			startActivity(intent);
			finish();

			progressDialog.dismiss();
		}
	}

	Button.OnClickListener cancelOnClickListener = new Button.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			finish();
		}
	};

	//listner of crop image
	Button.OnClickListener cropImageListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {


			if (mImageBitmap == null)
			{
				Toast.makeText(TakePicture.this, "Please take picture", Toast.LENGTH_SHORT).show();
				return;
			}

			else {

//				new cropImageAsyncTask(TakePicture.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new cropImageAsyncTask(TakePicture.this).execute();
			}


		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case ACTION_TAKE_PHOTO_S:
			{
				if (resultCode == RESULT_OK)
				{
					handleSmallCameraPhoto(data);
					deleteingCapturedImage();
				}
				break;
			} // ACTION_TAKE_PHOTO_S

			case CROP_FROM_CAMERA:


			{
				if (resultCode == RESULT_OK)
				{
//					try {
//						Uri imageUri = data.getData();
//						mImageBitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//						mImageView.setImageBitmap(mImageBitmap);
//						mImageView.setVisibility(View.VISIBLE);
//
//					} catch (IOException e) {
//						e.printStackTrace();
//					}

					try {


						/*Bundle bundle = data.getExtras();
						mImageBitmap = bundle.getParcelable("data");*/

						if(data.getData()==null){
							mImageBitmap = (Bitmap)data.getExtras().get("data");
						}else{
							mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
						}

						//Uri imageUri = data.getData();
						//mImageBitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
						mImageView.setImageBitmap(mImageBitmap);
						mImageView.setVisibility(View.VISIBLE);

					} catch (Exception e) {
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

	public void deleteingCapturedImage()
	{

//		String[] projection = { MediaStore.Images.ImageColumns.SIZE, MediaStore.Images.ImageColumns.DISPLAY_NAME,
//				MediaStore.Images.ImageColumns.DATA, BaseColumns._ID, };
//
//		Cursor c = null;
//		Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//		MobiculeLogger.verbose("on activityresult Uri u " + u.toString());
//
//		try
//		{
//			if (u != null)
//			{
//				c = managedQuery(u, projection, null, null, null);
//			}
//			if ((c != null) && (c.moveToLast()))
//			{
//				MobiculeLogger.verbose("c.getString(0) " + c.getString(0));
//				MobiculeLogger.verbose("c.getString(1) " + c.getString(1));
//				MobiculeLogger.verbose("c.getString(2) " + c.getString(2));
//				MobiculeLogger.verbose("c.getString(3) " + c.getString(3));
//
//				ContentResolver cr = getContentResolver();
//				int i = cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, BaseColumns._ID + "=" + c.getString(3),
//						null);
//
//				MobiculeLogger.verbose("Number of column deleted : " + i);
//
//			}
//		}
//		finally
//		{
//			if (c != null)
//			{
//				c.close();
//			}
//		}


		try
		{
            if (uri != null) {

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

	// Some lifecycle callbacks so that the image can survive orientation change
//	@Override
//	protected void onSaveInstanceState(Bundle outState)
//	{
//		outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
//		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null));
//		super.onSaveInstanceState(outState);
////		Icepick.saveInstanceState(this,outState);
//	}

//	@Override
//	protected void onRestoreInstanceState(Bundle savedInstanceState)
//	{
//		super.onRestoreInstanceState(savedInstanceState);
//		mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
//		mImageView.setImageBitmap(mImageBitmap);
//		mImageView.setVisibility(savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ? ImageView.VISIBLE
//				: ImageView.INVISIBLE);
//	}

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
	 * 
	 * @param context
	 *            The application's environment.
	 * @param action
	 *            The Intent action to check for availability.
	 * 
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action)
	{
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

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
	
	@Override
	public void onBackPressed()
	{
		finish();
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
				Bitmap bitmap = getBitmapFromCaptureImageView();
				listOfImages = new ArrayList<Bitmap>();
				listOfImages.add(bitmap);

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
