package org.mycard.widget;

import org.mycard.ygo.ICardFilter;
import org.mycard.ygo.YGOCardSelectionBuilder;
import org.mycard.ygo.provider.YGOCards;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.AttributeSet;

public class CardFilterSearchActionView extends SearchView implements ICardFilter, SearchView.OnQueryTextListener {
	
	private OnCardFilterChangeListener mListener;
	
	private String mFilterString = "";

	public CardFilterSearchActionView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CardFilterSearchActionView(Context context) {
		this(context, null);
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		setOnQueryTextListener(this);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		setOnQueryTextListener(null);
	}
	
	public void setOnCardFilterListener(OnCardFilterChangeListener listener) {
		mListener = listener;
	}
	
	
	@Override
	public void onFilter(int type, int arg1, int arg2, Object obj) {
	}

	@Override
	public void resetFilter() {
		mListener = null;
	}

	@Override
	public String buildSelection() {
		if (TextUtils.isEmpty(mFilterString)) {
			return null;
		} else { 
			if (TextUtils.isDigitsOnly(mFilterString)){
				return " (" + YGOCards.Datas._ID
						+ " = " + mFilterString + ")";
			} else {
				return " ( " + YGOCards.Texts.NAME
						+ " LIKE '%" + mFilterString + "%' )";
			}
		}
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		if (!mFilterString.equals(arg0)) {
			mFilterString = arg0;
			mListener.onChange(YGOCardSelectionBuilder.SELECTION_SEGMENT_SEARCH, buildSelection());
		}
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		if (!mFilterString.equals(arg0)) {
			mFilterString = arg0;
			mListener.onChange(YGOCardSelectionBuilder.SELECTION_SEGMENT_SEARCH, buildSelection());
		}
		return true;
	}
	
	

}
