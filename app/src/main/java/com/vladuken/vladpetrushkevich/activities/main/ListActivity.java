package com.vladuken.vladpetrushkevich.activities.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vladuken.vladpetrushkevich.R;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    protected final List<Integer> mItems = new ArrayList<>();
    protected final ListAdapter mAdapter = new ListAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView mRecyclerView = findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton mFloatingActionButton = findViewById(R.id.activity_list_fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItems.add(1);
                mAdapter.notifyDataSetChanged();
            }
        });
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
}
