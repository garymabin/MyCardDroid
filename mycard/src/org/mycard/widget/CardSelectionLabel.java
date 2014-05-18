package org.mycard.widget;

import org.mycard.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CardSelectionLabel extends LinearLayout {
	
	private int originColor;
	private int selectionColor;
	
	private TextView mLabel;

	public CardSelectionLabel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CardSelectionLabel(Context context) {
		super(context);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mLabel = (TextView) findViewById(R.id.cardSelectionLabel1);
		originColor = getResources().getColor(R.color.black);
		selectionColor = getResources().getColor(R.color.apptheme_color);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean handled = false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setPressed(true);
			handled = true;
			break;
		case MotionEvent.ACTION_CANCEL:
			handled = false;
			break;

		case MotionEvent.ACTION_UP:
			setPressed(false);
			setSelected(!isSelected());
			performClick();
			handled = true;
			break;
		}
		if (!handled) {
			super.onTouchEvent(event);
		}
		return handled;
	}
	
	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		mLabel.setTextColor(isSelected() ? selectionColor : originColor);
	}
	
}
