package com.vladuken.vladpetrushkevich.activities.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;

public class SwipeFramePagerReceiver extends BroadcastReceiver {
    public static final String LEFT = "com.vladuken.vladpetrushkevich.LEFT";

    public static final String RIGHT = "com.vladuken.vladpetrushkevich.RIGHT";

    ViewPager mViewPager;

    public SwipeFramePagerReceiver(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()== null)
            return;

        if(intent.getAction().equals(LEFT)){
            if(mViewPager.getCurrentItem() != 0){
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
            }
        }else if(intent.getAction().equals(RIGHT)){
            if(mViewPager.getCurrentItem() != mViewPager.getAdapter().getCount()){
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
            }
        }
    }
}
