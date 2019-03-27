package com.vladuken.vladpetrushkevich.activities.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.fragments.GridLauncherFragment;
import com.vladuken.vladpetrushkevich.activities.main.fragments.ListLauncherFragment;
import com.vladuken.vladpetrushkevich.activities.main.fragments.SettingsFragment;
import com.vladuken.vladpetrushkevich.activities.main.fragments.desktop.DesktopFragment;

public class LauncherPagerAdapter extends FragmentPagerAdapter {

    private int mDesktopCount;

    public LauncherPagerAdapter(FragmentManager fm, int desktopCount) {
        super(fm);
        mDesktopCount = desktopCount;
    }

    @Override
    public Fragment getItem(int position) {
//        switch (position){
//            case 0:
//                return GridLauncherFragment.newInstance();
//            case 1:
//                return DesktopFragment.newInstance(position);
//            case 2:
//                return ListLauncherFragment.newInstance();
//            case 3:
//                return SettingsFragment.newInstance();
//            default:
//                return null;
//        }
//
        if(position == 0){
            return GridLauncherFragment.newInstance();
        }else if(position == getCount() - 1){
            return SettingsFragment.newInstance();
        }else if(position == getCount() - 2){
            return ListLauncherFragment.newInstance();
        }else {
            return DesktopFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return mDesktopCount + 3;
    }
}
