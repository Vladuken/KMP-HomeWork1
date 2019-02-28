package com.vladuken.vladpetrushkevich.activities.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.GridLayoutManager;

import com.vladuken.vladpetrushkevich.activities.main.fragments.GridLauncherFragment;
import com.vladuken.vladpetrushkevich.activities.main.fragments.ListLauncherFragment;
import com.vladuken.vladpetrushkevich.activities.main.fragments.SettingsFragment;
import com.vladuken.vladpetrushkevich.activities.main.fragments.desktop.DesktopFragment;

public class LauncherPagerAdapter extends FragmentPagerAdapter {

    public LauncherPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return GridLauncherFragment.newInstance();
            case 1:
                return ListLauncherFragment.newInstance();
            case 2:
                return SettingsFragment.newInstance();
            case 3:
                return DesktopFragment.newInstance(position);
            case 4:
                return DesktopFragment.newInstance(position);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}