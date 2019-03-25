package com.vladuken.vladpetrushkevich.activities.main.fragments;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.entity.App;
import com.vladuken.vladpetrushkevich.utils.AnimateUtils;
import com.yandex.metrica.YandexMetrica;


public class TopBarDragUtil {

    private static final int ACCENTCOLOR = Color.RED;
    private static final int ANIMATIONDELAY = 300;

    public static void setupTopBarDraggable(LinearLayout mTopBar, AppDatabase database){
        mTopBar.setOnDragListener(new TopBarOnDragListener());

        TextView remove = mTopBar.findViewById(R.id.top_bar_remove);
        remove.setOnDragListener(new RemoveDragListener());

        TextView delete = mTopBar.findViewById(R.id.top_bar_delete);
        delete.setOnDragListener(new DeleteDragListener());

        TextView settings = mTopBar.findViewById(R.id.top_bar_settings);
        settings.setOnDragListener(new SettingsDragListener());

        TextView launch_count = mTopBar.findViewById(R.id.top_bar_launch_count);
        launch_count.setOnDragListener(new LaunchCountDragListener(database));

    }


    private static class TopBarOnDragListener implements View.OnDragListener {

        private void dispatchToChildren(LinearLayout v,DragEvent event){
            LinearLayout linearLayout = v;//
            for(int i = 0; i< linearLayout.getChildCount(); i++){
                View child = linearLayout.getChildAt(i);
                child.dispatchDragEvent(event);
            }
        }
        @Override
        public boolean onDrag(View v, DragEvent event) {

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    params.weight = 20;
                    v.setLayoutParams(params);

                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    Log.d("LOCATION",event.getX() +" " + event.getY());
                    dispatchToChildren((LinearLayout) v,event);
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    return false;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    dispatchToChildren((LinearLayout) v,event);

                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0);
                    v.setLayoutParams(params2);
                    break;
                case DragEvent.ACTION_DROP:
                    dispatchToChildren((LinearLayout) v,event);
                    break;
                default:
                    break;
            }

            return true;
        }
    }



    private static class DeleteDragListener implements View.OnDragListener {

        Boolean isIn =false;
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_LOCATION:
                    isIn = paintBackgroundRect(v,event,isIn);
                    break;

                case DragEvent.ACTION_DROP:
                    AnimateUtils.animateBackground(v, ACCENTCOLOR, Color.TRANSPARENT, ANIMATIONDELAY);

                    ClipData data = event.getClipData();
                    if(isTouchPointInView(v,event) && getClipType(data).equals("app")){
                        uninstallApp(v.getContext(),getClipData(data));
                    }
                    break;
                default:
                    break;
            }

            return switchDragEvent(v,event);
        }
    }


    private static class RemoveDragListener implements View.OnDragListener {


        Boolean isIn =false;

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {

                case DragEvent.ACTION_DRAG_LOCATION:
                    isIn = paintBackgroundRect(v,event,isIn);
                    break;
                case DragEvent.ACTION_DROP:
                    AnimateUtils.animateBackground(v, ACCENTCOLOR, Color.TRANSPARENT, ANIMATIONDELAY);

                    break;
                default:
                    break;
            }

            return switchDragEvent(v,event);
        }
    }


    private static class SettingsDragListener implements View.OnDragListener {


        Boolean isIn =false;

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_LOCATION:
                    isIn = paintBackgroundRect(v,event,isIn);
                    break;

                case DragEvent.ACTION_DROP:
                    AnimateUtils.animateBackground(v, ACCENTCOLOR, Color.TRANSPARENT, ANIMATIONDELAY);

                    ClipData data = event.getClipData();
                    if(isTouchPointInView(v,event) && getClipType(data).equals("app")){
                        openAppSettings(v.getContext(),getClipData(data));
                    }
                    break;
                default:
                    break;
            }

            return switchDragEvent(v,event);
        }
    }


    private static class LaunchCountDragListener implements View.OnDragListener {

        AppDatabase mDatabase;

        Boolean isIn =false;

        public LaunchCountDragListener(AppDatabase database) {
            mDatabase = database;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_LOCATION:
                    isIn = paintBackgroundRect(v,event,isIn);
                    break;

                case DragEvent.ACTION_DROP:
                    AnimateUtils.animateBackground(v, ACCENTCOLOR, Color.TRANSPARENT, ANIMATIONDELAY);

                    ClipData data = event.getClipData();
                    if(isTouchPointInView(v,event) && getClipType(data).equals("app")){
                        App app = mDatabase.appDao().getById(getClipData(data));
                        Snackbar.make(v,""+app.launches_count,Snackbar.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }

            return switchDragEvent(v,event);
        }
    }

    private static boolean switchDragEvent(View v,DragEvent event){
        switch (event.getAction()){
            case DragEvent.ACTION_DRAG_STARTED:
                return false;
            case DragEvent.ACTION_DRAG_EXITED:
                AnimateUtils.animateBackground(v, ACCENTCOLOR, Color.TRANSPARENT, ANIMATIONDELAY);
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                AnimateUtils.animateBackground(v, ACCENTCOLOR, Color.TRANSPARENT, ANIMATIONDELAY);
                return false;
            default:
                return true;
        }
    }

    private static boolean paintBackgroundRect(View v,DragEvent event,Boolean isIn){
        Log.d("DELETELOCATION",event.getX() +" " + event.getY());

        if(isTouchPointInView(v,event)){
            if(!isIn){
                AnimateUtils.animateBackground(v, Color.TRANSPARENT, ACCENTCOLOR, ANIMATIONDELAY);
                isIn = true;
            }
        }else {
            if(isIn){
                isIn=false;
                AnimateUtils.animateBackground(v, ACCENTCOLOR, Color.TRANSPARENT, ANIMATIONDELAY);
            }
        }

        return isIn;
    }

    private static boolean isTouchPointInView(View v, DragEvent event){
        Rect rect = new Rect();
        Point point = new Point();

        v.getGlobalVisibleRect(rect,point);
        Log.d("OFFFSET",point.toString());

        rect.offset(0,-point.y);

        int x = Math.round(event.getX());
        int y = Math.round(event.getY());

        if(rect.contains(x,y)){
            Log.d("OK",rect.toString() + "\n" + event.getX() +" " + event.getY());
            return true;
        }else {
            Log.d("NOTOK",rect.toString() + "\n" + event.getX() +" " + event.getY());
            return false;
        }
    }


    protected static void uninstallApp(Context context,String package_name) {
        Intent i = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        i.setData(Uri.parse("package:" + package_name));
        //TODO StartactivityFor Result to update on gridview ondelete app
        context.startActivity(i);
        YandexMetrica.reportEvent("Start uninstalling app from popup menu");
    }

    protected static void openAppSettings(Context context, String package_name) {
        Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.setData(Uri.parse("package:" + package_name));
        //TODO StartactivityFor Result to update on gridview ondelete app
        context.startActivity(i);
        YandexMetrica.reportEvent("Start app settings from popup menu");
    }

    protected static String getClipType(ClipData clipData){
        return clipData.getItemAt(0).getText().toString();
    }
    protected static String getClipData(ClipData clipData){
        return clipData.getItemAt(1).getText().toString();
    }

}
