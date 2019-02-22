package com.vladuken.vladpetrushkevich.activities.main.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListLauncherFragment extends Fragment {

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
        View v = inflater.inflate(R.layout.activity_list,container,false);

        mDatabase = SingletonDatabase.getInstance(getActivity().getApplicationContext());
        mRecyclerView = v.findViewById(R.id.list_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mRecyclerView.setAdapter(mAdapter);

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
        GridLauncherAdapter launcherAdapter = new GridLauncherAdapter(activities,mDatabase,getContext(),false);


        boolean showPopApps = mSharedPreferences.getBoolean(getString(R.string.preference_key_popular_apps),false);

        List<ResolveInfo> popularActivities = new ArrayList<>(activities);
        if(showPopApps){
            Collections.sort(popularActivities, new LaunchCountComparator(mDatabase));
            launcherAdapter.setPopularAppInfo(popularActivities);
        }

        mRecyclerView.setAdapter(launcherAdapter);
    }


//    public class ListLauncherAdapter extends RecyclerView.Adapter<LauncherViewHolder>{
//
//        private final List<ResolveInfo> mInstalledAppInfo;
//        private final Map<ResolveInfo,Drawable> mIcons;
//
//        public ListLauncherAdapter(List<ResolveInfo> installedAppInfo) {
//            mInstalledAppInfo = installedAppInfo;
//            mIcons = new HashMap<>();
//        }
//
//        @NonNull
//        @Override
//        public LauncherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.item_list_view,parent,false);
//
//            return new LauncherViewHolder(view, mDatabase,R.id.list_app_icon,R.id.list_app_title);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull LauncherViewHolder viewHolder, int position) {
//            ResolveInfo resolveInfo = mInstalledAppInfo.get(position);
//            PackageManager pm = getActivity().getPackageManager();
//
//            if(mIcons.get(resolveInfo) == null){
//                mIcons.put(resolveInfo,mInstalledAppInfo.get(position).loadIcon(pm));
//            }
//
//            viewHolder.bind(resolveInfo,mIcons.get(resolveInfo));
//        }
//
//        @Override
//        public int getItemCount() {
//            return mInstalledAppInfo.size();
//        }
//    }
//
//
//

    public static ListLauncherFragment newInstance(){
        return new ListLauncherFragment();
    }

}
