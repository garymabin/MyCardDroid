package org.mycard.ygo;

import org.json.JSONException;
import org.json.JSONObject;
import org.mycard.model.data.BaseInfo;

public class YGOUserInfo extends BaseInfo {
	
	public String name;
	public int playerID;
	public boolean certified;
	
	@Override
	protected YGOUserInfo clone() {
		return (YGOUserInfo)super.clone();
	}
	
	@Override
	public void initFromJsonData(JSONObject data) throws JSONException {
		super.initFromJsonData(data);
		name = data.getString(JSON_KEY_NAME);
		playerID = data.getInt(JSON_KEY_USER_PLAYER_ID);
		certified = data.getBoolean(JSON_KEY_USER_CERTIFIED);
	}

}
