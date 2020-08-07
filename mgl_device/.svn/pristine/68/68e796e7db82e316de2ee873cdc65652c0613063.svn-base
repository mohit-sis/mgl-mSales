package com.mobicule.android.msales.mgl.meterreading.view;

import com.mahanagar.R;

import android.app.Activity;
import android.graphics.RadialGradient;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class gasburningpossible extends Activity implements OnClickListener
{
	RadioButton rb_yes, rb_no, rb_meterWorking, rb_meternotworking;

	LinearLayout ll_burnerchecked;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gasburning);
		initialization();

	}

	private void initialization()
	{
		rb_yes = (RadioButton) findViewById(R.id.rb_yesmetercheckingpossible);
		rb_yes.setOnClickListener(this);
		rb_no = (RadioButton) findViewById(R.id.rb_nometercheckingpossible);
		rb_no.setOnClickListener(this);
		rb_meterWorking = (RadioButton) findViewById(R.id.rb_yesburnerchecked);
		rb_meterWorking.setOnClickListener(this);
		rb_meternotworking = (RadioButton) findViewById(R.id.rb_noburnerchecked);
		rb_meternotworking.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stubs
		switch (v.getId())
		{
			case R.id.rb_yesmetercheckingpossible:
				ll_burnerchecked.setVisibility(View.VISIBLE);
				break;
			case R.id.rb_nometercheckingpossible:
				ll_burnerchecked.setVisibility(View.GONE);

				break;

			default:
				break;
		}

	}
}
