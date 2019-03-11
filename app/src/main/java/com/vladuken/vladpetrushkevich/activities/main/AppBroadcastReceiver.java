package com.vladuken.vladpetrushkevich.activities.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.RecyclerView;

import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.LauncherAdapter;
import com.yandex.metrica.YandexMetrica;

import java.util.List;

public class AppBroadcastReceiver extends BroadcastReceiver {

    Context context;

    RecyclerView mRecyclerView;

    public AppBroadcastReceiver(Context context, RecyclerView recyclerView) {
        this.context = context;
        mRecyclerView = recyclerView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        // when package removed
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
//            Log.e(" BroadcastReceiver ", "onReceive called "
//                    + " PACKAGE_REMOVED ");

            int uid = intent.getIntExtra(Intent.EXTRA_UID,0);
            LauncherAdapter adapter = (LauncherAdapter) mRecyclerView.getAdapter();

            ResolveInfo appToRemove = null;
            if(uid != 0){
                for(ResolveInfo resolveInfo:adapter.getInstalledAppInfo()){
                    if(resolveInfo.activityInfo.applicationInfo.uid == uid){
                        appToRemove = resolveInfo;
                    }
                }
            }

            if(appToRemove != null){
                YandexMetrica.reportEvent("Get PACKAGE_REMOVED broadcast and remove app");

                adapter.getInstalledAppInfo().remove(appToRemove);
                adapter.getPopularAppInfo().remove(appToRemove);
                adapter.notifyDataSetChanged();
            }

        }
        // when package installed
        else if (intent.getAction().equals(
                "android.intent.action.PACKAGE_ADDED")) {

//            Log.e(" BroadcastReceiver ", "onReceive called " + "PACKAGE_ADDED");
//            Toast.makeText(context, " onReceive !!!!." + "PACKAGE_ADDED",
//                    Toast.LENGTH_LONG).show();

            int uid = intent.getIntExtra(Intent.EXTRA_UID,0);
            LauncherAdapter adapter = (LauncherAdapter) mRecyclerView.getAdapter();
            adapter.getInstalledAppInfo();

            Intent startupIntent = new Intent(Intent.ACTION_MAIN);
            startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            PackageManager pm = context.getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);

            ResolveInfo appToAdd = null;
            if(uid == 0){
                return;
            }
            for(ResolveInfo resolveInfo:activities){
                if(resolveInfo.activityInfo.applicationInfo.uid == uid){
                    appToAdd = resolveInfo;
                }
            }

            if(appToAdd != null){
                //TODO ADD SORTING
                YandexMetrica.reportEvent("Get PACKAGE_ADDED broadcast and remove app");
                adapter.getInstalledAppInfo().add(appToAdd);
                adapter.getPopularAppInfo().add(appToAdd);
                adapter.notifyDataSetChanged();
            }
        }
    }
}