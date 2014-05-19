package org.mycard.core;


import org.apache.http.client.HttpClient;
import org.mycard.StaticApplication;
import org.mycard.common.Constants;
import org.mycard.model.data.wrapper.BaseDataWrapper;

import android.util.SparseArray;

public class InstantConnection implements IBaseConnection {
	protected SparseArray<IBaseThread> mWorkThreads;
	
	private HttpClient mHttpClient;
	
	private TaskStatusCallback mCallBack;

	public InstantConnection(StaticApplication app,
			TaskStatusCallback callback) {
		mHttpClient = app.getHttpClient();
		mCallBack = callback;
		mWorkThreads = new SparseArray<IBaseThread>();
	}
	

	@Override
	public void addTask(BaseDataWrapper wrapper) {
		int requestType = wrapper.getRequestType();
		IBaseThread thread = mWorkThreads.get(requestType);
		if (thread == null || !thread.isRunning()) {
			if (requestType == Constants.REQUEST_TYPE_UPDATE_SERVER) {
				thread = new ServerUpdateThread(mCallBack, mHttpClient); 
			} else if (requestType == Constants.REQUEST_TYPE_LOGIN) {
				thread = new LoginThread(mCallBack, mHttpClient);
			}
			mWorkThreads.put(requestType, thread);
			((InstantThread)thread).setWrapper(wrapper);
			thread.start();
		}
	}

	@Override
	public void purge() {
		for (int i = 0; i < mWorkThreads.size(); i++) {
			IBaseThread thread = mWorkThreads.valueAt(i);
			if (thread != null) {
				thread.terminate();
			}
		}
	}

}
