package com.vladuken.vladpetrushkevich.activities.main.fragments;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
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

public final class TopBarDragUtil {

    static final int ACCENTCOLOR = Color.RED;
    static final int ANIMATIONDELAY = 300;

    private TopBarDragUtil() {
    }

    public static void setupTopBarDraggable(LinearLayout mTopBar, AppDatabase database){

        //make top bar invisible
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0);

        mTopBar.setLayoutParams(params);
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

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);

                    params.weight = v.getResources().getInteger(R.integer.top_bar_layout_weight);
                    v.setLayoutParams(params);
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    Log.d("LOCATION",event.getX() +" " + event.getY());
                    dispatchToChild((LinearLayout) v,event);
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return false;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    dispatchToChild((LinearLayout) v,event);

                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0);

                    v.setLayoutParams(params2);
                    break;
                case DragEvent.ACTION_DROP:
                    dispatchToChild((LinearLayout) v,event);
                    break;
                default:
                    break;
            }

            return true;
        }

        private void dispatchToChild(LinearLayout v, DragEvent event){
            for(int i = 0; i < v.getChildCount(); i++){
                View child = v.getChildAt(i);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                    if(isTouchPointInView(child,event)){
                        child.dispatchDragEvent(event);
                    }
                }else {
                    child.dispatchDragEvent(event);
                }
            }
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

                    if(isTouchPointInView(v,event)
                            && getClipType(data).equals("app")){
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
                    if(isTouchPointInView(v,event)
                            && getClipType(data).equals("app")){
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
                    if(isTouchPointInView(v,event)
                            && getClipType(data).equals("app")){
                        App app = mDatabase.appDao().getById(getClipData(data));
                        //TODO Add messahe about
                        Resources resources = v.getResources();

                        String message = resources.getString(R.string.launched)
                                + " "
                                + app.launches_count
                                + " "
                                + resources.getString(R.string.times);

                        Snackbar.make(v,message,Snackbar.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
            return switchDragEvent(v,event);
        }
    }

    static boolean switchDragEvent(View v,DragEvent event){
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

    static boolean paintBackgroundRect(View v,DragEvent event,Boolean isIn){
        if(isTouchPointInView(v,event)){
            if(!isIn){
                AnimateUtils.animateBackground(v, Color.TRANSPARENT, ACCENTCOLOR, ANIMATIONDELAY);
                return true;
            }
        }else {
            if(isIn){
                AnimateUtils.animateBackground(v, ACCENTCOLOR, Color.TRANSPARENT, ANIMATIONDELAY);
                return false;
            }
        }

        return isIn;
    }

    static boolean isTouchPointInView(View v, DragEvent event){
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

    static void uninstallApp(Context context,String package_name) {
        Intent i = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        i.setData(Uri.parse("package:" + package_name));

        //TODO StartactivityFor Result to update on gridview ondelete app
        context.startActivity(i);
        YandexMetrica.reportEvent("Start uninstalling app");
    }

    static void openAppSettings(Context context, String package_name) {
        Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.setData(Uri.parse("package:" + package_name));
        //TODO StartactivityFor Result to update on gridview ondelete app
        context.startActivity(i);
        YandexMetrica.reportEvent("Start app settings from popup menu");
    }

    static String getClipType(ClipData clipData){
        return clipData.getItemAt(0).getText().toString();
    }

    static String getClipData(ClipData clipData){
        return clipData.getItemAt(1).getText().toString();
    }

}
