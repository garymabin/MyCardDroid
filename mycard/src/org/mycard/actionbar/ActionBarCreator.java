package org.mycard.actionbar;

import org.mycard.R;

import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ActionBarCreator {
	
	private Context mContext;

	public ActionBarCreator(ActionBarActivity activity) {
		mContext = activity;
	}

	private boolean mLoading = false;

	private boolean mSearch = false;

	private boolean mRoomCreate = false;

	private boolean mSettings = true;

	private boolean mPlay = false;

	private boolean mPersonalCenter = true;

	private boolean mFilter = false;

	private boolean mSupport = true;
	
	private boolean mReset = false;
	
	private int mSearchResId;
	
	public ActionBarCreator setPersonalCenter(boolean userStatus) {
		mPersonalCenter = userStatus;
		return this;
	}

	public ActionBarCreator setSettings(boolean settings) {
		mSearch = true;
		return this;
	}

	public ActionBarCreator setLoading(boolean loading) {
		mLoading = loading;
		return this;
	}

	public ActionBarCreator setSearch(boolean search, int resID) {
		mSearch = search;
		mSearchResId = resID;
		return this;
	}

	public ActionBarCreator setRoomCreate(boolean roomCreate) {
		mRoomCreate = roomCreate;
		return this;
	}

	public ActionBarCreator setPlay(boolean play) {
		mPlay = play;
		return this;
	}

	public ActionBarCreator setFilter(boolean filter) {
		mFilter = filter;
		return this;
	}
	
	public ActionBarCreator setReset(boolean reset) {
		mReset = reset;
		return this;
	}
	
	public boolean isFilterEnabled() {
		return mFilter;
	}

	public void createMenu(final Menu menu) {
		int index = 0;
		menu.removeGroup(Menu.NONE);
		if (mPersonalCenter) {
			MenuItem useritem = menu.add(Menu.NONE,
					R.id.action_personal_center, index++,
					R.string.personal_center);
			MenuItemCompat.setShowAsAction(useritem,
					MenuItemCompat.SHOW_AS_ACTION_NEVER);
		}
		if (mSettings) {
			MenuItem item = menu.add(Menu.NONE, R.id.action_settings, index++,
					R.string.action_settings);
			MenuItemCompat.setShowAsAction(item,
					MenuItemCompat.SHOW_AS_ACTION_NEVER);
		}
		
		if (mSupport) {
			MenuItem item = menu.add(Menu.NONE, R.id.action_support, index++,
					R.string.action_support);
			MenuItemCompat.setShowAsAction(item,
					MenuItemCompat.SHOW_AS_ACTION_NEVER);
		}
		
		if (mReset) {
			MenuItem item = menu.add(Menu.NONE, R.id.action_reset, index++, R.string.action_reset)
					.setIcon(R.drawable.ic_action_reset);
			MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		}

		if (mFilter) {
			MenuItem item = menu.add(Menu.NONE, R.id.action_filter, index++,
					R.string.action_filter)
					.setIcon(R.drawable.ic_action_empty_filter);
			MenuItemCompat
					.setShowAsAction(
							item,
							MenuItemCompat.SHOW_AS_ACTION_ALWAYS
									| MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		}
		if (mLoading) {
			MenuItem item = menu.add(Menu.NONE, R.id.action_loading, index++,
					"");
			MenuItemCompat.setActionView(item,
					R.layout.actionbar_loading_progress);
			MenuItemCompat
					.setShowAsAction(
							item,
							MenuItemCompat.SHOW_AS_ACTION_ALWAYS
									| MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
			MenuItemCompat.expandActionView(item);
		}
		if (mPlay) {
			MenuItem item = menu.add(Menu.NONE, R.id.action_play, index++,
					mContext.getResources().getString(R.string.action_play))
					.setIcon(R.drawable.ic_action_play);
			MenuItemCompat.setShowAsAction(item,
					MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		}
		if (mSearch) {
			MenuItem item = menu.add(Menu.NONE, R.id.action_search, index++,
					mContext.getResources().getString(R.string.action_search))
					.setIcon(R.drawable.ic_action_search);
			MenuItemCompat
					.setShowAsAction(
							item,
							MenuItemCompat.SHOW_AS_ACTION_ALWAYS
									| MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
			MenuItemCompat.setActionView(item, mSearchResId);
		}
		if (mRoomCreate) {
			MenuItem item = menu
					.add(Menu.NONE,
							R.id.action_new,
							index++,
							mContext.getResources().getString(
									R.string.action_new_room)).setIcon(
							R.drawable.ic_action_new);
			MenuItemCompat.setShowAsAction(item,
					MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		}
	}
}
