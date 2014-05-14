package org.mycard.core;

import org.apache.http.client.HttpClient;
import org.mycard.core.IBaseConnection.TaskStatusCallback;
import org.mycard.net.http.BaseHttpConnector;
import org.mycard.net.http.DataHttpConnector;

public class LoginThread extends InstantThread {

	public LoginThread(TaskStatusCallback callback, HttpClient client) {
		super(callback, client);
	}

	@Override
	protected BaseHttpConnector initConnector(HttpClient client) {
		return new DataHttpConnector(client);
	}
}
