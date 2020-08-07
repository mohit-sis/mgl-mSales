/**
 * 
 */
package com.mobicule.android.msales.mgl.commons.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mahanagar.R;
import com.mobicule.android.msales.mgl.commons.model.DownloadWebPageTask;
import com.mobicule.android.msales.mgl.util.Constants;
import com.mobicule.android.msales.mgl.util.DynamicUrl;

/**
 * @author nikita
 *
 */
public class DialogDisplay
{
	public static void diagnosticDialog(final Context context)
	{
		final Dialog dialog = new Dialog(context);
		dialog.setTitle("Diagnostics");
		dialog.setContentView(R.layout.diagnostic);
		dialog.setCancelable(true);
		TextView internetAccess = (TextView) dialog.findViewById(R.id.internetAccess);
		TextView serverAccess = (TextView) dialog.findViewById(R.id.serverAccess);
		internetAccess.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				callThread(context, dialog, "internet");
			}
		});

		serverAccess.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				callThread(context, dialog, "server");
			}
		});

		Button ok = (Button) dialog.findViewById(R.id.diagnostic_ok);
		ok.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private static void callThread(Context context, final Dialog dialog, final String which)
	{
		final DownloadWebPageTask task = new DownloadWebPageTask(context, dialog.findViewById(R.id.internet),
				dialog.findViewById(R.id.server), which);
		final Handler handler = new Handler();
		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				handler.post(new Runnable()
				{
					@Override
					public void run()
					{
						if (which.equals("internet"))
						{
							task.execute(new String[] { Constants.GOOGLE_URL });
						}
						else
						{
							String URL_1 = Constants.SERVER_URL_1 + Constants.MGL_SERVER;
							String URL_2 = Constants.SERVER_URL_2 + Constants.MGL_SERVER;
							task.execute(new String[] { URL_1,URL_2 });
						}

					}
				});

			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}
}
