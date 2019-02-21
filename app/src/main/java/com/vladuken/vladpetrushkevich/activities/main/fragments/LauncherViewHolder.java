package com.vladuken.vladpetrushkevich.activities.main.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.entity.App;

public class LauncherViewHolder extends RecyclerView.ViewHolder {
    protected ResolveInfo mResolveInfo;

    private final ImageView mAppIcon;
    private final TextView mAppTitle;

    int mIconLayoutId;
    int mTitleLayoutId;

    private final View mView;

    private App mApp;
    private final AppDatabase mDatabase;

    public LauncherViewHolder(@NonNull View itemView, AppDatabase database, int icon_layout_id, int title_layout_id) {
        super(itemView);
        mView = itemView;
        mAppIcon = itemView.findViewById(icon_layout_id);
        mAppTitle = itemView.findViewById(title_layout_id);
        mDatabase = database;

        mIconLayoutId = icon_layout_id;
        mTitleLayoutId = title_layout_id;
//        itemView.setOnClickListener(this::onClick);
//        itemView.setOnLongClickListener(this::showPopUp);
    }

    public ResolveInfo getResolveInfo() {
        return mResolveInfo;
    }

    public App getApp() {
        return mApp;
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
    }

//    public void onClick(View v) {
//        ActivityInfo activityInfo = mResolveInfo.activityInfo;
//
//        Intent i = new Intent(Intent.ACTION_MAIN)
//                .setClassName(activityInfo.applicationInfo.packageName, activityInfo.name)
//                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        mApp.launches_count++;
//        mDatabase.appDao().update(mApp);
//
//        mView.getContext().startActivity(i);
//    }

//    public boolean showPopUp(View v) {
//        PopupMenu popup = new PopupMenu(v.getContext(), v);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.app_popup, popup.getMenu());
//
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_uninstall_app:
//                        uninstallApp();
//                        return true;
//
//                    case R.id.action_open_app_settings:
//                        openAppSettings();
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//        });
//
//        MenuItem launchCountMenu = popup.getMenu().findItem(R.id.action_count_app_launches);
//        launchCountMenu.setTitle("Launched " + mApp.launches_count + " times");
//        launchCountMenu.setEnabled(false);
//        popup.show();
//        return true;
//    }

//    protected void uninstallApp() {
//        Intent i = new Intent();
//        i.setData(Uri.parse("package:" + mResolveInfo.activityInfo.packageName));
//        //TODO StartactivityFor Result to update on gridview ondelete app
//        mView.getContext().startActivity(i);
//    }
//
//    protected void openAppSettings() {
//        Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        i.setData(Uri.parse("package:" + mResolveInfo.activityInfo.packageName));
//        //TODO StartactivityFor Result to update on gridview ondelete app
//        mView.getContext().startActivity(i);
//
//    }

}