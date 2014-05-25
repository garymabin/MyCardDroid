package org.mycard.fragment;

import org.mycard.R;
import org.mycard.common.Constants;
import org.mycard.core.Controller;
import org.mycard.model.Model;
import org.mycard.ygo.YGOServerInfo;

import cn.garymb.ygodata.YGOGameOptions;
import cn.garymb.ygomobile.YGOMobileActivity;

import android.widget.BaseAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ServerListFragment extends BaseFragment implements OnItemClickListener {
	
	public static class ServerAdapter extends BaseAdapter {
		
		private SparseArray<YGOServerInfo> mServers;
		private LayoutInflater mInflater;
		
		public ServerAdapter(LayoutInflater inflater, SparseArray<YGOServerInfo> servers) {
			mServers = servers;
			mInflater = inflater;
		}

		@Override
		public int getCount() {
			return mServers.size();
		}

		@Override
		public YGOServerInfo getItem(int position) {
			return mServers.get(mServers.keyAt(position));
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.server_item, null);
			}
			YGOServerInfo info = getItem(position);
			((TextView) convertView.findViewById(R.id.server_name)).setText(info.name);
			((TextView) convertView.findViewById(R.id.server_addr)).setText(info.ipAddrString);
			((TextView) convertView.findViewById(R.id.server_port)).setText(info.port + "");
			return convertView;
		}

	}

	private ListView mListView;
	
	private ServerAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.server_list, null);
		mListView = (ListView) view.findViewById(R.id.server_list);
		mAdapter = new ServerAdapter(inflater, Model.peekInstance().getServers());
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Controller.peekInstance().registerForActionNew(mHandler);
		Controller.peekInstance().registerForActionPlay(mHandler);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Controller.peekInstance().unregisterForActionNew(mHandler);
		Controller.peekInstance().unregisterForActionPlay(mHandler);
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case Constants.ACTION_BAR_EVENT_TYPE_NEW:
			break;
		case Constants.ACTION_BAR_EVENT_TYPE_PLAY: {
				Intent intent = new Intent(getActivity(), YGOMobileActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
		return false;
	}
}
