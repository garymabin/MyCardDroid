package org.mycard.fragment;

import org.mycard.R;
import org.mycard.common.Constants;
import org.mycard.core.Controller;
import org.mycard.core.UserStatusTracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class PersonalCenterFragment extends BaseFragment {
	
	private ProgressDialog mProgressDialog;
	
	private int currentState;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_personal_center, null);
		int loginStatus = Controller.peekInstance().getLoginStatus();
		switchState(loginStatus);
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mProgressDialog = new ProgressDialog(activity);
		mProgressDialog.setMessage(activity.getString(R.string.logging));
		Controller.peekInstance().registerForLoginStatusChange(mHandler);
		mActivity.onActionBarChange(
				Constants.ACTION_BAR_CHANGE_TYPE_PAGE_CHANGE,
				FRAGMENT_ID_PERSONAL_CENTER, 0, null);
	}
	
	public void switchState(int state) {
		if (state == UserStatusTracker.LOGIN_STATUS_LOGGING) {
			mProgressDialog.show();
		} else {
			mProgressDialog.dismiss();
			Fragment fragment = null;
			switch (state) {
			case UserStatusTracker.LOGIN_STATUS_LOGGED_IN:
				fragment = new UserStatusFragment();
				break;
			case UserStatusTracker.LOGIN_STATUS_LOGIN_FAILED:
				Toast.makeText(mActivity, R.string.login_failed, Toast.LENGTH_SHORT).show();
			case UserStatusTracker.LOGIN_STATUS_LOG_OUT:
				fragment = new UserLoginFragment();
				break;
			default:
				break;
			}
			Bundle args = new Bundle();
			args.putString("username", Controller.peekInstance().getLoginName());
			fragment.setArguments(args);
			FragmentManager fm = getChildFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			fm.popBackStack();
			transaction.replace(R.id.personal_center_panel, fragment).commit();
		}
	}
	
	@Override
	public void onDetach() {
		Controller.peekInstance().unregisterForLoginStatusChange(mHandler);
		super.onDetach();
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		int status = msg.arg1;
		if (status != currentState) {
			switchState(status);
			currentState = status;
		}
		return false;
	}

}
