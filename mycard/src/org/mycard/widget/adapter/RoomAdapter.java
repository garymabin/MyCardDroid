package org.mycard.widget.adapter;

import java.util.LinkedList;
import java.util.List;

import org.mycard.R;
import org.mycard.model.data.RoomInfo;
import org.mycard.model.data.UserInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RoomAdapter extends BaseAdapter {

	public static class ViewHolder {
		public ImageView mLockImage;
		public ImageView mCustomImage;
		public TextView mTitle;
		public TextView mProperty;
		public TextView mStatus;
	}

	private List<RoomInfo> mDataList;
	private Context mContext;

	private int mFilter;

	public RoomAdapter(List<RoomInfo> lists, Context context, int filter) {
		// TODO Auto-generated constructor stub
		super();
		setData(lists);
		mContext = context;
		mFilter = filter;
	}

	public void setData(List<RoomInfo> lists) {
		// TODO Auto-generated method stub
		mDataList = new LinkedList<RoomInfo>();
		for (RoomInfo info : lists) {
			if (info.mode == mFilter) {
				if (!info.status) {
					((LinkedList<RoomInfo>)mDataList).addFirst(info.clone());
				} else {
					((LinkedList<RoomInfo>)mDataList).addLast(info.clone());
				}
			}
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.room_list_item, null);
			ViewHolder holder = new ViewHolder();
			holder.mTitle = (TextView) convertView
					.findViewById(R.id.item_list_name);
			holder.mLockImage = (ImageView) convertView
					.findViewById(R.id.item_flag_image);
			holder.mCustomImage = (ImageView) convertView
					.findViewById(R.id.item_list_icon);
			holder.mProperty = (TextView) convertView
					.findViewById(R.id.item_property_text);
			holder.mStatus = (TextView) convertView
					.findViewById(R.id.item_list_status);
			convertView.setTag(holder);
		}
		RoomInfo info = mDataList.get(position);
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.mCustomImage.setImageResource(R.drawable.logo);
		holder.mTitle.setText(info.name);
		holder.mProperty
				.setText(generatePropertyString(info));
		holder.mStatus.setText(info.status ? mContext.getString(R.string.ongoing)
				: mContext.getString(R.string.pending));
		if (info.privacy) {
			holder.mLockImage.setVisibility(View.VISIBLE);
		} else {
			holder.mLockImage.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	private String generatePropertyString(RoomInfo roomInfo) {
		StringBuilder builder = new StringBuilder();
		for (UserInfo userInfo : roomInfo.mUsers) {
			builder.append(userInfo.name + " | ");
		}
		if (builder.length() > 2) {
			builder.delete(builder.length() - 2, builder.length());
		}
		return builder.toString();
	}
}
