package org.mycard.fragment;


public interface FragmentNavigationListener {
	
	public static final int FRAGMENT_NAVIGATION_BACK_EVENT = 0x0;

	void onEventFromChild(int requestCode, int eventType, int arg1, int arg2, Object data);
}
