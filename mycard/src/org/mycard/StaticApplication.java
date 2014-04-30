package org.mycard;

import org.apache.http.client.HttpClient;
import org.mycard.common.Constants;
import org.mycard.core.Controller;
import org.mycard.model.Model;
import org.mycard.net.http.ThreadSafeHttpClientFactory;
import org.mycard.setting.Settings;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

public class StaticApplication extends Application {
	
	private ThreadSafeHttpClientFactory  mHttpFactory;
	
	private static StaticApplication INSTANCE;
	
	private SharedPreferences mSettingsPref;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		INSTANCE = this;
		mHttpFactory = new ThreadSafeHttpClientFactory(this);
		mSettingsPref = PreferenceManager.getDefaultSharedPreferences(this);
		Controller.peekInstance();
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
	
	public String getCardImagePath() {
		return mSettingsPref.getString(Settings.KEY_PREF_COMMON_CARD_PATH, getDefaultImageCacheRootPath());
	}

}
