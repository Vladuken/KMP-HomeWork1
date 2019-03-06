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
import java.util.Calendar;

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

    public static void setupBackground(View view, String fullPath){
        if(!new File(fullPath).exists()){
            setupBackgroundWithOffset(view,fullPath,0L);
        }else {
            Long offset = getOffset(view.getContext());
            setupBackgroundWithOffset(view,fullPath,offset);
        }
//        mBackgroundReceiver = new BackgroundReceiver(view,fullpath);
    }

    private static Long getOffset(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file), 0);
        String stringPeriod = sharedPreferences.getString(context.getString(R.string.preference_background_renew_frequency), "900000");
        Calendar calendar = Calendar.getInstance();
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

//        long period = Long.decode(stringPeriod);

        Integer minutesLeft;
        Integer hoursLeft;

        switch (stringPeriod) {
            case "900000":
                minutesLeft = 15 - (currentMinute % 15);
                hoursLeft = 0;
                break;
            case "3600000":
                minutesLeft = 60 - currentMinute;
                hoursLeft = 0;
                break;
            case "28800000":
                minutesLeft = 60 - currentMinute;
                hoursLeft = 8 - (currentHour % 8);
                break;
            case "86400000":
                minutesLeft = 60 - currentMinute;
                hoursLeft = 24 - currentHour;
                break;
            default:
                minutesLeft = Integer.MAX_VALUE;
                hoursLeft = Integer.MAX_VALUE;
                break;
        }
        return minutesLeft.longValue() * 60000L + hoursLeft.longValue() * 24L * 60L * 60L * 1000L;

    }

}
