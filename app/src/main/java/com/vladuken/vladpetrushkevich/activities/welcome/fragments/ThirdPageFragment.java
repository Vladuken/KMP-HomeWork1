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

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.utils.ThemeChanger;
import com.yandex.metrica.YandexMetrica;

public class ThirdPageFragment extends Fragment {

    protected RadioGroup mThemeRadioGroup;
    protected boolean theme; // 0 is light 1 is dark

    protected RadioButton mLightThemeRdb;
    protected RadioButton mDarkThemeRdb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_slide3,container,false);
        final View layout3 = v.findViewById(R.id.screen3id);
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
                YandexMetrica.reportEvent("Theme changed in welcome page");

            }
        });

        mLightThemeRdb = layout3.findViewById(R.id.light_theme_radiobutton);
        mLightThemeRdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rdb = (RadioButton) v;
                if(rdb.isChecked()){
                    ThemeChanger.reloadActivity(getActivity());
                }
            }
        });
        mDarkThemeRdb = layout3.findViewById(R.id.dark_theme_radiobutton);
        mDarkThemeRdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rdb = (RadioButton) v;
                if(rdb.isChecked()){
                    ThemeChanger.reloadActivity(getActivity());
                }
            }
        });


        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.preference_file),0);
        boolean buffTheme = sharedPreferences.getBoolean(getString(R.string.preference_key_theme),false);
        if(!buffTheme){
            mLightThemeRdb.setChecked(true);
        }else {
            mDarkThemeRdb.setChecked(true);
        }
        return v;
    }

    public static ThirdPageFragment newInstance(){
        return new ThirdPageFragment();
    }

    protected void savePreferences(){
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences(getString(R.string.preference_file),0).edit();
        editor.putBoolean(getString(R.string.preference_key_theme), theme);
        editor.apply();
    }
}
