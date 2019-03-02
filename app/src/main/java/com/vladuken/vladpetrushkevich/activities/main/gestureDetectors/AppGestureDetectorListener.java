package com.vladuken.vladpetrushkevich.activities.main.gestureDetectors;

import android.content.ClipData;
import android.content.ClipDescription;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.vladuken.vladpetrushkevich.db.entity.App;
import com.yandex.metrica.YandexMetrica;

public class AppGestureDetectorListener extends GestureDetector.SimpleOnGestureListener {

    private App mApp;
    private View mView;

    public AppGestureDetectorListener(App app, View view) {
        mApp = app;
        mView = view;
    }

    @Override
    public void onLongPress(MotionEvent e) {
//        Log.d(TAG,"GestureDetector long press");
        //TODO CLIPDATA TO ONE PLACE
        ClipData.Item type = new ClipData.Item("app");
        ClipData.Item data = new ClipData.Item(mApp.package_name);

        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

        ClipDescription description = new ClipDescription("",mimeTypes);

        ClipData dragData = new ClipData(description,type);
        dragData.addItem(data);

        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(mView);
        mView.startDragAndDrop(dragData,shadowBuilder,mView,0);

        YandexMetrica.reportEvent("Started app drag and drop");

        super.onLongPress(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

}
