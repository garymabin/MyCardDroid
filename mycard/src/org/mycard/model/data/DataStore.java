package org.mycard.model.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.mycard.common.Constants;
import org.mycard.model.data.wrapper.BaseDataWrapper;
import org.mycard.model.data.wrapper.RoomDataWrapper;
import org.mycard.model.data.wrapper.ServerDataWrapper;
import org.mycard.net.download.DownloadTask;
import org.mycard.net.download.TaskList;
import org.mycard.ygo.YGORoomInfo;
import org.mycard.ygo.YGOServerInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;


public class DataStore extends Observable {
	
	private static final int MODIFIABLE_SERVER_INFO_START = 0x1000;
	private static final int MODIFIABLE_SERVER_CHECKMATE_SERVER = 0x1001;
	private static final int MODIFIABLE_SERVER_ADS_SERVER = 0x1002;
	private static final int USER_DEFINE_SERVER_INFO_START = 0x1005;
	
	private static final String DEFAULT_CHECKMATE_SERVER_NAME = "checkmate";
	private static final String DEFAULT_CHECKMATE_SERVER_ADDR = "173.224.211.158";
	private static final int DEFAULT_CHECKMATE_SERVER_PORT = 21001;
	
	private static final String DEFAULT_ADS_SERVER_ADDR = "172.19.0.6";
	private static final int DEFAULT_ADS_SERVER_PORT = 9911;
	
	private static final String TAG = "DataStore";
	
	private SparseArray<YGOServerInfo> mServers;
	private Map<String, YGORoomInfo> mRooms;
	
	private TaskList mTaskList;
	
	private Context mContext;
	
	
	public DataStore(Context context) {
		mContext = context;
		mServers = new SparseArray<YGOServerInfo>();
		LoadModifiableServers();
		mRooms = new HashMap<String, YGORoomInfo>();
		mTaskList = new TaskList(context);
		mTaskList.initializeTasks();
	}

	private void LoadModifiableServers() {
		SharedPreferences sp = mContext.getSharedPreferences(Constants.PREF_FILE_SERVER_LIST,
				Context.MODE_PRIVATE);
		//add checkmate server.
		addNewServer(sp, MODIFIABLE_SERVER_CHECKMATE_SERVER, DEFAULT_CHECKMATE_SERVER_NAME, DEFAULT_CHECKMATE_SERVER_ADDR, DEFAULT_CHECKMATE_SERVER_PORT);
		int size = sp.getInt(Constants.PREF_KEY_USER_DEF_SERVER_SIZE, 0);
		//add user define server.
		for (int i = 0; i < size; i ++) {
			addNewServer(sp, i + USER_DEFINE_SERVER_INFO_START, "", "", 0);
		}
	}

	private void addNewServer(SharedPreferences sp, int index, String defname, String defAddr, int defPort) {
		if (index < MODIFIABLE_SERVER_INFO_START) {
			Log.w(TAG, "can not add a server index less than 0x1000");
		}
		String server = sp.getString(Constants.PREF_KEY_SERVER_ADDR + index, defAddr);
		String name = sp.getString(Constants.PREF_KEY_SERVER_ADDR + index, defname);
		int port  = sp.getInt(Constants.PREF_KEY_SERVER_PORT + index, defPort);
		YGOServerInfo info = new YGOServerInfo(name, server, port);
		mServers.put(index, info);
	}

	public synchronized void updateData(BaseDataWrapper wrapper) {
		if (wrapper instanceof ServerDataWrapper) {
			int size = ((ServerDataWrapper) wrapper).size();
			for (int i = 0; i < size; i++) {
				mServers.put(i, (YGOServerInfo) ((ServerDataWrapper) wrapper).getItem(i));
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

	public synchronized YGOServerInfo getMyCardServer() {
		//try to set default server addr
		if (mServers.get(0) == null) {
			mServers.put(0, new YGOServerInfo(ResourcesConstants.DEFAULT_MC_SERVER_NAME,
					ResourcesConstants.DEFAULT_MC_SERVER_ADDR, ResourcesConstants.DEFAULT_MC_SERVER_PORT));
		}
		return mServers.get(0);
	}
	
	public synchronized SparseArray<YGOServerInfo> getServers() {
		return mServers;
	}
	
	public synchronized List<YGORoomInfo> getRooms() {
		List<YGORoomInfo> rooms = new ArrayList<YGORoomInfo>();
		for (YGORoomInfo info : mRooms.values()) {
			rooms.add(info.clone());
		}
		return rooms;
	}

	public TaskList getTaskList() {
		return mTaskList;
	}
	
	/**
	 * 获取特定任务
	 */
	public DownloadTask getTask(String packageName) {
		return mTaskList.getTaskItem(packageName);
	}
	
	/**
	 * 是否有下载任务正在进行
	 */
	public boolean hasTaskRunning() {
		return mTaskList.getRunningTaskCount() > 0;
	}

	public void reportDownloadEvent(int msgId, Object obj) {
		setChanged();
		Message msg = new Message();
		msg.what = msgId;
		msg.obj = obj;
		notifyObservers(msg);
	}
}
