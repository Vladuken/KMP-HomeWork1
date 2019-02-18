package com.vladuken.vladpetrushkevich.activities.welcome.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.vladuken.vladpetrushkevich.R;

public class ForthPageFragment extends Fragment {

    protected RadioGroup mLayoutRadioGroup;
    protected int portrait_rows;
    protected int landscape_rows;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_slide4,container,false);

        final RelativeLayout layout4 = v.findViewById(R.id.screen4id);
        mLayoutRadioGroup = layout4.findViewById(R.id.radio_group_layout);
        mLayoutRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = mLayoutRadioGroup.getCheckedRadioButtonId();

                if (selectedId == R.id.standard_layout_rdb) {
                    portrait_rows = getResources().getInteger(R.integer.standard_portrait_layout_span);
                    landscape_rows = getResources().getInteger(R.integer.standard_landscape_layout_span);
                } else if (selectedId == R.id.compact_layout_rdb) {
                    portrait_rows = getResources().getInteger(R.integer.compact_portrait_layout_span);
                    landscape_rows = getResources().getInteger(R.integer.compact_landscape_layout_span);
                }
                savePreferences();
            }
        });

        SharedPreferences sharedPreferences = this.getActivity()
                .getSharedPreferences(getString(R.string.preference_file),0);

        int portraitRows = sharedPreferences.getInt(
                getString(R.string.preference_portrait_rows),
                getResources().getInteger(R.integer.standard_portrait_layout_span)
        );

        int landscapeRows = sharedPreferences.getInt(
                getString(R.string.preference_landscape_rows),
                getResources().getInteger(R.integer.standard_landscape_layout_span)
        );

        if(portraitRows == getResources().getInteger(R.integer.compact_portrait_layout_span) &&
        landscapeRows == getResources().getInteger(R.integer.compact_landscape_layout_span)){
            ((RadioButton) layout4.findViewById(R.id.compact_layout_rdb)).setChecked(true);
        }else {
            ((RadioButton) layout4.findViewById(R.id.standard_layout_rdb)).setChecked(true);
        }

        return v;
    }

    public static ForthPageFragment newInstance(){
        return new ForthPageFragment();
    }

    protected void savePreferences(){
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(getString(R.string.preference_file),0).edit();
        editor.putInt(getString(R.string.preference_portrait_rows), portrait_rows);
        editor.putInt(getString(R.string.preference_landscape_rows), landscape_rows);

        editor.commit();
    }
}

