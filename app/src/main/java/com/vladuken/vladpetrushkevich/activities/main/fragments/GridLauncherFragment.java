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
//        boolean theme = mSharedPreferences.getBoolean(getString(R.string.preference_key_theme), false);
//        if(!theme){
//            getActivity().setTheme(R.style.AppTheme);
//        }else{
//            getActivity().setTheme(R.style.AppThemeDark);
//        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_launcher,container,false);

        mDatabase = SingletonDatabase.getInstance(getActivity().getApplicationContext());
        mRecyclerView = v.findViewById(R.id.icon_recycler_view);

        int portraitSpanCount =
                mSharedPreferences.getInt(
                        getString(R.string.preference_portrait_rows),
                        getResources().getInteger(R.integer.standard_portrait_layout_span)
                );
        int landscapeSpanCount =
                mSharedPreferences.getInt(
                        getString(R.string.preference_landscape_rows),
                        getResources().getInteger(R.integer.standard_landscape_layout_span)
                );

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), portraitSpanCount));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), landscapeSpanCount));
        }

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

        mRecyclerView.setAdapter(new GridLauncherAdapter(activities));
    }

    public static GridLauncherFragment newInstance(){
        return new GridLauncherFragment();
    }

    public class GridLauncherAdapter extends RecyclerView.Adapter<LauncherViewHolder>{
        private final List<ResolveInfo> mInstalledAppInfo;
        private final Map<ResolveInfo,Drawable> mIcons;

        public GridLauncherAdapter(List<ResolveInfo> installedAppsInfo) {
            mInstalledAppInfo =  installedAppsInfo;
            mIcons = new HashMap<>();
        }



        @NonNull
        @Override
        public LauncherViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int position) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_icon_view,parent,false);

            return new LauncherViewHolder(view,mDatabase,R.id.grid_app_icon,R.id.grid_app_title);
        }

        @Override
        public void onBindViewHolder(@NonNull LauncherViewHolder viewHolder, int position) {
            ResolveInfo resolveInfo = mInstalledAppInfo.get(position);
            PackageManager pm = getActivity().getPackageManager();

            if(mIcons.get(resolveInfo) == null){
                mIcons.put(resolveInfo,mInstalledAppInfo.get(position).loadIcon(pm));
            }

            viewHolder.bind(resolveInfo,mIcons.get(resolveInfo));
        }

        @Override
        public int getItemCount() {
            return mInstalledAppInfo.size();
        }
    }
}
