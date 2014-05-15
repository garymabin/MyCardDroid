package org.mycard.widget;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.util.Log;
import android.util.SparseIntArray;
import android.widget.SectionIndexer;

public class LevelsIndexer extends DataSetObserver implements SectionIndexer {

	protected Cursor mDataCursor;

	protected int mColumnIndex;

	protected CharSequence mLevels;

	protected int mLevelsLength;

	private SparseIntArray mLevelMap;

	private java.text.Collator mCollator;

	private String[] mLevelsArray;

	private final static String LOG_TAG = "LevelsIndexer";

	public LevelsIndexer(Cursor cursor, int sortedColumnIndex,
			CharSequence levels) {
		// TODO Auto-generated constructor stub
		mDataCursor = cursor;
		mColumnIndex = sortedColumnIndex;
		mLevels = levels;
		// mLevelsLength = levels.length();
		mLevelsArray = levels.toString().split(",");

		mLevelsLength = mLevelsArray.length;
		mLevelMap = new SparseIntArray(mLevelsLength);
		if (cursor != null) {
			cursor.registerDataSetObserver(this);
		}
		// Get a Collator for the current locale for string comparisons.
		mCollator = java.text.Collator.getInstance();
		mCollator.setStrength(java.text.Collator.PRIMARY);
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return mLevelsArray;
	}

	public void setCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		if (mDataCursor != null) {
			mDataCursor.unregisterDataSetObserver(this);
		}
		mDataCursor = cursor;
		if (cursor != null) {
			mDataCursor.registerDataSetObserver(this);
		}
		mLevelMap.clear();
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		// TODO Auto-generated method stub
		final SparseIntArray levelMap = mLevelMap;
		final Cursor cursor = mDataCursor;

		if (cursor == null || mLevelMap == null) {
			return 0;
		}

		if (sectionIndex <= 0) {
			return 0;
		}
		if (sectionIndex >= mLevelsLength) {
			sectionIndex = mLevelsLength - 1;
		}

		int savedCursorPos = cursor.getPosition();
		int count = cursor.getCount();
		int start = 0;
		int end = count;
		int pos;

		String targetLevel = mLevelsArray[sectionIndex]; //sectionIndex -1
		int key = Integer.parseInt(targetLevel);
		
		pos = (end + start) / 2;
		while (pos < end) {
			cursor.moveToPosition(pos);
			String curLevel = cursor.getString(mColumnIndex);
			if (curLevel == null) {
				if (pos == 0) {
					break;
				} else {
					pos--;
					continue;
				}
			}

			int diff = compareInt(curLevel, targetLevel);
			if (diff != 0) {
				if (diff > 0) {
					start = pos + 1;
					if (start >= count) {
						pos = count;
						break;
					}
				} else {
					end = pos;
				}
			} else {
				if (start == pos) {
					break;
				} else {
					end = pos;
				}
			}
			pos = (start + end) / 2;
		}
		cursor.moveToPosition(savedCursorPos);
		return pos;

	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		int savedCursorPos = mDataCursor.getPosition();
		mDataCursor.moveToPosition(position);
		String curName = mDataCursor.getString(mColumnIndex);
		mDataCursor.moveToPosition(savedCursorPos);
		for (int i = 0; i < mLevelsLength; i++) {
			String level = mLevelsArray[i];
			if (compare(curName, level) == 0) {
				return i;
			}
		}
		return 0;
	}

	protected int compare(String word, String level) {
		final String getWord;
		// if (word.length() == 0) {
		// getWord = " ";
		// } else {
		// getWord = word;
		// }
		getWord = word;
		return mCollator.compare(getWord, level);
	}

	protected int compareInt(String cur, String target) {
		int curLevel = Integer.parseInt(cur);
		int targetLevel = Integer.parseInt(target);
		if (curLevel < targetLevel) {
			return -1;
		} else if (curLevel > targetLevel) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public void onChanged() {
		// TODO Auto-generated method stub
		super.onChanged();
		mLevelMap.clear();
	}

	@Override
	public void onInvalidated() {
		// TODO Auto-generated method stub
		super.onInvalidated();
		mLevelMap.clear();
	}

}
