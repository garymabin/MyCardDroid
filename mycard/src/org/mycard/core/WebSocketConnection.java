package org.mycard.core;

import org.mycard.model.data.wrapper.BaseDataWrapper;
import org.mycard.net.websocket.WebSocketConnector;

public class WebSocketConnection implements IBaseConnection {
	
	private IBaseThread mUpdateThread;
	private WebSocketConnector mConnector;
	
	public WebSocketConnection(TaskStatusCallback callback) {
		mConnector = new WebSocketConnector();
		mUpdateThread = new MiscUpdateThread(callback, mConnector);
		mUpdateThread.start();
	}

	@Override
	public void addTask(BaseDataWrapper wrapper) {
		// TODO Auto-generated method stub
		((WebSocketThread)mUpdateThread).executeTask(wrapper);
	}

	@Override
	public void purge() {
		// TODO Auto-generated method stub
		mUpdateThread.terminate();
	}

}
