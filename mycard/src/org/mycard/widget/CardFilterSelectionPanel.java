package org.mycard.widget;

import java.util.ArrayList;
import java.util.List;

import org.mycard.R;
import org.mycard.ygo.ICardFilter;
import org.mycard.ygo.YGOCardSelectionBuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CardFilterSelectionPanel extends LinearLayout implements ICardFilter, OnMenuItemClickListener{
	
	private static final String TAG = "CardFilterSelectionPanel";
	
	private static final String PREF_KEY_LAST_INDEX = "index";
	
	private static final String PREF_KEY_LAST_SELECTION = "selection";
	
	private static final int MAX_CARD_FILTER_SUB_SELECTION_TYPE  = 5;
	
	private int mIndex;
	
	private int mSelection;
	
	private TextView mDes;
	
	private List<String[]> mSelectionArrays;
	
	private ICardFilter mCardFilterDelegate;
	
	private OnCardFilterChangeListener mListener;

	public CardFilterSelectionPanel(Context context) {
		this(context, null);
	}
	public CardFilterSelectionPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		mSelectionArrays = new ArrayList<String[]>(MAX_CARD_FILTER_SUB_SELECTION_TYPE);
	}
	
	private void loadLastSelection() {
		SharedPreferences sp = getContext().getSharedPreferences(TAG + getId(), Context.MODE_PRIVATE);
		mIndex = sp.getInt(PREF_KEY_LAST_INDEX, 0);
		mSelection = sp.getInt(PREF_KEY_LAST_SELECTION, 0);
	}
	
	private void saveLastSelection() {
		SharedPreferences.Editor editor = getContext().getSharedPreferences(TAG + getId(), Context.MODE_PRIVATE).edit();
		editor.putInt(PREF_KEY_LAST_INDEX, mIndex);
		editor.putInt(PREF_KEY_LAST_SELECTION, mSelection);
		editor.commit();
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mDes = (TextView) findViewById(R.id.des);
	}
	
	
	public void setResourceArrays(int[] resArray) {
		for (int res : resArray) {
			mSelectionArrays.add(getContext().getResources().getStringArray(res));
		}
		if (mDes != null){
			loadLastSelection();
			mDes.setText(mSelectionArrays.get(mIndex)[mSelection]);
		}
	}
	
	public void setCardFilterDelegate(ICardFilter filter) {
		mCardFilterDelegate = filter;
	}
	
	public void setCardFilterChangeListener(OnCardFilterChangeListener listener) {
		mListener = listener;
	}
	
	public boolean setCurrentSelection(int index, int selection) {
		boolean isChanged = false;
		if (mIndex != index || mSelection != selection) {
			mIndex = index;
			mSelection = selection;
			mDes.setText(mSelectionArrays.get(index)[selection]);
			saveLastSelection();
			isChanged = true;
		}
		return isChanged;
	}


	@Override
	public void onFilter(int type, int arg1, Bundle obj) {
		if (mCardFilterDelegate != null) {
			mCardFilterDelegate.onFilter(type, arg1, obj);
		}
	}
	@Override
	public void resetFilter() {
		mIndex = 0;
		mSelection = 0;
		saveLastSelection();
		mListener = null;
		if (mCardFilterDelegate != null) {
			mCardFilterDelegate.resetFilter();
		}
	}
	@Override
	public String buildSelection() {
		if (mCardFilterDelegate != null) {
			return mCardFilterDelegate.buildSelection();
		}
		return null;
	}
	@Override
	public boolean onMenuItemClick(MenuItem paramMenuItem) {
		boolean handled = true;
		switch (paramMenuItem.getGroupId()) {
		case R.id.filter_group_monster:
			handled = setCurrentSelection(1, paramMenuItem.getOrder());
			if (handled) {
				onFilter(ICardFilter.CARD_FILTER_MONSTER_TYPE, paramMenuItem.getOrder(), null);
			}
			break;
		case R.id.filter_group_race:
			handled = setCurrentSelection(0, paramMenuItem.getOrder());
			if (handled) {
				onFilter(ICardFilter.CARD_FILTER_RACE, paramMenuItem.getOrder(), null);
			}
			break;
		case R.id.filter_group_property:
			handled = setCurrentSelection(0, paramMenuItem.getOrder());
			if (handled) {
				onFilter(ICardFilter.CARD_FILTER_ATTR, paramMenuItem.getOrder(), null);
			}
			break;
		case R.id.filter_group_spell:
			handled = setCurrentSelection(2, paramMenuItem.getOrder());
			if (handled) {
				onFilter(ICardFilter.CARD_FILTER_SPELL_TYPE, paramMenuItem.getOrder(), null);
			}
			break;
		case R.id.filter_group_trap:
			handled = setCurrentSelection(3, paramMenuItem.getOrder());
			if (handled) {
				onFilter(ICardFilter.CARD_FILTER_TRAP_TYPE, paramMenuItem.getOrder(), null);
			}
			break;
		case R.id.filter_group_type_none:
			handled = setCurrentSelection(0, 0);
			if (handled) {
				onFilter(ICardFilter.CARD_FILTER_TYPE_ALL, ICardFilter.CARD_FILTER_TYPE_MONSTER_ALL, null);
			}
			break;
		case R.id.filter_group_ot:
			handled = setCurrentSelection(0, paramMenuItem.getOrder());
			if (handled) {
				onFilter(ICardFilter.CARD_FILTER_OT, paramMenuItem.getOrder(), null);
			}
			break;
		default:
			handled = false;
			break;
		}
		if (handled) {
			if (mListener != null) {
				mListener.onChange(YGOCardSelectionBuilder.SELECTION_SEGMENT_TOTAL, buildSelection());
			}
		}
		return handled;
	}
}
