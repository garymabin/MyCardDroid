package org.mycard.core;

import java.io.InputStream;

import org.mycard.model.data.wrapper.BaseDataWrapper;
import org.apache.http.client.HttpClient;
import org.mycard.core.IBaseConnection.TaskStatusCallback;
import org.mycard.net.http.BaseHttpConnector;

public class LoginThread extends InstantThread {

	public LoginThread(TaskStatusCallback callback, HttpClient client) {
		super(callback, client);
	}

	@Override
	protected BaseHttpConnector initConnector(HttpClient client) {
		return new BaseHttpConnector(client) {
			
			@Override
			protected void handleResponse(InputStream data, BaseDataWrapper wrapper)
					throws InterruptedException {
				handleResponse(data, wrapper);
			}
		};
	}
	
	protected void handleResult(InputStream data, BaseDataWrapper wrapper) {
		
	}

}
