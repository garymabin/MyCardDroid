package org.mycard.widget;

import org.mycard.R;
import org.mycard.model.data.ResourcesConstants;
import org.mycard.ygo.YGOServerInfo;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ServerDialogController extends BaseDialogConfigController implements TextWatcher{
	
	private EditText mNameEditText;

	private EditText mIPEditText;

	private EditText mPortEditText;
	
	private String mId;

	public ServerDialogController(DialogConfigUIBase configUI, View view, Bundle param) {
		super(configUI, view);
		final Resources res = configUI.getContext().getResources();
		int index = param.getInt("index");
		int mode = param.getInt(ResourcesConstants.MODE_OPTIONS);
		YGOServerInfo info = null;
		if (mode == ResourcesConstants.DIALOG_MODE_EDIT_SERVER) {
			info = param.getParcelable("server");
		}
		mNameEditText = (EditText) view.findViewById(R.id.server_name_edit_text);
		mNameEditText.addTextChangedListener(this);
		mIPEditText = (EditText) view.findViewById(R.id.server_addr_edit_text);
		mIPEditText.addTextChangedListener(this);
		mPortEditText = (EditText) view.findViewById(R.id.server_port_edit_text);
		mPortEditText.addTextChangedListener(this);
		
		if (info != null) {
			mNameEditText.setText(info.name);
			mIPEditText.setText(info.ipAddrString);
			mPortEditText.setText(info.port + "");
		}
		
		mConfigUI.setCancelButton(res.getString(R.string.button_cancel));
		mId = index + "";
		
		if(mode == ResourcesConstants.DIALOG_MODE_ADD_NEW_SERVER) {
			mConfigUI.setPositiveButton(res.getString(R.string.button_create));
			configUI.setTitle(R.string.action_new_server);
		} else if (mode == ResourcesConstants.DIALOG_MODE_EDIT_SERVER) {
			mConfigUI.setPositiveButton(res.getString(R.string.button_update));
			configUI.setTitle(R.string.action_edit_server);
		}
		enableSubmitIfAppropriate();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(final Editable s) {
		enableSubmitIfAppropriate();		
	}
	
	/*package*/ void enableSubmitIfAppropriate() {
		Button positive = mConfigUI.getPosiveButton();
		if (positive == null)
			return;
		String name = mNameEditText.getText().toString().trim();
		String addr = mIPEditText.getText().toString().trim();
		String portString = mPortEditText.getText().toString().trim();
		if (TextUtils.isEmpty(name) || 
				TextUtils.isEmpty(addr) ||
				TextUtils.isEmpty(portString)) {
			positive.setEnabled(false);
		} else {
			positive.setEnabled(true);
		}
	}
	
	public YGOServerInfo getServerInfo() {
		YGOServerInfo info = new YGOServerInfo();
		info.name = mNameEditText.getText().toString().trim();
		info.ipAddrString = mIPEditText.getText().toString().trim();
		info.port = Integer.parseInt(mPortEditText.getText().toString().trim());
		info.id = mId;
		return info;
	}

}
