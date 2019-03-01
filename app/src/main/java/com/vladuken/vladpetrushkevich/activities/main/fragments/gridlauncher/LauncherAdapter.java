package com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher;

import android.content.ClipData;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.fragments.LauncherViewHolder;
import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.listeners.AppLongClickListener;
import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.listeners.IconOnClickListener;
import com.vladuken.vladpetrushkevich.db.AppDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LauncherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int POPULAR_APP_SIZE = 5;
    private static final int POPULAR_APP_SIZE_WITH_HEADER_AND_FOOTER = POPULAR_APP_SIZE + 1;


    private static final int POPULAR_GROUP_TITLE = 1;
    private static final int POPULAR_APP = 2;
    private static final int DIVIDER = 3;


    private List<ResolveInfo> mPopularAppInfo = new ArrayList<>();
    private final List<ResolveInfo> mInstalledAppInfo;
    private final Map<ResolveInfo,Drawable> mIcons;

    private final AppDatabase mDatabase;

    private final boolean mIsGridView;


    public LauncherAdapter(List<ResolveInfo> installedAppsInfo, AppDatabase database, boolean isGridView) {
        mInstalledAppInfo =  installedAppsInfo;
        mDatabase = database;
        mIcons = new HashMap<>();

        mIsGridView = isGridView;
    }

    public List<ResolveInfo> getInstalledAppInfo() {
        return mInstalledAppInfo;
    }

    public List<ResolveInfo> getPopularAppInfo() {
        return mPopularAppInfo;
    }

    public void setPopularAppInfo(List<ResolveInfo> popularAppInfos){
        mPopularAppInfo = popularAppInfos;
    }


    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        if(viewType == POPULAR_GROUP_TITLE){
            return new FooterViewHolder(inflateView(parent,R.layout.popular_group_title));
        }else if (viewType == DIVIDER){
            return new FooterViewHolder(inflateView(parent,R.layout.horizontal_divider));
        }else{
            if(mIsGridView){
                //TODO use OOP to create two classes
                return new LauncherViewHolder(
                        inflateView(parent,R.layout.item_icon_view),
                        mDatabase,
                        R.id.grid_app_icon,
                        R.id.grid_app_title
                );
            }else {
                return new LauncherViewHolder(
                        inflateView(parent,R.layout.item_list_view),
                        mDatabase,
                        R.id.list_app_icon,
                        R.id.list_app_title
                );
            }
        }
    }

    private View inflateView(ViewGroup parent, int id){
        return LayoutInflater.from(parent.getContext())
                .inflate(id,parent,false);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ResolveInfo resolveInfo;

        int pos = position;
        if(!mPopularAppInfo.isEmpty()){
            if(pos == 0){
                //TODO
            }else if(pos < POPULAR_APP_SIZE_WITH_HEADER_AND_FOOTER) {
                resolveInfo = mPopularAppInfo.get(pos - 1);
                bindLauncherViewHolder(viewHolder,resolveInfo);
            }else if(pos == POPULAR_APP_SIZE_WITH_HEADER_AND_FOOTER){
                //TODO
            }else {
                pos = pos - POPULAR_APP_SIZE_WITH_HEADER_AND_FOOTER - 1;
                resolveInfo = mInstalledAppInfo.get(pos);
                bindLauncherViewHolder(viewHolder,resolveInfo);
            }
        } else {
            resolveInfo = mInstalledAppInfo.get(pos);
            bindLauncherViewHolder(viewHolder,resolveInfo);
        }

    }

    private void bindLauncherViewHolder(RecyclerView.ViewHolder viewHolder,ResolveInfo resolveInfo){
        if(viewHolder instanceof LauncherViewHolder) {
            LauncherViewHolder vh = (LauncherViewHolder) viewHolder;
            if (mIcons.get(resolveInfo) == null) {
                new LoadIconTask(this,vh, mIcons, resolveInfo, viewHolder.itemView.getContext().getPackageManager()).execute();

            } else {
                vh.bind(resolveInfo, mIcons.get(resolveInfo));
            }


            final GestureDetector gestureDetector = new GestureDetector(vh.itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public void onLongPress(MotionEvent e) {
//                    Snackbar.make(vh.itemView, "Longpress", Snackbar.LENGTH_SHORT).show();
                    super.onLongPress(e);
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    ClipData data = ClipData.newPlainText("text", resolveInfo.activityInfo.packageName);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(vh.itemView);

                    vh.itemView.startDragAndDrop(data, shadowBuilder, vh.itemView, 0);
//                    Snackbar.make(vh.itemView, "Scrolling detected", Snackbar.LENGTH_SHORT).show();
                    return super.onScroll(e1, e2, distanceX, distanceY);
                }
            });


            if(vh.isBinded()){
                vh.itemView.setOnClickListener(new IconOnClickListener(vh, this));
                vh.itemView.setOnLongClickListener(new AppLongClickListener(vh.getApp(),vh.itemView));
            }

            vh.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });

            vh.itemView.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    if (event.getAction() == DragEvent.ACTION_DRAG_STARTED) {
                        Snackbar.make(v, "dragstarted", Snackbar.LENGTH_SHORT);
                        return true;
                    }

                    return true;
                }
            });
        }
    }

    public boolean isGroupTitle(int position){
        return  !mPopularAppInfo.isEmpty() &&
                (position == 0 || position == POPULAR_APP_SIZE_WITH_HEADER_AND_FOOTER);
    }

    @Override
    public int getItemCount() {
        int popularAppsCount = 0;
        if(!mPopularAppInfo.isEmpty()){
            popularAppsCount = POPULAR_APP_SIZE_WITH_HEADER_AND_FOOTER + 1;
        }

        return mInstalledAppInfo.size() + popularAppsCount;
    }

    @Override
    public int getItemViewType(int position) {

        if (mPopularAppInfo.isEmpty()) {
            return super.getItemViewType(position);
        }

        if (position == 0){
            return POPULAR_GROUP_TITLE;//TODO APP TITLE
        }
        if (position < POPULAR_APP_SIZE_WITH_HEADER_AND_FOOTER){
            return POPULAR_APP;
        }
        if (position == POPULAR_APP_SIZE_WITH_HEADER_AND_FOOTER){
            return DIVIDER;

        }else {
            return super.getItemViewType(position);
        }
    }


}