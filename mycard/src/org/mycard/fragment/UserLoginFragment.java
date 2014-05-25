package org.mycard.fragment;

import org.mycard.R;
import org.mycard.core.Controller;
import org.mycard.core.UserStatusTracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserLoginFragment extends BaseFragment {
	
	private String mLoginName;
	private int mLoginStatus;
	
	private ProgressDialog mProgressDialog;
	
	private int currentState = UserStatusTracker.LOGIN_STATUS_LOG_OUT;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLoginName = getArguments().getString("username");
		mLoginStatus = getArguments().getInt("userstatus");
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mProgressDialog = new ProgressDialog(activity);
		mProgressDialog.setMessage(activity.getString(R.string.logging));
		Controller.peekInstance().registerForLoginStatusChange(mHandler);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Controller.peekInstance().unregisterForLoginStatusChange(mHandler);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.user_login, null);
		final EditText usernameEditText = (EditText) rootView
				.findViewById(R.id.usernameEditText);
		final EditText passwordEditText = (EditText) rootView
				.findViewById(R.id.passWordEditText);
		if (!TextUtils.isEmpty(mLoginName)) {
			usernameEditText.setText(mLoginName);
		}
		Button loginButton = (Button) rootView.findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				String username = usernameEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				Bundle loginBundle = new Bundle();
				loginBundle.putString(BUNDLE_KEY_USER_NAME, username);
				loginBundle.putString(BUNDLE_KEY_USER_PW, password);
				Controller.peekInstance().asyncLogin(loginBundle);
				InputMethodManager im = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
				im.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
			}

		});
		switchState(mLoginStatus);
		return rootView;
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

	private void switchState(int status) {
		if (status == UserStatusTracker.LOGIN_STATUS_LOGGING) {
			mProgressDialog.show();
		} else if (status == UserStatusTracker.LOGIN_STATUS_LOGIN_FAILED) {
			mProgressDialog.dismiss();
			Toast.makeText(mActivity, R.string.login_failed, Toast.LENGTH_SHORT).show();
		} else if (status == UserStatusTracker.LOGIN_STATUS_LOGGED_IN){
			mProgressDialog.dismiss();
			((BaseFragment)getTargetFragment()).onEventFromChild(getTargetRequestCode(),
					FRAGMENT_NAVIGATION_DUEL_LOGIN_SUCCEED_EVENT, -1, -1, null);
		}
	}
}
