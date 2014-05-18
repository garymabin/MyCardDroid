package org.mycard.widget;

import org.mycard.R;

import cn.garymb.ygodata.YGOGameOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class RoomDialog extends BaseDialog {
	
	private YGOGameOptions mOptions;
	private boolean mIsPrivate;
	
	private int mMode;
	
	public RoomDialog(Context context, DialogInterface.OnClickListener listener, YGOGameOptions options, boolean isPrivate, int mode) {
		super(context, listener);
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
		((RoomDialogConfigController) mController).enableSubmitIfAppropriate();
	}
	
}
