/*
 *****************************************************************************
 * Copyright (C) 2005-2013 UCWEB Corporation. All Rights Reserved
 * File        : RoomInfoFragment.java
 * Description : 
 * Creation    : 2014年4月9日
 * Author      : mabin@ucweb.com
 * History     : 
 *               Creation, 2014年4月9日, mabin, Create the file
 ******************************************************************************
 **/
package org.mycard.fragment;

import org.mycard.MainActivity;
import org.mycard.R;
import org.mycard.model.data.ResourcesConstants;
import org.mycard.widget.RoomConfigController;
import org.mycard.widget.RoomConfigUIBase;
import org.mycard.widget.RoomDialog;

import cn.garymb.ygodata.YGOGameOptions;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author mabin
 * 
 */
public class RoomDetailFragment extends DialogFragment implements
		OnTouchListener, ResourcesConstants, OnClickListener, RoomConfigUIBase{


	private MainActivity mActivity;

	private YGOGameOptions mGameOptions;
	
	private boolean mIsPrivate;
	
	
	private int mDialogMode;

	/**
	 * Create a new instance of WelfareDialogFragment, providing "num" as an
	 * argument.
	 */
	public static RoomDetailFragment newInstance(Bundle bundle) {
		RoomDetailFragment f = new RoomDetailFragment();

		// Supply num input as an argument.
		f.setArguments(bundle);

		return f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.DialogFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity = (MainActivity) activity;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		final Resources res = getResources();
        // Title
        final int titleId = res.getIdentifier("alertTitle", "id", "android");
        final View title = getDialog().findViewById(titleId);
        if (title != null) {
            ((TextView) title).setTextColor(res.getColor(R.color.apptheme_color));
        }

        // Title divider
        final int titleDividerId = res.getIdentifier("titleDivider", "id", "android");
        final View titleDivider = getDialog().findViewById(titleDividerId);
        if (titleDivider != null) {
            titleDivider.setBackgroundColor(res.getColor(R.color.apptheme_color));
        }
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGameOptions = getArguments().getParcelable(GAME_OPTIONS);
		mIsPrivate = getArguments().getBoolean(PRIVATE_OPTIONS,  false);
		mDialogMode = getArguments().getInt(MODE_OPTIONS);
		
		setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View,
	 * android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		view.setOnTouchListener(this);
		super.onViewCreated(view, savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		RoomDialog dlg = new RoomDialog(mActivity, this, mGameOptions, mIsPrivate, mDialogMode);
		return dlg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == AlertDialog.BUTTON_POSITIVE) {
			Intent intent = new Intent();
			YGOGameOptions options = null ;
			if (mDialogMode == DIALOG_MODE_CREATE_ROOM) {
				options = ((RoomDialog)dialog).getController().getGameOption();
				options.mServerAddr = mActivity.getServer().ipAddrString;
				options.mPort = mActivity.getServer().port;
				options.mName = "illusory";
			} else if (mDialogMode == DIALOG_MODE_JOIN_GAME) {
				options = mGameOptions;
			} else if (mDialogMode == DIALOG_MODE_QUICK_JOIN) {
				Fragment f = getTargetFragment();
				((DuelFragment)f).handleMessage(Message.obtain(null, getTargetRequestCode(), 0, 0, options = ((RoomDialog)dialog).getController().getGameOption()));
				return;
			}
			ComponentName component = new ComponentName("cn.garymb.ygomobile", "cn.garymb.ygomobile.YGOMobileActivity");
			intent.setComponent(component);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent.putExtra(YGOGameOptions.YGO_GAME_OPTIONS_BUNDLE_KEY, options);
			startActivity(intent);
		} else if (which == AlertDialog.BUTTON_NEGATIVE) {
		}
		
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RoomConfigController getController() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPositiveButton(CharSequence text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCancelButton(CharSequence text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Button getPosiveButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Button getCancelButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTitle(CharSequence text) {
		getDialog().setTitle(text);
	}

	@Override
	public void setTitle(int resId) {
		getDialog().setTitle(resId);
	}
}
