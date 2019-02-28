package com.vladuken.vladpetrushkevich.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.vladuken.vladpetrushkevich.db.entity.DesktopItem;

@Dao
public interface DesckopAppDao {

    @Query("SELECT * FROM DesktopItem WHERE screen_id=:screenId AND row_index=:row AND column_index=:column")
    DesktopItem getByIds(int screenId, int row, int column);

    @Query("SELECT * FROM DesktopItem")
    DesktopItem getAll();

    @Update
    void update(DesktopItem desktopItem);

    @Insert
    void insertAll(DesktopItem... desktopItems);

    @Delete
    void delete(DesktopItem desktopItem);
}
