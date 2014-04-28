package org.mycard.core;

import java.util.concurrent.BlockingQueue;

import org.apache.http.client.HttpClient;
import org.mycard.core.IBaseConnection.TaskStatusCallback;
import org.mycard.model.data.wrapper.BaseDataWrapper;
import org.mycard.net.http.DataHttpConnector;

public class SingleUpdateThreadPool extends SingleHttpThreadPool {

	public SingleUpdateThreadPool(BlockingQueue<BaseDataWrapper> queue, TaskStatusCallback callback, HttpClient client) {
		super(queue, callback);
		// TODO Auto-generated constructor stub
		mConnector = new DataHttpConnector(client);
	}
}
