package com.intel.asachkov.yamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.thenewcircle.yamba.client.YambaClient;
import com.thenewcircle.yamba.client.YambaClientInterface;

/**
 * Created by asachkov on 15.12.2015.
 */
public class StatusUpdateService extends IntentService {

    public static final String EXTRA_MESSAGE = "message";
    private static final String TAG = StatusUpdateService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 43;

    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public StatusUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String status = intent.getStringExtra(EXTRA_MESSAGE);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String username = prefs.getString(getString(R.string.username_key), "");
        final String password = prefs.getString(getString(R.string.password_key), "");

        // Check that username and password are not empty
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Log.w(TAG, "Please update your username and password");
            return;
        }

        try {
            this.postProgressNotification();

            YambaClientInterface cloud = YambaClient.getClient(username, password);
            cloud.postStatus(status);

            mNotificationManager.cancel(NOTIFICATION_ID);
            Log.d(TAG, "Successfuly posted to the cloud: " + status);
        } catch (Exception e) {
            this.postErrorNotification(status);
            Log.e(TAG, "Could not post to the cloud!", e);
        }
    }

    private void postProgressNotification() {
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Posting status")
                .setContentText("Status update in progress...")
                .setSmallIcon(android.R.drawable.sym_action_email)
                .setOngoing(true)
                .build();

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void postErrorNotification(String originalMessage) {
        Intent intent = new Intent(this, StatusActivity.class);
        intent.putExtra(EXTRA_MESSAGE, originalMessage);

        PendingIntent operaion = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Posting status")
                .setContentText("Error during status update. Tap to try again")
                .setSmallIcon(android.R.drawable.sym_action_email)
                .setContentIntent(operaion)
                .setAutoCancel(true)
                .build();

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }
}
