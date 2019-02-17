package com.vladuken.vladpetrushkevich;

import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.entity.App;
import com.vladuken.vladpetrushkevich.settings.SettingsActivity;
import com.vladuken.vladpetrushkevich.utils.InstallDateComparator;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LauncherActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    protected BroadcastReceiver mBroadcastReceiver;
    protected SharedPreferences mSharedPreferences;
    protected PrefManager mPrefManager;
    protected AppDatabase mDatabase;

    //TODO change preference to string xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        mPrefManager = new PrefManager(this);
        mSharedPreferences = getSharedPreferences(getString(R.string.preference_file),0);
        boolean theme = mSharedPreferences.getBoolean(getString(R.string.preference_key_theme), false);
        if(!theme){
            setTheme(R.style.AppTheme);
        }else{
            setTheme(R.style.AppThemeDark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        mDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app_db")
                .allowMainThreadQueries()
                .build();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        View headerView =  navigationView.getHeaderView(0);
        ImageView avatar = headerView.findViewById(R.id.avatarHeaderImageView);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),MainActivity.class);
                startActivity(i);
            }
        });

        mRecyclerView = findViewById(R.id.icon_recycler_view);

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
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, portraitSpanCount));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, landscapeSpanCount));
        }

        setupAdapter();
        int offset = getResources().getDimensionPixelOffset(R.dimen.offset);
        mRecyclerView.addItemDecoration(new LauncherItemDecoration(offset));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"TODO",Snackbar.LENGTH_SHORT)
                        .setAction("Action",null)
                        .show();
            }
        });


        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mRecyclerView.getAdapter().notifyDataSetChanged();
                Log.d("AAAAAAAAAAAAA","Help");
//                recreate();
                //TODO set up normal broadcast receiver that would work to detect app install-uninstall
            }
        };
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_UNINSTALL_PACKAGE);
        this.registerReceiver(mBroadcastReceiver, filter);
    }

    private void setupAdapter() {
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);


        int sortMethod = Integer.parseInt(
                mSharedPreferences.getString("com.vladuken.vladpetrushkevich.SORT_METHOD","0"));

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

        }

        //TODO set comparator from settings
//        Collections.sort(activities, new ResolveInfo.DisplayNameComparator(pm));
//        Collections.sort(activities, new InstallDateComparator(pm));

        mRecyclerView.setAdapter(new LauncherAdapter(activities));
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
            PackageManager pm = getPackageManager();

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
        private ResolveInfo mResolveInfo;

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
            PackageManager pm = getPackageManager();
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
                        default:
                            return false;
                    }
                }
            });
            popup.show();
            return true;
        }

        private void uninstallApp(){
            Intent i = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
            i.setData(Uri.parse("package:" + mResolveInfo.activityInfo.packageName));
            //TODO StartactivityFor Result to update on gridview ondelete app
            startActivity(i);
        }

        private void showLaunchCounts(){
            Snackbar.make(this.itemView,
                    "Launched " + mApp.launches_count + " times",
                    Snackbar.LENGTH_SHORT)
                    .setAction("Action",null)
                    .show();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mBroadcastReceiver);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_launcher_activity) {
            // Handle the camera action
        } else if (id == R.id.nav_list_activity) {
            Intent i = new Intent(getApplicationContext(),ListActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_settings) {
            Intent i = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
