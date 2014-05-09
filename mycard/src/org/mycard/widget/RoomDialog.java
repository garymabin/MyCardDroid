package org.mycard.widget;

import org.mycard.R;

import cn.garymb.ygodata.YGOGameOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RoomDialog extends AlertDialog implements DialogConfigUIBase{
	
	private final DialogInterface.OnClickListener mListener;
	
	private YGOGameOptions mOptions;
	private View mView;
	
	private boolean mIsPrivate;
	
	private int mMode;
	
	private RoomDialogConfigController mController;
	
	
	public RoomDialog(Context context, DialogInterface.OnClickListener listener, YGOGameOptions options, boolean isPrivate, int mode) {
		super(context);
		mListener = listener;
		mOptions = options;
		mIsPrivate = isPrivate;
		mMode = mode;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mView = getLayoutInflater().inflate(R.layout.room_detail_content, null);
		setView(mView);
		mController = new RoomDialogConfigController(this, mView, mOptions, mIsPrivate, mMode);
		setInverseBackgroundForced(true);
		super.onCreate(savedInstanceState);
		mController.enableSubmitIfAppropriate();
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
	public BaseDialogConfigController getController() {
		return mController;
	}

	@Override
	public Button getPosiveButton() {
		// TODO Auto-generated method stub
		return getButton(BUTTON_POSITIVE);
	}

	@Override
	public Button getCancelButton() {
		// TODO Auto-generated method stub
		return getButton(BUTTON_NEGATIVE);
	}
	
}
