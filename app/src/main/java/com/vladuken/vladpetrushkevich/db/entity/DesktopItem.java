package com.vladuken.vladpetrushkevich.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(primaryKeys = {"screen_id","row_index","column_index"},
        foreignKeys = @ForeignKey(entity = DesktopScreen.class,
                                    parentColumns = "id",
                                    childColumns = "screen_id"))
public class DesktopItem {

    @ColumnInfo(name = "screen_id")
    public int screenId;

    @ColumnInfo(name = "row_index")
    public int row;

    @ColumnInfo(name = "column_index")
    public int column;

    @ColumnInfo(name = "item_type")
    public String itemType;

    @ColumnInfo(name = "item_data")
    public String itemData;

    public DesktopItem(int screenId, int row, int column, String itemType, String itemData) {
        this.screenId = screenId;
        this.row = row;
        this.column = column;
        this.itemType = itemType;
        this.itemData = itemData;
    }
}
