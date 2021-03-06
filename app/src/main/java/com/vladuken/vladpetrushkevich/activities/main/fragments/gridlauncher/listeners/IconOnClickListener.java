package com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.listeners;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.view.View;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.fragments.LauncherViewHolder;
import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.LauncherAdapter;
import com.vladuken.vladpetrushkevich.utils.LaunchCountComparator;
import com.yandex.metrica.YandexMetrica;

import java.util.Collections;

public class IconOnClickListener implements View.OnClickListener {
    protected LauncherViewHolder mViewHolder;
    protected LauncherAdapter mAdapter;

    public IconOnClickListener(LauncherViewHolder viewHolder, LauncherAdapter adapter) {
        mViewHolder = viewHolder;
        mAdapter = adapter;
    }

    @Override
    public void onClick(View v) {
        ActivityInfo activityInfo = mViewHolder.getResolveInfo().activityInfo;

        Intent i = new Intent(Intent.ACTION_MAIN)
                .setClassName(activityInfo.applicationInfo.packageName, activityInfo.name)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        mViewHolder.getApp().launches_count++;
        mViewHolder.getApp().last_time_launched = System.currentTimeMillis();
        mViewHolder.getDatabase().appDao().update(mViewHolder.getApp());
        v.getContext().startActivity(i);

        YandexMetrica.reportEvent("App start on icon click in launcher page");


        Collections.sort(mAdapter.getPopularAppInfo(),new LaunchCountComparator(mViewHolder.getDatabase()));


        SharedPreferences sharedPreferences = v.getContext()
                .getSharedPreferences(v.getContext()
                        .getString(R.string.preference_file),0);

        int sortMethod = Integer.parseInt(
                sharedPreferences.getString(
                        v.getContext().getString(R.string.preference_key_sort_method),"0"
                )
        );

        if(sortMethod == 4){
            Collections.sort(
                    mAdapter.getInstalledAppInfo(),
                    new LaunchCountComparator(mViewHolder.getDatabase())
            );
        }

        mAdapter.notifyDataSetChanged();
    }
}
