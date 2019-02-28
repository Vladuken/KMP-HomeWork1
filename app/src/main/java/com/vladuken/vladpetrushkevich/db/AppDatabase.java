package com.vladuken.vladpetrushkevich.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.vladuken.vladpetrushkevich.db.dao.AppDao;
import com.vladuken.vladpetrushkevich.db.dao.DesktopScreenDao;
import com.vladuken.vladpetrushkevich.db.entity.App;
import com.vladuken.vladpetrushkevich.db.entity.DesktopScreen;
import com.vladuken.vladpetrushkevich.db.entity.DesktopApp;

@Database(entities = {App.class, DesktopScreen.class, DesktopApp.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao appDao();

    public abstract DesktopScreenDao desktopScreenDao();
}
