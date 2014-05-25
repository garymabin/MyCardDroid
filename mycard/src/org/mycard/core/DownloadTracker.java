package org.mycard.core;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import org.mycard.R;
import org.mycard.common.Constants;
import org.mycard.common.NotificationMgr;
import org.mycard.model.Model;
import org.mycard.net.download.DownloadService;
import org.mycard.net.download.DownloadTask;
import org.mycard.net.download.TaskList;
import org.mycard.net.download.TaskStatus;
import org.mycard.net.network.HttpTaskEventArg;
import org.mycard.net.network.HttpTaskListener;
import org.mycard.net.network.HttpTaskMgr;
import org.mycard.utils.Statistics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.SparseArrayCompat;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 下载控制逻辑
 * @author qiaozhi
 *
 */
public class DownloadTracker implements TaskStatus, HttpTaskListener {

	private static final int MAX_RUNNING_COUNT = 2;
	
	private static final int MSG_PROGRESS = 1;
	private static final int MSG_SUCCESS = 2;
	private static final int MSG_FAILED = 3;
	private static final int MSG_CANCEL = 4;
	private static final int MSG_TRY_NEXT_OR_STOP = 5;
	private static final int MSG_SAVE_TASK = 6;
	private static final int MSG_REDIRECT = 7;

	private DownloadService mService;
	private TaskList mTaskList;
	private SparseArrayCompat<DownloadTask> mDownloadList;
	private ControlHandler mHandler;
	private boolean mRunningFlag;
	
	private Bitmap mDownLargeIcon;
	private Bitmap mSuccessLargeIcon;
	private Toast mDownStatusToast;

	public DownloadTracker(DownloadService service) {
		mService = service;
		mRunningFlag = false;
		mDownloadList = new SparseArrayCompat<DownloadTask>();
		mHandler = new ControlHandler(this);
	}
	
	public synchronized void initControl() {
		mTaskList = Model.peekInstance().getTaskList();
		mDownLargeIcon = BitmapFactory.decodeResource(mService.getResources(),
				R.drawable.notification_down_large_icon);
		mSuccessLargeIcon = BitmapFactory.decodeResource(mService.getResources(),
				R.drawable.notification_success_large_icon);
	}
	
	public boolean isRunning() {
		return mRunningFlag;
	}
	
	public void addTask(DownloadTask task) {
		if (task == null || TextUtils.isEmpty(task.mPackageName))
			return;
		
		// 排重
		DownloadTask existTask = mTaskList.getTaskItem(task.mPackageName);
		if (existTask != null)
			return;

		if (TextUtils.isEmpty(task.mLocalPath))
			return;
		File targetFile = new File(task.mLocalPath);
		if (targetFile.exists()) {
			targetFile.delete();
		}
		
		task.mCreateTime = System.currentTimeMillis();
		
		int runningCount = mTaskList.getRunningTaskCount();
		if (runningCount >= MAX_RUNNING_COUNT) {
			task.mStatus = STATUS_WAIT;
			onTaskStatusChanged(task);
			
			mTaskList.addTask(task);
			onTaskListChanged();
			return;
		}
		
		boolean reqSuccess = startDownload(task, 0);
		if (reqSuccess) {
			task.mStatus = STATUS_DOWNLOADING;
		} else {
			task.mStatus = STATUS_FAILED;
		}
		onTaskStatusChanged(task);
		
		mTaskList.addTask(task);
		onTaskListChanged();
		
		if (!reqSuccess) {
			mHandler.sendEmptyMessage(MSG_TRY_NEXT_OR_STOP);
		}
	}

	public void resumeTask(String packageName) {
		DownloadTask task = mTaskList.getTaskItem(packageName);
		if (task == null || (task.mStatus != STATUS_PAUSE && task.mStatus != STATUS_FAILED))
			return;
		
		int runningCount = mTaskList.getRunningTaskCount();
		if (runningCount >= MAX_RUNNING_COUNT) {
			task.mStatus = STATUS_WAIT;
			onTaskStatusChanged(task);
			return;
		}
		
		boolean reqSuccess = startDownload(task, task.mTransfered);
		if (reqSuccess) {
			task.mStatus = STATUS_DOWNLOADING;
		} else {
			task.mStatus = STATUS_FAILED;
		}
		onTaskStatusChanged(task);
		
		if (!reqSuccess) {
			mHandler.sendEmptyMessage(MSG_TRY_NEXT_OR_STOP);
		}
	}
	
	public void stopTask(String packageName) {
		if (TextUtils.isEmpty(packageName))
			return;
		
		DownloadTask task = mTaskList.getTaskItem(packageName);
		if (task == null || (task.mStatus != STATUS_DOWNLOADING && task.mStatus != STATUS_WAIT))
			return;
		
		if (task.mStatus == STATUS_WAIT) {
			task.mStatus = STATUS_PAUSE;
			onTaskStatusChanged(task);
			return;
		}
		
		int index = mDownloadList.indexOfValue(task);
		if (index >= 0) {
			int taskId = mDownloadList.keyAt(index);
			HttpTaskMgr.instance(mService).cancel(taskId);
			mHandler.obtainMessage(MSG_CANCEL, taskId, 0).sendToTarget();
		}
	}
	
	public void removeTask(String packageName) {
		if (TextUtils.isEmpty(packageName))
			return;
		stopTask(packageName);
		
		DownloadTask task = mTaskList.getTaskItem(packageName);
		mTaskList.removeTask(task);
		onTaskListChanged();
	}
	
	public void stopAllTask() {
		List<DownloadTask> list = mTaskList.getTaskList();
		if (list != null && list.size() > 0) {
			for (DownloadTask task : list) {
				stopTask(task.mPackageName);
			}
		}
	}
	
	public DownloadTask getTask(String packageName) {
		return mTaskList.getTaskItem(packageName);
	}
	
	private boolean startDownload(DownloadTask task, long position) {
		if (task == null || TextUtils.isEmpty(task.mTaskUrl)) {
			Statistics.addDownFailedCount(mService, task);
			return false;
		}
		
		mRunningFlag = true;
		
		HttpTaskMgr taskMgr = HttpTaskMgr.instance(mService);
		String url = task.mTaskUrl;
		String savePath = task.mLocalPath;
//		HashMap<String, String> userHeaders = Utils.generateXHeaders(mService, url, null);
		int id = taskMgr.sendRequest(url, savePath, true, null, this, position, null);
		if (id != HttpTaskMgr.HTTPTASK_INVALID_ID) {
			mDownloadList.append(id, task);
			String format = mService.getString(R.string.toast_start_to_download);
			String toast = String.format(Locale.getDefault(), format, task.mTitle);
			showToast(toast);
			return true;
		} else {
			Statistics.addDownFailedCount(mService, task);
			return false;
		}
	}
	
	private void tryNextOrStopService() {
		DownloadTask next = mTaskList.getNextWaitTask();
		if (next == null) {
			if (mTaskList.getRunningTaskCount() <= 0) {
				mRunningFlag = false;
				mService.stopSelf();
				NotificationMgr.cancelDownloadStatus(mService);
			}
			mHandler.sendEmptyMessage(MSG_SAVE_TASK);
			return;
		}
		
		int runningCount = mTaskList.getRunningTaskCount();
		if (runningCount >= MAX_RUNNING_COUNT) {
			next.mStatus = STATUS_WAIT;
			onTaskStatusChanged(next);
			return;
		}
		
		boolean reqSuccess = startDownload(next, next.mTransfered);
		if (reqSuccess) {
			next.mStatus = STATUS_DOWNLOADING;
		} else {
			next.mStatus = STATUS_FAILED;
		}
		onTaskStatusChanged(next);
		
		if (!reqSuccess) {
			mHandler.sendEmptyMessage(MSG_TRY_NEXT_OR_STOP);
		}
	}

	public List<DownloadTask> getTaskList() {
		return mTaskList.getTaskList();
	}

	private static class ControlHandler extends Handler {
		private WeakReference<DownloadTracker> mControl;
		private Model model = Model.peekInstance();
		
		public ControlHandler(DownloadTracker ctrl) {
			mControl = new WeakReference<DownloadTracker>(ctrl);
		}
		
		@Override
		public void handleMessage(Message msg) {
			DownloadTracker control = mControl.get();
			if (control == null)
				return;
			
			switch (msg.what) {
			case MSG_PROGRESS:
				int length = msg.arg1;
				int total = msg.arg2;
				int taskId = (Integer) msg.obj;
				DownloadTask task = control.mDownloadList.get(taskId);
				if (task == null)
					return;
				
				boolean obtainTotal = (task.mTotal == 0 && total != 0);
				task.mTransfered = length;
				task.mTotal = total;
				if (obtainTotal) {
					model.reportDownloadEvent(Constants.MSG_DOWN_EVENT_STATUS_CHANGED, task);
				} else {
					model.reportDownloadEvent(Constants.MSG_DOWN_EVENT_PROGRESS, task);
				}
				
				sendEmptyMessageDelayed(MSG_SAVE_TASK, 3000);
				break;
			case MSG_SUCCESS: {
				taskId = msg.arg1;
				task = control.mDownloadList.get(taskId);
				control.mDownloadList.remove(taskId);
				if (task == null)
					return;
				
				Context context = control.mService.getApplicationContext();
				Statistics.addDownSuccessCount(context, task);
				
//				ShellUtils.execCommand(COMMAND_CHMOD + " " + task.mLocalPath);
				
//				if (AppSettings.isAutoInstall(context)) {
//					task.mStatus = STATUS_INSTALLING;
//					
//					String format = context.getString(R.string.toast_download_installing);
//					String toast = String.format(Locale.getDefault(), format, task.mTitle);
//					control.showToast(toast);
//					
//					Utils.AsyncTaskExecute(new SilenceInstall(control), task);
//				} else {
					task.mStatus = STATUS_DOWNLOAD;
					
					String format = context.getString(R.string.toast_download_success);
					String toast = String.format(Locale.getDefault(), format, task.mTitle);
					control.showToast(toast);

					NotificationMgr.showDownloadSuccess(control.mService, control.mSuccessLargeIcon,
							task.mTitle, task);
//				}
				
				control.onTaskStatusChanged(task);
				sendEmptyMessage(MSG_TRY_NEXT_OR_STOP);
				break;
			}
			case MSG_FAILED: {
				taskId = msg.arg1;
				task = control.mDownloadList.get(taskId);
				control.mDownloadList.remove(taskId);
				if (task == null)
					return;
				
				Context context = control.mService.getApplicationContext();
				Statistics.addDownFailedCount(context, task);
				
				task.mStatus = STATUS_FAILED;
				control.onTaskStatusChanged(task);
				
				String format = control.mService.getString(R.string.toast_download_failed);
				String toast = String.format(Locale.getDefault(), format, task.mTitle);
				control.showToast(toast);
				
				sendEmptyMessage(MSG_TRY_NEXT_OR_STOP);
				break;
			}
			case MSG_CANCEL:
				taskId = msg.arg1;
				task = control.mDownloadList.get(taskId);
				control.mDownloadList.remove(taskId);
				if (task == null)
					return;
				task.mStatus = STATUS_PAUSE;
				control.onTaskStatusChanged(task);
				sendEmptyMessage(MSG_TRY_NEXT_OR_STOP);
			case MSG_TRY_NEXT_OR_STOP:
				control.tryNextOrStopService();
				break;
			case MSG_SAVE_TASK:
				removeMessages(MSG_SAVE_TASK);
				control.mTaskList.saveTaskListAsync();
				break;
			case MSG_REDIRECT:
				taskId = msg.arg1;
				task = control.mDownloadList.get(taskId);
				task.mTaskUrl = (String) msg.obj;
				sendEmptyMessage(MSG_SAVE_TASK);
				break;
			default:
				throw new RuntimeException();
			}
		}
	}
	
	@Override
	public void onHttpTaskEvent(int taskid, int type, HttpTaskEventArg arg) {
		switch (type) {
		case HTTPTASK_EVENT_DATARECIVE:
			if (arg == null)
				return;
			mHandler.obtainMessage(MSG_PROGRESS, arg.mlen, arg.mTotal, taskid).sendToTarget();
			break;
		case HTTPTASK_EVENT_END:
			mHandler.obtainMessage(MSG_SUCCESS, taskid, 0).sendToTarget();
			break;
		case HTTPTASK_EVENT_FAIL:
			if (arg == null)
				return;
			mHandler.obtainMessage(MSG_FAILED, taskid, arg.mErrorId).sendToTarget();
			break;
		case HTTPTASK_EVENT_REDIRECT:
			if (arg == null)
				return;
			String url = new String(arg.buffer);
			mHandler.obtainMessage(MSG_REDIRECT, taskid, 0, url).sendToTarget();
			break;
		}
	}

	private void onTaskStatusChanged(DownloadTask task) {
		Context context = mService;
		List<String> labels = mTaskList.getNeedDownTaskLabels();
		NotificationMgr.showDownloadStatus(context, mDownLargeIcon, labels);
		
		Model.peekInstance().reportDownloadEvent(Constants.MSG_DOWN_EVENT_STATUS_CHANGED, task);
		mHandler.sendEmptyMessage(MSG_SAVE_TASK);
	}
	
	private void onTaskListChanged() {
		Context context = mService;
		List<String> labels = mTaskList.getNeedDownTaskLabels();
		NotificationMgr.showDownloadStatus(context, mDownLargeIcon, labels);
		
		int eventId = Constants.MSG_DOWN_EVENT_TASK_LIST_CHANGED;
		Model.peekInstance().reportDownloadEvent(eventId, mTaskList.getTaskList());
		mHandler.sendEmptyMessage(MSG_SAVE_TASK);
	}

	private void showToast(String toast) {
		if (mDownStatusToast == null) {
			mDownStatusToast = Toast.makeText(mService, toast, Toast.LENGTH_SHORT);
		} else {
			mDownStatusToast.setText(toast);
		}
		mDownStatusToast.show();
	}
}
