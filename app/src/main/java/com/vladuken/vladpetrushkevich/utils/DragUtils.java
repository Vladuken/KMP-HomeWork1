package com.vladuken.vladpetrushkevich.utils;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Build;
import android.view.DragEvent;
import android.view.View;

public class DragUtils {

    private DragUtils() {
    }

    public static void startDrag(ClipData clipData, View.DragShadowBuilder shadowBuilder, View view, int flags){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.startDragAndDrop(clipData,shadowBuilder,view,flags);
        }else{
            view.startDrag(clipData,shadowBuilder,view,flags);
        }
    }

    public static ClipData createDragData(String itemType, String itemData){
        ClipData.Item type = new ClipData.Item(itemType);
        ClipData.Item data = new ClipData.Item(itemData);

        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

        ClipDescription description = new ClipDescription("",mimeTypes);

        ClipData dragData = new ClipData(description,type);
        dragData.addItem(data);

        return dragData;
    }

    public static class  DebugDragListener implements View.OnDragListener{
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()){
                case DragEvent.ACTION_DRAG_STARTED:
//                    v.setBackgroundColor(Color.rgb(220,220,200));
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
//                    v.setBackgroundColor(Color.GREEN);
//                    return false;
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
//                    v.setBackgroundColor(Color.RED);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
//                    v.setBackgroundColor(Color.BLACK);
                    break;
                case DragEvent.ACTION_DROP:
//                    v.setBackgroundColor(Color.YELLOW);
                    break;
                default:
                    break;
            }

            return true;
        }
    }
}
