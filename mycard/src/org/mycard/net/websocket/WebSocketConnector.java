package org.mycard.net.websocket;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;
import org.mycard.core.WebSocketThread;
import org.mycard.core.WebSocketThread.MoeEventHandler;
import org.mycard.model.data.wrapper.BaseDataWrapper;
import org.mycard.model.data.wrapper.IBaseWrapper;

import android.os.Message;
import android.util.Log;

public class WebSocketConnector {
	private static final String TAG = "MoeSocketClient";

	private MoeEventHandler mHandler;

	private URI mURL;

	private WebSocketClient mClient;

	private BaseDataWrapper mWrapper;

	private StringBuilder mDataCache = new StringBuilder();

	public WebSocketConnector() {
		// TODO Auto-generated constructor stub
	}

	public void setHandler(MoeEventHandler handler) {
		// TODO Auto-generated method stub
		mHandler = handler;
	}

	public void connect(BaseDataWrapper wrapper) {
		mWrapper = wrapper;
		mURL = URI.create(wrapper.getUrl(0));
		mClient = new WebSocketClient(mURL) {
			@Override
			public void onOpen(ServerHandshake arg0) {
				Log.d(TAG, "opened connection");
			}

			@Override
			public void onMessage(String arg0) {
				Log.d(TAG, "received message from server: " + arg0);
				StringBuilder builder = new StringBuilder();
				mWrapper.parse(builder.append(arg0));
				mHandler.sendEmptyMessage(WebSocketThread.MSG_ID_DATA_UPDATE);
			}

			@Override
			public void onError(Exception arg0) {
				// TODO Auto-generated method stub
				arg0.printStackTrace();
				mHandler.sendMessage(Message.obtain(null,
						WebSocketThread.MSG_ID_CONNECTION_CLOSED, 0,
						IBaseWrapper.TASK_STATUS_FAILED));
			}

			@Override
			public void onClose(int arg0, String arg1, boolean arg2) {
				// TODO Auto-generated method stub
				Log.d(TAG, "connection closed by " + (arg2 ? "remote" : "self")
						+ " due to " + arg1);
				mHandler.sendMessage(Message.obtain(null,
						WebSocketThread.MSG_ID_CONNECTION_CLOSED, 0,
						IBaseWrapper.TASK_STATUS_FAILED));
			}

			@Override
			public void onFragment(Framedata frame) {
				super.onFragment(frame);
				mDataCache.append(new String(frame.getPayloadData().array()));
				if (!frame.isFin()) {
					return;
				} else {
					Log.d(TAG,
							"received fragment:"
									+ new String(mDataCache.toString()));
					mWrapper.parse(mDataCache);
					mHandler.sendEmptyMessage(WebSocketThread.MSG_ID_DATA_UPDATE);
					mDataCache.delete(0, mDataCache.length());
				}
			}
		};
		mClient.connect();
	}

	public void terminate() {
		mClient.close();
	}

}
