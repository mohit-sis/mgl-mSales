package com.mobicule.android.msales.mgl.meterreading.view;

import android.app.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mobicule.msales.mgl.client.meterreading.IMeterReading.IInspection;

/**
 * 
 * <enter description here>
 * 
 * @author Prashant
 * @see
 * 
 * @createdOn 21-Mar-2013
 * @modifiedOn
 * 
 * @copyright © 2010-2011 Mobicule Technologies Pvt. Ltd. All rights reserved.
 */

public class InspectionDetails extends Dialog
{
	private IInspection inspection;

	private TextView textViewMeterIndexTampered;

	private TextView textViewMeterIndexTamperedRemark;

	private TextView textViewMeterProtectionSeal;

	private TextView textViewMeterProtectionSealRemark;

	private TextView textViewInputSealNo;

	private TextView textViewMeterBypass;

	private TextView textViewMeterBypassRemark;
	
	private TextView textViewCurrentSealNo;

	public InspectionDetails(Context context, IInspection inspection)
	{
		super(context);
		this.inspection = inspection;
		this.setCancelable(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(com.mahanagar.R.layout.inspection_details);
		initializeControls();
		fillControls();
	}

	public void showDialog()
	{
		show();
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = WindowManager.LayoutParams.FILL_PARENT;
		getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
	}

	private void initializeControls()
	{
		textViewMeterIndexTampered = (TextView) findViewById(com.mahanagar.R.id.inspectionDetails_Meter_Index_Tampered_Value);
		textViewMeterIndexTamperedRemark = (TextView) findViewById(com.mahanagar.R.id.inspectionDetails_Meter_Index_Tampered_Remark_Value);
		textViewMeterProtectionSeal = (TextView) findViewById(com.mahanagar.R.id.inspectionDetails_Meter_Protection_Seal_Value);
		textViewMeterProtectionSealRemark = (TextView) findViewById(com.mahanagar.R.id.inspectionDetails_Meter_Protection_Seal_Remark_Value);
		textViewInputSealNo = (TextView) findViewById(com.mahanagar.R.id.inspectionDetails_Input_Seal_No_Value);
		textViewMeterBypass = (TextView) findViewById(com.mahanagar.R.id.inspectionDetails_Meter_ByPass_Value);
		textViewMeterBypassRemark = (TextView) findViewById(com.mahanagar.R.id.inspectionDetails_Meter_ByPass_Remark_Value);
		textViewCurrentSealNo = (TextView) findViewById(com.mahanagar.R.id.inspectionDetails_Current_Seal_No_Value);
	}

	private void fillControls()
	{
		if (null == inspection)
		{
			return;
		}

		textViewMeterIndexTampered.setText(inspection.isMeterIndexTampered());
		textViewMeterIndexTamperedRemark.setText(inspection.getMeterIndexTamperedRemark());
		textViewMeterProtectionSeal.setText(inspection.getMeterProtectionSeal());
		textViewMeterProtectionSealRemark.setText(inspection.getMeterProtectionSealRemark());
		textViewInputSealNo.setText(inspection.getInputSealNo());
		textViewMeterBypass.setText(inspection.isMeterByPass());
		textViewCurrentSealNo.setText(inspection.getCurrentSealNo());
		textViewMeterBypassRemark.setText(inspection.getMeterByPassRemark());
	}
}
