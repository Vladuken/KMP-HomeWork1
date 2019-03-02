package com.vladuken.vladpetrushkevich.activities.main;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.vladuken.vladpetrushkevich.activities.main.fragments.desktop.DesktopItemViewHolder;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.SingletonDatabase;
import com.vladuken.vladpetrushkevich.db.entity.DesktopItem;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;

public class MyScrollGestureLinstener extends GestureDetector.SimpleOnGestureListener {

    private DesktopItemViewHolder mViewHolder;
//    private DesktopItem mDesktopItem;
    private View mView;

    public MyScrollGestureLinstener(DesktopItemViewHolder viewHolder, View view) {
        mViewHolder = viewHolder;
        mView = view;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        //TODO CLIPDATA TO ONE PLACE
        ClipData.Item type = new ClipData.Item(mViewHolder.getDesktopItem().itemType);
        ClipData.Item data = new ClipData.Item(mViewHolder.getDesktopItem().itemData);

        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

        ClipDescription description = new ClipDescription("",mimeTypes);

        ClipData dragData = new ClipData(description,type);
        dragData.addItem(data);

        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(mView);
        Log.d("STARTDRAGANDDROP","Drag Started");


        if(!mViewHolder.getDesktopItem().itemType.equals("empty")){
            AppDatabase database = SingletonDatabase.getInstance(mView.getContext());
            mViewHolder.getDesktopItem().itemType = "empty";
            mViewHolder.getDesktopItem().itemData ="";
            database.desckopAppDao().update(mViewHolder.getDesktopItem());
            mView.startDragAndDrop(dragData,shadowBuilder,mView,0);
            mViewHolder.bind(mViewHolder.getDesktopItem());

            YandexMetrica.reportEvent("Started app drag and drop");
        }

        return super.onScroll(e1, e2, distanceX, distanceY);
    }
}
