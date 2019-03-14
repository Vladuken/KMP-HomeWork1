package com.vladuken.contentproviderapp;

import android.support.annotation.NonNull;

public class App {
    @NonNull
    public static final String PACKAGE_NAME = "package";
    public String package_name;

    public static final String LAUNCH_COUNT = "launches_count";

    public int launches_count;

    public static final String LAST_TIME_LAUNCHED = "last_time_launched";
    public long last_time_launched;

    public App(String package_name, int launches_count, long last_time_launched) {
        this.package_name = package_name;
        this.launches_count = launches_count;
        this.last_time_launched = last_time_launched;
    }

    @Override
    public String toString() {
        return package_name;
    }
}
