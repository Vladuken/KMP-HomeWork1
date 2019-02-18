package com.vladuken.vladpetrushkevich.activities.welcome.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.vladuken.vladpetrushkevich.R;

public class ThirdPageFragment extends Fragment {

    protected RadioGroup mThemeRadioGroup;
    protected boolean theme; // 0 is light 1 is dark

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_slide3,container,false);
        final RelativeLayout layout3 = v.findViewById(R.id.screen3id);
        mThemeRadioGroup = layout3.findViewById(R.id.radio_group_theme);
        mThemeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = mThemeRadioGroup.getCheckedRadioButtonId();
                if (selectedId == R.id.light_theme_radiobutton) {
                    theme = false;
                    savePreferences();
//                                findViewById(R.id.dark_theme_radiobutton).setBackgroundColor(Color.argb(0,0,0,0));
                } else if (selectedId == R.id.dark_theme_radiobutton) {
                    theme = true;
                    savePreferences();
//                                findViewById(R.id.dark_theme_radiobutton).setBackgroundColor(Color.argb(255,0,0,0));
                }
            }
        });

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.preference_file),0);
        boolean buffTheme = sharedPreferences.getBoolean(getString(R.string.preference_key_theme),false);
        if(!buffTheme){
            ((RadioButton) layout3.findViewById(R.id.light_theme_radiobutton)).setChecked(true);
        }else {
            ((RadioButton) layout3.findViewById(R.id.dark_theme_radiobutton)).setChecked(true);
        }
        return v;
    }

    public static ThirdPageFragment newInstance(){
        return new ThirdPageFragment();
    }

    protected void savePreferences(){
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences(getString(R.string.preference_file),0).edit();
        editor.putBoolean(getString(R.string.preference_key_theme), theme);
        editor.commit();
    }
}
