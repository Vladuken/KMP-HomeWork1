package com.vladuken.contentproviderapp;

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
    public static final String PATH_UPDATE_APP = "updateApp";

//    public static final class AppInfoColumn implements BaseColumns{
//
//        // Content URI represents the base location for the table
//        public static final Uri CONTENT_URI =
//                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LAST_LAUNCHED_APP).build();
//
//
//        // Define the table schema
//        public static final String TABLE_NAME = "appInfoTable";
//        public static final String COLUMN_APP_TITLE = "appTitle";
//        public static final String COLUMN_PACKAGE_NAME = "appPackageName";
//        public static final String COLUMN_LAUNCHED = "appLaunchedTimes";
//
//        // Define a function to build a URI to find a specific movie by it's identifier
//        public static Uri buildMovieUri(long id){
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }
//    }
}
