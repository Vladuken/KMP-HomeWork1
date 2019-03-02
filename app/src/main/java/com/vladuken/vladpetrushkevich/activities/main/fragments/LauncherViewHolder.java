package com.vladuken.vladpetrushkevich.activities.main.fragments;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.entity.App;

public class LauncherViewHolder extends RecyclerView.ViewHolder {
    protected ResolveInfo mResolveInfo;

    private final ImageView mAppIcon;
    private final TextView mAppTitle;

    private int mIconLayoutId;
    private int mTitleLayoutId;

    private final View mView;

    private App mApp;
    private final AppDatabase mDatabase;
    private boolean isBinded = false;


    public LauncherViewHolder(@NonNull View itemView, AppDatabase database, int icon_layout_id, int title_layout_id) {
        super(itemView);
        mView = itemView;
        mAppIcon = itemView.findViewById(icon_layout_id);
        mAppTitle = itemView.findViewById(title_layout_id);
        mDatabase = database;

        mIconLayoutId = icon_layout_id;
        mTitleLayoutId = title_layout_id;
    }

    public ResolveInfo getResolveInfo() {
        return mResolveInfo;
    }

    public App getApp() {
        return mApp;
    }

    public void setApp(App app) {
        mApp = app;
    }

    public boolean isBinded() {
        return isBinded;
    }

    public AppDatabase getDatabase() {
        return mDatabase;
    }

    public void bind(ResolveInfo resolveInfo, Drawable icon) {

        mResolveInfo = resolveInfo;
        PackageManager pm = mView.getContext().getPackageManager();
        String appName = mResolveInfo.loadLabel(pm).toString();


        //TODO move all this code to Adaper
        mApp = mDatabase.appDao().getById(mResolveInfo.activityInfo.packageName);
        if (mApp == null) {
            mApp = new App(mResolveInfo.activityInfo.packageName, 0);
            mDatabase.appDao().insertAll(mApp);
        }

        mAppIcon.setImageDrawable(icon);
        mAppTitle.setText(appName);
        isBinded = true;
    }
}