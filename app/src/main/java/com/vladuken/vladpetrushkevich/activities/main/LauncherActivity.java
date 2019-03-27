package com.vladuken.vladpetrushkevich.activities.main;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.eftimoff.viewpagertransformers.AccordionTransformer;
import com.eftimoff.viewpagertransformers.RotateDownTransformer;
import com.eftimoff.viewpagertransformers.RotateUpTransformer;
import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.profile.ProfilePageActivity;
import com.vladuken.vladpetrushkevich.utils.AnimateUtils;
import com.vladuken.vladpetrushkevich.utils.DragUtils;
import com.vladuken.vladpetrushkevich.utils.ThemeChanger;
import com.yandex.metrica.YandexMetrica;

import io.fabric.sdk.android.Fabric;

public class LauncherActivity extends AppCompatActivity {

    private static final String CHECKED_NAV_ID = "CheckedNavId";

    protected SharedPreferences mSharedPreferences;
    protected NavigationView mNavigationView;
    protected NestedScrollView mNestedScrollView;
    protected ViewPager mFramePager;

    private SwipeFramePagerReceiver mPagerReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPreferences = getSharedPreferences(getString(R.string.preference_file), 0);
        boolean theme = mSharedPreferences.getBoolean(getString(R.string.preference_key_theme), false);
        ThemeChanger.onCreateSetTheme(this, theme);

        super.onCreate(savedInstanceState);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_nav_drawer);

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        View headerView = mNavigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ProfilePageActivity.class);
                startActivity(i);
                onNavigationItemSelected(mNavigationView.getMenu().findItem(R.id.nav_none));
                YandexMetrica.reportEvent("Profile page started from launcher page");

            }
        });

        mNestedScrollView = findViewById(R.id.nested_scrollview);
        mNestedScrollView.setFillViewport(true);
        mNestedScrollView.setOnDragListener(new DragUtils.DebugDragListener());

        mFramePager = setupViewPager(mSharedPreferences);

        mPagerReceiver = new SwipeFramePagerReceiver(mFramePager);

        mNavigationView.setCheckedItem(R.id.nav_launcher_activity);
        onNavigationItemSelected(mNavigationView.getMenu().findItem(R.id.nav_launcher_activity));

        if (savedInstanceState != null) {
            int menuId = savedInstanceState.getInt(CHECKED_NAV_ID, R.id.nav_list_activity);
            MenuItem menuItem = mNavigationView.getMenu().findItem(menuId);

            mNavigationView.setCheckedItem(menuId);
            onNavigationItemSelected(menuItem);
        }else {
            MenuItem menuItem = mNavigationView.getMenu().findItem(R.id.nav_desktop);
            mNavigationView.setCheckedItem(R.id.nav_desktop);
            onNavigationItemSelected(menuItem);
        }

        //if was reload activity
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            int menuId = bundle.getInt(CHECKED_NAV_ID,R.id.nav_list_activity);
            MenuItem menuItem = mNavigationView.getMenu().findItem(menuId);

            mNavigationView.setCheckedItem(menuId);
            onNavigationItemSelected(menuItem);
        }
    }

    private ViewPager setupViewPager(SharedPreferences preferences){
        ViewPager framePager = findViewById(R.id.launcher_fragment_viewpager);
        framePager.setOnDragListener(new DragUtils.DebugDragListener());

        LauncherPagerAdapter launcherPagerAdapter = new LauncherPagerAdapter(
                getSupportFragmentManager()
        );

        TabLayout tabLayout = findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(framePager, true);

        framePager.setAdapter(launcherPagerAdapter);
        framePager.setOffscreenPageLimit(launcherPagerAdapter.getCount());

        setupAnimation(framePager,preferences);

        framePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                //TODO
            }

            @Override
            public void onPageSelected(int i) {
                mNavigationView.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if(i == ViewPager.SCROLL_STATE_DRAGGING || i == ViewPager.SCROLL_STATE_SETTLING){
                    AnimateUtils.animateToVisible(tabLayout,250);
                }else {
                    AnimateUtils.animateToGone(tabLayout,250);
                }
            }
        });

        framePager.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return true;
            }
        });

        return framePager;
    }

    private void setupAnimation(ViewPager viewPager,SharedPreferences sharedPreferences){

        if(sharedPreferences == null){
            return;
        }

        String animationTypeString = sharedPreferences.getString(getString(R.string.preference_key_animation_type), "1");
        int animationType;
        if(animationTypeString != null){
            animationType = Integer.parseInt(animationTypeString);
        }else {
            return;
        }

        switch (animationType){
            case 1:
                break;
            case 2:
                viewPager.setPageTransformer(true, new AccordionTransformer());
                break;
            case 3:
                viewPager.setPageTransformer(true, new RotateDownTransformer());
                break;
            case 4:
                viewPager.setPageTransformer(true, new RotateUpTransformer());
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CHECKED_NAV_ID,mNavigationView.getCheckedItem().getItemId());
        super.onSaveInstanceState(outState);
    }

    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        onNavigationItemSelected(id);

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(int id) {

        if(id == R.id.nav_launcher_activity){
            mFramePager.setCurrentItem(0);
        }else if(id == R.id.nav_desktop) {
            mFramePager.setCurrentItem(1);
        }else if(id == R.id.nav_list_activity){
            mFramePager.setCurrentItem(2);
        }else if(id == R.id.nav_settings){
            mFramePager.setCurrentItem(3);
        }else if(id == R.id.nav_none){
            //TODO
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter(SwipeFramePagerReceiver.RIGHT);
        filter.addAction(SwipeFramePagerReceiver.LEFT);

        this.registerReceiver(mPagerReceiver,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(mPagerReceiver);
    }

    public void restartActivity() {
        Intent i = getIntent();

        Bundle bundle = new Bundle();
        bundle.putInt(CHECKED_NAV_ID,mNavigationView.getCheckedItem().getItemId());

        i.putExtras(bundle);

        this.finish();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(i);
    }

}
