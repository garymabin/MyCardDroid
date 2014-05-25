package org.mycard.net.download;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.mycard.R;
import org.mycard.core.DownloadTracker;
import org.mycard.utils.DeviceUtils;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;


/**
 * 下载服务
 * @author qiaozhi
 *
 */
public class DownloadService extends Service {

	public static final String ACTION_START_TASK = "action_start_task";
	public static final String ACTION_START_BATCH_TASK = "action_start_batch_task";
	public static final String ACTION_STOP_TASK = "action_pause_task";
	public static final String ACTION_STOP_ALL_TASK = "action_stop_all_task";
	public static final String ACTION_RESUME_TASK = "action_resume_task";
	public static final String ACTION_REMOVE_TASK = "action_remove_task";
	public static final String ACTION_RESTART_TASK = "action_restart_task";
	public static final String ACTION_PACKAGE_CHANGED = "action_package_changed";
	public static final String ACTION_INSTALL_TASK = "action_install_task";
	public static final String ACTION_CLEAR_CACHE = "action_clear_cache";
	
	public static final String EXTRA_TASK = "task";
	public static final String EXTRA_TASK_BATCH = "task_batch";
	public static final String EXTRA_PACKAGE_NAME = "package_name";

	private ServiceBinder mBinder;
	private DownloadTracker mControl;
	private CacheCleanner mCacheCleanner;
	
	private Toast mAddTaskToast;
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mBinder = new ServiceBinder();
		mControl = new DownloadTracker(this);
		mControl.initControl();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String action = null;
		if (intent != null) {
			action = intent.getAction();
		}
		
		if (ACTION_START_TASK.equals(action)) {
			Serializable extra = intent.getSerializableExtra(EXTRA_TASK);
			if (extra != null && extra instanceof DownloadTask) {
				DownloadTask task = (DownloadTask) extra;
				showAddTaskToast(task);
				mControl.addTask(task);
			}
		} else if (ACTION_START_BATCH_TASK.equals(action)) {
			Serializable extra = intent.getSerializableExtra(EXTRA_TASK_BATCH);
			if (extra != null && extra instanceof ArrayList<?>) {
				for (Object obj : (ArrayList<?>) extra) {
					if (obj != null && obj instanceof DownloadTask) {
						DownloadTask task = (DownloadTask) obj;
						mControl.addTask(task);
					}
				}
			}
		} else if (ACTION_STOP_TASK.equals(action)) {
			Serializable extra = intent.getSerializableExtra(EXTRA_TASK);
			if (extra != null && extra instanceof DownloadTask) {
				mControl.stopTask(((DownloadTask) extra).mPackageName);
			}
		} else if (ACTION_STOP_ALL_TASK.equals(action)) {
			mControl.stopAllTask();
		} else if (ACTION_RESUME_TASK.equals(action)) {
			Serializable extra = intent.getSerializableExtra(EXTRA_TASK);
			if (extra != null && extra instanceof DownloadTask) {
				mControl.resumeTask(((DownloadTask) extra).mPackageName);
			}
		} else if (ACTION_REMOVE_TASK.equals(action)) {
			Serializable extra = intent.getSerializableExtra(EXTRA_TASK);
			if (extra != null && extra instanceof DownloadTask) {
				DownloadTask task = (DownloadTask) extra;
				mControl.removeTask(((DownloadTask) extra).mPackageName);
				if (!TextUtils.isEmpty(task.mLocalPath)) {
					File apkFile = new File(task.mLocalPath);
					apkFile.delete();
				}
			}
		} else if (ACTION_RESTART_TASK.equals(action)) {
			Serializable extra = intent.getSerializableExtra(EXTRA_TASK);
			if (extra != null && extra instanceof DownloadTask) {
				DownloadTask task = (DownloadTask) extra;
				mControl.removeTask(task.mPackageName);
				mControl.addTask(task);
			}
		} else if (ACTION_CLEAR_CACHE.equals(action)) {
			if (mCacheCleanner == null) {
				mCacheCleanner = new CacheCleanner(this);
				DeviceUtils.executeWithAsyncTask(mCacheCleanner);
			}
		}
		
		if (!mControl.isRunning())
			stopSelf();
		
		return super.onStartCommand(intent, flags, startId);
	}

	private void showAddTaskToast(DownloadTask task) {
		if (task == null)
			return;
		
		String formatter = getString(org.mycard.R.string.toast_already_added_to_download);
		String text = String.format(Locale.getDefault(), formatter, task.mTitle);
		if (mAddTaskToast == null) {
			mAddTaskToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		} else {
			mAddTaskToast.setText(text);
		}
		
		mAddTaskToast.show();
	}

	/**
	 * 请求任务列表
	 * @return
	 */
	public List<DownloadTask> getTaskList() {
		return mControl.getTaskList();
	}
	
	/**
	 * 请求某一项任务
	 */
	public DownloadTask getTask(String packageName) {
		return mControl.getTask(packageName);
	}
	
	private static class CacheCleanner extends AsyncTask<Object, Object, Object> {
		
		private WeakReference<DownloadService> mRef;

		public CacheCleanner(DownloadService service) {
			mRef = new WeakReference<DownloadService>(service);
		}
		
		@Override
		protected Object doInBackground(Object... params) {
			DownloadService service = mRef.get();
			if (service == null)
				return null;
			
			List<String> filter = new ArrayList<String>();
			List<DownloadTask> list = service.getTaskList();
			if (list != null && list.size() > 0) {
				for (DownloadTask task : list) {
					if (TextUtils.isEmpty(task.mLocalPath))
						continue;
					filter.add(task.mLocalPath);
				}
			}
			
			DeviceUtils.clearDownloadDirectory(service, filter);
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			DownloadService service = mRef.get();
			if (service == null)
				return;
			
			Toast.makeText(service, R.string.clear_cache_completed, Toast.LENGTH_LONG).show();
			service.mCacheCleanner = null;
		}
	}
	
	/**
	 * 本地服务，当外部绑定Binder后，可以直接获取Service的实例
	 * @author qiaozhi
	 *
	 */
	public class ServiceBinder extends Binder {
		public DownloadService getService() {
			return DownloadService.this;
		}
	}
}
