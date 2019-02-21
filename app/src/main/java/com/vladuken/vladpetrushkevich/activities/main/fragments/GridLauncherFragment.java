package com.vladuken.vladpetrushkevich.activities.main.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.LauncherItemDecoration;
import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.GridLauncherAdapter;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.SingletonDatabase;
import com.vladuken.vladpetrushkevich.utils.InstallDateComparator;
import com.vladuken.vladpetrushkevich.utils.LaunchCountComparator;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridLauncherFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected SharedPreferences mSharedPreferences;
    protected AppDatabase mDatabase;

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

        setupAdapter();

        int offset = getResources().getDimensionPixelOffset(R.dimen.offset);
        mRecyclerView.addItemDecoration(new LauncherItemDecoration(offset));

        return v;
    }

    private void setupAdapter() {
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);

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

        GridLauncherAdapter launcherAdapter = new GridLauncherAdapter(activities,mDatabase,getContext());
        launcherAdapter.setPopularAppInfo(activities.subList(7,15));

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
    }

    public static GridLauncherFragment newInstance(){
        return new GridLauncherFragment();
    }

}
