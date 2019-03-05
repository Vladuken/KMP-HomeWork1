package com.vladuken.vladpetrushkevich.activities.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.vladuken.vladpetrushkevich.utils.picasso.SetBackgroundAsynkTask;

public class BackgroundReceiver extends BroadcastReceiver {

    public static final String UPDATE_BACKGROUND = "com.vladuken.vladpetrushkevich.UPDATE_BACKGROUND";


    private View mView;
    private String mPath;

    public BackgroundReceiver(View view, String path) {
        mView = view;
        mPath = path;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        new SetBackgroundAsynkTask(mView,mPath).execute();
    }
}
