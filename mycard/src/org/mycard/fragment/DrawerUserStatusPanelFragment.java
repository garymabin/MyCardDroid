package org.mycard.fragment;

import org.mycard.R;
import org.mycard.common.Constants;
import org.mycard.core.Controller;
import org.mycard.core.UserStatusTracker;
import org.mycard.core.images.BitmapHolder;
import org.mycard.core.images.ImageFileDownloadTaskHolder;
import org.mycard.core.images.ImageViewImageItemController;
import org.mycard.model.IDataObserver;
import org.mycard.model.Model;
import org.mycard.model.data.ImageItem;
import org.mycard.model.data.ImageItemInfoHelper;
import org.mycard.model.data.ResourcesConstants;

import android.app.Activity;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerUserStatusPanelFragment extends BaseFragment implements
		IDataObserver {
	
	private ViewGroup mUserPanel;

	private TextView mUserStatusDes;
	
	private ImageView mUserAvatar;

	private ImageViewImageItemController mImageItemController;
	
	private int mUserImageHeightInPixel;
	
	private int mUserImageWidthInPixel;

	private ImageItem mImageItem;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mUserPanel = (ViewGroup) inflater.inflate(R.layout.drawer_user_panel,
				null);
		mUserAvatar = (ImageView) mUserPanel.findViewById(R.id.userImage);
		mImageItemController = new ImageViewImageItemController(mActivity,
				mUserAvatar);
		mUserStatusDes = (TextView) mUserPanel
				.findViewById(R.id.user_status_des_text);
		mHandler.sendMessage(Message.obtain(null, Constants.MSG_ID_LOGIN, Controller.peekInstance().getLoginStatus(), -1));
		Controller.peekInstance().registerForLoginStatusChange(mHandler);
		mUserPanel.setOnClickListener(mActivity);
		return mUserPanel;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Controller.peekInstance().unregisterForLoginStatusChange(mHandler);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Controller.peekInstance().registerDataObserver(this);
		mUserImageHeightInPixel = getResources().getDimensionPixelSize(R.dimen.user_image_height);
		mUserImageWidthInPixel = getResources().getDimensionPixelSize(R.dimen.user_image_width);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Controller.peekInstance().unregisterDataObserver(this);
	}

	private void requestImage(ImageItem item, boolean isPreload) {
		// 确定加载的类型
		int type = isPreload ? Constants.BITMAP_LOAD_TYPE_PRELOAD
				: Constants.BITMAP_LOAD_TYPE_LOAD;

		// 已下载就加载
		if (ImageItemInfoHelper.isImageExist(item)) {
			Message msg = Controller.buildMessage(
					Constants.REQUEST_TYPE_LOAD_BITMAP,
					Constants.IMAGE_TYPE_ORIGINAL, type, item);
			Controller.peekInstance().requestDataOperation(this, msg);
		} else {
			// 未下载则进行请求下载
			Message msg = Controller.buildMessage(
					Constants.REQUEST_TYPE_DOWNLOAD_IMAGE,
					Constants.IMAGE_TYPE_ORIGINAL, type, item);
			Controller.peekInstance().requestDataOperation(this, msg);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case Constants.MSG_ID_LOGIN: {
			int status = msg.arg1;
			if (status == UserStatusTracker.LOGIN_STATUS_LOGGING) {
				mUserStatusDes.setText(R.string.logging);
			} else if (status == UserStatusTracker.LOGIN_STATUS_LOGIN_FAILED) {
				mUserStatusDes.setText(R.string.login_failed);
			} else if (status == UserStatusTracker.LOGIN_STATUS_LOGGED_IN) {
				String loginName = Controller.peekInstance().getLoginName();
				mUserStatusDes.setText(loginName);
				mImageItem = new ImageItem(ImageItemInfoHelper.AVATAR_IMAGE_PRIFIX + loginName, 
						mUserImageHeightInPixel, mUserImageWidthInPixel,
						ResourcesConstants.AVATAR_URL + loginName + ImageItemInfoHelper.PNG_IMAGE_SUFFIX);
				Bitmap cardImage = Model.peekInstance().getBitmap(mImageItem,
						Constants.IMAGE_TYPE_ORIGINAL);
				if (cardImage != null) {
					mImageItemController.setBitmap(cardImage, false);
				} else {
					mImageItemController.setImageItem(mImageItem);
					requestImage(mImageItem, false);
				}
			} else if (status == UserStatusTracker.LOGIN_STATUS_LOG_OUT) {
				mUserStatusDes.setText(R.string.login_sign_up);
				mUserAvatar.setImageResource(R.drawable.logo);
			}
			break;
		}
		default:
			break;
		}
		return false;
	}

	@Override
	public void notifyDataUpdate(Message msg) {
		if (msg == null)
			return;

		switch (msg.what) {
		case Constants.REQUEST_TYPE_LOAD_BITMAP:
			if (msg.obj != null && msg.obj instanceof BitmapHolder)
				onBitmapLoaded((BitmapHolder) msg.obj);
			break;
		case Constants.REQUEST_TYPE_DOWNLOAD_IMAGE:
			if (msg.obj != null && msg.obj instanceof ImageItem)
				onImageFileDownloaded(msg.arg1, msg.arg2, (ImageItem) msg.obj);
			break;
		}
	}

	private void onImageFileDownloaded(int type, int result, ImageItem item) {
		if (result != ImageFileDownloadTaskHolder.RET_DOWNLOAD_SUCCEED) {
			return;
		}

		// 下载图片完成能够找到持有该item的ImageController才继续走异步加载图片的流程
		if (mImageItemController.getImageItem().equals(item)) {
			Message msg = Controller.buildMessage(
					Constants.REQUEST_TYPE_LOAD_BITMAP, type,
					Constants.BITMAP_LOAD_TYPE_LOAD, item);
			Controller.peekInstance().requestDataOperation(this, msg);
		}
	}

	private void onBitmapLoaded(BitmapHolder holder) {
		if (holder == null)
			return;

		final ImageItem item = holder.getImageItem();
		if (item == null)
			return;

		mImageItemController.setBitmap(holder.getBitmap(), true);
	}

}
