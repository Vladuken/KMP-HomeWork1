package com.vladuken.vladpetrushkevich.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(primaryKeys = {"screen_position","row_index","column_index"},
        foreignKeys = @ForeignKey(entity = DesktopScreen.class,
                                    parentColumns = "position",
                                    childColumns = "screen_position"))
public class DesktopItem {

    @ColumnInfo(name = "screen_position")
    public int screenPosition;

    @ColumnInfo(name = "row_index")
    public int row;

    @ColumnInfo(name = "column_index")
    public int column;

    @ColumnInfo(name = "item_type")
    public String itemType;

    @ColumnInfo(name = "item_data")
    public String itemData;

    public DesktopItem(int screenPosition, int row, int column, String itemType, String itemData) {
        this.screenPosition = screenPosition;
        this.row = row;
        this.column = column;
        this.itemType = itemType;
        this.itemData = itemData;
    }
}
