package org.mycard.fragment;

import org.mycard.R;
import org.mycard.common.Constants;
import org.mycard.core.Controller;
import org.mycard.core.UserStatusTracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class DuelFragment extends BaseFragment implements OnNavigationListener {

	private String[] mDuelList;
	
	private static final int REQUEST_ID_DUEL = 0;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity.onActionBarChange(Constants.ACTION_BAR_CHANGE_TYPE_PAGE_CHANGE,
				FRAGMENT_ID_DUEL, 0, null);
		mDuelList = getResources().getStringArray(R.array.duel_list);
		mActivity.getSupportActionBar().setListNavigationCallbacks(new ArrayAdapter<String>(mActivity,
				android.R.layout.simple_spinner_dropdown_item, mDuelList), this);
		mActivity.getSupportActionBar().setSelectedNavigationItem(0);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.duel_panel, null);
		return view;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long arg1) {
		switchState(position, Controller.peekInstance().getLoginStatus());
		return true;
	}

	private void switchState(int position, int loginStatus) {
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		Fragment fragment;
		if (position == 0) {
			if (loginStatus == UserStatusTracker.LOGIN_STATUS_LOGGED_IN) {
				fragment = new RoomListFragment();
			} else if (loginStatus == UserStatusTracker.LOGIN_STATUS_LOG_OUT || 
					loginStatus == UserStatusTracker.LOGIN_STATUS_LOGIN_FAILED){
				fragment = new LoginHintFragment();
				Bundle bundle = new Bundle();
				bundle.putInt("loginstatus", loginStatus);
				fragment.setTargetFragment(this, REQUEST_ID_DUEL);
				fragment.setArguments(bundle);
			} else {
				Bundle bundle = new Bundle();
				bundle.putString("username", Controller.peekInstance().getLoginName());
				bundle.putInt("userstatus", UserStatusTracker.LOGIN_STATUS_LOGGING);
				mActivity.navigateToChildFragment(bundle, FRAGMENT_ID_USER_LOGIN, REQUEST_ID_DUEL);
				return;
			}
		} else {
			fragment = new FreeDuelFragment();
		}
		ft.replace(R.id.duel_panel, fragment);
		ft.commit();
	}
	
	@Override
	public void onEventFromChild(int requestCode, int eventType, int arg1,
			int arg2, Object data) {
		if (requestCode == REQUEST_ID_DUEL) {
			if (eventType == FRAGMENT_NAVIGATION_DUEL_FREE_MODE_EVENT) {
				mActivity.getSupportActionBar().setSelectedNavigationItem(1);
			} else if (eventType == FRAGMENT_NAVIGATION_DUEL_LOGIN_ATTEMP_EVENT) {
				Bundle bundle = new Bundle();
				bundle.putString("username", Controller.peekInstance().getLoginName());
				bundle.putInt("userstatus", arg1);
				mActivity.navigateToChildFragment(bundle, FRAGMENT_ID_USER_LOGIN, REQUEST_ID_DUEL);
			} else if (eventType == FRAGMENT_NAVIGATION_DUEL_LOGIN_SUCCEED_EVENT) {
				FragmentManager fm = mActivity.getSupportFragmentManager();
				fm.popBackStackImmediate();
				FragmentTransaction ft = getChildFragmentManager().beginTransaction();
				Fragment fragment = new RoomListFragment();
				ft.replace(R.id.duel_panel, fragment);
				ft.commitAllowingStateLoss();
			}
		}
	}
}
