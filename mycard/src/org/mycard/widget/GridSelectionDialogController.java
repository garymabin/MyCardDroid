package org.mycard.widget;

import java.util.ArrayList;
import java.util.List;

import org.mycard.R;

import android.content.Context;
import android.content.res.Resources;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class GridSelectionDialogController extends BaseDialogConfigController implements OnClickListener {
	
	public static final int GRID_SELECTION_TYPE_LEVEL = 0;
	public static final int GRID_SELECTION_TYPE_EFFECT = 1;
	
	public final class MultiSelectionAdapter extends ArrayAdapter<String> {

		public MultiSelectionAdapter(Context context, int resource,
				int textViewResourceId, String[] objects) {
			super(context, resource, textViewResourceId, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			view.setOnClickListener(GridSelectionDialogController.this);
			view.setTag(position);
			if (mSelectionMap.get(position)) {
				((CardSelectionLabel)view).setSelected(true);
			} else {
				((CardSelectionLabel)view).setSelected(false);
			}
			return view;
		}

	}

	private GridView mGridView;
	
	private BaseAdapter mAdapter;
	
	private SparseBooleanArray mSelectionMap;
	
	private int mSize;

	public GridSelectionDialogController(DialogConfigUIBase configUI, View view, int gridRes, int type, List<Integer> initSelection) {
		super(configUI, view);
		final Context context = configUI.getContext();
		final Resources res = context.getResources();
		mSelectionMap = new SparseBooleanArray();
		mGridView = (GridView) view.findViewById(R.id.grid_view);
		String[] grid = context.getResources().getStringArray(gridRes);
		mSize = grid.length;
		for (int i = 0; i < mSize; i++) {
			if (initSelection != null && initSelection.contains(i)) {
				mSelectionMap.append(i, true);
			} else {
				mSelectionMap.append(i, false);
			}
			
		}
		mAdapter = new MultiSelectionAdapter(context, R.layout.grid_item, R.id.cardSelectionLabel1, grid);
		mGridView.setAdapter(mAdapter);
		if (type == GRID_SELECTION_TYPE_LEVEL) {
			mConfigUI.setTitle(R.string.action_filter_level);
		} else if (type == GRID_SELECTION_TYPE_EFFECT) {
			mConfigUI.setTitle(R.string.action_filter_effect);
		}
		mConfigUI.setCancelButton(res.getString(R.string.button_cancel));
		mConfigUI.setPositiveButton(res.getString(R.string.action_filter));
	}

	public List<Integer> getSelections() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < mSize; i++) {
			if (mSelectionMap.get(i)) {
				list.add(i);
			}
			
		}
		return list;
	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		mSelectionMap.put(position, !mSelectionMap.get(position));
	}
}
