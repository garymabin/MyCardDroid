package org.mycard.model.data.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mycard.model.data.BaseInfo;
import org.mycard.model.data.RoomInfo;

public class RoomDataWrapper extends BaseDataWrapper {

	private List<JSONObject> mData;

	public RoomDataWrapper(int requestType) {
		super(requestType);
		mData = new ArrayList<JSONObject>();
		mUrls.add(ROOM_LIST_URL);
	}

	@Override
	public void parse(StringBuilder out) {
		try {
			JSONArray array = new JSONArray(out.toString());
			for (int i = 0; i < out.length(); i++) {
				mData.add(array.getJSONObject(i));
			}
		} catch (JSONException e) {
		}
	}

	public int size() {
		return mData.size();
	}

	public BaseInfo getItem(int index) {
		BaseInfo info = new RoomInfo();
		try {
			info.initFromJsonData(mData.get(index));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return info;
	}

}
