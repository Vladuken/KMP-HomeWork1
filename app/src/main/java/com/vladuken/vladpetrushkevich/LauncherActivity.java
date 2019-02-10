package com.vladuken.vladpetrushkevich;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Random;

public class LauncherActivity extends AppCompatActivity {



    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref =
                getSharedPreferences(Constants.SharedPreferences.APP_PREFERENCES,0);
        int theme = sharedPref.getInt(Constants.SharedPreferences.THEME,
                getResources().getInteger(R.integer.standard_layout_theme));

        if(theme == 0){
            setTheme(R.style.AppTheme);
        }else{
            setTheme(R.style.AppThemeDark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        mRecyclerView = findViewById(R.id.icon_recycler_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPref = getSharedPreferences(
                Constants.SharedPreferences.APP_PREFERENCES, 0);

        int portraitSpanCount =
                sharedPref.getInt(
                        Constants.SharedPreferences.PORTRAITE_ROWS,
                        getResources().getInteger(R.integer.standard_portrait_layout_span)
                );
        int landscapeSpanCount =
                sharedPref.getInt(
                        Constants.SharedPreferences.LANDSCAPE_ROWS,
                        getResources().getInteger(R.integer.standard_landscape_layout_span)
                );

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, portraitSpanCount));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, landscapeSpanCount));
        }

        LauncherAdapter mAdapter;
        mAdapter = new LauncherAdapter();
        mRecyclerView.setAdapter(mAdapter);
        int offset = getResources().getDimensionPixelOffset(R.dimen.offset);
        mRecyclerView.addItemDecoration(new LauncherItemDecoration(offset));


    }

    public class LauncherAdapter extends RecyclerView.Adapter<LauncherViewHolder>{

        private final Random mRandom = new Random();
        private final SparseIntArray mColorMap = new SparseIntArray();

        public LauncherAdapter() {
            //TODO
        }

        @NonNull
        @Override
        public LauncherViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int position) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_icon_view,parent,false);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int color = Color.TRANSPARENT;
                    Drawable background = v.getBackground();
                    if (background instanceof ColorDrawable)
                        color = ((ColorDrawable) background).getColor();
                    String textColor =  String.format("#%06X", (0xFFFFFF & color));
                    Snackbar.make(v,textColor,Snackbar.LENGTH_SHORT)
                            .show();
                    return true;
                }
            });
            return new LauncherViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LauncherViewHolder viewHolder, int position) {
            viewHolder.itemView.setBackgroundColor(getColor(position));
        }

        @Override
        public int getItemCount() {
            return 100;
        }

        private int getColor(int position){
            Integer color = mColorMap.get(position);
            if(color == 0)
            {
                color = Color.rgb(mRandom.nextInt(256),
                        mRandom.nextInt(256),
                        mRandom.nextInt(256)
                );

                mColorMap.put(position,color);
            }

            return color;
        }
    }

    public class LauncherViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public LauncherViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
    }

}
