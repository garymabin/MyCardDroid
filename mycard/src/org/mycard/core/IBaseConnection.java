package org.mycard.core;

import org.mycard.model.data.wrapper.BaseDataWrapper;


public interface IBaseConnection {
	
	public interface TaskStatusCallback {
		void onTaskFinish(BaseDataWrapper wrapper);
		void onTaskContinue(BaseDataWrapper wrapper);
	}
	
	
	/**
	 * Add a new task
	 * @param wrapper
	 */
	void addTask(BaseDataWrapper wrapper);
	
	/**
	 * Clean up the connection
	 */
	void purge();

}
