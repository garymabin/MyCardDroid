package org.mycard.widget;

import android.content.Context;
import android.widget.Button;

public interface DialogConfigUIBase {
	Context getContext();
	
	BaseDialogConfigController getController();
	
	void setPositiveButton(CharSequence text);
	
	void setCancelButton(CharSequence text);
	
	Button getPosiveButton();
	
	Button getCancelButton();
	
	void setTitle(CharSequence text);
	
	void setTitle(int resId);

}
