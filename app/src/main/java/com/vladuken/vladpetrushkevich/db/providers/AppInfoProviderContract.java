package com.vladuken.vladpetrushkevich.db.providers;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public final class AppInfoProviderContract {
    public static final String AUTHORITY =
            "com.vladuken.vladpetrushkevich.providers.appinfos";

    public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    public static final String PATH_ALL_APPS = "apps";
    public static final String PATH_LAST_LAUNCHED_APP = "lastLaunchedApp";
}
