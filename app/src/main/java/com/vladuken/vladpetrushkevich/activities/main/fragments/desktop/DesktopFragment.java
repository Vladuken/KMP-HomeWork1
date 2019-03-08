package com.vladuken.vladpetrushkevich.activities.main.fragments.desktop;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.BackgroundReceiver;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.SingletonDatabase;
import com.vladuken.vladpetrushkevich.db.entity.DesktopItem;
import com.vladuken.vladpetrushkevich.db.entity.DesktopScreen;
import com.vladuken.vladpetrushkevich.utils.BackgroundManager;


public class DesktopFragment extends Fragment {

    public static final int RESULT_CODE_PICK_PHONE = 555;



    private static final String SCREEN_ITEM_EMPTY_TYPE = "empty";
    private static final String SCREEN_ITEM_APP_TYPE = "app";
    private static final String SCREEN_ITEM_LINK_TYPE = "link";



    private static final String ARG_ROWS = "rows";
    private static final String ARG_COLUMNS = "columns";
    private static final String ARG_VP_POSITION = "position";


    protected GridLayout mTableLayout;

    protected AppDatabase mDatabase;
    protected DesktopScreen mDesktopScreen;

    private LinearLayout mTopRemoveBar;
    private int mViewPagerPosition;
    int mRows;
    int mColumns;
    private DesktopItemViewHolder mViewHolder;

    private BackgroundReceiver mBackgroundReceiver;


    private  int mLongClickedScreen;
    private int mLongClickedRow;
    protected int mLongClickedColumn;
    protected DesktopItemViewHolder mLongClickedViewHolder;

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

        mTopRemoveBar = v.findViewById(R.id.desktop_top_remove_bar);
        TextView textView = v.findViewById(R.id.desktop_top_remove_text_view);
        v.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()){
                    case DragEvent.ACTION_DRAG_STARTED:
                        mTopRemoveBar.setVisibility(View.VISIBLE);
                        textView.setTextSize(20);
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        textView.setTextSize(40);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        textView.setTextSize(20);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        mTopRemoveBar.setVisibility(View.GONE);
                        break;
                    case DragEvent.ACTION_DROP:
                        break;
                }

                return true;
            }
        });
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


        String fullpath = "";

        if(preferences.getBoolean(getString(R.string.preference_one_background_for_all_screens),false)){
            fullpath = mTableLayout.getContext().getFilesDir().toString()  + getString(R.string.global_image_title) + ".png";
        }else {
            fullpath = mTableLayout.getContext().getFilesDir().toString()  + mDesktopScreen.viewPagerPosition+ ".png";
        }

        BackgroundManager.setupBackground(mTableLayout,fullpath);
        mBackgroundReceiver = new BackgroundReceiver(mTableLayout,fullpath);



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

                mViewHolder = new DesktopItemViewHolder(
                        myview,
                        this,
                        desktopItem,
                        SingletonDatabase.getInstance(getContext()),
                        R.id.grid_app_icon,
                        R.id.grid_app_title
                );


                mViewHolder.bind(desktopItem);

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

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(BackgroundReceiver.UPDATE_BACKGROUND);
        filter.addAction(BackgroundReceiver.UPDATE_BACKGROUND_ONCE);
        getContext().registerReceiver(mBackgroundReceiver,filter);
        getContext().sendBroadcast(new Intent(BackgroundReceiver.UPDATE_BACKGROUND));
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(mBackgroundReceiver);
    }

    public static DesktopFragment newInstance(int viewPagerPosition){

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_VP_POSITION,viewPagerPosition);


        DesktopFragment fragment = new DesktopFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == RESULT_CODE_PICK_PHONE){

                Uri contactUri = data.getData();
                String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getContext().getContentResolver().query(contactUri, projection,
                        null, null, null);

                // If the cursor returned is valid, get the phone number
                if (cursor != null && cursor.moveToFirst()) {
                    int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = cursor.getString(numberIndex);
                    Log.d("number",number);
                    // Do something with the phone number
                    int a = data.getIntExtra("HELP",-1);
                    Log.d("number",a +"");


                    DesktopItem item = new DesktopItem(mLongClickedScreen,mLongClickedRow,mLongClickedColumn,"contact",number);
                    mLongClickedViewHolder.bind(item);
                }

                cursor.close();
            }
        }
    }

    public void setLongClickedScreen(int longClickedScreen) {
        mLongClickedScreen = longClickedScreen;
    }

    public void setLongClickedRow(int longClickedRow) {
        mLongClickedRow = longClickedRow;
    }

    public void setLongClickedColumn(int loncgClickedColumn) {
        mLongClickedColumn = loncgClickedColumn;
    }

    public void setLongClickedViewHolder(DesktopItemViewHolder longClickedViewHolder) {
        mLongClickedViewHolder = longClickedViewHolder;
    }
}
