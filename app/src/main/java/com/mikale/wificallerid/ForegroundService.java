package com.mikale.wificallerid;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ForegroundService extends Service {
    private NotificationManagerCompat mNotificationManager;
    private final static int NOTIFICATION_ID = 95;
    private final static String CHANNEL_ID = "service_notification_id";
    public final static String ACTION_STOP = "action_stop";

    public ForegroundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = NotificationManagerCompat.from(this);
        // Creating notification channel
        createNotificationChannel();
        Intent intent = new Intent(this, MainActivity.class);
        // Use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Building notification here
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("MyForegroundService");
        mBuilder.setContentText("The Service is currently running");
        mBuilder.setAutoCancel(false);
        mBuilder.setContentIntent(pIntent);
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        // Creating Intent and Pending Intent for actions
        Intent bdIntent = new Intent(this, CallReceiver.class);
        bdIntent.setAction(ACTION_STOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, bdIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Adding action to notification builder
        mBuilder.addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop Service", pendingIntent);
        // Launch notification
        startForeground(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mNotificationManager.cancel(NOTIFICATION_ID);
        super.onDestroy();
    }

    /**
     * -------------------------- Private Methods ------------------------------------------------
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Deneme Service";
            String description = "Deneme aciklama";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}