package org.mycard.fragment;

import org.mycard.R;
import org.mycard.common.Constants;
import org.mycard.core.Controller;
import org.mycard.core.UserStatusTracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class UserStatusFragment extends BaseFragment {
	private ProgressDialog mProgressDialog;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity.onActionBarChange(
				Constants.ACTION_BAR_CHANGE_TYPE_PAGE_CHANGE,
				FRAGMENT_ID_USER_STATUS, null);
		Controller.peekInstance().registerForLoginStatusChange(mHandler);
		mProgressDialog = new ProgressDialog(activity);
		mProgressDialog.setMessage(activity.getString(R.string.logging));
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Controller.peekInstance().unregisterForLoginStatusChange(mHandler);
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.user_login, null);
		final EditText usernameEditText = (EditText) rootView.findViewById(R.id.usernameEditText);
		final EditText passwordEditText = (EditText) rootView.findViewById(R.id.passWordEditText);
		Button loginButton = (Button) rootView.findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				String username = usernameEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				Bundle loginBundle = new Bundle();
				loginBundle.putString(BUNDLE_KEY_USER_NAME, username);
				loginBundle.putString(BUNDLE_KEY_USER_PW, password);
				Controller.peekInstance().asyncLogin(loginBundle);
			}
			
		});
		return rootView;
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		int status = msg.arg1;
		if (status == UserStatusTracker.LOGIN_STATUS_LOGGING) {
			mProgressDialog.show();
			Log.e("233", "logging...");
		}
		else if (status == UserStatusTracker.LOGIN_STATUS_LOGIN_FAILED) {
			mProgressDialog.dismiss();
			Log.e("233", "login failed");
		}
		else if (status == UserStatusTracker.LOGIN_STATUS_LOGGED_IN) {
			mProgressDialog.dismiss();
			Log.e("233", "logged in");
		}
		else if (status == UserStatusTracker.LOGIN_STATUS_LOG_OUT) {
			mProgressDialog.dismiss();
			Log.e("233", "logged out");
		}
		return false;
	}

}
