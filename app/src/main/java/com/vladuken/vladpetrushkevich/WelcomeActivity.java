package com.vladuken.vladpetrushkevich;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
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

    protected final static String TAG = "WelcomeActivity:";

    protected ViewPager mViewPager;
//    private WelcomeViewPagerAdapter mViewPagerAdapter;
    protected int[] mLayouts;
    protected Button mBtnSkip, mBtnNext;

    protected RadioGroup mThemeRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
//
//    private void changeStatusBarColor() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
//    }

    private void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    protected void launchHomeScreen(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    public class WelcomeViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        protected RadioGroup mLayoutRadioGroup;

        public WelcomeViewPagerAdapter() {
            //TODO
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(mLayouts[position], container, false);
            container.addView(view);
            switch (position){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    RelativeLayout layout3 = view.findViewById(R.id.screen3id);
                    mLayoutRadioGroup = layout3.findViewById(R.id.radio_group_theme);
                    mLayoutRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            int selectedId = mLayoutRadioGroup.getCheckedRadioButtonId();

                            switch (selectedId){
                                case R.id.light_theme_radiobutton:
                                    Toast.makeText(getApplicationContext(),"Light theme",Toast.LENGTH_SHORT)
                                    .show();
                                    break;
                                case R.id.dark_theme_radiobutton:
                                    Toast.makeText(getApplicationContext(),"Dark theme",Toast.LENGTH_SHORT)
                                    .show();

                                    break;
                            }
                        }
                    });
                    break;
                case 3:
                    RelativeLayout layout4 = view.findViewById(R.id.screen4id);
                    mLayoutRadioGroup = layout4.findViewById(R.id.radio_group_layout);
                    mLayoutRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            int selectedId = mLayoutRadioGroup.getCheckedRadioButtonId();

                            switch (selectedId){
                                case R.id.standard_layout_rdb:
                                    Toast.makeText(getApplicationContext(),"Standard layout",Toast.LENGTH_SHORT)
                                            .show();
                                    break;
                                case R.id.compact_layout_rdb:
                                    Toast.makeText(getApplicationContext(),"Compact layout",Toast.LENGTH_SHORT)
                                            .show();

                                    break;
                            }
                        }
                    });
                    break;

            }
            return view;
//            mLayoutRadioGroup = findViewById(R.id.radio_group_layout);
//




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
