package com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.fragments.LauncherViewHolder;
import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.listeners.IconLongClickListener;
import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.listeners.IconOnClickListener;
import com.vladuken.vladpetrushkevich.db.AppDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridLauncherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int POPULAR_APP_SIZE = 5;
    private static final int POPULAR_APP_SIZE_WITH_HEADER_AND_FOOTER = POPULAR_APP_SIZE + 1;


    private static final int POPULAR_GROUP_TITLE = 1;
    private static final int POPULAR_APP = 2;
    private static final int DIVIDER = 3;


    private List<ResolveInfo> mPopularAppInfo;
    private final List<ResolveInfo> mInstalledAppInfo;
    private final Map<ResolveInfo,Drawable> mIcons;

    private final AppDatabase mDatabase;
    private Context  mContext;


    public GridLauncherAdapter(List<ResolveInfo> installedAppsInfo, AppDatabase database, Context context) {
        mInstalledAppInfo =  installedAppsInfo;
        mDatabase = database;
        mContext = context;
        mIcons = initIconHashMap(mContext,mInstalledAppInfo);
    }

    private Map initIconHashMap(Context context,List<ResolveInfo> installedAppInfo){

        HashMap<ResolveInfo, Drawable> iconMap = new HashMap<>();
        for (ResolveInfo appInfo : installedAppInfo){
            iconMap.put(appInfo,appInfo.loadIcon(context.getPackageManager()));
        }

        return iconMap;
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
            return new LauncherViewHolder(
                    inflateView(parent,R.layout.item_icon_view),
                    mDatabase,
                    R.id.grid_app_icon,
                    R.id.grid_app_title
            );
        }
    }

    private View inflateView(ViewGroup parent, int id){
        return LayoutInflater.from(parent.getContext())
                .inflate(id,parent,false);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ResolveInfo resolveInfo;

        if(mPopularAppInfo != null){
            if(position == 0){
                //TODO
            }else if(position < POPULAR_APP_SIZE_WITH_HEADER_AND_FOOTER) {
                resolveInfo = mPopularAppInfo.get(position);
                bindLauncherViewHolder(viewHolder,resolveInfo);
            }else if(position == POPULAR_APP_SIZE_WITH_HEADER_AND_FOOTER){
                //TODO
            }else {
                position = position - POPULAR_APP_SIZE_WITH_HEADER_AND_FOOTER - 1;
                resolveInfo = mInstalledAppInfo.get(position);
                bindLauncherViewHolder(viewHolder,resolveInfo);
            }
        } else {
            if(position == 128)
            {
                Log.d("a","a");
            }
            resolveInfo = mInstalledAppInfo.get(position);
            bindLauncherViewHolder(viewHolder,resolveInfo);
        }

    }

    private void bindLauncherViewHolder(RecyclerView.ViewHolder viewHolder,ResolveInfo resolveInfo){
        if(viewHolder instanceof LauncherViewHolder){
            LauncherViewHolder vh = (LauncherViewHolder) viewHolder;
            vh.bind(resolveInfo,mIcons.get(resolveInfo));

            vh.itemView.setOnClickListener(new IconOnClickListener(vh));
            vh.itemView.setOnLongClickListener(new IconLongClickListener(vh));
        }

    }

    public boolean isGroupTitle(int position){
        if(mPopularAppInfo != null){
            if(position == 0 || position == POPULAR_APP_SIZE_WITH_HEADER_AND_FOOTER)
                return true;
        }

        return false;
    }

    @Override
    public int getItemCount() {
        int popularAppsCount = 0;
        if(mPopularAppInfo != null){
            popularAppsCount = POPULAR_APP_SIZE_WITH_HEADER_AND_FOOTER + 1;
        }

        return mInstalledAppInfo.size() + popularAppsCount;
    }

    @Override
    public int getItemViewType(int position) {

        int popularAppsCount = 0;

        if (mPopularAppInfo == null){
            return super.getItemViewType(position);
        }else {
            popularAppsCount = mPopularAppInfo.size();
        }



        if(popularAppsCount > 0){
            if(position == 0) return POPULAR_GROUP_TITLE;//TODO APP TITLE
            if(position < POPULAR_APP_SIZE_WITH_HEADER_AND_FOOTER){
                return POPULAR_APP;
            }
            if (position == POPULAR_APP_SIZE_WITH_HEADER_AND_FOOTER)
                return DIVIDER;
            else {
                return super.getItemViewType(position);
            }
        }

        return super.getItemViewType(position);
    }
}