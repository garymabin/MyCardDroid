package org.mycard.data.wrapper;

import java.util.ArrayList;

import org.json.JSONArray;

/**
 * @author mabin
 * 
 */
public abstract class BaseDataWrapper implements IBaseWrapper {
	
	protected ArrayList<String> mUrls;
	protected int mResult;
	
	protected int mRequestType;
	
	/**
	 * 
	 */
	public BaseDataWrapper(int requestType) {
		mUrls = new ArrayList<String>();
		mRequestType = requestType;
	}

	
	public abstract void parse(JSONArray data);

	@Override
	public void recyle() {
		// TODO Auto-generated method stub
	}

	public int getResult() {
		return mResult;
	}

	public void setResult(int result) {
		mResult = result;
	}
	
	/* (non-Javadoc)
	 * @see com.uc.addon.indoorsmanwelfare.model.data.wrapper.IBaseWrapper#getUrl(int)
	 */
	@Override
	public String getUrl(int index) {
		// TODO Auto-generated method stub
		if (index >= mUrls.size()) {
			return null;
		} else {
			return mUrls.get(index);
		}
	}
	
	@Override
	public int getRequestType() {
		return mRequestType;
	}
}