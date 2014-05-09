package org.mycard.widget;

import android.view.View;

public class BaseDialogConfigController {
	
	protected final DialogConfigUIBase mConfigUI;
	protected View mView;
	
	public BaseDialogConfigController(DialogConfigUIBase configUI, View view) {
		mConfigUI = configUI;
		mView = view;
	}

}
