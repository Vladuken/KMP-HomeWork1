package com.vladuken.vladpetrushkevich.activities.main.fragments.desktop;

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
import android.widget.TableLayout;
import android.widget.TableRow;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.SquareView;

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

        for(int c = 0; c < colums; c++){
            for(int r = 0; r < rows; r++){
                ImageView oImageView = new ImageView(getContext());
                oImageView.setImageResource(R.drawable.photo);
                GridLayout.LayoutParams param =new GridLayout.LayoutParams();

                GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                        GridLayout.spec(c, GridLayout.FILL,1f),
                        GridLayout.spec(r,GridLayout.FILL,1f)
                );

                params.height = 0;
                params.width = 0;

                oImageView.setLayoutParams (params);

                mTableLayout.addView(oImageView);
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
