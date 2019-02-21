package com.vladuken.vladpetrushkevich.activities.main.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vladuken.vladpetrushkevich.R;

public class ProfileCardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main,container,false);

//        TextView gitHubLink;
//        gitHubLink = v.findViewById(R.id.github_link_text_view);
//        gitHubLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String link = getResources().getString(R.string.github_link);
//                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
//                startActivity(i);
//            }
//        });
//        getActivity().setContentView(R.layout.activity_scrolling);


//        Toolbar toolbar = v.findViewById(R.id.main_toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return v;
    }

    public static ProfileCardFragment newInstance(){
        return new ProfileCardFragment();
    }
}
