package org.mycard.widget.adapter;

import org.mycard.fragment.BaseFragment;
import org.mycard.model.IDataObserver;

import android.database.Cursor;
import android.os.Message;
import android.support.v4.app.FragmentManager;

public class CardDetailAdapter<F extends BaseFragment> extends CursorPagerAdapter<F> implements
		IDataObserver {
	
	public CardDetailAdapter(FragmentManager fm,
			Class<F> fragmentClass, String[] projection,
			Cursor cursor) {
		super(fm, fragmentClass, projection, cursor);
	}
	
	@Override
	public void notifyDataUpdate(Message msg) {
	}

}
