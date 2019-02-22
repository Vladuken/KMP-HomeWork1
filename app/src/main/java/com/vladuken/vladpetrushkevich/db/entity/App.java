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

    public App(String package_name, int launches_count) {
        this.package_name = package_name;
        this.launches_count = launches_count;
    }
}
