package org.mycard.fragment;

import org.mycard.R;
import org.mycard.core.Controller;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class UserLoginFragment extends BaseFragment {
	
	private String mLoginName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLoginName = getArguments().getString("username");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		rootView = inflater.inflate(R.layout.user_login, null);
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
			}

		});
		return rootView;
	}
}
