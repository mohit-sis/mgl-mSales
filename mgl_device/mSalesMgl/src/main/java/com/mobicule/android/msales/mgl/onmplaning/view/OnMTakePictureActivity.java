
/**
 * *****************************************************************************
 * C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
 * <p>
 * Copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
 * This is proprietary information of Mobicule Technologies Pvt. Ltd.and is
 * subject to applicable licensing agreements. Unauthorized reproduction,
 * transmission or distribution of this file and its contents is a
 * violation of applicable laws.
 * *****************************************************************************
 *
 * @project mSalesMgl
 */
package com.mobicule.android.msales.mgl.onmplaning.view;

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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.util.Base64;
import com.mobicule.android.msales.mgl.util.CustomExceptionHandler;
import com.mobicule.android.msales.mgl.util.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.zelory.compressor.Compressor;

//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;

/**
 * <enter description here>
 *
 * @author namrata <enter lastname>
 * @createdOn 21-Apr-2017
 * @modifiedOn
 * @copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
 * @see
 */
public class OnMTakePictureActivity extends DefaultOnMPlanningActivity implements OnClickListener {
    private String TAG = "OnMTakePictureActivity";

    public static final String EXTRA_IMG = "IMG";

    private Button b_onmSavePhoto, b_onmCancel, b_onmTakePicture, b_onmCropImage;

    private static final String BITMAP_STORAGE_KEY = "viewbitmap";

    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";

    private static final int CROP_FROM_CAMERA = 5;

    private ImageView iv_onmCapturedImage;

    private static final String OnMPLANNINGBO_KEY = "onMPlanningBo";

    private String Takephoto;

    private Bitmap thumbnail;

    private int REQUEST_CAMERA = 100;

    private static final int ACTION_TAKE_PHOTO_S = 2;

    private static final int PERMISSION_REQUEST_CODE = 7;

    String strJson;

    Intent intent;

    private int imageCount = 0;

    private Bitmap mImageBitmap;

    private byte[] bitmapdata;

    private String strHoseAvailable;

    private String IMAGE_COUNT;

    ArrayList<Bitmap> listOfImages;

    private Uri imageUri;

    File photoFile;

    String replacedStringPath = "";

    Uri uri = null;

    private String mCurrentPhotoPath;
    private File actualImage, compressedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

    private void initializecomponents() {

        b_onmSavePhoto = (Button) findViewById(R.id.b_onmSavePhoto);
        b_onmCancel = (Button) findViewById(R.id.b_onmCancel);
        iv_onmCapturedImage = (ImageView) findViewById(R.id.iv_onmCapturedImage);
        b_onmTakePicture = (Button) findViewById(R.id.b_onmTakePicture);
        b_onmCropImage = (Button) findViewById(R.id.btncropImage);


        b_onmSavePhoto.setOnClickListener(this);
        b_onmCancel.setOnClickListener(this);
        b_onmTakePicture.setOnClickListener(this);
        b_onmCropImage.setOnClickListener(this);

        //	setBtnListenerOrDisable(b_onmCropImage,cropImageListener,MediaStore.ACTION_IMAGE_CAPTURE);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.b_onmSavePhoto:

            {

                if (mImageBitmap == null) {
                    Toast.makeText(OnMTakePictureActivity.this, "Please take picture", Toast.LENGTH_LONG).show();
                    return;
                } else {

//                    new saveImageAsyncTask(OnMTakePictureActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    saveImageAsyncTask saveImageAsyncTask = new saveImageAsyncTask(OnMTakePictureActivity.this);
                    // saveImageAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    if (Build.VERSION.SDK_INT >= 11) {
                        //--post GB use serial executor by default --
                        saveImageAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        //--GB uses ThreadPoolExecutor by default--
                        saveImageAsyncTask.execute();
                    }
                    //new saveImageAsyncTask(OnMTakePictureActivity.this).execute();
                }
            }


            break;

            case R.id.b_onmCancel:

                //finish();
                break;

            case R.id.b_onmTakePicture:
                try {
                   /* iv_onmCapturedImage.setImageBitmap(null);
					if (imageCount <= 2)
					{
						FileUtil.writeToFile("************ Take Picture Activity************  : onMPlanningBO :: "
								+ onMPlanningBO.toJSON().toString());
						Intent cam_intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(cam_intent, REQUEST_CAMERA);

					}
*/

                    iv_onmCapturedImage.setImageBitmap(null);

					/*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					ContentValues values = new ContentValues();
					values.put(MediaStore.Images.Media.TITLE, "NewPicture");
					imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
					//MobiculeLogger.debug("image uri path is: "+imageUri);
					takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

					startActivityForResult(takePictureIntent, REQUEST_CAMERA);*/

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        } else {
                            dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
                        }

                    } else {
                        dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
                    }
                } catch (Exception e) {
					/*IGenericExceptionHandler handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
					handler.setIsLogEnable(true);*/
                    handler.logCrashReport(e);
                    e.printStackTrace();
                    e.printStackTrace(FileUtil.exceptionToFile());
                }
                break;

            case R.id.btncropImage:


                if (mImageBitmap == null) {
                    Toast.makeText(OnMTakePictureActivity.this, "Please take picture", Toast.LENGTH_SHORT).show();
                    return;
                } else {
//							new cropImageAsyncTask(OnMTakePictureActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new cropImageAsyncTask(OnMTakePictureActivity.this).execute();
                }

            default:
                break;


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera

                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
            } else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question

                Toast.makeText(this, "Permission need to be granted for access camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void dispatchTakePictureIntent(int actionCode) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {


            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                try {

                    photoFile = createPhotoFile();

                    if (photoFile != null) {
                        imageUri = FileProvider.getUriForFile(this, "com.mahanagar", photoFile);
                        takePictureIntent.putExtra("return-data", true);
                        takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//						takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,imageUri);
                        Log.e("EXTRA_OUTPUT_greater", "" + imageUri);
                        startActivityForResult(takePictureIntent, actionCode);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        } else {

            try {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //File temFile = new File(Environment.getDownloadCacheDirectory() + "/msales_images/" + System.currentTimeMillis() + ".jpg");

                File temFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + System.currentTimeMillis() + ".jpg");
                if (!temFile.exists()) {
                    temFile.createNewFile();
                }
                imageUri = Uri.fromFile(temFile);
                takePictureIntent.putExtra("return-data", true);
                takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//				takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,imageUri);
                Log.e("EXTRA_OUTPUT_less", "" + imageUri);
                startActivityForResult(takePictureIntent, actionCode);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    private File createPhotoFile() throws IOException {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_TAKE_PHOTO_S && resultCode == RESULT_OK) {
			/*Bundle extras = data.getExtras();
			mImageBitmap = (Bitmap) extras.get("data");
*/

			/*if(data.getData()==null){
				mImageBitmap = (Bitmap)data.getExtras().get("data");
			}else{
				try {
					mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
*/

			/*HandleImage handleImage = new HandleImage(this);
			mImageBitmap = handleImage.handleSmallCameraPhoto(imageUri);
			iv_onmCapturedImage.setImageBitmap(mImageBitmap);
			iv_onmCapturedImage.setVisibility(View.VISIBLE);*/

            handleSmallCameraPhoto(data);
            deleteingCapturedImage();

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

        } else if (requestCode == CROP_FROM_CAMERA) {
            if (resultCode == RESULT_OK) {
                try {


                    if (data.getData() == null) {
                        mImageBitmap = (Bitmap) data.getExtras().get("data");
                    } else {
                        mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    }

					/*Uri imageUri = data.getData();
					mImageBitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);*/
                    iv_onmCapturedImage.setImageBitmap(mImageBitmap);
                    iv_onmCapturedImage.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void handleSmallCameraPhoto(Intent data) {
        try {

            if (imageUri != null) {
                String path = imageUri.getPath();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                   replacedStringPath = "/storage/emulated/0" + path.replaceAll("/shared_files", "");
                    //replacedStringPath = (Environment.getDownloadCacheDirectory() + "/msales_images/");

                    path = replacedStringPath;
                    uri = Uri.parse(replacedStringPath);
//					mImageBitmap = BitmapFactory.decodeFile(replacedStringPath, null);
//				} else {
//					uri = Uri.parse(path);
//					mImageBitmap = BitmapFactory.decodeFile(path, null);
//				}


                    actualImage = new File(replacedStringPath);


                    compressedImage = new Compressor.Builder(this)
                           // .setMaxWidth(150)
                           // .setMaxHeight(150)
                            .setQuality(85)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .build()
                            .compressToFile(actualImage);
                    mImageBitmap = BitmapFactory.decodeFile(compressedImage.getAbsolutePath());
uri=Uri.parse(actualImage.getPath());
                    Log.e("ActualSize", "SIZE" + getReadableFileSize(actualImage.length()));
                    Log.e("CompressedSIze", "SIZE" + getReadableFileSize(compressedImage.length()));

                } else {
                    uri = Uri.parse(path);
                    //  mImageBitmap = BitmapFactory.decodeFile(path, null);

                    actualImage = new File(path);

                    compressedImage = new Compressor.Builder(this)
                           // .setMaxWidth(150)
                           // .setMaxHeight(150)
                            .setQuality(85)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .build()
                            .compressToFile(actualImage);
                    mImageBitmap = BitmapFactory.decodeFile(compressedImage.getAbsolutePath());


                    Log.e("ActualSize", "SIZE" + getReadableFileSize(actualImage.length()));
                    Log.e("CompressedSIze", "SIZE" + getReadableFileSize(compressedImage.length()));
                    uri=Uri.parse(actualImage.getPath());

                }

                Log.e("mImageBitmap", "mImageBitmap" + mImageBitmap);
//                try {
//                    int maxSize = 150;
//                    int outWidth;
//                    int outHeight;
//                    int inWidth = mImageBitmap.getWidth();
//                    int inHeight = mImageBitmap.getHeight();
//                    if (inWidth > inHeight) {
//                        outWidth = maxSize;
//                        outHeight = (inHeight * maxSize) / inWidth;
//                    } else {
//                        outHeight = maxSize;
//                        outWidth = (inWidth * maxSize) / inHeight;
//                    }
//
//                    mImageBitmap = Bitmap.createScaledBitmap(mImageBitmap, outWidth, outHeight, false);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

               // mImageBitmap = rotateImage(mImageBitmap);
                iv_onmCapturedImage.setImageBitmap(mImageBitmap);

                iv_onmCapturedImage.setVisibility(View.VISIBLE);
                b_onmSavePhoto.setEnabled(true);

            } else {
                Toast.makeText(getApplicationContext(), "Couldn't display image", Toast.LENGTH_LONG).show();
                return;
            }


        } catch (Exception e) {
            MobiculeLogger.error(TAG, "handleSmallCameraPhoto() - " + e.toString());
            e.printStackTrace();
        }
    }

    private Bitmap rotateImage(Bitmap bitmap) {
        Bitmap newBitmap = null;
        try {
            ExifInterface ei = new ExifInterface(uri.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            Matrix mat = new Matrix();

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    mat.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    mat.postRotate(90);
                    break;
            }

            newBitmap = Bitmap.createBitmap(mImageBitmap, 0, 0, mImageBitmap.getWidth(), mImageBitmap.getHeight(), mat, true);
        } catch (Exception e) {
            MobiculeLogger.error(TAG, "rotateImage() - " + e.toString());
        }

        return newBitmap;
    }

    public void deleteingCapturedImage() {
        try {
            if (uri != null) {

                MobiculeLogger.error(TAG, "URI path - " + uri.getPath());
                // getContentResolver().delete(imageUri, null, null);
                File file = new File(uri.getPath());
                if (file.exists()) {
                    file.delete();
                }
                MobiculeLogger.error(TAG, "Photo Deleted - " + uri.getPath());
            }
        } catch (Exception e) {
            MobiculeLogger.error(TAG, "deleteCaptureImage() - " + e.toString());
            e.printStackTrace();
        }

    }

	/*public void deleteingCapturedImage()
	{

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
				MobiculeLogger.verbose("c.getString(0) "  + c.getString(0));
				MobiculeLogger.verbose("c.getString(1) " + c.getString(1));
				MobiculeLogger.verbose("c.getString(2) " + c.getString(2));
				MobiculeLogger.verbose("c.getString(3) " + c.getString(3));

				ContentResolver cr = getContentResolver();
				int i = cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, BaseColumns._ID + "=" + c.getString(3),
						null);

				MobiculeLogger.verbose("Number of column deleted : " + i);

			}
		}
		finally
		{
			if (c != null)
			{
				//c.close();
			}

		}
	}*/

    // Some lifecycle callbacks so that the image can survive orientation change
	/*@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		*//*outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null));
		outState.putInt(IMAGE_COUNT, imageCount);
		super.onSaveInstanceState(outState);*//*
		super.onSaveInstanceState(outState);
		outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null));
		outState.putInt(IMAGE_COUNT, imageCount);
		outState.putString(OnMPLANNINGBO_KEY, onMPlanningBO.toJSON().toString());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		try
		{
		mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
		iv_onmCapturedImage.setImageBitmap(mImageBitmap);
		iv_onmCapturedImage
				.setVisibility(savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ? ImageView.VISIBLE
						: ImageView.INVISIBLE);
		imageCount = savedInstanceState.getInt(IMAGE_COUNT);

		JSONObject onMJson = new JSONObject(savedInstanceState.getString(OnMPLANNINGBO_KEY));

			if(onMJson.has(IOnMPlanning.KEY_ADDRESS))
			{
				onMPlanningBO.setAddress(onMJson.getString(IOnMPlanning.KEY_ADDRESS));
			}
			if(onMJson.has(IOnMPlanning.KEY_BP_NO))
			{
				onMPlanningBO.setBPNumber(onMJson.getString(IOnMPlanning.KEY_BP_NO));
			}
			if(onMJson.has(IOnMPlanning.KEY_CA_NO))
			{
				onMPlanningBO.setCANumber(onMJson.getString(IOnMPlanning.KEY_CA_NO));
			}
			if(onMJson.has(IOnMPlanning.KEY_CONN_OBJ))
			{
				onMPlanningBO.setConnObj(onMJson.getString(IOnMPlanning.KEY_CONN_OBJ));
			}
			if(onMJson.has(IOnMPlanning.KEY_CUSTOMER_NAME))
			{
				onMPlanningBO.setCutsomerName(onMJson.getString(IOnMPlanning.KEY_CUSTOMER_NAME));
			}
			if(onMJson.has(IOnMPlanning.KEY_DATE))
			{
				onMPlanningBO.setDate(onMJson.getString(IOnMPlanning.KEY_DATE));
			}
			if(onMJson.has(IOnMPlanning.KEY_IMAGE))
			{
				onMPlanningBO.setImage(onMJson.getString(IOnMPlanning.KEY_IMAGE));
			}
			if(onMJson.has(IOnMPlanning.KEY_IMAGE2))
			{
				onMPlanningBO.setImage2(onMJson.getString(IOnMPlanning.KEY_IMAGE2));
			}
			if(onMJson.has(IOnMPlanning.KEY_IMAGE3))
			{
				onMPlanningBO.setImage3(onMJson.getString(IOnMPlanning.KEY_IMAGE3));
			}
			if(onMJson.has(IOnMPlanning.KEY_IS_HOSE_AVAILABLE))
			{
				onMPlanningBO.setIsHoseAvailable(onMJson.getString(IOnMPlanning.KEY_IS_HOSE_AVAILABLE));
			}
			if(onMJson.has(IOnMPlanning.KEY_METER_NO))
			{
				onMPlanningBO.setMeterNo(onMJson.getString(IOnMPlanning.KEY_METER_NO));
			}
			if(onMJson.has(IOnMPlanning.KEY_MR_CODE))
			{
				onMPlanningBO.setMrCode(onMJson.getString(IOnMPlanning.KEY_MR_CODE));
			}
			if(onMJson.has(IOnMPlanning.KEY_MRO_NO))
			{
				onMPlanningBO.setMroNo(onMJson.getString(IOnMPlanning.KEY_MRO_NO));
			}
			if(onMJson.has(IOnMPlanning.KEY_MR_REASON))
			{
				onMPlanningBO.setMrReason(onMJson.getString(IOnMPlanning.KEY_MR_REASON));
			}
			if(onMJson.has(IOnMPlanning.KEY_MR_SUBMIT))
			{
				onMPlanningBO.setMrSubmit(onMJson.getString(IOnMPlanning.KEY_MR_SUBMIT));
			}
			if(onMJson.has(IOnMPlanning.KEY_MRU_CODE))
			{
				onMPlanningBO.setMruCode(onMJson.getString(IOnMPlanning.KEY_MRU_CODE));
			}
			if(onMJson.has(IOnMPlanning.KEY_MSG_FRM_MR))
			{
				onMPlanningBO.setMsgFrmMr(onMJson.getString(IOnMPlanning.KEY_MSG_FRM_MR));
			}
			if(onMJson.has(IOnMPlanning.KEY_MSG_REMARKS))
			{
				onMPlanningBO.setMsgRemarks(onMJson.getString(IOnMPlanning.KEY_MSG_REMARKS));
			}
			if(onMJson.has(IOnMPlanning.KEY_MSG_TO_MR))
			{
				onMPlanningBO.setMsgToMr(onMJson.getString(IOnMPlanning.KEY_MSG_TO_MR));
			}
			if(onMJson.has(IOnMPlanning.KEY_NO_OF_BURNERS))
			{
				onMPlanningBO.setNoOfBurners(onMJson.getString(IOnMPlanning.KEY_NO_OF_BURNERS));
			}
			if(onMJson.has(IOnMPlanning.KEY_NO_OF_GAS_GEYSERS))
			{
				onMPlanningBO.setNoOfGasGeysers(onMJson.getString(IOnMPlanning.KEY_NO_OF_GAS_GEYSERS));
			}
			if(onMJson.has(IOnMPlanning.KEY_PLOT))
			{
				onMPlanningBO.setPlot(onMJson.getString(IOnMPlanning.KEY_PLOT));
			}
			if(onMJson.has(IOnMPlanning.KEY_STATUS))
			{
				onMPlanningBO.setStatus(onMJson.getString(IOnMPlanning.KEY_STATUS));
			}
			if(onMJson.has(IOnMPlanning.KEY_TIME))
			{
				onMPlanningBO.setTime(onMJson.getString(IOnMPlanning.KEY_TIME));
			}


		}
		catch (JSONException e)
		{
			*//*IGenericExceptionHandler handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
			handler.setIsLogEnable(true);*//*
			handler.logCrashReport(e);
			e.printStackTrace();
		}
	}*/

    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
     *
     * @param context The application's environment.
     * @param action  The Intent action to check for availability.
     * @return True if an Intent with the specified action can be sent and
     * responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void setBtnListenerOrDisable(Button btn, Button.OnClickListener onClickListener, String intentName) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(onClickListener);
        } else {
            btn.setText(getText(R.string.cannot).toString() + " " + btn.getText());
            btn.setClickable(false);
        }
    }

    public class cropImageAsyncTask extends AsyncTask<Void, Void, Void> {
        private Context context;

        private ProgressDialog progressDialog;

        public cropImageAsyncTask(Context _context) {
            context = _context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(context);
            this.progressDialog.setMessage("Loading...");
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setType("image/*");

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

            int size = list.size();

            if (size == 0) {
                //TODO TOAST
                // CustomToast.getInstance(MITCImageCapture.this).showCustomToast("Cannot find image crop app");

            } else {
//				Bitmap bitmap = getBitmapFromCaptureImageView();
//				listOfImages = new ArrayList<Bitmap>();
//				listOfImages.add(bitmap);

                Uri mImageCaptureUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                        getBitmapFromCaptureImageView(), "some Title", "some_Description"));

                intent.setData(mImageCaptureUri);

                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);
                intent.putExtra("outputX", 400);
                intent.putExtra("outputY", 300);
                intent.putExtra("scale", true);
                intent.putExtra("scaleUpIfNeeded", true);

                intent.putExtra("return-data", true);

                MobiculeLogger.info("CROP ", "if Size == 0");
                //flagForGps = false;
                startActivityForResult(intent, CROP_FROM_CAMERA);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }

        public Bitmap getBitmapFromCaptureImageView() {
            Drawable drawable = iv_onmCapturedImage.getDrawable();
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
            Bitmap bitmap = bitmapDrawable.getBitmap();

            return bitmap;
        }

    }

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


                // mImageBitmap = BitmapFactory.decodeFile(compressedImage.getAbsolutePath());

                mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100 /* ignored for PNG */, blob);
                bitmapdata = blob.toByteArray();
                imageStr = Base64.encodeBytes(bitmapdata);

                double bitmapLength = bitmapdata.length;
                long fileSizeInKB = (long) (bitmapLength / 1024);
                long fileSizeInMB = fileSizeInKB / 1024;
                Log.e("CompressedSIze:111", "" + (fileSizeInMB / 1024) + " MB");

                //Delete file captured img file from storage
                File file2 = new File(compressedImage.getAbsolutePath());
                if (file2.exists()) {
                    MobiculeLogger.verbose("fileName 2 : "+file2.getPath());
                    file2.delete();
                    MobiculeLogger.verbose("file 2 deleted : "+file2.getPath());

                }

                //mImageBitmap = readImage(imageFile.getPath());
				/*try
				{

					FileInputStream fis = new FileInputStream(fileUri.getPath());

					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					byte[] buf = new byte[(int) fileUri.toString().length()];

					for (int readNum; (readNum = fis.read(buf)) != -1;)
					{
						bos.write(buf, 0, readNum);
					}

					byte[] bytes = bos.toByteArray();

					encodedString = Base64.encode(bytes, 0, bytes.length);

				}
				catch (Exception e)
				{
					e.printStackTrace();
// e.printStackTrace(FileUtil.exceptionToFile());
				}*/

                /*mImageBitmap.compress(CompressFormat.PNG,  100  *//*ignored for PNG*//* , blob);
				bitmapdata = blob.toByteArray();*/


                //FileUtil.writeToFile("************ If no pic selected************  : : onMPlanningBO" + onMPlanningBO.toJSON().toString());

            } catch (Exception e) {
                // TODO: handle exception
					/*IGenericExceptionHandler handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
					handler.setIsLogEnable(true);*/
                //handler.logCrashReport(e);
                e.printStackTrace();
                e.printStackTrace(FileUtil.exceptionToFile());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                if (bitmapdata == null || (bitmapdata != null && bitmapdata.length == 0)) {
                    Toast.makeText(OnMTakePictureActivity.this, "Please take picture", Toast.LENGTH_LONG).show();
                    return;
                }


                imageCount++;
                if (imageCount == 1) {
                    onMPlanningBO.setImage(imageStr);

                    iv_onmCapturedImage.setImageBitmap(null);
                    recycling();
                    mImageBitmap = null;
                    Toast.makeText(OnMTakePictureActivity.this, "Image 1 has been Saved Successfully",
                            Toast.LENGTH_SHORT).show();
                    FileUtil.writeToFile("//OnMPlanningSummaryActivity *********First image captured************ : : onMPlanningBO"
                            + onMPlanningBO.toJSON().toString());

                } else if (imageCount == 2) {
                    recycling();
                    onMPlanningBO.setImage2(imageStr);
                    iv_onmCapturedImage.setImageBitmap(null);
                    mImageBitmap = null;

                    Toast.makeText(OnMTakePictureActivity.this, "Image 2 has been Saved Successfully",
                            Toast.LENGTH_SHORT).show();
                    FileUtil.writeToFile("//OnMPlanningSummaryActivity ************ Second image captured************  : :onMPlanningBO"
                            + onMPlanningBO.toJSON().toString());
                    //fileUri=null;
                    Intent intent = new Intent(OnMTakePictureActivity.this, OnMPlanningSummaryActivity.class);
                    intent.putExtra("strHoseAvailable", strHoseAvailable);
                    startActivity(intent);
                    finish();
                }

                onMPlanningBO.setImage3("");

            } catch (Exception e) {
                // TODO: handle exception
					/*IGenericExceptionHandler handler = GenericExceptionHandler.sharedInstance(this.getApplicationContext());
					handler.setIsLogEnable(true);*/
                //handler.logCrashReport(e);
                e.printStackTrace();
                e.printStackTrace(FileUtil.exceptionToFile());
            }

            progressDialog.dismiss();
        }


    }


    public void recycling() {
        if (mImageBitmap != null) {
            mImageBitmap.recycle();
            mImageBitmap = null;
            iv_onmCapturedImage.setImageBitmap(null);
        }
    }

    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}

