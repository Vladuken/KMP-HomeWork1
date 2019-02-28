package com.vladuken.vladpetrushkevich.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class DesktopScreen {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "position")
    public int viewPagerPosition;

    @ColumnInfo(name = "rows")
    public int rowCount;

    @ColumnInfo(name = "columns")
    public int columntCount;


}
