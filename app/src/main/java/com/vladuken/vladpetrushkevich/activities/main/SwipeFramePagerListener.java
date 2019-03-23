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
            case DragEvent.ACTION_DRAG_STARTED:
                break;
//                v.setVisibility(View.VISIBLE);
//                break;
            case DragEvent.ACTION_DRAG_ENTERED:
//                v.setVisibility(View.VISIBLE);
                handler.postDelayed(mRunnable, 600);

                break;
            case DragEvent.ACTION_DRAG_EXITED:

                handler.removeCallbacks(mRunnable);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
//                v.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }

        return true;
    }

}
