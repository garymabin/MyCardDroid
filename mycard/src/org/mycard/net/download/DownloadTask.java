package org.mycard.net.download;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 下载任务
 * @author qiaozhi
 *
 */
public class DownloadTask implements TaskStatus, Serializable {

	public static final int SOURCE_NOKIA = 0;
	public static final int SOURCE_UPDATE = 1;
	
	private static final long serialVersionUID = -5652095125056203361L;
	
	private static final String NAME_PACKAGE_NAME = "package_name";
	private static final String NAME_STATUS = "status";
	private static final String NAME_TITLE = "title";
	private static final String NAME_ICON_DATA = "icon_path";
	private static final String NAME_TRANSFERED = "transfered";
	private static final String NAME_TOTAL = "total";
	private static final String NAME_TASK_URL = "task_url";
	private static final String NAME_LOCAL_PATH = "local_path";
	private static final String NAME_CREATE_TIME = "create_time";
	private static final String NAME_VERSION_CODE = "version_code";
	private static final String NAME_HAS_STAT = "has_stat";
	private static final String NAME_SOURCE = "source";
	private static final String NAME_SOFT_TYPE = "soft_type";
	
	public String mPackageName;
	public int mStatus;
	public String mTitle;
	public String mIconData;
	public long mTransfered;
	public long mTotal;
	public String mTaskUrl;
	public String mLocalPath;
	public long mCreateTime;
	public int mVersionCode;
	public boolean mHasStat;
	public int mSource;
	public String mSoftType;
	
	public static DownloadTask buildNewTask(String packageName, int source, String type) {
		DownloadTask task = new DownloadTask();
		task.mPackageName = packageName;
		task.mSource = source;
		task.mSoftType = type;
		return task;
	}
	
	public void readFromJSON(JSONObject jsonObj) throws JSONException {
		mPackageName = jsonObj.optString(NAME_PACKAGE_NAME);
		mStatus = jsonObj.optInt(NAME_STATUS, STATUS_UNKNOWN);
		mTitle = jsonObj.optString(NAME_TITLE);
		mIconData = jsonObj.optString(NAME_ICON_DATA);
		mTransfered = jsonObj.optLong(NAME_TRANSFERED);
		mTotal = jsonObj.optLong(NAME_TOTAL);
		mTaskUrl = jsonObj.optString(NAME_TASK_URL);
		mLocalPath = jsonObj.optString(NAME_LOCAL_PATH);
		mCreateTime = jsonObj.optLong(NAME_CREATE_TIME);
		mVersionCode = jsonObj.optInt(NAME_VERSION_CODE);
		mHasStat = jsonObj.optBoolean(NAME_HAS_STAT);
		mSource = jsonObj.optInt(NAME_SOURCE);
		mSoftType = jsonObj.optString(NAME_SOFT_TYPE);
	}

	public JSONObject generateJSONObject() throws JSONException {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(NAME_PACKAGE_NAME, mPackageName);
		jsonObj.put(NAME_STATUS, mStatus);
		jsonObj.put(NAME_TITLE, mTitle);
		jsonObj.put(NAME_ICON_DATA, mIconData);
		jsonObj.put(NAME_TRANSFERED, mTransfered);
		jsonObj.put(NAME_TOTAL, mTotal);
		jsonObj.put(NAME_TASK_URL, mTaskUrl);
		jsonObj.put(NAME_LOCAL_PATH, mLocalPath);
		jsonObj.put(NAME_CREATE_TIME, mCreateTime);
		jsonObj.put(NAME_VERSION_CODE, mVersionCode);
		jsonObj.put(NAME_HAS_STAT, mHasStat);
		jsonObj.put(NAME_SOURCE, mSource);
		jsonObj.put(NAME_SOFT_TYPE, mSoftType);
		return jsonObj;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof DownloadTask))
			return false;
		DownloadTask another = (DownloadTask) o;
		
		if (mPackageName == null) {
			return another.mPackageName == null;
		} else {
			return mPackageName.equals(another.mPackageName);
		}
	}

	@Override
	public int hashCode() {
		return mPackageName == null ? 0 : mPackageName.hashCode();
	}
	
}
