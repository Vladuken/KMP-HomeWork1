package com.vladuken.vladpetrushkevich.activities.main.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TimingLogger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.AppBroadcastReceiver;
import com.vladuken.vladpetrushkevich.activities.main.LauncherItemDecoration;
import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.LauncherAdapter;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.SingletonDatabase;
import com.vladuken.vladpetrushkevich.utils.InstallDateComparator;
import com.vladuken.vladpetrushkevich.utils.LaunchCountComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GridLauncherFragment extends Fragment {

    private static final String TAG = "VladSettingsFragment";


    protected RecyclerView mRecyclerView;
    protected SharedPreferences mSharedPreferences;
    protected AppDatabase mDatabase;

    protected AppBroadcastReceiver mBroadcastReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file),0);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_launcher,container,false);

        mDatabase = SingletonDatabase.getInstance(getActivity().getApplicationContext());
        mRecyclerView = v.findViewById(R.id.icon_recycler_view);

        mBroadcastReceiver = new AppBroadcastReceiver(getContext(),mRecyclerView);


        setupAdapter();
        int offset = getResources().getDimensionPixelOffset(R.dimen.offset);
        mRecyclerView.addItemDecoration(new LauncherItemDecoration(offset));

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);

        filter.addDataScheme("package");

        getContext().registerReceiver(mBroadcastReceiver, filter);
//        mRecyclerView.getAdapter().notifyDataSetChanged();

    }

    private void setupAdapter() {

        TimingLogger timings = new TimingLogger(TAG, "Reload activity");

        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);


        timings.addSplit("Start Work");
        int sortMethod = Integer.parseInt(
                mSharedPreferences.getString(getString(R.string.preference_key_sort_method),"0"));
        switch (sortMethod){
            case 0:
                break;
            case 1:
                Collections.sort(activities, new ResolveInfo.DisplayNameComparator(pm));
                break;
            case 2:
                Collections.sort(activities, new ResolveInfo.DisplayNameComparator(pm));
                Collections.reverse(activities);
                break;
            case 3:
                Collections.sort(activities, new InstallDateComparator(pm));
                break;
            case 4:
                Collections.sort(activities, new LaunchCountComparator(mDatabase));
                break;

            default:
                break;
        }

        timings.addSplit("Sorting");
        LauncherAdapter launcherAdapter = new LauncherAdapter(activities,mDatabase,getContext(),true);
        timings.addSplit("LauncherAdapter init");





        boolean showPopApps = mSharedPreferences.getBoolean(getString(R.string.preference_key_popular_apps),false);
        timings.addSplit("Get boolean Popular Apps from SharedPref");

        List<ResolveInfo> popularActivities = new ArrayList<>(activities);
        timings.addSplit("Create new list of popular apps");
        if(showPopApps){
            Collections.sort(popularActivities, new LaunchCountComparator(mDatabase));
            timings.addSplit("Sort popular apps");
            launcherAdapter.setPopularAppInfo(popularActivities);
            timings.addSplit("Set popular app info");
        }



        boolean isCompactLayout =
                mSharedPreferences.getBoolean(
                        getString(R.string.preference_key_layout),
                        false
                );

        int portraitSpanCount;
        int landscapeSpanCount;
        if(isCompactLayout){
            portraitSpanCount = getResources().getInteger(R.integer.compact_portrait_layout_span);
            landscapeSpanCount = getResources().getInteger(R.integer.compact_landscape_layout_span);
        }else {
            portraitSpanCount = getResources().getInteger(R.integer.standard_portrait_layout_span);
            landscapeSpanCount = getResources().getInteger(R.integer.standard_landscape_layout_span);
        }

        int orientation = getResources().getConfiguration().orientation;
        GridLayoutManager gridLayoutManager;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(getContext(),portraitSpanCount);

        }else {
            gridLayoutManager = new GridLayoutManager(getContext(), landscapeSpanCount);
        }


        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return launcherAdapter.isGroupTitle(i) ? gridLayoutManager.getSpanCount() : 1;
            }
        });


        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setAdapter(launcherAdapter);

        timings.dumpToLog();

    }

    @Override
    public void onPause() {
        super.onPause();

//        mRecyclerView.getAdapter().notifyDataSetChanged();
        getContext().unregisterReceiver(mBroadcastReceiver);
    }

    public static GridLauncherFragment newInstance(){
        return new GridLauncherFragment();
    }

}
