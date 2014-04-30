package org.mycard.core.images;


import org.mycard.R;
import org.mycard.utils.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageViewImageItemController extends AbstractImageItemController {
	protected ImageView mImageView;
	private Context mContext;
	
	public ImageViewImageItemController(Context context,ImageView view) {
		mImageView = view;
		this.mContext = context;
	}

	@Override
	protected void onImageItemChanged(int width, int height) {
		if (mImageView != null) {
			//设置默认的占位图
			Bitmap defaultBitmap = BitmapUtils.createNewBitmapWithResource
					(mContext.getResources(), R.drawable.unknown, new int[]{width, height}, false);
			mImageView.setScaleType(ScaleType.CENTER_INSIDE);
			mImageView.setImageBitmap(defaultBitmap);
		}
	}

	@Override
	public void setBitmap(Bitmap bmp) {
		if (mImageView != null) {
			if (bmp != null) {
				mImageView.setScaleType(ScaleType.CENTER_CROP);
				mImageView.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
				mImageView.setImageBitmap(bmp);
				mIsLoaded = true;
			}
		}
	}
}
