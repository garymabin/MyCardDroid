package org.mycard.fragment;

import java.util.List;

import org.mycard.R;
import org.mycard.data.RoomInfo;
import org.mycard.widget.adapter.RoomAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RoomPageFragment extends BaseFragment implements OnItemClickListener {
	
	private static final String TAG = "RoomPageFragment";
	
	private ListView mContentView;
	private RoomAdapter mAdapter;
	private List<RoomInfo> mData;
	
	private boolean isDataBinded = false;
	
	public static RoomPageFragment newInstance(int index) {
		RoomPageFragment fragment = new RoomPageFragment();
		
		Bundle data = new Bundle();
		data.putInt("index", index);
		fragment.setArguments(data);
		return fragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onAttach: E");
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onCreateView: E");
		mContentView = (ListView) inflater.inflate(R.layout.common_list, null);
		mContentView.setOnItemClickListener(this);
		isDataBinded = false;
		return mContentView;
	}

	/* (non-Javadoc)
	 * @see android.os.Handler.Callback#handleMessage(android.os.Message)
	 */
	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}

	/**
	 * 
	 * @return
	**/
	/*package*/ void setData(List<RoomInfo> data) {
		mData = data;
		if (mAdapter == null) {
			mAdapter = new RoomAdapter(mData, mActivity, getArguments().getInt("index", 0));
		} else {
			mAdapter.setData(mData);
			mAdapter.notifyDataSetChanged();
		}
		if (mContentView != null && !isDataBinded) {
			Log.d(TAG, "bind view data with index " + getArguments().getInt("index", 0));
			mContentView.setAdapter(mAdapter);
			isDataBinded = true;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

}
