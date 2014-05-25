package org.mycard.widget.preference;

import org.mycard.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

public class MyCardListPreference extends ListPreference {

	public MyCardListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyCardListPreference(Context context) {
		super(context);
	}
	
	@Override
	protected void showDialog(Bundle state) {
		super.showDialog(state);
		final Resources res = getContext().getResources();
		// Title
        final int titleId = res.getIdentifier("alertTitle", "id", "android");
        final View title = getDialog().findViewById(titleId);
        if (title != null) {
            ((TextView) title).setTextColor(res.getColor(R.color.apptheme_color));
        }

        // Title divider
        final int titleDividerId = res.getIdentifier("titleDivider", "id", "android");
        final View titleDivider = getDialog().findViewById(titleDividerId);
        if (titleDivider != null) {
            titleDivider.setBackgroundColor(res.getColor(R.color.apptheme_color));
        }
	}
}
