package org.mycard.core.images;


import org.mycard.R;
import org.mycard.utils.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageViewImageItemController extends AbstractImageItemController {
	private static final int FADE_IN_TIME = 400;
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
			mImageView.setScaleType(ScaleType.CENTER_CROP);
			mImageView.setImageBitmap(defaultBitmap);
		}
	}

	@Override
	public void setBitmap(Bitmap bmp) {
		if (mImageView != null) {
			if (bmp != null) {
				mImageView.setScaleType(ScaleType.CENTER_CROP);
				showTransitionDrawable(mImageView, bmp);
				mIsLoaded = true;
			}
		}
	}
	
	private void showTransitionDrawable(ImageView v, Bitmap bitmap) {
		final TransitionDrawable td = new TransitionDrawable(
				new Drawable[] {
						new ColorDrawable(
								android.R.color.transparent),
						new BitmapDrawable(mContext.getResources(),
								bitmap) });
		v.setImageDrawable(td);
		td.startTransition(FADE_IN_TIME);
	}
}
