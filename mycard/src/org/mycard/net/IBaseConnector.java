package org.mycard.net;

import org.mycard.model.data.wrapper.BaseDataWrapper;


public interface IBaseConnector {
	
	void get(BaseDataWrapper wrapper) throws InterruptedException;
	
}
