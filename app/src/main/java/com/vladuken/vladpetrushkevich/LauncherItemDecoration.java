package com.vladuken.vladpetrushkevich;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class LauncherItemDecoration extends RecyclerView.ItemDecoration {

    private final int offset;

    public LauncherItemDecoration(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(offset,offset,offset,offset);
    }
}
