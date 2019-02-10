package com.vladuken.vladpetrushkevich;

import android.content.Context;
import android.content.SharedPreferences;

import static com.vladuken.vladpetrushkevich.Constants.SharedPreferences.APP_PREFERENCES;
import static com.vladuken.vladpetrushkevich.Constants.SharedPreferences.IS_FIRST_TIME_LAUNCH;

public class PrefManager {
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private Context mContext;

    int PRIVATE_MODE = 0;


    public PrefManager(Context context) {
        mContext = context;

        mPreferences = context.getSharedPreferences(APP_PREFERENCES, PRIVATE_MODE);
        mEditor = mPreferences.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime){
        mEditor.putBoolean(IS_FIRST_TIME_LAUNCH,isFirstTime);
        mEditor.commit();
    }

    public boolean isFirstTimeLaunch(){
        return mPreferences.getBoolean(IS_FIRST_TIME_LAUNCH,true);
    }
}
