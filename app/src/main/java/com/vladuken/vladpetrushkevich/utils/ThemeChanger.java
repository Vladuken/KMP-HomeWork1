package com.vladuken.vladpetrushkevich.utils;

import android.app.Activity;

import com.vladuken.vladpetrushkevich.R;

public final class ThemeChanger {
    private ThemeChanger() {
    }

    public static void reloadActivity(Activity activity){
        //TODO add smooth recreate
        activity.recreate();
//        activity.overridePendingTransition(android.R.anim.transition_for_incoming_activity, R.anim.transition_for_outgoing_activity);

//        activity.finish();
//        activity.overridePendingTransition(android.R.anim.transition_for_incoming_activity, R.anim.transition_for_outgoing_activity);
//        activity.startActivity(activity.getIntent());
    }

    public static void onCreateSetTheme(Activity activity,boolean isDarktheme){
        if(isDarktheme){
            activity.setTheme(R.style.AppThemeDark);
        }else {
            activity.setTheme(R.style.AppTheme);
        }
    }
}

