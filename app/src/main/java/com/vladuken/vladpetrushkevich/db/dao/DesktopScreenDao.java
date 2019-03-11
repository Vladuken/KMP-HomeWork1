package com.vladuken.vladpetrushkevich.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.vladuken.vladpetrushkevich.db.entity.DesktopScreen;

import java.util.List;

@Dao
public interface DesktopScreenDao {

    @Query("SELECT * FROM desktopscreen WHERE position=:position")
    DesktopScreen getByPosition(int position);

    @Query("SELECT * FROM desktopscreen")
    List<DesktopScreen> getAll();

    @Update
    void update(DesktopScreen screen);

    @Insert
    void insertAll(DesktopScreen... screens);

    @Delete
    void delete(DesktopScreen screen);
}
