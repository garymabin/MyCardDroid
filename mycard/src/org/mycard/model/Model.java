package org.mycard.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.mycard.StaticApplication;
import org.mycard.model.data.DataStore;
import org.mycard.model.data.ImageItem;
import org.mycard.model.data.RoomInfo;
import org.mycard.model.data.ServerInfo;
import org.mycard.model.data.wrapper.BaseDataWrapper;

import android.graphics.Bitmap;
import android.os.Message;


public class Model {
	
	private static Model INSTANCE;
	
	private DataStore mDataStore;
	
	private List<ServerInfo> mServerList;
	
	private ImageModelHelper mImgModelHelper;
	
	private HashSet<IDataObserver> mObserverList;
	
	private Model(StaticApplication app) {
		mDataStore = new DataStore();
		mImgModelHelper = new ImageModelHelper();
		mServerList = new ArrayList<ServerInfo>();
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

	public List<ServerInfo> getServerList() {
		return mDataStore.getServerList();
	}

	public List<RoomInfo> getRooms() {
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

	public void requestDataOperation(IDataObserver observer, Message msg) {
		mImgModelHelper.requestDataOperation(observer, msg);
	}
	

}
