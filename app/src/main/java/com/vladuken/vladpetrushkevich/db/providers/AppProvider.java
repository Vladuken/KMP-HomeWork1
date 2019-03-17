package com.vladuken.vladpetrushkevich.db.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.SingletonDatabase;
import com.vladuken.vladpetrushkevich.db.entity.App;

public class AppProvider extends ContentProvider {


    private AppDatabase mDatabase;
    private static final UriMatcher sUriMatcher = buildUriMatcher();


    private static final int ALL_USED_APPS = 100;
    private static final int LAST_LAUNCHED_APP = 101;

    private static final int UPDATE_APP = 102;



    @Override
    public boolean onCreate() {
        mDatabase = SingletonDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,@Nullable String[] projection,@Nullable String selection,@Nullable String[] selectionArgs,@Nullable String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case LAST_LAUNCHED_APP:
                retCursor = mDatabase.appDao().getLastLaunched();
                break;
            case ALL_USED_APPS:
                retCursor = mDatabase.appDao().getAllUsed();
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return retCursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri,@Nullable String selection,@Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri,@Nullable ContentValues values, @Nullable String selection,@Nullable String[] selectionArgs) {

        if(sUriMatcher.match(uri) == UPDATE_APP){
            SingletonDatabase.getInstance(getContext()).appDao()
                    .update(
                            new App(values.getAsString(App.PACKAGE_NAME),
                                    values.getAsInteger(App.LAUNCH_COUNT),
                                    values.getAsLong(App.LAST_TIME_LAUNCHED)));

            return 1;
        }

        return 0;
    }

    private static UriMatcher buildUriMatcher(){
        String authority = AppInfoProviderContract.AUTHORITY;

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(authority,AppInfoProviderContract.PATH_ALL_APPS,ALL_USED_APPS);
        matcher.addURI(authority,AppInfoProviderContract.PATH_LAST_LAUNCHED_APP,LAST_LAUNCHED_APP);
        matcher.addURI(authority,AppInfoProviderContract.PATH_UPDATE_APP,UPDATE_APP);

        return matcher;
    }
}
