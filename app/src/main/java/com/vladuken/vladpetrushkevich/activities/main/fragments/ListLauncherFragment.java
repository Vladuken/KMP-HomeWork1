package com.vladuken.vladpetrushkevich.activities.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vladuken.vladpetrushkevich.R;

import java.util.ArrayList;
import java.util.List;

public class ListLauncherFragment extends Fragment {

    protected final List<Integer> mItems = new ArrayList<>();
    protected final ListAdapter mAdapter = new ListAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_list,container,false);

        RecyclerView mRecyclerView = v.findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton mFloatingActionButton = v.findViewById(R.id.activity_list_fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItems.add(1);
                mAdapter.notifyDataSetChanged();
            }
        });

        return v;
    }


    public class ListAdapter extends RecyclerView.Adapter<ListViewHolder>{

        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_view,parent,false);

            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, int position) {
            //TODO
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
    }


    public static ListLauncherFragment newInstance(){
        return new ListLauncherFragment();
    }

}
