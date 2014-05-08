package org.mycard.model.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mycard.model.data.wrapper.BaseDataWrapper;
import org.mycard.model.data.wrapper.RoomDataWrapper;
import org.mycard.model.data.wrapper.ServerDataWrapper;
import org.mycard.ygo.YGORoomInfo;
import org.mycard.ygo.YGOServerInfo;


public class DataStore {
	private List<YGOServerInfo> mServers;
	private Map<String, YGORoomInfo> mRooms;
	
	public DataStore() {
		mServers = new ArrayList<YGOServerInfo>();
		mRooms = new HashMap<String, YGORoomInfo>();
	}

	public synchronized void updateData(BaseDataWrapper wrapper) {
		if (wrapper instanceof ServerDataWrapper) {
			mServers.clear();
			int size = ((ServerDataWrapper) wrapper).size();
			for (int i = 0; i < size; i++) {
				mServers.add(i,
						(YGOServerInfo) ((ServerDataWrapper) wrapper).getItem(i));
			}
		} else if (wrapper instanceof RoomDataWrapper) {
			int size = ((RoomDataWrapper) wrapper).size();
			for (int i = 0; i < size; i++) {
				YGORoomInfo info = (YGORoomInfo) ((RoomDataWrapper) wrapper).getItem(i);
				if (info.deleted) {
					mRooms.remove(info.id);
				} else {
					mRooms.put(info.id, info);
				}
			}
		}
	}

	public synchronized List<YGOServerInfo> getServerList() {
		List<YGOServerInfo> servers = new ArrayList<YGOServerInfo>();
		for (YGOServerInfo info : mServers) {
			servers.add(info.clone());
		}
		//try to set default server addr
		if (servers.size() == 0) {
			servers.add(new YGOServerInfo(ResourcesConstants.DEFAULT_MC_SERVER_ADDR, ResourcesConstants.DEFAULT_MC_SERVER_PORT));
		}
		return servers;
	}
	
	public synchronized List<YGORoomInfo> getRooms() {
		List<YGORoomInfo> rooms = new ArrayList<YGORoomInfo>();
		for (YGORoomInfo info : mRooms.values()) {
			rooms.add(info.clone());
		}
		return rooms;
	}

}
