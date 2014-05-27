package org.mycard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mycard.actionbar.ActionBarCreator;
import org.mycard.common.Constants;
import org.mycard.core.Controller;
import org.mycard.fragment.BaseFragment.OnActionBarChangeCallback;
import org.mycard.fragment.BaseFragment;
import org.mycard.fragment.CardDetailFragment;
import org.mycard.fragment.DuelFragment;
import org.mycard.fragment.HomePageFragment;
import org.mycard.fragment.CardWikiFragment;
import org.mycard.fragment.ChatRoomFragment;
import org.mycard.fragment.FinalPhaseFragment;
import org.mycard.fragment.PersonalCenterFragment;
import org.mycard.fragment.UserLoginFragment;
import org.mycard.model.Model;
import org.mycard.model.data.ResourcesConstants;
import org.mycard.setting.SettingsActivity;
import org.mycard.ygo.YGOServerInfo;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.google.analytics.tracking.android.EasyTracker;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		OnActionBarChangeCallback, Handler.Callback, OnClickListener, Constants {

	public static class EventHandler extends Handler {
		public EventHandler(Callback back) {
			super(back);
		}
	}

	/**
	 * @author mabin
	 * 
	 */
	public class DrawerItemClickListener implements OnItemClickListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemClickListener#onItemClick(android
		 * .widget.AdapterView, android.view.View, int, long)
		 */
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position != -1) {
				selectItem(position + 1);
			}
		}

	}

	private static final String IMAGE_TAG = "image";
	private static final String TEXT_TAG = "text";

	private static final String TAG = "MainActivity";

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerList;
	private String[] mFragmentItems;
	
	private boolean isExit;

	private Integer[] mDrawerImageArray = { R.drawable.ic_drawer_home,
			R.drawable.ic_drawer_duel, R.drawable.ic_drawer_card_wiki,
			R.drawable.ic_drawer_chat, R.drawable.ic_drawer_forum };
	private int[] viewTo = { R.id.drawer_item_image, R.id.drawer_item_text };
	private String[] dataFrom = { IMAGE_TAG, TEXT_TAG };

	private List<Map<String, Object>> mDrawerListData = new ArrayList<Map<String, Object>>();

	private Controller mController;

	private ActionBar mActionBar;

	private ActionBarCreator mActionBarCreator;

	private EventHandler mHandler;

	private LinearLayout mLeftDrawer;

	private Menu mMenu;
	
	private FragmentManager mFragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentManager = getSupportFragmentManager();
		setContentView(R.layout.activity_main);
		initActionBar();
		initView();
		setTitle(R.string.mycard);
		mController = Controller.peekInstance();
		mActionBarCreator = new ActionBarCreator(this);
		mHandler = new EventHandler(this);
		mController.asyncUpdateMycardServer(mHandler
				.obtainMessage(Constants.MSG_ID_UPDATE_SERVER));
	}
	
	@Override
	protected void onResume() {
		mController.registerForActionSettings(mHandler);
		mController.registerForActionSupport(mHandler);
		mController.registerForActionPersonalCenter(mHandler);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		mController.unregisterForActionSettings(mHandler);
		mController.unregisterForActionSupport(mHandler);
		mController.unregisterForActionPersonalCenter(mHandler);
		super.onPause();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance(getApplicationContext()).activityStart(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(getApplicationContext()).activityStop(this);
	}

	private void initView() {

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_navigation_drawer, R.string.mycard,
				R.string.mycard);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		mFragmentItems = getResources().getStringArray(R.array.fragment_items);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		int size = mDrawerImageArray.length;
		for (int i = 0; i < size; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put(IMAGE_TAG, mDrawerImageArray[i]);
			item.put(TEXT_TAG, mFragmentItems[i]);
			mDrawerListData.add(item);
		}

		mDrawerList.setAdapter(new SimpleAdapter(this, mDrawerListData,
				R.layout.drawer_list_item, dataFrom, viewTo));
		mLeftDrawer = (LinearLayout) findViewById(R.id.left_layout);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		selectItem(2);
	}

	private void initActionBar() {
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);
	}

	@Override
	public boolean onPrepareOptionsMenu(final Menu menu) {
		mActionBarCreator.createMenu(menu);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mMenu = menu;
		mActionBarCreator.createMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public Menu getMenu() {
		return mMenu;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return Controller.peekInstance().handleActionBarEvent(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		// Highlight the selected item, update the title, and close the drawer
		navigateToFragment(position);
		mDrawerList.setItemChecked(position - 1, true);
		mDrawerLayout.closeDrawer(mLeftDrawer);
	}

	protected void navigateToFragment(int id) {
		Fragment fragment = null;
		switch (id) {
		case FRAGMENT_ID_DUEL:
			fragment = new DuelFragment();
			break;
		case FRAGMENT_ID_MY_CARD:
			fragment = new HomePageFragment();
			break;
		case FRAGMENT_ID_CARD_WIKI:
			fragment = new CardWikiFragment();
			break;
		case FRAGMENT_ID_CHAT_ROOM:
			fragment = new ChatRoomFragment();
			break;
		case FRAGMENT_ID_FORUM_LINK:
			Uri uri = Uri.parse(ResourcesConstants.FORUM_URL);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			return;
		case FRAGMENT_ID_FINAL_PHASE:
			fragment = new FinalPhaseFragment();
			break;
		case FRAGMENT_ID_PERSONAL_CENTER:
			fragment = new PersonalCenterFragment();
			break;
		default:
			break;
		}
		Bundle args = new Bundle();
		args.putString(BaseFragment.ARG_ITEM_TITLE, mFragmentItems[id - 1]);
		fragment.setArguments(args);
		// Insert the fragment by replacing any existing fragment
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		mFragmentManager.popBackStack();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.replace(R.id.content_frame, fragment).commit();
	}
	
	public void navigateToChildFragment(Bundle param, int id, int requestCode) {
		Fragment fragment = null;
		switch (id) {
		case FRAGMENT_ID_CARD_DETAIL:
			fragment = CardDetailFragment.newInstance(param);
			break;
		case FRAGMENT_ID_USER_LOGIN:
			fragment = new UserLoginFragment();
			fragment.setArguments(param);
		default:
			break;
		}
		// Insert the fragment by adding a new fragment
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		Fragment parent = mFragmentManager.findFragmentById(R.id.content_frame);
		fragment.setTargetFragment(parent, requestCode);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
	}

	@Override
	public void onActionBarChange(int msgType, int action, int arg1, Object extra) {
		// TODO Auto-generated method stub
		switch (msgType) {
		case Constants.ACTION_BAR_CHANGE_TYPE_PAGE_CHANGE:
			if (action == FRAGMENT_ID_DUEL) {
				mActionBarCreator = new ActionBarCreator(this).setNew(
						true, arg1).setPlay(true);
				mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
				mActionBar.setDisplayShowTitleEnabled(false);
			} else if (action == FRAGMENT_ID_CARD_WIKI) {
				mActionBarCreator = new ActionBarCreator(this).setFilter(true).setSearch(true, arg1).setReset(true);
				mActionBar.setDisplayShowTitleEnabled(true);
				mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			} else if (action == FRAGMENT_ID_PERSONAL_CENTER){
				mActionBarCreator = new ActionBarCreator(this).setPersonalCenter(true, R.string.logout);
				mActionBar.setDisplayShowTitleEnabled(true);
				mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			} else {
				mActionBarCreator = new ActionBarCreator(this);
				mActionBar.setDisplayShowTitleEnabled(true);
				mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			}
			break;
		case Constants.ACTION_BAR_CHANGE_TYPE_DATA_LOADING:
			if (action == 0) {
				mActionBarCreator = new ActionBarCreator(this).setNew(
						true, 0).setPlay(true);
			} else {
				mActionBarCreator = new ActionBarCreator(this).setLoading(true);
			}
			mActionBar.setDisplayShowTitleEnabled(false);
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		default:
			break;
		}
		supportInvalidateOptionsMenu();
	}

	public YGOServerInfo getServer() {
		return Model.peekInstance().getMyCardServer();
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case Constants.MSG_ID_UPDATE_SERVER:
			break;
		case Constants.ACTION_BAR_EVENT_TYPE_PERSONAL_CENTER:
			if (!(mFragmentManager.findFragmentById(R.id.content_frame) instanceof PersonalCenterFragment)) {
				navigateToFragment(FRAGMENT_ID_PERSONAL_CENTER);
			}
			break;
		case Constants.ACTION_BAR_EVENT_TYPE_SETTINGS:
			Log.d(TAG, "receive settings click action");
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		case Constants.ACTION_BAR_EVENT_TYPE_DONATE:
			BaseFragment fragment = (BaseFragment) mFragmentManager.findFragmentById(R.id.content_frame);
			Bundle bundle = new Bundle();
			bundle.putInt(ResourcesConstants.MODE_OPTIONS, ResourcesConstants.DIALOG_MODE_DONATE);
			fragment.showDialog(bundle);
			break;
		case Constants.MSG_ID_EXIT_CONFIRM_ALARM:
			isExit = false;
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.user_panel) {
			navigateToFragment(FRAGMENT_ID_PERSONAL_CENTER);
			mDrawerLayout.closeDrawer(mLeftDrawer);
		}
	}
	
	@Override
	public void finish() {
		if (!isExit) {
			isExit = true;
			SuperActivityToast.create(this, getResources().getString(R.string.exit_hint),
					SuperToast.Duration.MEDIUM).show();
			mHandler.sendEmptyMessageDelayed(Constants.MSG_ID_EXIT_CONFIRM_ALARM, 2000);
		} else {
			super.finish();
		}
	}
}
