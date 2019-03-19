package com.vladuken.vladpetrushkevich.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.LauncherActivity;

public final class ThemeChanger {
    private ThemeChanger() {
    }

    public static void reloadActivity(Activity activity){
        //TODO add smooth recreate
        if(activity instanceof LauncherActivity){
            ((LauncherActivity)activity).restartActivity();
        }else {
            activity.recreate();
        }
    }

    public static void onCreateSetTheme(Activity activity,boolean isDarktheme){
        if(isDarktheme){
            activity.setTheme(R.style.AppThemeDark);
        }else {
            activity.setTheme(R.style.AppTheme);
        }
    }
}

