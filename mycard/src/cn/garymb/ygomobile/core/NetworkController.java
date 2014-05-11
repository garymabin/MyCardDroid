/*
 * NetworkController.java
 * 
 *  Created on: 2014年3月28日
 * 		Author: mabin
 */
package cn.garymb.ygomobile.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * @author mabin
 * 
 */
public final class NetworkController {

	private Context mContext;
	private WifiManager mWM;
	private ConnectivityManager mCM;

	/**
	 * 
	 */
	public NetworkController(Context context) {
		mContext = context;
		init();
	}

	/**
	 * 
	 * @return
	 **/
	private void init() {
		mWM = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		mCM = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * 
	 * @author: mabin
	 * @return
	 **/
	public boolean isWifiConnected() {
		// TODO Auto-generated method stub
		boolean isWifiEnabled = mWM.isWifiEnabled();
		NetworkInfo ni = mCM.getActiveNetworkInfo();
		if (isWifiEnabled && null != ni && ni.isConnected()
				&& ni.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	public int getIPAddress() {
		if (!isWifiConnected()) {
			return -1;
		}
		WifiInfo wi = mWM.getConnectionInfo();
		return wi.getIpAddress();
	}

}
