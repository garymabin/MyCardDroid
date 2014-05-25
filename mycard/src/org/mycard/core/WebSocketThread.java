package org.mycard.core;

import org.mycard.core.IBaseConnection.TaskStatusCallback;
import org.mycard.model.data.wrapper.BaseDataWrapper;
import org.mycard.model.data.wrapper.IBaseWrapper;
import org.mycard.net.websocket.WebSocketConnector;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class WebSocketThread extends HandlerThread implements IBaseThread,
		Handler.Callback {

	public static final int MSG_ID_DATA_UPDATE = 0;
	public static final int MSG_ID_CONNECTION_CLOSED = 1;

	public static class MoeEventHandler extends Handler {
		public MoeEventHandler(Looper lopper, Callback callback) {
			super(lopper, callback);
		}

	}

	private static final String TAG = "MoeThread";

	private TaskStatusCallback mCallback;

	private BaseDataWrapper mWrapper;

	private WebSocketConnector mConnector;

	private MoeEventHandler mHandler;

	private volatile boolean isTerminateRequest = false;

	private static Object sLooperLock = new Object();
	private volatile boolean isLooperPrepared = false;

	public WebSocketThread(TaskStatusCallback callback,
			WebSocketConnector connector) {
		super(TAG);
		mCallback = callback;
		mConnector = connector;
	}

	@Override
	protected void onLooperPrepared() {
		super.onLooperPrepared();
		mHandler = new MoeEventHandler(getLooper(), this);
		mConnector.setHandler(mHandler);
		synchronized (sLooperLock) {
			sLooperLock.notifyAll();
			isLooperPrepared = true;
		}

	}

	public void executeTask(BaseDataWrapper wrapper) {
		if (!isLooperPrepared) {
			synchronized (sLooperLock) {
				try {
					sLooperLock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		mWrapper = wrapper;
		isTerminateRequest = false;
		mConnector.connect(wrapper);
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case MSG_ID_DATA_UPDATE:
			mWrapper.setResult(msg.arg2);
			mCallback.onTaskContinue(mWrapper);
			break;
		case MSG_ID_CONNECTION_CLOSED:
			mWrapper.setResult(isTerminateRequest ? IBaseWrapper.TASK_STATUS_CANCELED
					: msg.arg2);
			mCallback.onTaskFinish(mWrapper);
		default:
			break;
		}
		return false;
	}

	@Override
	public void terminate() {
		isTerminateRequest = true;
		mConnector.terminate();
	}

	@Override
	public boolean isRunning() {
		return !isTerminateRequest;
	}
}
