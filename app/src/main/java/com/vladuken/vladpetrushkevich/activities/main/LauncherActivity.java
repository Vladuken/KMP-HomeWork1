package com.vladuken.vladpetrushkevich.activities.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.MainActivity;
import com.vladuken.vladpetrushkevich.activities.main.fragments.GridLauncherFragment;
import com.vladuken.vladpetrushkevich.activities.main.fragments.ListLauncherFragment;
import com.vladuken.vladpetrushkevich.fragment.SettingsFragment;

public class LauncherActivity extends AppCompatActivity {

    protected SharedPreferences mSharedPreferences;
    protected FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPreferences = getSharedPreferences(getString(R.string.preference_file),0);
        boolean theme = mSharedPreferences.getBoolean(getString(R.string.preference_key_theme), false);
        if(!theme){
            setTheme(R.style.AppTheme);
        }else{
            setTheme(R.style.AppThemeDark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        View headerView =  navigationView.getHeaderView(0);
        ImageView avatar = headerView.findViewById(R.id.avatarHeaderImageView);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        mFrameLayout = findViewById(R.id.launcher_container_fragments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.launcher_container_fragments,new GridLauncherFragment())
                .commit();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"TODO",Snackbar.LENGTH_SHORT)
                        .setAction("Action",null)
                        .show();
            }
        });

        fab.hide();


//        mBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                mRecyclerView.getAdapter().notifyDataSetChanged();
//                Log.d("AAAAAAAAAAAAA","Help");
////                recreate();
//                //TODO set up normal broadcast receiver that would work to detect app install-uninstall
//            }
//        };
//        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        filter.addAction(Intent.ACTION_UNINSTALL_PACKAGE);
//        this.registerReceiver(mBroadcastReceiver, filter);
    }

//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        this.unregisterReceiver(mBroadcastReceiver);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_launcher_activity) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.launcher_container_fragments,GridLauncherFragment.newInstance())
                    .commit();
        } else if (id == R.id.nav_list_activity) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.launcher_container_fragments, ListLauncherFragment.newInstance())
                    .commit();
        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.launcher_container_fragments, SettingsFragment.newInstance())
                    .commit();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
