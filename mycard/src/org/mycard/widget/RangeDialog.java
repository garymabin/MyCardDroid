package org.mycard.widget;

import org.mycard.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RangeDialog extends AlertDialog implements DialogConfigUIBase {
	
	private View mView;
	
	private RangeDialogConfigController mController;
	
	private DialogInterface.OnClickListener mListener;
	
	private int mType;
	
	private int mCurrentMax;
	
	private int mCurrentMin;

	public RangeDialog(Context context, OnClickListener listener, int type, Bundle bundle) {
		super(context);
		mListener = listener;
		mType = type;
		mCurrentMax = bundle.getInt("max");
		mCurrentMin = bundle.getInt("min");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mView = getLayoutInflater().inflate(R.layout.range_dialog_content, null);
		setView(mView);
		mController = new RangeDialogConfigController(this, mView, mType, mCurrentMax, mCurrentMin);
		super.onCreate(savedInstanceState);
	}

	@Override
	public BaseDialogConfigController getController() {
		return mController;
	}

	@Override
	public void setPositiveButton(CharSequence text) {
		setButton(BUTTON_POSITIVE, text, mListener);
	}

	@Override
	public void setCancelButton(CharSequence text) {
		setButton(BUTTON_NEGATIVE, text, mListener);
	}

	@Override
	public Button getPosiveButton() {
		return getButton(BUTTON_POSITIVE);
	}

	@Override
	public Button getCancelButton() {
		return getButton(BUTTON_NEGATIVE);
	}

}
