package org.mycard.fragment;

import org.mycard.R;
import org.mycard.common.Constants;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class UserStatusFragment extends BaseFragment {
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity.onActionBarChange(
				Constants.ACTION_BAR_CHANGE_TYPE_PAGE_CHANGE,
				FRAGMENT_ID_USER_STATUS, null);
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
			}
			
		});
		return rootView;
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}

}
