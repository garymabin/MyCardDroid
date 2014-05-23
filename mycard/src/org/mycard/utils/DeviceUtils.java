package org.mycard.utils;

import org.mycard.StaticApplication;

public class DeviceUtils {
	
	public static float getDensity() {
		return StaticApplication.peekInstance().getDensity();
	}

}
