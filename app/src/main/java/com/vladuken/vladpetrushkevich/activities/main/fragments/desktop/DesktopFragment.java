package com.vladuken.vladpetrushkevich.activities.main.fragments.desktop;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.SquareView;
import com.vladuken.vladpetrushkevich.activities.main.fragments.LauncherViewHolder;
import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.listeners.IconLongClickListener;
import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.listeners.IconOnClickListener;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.SingletonDatabase;

import java.util.List;
import java.util.Random;

public class DesktopFragment extends Fragment {


    private static final String ARG_ROWS = "rows";
    private static final String ARG_COLUMNS = "columns";

//    protected RecyclerView mRecyclerView;
    protected GridLayout mTableLayout;

    int mRows;
    int mColumns;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRows = getArguments().getInt(ARG_ROWS);
        mColumns = getArguments().getInt(ARG_COLUMNS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.desktop_table_layout,container,false);

        mTableLayout = v.findViewById(R.id.desktop_grid_layout);

        setupGridLayout(mRows,mColumns);
        return v;
    }

    private void setupGridLayout(int rows, int colums){

        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);




        for(int c = 0; c < colums; c++){
            for(int r = 0; r < rows; r++){

                View myview =  View.inflate(getContext(),R.layout.desctop_icon_view,null);

                SquareView imageView = myview.findViewById(R.id.grid_app_icon);

//                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,0));

//                myview.setLayoutParams(new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT
//                ));

                LauncherViewHolder viewHolder = new LauncherViewHolder(
                        myview,
                        SingletonDatabase.getInstance(getContext()),
                        R.id.grid_app_icon,
                        R.id.grid_app_title
                );


                Random random = new Random();
                ResolveInfo buffResolveInfo = activities.get(random.nextInt(activities.size()));
                viewHolder.bind(buffResolveInfo,buffResolveInfo.loadIcon(pm));

//                viewHolder.itemView.setOnClickListener(new IconOnClickListener(viewHolder,this));
                viewHolder.itemView.setOnLongClickListener(new IconLongClickListener(viewHolder));


//                ImageView bufferImageView = new ImageView(getContext());
//                bufferImageView.setImageResource(R.drawable.photo);


                GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                        GridLayout.spec(c, 1f),
                        GridLayout.spec(r,1f)
                );

                params.height = 0;
                params.width = 0;

                myview.setLayoutParams (params);

                mTableLayout.addView(myview);
            }
        }
    }

    public static DesktopFragment newInstance(int rows, int columns){

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_COLUMNS,columns);
        bundle.putInt(ARG_ROWS,rows);


        DesktopFragment fragment = new DesktopFragment();
        fragment.setArguments(bundle);

        return fragment;
    }
}
