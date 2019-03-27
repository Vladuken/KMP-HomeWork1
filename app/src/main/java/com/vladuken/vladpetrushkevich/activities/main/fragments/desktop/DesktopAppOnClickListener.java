package com.vladuken.vladpetrushkevich.activities.main.fragments.desktop;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.entity.App;
import com.yandex.metrica.YandexMetrica;

public class DesktopAppOnClickListener implements View.OnClickListener {

    private final AppDatabase mDatabase;
    private App mApp;

    public DesktopAppOnClickListener(AppDatabase database, App app) {
        mDatabase = database;
        mApp = app;
    }

    @Override
    public void onClick(View v) {
        mApp.launches_count++;
        mApp.last_time_launched = System.currentTimeMillis();
        mDatabase.appDao().update(mApp);
        YandexMetrica.reportEvent("App start on icon click in desktop page");

        PackageManager pm = v.getContext().getPackageManager();

        Intent i = pm.getLaunchIntentForPackage(mApp.package_name);
        if(i == null){
            Snackbar.make(v,v.getResources().getString(R.string.desktop_item_remove),Snackbar.LENGTH_LONG).show();
            return;
        }
        v.getContext().startActivity(i);
    }
}
