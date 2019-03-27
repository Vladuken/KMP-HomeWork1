package com.vladuken.vladpetrushkevich.activities.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.DragEvent;
import android.view.View;

import com.vladuken.vladpetrushkevich.utils.AnimateUtils;

public class SwipeFramePagerListener implements View.OnDragListener {

    private static final int COLOR = Color.argb(220,255,255,255);
    private static final int ACCENT_COLOR = Color.argb(90,255,255,255);
    private static final int DURATION = 300;

    private final Handler handler = new Handler();
    private final Context mContext;
    private final Intent mIntent;
    private final Runnable mRunnable;

    public SwipeFramePagerListener(Context context, Intent intent) {
        mContext = context;
        mIntent = intent;
        mRunnable = () -> mContext.sendBroadcast(mIntent);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                AnimateUtils.animateBackground(v,Color.TRANSPARENT, ACCENT_COLOR,DURATION);
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                AnimateUtils.animateBackground(v, ACCENT_COLOR, COLOR,DURATION);
                handler.postDelayed(mRunnable, 600);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                AnimateUtils.animateBackground(v, COLOR, ACCENT_COLOR,DURATION);
                handler.removeCallbacks(mRunnable);
                break;
            case DragEvent.ACTION_DROP:
                handler.removeCallbacks(mRunnable);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackgroundColor(Color.TRANSPARENT);
                AnimateUtils.animateBackground(v, ACCENT_COLOR,Color.TRANSPARENT,DURATION);
                break;
            default:
                break;
        }

        return true;
    }

}
