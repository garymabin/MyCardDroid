package org.mycard.fragment;

import org.mycard.R;
import org.mycard.StaticApplication;
import org.mycard.common.Constants;
import org.mycard.core.Controller;
import org.mycard.model.Model;
import org.mycard.model.data.DataStore;
import org.mycard.model.data.ResourcesConstants;
import org.mycard.widget.ServerOperationPanel;
import org.mycard.widget.ServerOperationPanel.ServerOperationListener;
import org.mycard.ygo.YGOServerInfo;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.SuperToast.OnClickListener;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;

import cn.garymb.ygodata.YGOGameOptions;
import cn.garymb.ygomobile.YGOMobileActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

public class ServerListFragment extends BaseFragment implements ServerOperationListener, OnGroupExpandListener, OnClickListener {

	public static class ServerAdapter extends BaseExpandableListAdapter {

		private SparseArray<YGOServerInfo> mServers;
		private LayoutInflater mInflater;
		private ServerOperationListener mListener;

		public ServerAdapter(LayoutInflater inflater,
				SparseArray<YGOServerInfo> servers,
				ServerOperationListener listener) {
			mServers = servers;
			mInflater = inflater;
			mListener = listener;
		}

		@Override
		public int getGroupCount() {
			return mServers.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		@Override
		public YGOServerInfo getGroup(int groupPosition) {
			return mServers.get(mServers.keyAt(groupPosition));
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return mServers.keyAt(groupPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.server_item, null);
			}
			YGOServerInfo info = getGroup(groupPosition);
			((TextView) convertView.findViewById(R.id.server_name))
					.setText(info.name);
			((TextView) convertView.findViewById(R.id.server_addr))
					.setText(info.ipAddrString);
			((TextView) convertView.findViewById(R.id.server_port))
					.setText(info.port + "");
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			View v = convertView;

			if (v == null) {
				v = mInflater.inflate(R.layout.server_ops_expand_view, parent,
						false);
				((ServerOperationPanel) v.findViewById(R.id.handle_panel))
						.setServerOperationListener(mListener);
			}
			((ServerOperationPanel) v.findViewById(R.id.handle_panel))
					.setGroupPosition(groupPosition);
			View editPanel = v.findViewById(R.id.edit_panel);
			long id = getGroupId(groupPosition);
			if (id < DataStore.MODIFIABLE_SERVER_INFO_START) {
				editPanel.setVisibility(View.GONE);
			} else {
				editPanel.setVisibility(View.VISIBLE);
			}
			View deletePanel = v.findViewById(R.id.delete_panel);
			if (id < DataStore.USER_DEFINE_SERVER_INFO_START) {
				deletePanel.setVisibility(View.GONE);
			} else {
				deletePanel.setVisibility(View.VISIBLE);
			}
			return v;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}
	
	private static final int REQUEST_CODE_SERVER = 0;

	private ExpandableListView mListView;

	private ServerAdapter mAdapter;

	private float mScreenWidth;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mScreenWidth = StaticApplication.peekInstance().getScreenWidth();
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mListView = (ExpandableListView) inflater.inflate(
				R.layout.common_expanable_list, null);
		mAdapter = new ServerAdapter(inflater, Model.peekInstance()
				.getServers(), this);
		mListView.setAdapter(mAdapter);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			mListView.setIndicatorBoundsRelative((int) (mScreenWidth - 80),
					(int) (mScreenWidth - 20));
		} else {
			mListView.setIndicatorBounds((int) (mScreenWidth - 80),
					(int) (mScreenWidth - 20));
		}
		mListView.setOnGroupExpandListener(this);
		return mListView;
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
			Bundle bundle = new Bundle();
			bundle.putInt(ResourcesConstants.MODE_OPTIONS,
					ResourcesConstants.DIALOG_MODE_ADD_NEW_SERVER);
			int index = (int) mAdapter.getGroupId(mAdapter.getGroupCount() - 1);
			bundle.putInt("index", ++index);
			showDialog(bundle, this, REQUEST_CODE_SERVER);
			break;
		case Constants.ACTION_BAR_EVENT_TYPE_PLAY:
			Intent intent = new Intent(getActivity(), YGOMobileActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void onEventFromChild(int requestCode, int eventType, int arg1,
			int arg2, Object data) {
		if (requestCode == REQUEST_CODE_SERVER) {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onOperation(int operationId, int position) {
		if (operationId == ServerOperationPanel.SERVER_OPERATION_CONNECT) {
			YGOServerInfo info = mAdapter.getGroup(position);
			YGOGameOptions options = new YGOGameOptions();
			options.mServerAddr = info.ipAddrString;
			options.mPort = info.port;
			options.mName = Controller.peekInstance().getLoginName();
			Intent intent = new Intent(getActivity(), YGOMobileActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent.putExtra(YGOGameOptions.YGO_GAME_OPTIONS_BUNDLE_KEY, options);
			startActivity(intent);
		} else if (operationId == ServerOperationPanel.SERVER_OPERATION_EDIT) {
			Bundle bundle = new Bundle();
			bundle.putInt(ResourcesConstants.MODE_OPTIONS,
					ResourcesConstants.DIALOG_MODE_EDIT_SERVER);
			int index = (int) mAdapter.getGroupId(position);
			bundle.putParcelable("server", mAdapter.getGroup(position));
			bundle.putInt("index", index);
			showDialog(bundle, this, REQUEST_CODE_SERVER);
		} else if (operationId == ServerOperationPanel.SERVER_OPERATION_DELETE) {
			YGOServerInfo info = mAdapter.getGroup(position);
			SuperActivityToast superActivityToast = new SuperActivityToast(mActivity, SuperToast.Type.BUTTON);
			superActivityToast.setDuration(SuperToast.Duration.EXTRA_LONG);
			superActivityToast.setText(getResources().getString(R.string.toast_delete, info.name));
			superActivityToast.setButtonIcon(SuperToast.Icon.Dark.UNDO, "UNDO");
			superActivityToast.setOnClickWrapper(new OnClickWrapper(info.id, this), info);
			superActivityToast.show();
			int index = (int)mAdapter.getGroupId(position);
			Model.peekInstance().removeServer(index);
			mAdapter.notifyDataSetChanged();
		}
		mListView.collapseGroup(position);
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		int len = mAdapter.getGroupCount();
		for (int i = 0; i < len; i++) {
			if (i != groupPosition) {
				mListView.collapseGroup(i);
			}
		}
	}

	@Override
	public void onClick(View view, Parcelable token) {
		Model.peekInstance().addNewServer((YGOServerInfo) token);
		mAdapter.notifyDataSetChanged();
	}
}
