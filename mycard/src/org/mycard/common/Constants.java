package org.mycard.common;

/**
 * @author mabin
 *
 */
public interface Constants {
	
	public static final String WORKING_DIRECTORY = "/ygocore/";
	
	public static final String CARD_IMAGE_DIRECTORY = "/pics/";
	public static final String THUMBNAIL_IMAGE_DIRECTORY = "/pics/thumbnail/";
	
	public static final int IO_BUFFER_SIZE = 8192;
	public static final int TRANSACT_TIMEOUT = 2 * 60 * 1000;
	
	public static final int FRAGMENT_ID_MY_CARD = 1;
	public static final int FRAGMENT_ID_DUEL = 2;
	public static final int FRAGMENT_ID_CARD_WIKI = 3;
	public static final int FRAGMENT_ID_CHAT_ROOM = 4;
	public static final int FRAGMENT_ID_FORUM_LINK = 5;
	public static final int FRAGMENT_ID_USER_STATUS = 6;
	public static final int FRAGMENT_ID_FINAL_PHASE = 7;
	public static final int FRAGMENT_ID_CARD_DETAIL = 8;
	
	
	public static final int MSG_ID_UPDATE_ROOM_LIST = 0;
	public static final int MSG_ID_UPDATE_SERVER = 1;
	public static final int MSG_ID_LOGIN = 2;
	
	public static final String PREF_FILE_COMMON = "pref_common";
	public static final String PREF_KEY_LOGIN_STATUS = "pref_login_status";
	public static final String PREF_KEY_LOGIN_NAME = "pref_login_name";
	
	
	public static final int ACTION_BAR_CHANGE_TYPE_PAGE_CHANGE = 0x1000;
	public static final int ACTION_BAR_CHANGE_TYPE_DATA_LOADING = 0x1001;
	
	public static final int ACTION_BAR_EVENT_TYPE_NEW = 0x2000;
	public static final int ACTION_BAR_EVENT_TYPE_SETTINGS = 0x2001;
	public static final int ACTION_BAR_EVENT_TYPE_SEARCH = 0x2002;
	public static final int ACTION_BAR_EVENT_TYPE_PLAY = 0x2003;
	public static final int ACTION_BAR_EVENT_TYPE_FILTER = 0x2004;
	public static final int ACTION_BAR_EVENT_TYPE_DONATE = 0x2005;
	public static final int ACTION_BAR_EVENT_TYPE_PERSONAL_CENTER = 0x2006;
	
	
	public static final int REQUEST_TYPE_UPDATE_SERVER = 0x3000;
	public static final int REQUEST_TYPE_UPDATE_ROOM = 0x3001;
	public static final int REQUEST_TYPE_LOGIN = 0x3002;
	
	public static final int REQUEST_TYPE_DOWNLOAD_IMAGE = 0x3003;
	public static final int REQUEST_TYPE_LOAD_BITMAP = 0x3004;
	
	public static final int REQUEST_TYPE_CHANGE_IMAGE_LOAD_PRIORITY = 0x3005;
	
	public static final int REQUEST_TYPE_RESET_LOAD_QUEUE = 0x3006;
	
	public static final int REQUEST_TYPE_RESET_DOWNLOAD_QUEUE = 0x3007;
	
	public static final int REQUEST_TYPE_CLEAR_BITMAP_CACHE = 0x3008;
	
	
	public static final String BUNDLE_KEY_USER_NAME = "bundle.key.user.name";
	public static final String BUNDLE_KEY_USER_PW = "bundle.key.user.pw";
	
	
	
	public static final int IMAGE_TYPE_THUMNAIL = 0;
	public static final int IMAGE_TYPE_ORIGINAL = 1;
	
	public static final int BITMAP_LOAD_TYPE_PRELOAD = 0;
	public static final int BITMAP_LOAD_TYPE_LOAD = 1;
	
	
	public static final String SETTINGS_ACTION_COMMON = "org.mycard.prefs.PREFS_COMMON";
	public static final String SETTINGS_ACTION_GAME = "org.mycard.prefs.PREFS_GAME";
	public static final String SETTINGS_ACTION_ABOUT = "org.mycard.prefs.PREFS_ABOUT";
	
	public static final String SETTINGS_FARGMENT_COMMON = "org.mycard.fragment.setting.CommonSettingsFragment";
	public static final String SETTINGS_FARGMENT_GAME = "org.mycard.fragment.setting.GameSettingsFragment";
	public static final String SETTINGS_FARGMENT_ABOUT = "org.mycard.fragment.setting.AboutSettingsFragment";

}
