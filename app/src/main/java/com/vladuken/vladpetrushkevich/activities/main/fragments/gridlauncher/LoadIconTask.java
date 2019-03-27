package com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.vladuken.vladpetrushkevich.activities.main.fragments.LauncherViewHolder;
import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.listeners.IconOnClickListener;

import java.util.Map;

public class LoadIconTask extends AsyncTask<Void,Void, Drawable> {
    Map<ResolveInfo,Drawable> mDrawableMap;
    ResolveInfo mAppInfo;
    PackageManager mPackageManager;
    LauncherViewHolder mViewHolder;
    LauncherAdapter mAdapter;

    public LoadIconTask(LauncherAdapter adapter,LauncherViewHolder viewHolder,Map<ResolveInfo, Drawable> drawableMap, ResolveInfo appInfo, PackageManager packageManager) {
        mAdapter = adapter;
        mDrawableMap = drawableMap;
        mAppInfo = appInfo;
        mPackageManager = packageManager;
        mViewHolder = viewHolder;
    }

    @Override
    protected Drawable doInBackground(Void... voids) {
        return mAppInfo.loadIcon(mPackageManager);
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        mDrawableMap.put(mAppInfo,drawable);
        mViewHolder.bind(mAppInfo,drawable);
        mViewHolder.itemView.setOnClickListener(new IconOnClickListener(mViewHolder, mAdapter));
//        mViewHolder.itemView.setOnLongClickListener(new AppLongClickListener(mViewHolder.getApp(),mViewHolder.itemView));
    }
}
