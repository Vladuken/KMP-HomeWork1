package com.vladuken.vladpetrushkevich.activities.main.fragments.desktop;

import android.view.View;

public class DesktopItemOnLongClickListener implements View.OnLongClickListener{
    protected DesktopItemViewHolder mViewHolder;

    public DesktopItemOnLongClickListener(DesktopItemViewHolder viewHolder) {
        mViewHolder = viewHolder;
    }

    @Override
    public boolean onLongClick(View v) {



        return true;
    }
}
