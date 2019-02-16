package com.vladuken.vladpetrushkevich;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.microsoft.appcenter.distribute.Distribute;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Fabric.with(this, new Crashlytics());
        AppCenter.start(getApplication(),
                "77770b97-7fc5-4620-b874-95c26bb3e37c",
                Analytics.class,
                Crashes.class,
                Distribute.class);
    }
}
