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
import com.yandex.metrica.YandexMetrica;

public class DesktopItemGestureDetectorListener extends GestureDetector.SimpleOnGestureListener {

    private static final String TAG = "DesktopItemGesture";
    private DesktopItem mDesktopItem;
    private View mView;
    private DesktopItemViewHolder mViewHolder;

    public DesktopItemGestureDetectorListener(DesktopItem desktopItem, View view, DesktopItemViewHolder viewHolder) {
        mDesktopItem = desktopItem;
        mView = view;
        mViewHolder = viewHolder;
    }

    @Override
    public void onLongPress(MotionEvent e) {

        ClipData.Item type = new ClipData.Item(mDesktopItem.itemType);
        ClipData.Item data = new ClipData.Item(mDesktopItem.itemData);

        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

        ClipDescription description = new ClipDescription("",mimeTypes);

        ClipData dragData = new ClipData(description,type);
        dragData.addItem(data);

        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(mView);


        AppDatabase database = SingletonDatabase.getInstance(mView.getContext());
        mView.startDragAndDrop(dragData,shadowBuilder,mView,0);

        mViewHolder.getDesktopItem().itemType = "empty";
        mViewHolder.getDesktopItem().itemData ="";
        database.desckopAppDao().update(mViewHolder.getDesktopItem());
//            mView.startDragAndDrop(dragData,shadowBuilder,mView,0);
        mViewHolder.bind(createEmptyDesctopItem(mViewHolder.getDesktopItem()));


        YandexMetrica.reportEvent("Started desktopItem drag and drop");

        super.onLongPress(e);
    }

    private DesktopItem createEmptyDesctopItem(DesktopItem desktopItem){
        return new DesktopItem(desktopItem.screenPosition,desktopItem.row,desktopItem.column,"empty","");
    }

}
