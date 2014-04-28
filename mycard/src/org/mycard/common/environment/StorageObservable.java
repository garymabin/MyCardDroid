package org.mycard.common.environment;

import java.util.Observer;

import org.mycard.utils.FastObservable;


/**
 * @author mabin
 * 
 */
public class StorageObservable extends FastObservable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observable#addObserver(java.util.Observer)
	 */
	@Override
	public void addObserver(Observer observer) {
		super.addObserver(observer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observable#deleteObserver(java.util.Observer)
	 */
	@Override
	public synchronized void deleteObserver(Observer observer) {
		super.deleteObserver(observer);
	}

}
