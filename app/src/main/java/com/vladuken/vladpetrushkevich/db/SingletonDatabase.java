package com.vladuken.vladpetrushkevich.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.vladuken.vladpetrushkevich.db.AppDatabase;

public  class SingletonDatabase {

    private static AppDatabase mDatabase;


    public static AppDatabase getInstance(Context context){

        if(mDatabase == null){
            mDatabase = Room.databaseBuilder(context,
                    AppDatabase.class, "app_db")
                    .allowMainThreadQueries()
                    .build();
        }

        return mDatabase;
    }
}
