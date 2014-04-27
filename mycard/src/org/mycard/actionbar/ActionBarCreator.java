package org.mycard.actionbar;

import java.lang.ref.WeakReference;

import org.mycard.R;

import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class ActionBarCreator {

	private Context mContext;

	public ActionBarCreator(Context context) {
		mContext = context;
	}
	
	private boolean mLoading = false;

	private boolean mSearch = false;

	private boolean mRoomCreate = false;

	private boolean mSettings = true;
	
	private WeakReference<View> mFilterView;

	private boolean mPlay = false;
	
	private boolean mPersonalCenter = true;
	
	private boolean mFilter = false;
	
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

	public ActionBarCreator setSearch(boolean search) {
		mSearch = search;
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
	
	public ActionBarCreator setFilter(boolean filter, View filterView) {
		mFilter = filter;
		mFilterView = new WeakReference<View>(filterView);
		return this;
	}
	
	public boolean isFilterEnabled() {
		return mFilter;
	}

	public void createMenu(final Menu menu) {
		int index = 0;
		menu.removeGroup(Menu.NONE);
		if (mPersonalCenter) {
			MenuItem useritem = menu.add(Menu.NONE, R.id.action_personal_center, index++, R.string.personal_center);
			MenuItemCompat.setShowAsAction(useritem, MenuItemCompat.SHOW_AS_ACTION_NEVER);
		}
		if (mSettings) {
			MenuItem item = menu.add(Menu.NONE, R.id.action_settings, index++,
					R.string.action_settings);
			MenuItemCompat.setShowAsAction(item,
					MenuItemCompat.SHOW_AS_ACTION_NEVER);
		}
		
		if (mFilter) {
			MenuItem item = menu.add(Menu.NONE, R.id.action_filter, index++,
					mContext.getResources().getString(R.string.action_filter))
					.setIcon(R.drawable.ic_action_empty_filter);
			MenuItemCompat.setShowAsAction(item,
					MenuItemCompat.SHOW_AS_ACTION_ALWAYS
					| MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
//			MenuItemCompat.setActionView(item, mFilterView.get());
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
			MenuItemCompat.setActionView(item, R.layout.custom_actionbar_searchview);
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
