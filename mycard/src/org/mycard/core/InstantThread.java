package org.mycard.core;

import org.apache.http.client.HttpClient;
import org.mycard.core.IBaseConnection.TaskStatusCallback;
import org.mycard.model.data.wrapper.BaseDataWrapper;
import org.mycard.model.data.wrapper.IBaseWrapper;
import org.mycard.net.http.BaseHttpConnector;

public abstract class InstantThread extends BaseThread {

	private BaseHttpConnector mConnector;

	protected BaseDataWrapper mWrapper;

	public InstantThread(TaskStatusCallback callback, HttpClient client) {
		super(callback);
		mConnector = initConnector(client);
	}
	
	protected abstract BaseHttpConnector initConnector(HttpClient client);

	/* package */ void setWrapper(BaseDataWrapper wrapper) {
		mWrapper = wrapper;
	}

	@Override
	public void run() {
		if (isRunning) {
			try {
				if (mWrapper != null) {
					mConnector.get(mWrapper);
					mCallback.onTaskFinish(mWrapper);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				mWrapper.setResult(IBaseWrapper.TASK_STATUS_CANCELED);
				mCallback.onTaskFinish(mWrapper);
			}
		}
	}

}
