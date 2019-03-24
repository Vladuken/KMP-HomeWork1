package com.vladuken.vladpetrushkevich.activities.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.DragEvent;
import android.view.View;

import com.vladuken.vladpetrushkevich.utils.AnimateUtils;

public class SwipeFramePagerListener implements View.OnDragListener {

    private static final int COLORTO = Color.argb(180,255,255,255);


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
//                v.setBackgroundColor(Color.argb(55,255,255,255));
//                v.animate().alpha(1f).start();
                AnimateUtils.animateBackground(v,Color.TRANSPARENT,COLORTO,300);
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
            case DragEvent.ACTION_DROP:
                handler.removeCallbacks(mRunnable);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackgroundColor(Color.TRANSPARENT);
                AnimateUtils.animateBackground(v,COLORTO,Color.TRANSPARENT,300);

//                v.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }

        return true;
    }

}
