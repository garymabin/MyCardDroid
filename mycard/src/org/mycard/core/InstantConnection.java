package org.mycard.core;

import org.apache.http.client.HttpClient;
import org.mycard.StaticApplication;
import org.mycard.model.data.wrapper.BaseDataWrapper;

public class InstantConnection implements IBaseConnection {
	protected IBaseThread mUpdateThread;
	
	private HttpClient mHttpClient;
	
	private TaskStatusCallback mCallBack;

	public InstantConnection(StaticApplication app,
			TaskStatusCallback callback) {
		mHttpClient = app.getHttpClient();
		mCallBack = callback;
	}
	

	@Override
	public void addTask(BaseDataWrapper wrapper) {
		if (mUpdateThread == null) {
			mUpdateThread = new ServerUpdateThread(mCallBack, mHttpClient);
			((ServerUpdateThread)mUpdateThread).setWrapper(wrapper);
			mUpdateThread.start();
		}
	}

	@Override
	public void purge() {
		mUpdateThread.terminate();
	}

}
