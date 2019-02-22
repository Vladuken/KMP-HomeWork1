package com.vladuken.vladpetrushkevich.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.vladuken.vladpetrushkevich.db.dao.AppDao;
import com.vladuken.vladpetrushkevich.db.entity.App;

@Database(entities = {App.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao appDao();
}
