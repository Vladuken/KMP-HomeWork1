package com.vladuken.vladpetrushkevich.activities.main.gestureDetectors;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.vladuken.vladpetrushkevich.activities.main.fragments.desktop.DesktopItemViewHolder;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.SingletonDatabase;
import com.vladuken.vladpetrushkevich.db.entity.DesktopItem;
import com.vladuken.vladpetrushkevich.utils.DragUtils;
import com.yandex.metrica.YandexMetrica;

public class DesktopItemGestureDetectorListener extends GestureDetector.SimpleOnGestureListener {

    private final DesktopItem mDesktopItem;
    private final View mView;
    private final DesktopItemViewHolder mViewHolder;

    public DesktopItemGestureDetectorListener(DesktopItem desktopItem, View view, DesktopItemViewHolder viewHolder) {
        mDesktopItem = desktopItem;
        mView = view;
        mViewHolder = viewHolder;
    }

    @Override
    public void onLongPress(MotionEvent e) {

        ClipData dragData = DragUtils.createDragData(mDesktopItem.itemType,mDesktopItem.itemData);
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(mView);

        AppDatabase database = SingletonDatabase.getInstance(mView.getContext());

        DragUtils.startDrag(dragData,shadowBuilder,mView,0);

        mViewHolder.getDesktopItem().itemType = "empty";
        mViewHolder.getDesktopItem().itemData ="";
        database.desckopAppDao().update(mViewHolder.getDesktopItem());
        mViewHolder.bind(createEmptyDesctopItem(mViewHolder.getDesktopItem()));

        YandexMetrica.reportEvent("Started desktopItem drag and drop");

        super.onLongPress(e);
    }

    private DesktopItem createEmptyDesctopItem(DesktopItem desktopItem){
        return new DesktopItem(desktopItem.screenPosition,desktopItem.row,desktopItem.column,"empty","");
    }

}
