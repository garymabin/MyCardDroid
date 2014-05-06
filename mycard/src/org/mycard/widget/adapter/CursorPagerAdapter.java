package org.mycard.widget.adapter;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
 
public class CursorPagerAdapter<F extends Fragment> extends FragmentStatePagerAdapter {
    protected final Class<F> fragmentClass;
    protected final String[] projection;
    protected Cursor mCursor;
 
    public CursorPagerAdapter(FragmentManager fm, Class<F> fragmentClass, String[] projection, Cursor cursor) {
        super(fm);
        this.fragmentClass = fragmentClass;
        this.projection = projection;
        this.mCursor = cursor;
    }
 
    @Override
    public F getItem(int position) {
        if (mCursor == null) // shouldn't happen
            return null;
 
        mCursor.moveToPosition(position);
        F frag;
        try {
            frag = fragmentClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        Bundle args = new Bundle();
        for (int i = 0; i < projection.length; ++i) {
            args.putString(projection[i], mCursor.getString(i));
        }
        frag.setArguments(args);
        return frag;
    }
 
    @Override
    public int getCount() {
        if (mCursor == null)
            return 0;
        else
            return mCursor.getCount();
    }
 
    public void swapCursor(Cursor c) {
        if (mCursor == c)
            return;
 
        this.mCursor = c;
        notifyDataSetChanged();
    }
 
    public Cursor getCursor() {
        return mCursor;
    }
}
