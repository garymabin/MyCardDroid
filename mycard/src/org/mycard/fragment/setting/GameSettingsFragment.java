package org.mycard.fragment.setting;

import java.io.File;

import org.mycard.R;
import org.mycard.StaticApplication;
import org.mycard.common.Constants;
import org.mycard.setting.Settings;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.text.TextUtils;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GameSettingsFragment extends PreferenceFragment implements OnPreferenceChangeListener {
	
	private EditTextPreference mResPathPreference;
	
	private ListPreference mOGLESPreference;
	
	private ListPreference mCardQualityPreference;
	
	private ListPreference mFontNamePreference;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference_game);
		
		
		mResPathPreference = (EditTextPreference) findPreference(Settings.KEY_PREF_GAME_RESOURCE_PATH);
		if (TextUtils.isEmpty(mResPathPreference.getText())) {
			mResPathPreference.setText(StaticApplication.peekInstance().getDefaultResPath());
		}
		mResPathPreference.setSummary(mResPathPreference.getText());
		mResPathPreference.setOnPreferenceChangeListener(this);
		
		mOGLESPreference = (ListPreference) findPreference(Settings.KEY_PREF_GAME_OGLES_CONFIG);
		mOGLESPreference.setSummary(mOGLESPreference.getEntry());
		mOGLESPreference.setOnPreferenceChangeListener(this);
		
		mCardQualityPreference = (ListPreference) findPreference(Settings.KEY_PREF_GAME_IMAGE_QUALITY);
		mCardQualityPreference.setSummary(mCardQualityPreference.getEntry());
		mCardQualityPreference.setOnPreferenceChangeListener(this);
		
		mFontNamePreference = (ListPreference) findPreference(Settings.KEY_PREF_GAME_FONT_NAME);
		File fontsPath = new File(mResPathPreference.getText(), Constants.FONT_DIRECTORY);
		mFontNamePreference.setEntries(fontsPath.list());
		mFontNamePreference.setEntryValues(fontsPath.list());
		if (TextUtils.isEmpty(mFontNamePreference.getValue())) {
			mFontNamePreference.setValue(Constants.DEFAULT_FONT_NAME);
		}
		mFontNamePreference.setSummary(mFontNamePreference.getValue());
		
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference.getKey().equals(Settings.KEY_PREF_GAME_RESOURCE_PATH)) {
			mResPathPreference.setSummary((CharSequence) newValue);
			mResPathPreference.setText((String) newValue);
		} else if(preference.getKey().equals(Settings.KEY_PREF_GAME_OGLES_CONFIG)) {
			mOGLESPreference.setValue((String) newValue);
			mOGLESPreference.setSummary(mOGLESPreference.getEntry());
		} else if(preference.getKey().equals(Settings.KEY_PREF_GAME_IMAGE_QUALITY)) {
			mOGLESPreference.setValue((String) newValue);
			mCardQualityPreference.setSummary(mCardQualityPreference.getEntry());
		} else if (preference.getKey().equals(Settings.KEY_PREF_GAME_FONT_NAME)) {
			mOGLESPreference.setValue((String) newValue);
			mFontNamePreference.setSummary(mFontNamePreference.getEntry());
		}
		return false;
	}
}
