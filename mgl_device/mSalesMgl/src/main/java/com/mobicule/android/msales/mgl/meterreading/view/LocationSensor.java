package com.mobicule.android.msales.mgl.meterreading.view;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.mobicule.android.component.logging.MobiculeLogger;
import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IMeterReadingInstance;

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
* @copyright � 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
*/
public class LocationSensor
{
	private Context context;

	private LocationManager mLocManager;

	private LocationListener mLocListener;

	private IMeterReadingInstance meterReadingBO;

	private static String locationArea = "";

	private static final String TAG = "LocationSensor";

	public LocationSensor(Context context, IMeterReadingInstance meterReadingBO)
	{
		this.context = context;
		this.meterReadingBO = meterReadingBO;
		mLocManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		mLocListener = new MyLocationListener();

		Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = mLocManager.getBestProvider(criteria, true);
		mLocManager.requestLocationUpdates(provider, 0, 0, mLocListener);
		
		//mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocListener);
	}
	
	public static String getArea()
	{
		MobiculeLogger.verbose("locationArea: "+locationArea);
		return locationArea;
	}

	private void updateWithNewLocation(Location location)
	{
		//double lat=0.0;
	//	double lng=0.0;
		MobiculeLogger.verbose("location - "+location);
		
		if (location != null)
		{

			String area = "";
			try
			{
				double lat = location.getLatitude();
				double lng = location.getLongitude();

				Geocoder gc = new Geocoder(context);
				
				MobiculeLogger.verbose("Latitude is :::  " + lat);
				MobiculeLogger.verbose("Longitude is ::: " + lng);
				
				List<Address> addresses = gc.getFromLocation(lat, lng, 5);

				if (addresses.size() > 0)
				{
					Address address = addresses.get(0);
					area = address.getAddressLine(0);
				}
				
				if(meterReadingBO != null)
				{
					MobiculeLogger.verbose("meterReadingBO - "+meterReadingBO);
					
					meterReadingBO.setArea(area);
				}
				else
				{
					MobiculeLogger.verbose("area - "+area);
					
					locationArea = area;
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			if(meterReadingBO != null)
			{
				meterReadingBO.setArea("");
			}
			else
			{
				locationArea = "";
			}
		}
		mLocManager.removeUpdates(mLocListener);

		/*if (location !=null)
		{
			 new Thread(new Runnable()
			{
				
				@Override
				public void run()
				{
					double lat = location.getLatitude();
					double lng = location.getLongitude();
					String area = "";
					try
					{
						Geocoder gc = new Geocoder(context);
						List<Address> addresses = gc.getFromLocation(lat, lng, 5);

						if (addresses.size() > 0)
						{
							Address address = addresses.get(0);
							area = address.getAddressLine(0);
						}
						meterReadingBO.setArea(area);

					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}).start();
		}
		else
		{
			meterReadingBO.setArea("");
		}
		mLocManager.removeUpdates(mLocListener);*/
	}

	public class MyLocationListener implements LocationListener
	{

		@Override
		public void onLocationChanged(Location location)
		{
			MobiculeLogger.verbose("onLocationChanged: ");
			
			updateWithNewLocation(location);
		}

		@Override
		public void onProviderDisabled(String provider)
		{
			MobiculeLogger.verbose("onProviderDisabled: ");
			
			if(meterReadingBO != null)
			{
				meterReadingBO.setArea("");
			}
			else
			{
				locationArea = "";
			}
		}

		@Override
		public void onProviderEnabled(String provider)
		{
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{

		}
	}

}