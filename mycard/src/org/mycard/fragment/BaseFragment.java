package org.mycard.fragment;

import org.mycard.MainActivity;
import org.mycard.core.UpdateController;
import org.mycard.data.DataStore;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment implements Handler.Callback{
	
	public interface OnActionBarChangeCallback {
		void onActionBarChange(int msgType, int action);
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

	protected MainActivity mActivity;
	protected UpdateController mController;
	protected DataHandler mHandler;
	protected DataStore mDataStore;
	protected OnActionBarChangeCallback mActionBarCallback;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity = (MainActivity) activity;
		mController = mActivity.getController();
		mDataStore = mController.getDataStore();
		mHandler = new DataHandler(mActivity.getMainLooper(), this);
		if (activity instanceof OnActionBarChangeCallback) {
			mActionBarCallback = (OnActionBarChangeCallback) activity;
		}
	}
}
