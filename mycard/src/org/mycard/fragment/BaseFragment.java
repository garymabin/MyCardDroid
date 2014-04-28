package org.mycard.fragment;

import org.mycard.MainActivity;
import org.mycard.R;
import org.mycard.common.Constants;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment implements Handler.Callback, Constants{

	public interface OnActionBarChangeCallback {
		void onActionBarChange(int msgType, int action, Object extra);
	}

	/**
	 * @author mabin
	 * 
	 */
	public static class DataHandler extends Handler {
		/**
		 * 
		 */
		public DataHandler(Looper looper, Callback callback) {
			// TODO Auto-generated constructor stub
			super(looper, callback);
		}
	}
	
	public static final String ARG_ITEM_TITLE = "basefragment.title";

	protected MainActivity mActivity;
	protected DataHandler mHandler;
	protected OnActionBarChangeCallback mActionBarCallback;
	
	protected TextView mActionBarTitle;
	
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		mActionBarTitle = (TextView) mActivity.findViewById(actionBarTitleId);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity = (MainActivity) activity;
		mHandler = new DataHandler(mActivity.getMainLooper(), this);
		if (activity instanceof OnActionBarChangeCallback) {
			mActionBarCallback = (OnActionBarChangeCallback) activity;
		}
	}

	protected void showDialog(Bundle params, Fragment target, int requestCode) {
		// DialogFragment.show() will take care of adding the fragment
		// in a transaction. We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		Fragment prev = getChildFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		RoomDetailFragment newFragment = RoomDetailFragment
				.newInstance(params);
		newFragment.setTargetFragment(target, requestCode);
		newFragment.show(ft, "dialog");
	}
	
	protected void showDialog(Bundle params) {
		// DialogFragment.show() will take care of adding the fragment
		// in a transaction. We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		Fragment prev = getChildFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		RoomDetailFragment newFragment = RoomDetailFragment
				.newInstance(params);
		newFragment.show(ft, "dialog");
	}
}
