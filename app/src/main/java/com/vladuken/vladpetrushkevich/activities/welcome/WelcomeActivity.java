package com.vladuken.vladpetrushkevich.activities.welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.LauncherActivity;
import com.vladuken.vladpetrushkevich.activities.welcome.fragments.FirstPageFragment;
import com.vladuken.vladpetrushkevich.activities.welcome.fragments.ForthPageFragment;
import com.vladuken.vladpetrushkevich.activities.welcome.fragments.SecondPageFragment;
import com.vladuken.vladpetrushkevich.activities.welcome.fragments.ThirdPageFragment;
import com.vladuken.vladpetrushkevich.utils.PrefManager;


public class WelcomeActivity extends AppCompatActivity {

    protected ViewPager mViewPager;
    protected Button mBtnSkip, mBtnNext;

    protected boolean mIsDarkTheme; // 0 is light 1 is dark
    protected boolean mIsCompactLayout;

    protected PrefManager mPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefManager = new PrefManager(this);
        if(!mPrefManager.isFirstTimeLaunch()){
            launchHomeScreen();
            finish();
        }

        if(savedInstanceState != null){
            mIsDarkTheme = savedInstanceState.getBoolean(getString(R.string.preference_key_theme));
            mIsCompactLayout = savedInstanceState.getBoolean(getString(R.string.preference_key_layout));
        }
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);

        mViewPager = findViewById(R.id.view_pager);
        mBtnSkip = findViewById(R.id.btn_skip);
        mBtnNext = findViewById(R.id.btn_next);

        mBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
                launchHomeScreen();
            }
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = mViewPager.getCurrentItem();
                if(current < mViewPager.getAdapter().getCount() - 1){
                    mViewPager.setCurrentItem(current+1);
                }else {
                    launchHomeScreen();
                }
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new WelcomeFragmentPagerAdapter(fragmentManager));

        changeStatusBarColor();
    }

    private void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    protected void launchHomeScreen(){
        Intent i = new Intent(this, LauncherActivity.class);

        mPrefManager.setFirstTimeLaunch(false);
        startActivity(i);
        finish();
    }

    protected void savePreferences(){
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.preference_file),0).edit();
        editor.putBoolean(getString(R.string.preference_key_theme), mIsDarkTheme);
        editor.putBoolean(getString(R.string.preference_key_layout),mIsCompactLayout);

        editor.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(getString(R.string.preference_key_theme), mIsDarkTheme);
        outState.putBoolean(getString(R.string.preference_key_layout),mIsCompactLayout);

    }

    @Override
    public void onBackPressed() {
        int currentItem =mViewPager.getCurrentItem();
        if(currentItem != 0){
            mViewPager.setCurrentItem(currentItem - 1,true);
        }else {
            super.onBackPressed();
        }
    }

    private class WelcomeFragmentPagerAdapter extends FragmentPagerAdapter{
        int[] mLayouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4};

        public WelcomeFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i){
                case 0:
                    return FirstPageFragment.newInstance();
                case 1:
                    return SecondPageFragment.newInstance();
                case 2:
                    return ThirdPageFragment.newInstance();
                case 3:
                    return ForthPageFragment.newInstance();
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return mLayouts.length;
        }
    }
}
