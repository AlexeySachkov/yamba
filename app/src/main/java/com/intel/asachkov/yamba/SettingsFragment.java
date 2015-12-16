package com.intel.asachkov.yamba;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by asachkov on 15.12.2015.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }
}
