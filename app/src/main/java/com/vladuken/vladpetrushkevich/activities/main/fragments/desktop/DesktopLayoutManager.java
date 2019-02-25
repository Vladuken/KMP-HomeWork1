package com.vladuken.vladpetrushkevich.activities.main.fragments.desktop;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class DesktopLayoutManager extends GridLayoutManager {

    private int mRowCount;

    public DesktopLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DesktopLayoutManager(Context context, int spanCount, int rowCount) {
        super(context, spanCount);
        mRowCount = rowCount;
    }

    public DesktopLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }



    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return null;
    }


    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
