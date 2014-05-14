package org.mycard.fragment;

import org.mycard.R;
import org.mycard.common.ComplexCursorLoader;
import org.mycard.common.Constants;
import org.mycard.core.Controller;
import org.mycard.ygo.ICardFilter;
import org.mycard.ygo.YGOCardFilter;
import org.mycard.ygo.YGOCardSelectionBuilder;
import org.mycard.ygo.provider.YGOCards;
import org.mycard.utils.ResourceUtils;
import org.mycard.widget.CardFilterActionBarView;
import org.mycard.widget.CardFilterSelectionPanel;
import org.mycard.widget.OnCardFilterChangeListener;
import org.mycard.widget.CardFilterSearchActionView;
import org.mycard.widget.adapter.CardAdapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CardWikiFragment extends BaseFragment implements
		LoaderCallbacks<Cursor>, ActionMode.Callback, OnItemClickListener, OnCardFilterChangeListener {

	public static final String BUNDLE_KEY_CURSOR_WINDOW = "cardwikifragment.bundle.key.cursor.window";
	public static final String BUNDLE_KEY_PROJECTION = "cardwikifragment.bundle.key.projection";
	public static final String BUNDLE_KEY_INIT_POSITON = "cardwikifragment.bundle.key.init.pos";
	
	private static final String BUNDLE_KEY_INTERNAL_SELECTION = "selection";
	
	private static final int QUERY_SOURCE_LOADER_ID = 0;
	
	private static final int REQUEST_ID_CARD_DETAIL = 0;
	
	
	private static final String TAG = "CardWikiFragment";
	private ComplexCursorLoader mCursorLoader;

	private String[] mProjects = YGOCards.COMMON_DATA_PROJECTION;
	private String[] mProjects_id = YGOCards.COMMON_DATA_PROJECTION_ID;
	
	private String mSelection;
	
	private String[] mSelectionExtra;
	
	private String mSortOrder;

	private Uri mContentUri = YGOCards.CONTENT_URI;

	private CardAdapter mAdapter;
	
	private CardFilterActionBarView mActionBarView;
	private ListView listView;
	private Context mContext;

	private ActionMode mActionMode;
	
	private CursorWindow mCursorWindow;
	
	private YGOCardSelectionBuilder mSelectionBuilder;
	private ICardFilter mCardFilter;
	
	private CardFilterSelectionPanel mTypePanel;
	private CardFilterSelectionPanel mRacePanel;
	private CardFilterSelectionPanel mPropPanel;
	
	private CardFilterSearchActionView mSearchView;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Handler.Callback#handleMessage(android.os.Message)
	 */
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case Constants.ACTION_BAR_EVENT_TYPE_SEARCH:
			Log.i(TAG, "receive action bar search click event");
			mSearchView = (CardFilterSearchActionView) MenuItemCompat.getActionView(mActivity.getMenu().findItem(R.id.action_search));
			mSearchView.setOnCardFilterListener(this);
			break;
		case Constants.ACTION_BAR_EVENT_TYPE_FILTER:
			Log.i(TAG, "receive action bar filter click event");
			mActionMode = mActivity.startSupportActionMode(this);
			mActionBarView = (CardFilterActionBarView) LayoutInflater.from(mActivity).inflate(R.layout.custom_actionbar_view, null);
			mActionMode.setCustomView(mActionBarView);
			mTypePanel = mActionBarView.addNewPopupMenu(R.menu.filter_type, R.string.action_filter_string_type,
					new int[]{R.array.card_type_none, R.array.card_monster_type, R.array.card_spell_type, R.array.card_trap_type}, this, false);
			mTypePanel.setCardFilterDelegate(mCardFilter);
			mRacePanel = mActionBarView.addNewPopupMenu(R.menu.filter_race, R.string.action_filter_string_race, new int[]{R.array.card_race}, this, false);
			mRacePanel.setCardFilterDelegate(mCardFilter);
			mPropPanel = mActionBarView.addNewPopupMenu(R.menu.filter_property, R.string.action_filter_string_property, new int[]{R.array.card_attr}, this, false);
			mPropPanel.setCardFilterDelegate(mCardFilter);
			break;

		default:
			break;
		}
		return false;
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onResume() {
		super.onResume();
		Controller.peekInstance().registerForActionSearch(mHandler);
		Controller.peekInstance().registerForActionFilter(mHandler);
	}

	@Override
	public void onPause() {
		super.onPause();
		Controller.peekInstance().unregisterForActionSearch(mHandler);
		Controller.peekInstance().unregisterForActionFilter(mHandler);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		refreshActionBar();
		mCardFilter = new YGOCardFilter();
		mSelectionBuilder = new YGOCardSelectionBuilder();
		mSelection = null;
		mSelectionExtra = null;
		mSortOrder = mProjects[5] + " desc";
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mAdapter.onFragmentInactive();
		mTypePanel.resetFilter();
		mRacePanel.resetFilter();
		mPropPanel.resetFilter();
	}

	private void refreshActionBar() {
		mActivity.onActionBarChange(
				Constants.ACTION_BAR_CHANGE_TYPE_PAGE_CHANGE,
				FRAGMENT_ID_CARD_WIKI, R.layout.card_filter_actionbar_searchview, null);
		setTitle();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity().getApplicationContext();
		ResourceUtils.init(mContext);

		View view = inflater.inflate(R.layout.card_info_list, null);

		listView = (ListView) view.findViewById(R.id.card_info_list);
		mAdapter = new CardAdapter(mContext, mProjects_id,  null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER, listView);
		mAdapter.onFragmentActive();
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
		initCursorLoader();
		return view;
	}
	
	private void initCursorLoader() {
		getLoaderManager().initLoader(QUERY_SOURCE_LOADER_ID, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle bundle) {
		if (bundle != null) {
			mSelection = bundle.getString(BUNDLE_KEY_INTERNAL_SELECTION);
		}
		mCursorLoader = new ComplexCursorLoader(mContext, mContentUri, mProjects,
				mSelection, mSelectionExtra, mSortOrder);
		return mCursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		if (arg1 != null) {
			Log.d(TAG, "--->load finished");
		}
		mAdapter.swapCursor(arg1);
		mCursorWindow = mCursorLoader.getCursorWindow();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}

	@Override
	public boolean onCreateActionMode(ActionMode paramActionMode, Menu paramMenu) {
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode paramActionMode,
			Menu paramMenu) {
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode paramActionMode,
			MenuItem paramMenuItem) {
		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode paramActionMode) {
		mActionMode = null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Bundle bundle = new Bundle();
		bundle.putStringArray(BUNDLE_KEY_PROJECTION, mProjects);
		bundle.putInt(BUNDLE_KEY_INIT_POSITON, position);
		bundle.putParcelable(BUNDLE_KEY_CURSOR_WINDOW, mCursorWindow);
		mActivity.navigateToChildFragment(bundle, FRAGMENT_ID_CARD_DETAIL, REQUEST_ID_CARD_DETAIL);
		if (mActionMode != null) {
			mActionMode.finish();
		}
	}
	
	@Override
	public void onEventFromChild(int requestCode, int eventType, Bundle data) {
		if (REQUEST_ID_CARD_DETAIL == requestCode) {
			if (eventType == FRAGMENT_NAVIGATION_BACK_EVENT) {
				refreshActionBar();
			}
		}
	}


	@Override
	public void onChange(int type, String newSelection) {
		Bundle bundle = new Bundle();
		bundle.putString(BUNDLE_KEY_INTERNAL_SELECTION, mSelectionBuilder.setSelection(type, newSelection).toString());
		getLoaderManager().restartLoader(QUERY_SOURCE_LOADER_ID,
				bundle, this);
	}

}
