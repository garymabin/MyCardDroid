package org.mycard.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mycard.StaticApplication;
import org.mycard.model.data.DataStore;
import org.mycard.model.data.ImageItem;
import org.mycard.model.data.wrapper.BaseDataWrapper;
import org.mycard.net.download.DownloadTask;
import org.mycard.net.download.TaskList;
import org.mycard.ygo.YGOArrayStore;
import org.mycard.ygo.YGORoomInfo;
import org.mycard.ygo.YGOServerInfo;

import android.graphics.Bitmap;
import android.os.Message;
import android.util.SparseArray;


public class Model {
	
	private static Model INSTANCE;
	
	private DataStore mDataStore;
	
	private YGOArrayStore mYGOArrayStore;
	
	private ImageModelHelper mImgModelHelper;
	
	private Set<IDataObserver> mObserverList;
	
	private Model(StaticApplication app) {
		mDataStore = new DataStore(app);
		mImgModelHelper = new ImageModelHelper();
		mYGOArrayStore = new YGOArrayStore(app.getResources());
		mObserverList = new HashSet<IDataObserver>();
	}

	public static Model peekInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Model(StaticApplication.peekInstance());
		}
		return INSTANCE;
		
	}

	public void updateData(BaseDataWrapper wrapper) {
		mDataStore.updateData(wrapper);
	}

	public YGOServerInfo getMyCardServer() {
		return mDataStore.getMyCardServer();
	}
	
	public SparseArray<YGOServerInfo> getServers() {
		return mDataStore.getServers();
	}

	public List<YGORoomInfo> getRooms() {
		return mDataStore.getRooms();
	}
	
	/*package*/ boolean hasDataObserver(IDataObserver ob) {
		if (mObserverList == null)
			return false;
		
		synchronized (mObserverList) {
			return mObserverList.contains(ob);
		}
	}
	
	public void registerDataObserver(IDataObserver o) {
		synchronized (mObserverList) {
			mObserverList.add(o);
		}
	}

	public void unregisterDataObserver(IDataObserver o) {
		synchronized (mObserverList) {
			mObserverList.remove(o);
			mImgModelHelper.onDataObserverUnregistered(o);
		}
	}
	
	public Bitmap getBitmap(ImageItem item, int type) {
		return mImgModelHelper.getBitmap(item, type);
	}
	
	public String getYGOCardType(int code) {
		return mYGOArrayStore.getCardType(code);
	}
	
	public String getYGOCardRace(int code) {
		return mYGOArrayStore.getCardRace(code);
	}
	
	public String getYGOCardAttr(int code) {
		return mYGOArrayStore.getCardAttr(code);
	}
	
	public String getYGOCardOT(int code) {
		return mYGOArrayStore.getCardOT(code);
	}

	public void requestDataOperation(IDataObserver observer, Message msg) {
		mImgModelHelper.requestDataOperation(observer, msg);
	}

	public void reportDownloadEvent(int eventId, List<DownloadTask> taskList) {
		mDataStore.reportDownloadEvent(eventId, taskList);
	}

	public void reportDownloadEvent(int eventId,
			DownloadTask task) {
		mDataStore.reportDownloadEvent(eventId, task);
	}

	public TaskList getTaskList() {
		return mDataStore.getTaskList();
	}

}
