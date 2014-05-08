package org.mycard.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mycard.StaticApplication;
import org.mycard.model.data.DataStore;
import org.mycard.model.data.ImageItem;
import org.mycard.model.data.wrapper.BaseDataWrapper;
import org.mycard.ygo.YGOArrayStore;
import org.mycard.ygo.YGORoomInfo;
import org.mycard.ygo.YGOServerInfo;

import android.graphics.Bitmap;
import android.os.Message;


public class Model {
	
	private static Model INSTANCE;
	
	private DataStore mDataStore;
	
	private YGOArrayStore mYGOArrayStore;
	
	private List<YGOServerInfo> mServerList;
	
	private ImageModelHelper mImgModelHelper;
	
	private Set<IDataObserver> mObserverList;
	
	private Model(StaticApplication app) {
		mDataStore = new DataStore();
		mImgModelHelper = new ImageModelHelper();
		mServerList = new ArrayList<YGOServerInfo>();
		mYGOArrayStore = new YGOArrayStore(app.getResources());
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

	public List<YGOServerInfo> getServerList() {
		return mDataStore.getServerList();
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
	

}
