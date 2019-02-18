package com.vladuken.vladpetrushkevich.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.vladuken.vladpetrushkevich.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState,
                                    String rootKey) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey);
    }

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }
}
