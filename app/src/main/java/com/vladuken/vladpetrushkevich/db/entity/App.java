package com.vladuken.vladpetrushkevich.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class App {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "package")
    public String package_name;

    @ColumnInfo(name ="launches_count")
    public int launches_count;

    @ColumnInfo(name = "last_time_launched")
    public long last_time_launched;

    public App(String package_name, int launches_count, long last_time_launched) {
        this.package_name = package_name;
        this.launches_count = launches_count;
        this.last_time_launched = last_time_launched;
    }
}
