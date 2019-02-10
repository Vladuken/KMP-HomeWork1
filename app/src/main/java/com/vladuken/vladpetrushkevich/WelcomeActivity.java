package com.vladuken.vladpetrushkevich;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class WelcomeActivity extends AppCompatActivity {

    protected static final String TAG = "WelcomeActivity:";


    private static final String PREFERENCE_THEME_KEY = "PREFERENCE_THEME";



    protected ViewPager mViewPager;
    protected int[] mLayouts;
    protected Button mBtnSkip, mBtnNext;

    //TODO get rid of magic numbers
    protected int theme; // 0 is light 1 is dark
    protected int portrait_rows;
    protected int landscape_rows;


    private PrefManager mPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        portrait_rows = getResources().getInteger(R.integer.standard_portrait_layout_span);
        landscape_rows = getResources().getInteger(R.integer.standard_landscape_layout_span);

        mPrefManager = new PrefManager(this);
        if(!mPrefManager.isFirstTimeLaunch()){
            launchHomeScreen();
            finish();
        }


        if(savedInstanceState != null){
            theme = savedInstanceState.getInt(Constants.SharedPreferences.THEME);
            portrait_rows = savedInstanceState.getInt(Constants.SharedPreferences.PORTRAITE_ROWS);
            landscape_rows = savedInstanceState.getInt(Constants.SharedPreferences.LANDSCAPE_ROWS);
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
                if(current < mLayouts.length - 1){
                    mViewPager.setCurrentItem(current+1);
                }else {
                    savePreferences();
                    launchHomeScreen();
                }
            }
        });

        mLayouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4};

        WelcomeViewPagerAdapter mViewPagerAdapter = new WelcomeViewPagerAdapter();
        mViewPager.setAdapter(mViewPagerAdapter);

        changeStatusBarColor();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                //TODO
            }

            @Override
            public void onPageSelected(int i) {
                if(i == mLayouts.length - 1){
                    mBtnNext.setText(getString(R.string.start));
                    mBtnSkip.setVisibility(View.GONE);
                }else {
                    mBtnNext.setText(getString(R.string.next));
                    mBtnSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                //TODO
            }
        });
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
    }

    private void savePreferences(){
        SharedPreferences.Editor editor = getSharedPreferences(Constants.SharedPreferences.APP_PREFERENCES,0).edit();
        editor.putInt(Constants.SharedPreferences.THEME, theme);
        editor.putInt(Constants.SharedPreferences.PORTRAITE_ROWS, portrait_rows);
        editor.putInt(Constants.SharedPreferences.LANDSCAPE_ROWS, landscape_rows);

        editor.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(Constants.SharedPreferences.THEME,theme);
        outState.putInt(Constants.SharedPreferences.PORTRAITE_ROWS,portrait_rows);
        outState.putInt(Constants.SharedPreferences.LANDSCAPE_ROWS,landscape_rows);

    }

    public class WelcomeViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        protected RadioGroup mLayoutRadioGroup;
        protected RadioGroup mThemeRadioGroup;
        public WelcomeViewPagerAdapter() {
            //TODO
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View view = layoutInflater.inflate(mLayouts[position], container, false);
            container.addView(view);
            switch (position){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    final RelativeLayout layout3 = view.findViewById(R.id.screen3id);
                    mThemeRadioGroup = layout3.findViewById(R.id.radio_group_theme);
                    mThemeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            int selectedId = mThemeRadioGroup.getCheckedRadioButtonId();

                            if (selectedId == R.id.light_theme_radiobutton) {
                                theme = 0;
//                                findViewById(R.id.dark_theme_radiobutton).setBackgroundColor(Color.argb(0,0,0,0));

                                Snackbar.make(layout3,"Light theme",Snackbar.LENGTH_SHORT)
                                        .show();

                            } else if (selectedId == R.id.dark_theme_radiobutton) {
                                theme = 1;
//                                findViewById(R.id.dark_theme_radiobutton).setBackgroundColor(Color.argb(255,0,0,0));


                                Snackbar.make(layout3,"Dark theme",Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
                    break;
                case 3:
                    final RelativeLayout layout4 = view.findViewById(R.id.screen4id);
                    mLayoutRadioGroup = layout4.findViewById(R.id.radio_group_layout);
                    mLayoutRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            int selectedId = mLayoutRadioGroup.getCheckedRadioButtonId();

                            if (selectedId == R.id.standard_layout_rdb) {
                                portrait_rows = getResources().getInteger(R.integer.standard_portrait_layout_span);
                                landscape_rows = getResources().getInteger(R.integer.standard_landscape_layout_span);

                                Snackbar.make(layout4,"Standard layout",Snackbar.LENGTH_SHORT)
                                        .show();

                            } else if (selectedId == R.id.compact_layout_rdb) {
                                portrait_rows = getResources().getInteger(R.integer.compact_portrait_layout_span);
                                landscape_rows = getResources().getInteger(R.integer.compact_landscape_layout_span);
                                Snackbar.make(layout4,"Compact layout",Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
            return view;
        }

        @Override
        public int getCount() {
            return mLayouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view.equals(o);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
