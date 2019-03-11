package com.vladuken.vladpetrushkevich.activities.main.fragments;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.BackgroundReceiver;
import com.vladuken.vladpetrushkevich.utils.BackgroundManager;
import com.vladuken.vladpetrushkevich.utils.ThemeChanger;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState,
                                    String rootKey) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey);

        Preference themePreference = findPreference(getString(R.string.preference_key_theme));
        themePreference.setOnPreferenceClickListener(this::onPreferenceClick);

        Preference layoutPreference = findPreference(getString(R.string.preference_key_layout));
        layoutPreference.setOnPreferenceClickListener(this::onPreferenceClick);

        Preference popularApps = findPreference(getString(R.string.preference_key_popular_apps));
        popularApps.setOnPreferenceClickListener(this::onPreferenceClick);

        Preference sortPreference = findPreference(getString(R.string.preference_key_sort_method));
        sortPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                ThemeChanger.reloadActivity(getActivity());
                return true;
            }
        });

        Preference globalBackgroundPreference = findPreference(getString(R.string.preference_one_background_for_all_screens));
        globalBackgroundPreference.setOnPreferenceClickListener(this::onPreferenceClick);


        Preference backgroundUpdateFreq = findPreference(getString(R.string.preference_background_renew_frequency));
        backgroundUpdateFreq.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                ThemeChanger.reloadActivity(getActivity());
                return true;
            }
        });
        Preference backUpdateButton = findPreference(getString(R.string.preference_button));
        backUpdateButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getActivity().sendBroadcast(
                        new Intent(BackgroundReceiver.UPDATE_BACKGROUND_ONCE)
                );
                return true;
            }
        });
    }

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    private boolean onPreferenceClick(Preference preference) {
        Log.d("KEYPREF",preference.getKey());
        ThemeChanger.reloadActivity(getActivity());


        return true;
    }
}
