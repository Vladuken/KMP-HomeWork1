package com.vladuken.vladpetrushkevich.utils;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Build;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewParent;

import java.util.ArrayList;

public class DragUtils {
    public static void startDrag(ClipData clipData, View.DragShadowBuilder shadowBuilder, View view, int flags){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.startDragAndDrop(clipData,shadowBuilder,view,flags);
        }else{
            view.startDrag(clipData,shadowBuilder,view,flags);
        }
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

    public class DropTarget implements View.OnDragListener {
        private ArrayList<View> views=new ArrayList<>();
        private View.OnDragListener listener;

        public DropTarget on(View... views) {
            for (View v : views) {
                this.views.add(v);
                v.setOnDragListener(this);
            }

            return(this);
        }

        public void to(View.OnDragListener listener) {
            this.listener=listener;
        }

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            if (Build.VERSION.SDK_INT<Build.VERSION_CODES.N) {
                return(listener.onDrag(view, dragEvent));
            }

            boolean result=listener.onDrag(view, dragEvent);
            ViewParent parent=view.getParent();

            while (parent!=null && parent instanceof View) {
                View parentView=(View)parent;

                if (views.contains(parentView)) {
                    listener.onDrag(parentView, dragEvent);
                }

                parent=parentView.getParent();
            }

            return(result);
        }
    }
}
