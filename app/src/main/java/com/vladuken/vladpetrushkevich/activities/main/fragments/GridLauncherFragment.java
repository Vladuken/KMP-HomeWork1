package com.vladuken.vladpetrushkevich.activities.main.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.LauncherItemDecoration;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.SingletonDatabase;
import com.vladuken.vladpetrushkevich.db.entity.App;
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
        boolean theme = mSharedPreferences.getBoolean(getString(R.string.preference_key_theme), false);
        if(!theme){
            getActivity().setTheme(R.style.AppTheme);
        }else{
            getActivity().setTheme(R.style.AppThemeDark);
        }
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

        mRecyclerView.setAdapter(new LauncherAdapter(activities));
    }

    public static GridLauncherFragment newInstance(){
        return new GridLauncherFragment();
    }

    public class LauncherAdapter extends RecyclerView.Adapter<LauncherViewHolder>{
        private final List<ResolveInfo> mInstalledAppInfo;
        private final Map<ResolveInfo,Drawable> mIcons;

        public LauncherAdapter(List<ResolveInfo> installedAppsInfo) {
            mInstalledAppInfo =  installedAppsInfo;
            mIcons = new HashMap<>();
        }



        @NonNull
        @Override
        public LauncherViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int position) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_icon_view,parent,false);

            return new LauncherViewHolder(view);
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

    public class LauncherViewHolder extends RecyclerView.ViewHolder {
        protected ResolveInfo mResolveInfo;

        private final ImageView mAppIcon;
        private final TextView mAppTitle;

        private App mApp;

        public LauncherViewHolder(@NonNull View itemView) {
            super(itemView);
            mAppIcon = itemView.findViewById(R.id.app_icon_with_title);
            mAppTitle = itemView.findViewById(R.id.app_title);
            itemView.setOnClickListener(this::onClick);
            itemView.setOnLongClickListener(this::showPopUp);

        }

        public void bind(ResolveInfo resolveInfo,Drawable icon) {

            mResolveInfo = resolveInfo;
            PackageManager pm = getActivity().getPackageManager();
            String appName = mResolveInfo.loadLabel(pm).toString();


            //TODO move all this code to Adaper
            mApp = mDatabase.appDao().getById(mResolveInfo.activityInfo.packageName);
            if(mApp == null){
                mApp = new App(mResolveInfo.activityInfo.packageName,0);
                mDatabase.appDao().insertAll(mApp);
            }
            //

            mAppIcon.setImageDrawable(icon);
            mAppTitle.setText(appName);
        }

        public void onClick(View v) {
            ActivityInfo activityInfo = mResolveInfo.activityInfo;

            Intent i = new Intent(Intent.ACTION_MAIN)
                    .setClassName(activityInfo.applicationInfo.packageName,activityInfo.name)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            mApp.launches_count++;
            mDatabase.appDao().update(mApp);

            startActivity(i);
        }

        public boolean showPopUp(View v){
            PopupMenu popup = new PopupMenu(v.getContext(),v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.app_popup,popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_uninstall_app:
                            uninstallApp();
                            return true;
                        case R.id.action_count_app_launches:
                            showLaunchCounts();
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popup.show();
            return true;
        }

        protected void uninstallApp(){
            Intent i = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
            i.setData(Uri.parse("package:" + mResolveInfo.activityInfo.packageName));
            //TODO StartactivityFor Result to update on gridview ondelete app
            startActivity(i);
        }

        protected void showLaunchCounts(){
            Snackbar.make(this.itemView,
                    "Launched " + mApp.launches_count + " times",
                    Snackbar.LENGTH_SHORT)
                    .setAction("Action",null)
                    .show();

        }

    }



}
