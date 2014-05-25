package org.mycard.utils;

import java.io.File;
import java.util.List;

import org.mycard.StaticApplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

public class DeviceUtils {
	
	private static final String DOWNLOAD_DIR_NAME = "downloads";

	public static float getDensity() {
		return StaticApplication.peekInstance().getDensity();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void executeWithAsyncTask(AsyncTask<Object, Object, Object> task, Object... params) {
		if (Build.VERSION.SDK_INT >= 11) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		} else {
			task.execute(params);
		}
	}
	
	/**
	 * 清空下载目录
	 */
	public static void clearDownloadDirectory(Context context, List<String> filter) {
		File dir = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			dir = new File(context.getExternalCacheDir(), DOWNLOAD_DIR_NAME);
		} else {
			dir = new File(context.getCacheDir(), DOWNLOAD_DIR_NAME);
		}
		deleteDirectory(context, dir, filter);
	}
	
	/**
	 * 删除某个目录及所有文件
	 * @param filter 
	 */
	final private static void deleteDirectory(Context context, File dir, List<String> filter) {
		if (!dir.isDirectory() && (filter == null || !filter.contains(dir.getAbsolutePath()))) {
			dir.delete();
			return;
		}
		
		File[] files = dir.listFiles();
		if (files == null)
			return;
		if (files.length == 0 && (filter == null || !filter.contains(dir.getAbsolutePath()))) {
			dir.delete();
			return;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				deleteDirectory(context, file, filter);
			} else {
				if (filter == null || !filter.contains(file.getAbsolutePath())) {
					file.delete();
				}
			}
		}
	}

}
