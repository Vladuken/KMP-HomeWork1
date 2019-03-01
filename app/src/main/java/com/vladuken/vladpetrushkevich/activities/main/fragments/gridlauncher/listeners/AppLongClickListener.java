package com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.listeners;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.db.entity.App;

public class AppLongClickListener implements View.OnLongClickListener {

    protected App mApp;
    protected View mView;

    public AppLongClickListener(App app, View view) {
        mApp = app;
        mView = view;
    }

    @Override
    public boolean onLongClick(View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.app_popup, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_uninstall_app:
                        uninstallApp();
                        return true;

                    case R.id.action_open_app_settings:
                        openAppSettings();
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuItem launchCountMenu = popup.getMenu().findItem(R.id.action_count_app_launches);
        launchCountMenu.setTitle(v.getResources().getString(R.string.launched) + " " + mApp.launches_count + " " + v.getResources().getString(R.string.times));
        launchCountMenu.setEnabled(false);
        popup.show();
        return true;
    }

    protected void uninstallApp() {
        Intent i = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        i.setData(Uri.parse("package:" + mApp.package_name));
        //TODO StartactivityFor Result to update on gridview ondelete app
        mView.getContext().startActivity(i);

    }

    protected void openAppSettings() {
        Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.setData(Uri.parse("package:" + mApp.package_name));
        //TODO StartactivityFor Result to update on gridview ondelete app
        mView.getContext().startActivity(i);

    }
}
