package com.vladuken.vladpetrushkevich.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.vladuken.vladpetrushkevich.R;

public class PrefManager {
    private final SharedPreferences mPreferences;
    private final SharedPreferences.Editor mEditor;

    private final Context mContext;

    int PRIVATE_MODE = 0;

    public PrefManager(Context context) {
        mContext = context;

        mPreferences = context.getSharedPreferences(mContext.getString(R.string.preference_file), PRIVATE_MODE);
        mEditor = mPreferences.edit();
        mEditor.apply();
    }

    public void setFirstTimeLaunch(boolean isFirstTime){
        mEditor.putBoolean(mContext.getString(R.string.preference_key_is_first_launch),isFirstTime);
        mEditor.commit();
    }

    public boolean isFirstTimeLaunch(){
        return mPreferences.getBoolean(mContext.getString(R.string.preference_key_is_first_launch),true);
    }
}
