package org.mycard.fragment;

import org.mycard.common.Constants;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;


public class ChatRoomFragment extends BaseFragment {
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity.onActionBarChange(Constants.ACTION_BAR_CHANGE_TYPE_PAGE_CHANGE,
				FRAGMENT_ID_CHAT_ROOM, null);
	}

	/* (non-Javadoc)
	 * @see android.os.Handler.Callback#handleMessage(android.os.Message)
	 */
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

}
