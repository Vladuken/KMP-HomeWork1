package com.vladuken.vladpetrushkevich.activities.main.gestureDetectors;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Build;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.vladuken.vladpetrushkevich.activities.main.fragments.desktop.DesktopItemViewHolder;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.SingletonDatabase;
import com.vladuken.vladpetrushkevich.db.entity.App;
import com.vladuken.vladpetrushkevich.db.entity.DesktopItem;
import com.yandex.metrica.YandexMetrica;

public class AppGestureDetectorListener extends GestureDetector.SimpleOnGestureListener {

//    private static final String TAG = "AppGestureDetector";
    private final App mApp;
    private final View mView;

    private DesktopItemViewHolder mViewHolder;

    public AppGestureDetectorListener(App app, View view) {
        mApp = app;
        mView = view;
    }

    public AppGestureDetectorListener(App app, View view, DesktopItemViewHolder viewHolder) {
        mApp = app;
        mView = view;
        mViewHolder = viewHolder;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //TODO CLIPDATA TO ONE PLACE
        ClipData.Item type = new ClipData.Item("app");
        ClipData.Item data = new ClipData.Item(mApp.package_name);

        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

        ClipDescription description = new ClipDescription("",mimeTypes);

        ClipData dragData = new ClipData(description,type);
        dragData.addItem(data);

        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(mView);

        if(mViewHolder != null){

            AppDatabase database = SingletonDatabase.getInstance(mView.getContext());
            mViewHolder.getDesktopItem().itemType = "empty";
            mViewHolder.getDesktopItem().itemData ="";
            database.desckopAppDao().update(mViewHolder.getDesktopItem());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mView.startDragAndDrop(dragData,shadowBuilder,mView,0);
            }
//            mView.startDragAndDrop(dragData,shadowBuilder,mView,0);
            mViewHolder.bind(createEmptyDesktopItem(mViewHolder.getDesktopItem()));

//            mView.setOnDragListener(null);
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mView.startDragAndDrop(dragData,shadowBuilder,mView,0);
            }
        }



//        mView = null;
        YandexMetrica.reportEvent("Started app drag and drop");

        super.onLongPress(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }


    private DesktopItem createEmptyDesktopItem(DesktopItem desktopItem){
        return new DesktopItem(desktopItem.screenPosition,desktopItem.row,desktopItem.column,"empty","");
    }

}
