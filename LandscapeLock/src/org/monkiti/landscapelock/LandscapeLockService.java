package org.monkiti.landscapelock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class LandscapeLockService extends Service {
	private static final Class<?>[] mSetForegroundSignature = new Class[] {boolean.class};
	private static final Class<?>[] mStartForegroundSignature = new Class[] {int.class, Notification.class};
	private static final Class<?>[] mStopForegroundSignature = new Class[] {boolean.class};
	
	private NotificationManager mNM;
	private Method mSetForeground;
	private Method mStartForeground;
	private Method mStopForeground;
	private Object[] mSetForegroundArgs = new Object[1];
	private Object[] mStartForegroundArgs = new Object[2];
	private Object[] mStopForegroundArgs = new Object[1];

	private View view;
	private WindowManager wm;
	
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void onCreate() {
		super.onCreate();

	    mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	    
	    view = new View(getApplicationContext());
	    wm = ((WindowManager)getSystemService(WINDOW_SERVICE));
	    wm.addView(view, new LandscapeLockLayoutParams());
	    
	    try {
	        mStartForeground = getClass().getMethod("startForeground", mStartForegroundSignature);
	        mStopForeground = getClass().getMethod("stopForeground", mStopForegroundSignature);
	        return;
	    } catch (NoSuchMethodException e) {
	        // Running on an older platform.
	        mStartForeground = mStopForeground = null;
	    }
	    
	    try {
	        mSetForeground = getClass().getMethod("setForeground", mSetForegroundSignature);
	    } catch (NoSuchMethodException e) {
	        throw new IllegalStateException("OS doesn't have Service.startForeground OR Service.setForeground!");
	    }
	}

	public void onDestroy() {
	    super.onDestroy();
	    
	    stopForegroundCompat();
	    Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
	    
	    if (view != null) {
	    	wm.removeView(view);
	        view = null;
	    }
	    
	    wm = null;
	}

	public void onStart(Intent intent, int startId) {
	    Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
		wm.updateViewLayout(view, new LandscapeLockLayoutParams());
		
		startForegroundCompat(new LandscapeLockNotification(getApplicationContext()));
	}

	private void invokeMethod(Method method, Object[] args) {
	    try {
	        method.invoke(this, args);
	    } catch (InvocationTargetException e) {
	        // Should not happen.
	        Log.w("ApiDemos", "Unable to invoke method", e);
	    } catch (IllegalAccessException e) {
	        // Should not happen.
	        Log.w("ApiDemos", "Unable to invoke method", e);
	    }
	}

	private void startForegroundCompat(Notification notification) {
		startForegroundCompat(1, notification);
	}
	
	private void startForegroundCompat(int id, Notification notification) {
	    // If we have the new startForeground API, then use it.
	    if (mStartForeground != null) {
	        mStartForegroundArgs[0] = Integer.valueOf(id);
	        mStartForegroundArgs[1] = notification;
	        invokeMethod(mStartForeground, mStartForegroundArgs);
	        return;
	    }

	    // Fall back on the old API.
	    mSetForegroundArgs[0] = Boolean.TRUE;
	    invokeMethod(mSetForeground, mSetForegroundArgs);
	    mNM.notify(id, notification);
	}

	private void stopForegroundCompat() {
		stopForegroundCompat(1);
	}
	
	private void stopForegroundCompat(int id) {
	    // If we have the new stopForeground API, then use it.
	    if (mStopForeground != null) {
	        mStopForegroundArgs[0] = Boolean.TRUE;
	        invokeMethod(mStopForeground, mStopForegroundArgs);
	        return;
	    }

	    // Fall back on the old API.  Note to cancel BEFORE changing the
	    // foreground state, since we could be killed at that point.
	    mNM.cancel(id);
	    mSetForegroundArgs[0] = Boolean.FALSE;
	    invokeMethod(mSetForeground, mSetForegroundArgs);
	}

}
