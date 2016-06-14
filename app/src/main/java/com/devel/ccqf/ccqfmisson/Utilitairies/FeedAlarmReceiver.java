package com.devel.ccqf.ccqfmisson.Utilitairies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by thierry on 13/06/16.
 */
public class FeedAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 00000;
    public static final String ACTION = "com.devel.ccqf.ccqfmisson.Utilitairies.actionCode";
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, FeedService.class);
        context.startService(i);
    }
}
