package com.vladuken.vladpetrushkevich.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

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

    public DesktopScreen(int viewPagerPosition, int rowCount, int columntCount) {
        this.viewPagerPosition = viewPagerPosition;
        this.rowCount = rowCount;
        this.columntCount = columntCount;
    }
}
