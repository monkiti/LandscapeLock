package org.monkiti.landscapelock;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;

public class LandscapeLockLayoutParams extends LayoutParams {
	public LandscapeLockLayoutParams() {
		super(0, 0, TYPE_TOAST, FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
		
	    gravity = Gravity.TOP;
	    screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	}
}
