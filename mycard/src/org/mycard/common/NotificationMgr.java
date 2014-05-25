package org.mycard.common;

import java.util.List;
import java.util.Locale;

import org.mycard.MainActivity;
import org.mycard.R;
import org.mycard.net.download.DownloadService;
import org.mycard.net.download.DownloadTask;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

/**
 * 管理应用程序的通知栏
 * @author qiaozhi
 *
 */
public class NotificationMgr {

	private static final int ID_DOWNLOAD_STATUS = 1;
	private static final int ID_UPDATE = 2;
	private static final int ID_CLIENT_NEW_VERSION = 3;

	public static void showDownloadStatus(Context context, Bitmap largeIcon, List<String> labels) {
		if (labels == null || labels.size() <= 0)
			return;
		
		int count = labels.size();
		String spilit = context.getString(R.string.notification_labels_spilit);
		String title = String.format(Locale.getDefault(), context.getString(R.string.app_is_downloading), count);
		String text = buildLabelsText(labels, spilit);
		
		Intent intent = new Intent(context, MainActivity.class);
		intent.setAction(Constants.ACTION_VIEW_DOWNLOAD_STATUS);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		
		NotificationManager nm = (NotificationManager) context.getSystemService(
				Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setContentIntent(pendingIntent);
		builder.setContentTitle(title);
		builder.setContentText(text);
		builder.setTicker(title);
		builder.setAutoCancel(false);
		builder.setOngoing(true);
		builder.setSmallIcon(R.drawable.download_status_notification_icon);
		builder.setLargeIcon(largeIcon);
		nm.notify(ID_DOWNLOAD_STATUS, builder.build());
	}
	
	public static void showDownloadSuccess(Context context, Bitmap largeIcon, String label, DownloadTask task) {
		String title = String.format(Locale.getDefault(),
				context.getString(R.string.app_down_success), label);
		String text = context.getString(R.string.click_to_install);
		
		Intent intent = new Intent(context, DownloadService.class);
		intent.setAction(DownloadService.ACTION_INSTALL_TASK);
		intent.putExtra(DownloadService.EXTRA_PACKAGE_NAME, task.mPackageName);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		
		NotificationManager nm = (NotificationManager) context.getSystemService(
				Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setAutoCancel(true);
		builder.setContentIntent(pendingIntent);
		builder.setContentTitle(title);
		builder.setContentText(text);
		builder.setTicker(title);
		builder.setSmallIcon(R.drawable.download_success_notification_icon);
		builder.setLargeIcon(largeIcon);
		nm.notify(task.hashCode(), builder.build());
	}

	public static void showInstalled(Context context, Bitmap largeIcon, String label, DownloadTask task) {
//		String title = String.format(Locale.getDefault(),
//				context.getString(R.string.toast_download_inst_success), label);
//		String text = context.getString(R.string.click_to_open);
//		Intent intent = context.getPackageManager().getLaunchIntentForPackage(task.mPackageName);
//		if (intent == null) {
//			intent = new Intent();
//		}
//		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//		
//		NotificationManager nm = (NotificationManager) context.getSystemService(
//				Context.NOTIFICATION_SERVICE);
//		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//		builder.setAutoCancel(true);
//		builder.setContentIntent(pendingIntent);
//		builder.setContentTitle(title);
//		builder.setContentText(text);
//		builder.setTicker(title);
//		builder.setSmallIcon(R.drawable.download_success_notification_icon);
//		builder.setLargeIcon(largeIcon);
//		nm.notify(task.hashCode(), builder.build());
	}
	
	public static void cancelDownloadStatus(Context context) {
		NotificationManager nm = (NotificationManager) context.getSystemService(
				Context.NOTIFICATION_SERVICE);
		nm.cancel(ID_DOWNLOAD_STATUS);
	}
	
	private static String buildLabelsText(List<String> labels, String spilit) {
		if (labels == null || labels.size() == 0)
			return "";
		
		StringBuilder builder = new StringBuilder();
		for (String label : labels) {
			if (TextUtils.isEmpty(label))
				continue;
			builder.append(label + spilit);
		}
		
		if (builder.length() == 0)
			return "";

		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

	public static void showUpdate(Context context, Bitmap largeIcon, int count, List<String> labels) {
//		if (labels == null || labels.size() <= 0)
//			return;
//		
//		String spilit = context.getString(R.string.notification_labels_spilit);
//		String title = String.format(Locale.getDefault(), context.getString(R.string.app_can_be_update), count);
//		String text = buildLabelsText(labels, spilit);
//		
//		Intent intent = new Intent(context, MainActivity.class);
//		intent.setAction(Constants.ACTION_VIEW_UPDATE);
//		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//		
//		NotificationManager nm = (NotificationManager) context.getSystemService(
//				Context.NOTIFICATION_SERVICE);
//		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//		builder.setContentIntent(pendingIntent);
//		builder.setContentTitle(title);
//		builder.setContentText(text);
//		builder.setTicker(title);
//		builder.setAutoCancel(true);
//		builder.setSmallIcon(R.drawable.download_status_notification_icon);
//		builder.setLargeIcon(largeIcon);
//		nm.notify(ID_UPDATE, builder.build());
//		
//		Statistics.addUpremindNotificationCount(context);
	}

	public static void showClientNewVersion(Context context, String versionName) {
		if (TextUtils.isEmpty(versionName))
			return;
		
		String title = String.format(Locale.getDefault(), context.getString(R.string.new_client_version), versionName);
		
		Intent intent = new Intent(context, MainActivity.class);
		intent.setAction(Constants.ACTION_NEW_CLIENT_VERSION);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		
		NotificationManager nm = (NotificationManager) context.getSystemService(
				Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setContentIntent(pendingIntent);
		builder.setContentTitle(title);
		builder.setTicker(title);
		builder.setAutoCancel(true);
		builder.setSmallIcon(R.drawable.download_status_notification_icon);
		nm.notify(ID_CLIENT_NEW_VERSION, builder.build());
	}

}
