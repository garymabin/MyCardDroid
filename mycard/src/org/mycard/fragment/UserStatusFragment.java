package org.mycard.fragment;

import org.mycard.R;
import org.mycard.common.Constants;
import org.mycard.core.Controller;
import org.mycard.core.images.BitmapHolder;
import org.mycard.core.images.ImageFileDownloadTaskHolder;
import org.mycard.core.images.ImageViewImageItemController;
import org.mycard.model.IDataObserver;
import org.mycard.model.Model;
import org.mycard.model.data.ImageItem;
import org.mycard.model.data.ImageItemInfoHelper;
import org.mycard.model.data.ResourcesConstants;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class UserStatusFragment extends BaseFragment implements IDataObserver{
	
	private String mLoginName;
	
	private ImageView mUserImage;

	private ImageViewImageItemController mImageItemController;

	private ImageItem mImageItem;

	private ImageView mUserAvatar;

	private int mUserImageHeightInPixel;

	private int mUserImageWidthInPixel;
	
	private UserInfoTabFragment mFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLoginName = getArguments().getString("username");  
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_status, null);
		((TextView) view.findViewById(R.id.user_name)).setText(mLoginName);
		mUserAvatar = (ImageView) view.findViewById(R.id.user_avatar_image);
		mImageItemController = new ImageViewImageItemController(mActivity,
				mUserAvatar);
		mImageItem = new ImageItem(ImageItemInfoHelper.AVATAR_IMAGE_PRIFIX + mLoginName, 
				mUserImageHeightInPixel, mUserImageWidthInPixel,
				ResourcesConstants.AVATAR_URL + mLoginName + ImageItemInfoHelper.PNG_IMAGE_SUFFIX);
		Bitmap cardImage = Model.peekInstance().getBitmap(mImageItem,
				Constants.IMAGE_TYPE_ORIGINAL);

		if (cardImage != null) {
			mImageItemController.setBitmap(cardImage, false);
		} else {
			mImageItemController.setImageItem(mImageItem);
			requestImage(mImageItem, false);
		}
		mFragment = new UserInfoTabFragment();
		Bundle bundle = new Bundle();
		bundle.putString("username", mLoginName);
		mFragment.setArguments(bundle);
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.replace(R.id.user_info, mFragment);
		ft.commit();
		return view;
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
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Controller.peekInstance().registerDataObserver(this);
		mUserImageHeightInPixel = getResources().getDimensionPixelSize(R.dimen.user_large_image_height);
		mUserImageWidthInPixel = getResources().getDimensionPixelSize(R.dimen.user_large_image_width);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Controller.peekInstance().unregisterDataObserver(this);
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
