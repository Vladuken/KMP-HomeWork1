package com.vladuken.vladpetrushkevich.activities.main.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.utils.ThemeChanger;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState,
                                    String rootKey) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey);

        Preference themePreference = findPreference(getString(R.string.preference_key_theme));
        themePreference.setOnPreferenceClickListener(this::onPreferenceClick);

        Preference layoutPreference = findPreference((getString(R.string.preference_key_layout)));
        layoutPreference.setOnPreferenceClickListener(this::onPreferenceClick);

        Preference sortPreference = findPreference(getString(R.string.preference_key_sort_method));
        sortPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                ThemeChanger.reloadActivity(getActivity());
                return true;
            }
        });
    }

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    private boolean onPreferenceClick(Preference preference) {

        ThemeChanger.reloadActivity(getActivity());

        return true;
    }
}
