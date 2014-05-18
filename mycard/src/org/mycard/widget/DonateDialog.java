package org.mycard.widget;

import org.mycard.R;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

public class DonateDialog extends BaseDialog {
	
	public DonateDialog(Context context, OnClickListener listener) {
		super(context, listener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mView = LayoutInflater.from(getContext()).inflate(R.layout.donate_content, null);
		setView(mView);
		mController = new DonateDialogConfigController(this, mView);
		super.onCreate(savedInstanceState);
	}

}
