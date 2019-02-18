package com.vladuken.vladpetrushkevich.activities.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PackageBroadcastReceiver extends BroadcastReceiver {


    private static final String TAG = "BroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG,"Something happednd");
    }
}
