package com.vladuken.vladpetrushkevich.activities.main.fragments.desktop;

import android.view.View;

import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.entity.DesktopItem;

public class DesktopEmptyOnLongClickListener implements View.OnLongClickListener {


    private AppDatabase mDatabase;
    private DesktopItem mDesktopItem;

    public DesktopEmptyOnLongClickListener(AppDatabase database, DesktopItem desktopItem) {
        mDatabase = database;
        mDesktopItem = desktopItem;
    }

    @Override
    public boolean onLongClick(View v) {


        return false;
    }
}


