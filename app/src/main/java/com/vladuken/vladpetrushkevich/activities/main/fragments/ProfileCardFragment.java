package com.vladuken.vladpetrushkevich.activities.main.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

        TextView gitHubLink;
        gitHubLink = v.findViewById(R.id.github_link_text_view);
        gitHubLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = getResources().getString(R.string.github_link);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(i);
            }
        });

        return v;
    }

    public static ProfileCardFragment newInstance(){
        return new ProfileCardFragment();
    }
}
