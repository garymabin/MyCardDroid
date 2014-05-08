package org.mycard.fragment;

import android.os.Bundle;

public interface FragmentNavigationListener {
	
	public static final int FRAGMENT_NAVIGATION_BACK_EVENT = 0x0;

	void onEventFromChild(int requestCode, int eventType, Bundle data);
}
