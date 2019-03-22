package com.vladuken.vladpetrushkevich.utils;

import android.content.ClipData;
import android.os.Build;
import android.view.View;

public class DragUtils {
    public static void startDrag(ClipData clipData, View.DragShadowBuilder shadowBuilder, View view, int flags){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.startDragAndDrop(clipData,shadowBuilder,view,flags);
        }else{
            view.startDrag(clipData,shadowBuilder,view,flags);
        }
    }
}
