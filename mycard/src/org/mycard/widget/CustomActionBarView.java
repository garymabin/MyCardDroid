package org.mycard.widget;

import org.mycard.R;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class CustomActionBarView extends RelativeLayout implements
		android.view.View.OnClickListener {

	private ViewGroup mBasicPanel;
	private ViewGroup mMorePanel;
	
	private LinearLayout mBasicItemPanel;
	private LinearLayout mMoreItemPanel;

	private View mNextNavigation;
	private View mPrevNavigation;

	public CustomActionBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomActionBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomActionBarView(Context context) {
		super(context);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mBasicPanel = (ViewGroup) findViewById(R.id.basic_action_pannel);
		mMorePanel = (ViewGroup) findViewById(R.id.more_action_pannel);
		mBasicItemPanel = (LinearLayout) findViewById(R.id.basic_item_pannel);
		mMoreItemPanel = (LinearLayout) findViewById(R.id.more_item_pannel);
		mNextNavigation = findViewById(R.id.navigation_next);
		mPrevNavigation = findViewById(R.id.navigation_previous);
		mNextNavigation.setOnClickListener(this);
	}

	public int addNewSpinner(int promptRes, int entryRes,
			OnItemSelectedListener listener, boolean isExtended) {
		Spinner spinner = (Spinner) LayoutInflater.from(getContext()).inflate(
				R.layout.custom_spinner, null);
		SpinnerAdapter adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_spinner_dropdown_item, getResources()
						.getStringArray(entryRes));
		spinner.setPromptId(promptRes);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(listener);
		if (!isExtended) {
			mBasicItemPanel.addView(spinner);
		} else {
			mMoreItemPanel.addView(spinner);
		}
		return spinner.getId();
	}

	public int addNewPopupImage(int menuRes, int typeRes, int desRes, OnMenuItemClickListener listener,
			boolean isExtended) {
		ViewGroup panel = (ViewGroup) LayoutInflater.from(getContext())
				.inflate(R.layout.custom_image, null);
		TextView type = (TextView) panel.findViewById(R.id.type);
		TextView des = (TextView) panel.findViewById(R.id.des);
		type.setText(typeRes);
		des.setText(desRes);
		panel.setOnClickListener(this);
		panel.setTag(R.id.custom_view_menu, menuRes);
		panel.setTag(R.id.custom_view_listener, listener);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		params.weight = 1.0f;
		panel.setLayoutParams(params);
		if (!isExtended) {
			mBasicItemPanel.addView(panel);
		} else {
			mMoreItemPanel.addView(panel);
		}
		return panel.getId();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.navigation_next) {
			// TODO:perform alimation();
		} else if (v.getId() == R.id.navigation_previous) {
			// perform alimation();
		} else {
			PopupMenu popup = new PopupMenu(getContext(), v);
			MenuInflater inflater = popup.getMenuInflater();
			inflater.inflate((Integer) v.getTag(R.id.custom_view_menu),
					popup.getMenu());
			popup.show();
			popup.setOnMenuItemClickListener((OnMenuItemClickListener) v
					.getTag(R.id.custom_view_listener));
		}
	}

}
