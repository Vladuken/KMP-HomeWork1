package com.vladuken.vladpetrushkevich.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = DesktopScreen.class,
                                    parentColumns = "id",
                                    childColumns = "screen_id"))
public class DesktopApp {

    @PrimaryKey
    @ColumnInfo(name = "screen_id")
    public int screenId;

    @PrimaryKey
    @ColumnInfo(name = "row_index")
    public int row;

    @PrimaryKey
    @ColumnInfo(name = "column_index")
    public int column;

    @ColumnInfo(name = "item_type")
    public String itemType;

    public DesktopApp(int screenId, int row, int column, String itemType) {
        this.screenId = screenId;
        this.row = row;
        this.column = column;
        this.itemType = itemType;
    }
}
