package org.mycard.model.data;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfo extends BaseInfo {
	
	public String name;
	public int playerID;
	public boolean certified;
	
	@Override
	protected UserInfo clone() {
		return (UserInfo)super.clone();
	}
	
	@Override
	public void initFromJsonData(JSONObject data) throws JSONException {
		super.initFromJsonData(data);
		name = data.getString(JSON_KEY_NAME);
		playerID = data.getInt(JSON_KEY_USER_PLAYER_ID);
		certified = data.getBoolean(JSON_KEY_USER_CERTIFIED);
	}

}
