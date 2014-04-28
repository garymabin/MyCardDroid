package org.mycard.model.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RoomInfo extends BaseInfo {
	
	public String name;
	public boolean status;
	public int serverId;
	
	public List<UserInfo> mUsers = new ArrayList<UserInfo>();
	public int mode = 0;
	public int rule = -1;
	public boolean privacy = false;
	public int startLp = -1;
	public int startHand = -1;
	public int drawCount = -1;
	public boolean enablePriority = false;
	public boolean noDeckCheck = false;
	public boolean noDeckShuffle = false;
	
	public boolean deleted = false;
	
	private boolean isCompleteInfo;
	
	
	@Override
	public void initFromJsonData(JSONObject data) throws JSONException {
		super.initFromJsonData(data);
		name = data.getString(JSON_KEY_NAME);
		status = GAME_STATUS_START.equals(data.getString(JSON_KEY_ROOM_STATUS));
		serverId = data.getInt(JSON_KEY_ROOM_SERVER_ID);
		JSONArray usersArray = data.getJSONArray(JSON_KEY_ROOM_USERS);
		for (int i = 0; i < usersArray.length(); i ++) {
			UserInfo info = new UserInfo();
			info.initFromJsonData(usersArray.getJSONObject(i));
			mUsers.add(info);
		}
		if (data.has(JSON_KEY_ROOM_MODE)) {
			mode = data.getInt(JSON_KEY_ROOM_MODE);
		}
		if (data.has(JSON_KEY_ROOM_PRIVACY)) {
			privacy = data.getBoolean(JSON_KEY_ROOM_PRIVACY);
		}
		if (data.has(JSON_KEY_ROOM_RULE)) {
			rule = data.getInt(JSON_KEY_ROOM_RULE);
			isCompleteInfo = true;
		}
		if (data.has(JSON_KEY_ROOM_START_LP)) {
			startLp = data.getInt(JSON_KEY_ROOM_START_LP);
			isCompleteInfo = true;
		}
		if (data.has(JSON_KEY_ROOM_START_HAND)) {
			startHand = data.getInt(JSON_KEY_ROOM_START_HAND);
			isCompleteInfo = true;
		}
		if (data.has(JSON_KEY_ROOM_DRAW_COUNT)) {
			drawCount = data.getInt(JSON_KEY_ROOM_DRAW_COUNT);
			isCompleteInfo = true;
		}
		if (data.has(JSON_KEY_ROOM_ENABLE_PRIORITY)) {
			enablePriority = data.getBoolean(JSON_KEY_ROOM_ENABLE_PRIORITY);
			isCompleteInfo = true;
		}
		if (data.has(JSON_KEY_ROOM_NO_CHECK_DECK)) {
			noDeckCheck = data.getBoolean(JSON_KEY_ROOM_NO_CHECK_DECK);
			isCompleteInfo = true;
		}
		if (data.has(JSON_KEY_ROOM_NO_SHUFFLE_DECK)) {
			noDeckShuffle = data.getBoolean(JSON_KEY_ROOM_NO_SHUFFLE_DECK);
			isCompleteInfo = true;
		}
		if (data.has(JSON_KEY_ROOM_DELETED)) {
			deleted = data.getBoolean(JSON_KEY_ROOM_DELETED);
		}
	}
	
	public boolean isCompleteInfo() {
		return isCompleteInfo;
	}
	
	@Override
	public RoomInfo clone() {
		RoomInfo info = (RoomInfo)super.clone();
		info.mUsers = new ArrayList<UserInfo>();
		for (UserInfo item : mUsers) {
			info.mUsers.add(item.clone());
		}
		return info;
	}

}
