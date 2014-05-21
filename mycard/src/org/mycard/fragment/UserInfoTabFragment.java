package org.mycard.fragment;

import org.mycard.R;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class UserInfoTabFragment extends TabFragment {

	public class UserInfoFragmentPagerAdapter extends FragmentPagerAdapter {

		public UserInfoFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return new Fragment();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTabs.length;
		}

	}

	private String[] mTabs;

	@Override
	protected FragmentPagerAdapter initFragmentAdapter() {
		return new UserInfoFragmentPagerAdapter(getChildFragmentManager());
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mTabs = getResources().getStringArray(R.array.user_info);
		mTabCount = mTabs.length;
	}
	
	@Override
	protected void initTab() {
		super.initTab();
		int i = 0;
		for (String title : mTabs) {
			addTab(i++, title, mTabs.length);
		}
	}

}
