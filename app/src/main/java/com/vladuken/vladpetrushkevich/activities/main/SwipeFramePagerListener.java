package com.vladuken.vladpetrushkevich.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.DragEvent;
import android.view.View;

public class SwipeFramePagerListener implements View.OnDragListener {

    final Handler handler = new Handler();

    final Context mContext;
    final Intent mIntent;
    final Runnable mRunnable;
    public SwipeFramePagerListener(Context context, Intent intent) {
        mContext = context;
        mIntent = intent;

        mRunnable = new Runnable() {
            @Override
            public void run() {
                mContext.sendBroadcast(mIntent);
            }
        };
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_ENTERED:
                handler.postDelayed(mRunnable, 600);

                break;
            case DragEvent.ACTION_DRAG_EXITED:

                handler.removeCallbacks(mRunnable);
                break;
            default:
                break;
        }

        return true;
    }

}
