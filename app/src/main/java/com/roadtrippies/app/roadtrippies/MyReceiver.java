package com.roadtrippies.app.roadtrippies;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by stefan on 26-Jun-17.
 */

public class MyReceiver extends BroadcastReceiver{

    private final int NOTIF_ID = 9001;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String key = LocationManager.KEY_PROXIMITY_ENTERING;
        final Boolean entering = intent.getBooleanExtra(key, false);

        if (entering) {
            Toast.makeText(context, "entering", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "exiting", Toast.LENGTH_SHORT).show();
        }

        NotificationManager notManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification notification = createNotification();

        notManager.notify(NOTIF_ID, notification);
    }

    private Notification createNotification(){
        Notification notification = new Notification();

        notification.icon = R.drawable.ic_explore;
        notification.when = System.currentTimeMillis();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;

        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;

        notification.ledARGB = Color.CYAN;
        notification.ledOnMS = 1500;
        notification.ledOffMS = 1500;

        return notification;
    }

}
