package org.mycard.widget;

import org.mycard.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class DonateDialog extends AlertDialog implements DialogConfigUIBase {
	
	private View mView;
	
	private DonateDialogConfigController mController;
	
	private DialogInterface.OnClickListener mListener;

	public DonateDialog(Context context, DialogInterface.OnClickListener listener) {
		super(context);
		mListener = listener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mView = LayoutInflater.from(getContext()).inflate(R.layout.donate_content, null);
		setView(mView);
		mController = new DonateDialogConfigController(this, mView);
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
