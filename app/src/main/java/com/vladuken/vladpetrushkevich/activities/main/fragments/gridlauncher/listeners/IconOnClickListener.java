package com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.listeners;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.View;

import com.vladuken.vladpetrushkevich.activities.main.fragments.LauncherViewHolder;

public class IconOnClickListener implements View.OnClickListener {
    protected LauncherViewHolder mViewHolder;

    public IconOnClickListener(LauncherViewHolder viewHolder) {
        mViewHolder = viewHolder;
    }

    @Override
    public void onClick(View v) {
        ActivityInfo activityInfo = mViewHolder.getResolveInfo().activityInfo;

        Intent i = new Intent(Intent.ACTION_MAIN)
                .setClassName(activityInfo.applicationInfo.packageName, activityInfo.name)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        mViewHolder.getApp().launches_count++;
        mViewHolder.getDatabase().appDao().update(mViewHolder.getApp());
        v.getContext().startActivity(i);
    }
}
