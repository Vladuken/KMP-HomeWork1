package com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.vladuken.vladpetrushkevich.activities.main.fragments.LauncherViewHolder;

import java.util.Map;

public class LoadIconTask extends AsyncTask<Void,Void, Drawable> {
    Map<ResolveInfo,Drawable> mDrawableMap;
    ResolveInfo mAppInfo;
    PackageManager mPackageManager;
    LauncherViewHolder mViewHolder;

    public LoadIconTask(LauncherViewHolder viewHolder,Map<ResolveInfo, Drawable> drawableMap, ResolveInfo appInfo, PackageManager packageManager) {
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
    }
}
