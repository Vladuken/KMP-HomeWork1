package com.vladuken.vladpetrushkevich.utils;

import android.content.pm.ResolveInfo;

import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.entity.App;

import java.util.Comparator;

public class LaunchCountComparator implements Comparator<ResolveInfo> {

    AppDatabase mDatabase;

    public LaunchCountComparator(AppDatabase database) {
        mDatabase = database;
    }

    @Override
    public int compare(ResolveInfo a, ResolveInfo b) {
        App app1 = mDatabase.appDao().getById(a.activityInfo.packageName);
        App app2 = mDatabase.appDao().getById(b.activityInfo.packageName);

        return (app2.launches_count - app1.launches_count);
    }
}
