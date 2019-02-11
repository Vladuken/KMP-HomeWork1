package com.vladuken.vladpetrushkevich;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

public class ListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecyclerView = findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ListAdapter adapter = new ListAdapter();
        mRecyclerView.setAdapter(adapter);

    }

    public class ListAdapter extends RecyclerView.Adapter<ListViewHolder>{

        private final Random mRandom = new Random();
        private final SparseIntArray mColorMap = new SparseIntArray();

        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_view,parent,false);

            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, int position) {

        }

        @Override
        public int getItemCount() {
            return 100;
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
