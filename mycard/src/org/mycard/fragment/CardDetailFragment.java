package org.mycard.fragment;

import org.mycard.R;
import org.mycard.common.Constants;
import org.mycard.widget.adapter.CardDetailAdapter;

import android.app.Activity;
import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

public class CardDetailFragment extends BaseFragment implements
		OnTouchListener {

	private ViewPager mViewPager;
	private CardDetailAdapter<CardDetailPagerFragment> mAdapter;

	private String[] mCommonProjection;
	private int mInitPos;
	
	private CursorWindow mWindow;

	public static CardDetailFragment newInstance(Bundle param) {
		CardDetailFragment fragment = new CardDetailFragment();

		fragment.setArguments(param);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle param = getArguments();
		mCommonProjection = param
				.getStringArray(CardWikiFragment.BUNDLE_KEY_PROJECTION);
		mInitPos = param.getInt(CardWikiFragment.BUNDLE_KEY_INIT_POSITON);
		mWindow = param.getParcelable(CardWikiFragment.BUNDLE_KEY_CURSOR_WINDOW);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity.onActionBarChange(
				Constants.ACTION_BAR_CHANGE_TYPE_PAGE_CHANGE,
				FRAGMENT_ID_CARD_DETAIL, null);
	}

	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.card_detail, null);
		mViewPager = (ViewPager) view.findViewById(R.id.card_pager);
		mAdapter = new CardDetailAdapter<CardDetailPagerFragment>(
				getChildFragmentManager(), CardDetailPagerFragment.class,
				mCommonProjection, mWindow);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(mInitPos);
		view.setOnTouchListener(this);
		return view;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
}
