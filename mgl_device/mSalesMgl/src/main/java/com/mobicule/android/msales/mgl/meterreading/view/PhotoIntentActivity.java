package com.mobicule.android.msales.mgl.meterreading.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.zelory.compressor.Compressor;


/**
 *
 * <enter description here>
 *
 * @author Sathya Sheela
 * @see
 *
 * @createdOn Mar 6, 2012
 * @modifiedOn
 *
 * @copyright ? 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
 */
public class PhotoIntentActivity extends DefaultMeterReadingActivity
{
    private static final int ACTION_TAKE_PHOTO_S = 2;

    private static final int CROP_FROM_CAMERA = 5;

    private static final int PERMISSION_REQUEST_CODE = 7;

    private static final String BITMAP_STORAGE_KEY = "viewbitmap";

    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";

    private ImageView mImageView;

    private Button save, cancel ,cropImage;

    private Bitmap mImageBitmap;

    private byte[] bitmapdata;

    private String TAG = "PhotoIntentActivity";

    private String activityFromIntent;

    ArrayList<Bitmap>listOfImages;

    private Uri imageUri;

    private String mCurrentPhotoPath;

    File photoFile;

    String replacedStringPath = "";

    Uri uri ,uri1= null;
    private File actualImage, compressedImage;


    /** Called when the activity is first created. */

    @Override
    protected void onStart()
    {
        super.onStart();
        Constants.broadcastDialogContext = this;
    }

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

//        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.saveimage);

        MobiculeLogger.verbose("Create");

        mImageView = (ImageView) findViewById(R.id.imageview);
        save = (Button) findViewById(R.id.saveButton);
        cancel = (Button) findViewById(R.id.cancelButton);
        cropImage = (Button)findViewById(R.id.btncropImage);

        mImageBitmap = null;
        activityFromIntent = getIntent().getStringExtra("activity");

       // activityFromIntent = getIntent().getStringExtra("activity");
        MobiculeLogger.verbose("activityFromIntent :: " + activityFromIntent);

        Button picSBtn = (Button) findViewById(R.id.btnIntendS);
        setBtnListenerOrDisable(picSBtn, mTakePicSOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        setBtnListenerOrDisable(save, saveOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        setBtnListenerOrDisable(cancel, cancelOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        setBtnListenerOrDisable(cropImage,cropImageListener,MediaStore.ACTION_IMAGE_CAPTURE);

        /// if cammera not available for device
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            picSBtn.setEnabled(false);
        }  ///

    }

    private void dispatchTakePictureIntent(int actionCode)
    {
		/*mImageView.setImageBitmap(null);

		try{
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			takePictureIntent.putExtra("return-data",true);
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageBitmap);
			startActivityForResult(takePictureIntent, actionCode);

		}catch(ActivityNotFoundException e)
		{
			Toast toast = Toast
					.makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
			toast.show();
		}*/
        //please check and comment later if wont work


		/*mImageView.setImageBitmap(null);

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, "NewPicture");
		imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		//MobiculeLogger.debug("image uri path is: "+imageUri);
		takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

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
                File temFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + System.currentTimeMillis() + ".jpg");
                //File temFile = new File(Environment.getDownloadCacheDirectory() + "/msales_images/" + System.currentTimeMillis() + ".jpg");

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

    public Bitmap BITMAP_RESIZER(Bitmap bitmap,int newWidth,int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }

	/*private void handleSmallCameraPhoto(Intent intent)
	{
		Bundle extras = intent.getExtras();
		mImageBitmap = (Bitmap) extras.get("data");
		mImageView.setImageBitmap(mImageBitmap);
		mImageView.setVisibility(View.VISIBLE);
	}*/

    private void handleSmallCameraPhoto()
    {
        try
        {
            /*
             * Bundle extras = intent.getExtras(); mImageBitmap = (Bitmap)
             * extras.get("data"); mImageView.setImageBitmap(mImageBitmap);
             * mImageView.setVisibility(View.VISIBLE); save.setEnabled(true);
             */


            /*if (data.getData() == null) {
                mImageBitmap = (Bitmap) data.getExtras().get("data");
            } else {
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            }*/


            //mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            /**
             * IMAGE RESOLUTIONS - 390:280 480:350
             */

            if (imageUri != null) {
                String path = imageUri.getPath();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                   // replacedStringPath = (Environment.getDownloadCacheDirectory() + "/msales_images/");
                    replacedStringPath = "/storage/emulated/0" + path.replaceAll("/shared_files", "");
                    path = replacedStringPath;
                    uri = Uri.parse(replacedStringPath);
                    actualImage = new File(replacedStringPath);
                    Log.e("Compress","==="+actualImage);


                    compressedImage = new Compressor.Builder(this)
                           // .setMaxWidth(640)
                           // .setMaxHeight(480)
                            .setQuality(85)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            //.setDestinationDirectoryPath(Environment.getDownloadCacheDirectory().getAbsolutePath())
                            .build()
                            .compressToFile(actualImage);

                    Log.e("destination path","= "+Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath());
                    Log.e("captured img path","= " +compressedImage.getAbsolutePath());
                    Log.e("cache directory path", "= "+Environment.getDownloadCacheDirectory().getAbsolutePath());


                    mImageBitmap = BitmapFactory.decodeFile(compressedImage.getAbsolutePath());

                    Log.e("ActualSize","SIZE"+getReadableFileSize(actualImage.length()));
                    Log.e("CompressedSIze","SIZE"+getReadableFileSize(compressedImage.length()));
                    Log.e("Path","===="+Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath());
                   // Log.e("uri1","===="+compressedImage.getPath());

                } else {
                    uri = Uri.parse(path);
                   // mImageBitmap = BitmapFactory.decodeFile(path, null);

                    actualImage = new File(path);
                    Log.e("Compress","==="+actualImage);

                    compressedImage = new Compressor.Builder(this)
                            //.setMaxWidth(640)
                           // .setMaxHeight(480)
                            .setQuality(85)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            //.setDestinationDirectoryPath(Environment.getDownloadCacheDirectory().getAbsolutePath())
                            .build()
                            .compressToFile(actualImage);
                    Log.e("destination path",":"+Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath());
                    Log.e("captured img path","= " +compressedImage.getAbsolutePath());
                    Log.e("cache directory path", ":"+Environment.getDownloadCacheDirectory().getAbsolutePath());

                    mImageBitmap = BitmapFactory.decodeFile(compressedImage.getAbsolutePath());

                    Log.e("Path","===="+Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath());
                 //   Log.e("uri1","===="+compressedImage.getPath());


                            //  Log.e("ActualSize","SIZE"+getReadableFileSize(actualImage.length()));
                 //   Log.e("CompressedSIze","SIZE"+getReadableFileSize(compressedImage.length());
                }

                Log.e("mImageBitmap","mImageBitmap"+mImageBitmap);

//                    mImageBitmap = BitmapFactory.decodeFile(replacedStringPath, null);
//                } else {
//                    uri = Uri.parse(path);
//                    mImageBitmap = BitmapFactory.decodeFile(path, null);
//                }

//                try
//                {
//                    int maxSize = 600;
//                    int outWidth;
//                    int outHeight;
//                    int inWidth = mImageBitmap.getWidth();
//                    int inHeight = mImageBitmap.getHeight();
//                    if (inWidth > inHeight)
//                    {
//                        outWidth = maxSize;
//                        outHeight = (inHeight * maxSize) / inWidth;
//                    }
//                    else
//                    {
//                        outHeight = maxSize;
//                        outWidth = (inWidth * maxSize) / inHeight;
//                    }
//
//                    mImageBitmap = Bitmap.createScaledBitmap(mImageBitmap, outWidth, outHeight, false);
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }

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
            MobiculeLogger.error(TAG, "handleSmallCameraPhoto() - " + e.toString());
            e.printStackTrace();
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

    private void commonConfimrationDialog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoIntentActivity.this);
        builder.setCancelable(true);
        if (Constants.threePicsCapCnt == 0)
        {
            builder.setMessage("Capture complete Door locked photo including sorry to have missed you card. Ensure Sr.no.,BP no,Date,Time & Reader name are mentioned on card"
                    + "\r\n"
                    + this.getString(R.string.capture_complete_door_locked_photo_including_sorry_to_have_missed_you_card));

        }
        else if (Constants.threePicsCapCnt == 1)
        {
            builder.setMessage("Capture photo of Flat no/Name plate." + "\r\n"
                    + this.getString(R.string.capture_photo_of_flat_no_Name_plate));
        }
        else if (Constants.threePicsCapCnt == 2)
        {
            builder.setMessage("Capture photo of sorry to have missed you card dropped at locked premises."
                    + "\r\n"
                    + this.getString(R.string.capture_photo_of_sorry_to_have_missed_you_card_dropped_at_locked_premises));
        }

        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);

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

    Button.OnClickListener mTakePicSOnClickListener = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (getIntent().getBooleanExtra(getString(R.string.isdoorlockedpopup), false))
            {
                if (Constants.isThreePicsSelected)
                {
                    commonConfimrationDialog();
                }
                else
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {

                            requestPermissions(new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        } else {
                            dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
                        }

                    } else {
                        dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
                    }
                }

            }
            else
            {
                //dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if ( checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {

                        requestPermissions(new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    } else {
                        dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
                    }

                } else {
                    dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
                }
            }
        }
    };

    //listner of crop image
    Button.OnClickListener cropImageListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {


            if (mImageBitmap == null)
            {
                Toast.makeText(PhotoIntentActivity.this, "Please take picture", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
//			new cropImageAsyncTask(PhotoIntentActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new cropImageAsyncTask(PhotoIntentActivity.this).execute();
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
                Toast.makeText(PhotoIntentActivity.this, "Please take picture", Toast.LENGTH_LONG).show();
                return;
            } else {


                saveImageAsyncTask saveImageAsyncTask=new saveImageAsyncTask(PhotoIntentActivity.this);

                if (Build.VERSION.SDK_INT >= 11) {
                    //--post GB use serial executor by default --
                    saveImageAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    Log.e("THREAD_POOL_EXECUTOR","THREAD_POOL_EXECUTOR");
                } else {
                    //--GB uses ThreadPoolExecutor by default--
                    saveImageAsyncTask.execute();
                    Log.e("execute","execute");
                }
               // saveImageAsyncTask.execute();
               // new saveImageAsyncTask(PhotoIntentActivity.this).execute();
            }
        }
    };

    Button.OnClickListener cancelOnClickListener = new Button.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            if (Constants.isThreePicsSelected)
            {
                if (Constants.threePicsCapCnt > 2 || Constants.threePicsCapCnt <= 0)
                {
                    finish();
                }
                else
                {
                    return;
                }
            }
            else
            {
                finish();
            }
        }
    };

    @Override
    public void onBackPressed()
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        MobiculeLogger.info(TAG, "onActivityResult() " + data + " requestCode " + requestCode + " imageURI " + imageUri);

        switch (requestCode)
        {
            case ACTION_TAKE_PHOTO_S:
            {

                if (resultCode == RESULT_OK)
                {

					/*if (data.getData() != null) {

						Uri contentUri = data.getData();
						Log.e("contentUri : ",""+contentUri);

					}  else {

						if (data.getExtras() != null ) {

							if (data.getExtras().get("data") != null) {

								Bitmap photo = (Bitmap) data.getExtras().get("data");
								Log.e("bitmap", "" + photo);
							}
						}
					}*/


                    handleSmallCameraPhoto();
                    deleteingCapturedImage();

					/*

					//cropImageDialog(data);
                    HandleImage handleImage = new HandleImage(this);
                    mImageBitmap = handleImage.handleSmallCameraPhoto(imageUri);
					//handleSmallCameraPhoto(data);
					//BITMAP_RESIZER(mImageBitmap,800,800);

					if(data.getData()==null){
						mImageBitmap = (Bitmap)data.getExtras().get("data");
					}else{
						try {
							mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					mImageView.setImageBitmap(mImageBitmap);

					mImageView.setVisibility(View.VISIBLE);
					deleteingCapturedImage();

					*/
                }
                break;
            } // ACTION_TAKE_PHOTO_S
            case CROP_FROM_CAMERA:
            {
                if (resultCode == RESULT_OK)
                {
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

        }
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

        try
        {
            if(uri != null) {
               // uri1=Uri.parse(compressedImage.getPath());
//                File file1=new File(compressedImage.getPath());

                 //File file1 = new File(compressedImage.getAbsolutePath());
                 //uri1  = Uri.parse(file1.getPath());

//               uri1=Uri.parse(file1.getPath());

               // MobiculeLogger.error(TAG, "URI path111 - " + uri1.getPath());
                MobiculeLogger.error(TAG, "URI path - " + uri.getPath());
                // getContentResolver().delete(imageUri, null, null);
                File file = new File(uri.getPath());
                if (file.exists()) {
                    //MobiculeLogger.verbose("fileName : "+file.getAbsolutePath());
                    file.delete();
                    //MobiculeLogger.verbose("file deleted : "+file.getAbsolutePath());
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

	/*public void deleteingCapturedImage()
	{

		String[] projection = { MediaStore.Images.ImageColumns.SIZE, MediaStore.Images.ImageColumns.DISPLAY_NAME,
				MediaStore.Images.ImageColumns.DATA, BaseColumns._ID, };

		Cursor c = null;
		Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		MobiculeLogger.verbose("InfoLog - on activityresult Uri u " + u.toString());

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
		finally
		{
			if (c != null)
			{
				//c.close();
			}

		}
	}*/

    // Some lifecycle callbacks so that the image can survive orientation change


//	@Override
//	protected void onSaveInstanceState(Bundle outState)
//	{
//		super.onSaveInstanceState(outState);
//		outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
//		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null));
////		outState.clear();
////        Icepick.saveInstanceState(this,outState);
//
//         /*
//            reference :  https://github.com/frankiesardo/icepick
//         */
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
            //btn.setText(getText(R.string.cannot).toString() + " " + btn.getText());
            btn.setClickable(false);
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

                //mImageBitmap = readImage(imageFile.getPath());
                /*try {
                 *//*mImageBitmap.compress(Bitmap.CompressFormat.PNG, 0 *//**//* ignored for PNG *//**//*, blob);
					bitmapdata = blob.toByteArray();*//*




					try
					{
						ContentResolver contentResolver = getContentResolver();



						//mImageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.getData());

						FileInputStream fis = new FileInputStream(fileUri.toString());

						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						byte[] buf = new byte[(int) mediaFile.length()];

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
					}


				} catch (Exception e) {
					e.printStackTrace();

				}
*/

				/*ByteArrayOutputStream blob = new ByteArrayOutputStream();
				if (mImageBitmap == null)
				{
					Toast.makeText(PhotoIntentActivity.this, "Please take picture", Toast.LENGTH_LONG).show();
					//return;
				}*/

//                mImageBitmap = BitmapFactory.decodeFile(compressedImage.getAbsolutePath());
//
//                mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100 /* ignored for PNG */, blob);
//                bitmapdata = blob.toByteArray();
//                imageStr = Base64.encodeBytes(bitmapdata);
//                int bitmapLength = bitmapdata.length;
                bitmapdata = new byte[(int) compressedImage.length()];

                //read file into bytes[]
                FileInputStream fileInputStream = new FileInputStream(compressedImage);
                fileInputStream.read(bitmapdata);


                //mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100 /* ignored for PNG */, blob);
                //bitmapdata = blob.toByteArray();
                imageStr = Base64.encodeBytes(bitmapdata);

                double bitmapLength = bitmapdata.length;


                Log.e("CompressedSIze:111", "" + (bitmapLength / 1024) + " KB");

                //Delete file captured img file from storage
                File file1 = new File(compressedImage.getAbsolutePath());
                if (file1.exists()) {
                    MobiculeLogger.verbose("fileName 1 : "+file1.getPath());
                    file1.delete();
                    MobiculeLogger.verbose("file 1 deleted : "+file1.getPath());

                }

            } catch (Exception e) {

                e.printStackTrace();
                e.printStackTrace(FileUtil.exceptionToFile());
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (bitmapdata == null || (bitmapdata != null && bitmapdata.length == 0) )
            {
                Toast.makeText(PhotoIntentActivity.this, "Please take picture", Toast.LENGTH_LONG).show();
                return;
            }

            if (Constants.isThreePicsSelected)
            {
                if (Constants.threePicsCapCnt <= 2)
                {
                    if (Constants.threePicsCapCnt == 0)
                    {
                        meterReadingBO.setImage(imageStr);
                        mImageView.setImageBitmap(null);
                        mImageBitmap = null;
                        //recycling();
                        Constants.threePicsCapCnt = 1;
                        Toast.makeText(PhotoIntentActivity.this, "Image 1 has been Saved Successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if (Constants.threePicsCapCnt == 1)
                    {
                        meterReadingBO.setImage2(imageStr);
                        Constants.threePicsCapCnt = 2;

                        mImageView.setImageBitmap(null);
                        mImageBitmap = null;
                        //recycling();
                        Toast.makeText(PhotoIntentActivity.this, "Image 2 has been Saved Successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if (Constants.threePicsCapCnt == 2)
                    {
                        Toast.makeText(PhotoIntentActivity.this, "Image 3 has been Saved Successfully",
                                Toast.LENGTH_SHORT).show();
                        meterReadingBO.setImage3(imageStr);
                        //recycling();
                        MobiculeLogger.verbose("PhotoIntentActivity : meterReadingBO :: PhotoIntentActivity "+meterReadingBO.toJSON().toString());
                        Constants.threePicsCapCnt = 3;
                        Intent intent = new Intent(PhotoIntentActivity.this, Summary.class);
                        FileUtil.writeToFile("//PhotoIntentActivity to start Summary: meterReadingBO :: " + meterReadingBO.toJSON().toString());

                        intent.putExtra("TAG", "meterReading");
                        intent.putExtra("activity", activityFromIntent);

                        startActivity(intent);
                        finish();
                    }
                }

            }
            else if (Constants.isTwoPicsSelected)
            {
                if (Constants.threePicsCapCnt <= 1)
                {
                    if (Constants.threePicsCapCnt == 0)
                    {
                        meterReadingBO.setImage(imageStr);

                        mImageView.setImageBitmap(null);
                        mImageBitmap = null;
                        recycling();
                        Constants.threePicsCapCnt = 1;
                        Toast.makeText(PhotoIntentActivity.this, "Image 1 has been Saved Successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if (Constants.threePicsCapCnt == 1)
                    {
                        meterReadingBO.setImage2(imageStr);
                        Constants.threePicsCapCnt = 2;

                        mImageView.setImageBitmap(null);
                        mImageBitmap = null;
                        recycling();
                        Toast.makeText(PhotoIntentActivity.this, "Image 2 has been Saved Successfully",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(PhotoIntentActivity.this, Summary.class);
                        FileUtil.writeToFile("//PhotoIntentActivity to start Summary : meterReadingBO :: " + meterReadingBO.toJSON().toString());

                        intent.putExtra("TAG", "meterReading");
                        intent.putExtra("activity", activityFromIntent);
                        startActivity(intent);
                        finish();

                    }

                }

            }

            else
            {
                meterReadingBO.setImage(imageStr);
                Toast.makeText(PhotoIntentActivity.this, "Image has been Saved Successfully", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(PhotoIntentActivity.this, Summary.class);
                FileUtil.writeToFile("//PhotoIntentActivity to start Summary: meterReadingBO :: " + meterReadingBO.toJSON().toString());

                intent.putExtra("TAG", "meterReading");
                intent.putExtra("activity", activityFromIntent);
                startActivity(intent);
                finish();
            }


            progressDialog.dismiss();
        }

    }
    public void recycling()
    {
        if (mImageBitmap != null)
        {
            mImageBitmap.recycle();
            mImageBitmap = null;
            mImageView.setImageBitmap(null);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
}
