package org.mycard.fragment;

import org.mycard.R;
import org.mycard.core.UserStatusTracker;

import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LoginHintFragment extends BaseFragment {
	
	private static final String LOGIN_HINT_URL_LOGIN = "login";
	
	private static final String LOGIN_HINT_URL_FREE_MODE = "freemode";

	private ViewGroup mContenView;
	
	private int mStatus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStatus = getArguments().getInt("loginstatus");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContenView = (ViewGroup) inflater.inflate(R.layout.login_hint, null);
		showContent(mStatus);
		return mContenView;
	}

	private void showContent(int status) {
		if (status == UserStatusTracker.LOGIN_STATUS_LOG_OUT
				|| status == UserStatusTracker.LOGIN_STATUS_LOGIN_FAILED) {
			TextView textView = new TextView(getActivity());
			textView.setTextSize(18);
			textView.setAutoLinkMask(Linkify.WEB_URLS);
			textView.setMovementMethod(LinkMovementMethod.getInstance());
			textView.setLineSpacing(0, 1.5f);
			textView.setLinkTextColor(getResources().getColor(R.color.apptheme_color));
			textView.setFocusable(false);  
			textView.setClickable(false);  
			textView.setLongClickable(false);
			setTextViewHTML(textView, getResources().getString(R.string.login_hint_not_logged_in));
			mContenView.removeAllViews();
			mContenView.addView(textView);
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
	}

	protected void setTextViewHTML(TextView text, String html) {
		CharSequence sequence = Html.fromHtml(html);
		SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
		URLSpan[] urls = strBuilder.getSpans(0, sequence.length(),
				URLSpan.class);
		for (URLSpan span : urls) {
			makeLinkClickable(strBuilder, span);
		}
		text.setText(strBuilder);
	}

	private void makeLinkClickable(SpannableStringBuilder strBuilder,
			final URLSpan span) {
		int start = strBuilder.getSpanStart(span);
		int end = strBuilder.getSpanEnd(span);
		int flags = strBuilder.getSpanFlags(span);
		ClickableSpan clickable = new ClickableSpan() {
			public void onClick(View view) {
				if(span.getURL().equals(LOGIN_HINT_URL_LOGIN)) {
					((BaseFragment)getTargetFragment()).onEventFromChild(getTargetRequestCode(),
							FragmentNavigationListener.FRAGMENT_NAVIGATION_DUEL_LOGIN_ATTEMP_EVENT,
							UserStatusTracker.LOGIN_STATUS_LOG_OUT, -1, null);
				} else if (span.getURL().equals(LOGIN_HINT_URL_FREE_MODE)) {
					((BaseFragment)getTargetFragment()).onEventFromChild(getTargetRequestCode(),
							FragmentNavigationListener.FRAGMENT_NAVIGATION_DUEL_FREE_MODE_EVENT, -1, -1, null);
				}
			}
		};
		strBuilder.setSpan(clickable, start, end, flags);
		strBuilder.removeSpan(span);
	}

}
