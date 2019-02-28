package com.vladuken.vladpetrushkevich.activities.main.fragments.desktop;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.LoadIconTask;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.entity.App;
import com.vladuken.vladpetrushkevich.db.entity.DesktopItem;

public class DesktopItemViewHolder extends RecyclerView.ViewHolder {

    private final ImageView mAppIcon;
    private final TextView mAppTitle;

//    private int mIconLayoutId;
//    private int mTitleLayoutId;

    private final View mView;

    private DesktopItem mDesktopItem;
    private final AppDatabase mDatabase;

    public DesktopItemViewHolder(@NonNull View itemView,
                                 AppDatabase database,
                                 int iconLayoutId,
                                 int titleLayoutId){
        super(itemView);
        mView = itemView;
        mAppIcon = itemView.findViewById(iconLayoutId);
        mAppTitle = itemView.findViewById(titleLayoutId);
        mDatabase = database;
    }

    public void bind(DesktopItem item){



        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mView,item.screenId + " " + item.row + " " + item.column,Snackbar.LENGTH_SHORT)
                        .show();
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
        }else{
            //TODO bind empty item bind();
            bind();
        }
    }

    private void bind(){

    }


}
