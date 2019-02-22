package com.vladuken.vladpetrushkevich.activities.welcome;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vladuken.vladpetrushkevich.R;

public class TutorialFragment extends Fragment {

    private static final String ARG_TEXT = "TEXT";
    private static final String ARG_IMAGE_ID = "DRAWABLE";

    private String mText;
    private int mImageId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mText = getArguments().getString(ARG_TEXT);
        mImageId = getArguments().getInt(ARG_IMAGE_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tutorial_slide,container,false);

        ImageView imageView = v.findViewById(R.id.tutorial_image);
        TextView textView = v.findViewById(R.id.tutorial_text);

        imageView.setImageDrawable(getResources().getDrawable(mImageId));
        textView.setText(mText);

        return v;
    }

    public static TutorialFragment newInstance(String text, int imageId){

        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);

        args.putInt(ARG_IMAGE_ID,imageId);


        TutorialFragment fragment = new TutorialFragment();
        fragment.setArguments(args);

        return fragment;
    }
}
