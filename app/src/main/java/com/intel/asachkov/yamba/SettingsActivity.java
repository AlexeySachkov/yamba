package com.intel.asachkov.yamba;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by asachkov on 15.12.2015.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            SettingsFragment fragment = new SettingsFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment, fragment.getClass().getSimpleName())
                    .commit();
        }
    }
}
