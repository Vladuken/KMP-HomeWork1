package com.vladuken.vladpetrushkevich.activities.profile;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.utils.ThemeChanger;

public class ProfilePageActivity extends AppCompatActivity {


    SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPreferences = getSharedPreferences(getString(R.string.preference_file),0);
        boolean theme = mSharedPreferences.getBoolean(getString(R.string.preference_key_theme), false);

        ThemeChanger.onCreateSetTheme(this,theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View phone = findViewById(R.id.phone_number);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createIntentAndStart(v,R.id.text_view_phone_number,Intent.ACTION_DIAL,"tel:");
            }
        });


        View email = findViewById(R.id.email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createIntentAndStart(v,R.id.text_view_email,Intent.ACTION_SENDTO,"mailto:");
            }
        });

        View card = findViewById(R.id.card_number);
        TextView cardTextView = card.findViewById(R.id.text_view_card_number);
        String cardNumber = cardTextView.getText().toString();

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("",cardNumber);
                clipboard.setPrimaryClip(clip);

                Snackbar.make(v,getString(R.string.copied),Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.dismiss), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //TODO
                            }
                        })
                        .show();
            }
        });

        View instagram = findViewById(R.id.instagram);
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createIntentAndStart(v,R.id.text_view_instagram,Intent.ACTION_VIEW,"http:");
            }
        });

        View github = findViewById(R.id.github);
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createIntentAndStart(v,R.id.text_view_github,Intent.ACTION_VIEW,"http:");
            }
        });




//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        changeStatusBarColor();
    }

    private void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    protected void createIntentAndStart(View v, int textViewId, final String intentAction, String uriPrefix){
        Intent i = new Intent(intentAction);
        TextView textView = v.findViewById(textViewId);
        String s = textView.getText().toString();
        i.setData(Uri.parse(uriPrefix + s));
        startActivity(i);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            this.onBackPressed();
            return true;
        }

        return false;
    }
}
