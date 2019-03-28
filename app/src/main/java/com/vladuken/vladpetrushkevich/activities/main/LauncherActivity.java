package com.vladuken.vladpetrushkevich.activities.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

    private static final String CHECKED_POSITION = "CheckedNavId";

    protected SharedPreferences mSharedPreferences;
    protected NavigationView mNavigationView;
    protected NestedScrollView mNestedScrollView;
    protected ViewPager mFramePager;

    private int mDesktopCount;
    private SwipeFramePagerReceiver mPagerReceiver;

    private int mCheckedItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPreferences = getSharedPreferences(getString(R.string.preference_file), 0);
        boolean theme = mSharedPreferences.getBoolean(getString(R.string.preference_key_theme), false);
        ThemeChanger.onCreateSetTheme(this, theme);

        String desktopCountString = mSharedPreferences.getString(
                getString(R.string.preference_key_desktop_screen_count), "1");
        mDesktopCount = Integer.parseInt(desktopCountString);

        super.onCreate(savedInstanceState);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_nav_drawer);

        mNavigationView = findViewById(R.id.nav_view);
        setupMenu(mNavigationView,mDesktopCount);

        Button notification1 = mNavigationView.findViewById(R.id.notification_button1);
        notification1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendNotification(v.getContext(),
                        LauncherActivity.class,
                        R.drawable.ic_action_name,
                        1,
                        "launcher_page",
                        v.getResources().getString(R.string.notification) + " " + 1,
                        v.getResources().getString(R.string.notification_to_launcher),
                        Color.BLUE);
            }
        });


        Button notification2 = mNavigationView.findViewById(R.id.notification_button2);
        notification2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(v.getContext(),
                        ProfilePageActivity.class,
                        R.drawable.ic_list_launcher,
                        2,
                        "profile_page",
                        v.getResources().getString(R.string.notification) + " " + 2,
                        v.getResources().getString(R.string.notification_to_profile),
                        Color.RED);
            }
        });
        mNavigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        View headerView = mNavigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ProfilePageActivity.class);
                startActivity(i);
                YandexMetrica.reportEvent("Profile page started from launcher page");
            }
        });

        mNestedScrollView = findViewById(R.id.nested_scrollview);
        mNestedScrollView.setFillViewport(true);
        mNestedScrollView.setOnDragListener(new DragUtils.DebugDragListener());

        mFramePager = setupViewPager(mSharedPreferences);

        mPagerReceiver = new SwipeFramePagerReceiver(mFramePager);

        //if was reload activity
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            int id = bundle.getInt(CHECKED_POSITION,1);
            MenuItem menuItem = mNavigationView.getMenu().getItem(id);
            onNavigationItemSelected(menuItem);
        }
    }

    private void sendNotification(Context context,
                                  Class<?> className,
                                  int drawableResID,
                                  int id,
                                  String channelTitle,
                                  String title,
                                  String text,
                                  int color){
        Intent notificationIntent2 = new Intent(context, className);
        PendingIntent contentIntent2 = PendingIntent.getActivity(context,
                0, notificationIntent2,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder notif2;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelTitle,channelTitle, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            notif2 = new NotificationCompat.Builder(context,channelTitle);
        } else {
            notif2 = new NotificationCompat.Builder(context);
        }
        notif2.setContentIntent(contentIntent2)
                .setChannelId(channelTitle)
                .setSmallIcon(drawableResID)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setColor(color);
        NotificationManagerCompat.from(context).notify(id,notif2.build());
    }


    private void setupMenu(NavigationView navigationView,int desktopCount){
        Menu menu = navigationView.getMenu();
        for(int i = 0; i < desktopCount + 3; i++){
            if(i == 0){
                addMenuItem(menu,i,R.string.launcher_activity,R.drawable.ic_action_name,false);
            }else if(i == desktopCount + 1){
                addMenuItem(menu,i,R.string.list_activity,R.drawable.ic_list_launcher,false);
            }else if(i == desktopCount + 2){
                addMenuItem(menu,i,R.string.settings,R.drawable.ic_menu_launcher,false);
            }else {
                addMenuItem(menu,i,R.string.desktop,R.drawable.ic_action_name,true);
            }
        }
        navigationView.invalidate();
    }

    private void addMenuItem(Menu menu, int i, int stringResource,int drawableRecource,boolean isDesktop){
        String title = getResources().getString(stringResource);
        if(isDesktop){
            menu.add(0,i,0,title + " " + i);
        }else {
            menu.add(0,i,0,title);
        }
        menu.getItem(i+1).setIcon(drawableRecource);
    }

    protected void uncheckAllMenuItems(NavigationView navigationView){
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }
    public boolean onNavigationItemSelected(MenuItem item){
        uncheckAllMenuItems(mNavigationView);
        item.setChecked(true);
        mFramePager.setCurrentItem(item.getItemId());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private ViewPager setupViewPager(SharedPreferences preferences){
        ViewPager framePager = findViewById(R.id.launcher_fragment_viewpager);
        framePager.setOnDragListener(new DragUtils.DebugDragListener());


        LauncherPagerAdapter launcherPagerAdapter = new LauncherPagerAdapter(
                getSupportFragmentManager(),
                mDesktopCount
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
                uncheckAllMenuItems(mNavigationView);
                mNavigationView.getMenu().getItem(i+1).setChecked(true);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSharedPreferences = null;
        mNavigationView = null;
        mNestedScrollView = null;
        mFramePager = null;
        mPagerReceiver = null;
    }

    public void restartActivity() {
        Intent i = getIntent();

        Bundle bundle = new Bundle();
        bundle.putInt(CHECKED_POSITION,mCheckedItemPosition);
        i.putExtras(bundle);

        this.finish();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(i);
    }

}
