package com.vladuken.vladpetrushkevich.db;

import android.arch.persistence.room.Room;
import android.content.Context;

public final class SingletonDatabase {

    private static AppDatabase mDatabase;

    private SingletonDatabase() {
    }

    public static AppDatabase getInstance(Context context){


        synchronized (AppDatabase.class){
            if(mDatabase == null){
                mDatabase = Room.databaseBuilder(context,
                        AppDatabase.class, "app_db")
                        .allowMainThreadQueries()
                        .build();
            }
        }

        return mDatabase;
    }
}
