package org.mycard.net.download;

/**
 * 定义了更新列表项的状态
 * @author qiaozhi
 *
 */
public interface TaskStatus {
	
	/** 状态未知 */
	public static final int STATUS_UNKNOWN = 0;
	/** 软件正在下载中 */
	public static final int STATUS_DOWNLOADING = 1;
	/** 软件在队列中等待 */
	public static final int STATUS_WAIT = 2;
	/** 软件暂停中(或失败) */
	public static final int STATUS_PAUSE = 3;
	/** 软件下载完毕 */
	public static final int STATUS_DOWNLOAD = 4;
	/** 软件下载失败 */
	public static final int STATUS_FAILED = 5;
	/** 软件正在安装中 */
	public static final int STATUS_INSTALLING = 6;
}
