package com.vladuken.vladpetrushkevich.activities.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.crashlytics.android.Crashlytics;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.microsoft.appcenter.distribute.Distribute;
import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.fragments.GridLauncherFragment;
import com.vladuken.vladpetrushkevich.activities.main.fragments.ListLauncherFragment;
import com.vladuken.vladpetrushkevich.activities.main.fragments.SettingsFragment;
import com.vladuken.vladpetrushkevich.activities.profile.ProfilePageActivity;
import com.vladuken.vladpetrushkevich.utils.ThemeChanger;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import io.fabric.sdk.android.Fabric;

public class LauncherActivity extends AppCompatActivity {


    private static final String CHECKED_NAV_ID = "CheckedNavId";
    protected SharedPreferences mSharedPreferences;
    protected FrameLayout mFrameLayout;

    protected NavigationView mNavigationView;

    protected Toolbar mToolbar;
    protected ViewPager mFramePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPreferences = getSharedPreferences(getString(R.string.preference_file), 0);
        boolean theme = mSharedPreferences.getBoolean(getString(R.string.preference_key_theme), false);
        ThemeChanger.onCreateSetTheme(this, theme);
        super.onCreate(savedInstanceState);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        Fabric.with(this, new Crashlytics());
        AppCenter.start(getApplication(),
                "77770b97-7fc5-4620-b874-95c26bb3e37c",
                Analytics.class,
                Crashes.class,
                Distribute.class);


        setContentView(R.layout.activity_nav_drawer);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mNavigationView.setCheckedItem(R.id.nav_launcher_activity);
                onNavigationItemSelected(R.id.nav_launcher_activity);
            }
        });

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        View headerView = mNavigationView.getHeaderView(0);

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), ProfilePageActivity.class);
                startActivity(i);
//                mNavigationView.setCheckedItem(R.id.nav_none);
                onNavigationItemSelected(mNavigationView.getMenu().findItem(R.id.nav_none));
            }
        });

        //mFrameLayout = findViewById(R.id.launcher_container_fragments);

        NestedScrollView nestedScrollView = findViewById(R.id.nested_scrollview);
        nestedScrollView.setFillViewport(true);


        FragmentManager fm = getSupportFragmentManager();
        mFramePager = findViewById(R.id.launcher_fragment_viewpager);
        mFramePager.setAdapter(new LauncherPagerAdapter(fm));
        mFramePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
//                mNavigationView.setCheckedItem(i);
                mNavigationView.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mNavigationView.setCheckedItem(R.id.nav_launcher_activity);
        onNavigationItemSelected(mNavigationView.getMenu().findItem(R.id.nav_launcher_activity));

        if (savedInstanceState != null) {
            int menuId = savedInstanceState.getInt(CHECKED_NAV_ID, R.id.nav_list_activity);
            MenuItem menuItem = mNavigationView.getMenu().findItem(menuId);

            mNavigationView.setCheckedItem(menuId);
            onNavigationItemSelected(menuItem);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CHECKED_NAV_ID,mNavigationView.getCheckedItem().getItemId());
        super.onSaveInstanceState(outState);
    }

//    @Override
//    public void onBackPressed() {
//        //TODO add normal onbackpressed implementation
//        super.onBackPressed();
//    }


    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();

        onNavigationItemSelected(id);
        return true;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(int id) {

        if(id == R.id.nav_launcher_activity){
            mFramePager.setCurrentItem(0);
        }else if(id == R.id.nav_list_activity){
            mFramePager.setCurrentItem(1);
        }else if(id == R.id.nav_settings){
            mFramePager.setCurrentItem(2);
        }else if(id == R.id.nav_none){
            //TODO
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
