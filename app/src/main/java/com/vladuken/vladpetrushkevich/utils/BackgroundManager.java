package com.vladuken.vladpetrushkevich.utils;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.view.View;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.BackgroundReceiver;
import com.vladuken.vladpetrushkevich.utils.picasso.LoadImageJobService;

import java.io.File;

public class BackgroundManager {

    public static void setupBackgroundWithOffset(View view, String fullPath,Long offset){

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    Thread.sleep(offset);
                }catch (InterruptedException e){
                }

                SharedPreferences preferences = view.getContext().getSharedPreferences(
                        view.getContext().getString(R.string.preference_file),0
                );


                ComponentName componentName = new ComponentName(view.getContext(), LoadImageJobService.class);

                PersistableBundle bundle = new PersistableBundle();

                String link = getLink(view.getContext(),preferences);
                bundle.putString("link", link);
                bundle.putString("path", fullPath);

                String stringPeriod = view
                        .getContext()
                        .getSharedPreferences(view.getContext().getString(R.string.preference_file),0)
                        .getString(
                                view.getContext().getString(R.string.preference_background_renew_frequency),
                                "900000");
                long period = Long.decode(stringPeriod);



                JobInfo.Builder jobInfoBuilder = new JobInfo.Builder(0,componentName)
                        .setPeriodic(period)
                        .setOverrideDeadline(0)
                        .setExtras(bundle);
                JobInfo jobInfo = jobInfoBuilder.build();


                JobScheduler jobScheduler = (JobScheduler) view.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
                jobScheduler.schedule(jobInfo);
            }
        }).start();
    }

    private static String getLink(Context context,SharedPreferences preferences){
        return preferences.getString(context.getString(
                R.string.preference_source_link),
                "https://loremflickr.com/720/1080");
    }

}
