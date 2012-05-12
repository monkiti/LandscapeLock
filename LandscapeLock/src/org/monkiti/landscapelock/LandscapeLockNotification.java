package org.monkiti.landscapelock;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class LandscapeLockNotification extends Notification {
	public LandscapeLockNotification(Context context) {
		String contentTitle = "LandscapeLock";
        icon = R.drawable.ic_launcher;
        tickerText = "LandscapeLock";
        when = System.currentTimeMillis();
		flags = FLAG_NO_CLEAR | FLAG_ONGOING_EVENT;
		
		Context appContext = context.getApplicationContext();
		Intent intent = new Intent(appContext, LandscapeLockActivity.class);
		contentIntent = PendingIntent.getActivity(appContext, 0, intent, 0);
		setLatestEventInfo(context, contentTitle, tickerText, contentIntent);
	}
}
