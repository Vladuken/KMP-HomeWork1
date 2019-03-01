package com.vladuken.vladpetrushkevich.activities.main.fragments.desktop;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.listeners.AppLongClickListener;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.entity.App;
import com.vladuken.vladpetrushkevich.db.entity.DesktopItem;

public class DesktopItemViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "DesktopItemViewHolder";
    private final ImageView mAppIcon;
    private final TextView mAppTitle;

//    private int mIconLayoutId;
//    private int mTitleLayoutId;

    private final View mView;

    private DesktopItem mDesktopItem;
    private final AppDatabase mDatabase;

    public DesktopItemViewHolder(@NonNull View itemView,
                                 DesktopItem desktopItem,
                                 AppDatabase database,
                                 int iconLayoutId,
                                 int titleLayoutId){
        super(itemView);
        mView = itemView;
        mDesktopItem = desktopItem;
        mAppIcon = itemView.findViewById(iconLayoutId);
        mAppTitle = itemView.findViewById(titleLayoutId);
        mDatabase = database;
    }

    public void bind(DesktopItem item){

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mView,item.screenPosition + " " + item.row + " " + item.column,Snackbar.LENGTH_SHORT)
                        .show();
            }
        });

        mView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()){
                    case DragEvent.ACTION_DRAG_ENTERED:
                        itemView.setBackgroundColor(Color.RED);
                        Log.d(TAG,"Drag Entered" + " r:" +item.row + " c:" + item.column);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        itemView.setBackgroundColor(Color.TRANSPARENT);
                        Log.d(TAG,"Drag Exited"+ " r:" +item.row + " c:" + item.column);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.d(TAG,"Drag Ended"+ " r:" +item.row + " c:" + item.column);
                        break;
                    case DragEvent.ACTION_DROP:
//                        itemView.setBackgroundColor(Color.GREEN);
                        ClipData.Item clipData = event.getClipData().getItemAt(0);
                        String packageName = clipData.getText().toString();
                        bind(mDatabase.appDao().getById(packageName));
//                        Snackbar.make(v,clipData.getText(),Snackbar.LENGTH_LONG).show();

                        Log.d(TAG,"Drag Dropped"+ " r:" +item.row + " c:" + item.column);
                        break;
                }

                return true;
            }
        });

        switch (item.itemType){
            case "empty":
                bind();
                break;
            case "app":
                App app = mDatabase.appDao().getById(item.itemData);
                if(app == null){
                    app = new App(item.itemType,0);
                }
                bind(app);
                break;
            case "link":
                break;
            default:
                break;
        }
    }

    private void bind(App app){
        ResolveInfo resolveInfo = null;
        PackageManager pm = mView.getContext().getPackageManager();

        Intent i = pm.getLaunchIntentForPackage(app.package_name);
        if(i != null){
            resolveInfo = pm.resolveActivity(i,0);

            Drawable icon = resolveInfo.loadIcon(pm);
            String appName = resolveInfo.loadLabel(pm).toString();

            mAppIcon.setImageDrawable(icon);
            mAppTitle.setText(appName);

            mView.setOnClickListener(new DesktopAppOnClickListener(mDatabase,app));
            mView.setOnLongClickListener(new AppLongClickListener(app,mView));

            mDesktopItem.itemType = "app";
            mDesktopItem.itemData = app.package_name;
            mDatabase.desckopAppDao().update(mDesktopItem);
        }else{
            //TODO bind empty item bind();
            bind();
        }
    }

    private void bind(){

        //mView.setOnClickListener();
    }


}
