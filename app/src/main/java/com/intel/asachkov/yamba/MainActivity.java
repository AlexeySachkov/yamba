package com.intel.asachkov.yamba;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.thenewcircle.yamba.client.YambaStatus;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected ListView mStatusesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mStatusesListView = (ListView) findViewById(R.id.statusesListView);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        (new RefreshStatusesAsyncTask()).execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        (new RefreshStatusesAsyncTask()).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                (new RefreshStatusesAsyncTask()).execute();
                return true;
            case R.id.action_post_status:
                startActivity(new Intent(this, StatusActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void statusesNewFloatingActionButtonOnClick(View view) {
        startActivity(new Intent(this, StatusActivity.class));
    }

    /**
     * Created by asachkov on 15.12.2015.
     */
    public class RefreshStatusesAsyncTask extends AsyncTask<URL, Integer, List<YambaStatus>> {
        private RefreshService mRefreshService;
        //private MenuItem mActionRefresh;
        private boolean mBound = false;
        //private Animation loadAnimation;

        @Override
        protected void onPreExecute() {
            //this.mActionRefresh.st
        }

        @Override
        protected void onPostExecute(List<YambaStatus> statuses) {
            mStatusesListView.setAdapter(new YambaStatusAdapter(MainActivity.this, statuses));
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<YambaStatus> doInBackground(URL... params) {
            Intent intent = new Intent(MainActivity.this, RefreshService.class);
            MainActivity.this.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

            while (!mBound) Thread.yield();

            List<YambaStatus> statuses;

            try {
                statuses = mRefreshService.getTimeline();
            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
                statuses = new ArrayList<YambaStatus>();
            } finally {
                MainActivity.this.unbindService(mConnection);
            }

            return statuses;
        }

        private ServiceConnection mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                RefreshService.LocalBinder binder = (RefreshService.LocalBinder) service;
                mRefreshService = binder.getService();
                mBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mRefreshService = null;
                mBound = false;
            }
        };
    }
}
