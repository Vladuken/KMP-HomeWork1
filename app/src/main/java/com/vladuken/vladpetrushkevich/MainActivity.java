package com.vladuken.vladpetrushkevich;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class MainActivity extends AppCompatActivity {

    //private CardView mCardView;
    //private TextView mFullName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCenter.start(getApplication(), "77770b97-7fc5-4620-b874-95c26bb3e37c", Analytics.class, Crashes.class);

        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        //mCardView = findViewById(R.id.photo_card_view);

        //mFullName = findViewById(R.id.full_name_text_view);
        TextView gitHubLink;
        gitHubLink = findViewById(R.id.github_link_text_view);
        gitHubLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = getResources().getString(R.string.github_link);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(i);
            }
        });
    }


}
