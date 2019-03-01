package com.vladuken.vladpetrushkevich.activities.main.fragments.desktop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.SingletonDatabase;
import com.vladuken.vladpetrushkevich.db.entity.DesktopItem;
import com.vladuken.vladpetrushkevich.db.entity.DesktopScreen;


public class DesktopFragment extends Fragment {

    private static final String SCREEN_ITEM_EMPTY_TYPE = "empty";
    private static final String SCREEN_ITEM_APP_TYPE = "app";
    private static final String SCREEN_ITEM_LINK_TYPE = "link";



    private static final String ARG_ROWS = "rows";
    private static final String ARG_COLUMNS = "columns";
    private static final String ARG_VP_POSITION = "position";


    protected GridLayout mTableLayout;

    protected AppDatabase mDatabase;
    protected DesktopScreen mDesktopScreen;

    private int mViewPagerPosition;
    int mRows;
    int mColumns;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPagerPosition = getArguments().getInt(ARG_VP_POSITION);
        mDatabase = SingletonDatabase.getInstance(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.desktop_table_layout,container,false);

        SharedPreferences preferences = v.getContext().getSharedPreferences(getString(R.string.preference_file),0);
        boolean isCompactLayout = preferences.getBoolean(getString(R.string.preference_key_layout),false);

        if(isCompactLayout){
            mRows = getResources().getInteger(R.integer.compact_portrait_layout_span);
            mColumns = getResources().getInteger(R.integer.compact_landscape_layout_span);
        }else {
            mRows = getResources().getInteger(R.integer.standard_portrait_layout_span);
            mColumns = getResources().getInteger(R.integer.standard_landscape_layout_span);
        }

        mTableLayout = v.findViewById(R.id.desktop_grid_layout);

        mDesktopScreen = mDatabase.desktopScreenDao().getByPosition(mViewPagerPosition);
        if(mDesktopScreen == null){
            mDesktopScreen = new DesktopScreen(mViewPagerPosition,mRows,mColumns);
            mDatabase.desktopScreenDao().insertAll(mDesktopScreen);
        }


        int orientation = getResources().getConfiguration().orientation;
        if (orientation != Configuration.ORIENTATION_PORTRAIT) {
            int buff = mRows;
            mRows = mColumns;
            mColumns = buff;
            setupGridLayout(mColumns,mRows);
        }else {
            setupGridLayout(mRows,mColumns);
        }



        return v;
    }

    private void setupGridLayout(int rows, int colums){

        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
//        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);




        for(int r = 0; r < rows; r++){
            for(int c = 0; c < colums; c++){

                View myview =  View.inflate(getContext(),R.layout.desctop_icon_view,null);

                DesktopItem desktopItem = mDatabase.desckopAppDao().getByIds(mDesktopScreen.viewPagerPosition,r,c);
                if(desktopItem == null){
                    desktopItem = new DesktopItem(mDesktopScreen.viewPagerPosition,r,c,SCREEN_ITEM_EMPTY_TYPE,"");
                    mDatabase.desckopAppDao().insertAll(desktopItem);
                }
                if(desktopItem.itemType.equals("app")){
                    Log.d("AAA","AAA");
                }

                DesktopItemViewHolder viewHolder = new DesktopItemViewHolder(
                        myview,
                        this,
                        desktopItem,
                        SingletonDatabase.getInstance(getContext()),
                        R.id.grid_app_icon,
                        R.id.grid_app_title
                );


                viewHolder.bind(desktopItem);

                //TODO add to lover api
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                        GridLayout.spec(c, 1f),
                        GridLayout.spec(r,1f)
                );

                params.height = 0;
                params.width = 0;

                myview.setLayoutParams (params);

                mTableLayout.addView(myview);
            }
        }
    }

    public static DesktopFragment newInstance(int viewPagerPosition){

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_VP_POSITION,viewPagerPosition);


        DesktopFragment fragment = new DesktopFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

}
