package com.mobicule.android.msales.mgl.menu.view;

import com.mobicule.android.component.logging.MobiculeLogger;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;


/**
 * This class does most of the work of wrapping the {@link PopupWindow} so it's simpler to use.
 * 
 *  @author qberticus
 *
 */
public class NewPopUpWindow implements OnDismissListener
{
	protected final View anchor;

	protected final PopupWindow window;

	private View root;

	private Drawable background = null;

	protected final WindowManager windowManager;

	protected boolean isCustNote = false;

	/**
	 * Create a BetterPopupWindow
	 *
	 * @param anchor
	 *            the view that the BetterPopupWindow will be displaying 'from'
	 */
	public NewPopUpWindow(View anchor)
	{
		this.anchor = anchor;
		this.window = new PopupWindow(anchor.getContext());

		// when a touch even happens outside of the window
		// make the window go away
		this.window.setTouchInterceptor(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
				{

					MobiculeLogger.verbose("dismiss Touch outside");

					NewPopUpWindow.this.window.dismiss();
					return true;
				}
				return false;
			}
		});

		this.windowManager = (WindowManager) this.anchor.getContext().getSystemService(Context.WINDOW_SERVICE);
		onCreate();
	}

	protected void onCreate()
	{
	}

	/**
	 * Perform all actions needed before displaying in this method.
	 */
	protected void onShow()
	{
	}

	protected void preShow()
	{
		if (this.root == null)
		{
			throw new IllegalStateException("setContentView was not called with a view to display.");
		}
		onShow();

		if (this.background == null)
		{
			this.window.setBackgroundDrawable(new BitmapDrawable());
		}
		else
		{
			this.window.setBackgroundDrawable(this.background);
		}

		this.window.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		this.window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		this.window.setTouchable(true);
		this.window.setFocusable(true);
		this.window.setOutsideTouchable(true);
		this.window.setOnDismissListener(this);
		this.window.setContentView(this.root);
	}

	public void setBackgroundDrawable(Drawable background)
	{

		this.background = background;
	}

	/**
	 * Sets the content view. Probably should be called from {@link onCreate}
	 *
	 * @param root
	 *            the view the popup will display
	 */
	public void setContentView(View root)
	{
		this.root = root;
		this.window.setContentView(root);
	}

	/**
	 * Will inflate and set the view from a resource id
	 *
	 * @param layoutResID
	 */
	public void setContentView(int layoutResID)
	{
		LayoutInflater inflator = (LayoutInflater) this.anchor.getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		this.setContentView(inflator.inflate(layoutResID, null));
	}

	/**
	 * If you want to do anything when {@link dismiss} is called
	 *
	 * @param listener
	 */
	public void setOnDismissListener(PopupWindow.OnDismissListener listener)
	{
		this.window.setOnDismissListener(listener);
	}

	/**
	 * Displays like a popdown menu from the anchor view
	 */
	public void showLikePopDownMenu()
	{
		this.showLikePopDownMenu(0, 0);
	}

	/**
	 * Displays like a popdown menu from the anchor view.
	 *
	 * @param xOffset
	 *            offset in X direction
	 * @param yOffset
	 *            offset in Y direction
	 */
	public void showLikePopDownMenu(int xOffset, int yOffset)
	{
		this.preShow();

		this.window.showAsDropDown(this.anchor, xOffset, yOffset);
	}

	/**
	 * Displays like a QuickAction from the anchor view.
	 */
	public void showLikeQuickAction()
	{
		this.showLikeQuickAction(0, 0);
	}

	/**
	 * Displays like a QuickAction from the anchor view.
	 *
	 * @param xOffset
	 *            offset in the X direction
	 * @param yOffset
	 *            offset in the Y direction
	 */
	public void showLikeQuickAction(int xOffset, int yOffset)
	{
		this.preShow();

		//	this.window.setAnimationStyle(R.style.Animations_GrowFromBottom);

		int[] location = new int[2];
		this.anchor.getLocationOnScreen(location);

		Rect anchorRect = new Rect(location[0], location[1], location[0] + this.anchor.getWidth(), location[1]
				+ this.anchor.getHeight());

		this.root.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		int rootWidth = this.root.getMeasuredWidth();
		int rootHeight = this.root.getMeasuredHeight();

		int screenWidth = this.windowManager.getDefaultDisplay().getWidth();
		int screenHeight = this.windowManager.getDefaultDisplay().getHeight();

		int xPos = ((screenWidth - rootWidth) / 2) + xOffset;
		int yPos = anchorRect.top - rootHeight + yOffset;

		// display on bottom
		if (screenHeight - anchorRect.bottom > rootHeight)
		{
			yPos = anchorRect.bottom + yOffset;
			//	this.window.setAnimationStyle(R.style.Animations_GrowFromTop);
		}

		this.window.showAtLocation(this.anchor, Gravity.NO_GRAVITY, xPos, yPos);
	}

	@Override
	public void onDismiss()
	{
		MobiculeLogger.verbose("dismiss called");

		if (isCustNote)
		{
			MobiculeLogger.verbose("dismiss touch2 getting text");

			//MyBaseActivity.getEditText();
		}
		this.window.dismiss();

	}
}
