package org.mycard;

import org.apache.http.client.HttpClient;
import org.mycard.common.Constants;
import org.mycard.core.Controller;
import org.mycard.model.Model;
import org.mycard.net.http.ThreadSafeHttpClientFactory;

import android.app.Application;
import android.os.Environment;

public class StaticApplication extends Application {
	
	private ThreadSafeHttpClientFactory  mHttpFactory;
	
	private static StaticApplication INSTANCE;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		INSTANCE = this;
		mHttpFactory = new ThreadSafeHttpClientFactory(this);
		Controller.peekInstance();
		Model.peekInstance();
	}
	
	public HttpClient getHttpClient() {
		return mHttpFactory.getHttpClient();
	}
	
	public static StaticApplication peekInstance() {
		return INSTANCE;
	}

	public String getDefaultImageCacheRootPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.WORKING_DIRECTORY;
	}

}
