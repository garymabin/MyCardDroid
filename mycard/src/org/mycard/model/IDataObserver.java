/*
 *****************************************************************************
 * Copyright (C) 2005-2013 UCWEB Corporation. All Rights Reserved
　　　* File        : IDataObserver.java
 * Description : 
 * Creation    : 2014年4月2日
 * Author      : mabin@ucweb.com
 * History     : 
 *               Creation, 2014年4月2日, Administrator, Create the file
 ******************************************************************************
**/
package org.mycard.model;

import android.os.Message;

/**
 * @author mabin
 *
 */
public interface IDataObserver {
	void notifyDataUpdate(Message msg);
}
