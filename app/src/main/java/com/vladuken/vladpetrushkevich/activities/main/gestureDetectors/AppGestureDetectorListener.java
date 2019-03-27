package com.vladuken.vladpetrushkevich.activities.main.gestureDetectors;

import android.content.ClipData;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.vladuken.vladpetrushkevich.activities.main.fragments.desktop.DesktopItemViewHolder;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.SingletonDatabase;
import com.vladuken.vladpetrushkevich.db.entity.App;
import com.vladuken.vladpetrushkevich.db.entity.DesktopItem;
import com.vladuken.vladpetrushkevich.utils.DragUtils;
import com.yandex.metrica.YandexMetrica;

public class AppGestureDetectorListener extends GestureDetector.SimpleOnGestureListener {

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
        ClipData dragData = DragUtils.createDragData("app",mApp.package_name);
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(mView);
        if(mViewHolder != null){
            AppDatabase database = SingletonDatabase.getInstance(mView.getContext());
            mViewHolder.getDesktopItem().itemType = "empty";
            mViewHolder.getDesktopItem().itemData ="";
            database.desckopAppDao().update(mViewHolder.getDesktopItem());

            DragUtils.startDrag(dragData,shadowBuilder,mView,0);
            mViewHolder.bind(createEmptyDesktopItem(mViewHolder.getDesktopItem()));
        }else {
            DragUtils.startDrag(dragData,shadowBuilder,mView,0);
        }
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
