package com.mobicule.android.msales.mgl.util;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mahanagar.R;
import com.mobicule.android.msales.mgl.meterreading.view.MeterReading;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
	private Context activity;
 
    private int value;	
	private String[] spinnerList;

	public CustomSpinnerAdapter(Context context, int textViewResourceId,
			String[] msgList,int flag) {
		super(context, textViewResourceId, msgList);
		this.activity = context;
		spinnerList = msgList;
		this.value=flag; 
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
		View layout = inflater.inflate(R.layout.customspinner, parent, false);

		TextView txtCaseStatusItem = (TextView) layout
				.findViewById(R.id.textViewSpinItem);
		txtCaseStatusItem.setTypeface(null, Typeface.BOLD);
		txtCaseStatusItem.setText(spinnerList[position]);
		
		/*if (position == 10 || position == 17 || position == 3) {

			txtCaseStatusItem.setVisibility(View.GONE);

		}

		if (MeterReading.zeroConsumption == 1 && position == 3) {
			txtCaseStatusItem.setVisibility(View.VISIBLE);
		} 
		else if(MeterReading.zeroConsumption==0 && position==3 && value==0){
			txtCaseStatusItem.setVisibility(View.VISIBLE);
		}*/
		
		return layout;
	}

	// It gets a View that displays in the drop down popup the data at the
	// specified position
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	// It gets a View that displays the data at the specified position
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}
}
