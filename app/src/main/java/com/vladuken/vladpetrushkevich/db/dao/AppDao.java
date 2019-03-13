package com.vladuken.vladpetrushkevich.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.vladuken.vladpetrushkevich.db.entity.App;

import java.util.List;

@Dao
public interface AppDao {

    @Query("SELECT * FROM app WHERE package=:packageName")
    App getById(String packageName);

    @Query("SELECT * FROM app")
    List<App> getAll();

    @Update
    void update(App app);

    @Insert
    void insertAll(App... apps);

    @Delete
    void delete(App app);

    @Query("SELECT * FROM app WHERE launches_count != 0")
    Cursor getAllUsed();

}
