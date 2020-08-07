package com.mobicule.android.msales.mgl.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import com.mobicule.android.component.logging.MobiculeLogger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

public class HandleImage
{
    Activity activity;

    public HandleImage(Activity activity)
    {
        this.activity = activity;
    }

    public Bitmap handleSmallCameraPhoto(Uri imageUri)
    {
        Bitmap mImageBitmap = null;
        try
        {
            //Uri imgUri = intent.getData();
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            InputStream inputStream = new BufferedInputStream(activity.getContentResolver().openInputStream(imageUri));

            BitmapFactory.decodeStream(inputStream, null, o);

            inputStream.close();

            //The new size we want to scale to
            final int REQUIRED_SIZE = 200;

            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;

            InputStream inputStream1 = new BufferedInputStream(activity.getContentResolver().openInputStream(imageUri));

            mImageBitmap = BitmapFactory.decodeStream(inputStream1, null, o2);

            mImageBitmap = rotateImage(mImageBitmap, imageUri);

            mImageBitmap = changeBitmapContrastBrightness(mImageBitmap,1,1);

            inputStream1.close();

            deleteingCapturedImage();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return mImageBitmap;
    }

    private Bitmap rotateImage(Bitmap bitmap, Uri imageUri)
    {
        Bitmap newBitmap = null;
        try
        {
            ExifInterface ei = new ExifInterface(getRealPathFromURI(imageUri));
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

            newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
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
            Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
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

    private void deleteingCapturedImage()
    {

        String[] projection = { MediaStore.Images.ImageColumns.SIZE, MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATA, BaseColumns._ID, };

        Cursor c = null;
        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        MobiculeLogger.info("InfoLog", "on activityresult Uri u " + u.toString());

        try
        {
            File noMediaFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera", ".nomedia");

            if (!noMediaFile.exists())
            {
                noMediaFile.createNewFile();
            }

            if (u != null)
            {
                c = activity.managedQuery(u, projection, null, null, null);
            }
            if ((c != null) && (c.moveToLast()))
            {
                MobiculeLogger.debug("c.getString(0) " + c.getString(0));
                MobiculeLogger.debug("c.getString(1) " + c.getString(1));
                MobiculeLogger.debug("c.getString(2) " + c.getString(2));
                MobiculeLogger.debug("c.getString(3) " + c.getString(3));

                ContentResolver cr = activity.getContentResolver();
                int i = cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, BaseColumns._ID + "=" + c.getString(3),
                        null);

                MobiculeLogger.debug("Number of column deleted : " + i);

            }
        }
        catch (Exception e)
        {
            MobiculeLogger.debug("deleteingCapturedImage() - " + e.toString());
            e.printStackTrace();
        }
    }

    public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness)
    {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }
}