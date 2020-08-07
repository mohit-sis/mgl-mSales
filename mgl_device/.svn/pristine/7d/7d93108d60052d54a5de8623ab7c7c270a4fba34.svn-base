package com.mobicule.android.msales.mgl.meterreading.view;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String>
{

	private Context activity;

	private String[] alertMessageEnglishList;
	private String[] alertMessageHindiList;
	private String[] alertMessageCodeList;

	public CustomListAdapter(Context context, int textViewResourceId, String[] alertMessageCodeList, String[] alertMessageHindiList, String[] alertMessageEnglishList)
	{
		super(context, textViewResourceId, alertMessageEnglishList);
		this.activity = context;
		this.alertMessageCodeList = alertMessageCodeList;
		this.alertMessageEnglishList = alertMessageEnglishList;
		this.alertMessageHindiList = alertMessageHindiList;
	}

	public View getCustomView(int position, View convertView, ViewGroup parent)
	{
		MobiculeLogger.verbose("CustomListAdapter - alertMessageHindiList length : "+alertMessageHindiList.length);
		MobiculeLogger.verbose("CustomListAdapter - alertMessageEnglishList length : "+alertMessageEnglishList.length);
		MobiculeLogger.verbose("CustomListAdapter - position : "+position);
		
		LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
		View layout = inflater.inflate(R.layout.alert_dialog_list_item, parent, false);

		TextView txtCode = (TextView) layout.findViewById(R.id.txt_code);
		TextView txtEnglish = (TextView) layout.findViewById(R.id.txt_english);
		TextView txtHindi = (TextView) layout.findViewById(R.id.txt_hindi);
		
		if(alertMessageCodeList.length != 0)
		{
			txtCode.setVisibility(View.VISIBLE);
			txtCode.setTypeface(null, Typeface.BOLD);
			
			if(position < alertMessageCodeList.length)
			txtCode.setText(alertMessageCodeList[position]);
		}
		else
		{
			txtCode.setVisibility(View.GONE);
		}
		
		txtEnglish.setTypeface(null, Typeface.BOLD);
		
		if(position < alertMessageEnglishList.length)
		txtEnglish.setText(alertMessageEnglishList[position]);
		
		txtHindi.setTypeface(null, Typeface.BOLD);
		
		if(position < alertMessageHindiList.length)
		txtHindi.setText(alertMessageHindiList[position]);

		return layout;
	}

	// It gets a View that displays the data at the specified position
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return getCustomView(position, convertView, parent);
	}
	
	@Override
	public int getCount()
	{
		return super.getCount();
	}

}
