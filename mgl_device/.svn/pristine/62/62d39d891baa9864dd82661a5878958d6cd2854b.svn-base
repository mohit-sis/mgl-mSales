package com.mobicule.android.msales.mgl.meterreading.view;

import android.os.AsyncTask;

public abstract class cropImageAsyncTask extends AsyncTask<Void, Void, Void>
{
   /* private Context context;

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
          //  flagForGps = false;
            ActivityCompat.startActivityForResult(intent, CROP_FROM_CAMERA);

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);
        progressDialog.dismiss();
    }*/

}
