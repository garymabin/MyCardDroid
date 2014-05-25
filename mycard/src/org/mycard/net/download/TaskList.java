package org.mycard.net.download;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.mycard.common.Constants;
import org.mycard.utils.DeviceUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.support.v4.util.SparseArrayCompat;
import android.text.TextUtils;

/**
 * 下载管理队列，包括队列的相关操作
 * 
 * <p>队列使用SharedPreference进行持久化的存储<br>
 * 存储格式：<br>
 * Key:packageName<br>
 * Value:JSON<br>
 * 
 * @author qiaozhi
 *
 */
public class TaskList implements TaskStatus {

	private Context mContext;
	private SparseArrayCompat<DownloadTask> mTaskArray;
	private SaveTrascation mSaveTranscation;
	private boolean mTranscationInQueue;
	
	public TaskList(Context context) {
		mContext = context.getApplicationContext();
		mTaskArray = new SparseArrayCompat<DownloadTask>(0);
	}
	
	/**
	 * 在列表初始化时，需要删掉无效的下载项<br>
	 * 某些无效的状态，例如正在下载，会转为暂停处理
	 */
	public synchronized void initializeTasks() {
		List<DownloadTask> list = loadTaskList();
		mTaskArray.clear();
		
		for (DownloadTask task : list) {
			// 1. 先行删掉列表中的无效项
			if (task == null || TextUtils.isEmpty(task.mPackageName))
				continue;
			
			// 处于正在安装状态的软件，变更为下载完毕(待安装)
			// 正在下载状态的软件，变更为暂停
			// 其它状态移除
			if (task.mStatus == STATUS_DOWNLOADING || task.mStatus == STATUS_WAIT) {
				task.mStatus = STATUS_PAUSE;
			} else if (task.mStatus == STATUS_PAUSE || task.mStatus == STATUS_FAILED
					|| task.mStatus == STATUS_DOWNLOAD) {
				// Keep status
			} else if (task.mStatus == STATUS_INSTALLING) {
				// Change to Download
				task.mStatus = STATUS_DOWNLOAD;
			} else {
				continue;
			}
			
			mTaskArray.put(task.hashCode(), task);
		}
		
		saveTaskList();
	}
	
	public synchronized DownloadTask getTaskItem(String packageName) {
		if (TextUtils.isEmpty(packageName))
			return null;
		
		int hashCode = packageName.hashCode();
		return mTaskArray.get(hashCode);
	}
	
	public synchronized List<DownloadTask> getTaskList() {
		int size = mTaskArray.size();
		List<DownloadTask> ret = new ArrayList<DownloadTask>(size);
		for (int index = 0; index < size; ++index) {
			ret.add(mTaskArray.valueAt(index));
		}
		
		Collections.sort(ret, new TaskComparator());
		return ret;
	}
	
	public synchronized DownloadTask getNextWaitTask() {
		List<DownloadTask> sortedTasks = getTaskList();
		int size = sortedTasks.size();
		for (int index = 0; index < size; ++index) {
			DownloadTask task = sortedTasks.get(index);
			if (task.mStatus == STATUS_WAIT)
				return task;
		}
		
		return null;
	}
	
	public synchronized int getRunningTaskCount() {
		int count = 0;
		int size = mTaskArray.size();
		for (int index = 0; index < size; ++index) {
			DownloadTask task = mTaskArray.valueAt(index);
			if (task.mStatus == STATUS_DOWNLOADING)
				count += 1;
		}
		
		return count;
	}
	
	public synchronized int getUncompletedTaskCount() {
		int count = 0;
		int size = mTaskArray.size();
		for (int index = 0; index < size; ++index) {
			DownloadTask task = mTaskArray.valueAt(index);
			if (task.mStatus == STATUS_DOWNLOADING
					|| task.mStatus == STATUS_FAILED
					|| task.mStatus == STATUS_PAUSE
					|| task.mStatus == STATUS_WAIT) {
				count += 1;
			}
		}
		
		return count;
	}
	
	public synchronized List<String> getNeedDownTaskLabels() {
		List<String> ret = new ArrayList<String>();
		int size = mTaskArray.size();
		for (int index = 0; index < size; ++index) {
			DownloadTask task = mTaskArray.valueAt(index);
			if (task.mStatus == STATUS_DOWNLOADING || task.mStatus == STATUS_WAIT)
				ret.add(task.mTitle);
		}
		
		return ret;
	}
	
	public synchronized void addTask(DownloadTask task) {
		if (task == null)
			return;
		
		DownloadTask exist = mTaskArray.get(task.hashCode());
		if (exist == null) {
			mTaskArray.put(task.hashCode(), task);
		}
	}
	
	public synchronized void removeTask(DownloadTask task) {
		if (task == null || TextUtils.isEmpty(task.mPackageName))
			return;
		
		mTaskArray.remove(task.hashCode());
	}
	
	public synchronized void clearTask() {
		mTaskArray.clear();
	}
	
	public synchronized void saveTaskListAsync() {
		if (mSaveTranscation == null) {
			mTranscationInQueue = false;
			mSaveTranscation = new SaveTrascation(this);
			DeviceUtils.executeWithAsyncTask(mSaveTranscation);
		} else {
			mTranscationInQueue = true;
		}
	}
	
	private synchronized void saveTaskList() {
		SharedPreferences pref = mContext.getSharedPreferences(
				Constants.PREF_FILE_DOWNLOAD_TASK, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.clear();
		
		int size = mTaskArray.size();
		for (int index = 0; index < size; ++index) {
			DownloadTask task = mTaskArray.valueAt(index);
			if (task == null || TextUtils.isEmpty(task.mPackageName))
				continue;
			saveTask(editor, task);
		}
		editor.commit();
	}
	
	private synchronized List<DownloadTask> loadTaskList() {
		SharedPreferences pref = mContext.getSharedPreferences(
				Constants.PREF_FILE_DOWNLOAD_TASK, Context.MODE_PRIVATE);
		Set<String> items = pref.getAll().keySet();
		List<DownloadTask> tasks = new ArrayList<DownloadTask>();
		
		for (String packageName : items) {
			if (TextUtils.isEmpty(packageName))
				continue;
			DownloadTask task = loadTask(pref, packageName);
			if (task == null)
				continue;
			
			tasks.add(task);
		}
		
		return tasks;
	}
	
	final private void saveTask(Editor editor, DownloadTask task) {
		if (task == null)
			return;
		
		try {
			String packageName = task.mPackageName;
			editor.putString(packageName, task.generateJSONObject().toString());
		} catch (JSONException e) { }
	}
	
	final private DownloadTask loadTask(SharedPreferences pref, String packageName) {
		if (TextUtils.isEmpty(packageName))
			return null;
		String jsonString = pref.getString(packageName, null);
		if (TextUtils.isEmpty(jsonString))
			return null;
		
		try {
			DownloadTask ret = new DownloadTask();
			ret.readFromJSON(new JSONObject(jsonString));
			if (TextUtils.isEmpty(ret.mPackageName))
				return null;
			return ret;
		} catch (JSONException e) {
			return null;
		}
	}

	public static class SaveTrascation extends AsyncTask<Object, Object, Object> {
		private WeakReference<TaskList> mRef;
		public SaveTrascation(TaskList list) {
			mRef = new WeakReference<TaskList>(list);
		}

		@Override
		protected Object doInBackground(Object... params) {
			TaskList list = mRef.get();
			if (list == null)
				return null;
			list.saveTaskList();
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			TaskList list = mRef.get();
			if (list != null) {
				list.mSaveTranscation = null;
				if (list.mTranscationInQueue) {
					list.saveTaskListAsync();
				}
			}
		}
		
	}
	
	private static class TaskComparator implements Comparator<DownloadTask> {
		@Override
		public int compare(DownloadTask lhs, DownloadTask rhs) {
			if (lhs == null && rhs == null)
				return 0;
			if (lhs == null)
				return 1;
			if (rhs == null)
				return -1;
			
			if (lhs.mCreateTime == rhs.mCreateTime) {
				return 0;
			} else if (lhs.mCreateTime < rhs.mCreateTime) {
				return 1;
			} else {
				return -1;
			}
		}
	}

}
