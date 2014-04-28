package org.mycard.actionbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.mycard.R;
import org.mycard.common.Constants;

import android.os.Handler;
import android.view.MenuItem;

public class ActionBarController {
	
	private List<WeakReference<Handler>> mActionNewList;
	private List<WeakReference<Handler>> mActionFilterList;
	private List<WeakReference<Handler>> mActionSettingsList;
	private List<WeakReference<Handler>> mActionPlayList;
	private List<WeakReference<Handler>> mActionSearchList;
	
	public ActionBarController() {
		mActionNewList = new ArrayList<WeakReference<Handler>>();
		mActionSettingsList = new ArrayList<WeakReference<Handler>>();
		mActionPlayList = new ArrayList<WeakReference<Handler>>();
		mActionSearchList = new ArrayList<WeakReference<Handler>>();
		mActionFilterList = new ArrayList<WeakReference<Handler>>();
	}
	
	public boolean handleAction(MenuItem item) {
		boolean handled = true;
		switch (item.getItemId()) {
		case R.id.action_settings:
			notifyTarget(mActionSettingsList, Constants.ACTION_BAR_EVENT_TYPE_SETTINGS);
			break;
		case R.id.action_new:
			notifyTarget(mActionNewList, Constants.ACTION_BAR_EVENT_TYPE_NEW);
			break;
		case R.id.action_play:
			notifyTarget(mActionPlayList, Constants.ACTION_BAR_EVENT_TYPE_PLAY);
			break;
		case R.id.action_search:
			notifyTarget(mActionSearchList, Constants.ACTION_BAR_EVENT_TYPE_SEARCH);
			break;
		case R.id.action_filter:
			notifyTarget(mActionFilterList, Constants.ACTION_BAR_EVENT_TYPE_FILTER);
			break;
		default:
			handled = false;
			break;
		}
		return handled;
	}
	
	private void notifyTarget(List<WeakReference<Handler>> list, int msgType) {
		for (WeakReference<Handler> item : list) {
			Handler h = item.get();
			if (h != null) {
				h.sendEmptyMessage(msgType);
			}
		}
	}
	
	public void registerForActionNew(Handler h) {
		WeakReference<Handler> ref = new WeakReference<Handler>(h);
		mActionNewList.add(ref);
	}
	
	public void registerForActionSearch(Handler h) {
		WeakReference<Handler> ref = new WeakReference<Handler>(h);
		mActionSearchList.add(ref);
	}
	
	public void unregisterForActionSearch(Handler h) {
		for (WeakReference<Handler> item : mActionSearchList) {
			if (h == item.get()) {
				mActionSearchList.remove(item);
				item = null;
				break;
			}
		}
	}

	
	public void registerForActionFilter(Handler h) {
		WeakReference<Handler> ref = new WeakReference<Handler>(h);
		mActionFilterList.add(ref);
	}
	
	public void unregisterForActionFilter(Handler h) {
		for (WeakReference<Handler> item : mActionFilterList) {
			if (h == item.get()) {
				mActionFilterList.remove(item);
				item = null;
				break;
			}
		}
	}
	
	public void unregisterForActionNew(Handler h) {
		for (WeakReference<Handler> item : mActionNewList) {
			if (h == item.get()) {
				mActionNewList.remove(item);
				item = null;
				break;
			}
		}
	}
	
	public void registerForActionPlay(Handler h) {
		WeakReference<Handler> ref = new WeakReference<Handler>(h);
		mActionPlayList.add(ref);
	}
	
	public void unregisterForActionPlay(Handler h) {
		for (WeakReference<Handler> item : mActionPlayList) {
			if (h == item.get()) {
				mActionPlayList.remove(item);
				item = null;
				break;
			}
		}
	}
	

}
