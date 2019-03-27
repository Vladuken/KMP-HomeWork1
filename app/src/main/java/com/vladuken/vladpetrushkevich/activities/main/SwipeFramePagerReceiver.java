package com.vladuken.vladpetrushkevich.activities.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;

public class SwipeFramePagerReceiver extends BroadcastReceiver {
    public static final String LEFT = "com.vladuken.vladpetrushkevich.LEFT";
    public static final String RIGHT = "com.vladuken.vladpetrushkevich.RIGHT";

    private final ViewPager mViewPager;

    public SwipeFramePagerReceiver(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()== null){
            return;
        }

        String action = intent.getAction();
        int vpItemCount = mViewPager.getAdapter().getCount();
        int currentItem = mViewPager.getCurrentItem();

        if(action.equals(LEFT) && currentItem != 0){
            mViewPager.setCurrentItem(currentItem-1);
        }else if(action.equals(RIGHT) && currentItem != vpItemCount){
            mViewPager.setCurrentItem(currentItem+1);
        }
    }
}
