package com.mobicule.android.msales.mgl.menu.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.mobicule.android.component.logging.MobiculeLogger;

public class QuickActionBar extends NewPopUpWindow
{
	private View root;

	//private final ImageView arrowUp;
	//private final ImageView arrowDown;

	private final LayoutInflater inflater;

	private final Context context;

	public static final int GROW_FROM_LEFT = 1;

	public static final int GROW_FROM_CENTER = 2;

	public static final int GROW_FROM_RIGHT = 3;

	private int xPos;

	private int animationStyle;

	private boolean startAnimation;

	private ViewGroup itemArray;

	private ArrayList<QuickActionIcons> itemList;

	public int layout = -1;

	public static int selected_layout_for_alignment;

	public QuickActionBar(View anchor, View v, int xPos, boolean isCustNote)
	{
		super(anchor);

		this.xPos = xPos;
		this.isCustNote = isCustNote;

		itemList = new ArrayList<QuickActionIcons>();
		context = anchor.getContext();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		root = v;

		//arrowDown 	= (ImageView) root.findViewById(R.id.arrow_up);
		//arrowUp 	= (ImageView) root.findViewById(R.id.arrow_up);

		setContentView(root);

		//itemArray 	= (ViewGroup) root.findViewById(R.id.itemList);
		animationStyle = GROW_FROM_CENTER;
		startAnimation = true;
	}

	public void setLayoutTObeInflated(int layout)
	{
		this.layout = layout;
		MobiculeLogger.verbose("setting layout id - " + layout + " this.lay - " + this.layout);
	}

	public void setAnimationStyle(int style)
	{
		this.animationStyle = style;
	}

	public void addItem(QuickActionIcons action)
	{
		itemList.add(action);
	}

	public void dismiss()
	{
		window.dismiss();
	}

	public void show()
	{
		preShow();

		int[] location = new int[2];

		anchor.getLocationOnScreen(location);

		Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1]
				+ anchor.getHeight());

		root.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		root.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		int rootWidth = root.getMeasuredWidth();
		int rootHeight = root.getMeasuredHeight();

		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();

		//int xPos 			= (screenWidth - rootWidth) / 2;
		int yPos = anchorRect.top - rootHeight;

		boolean popupOnTop = true;

		// display popup at the bottom

		if (screenHeight - anchorRect.bottom > rootHeight)
		{
			yPos = anchorRect.bottom;
			popupOnTop = false;
		}

		setAnimationStyle(false);

		//setUpList();

		//window.showAtLocation(this.anchor, Gravity.NO_GRAVITY, xPos, yPos);

		window.showAtLocation(this.anchor, Gravity.NO_GRAVITY, xPos, yPos+1);

		//if (startAnimation) itemArray.startAnimation(loadAnimation);
	}

	private void setAnimationStyle(boolean popupOnTop)
	{

	}

}
