package com.vladuken.vladpetrushkevich.utils;

import android.content.pm.ResolveInfo;

import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.entity.App;

import java.util.Comparator;

public class LaunchCountComparator implements Comparator<ResolveInfo> {

    private final AppDatabase mDatabase;

    public LaunchCountComparator(AppDatabase database) {
        mDatabase = database;
    }

    @Override
    public int compare(ResolveInfo a, ResolveInfo b) {
        App app1 = mDatabase.appDao().getById(a.activityInfo.packageName);
        App app2 = mDatabase.appDao().getById(b.activityInfo.packageName);



        //TODO move this code to database initialisation
        if (app1 == null) {
            app1 = new App(a.activityInfo.packageName, 0,System.currentTimeMillis());
            mDatabase.appDao().insertAll(app1);
        }


        if (app2 == null) {
            app2 = new App(b.activityInfo.packageName, 0, System.currentTimeMillis());
            mDatabase.appDao().insertAll(app2);
        }




        return app2.launches_count - app1.launches_count;
    }
}
