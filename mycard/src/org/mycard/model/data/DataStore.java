package org.mycard.model.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mycard.model.data.wrapper.BaseDataWrapper;
import org.mycard.model.data.wrapper.RoomDataWrapper;
import org.mycard.model.data.wrapper.ServerDataWrapper;


public class DataStore {
	private List<ServerInfo> mServers;
	private Map<String, RoomInfo> mRooms;
	
	public DataStore() {
		mServers = new ArrayList<ServerInfo>();
		mRooms = new HashMap<String, RoomInfo>();
	}

	public synchronized void updateData(BaseDataWrapper wrapper) {
		if (wrapper instanceof ServerDataWrapper) {
			mServers.clear();
			int size = ((ServerDataWrapper) wrapper).size();
			for (int i = 0; i < size; i++) {
				mServers.add(i,
						(ServerInfo) ((ServerDataWrapper) wrapper).getItem(i));
			}
		} else if (wrapper instanceof RoomDataWrapper) {
			int size = ((RoomDataWrapper) wrapper).size();
			for (int i = 0; i < size; i++) {
				RoomInfo info = (RoomInfo) ((RoomDataWrapper) wrapper).getItem(i);
				if (info.deleted) {
					mRooms.remove(info.id);
				} else {
					mRooms.put(info.id, info);
				}
			}
		}
	}

	public synchronized List<ServerInfo> getServerList() {
		List<ServerInfo> servers = new ArrayList<ServerInfo>();
		for (ServerInfo info : mServers) {
			servers.add(info.clone());
		}
		//try to set default server addr
		if (servers.size() == 0) {
			servers.add(new ServerInfo(ResourcesConstants.DEFAULT_MC_SERVER_ADDR, ResourcesConstants.DEFAULT_MC_SERVER_PORT));
		}
		return servers;
	}
	
	public synchronized List<RoomInfo> getRooms() {
		List<RoomInfo> rooms = new ArrayList<RoomInfo>();
		for (RoomInfo info : mRooms.values()) {
			rooms.add(info.clone());
		}
		return rooms;
	}

}
