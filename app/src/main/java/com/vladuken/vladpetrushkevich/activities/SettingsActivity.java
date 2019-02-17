package com.vladuken.vladpetrushkevich.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vladuken.vladpetrushkevich.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
