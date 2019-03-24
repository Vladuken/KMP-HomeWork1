package com.vladuken.vladpetrushkevich.activities.main.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.AppBroadcastReceiver;
import com.vladuken.vladpetrushkevich.activities.main.BackgroundReceiver;
import com.vladuken.vladpetrushkevich.activities.main.LauncherItemDecoration;
import com.vladuken.vladpetrushkevich.activities.main.SwipeFramePagerListener;
import com.vladuken.vladpetrushkevich.activities.main.SwipeFramePagerReceiver;
import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.LauncherAdapter;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.SingletonDatabase;
import com.vladuken.vladpetrushkevich.utils.BackgroundManager;
import com.vladuken.vladpetrushkevich.utils.InstallDateComparator;
import com.vladuken.vladpetrushkevich.utils.LaunchCountComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListLauncherFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected SharedPreferences mSharedPreferences;
    protected AppDatabase mDatabase;

    protected BackgroundReceiver mBackgroundReceiver;

    protected AppBroadcastReceiver mBroadcastReceiver;

    protected List<ResolveInfo> mInstalledApps;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file),0);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_list,container,false);

        mDatabase = SingletonDatabase.getInstance(getActivity().getApplicationContext());
        mRecyclerView = v.findViewById(R.id.list_recycler_view);

        mBroadcastReceiver = new AppBroadcastReceiver(getContext(),mRecyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setupAdapter();

        int offset = getResources().getDimensionPixelOffset(R.dimen.offset);
        mRecyclerView.addItemDecoration(new LauncherItemDecoration(offset));


        String fullpath = "";

        if(mSharedPreferences.getBoolean(getString(R.string.preference_one_background_for_all_screens),false)){
            fullpath = mRecyclerView.getContext().getFilesDir().toString()  + getString(R.string.global_image_title) + ".png";
        }else {
            fullpath = mRecyclerView.getContext().getFilesDir().toString()  + this.getClass().toString() + ".png";
        }


        BackgroundManager.setupBackground(v,fullpath);
        mBackgroundReceiver = new BackgroundReceiver(v,fullpath);



        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);

        filter.addDataScheme("package");

        View leftBar = v.findViewById(R.id.left_vertical_viewpager_scroller);
        leftBar.setOnDragListener(new SwipeFramePagerListener(
                getContext(),
                new Intent(SwipeFramePagerReceiver.LEFT)));

        View rightBar = v.findViewById(R.id.right_vertical_viewpager_scroller);
        rightBar.setOnDragListener(new SwipeFramePagerListener(
                getContext(),
                new Intent(SwipeFramePagerReceiver.RIGHT)));


        getContext().registerReceiver(mBroadcastReceiver, filter);


        return v;
    }

    private void setupAdapter() {

        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        mInstalledApps = pm.queryIntentActivities(startupIntent, 0);


        int sortMethod = Integer.parseInt(
                mSharedPreferences.getString(getString(R.string.preference_key_sort_method),"0"));
        switch (sortMethod){
            case 0:
                break;
            case 1:
                Collections.sort(mInstalledApps, new ResolveInfo.DisplayNameComparator(pm));
                break;
            case 2:
                Collections.sort(mInstalledApps, new ResolveInfo.DisplayNameComparator(pm));
                Collections.reverse(mInstalledApps);
                break;
            case 3:
                Collections.sort(mInstalledApps, new InstallDateComparator(pm));
                break;
            case 4:
                Collections.sort(mInstalledApps, new LaunchCountComparator(mDatabase));
                break;

            default:
                break;
        }

        int popularLineSize = Integer.parseInt(
                mSharedPreferences.getString(getString(R.string.preference_popular_apps_line_size_key),"1")
        );
        LauncherAdapter launcherAdapter = new LauncherAdapter(mInstalledApps,mDatabase,false,popularLineSize);


        boolean showPopApps = mSharedPreferences.getBoolean(getString(R.string.preference_key_popular_apps),false);

        List<ResolveInfo> popularActivities = new ArrayList<>(mInstalledApps);
        if(showPopApps){
            Collections.sort(popularActivities, new LaunchCountComparator(mDatabase));
            launcherAdapter.setPopularAppInfo(popularActivities);
        }

        mRecyclerView.setAdapter(launcherAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(BackgroundReceiver.UPDATE_BACKGROUND);
        filter.addAction(BackgroundReceiver.UPDATE_BACKGROUND_ONCE);
        getContext().registerReceiver(mBackgroundReceiver,filter);
        getContext().sendBroadcast(new Intent(BackgroundReceiver.UPDATE_BACKGROUND));
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(mBackgroundReceiver);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(mBroadcastReceiver);

    }

    public static ListLauncherFragment newInstance(){
        return new ListLauncherFragment();
    }

}
