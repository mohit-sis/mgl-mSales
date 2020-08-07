package com.mobicule.android.msales.mgl.meterreading.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.android.msales.mgl.util.Constants;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomCustomerListAdapter extends BaseAdapter
{

	private Context _context;

	private ArrayList<HashMap<String, String>> _customerArrayList;

	private String _searchKey;

	private int startPos;

	private int endPos;

	public CustomCustomerListAdapter(Context context, ArrayList<HashMap<String, String>> customerList, String searchKey)
	{
		_context = context;
		_customerArrayList = customerList;
		_searchKey = searchKey;
	}

	@Override
	public int getCount()
	{
		return _customerArrayList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return _customerArrayList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		if (convertView == null)
		{
			LayoutInflater inflator = LayoutInflater.from(_context);
			convertView = inflator.inflate(R.layout.userinfo_row_search, parent, false);
		}

			TextView tv_custName = (TextView) convertView.findViewById(R.id.txt_user_info_header);
			TextView tv_address = (TextView) convertView.findViewById(R.id.txt_user_info_detail);
			TextView tv_bpNo = (TextView) convertView.findViewById(R.id.txt_user_info_flatno);
			TextView tv_meterNo = (TextView) convertView.findViewById(R.id.txt_user_info_status);
			RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);



			if (_searchKey != null && !_searchKey.equals(""))
			{
				
				String address = _customerArrayList.get(position).get("address").toString();
				String[] addressArray = address.split(",");
				String floor = addressArray[0];
				String[] floorArray = floor.split(":");
				String floorValue = floorArray[1];
				MobiculeLogger.verbose("CustomCustomerListAdapter - floorValue :: "+floorValue);
				
				String buildingName = addressArray[4];
				String[] buildingNameArray = buildingName.split(":");
				String buildingNameValue = buildingNameArray[1];
				
				String meterNumber = _customerArrayList.get(position).get(Constants.KEY_CUSTOMER_METER_NUMBER).toString();
				String[] meterNoArray = meterNumber.split(":");
				String meterNoValue = meterNoArray[1];
				
				String bpNumber = _customerArrayList.get(position).get(Constants.FIELD_BP_NO).toString();
				String[] bpNumberArray = bpNumber.split(":");
				String bpNoValue = bpNumberArray[1];
				
				tv_custName.setText(_customerArrayList.get(position).get("customer_name").toString());
				tv_address.setText(_customerArrayList.get(position).get("address").toString());
				tv_bpNo.setText(_customerArrayList.get(position).get(Constants.FIELD_BP_NO).toString());
				tv_meterNo.setText(_customerArrayList.get(position).get(Constants.KEY_CUSTOMER_METER_NUMBER).toString());
				/*startPos = _customerArrayList.get(position).get(Constants.FIELD_CUSTOMER_NAME).indexOf(_searchKey);
				endPos = startPos + _searchKey.length();

				if (startPos != -1)
				{*/
					//    countryTextView.setText(spannable);
					//worldpopulationlist.add(wp);
					if (_customerArrayList.get(position).get("customer_name").toString().toLowerCase().contains(_searchKey.toLowerCase()))
					{
						startPos = _customerArrayList.get(position).get("customer_name").toString().toLowerCase().indexOf(_searchKey.toLowerCase());
						endPos = startPos + _searchKey.length();
						Spannable spannable = new SpannableString(_customerArrayList.get(position).get(
								"customer_name"));
						ColorStateList blueColor = new ColorStateList(new int[][] { new int[] {} },
								new int[] { Color.BLUE });
						TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
						spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

						tv_custName.setText(spannable);
						
					}
					if (floorValue.toLowerCase().contains(_searchKey.toLowerCase()))
					{
						startPos = _customerArrayList.get(position).get("address").toString().toLowerCase().indexOf(_searchKey.toLowerCase());
						endPos = startPos + _searchKey.length();
						Spannable spannable = new SpannableString(_customerArrayList.get(position).get(
								"address"));
						ColorStateList blueColor = new ColorStateList(new int[][] { new int[] {} },
								new int[] { Color.BLUE });
						TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
						spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

						tv_address.setText(spannable);
						
					}
					if(buildingNameValue.toLowerCase().contains(_searchKey.toLowerCase()))
					{
						startPos = _customerArrayList.get(position).get("address").toString().toLowerCase().indexOf(_searchKey.toLowerCase());
						endPos = startPos + _searchKey.length();
						Spannable spannable = new SpannableString(_customerArrayList.get(position).get("address"));
						
						ColorStateList blueColor = new ColorStateList(new int[][] { new int[] {} },
								new int[] { Color.BLUE });
						TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
						spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

						tv_address.setText(spannable);
						
					}
					if (bpNoValue.toLowerCase().contains(_searchKey.toLowerCase()))
					{
						startPos = _customerArrayList.get(position).get(Constants.FIELD_BP_NO).toString().toLowerCase().indexOf(_searchKey.toLowerCase());
						endPos = startPos + _searchKey.length();
						Spannable spannable = new SpannableString(_customerArrayList.get(position).get(
								Constants.FIELD_BP_NO));
						ColorStateList blueColor = new ColorStateList(new int[][] { new int[] {} },
								new int[] { Color.BLUE });
						TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
						spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						
						tv_bpNo.setText(spannable);
						
					}
					if (meterNoValue.toLowerCase().contains(_searchKey.toLowerCase()))
					{
						startPos = _customerArrayList.get(position).get(Constants.KEY_CUSTOMER_METER_NUMBER).toString().toLowerCase().indexOf(_searchKey.toLowerCase());
						endPos = startPos + _searchKey.length();
						Spannable spannable = new SpannableString(_customerArrayList.get(position).get(
								Constants.KEY_CUSTOMER_METER_NUMBER));
						ColorStateList blueColor = new ColorStateList(new int[][] { new int[] {} },
								new int[] { Color.BLUE });
						TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
						spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						
						tv_meterNo.setText(spannable);
					}

				//}

			}
			else
			{

				tv_custName.setText(_customerArrayList.get(position).get("customer_name").toString());
				tv_address.setText(_customerArrayList.get(position).get("address").toString());
				tv_bpNo.setText(_customerArrayList.get(position).get(Constants.FIELD_BP_NO).toString());
				tv_meterNo.setText(_customerArrayList.get(position).get(Constants.KEY_CUSTOMER_METER_NUMBER).toString());
			}

				return convertView;
	}

}