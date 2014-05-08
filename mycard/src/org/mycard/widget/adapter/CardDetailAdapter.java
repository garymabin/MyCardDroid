package org.mycard.widget.adapter;

import org.mycard.fragment.BaseFragment;
import org.mycard.model.IDataObserver;

import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

public class CardDetailAdapter<F extends BaseFragment> extends CursorWindowPagerAdapter<F> implements
		IDataObserver {
	
	public CardDetailAdapter(FragmentManager fm,
			Class<F> fragmentClass, String[] projection,
			CursorWindow window) {
		super(fm, fragmentClass, projection, window);
	}
	
	@Override
	public void notifyDataUpdate(Message msg) {
	}
	
}
