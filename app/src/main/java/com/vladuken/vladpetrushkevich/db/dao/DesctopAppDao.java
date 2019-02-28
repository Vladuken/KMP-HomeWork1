package com.vladuken.vladpetrushkevich.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.vladuken.vladpetrushkevich.db.entity.DesktopApp;

@Dao
public interface DesctopAppDao {

    @Query("SELECT * FROM desktopapp WHERE screen_id=:screenId AND row_index=:row AND column_index=:column")
    DesktopApp getByIds(int screenId, int row,int column);

    @Query("SELECT * FROM desktopapp")
    DesktopApp getAll();

    @Update
    void update(DesktopApp desktopApp);

    @Insert
    void insertAll(DesktopApp... desktopApps);

    @Delete
    void delete(DesktopApp desktopApp);
}
