package com.mobicule.android.msales.mgl.sign;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.mobicule.android.msales.mgl.util.Base64;
import com.mobicule.crashnotifier.GenericExceptionHandler;
import com.mobicule.crashnotifier.IGenericExceptionHandler;

public class GraphicView extends View implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Paint backGroundPaint;

	private Bitmap signatureBitmap;

	private Canvas graphicCanvas;

	private Path linePath;

	private Paint paint;

	private float start_X, start_Y;

	Context context;

	private byte[] bitmapdata;

	private boolean isSignatureWritten = false;

	IGenericExceptionHandler handler;
	public GraphicView(Context c, boolean isRandomCollection)
	{
		super(c);
		try
		{
			this.context = c;
			backGroundPaint = new Paint();
			backGroundPaint.setAntiAlias(true);
			backGroundPaint.setDither(true);
			backGroundPaint.setColor(Color.BLACK);
			backGroundPaint.setStyle(Paint.Style.STROKE);
			backGroundPaint.setStrokeJoin(Paint.Join.ROUND);
			backGroundPaint.setStrokeCap(Paint.Cap.ROUND);
			backGroundPaint.setStrokeWidth(5);

			signatureBitmap = Bitmap.createBitmap(SignatureActivity.width, SignatureActivity.height,
					Bitmap.Config.ARGB_8888);
			graphicCanvas = new Canvas(signatureBitmap);
			linePath = new Path();
			paint = new Paint(Paint.DITHER_FLAG);
		//	handler = GenericExceptionHandler.sharedInstance(this);
			refreshView();
		}
		catch (Exception e)
		{
			//handler.logCrashReport(e);
			e.printStackTrace();
		}

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		try
		{
			canvas.drawColor(Color.WHITE);

			canvas.drawBitmap(signatureBitmap, 0, 0, paint);

			canvas.drawPath(linePath, backGroundPaint);
		}
		catch (Exception e)
		{
			//handler.logCrashReport(e);
			e.printStackTrace();
		}

	}

	private void start_stnature(float x, float y)
	{
		try
		{
			linePath.reset();
			linePath.moveTo(x, y);
			start_X = x;
			start_Y = y;
		}
		catch (Exception e)
		{
			//handler.logCrashReport(e);
			e.printStackTrace();
		}
	}

	private void do_sign(float x, float y)
	{
		try
		{
			linePath.quadTo(start_X, start_Y, (x + start_X) / 2, (y + start_Y) / 2);
			start_X = x;
			start_Y = y;
		}
		catch (Exception e)
		{
			//handler.logCrashReport(e);
			e.printStackTrace();
		}
	}

	private void stop_signing()
	{
		try
		{
			linePath.lineTo(start_X, start_Y);
			graphicCanvas.drawPath(linePath, backGroundPaint);
			linePath.reset();
		}
		catch (Exception e)
		{
			//handler.logCrashReport(e);
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		try
		{
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					start_stnature(x, y);
					invalidate();
					break;
				case MotionEvent.ACTION_MOVE:
					isSignatureWritten = true;
					do_sign(x, y);
					invalidate();
					break;
				case MotionEvent.ACTION_UP:
					stop_signing();
					invalidate();
					break;
			}
			return true;
		}
		catch (Exception e)
		{
			//handler.logCrashReport(e);
			e.printStackTrace();
			return false;
		}
	}

	public String getSignatureImage()
	{
		return signatureImageStr;
	}

	String signatureImageStr;

	public boolean save()
	{
		try
		{
			ByteArrayOutputStream blob = new ByteArrayOutputStream();
			signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, blob);

			bitmapdata = blob.toByteArray();
			if (!isSignatureWritten)
			{
				Toast.makeText(this.context, "Please take signature", Toast.LENGTH_LONG).show();
				return false;
			}

			signatureImageStr = Base64.encodeBytes(bitmapdata);
			
			/*File mediaStorageDir = new File(
					Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MobiculeSignatures");

			if (!mediaStorageDir.exists())
			{
				mediaStorageDir.mkdirs();
			}

			File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "SIGN" + ".png");
			FileOutputStream out = new FileOutputStream(mediaFile);

			signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();*/
		}
		catch (Exception e)
		{
			//handler.logCrashReport(e);
			e.printStackTrace();
			Toast.makeText(context, "Not saved", Toast.LENGTH_SHORT).show();
		}
		return true;

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		return super.onKeyDown(keyCode, event);
	}

	public void saveDrawing(View v) throws IOException
	{

	}

	public void refreshView()
	{
		try
		{
			
			isSignatureWritten = false;
			this.draw(graphicCanvas);
			invalidate();
		}
		catch (Exception e)
		{
			//handler.logCrashReport(e);
			e.printStackTrace();
		}
	}
}
