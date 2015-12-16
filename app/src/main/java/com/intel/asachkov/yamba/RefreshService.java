package com.intel.asachkov.yamba;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.thenewcircle.yamba.client.YambaClient;
import com.thenewcircle.yamba.client.YambaClientInterface;
import com.thenewcircle.yamba.client.YambaStatus;

import java.util.List;

/**
 * Created by asachkov on 15.12.2015.
 */
public class RefreshService extends IntentService {

    private static final String TAG = RefreshService.class.getSimpleName();

    private final IBinder mBinder = new LocalBinder();

    public RefreshService() {
        super(TAG);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            _getTimeline();
        } catch (Exception e) {
            Log.e(TAG, "Cannot update statuses!", e);
        }
    }

    public List<YambaStatus> getTimeline() throws Exception {
        return _getTimeline();
    }

    private List<YambaStatus> _getTimeline() throws Exception {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String username = prefs.getString(getString(R.string.username_key), "");
        final String password = prefs.getString(getString(R.string.password_key), "");

        // Check that username and password are not empty
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Log.w(TAG, "Please update your username and password");
            return null;
        }

        YambaClientInterface cloud = YambaClient.getClient(username, password);
        List<YambaStatus> timeline = cloud.getTimeline(20);

        Log.d(TAG, "Received " + timeline.size() + " statuses");

        return timeline;
    }

    public class LocalBinder extends Binder {
        public RefreshService getService() {
            return RefreshService.this;
        }
    }
}
