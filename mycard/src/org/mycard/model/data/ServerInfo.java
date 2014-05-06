package org.mycard.model.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerInfo extends BaseInfo {
	
	public ServerInfo() {
	}
	
	public ServerInfo(String ip, int port) {
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
	protected ServerInfo clone() {
		return (ServerInfo)super.clone();
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
