package org.mycard.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapLruCache<K> extends LruCache<K, Bitmap> {

	public BitmapLruCache(int maxSize) {
		super(maxSize);
	}

	@Override
	protected synchronized void entryRemoved(boolean evicted, K key, Bitmap oldValue,
			Bitmap newValue) {
		if (oldValue != null) {
			if (!oldValue.isRecycled())
				oldValue.recycle();
			oldValue = null;
		}
	}
}
