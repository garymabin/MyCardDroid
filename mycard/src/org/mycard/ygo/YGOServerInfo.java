package org.mycard.ygo;

import org.json.JSONException;
import org.json.JSONObject;
import org.mycard.model.data.BaseInfo;

public class YGOServerInfo extends BaseInfo {
	
	public YGOServerInfo() {
	}
	
	public YGOServerInfo(String ip, int port) {
		ipAddrString = ip;
		this.port = port;
	}
	
	public String name;
	public String ipAddrString;
	public int port;
	public boolean auth;
	public int maxRooms;
	public String urlIndex;
	public String serverType;
	
	@Override
	public YGOServerInfo clone() {
		return (YGOServerInfo)super.clone();
	}
	
	@Override
	public void initFromJsonData(JSONObject data) throws JSONException {
		super.initFromJsonData(data);
		name = data.getString(JSON_KEY_NAME);
		ipAddrString = data.getString(JSON_KEY_SERVER_IP_ADDR);
		port = data.getInt(JSON_KEY_SERVER_PORT);
		auth = data.getBoolean(JSON_KEY_SERVER_AUTH);
		maxRooms = data.getInt(JSON_KEY_SERVER_MAX_ROOMS);
		urlIndex = data.getString(JSON_KEY_SERVER_INDEX_URL);
		serverType = data.getString(JSON_KEY_SERVER_TYPE);
	}

}
