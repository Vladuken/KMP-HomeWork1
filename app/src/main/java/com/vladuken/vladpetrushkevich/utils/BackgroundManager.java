package com.vladuken.vladpetrushkevich.utils;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;
import android.view.View;

import com.vladuken.vladpetrushkevich.activities.main.BackgroundReceiver;
import com.vladuken.vladpetrushkevich.utils.picasso.LoadImageJobService;

import java.io.File;

public class BackgroundManager {

    public static void setupBackground(View view, String fullPath){

////
////        try{
////
////            Thread.sleep(2000);
////        }catch (InterruptedException e){
////        }
//
//        ComponentName componentName = new ComponentName(view.getContext(), LoadImageJobService.class);
//
//        PersistableBundle bundle = new PersistableBundle();
//
//        bundle.putString("link", "https://loremflickr.com/720/1080");
//        bundle.putString("path", fullPath);
//        JobInfo.Builder jobInfoBuilder = new JobInfo.Builder(0,componentName)
////                .setMinimumLatency(Math.max(1,1000*60))
//                .setPeriodic(1000 * 60 * 15 + 1)
////                .setOverrideDeadline(0)
////                .setTriggerContentMaxDelay(1000*30)
//                .setExtras(bundle);
//        JobInfo jobInfo = jobInfoBuilder.build();
//
//        JobScheduler jobScheduler = (JobScheduler) view.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        jobScheduler.schedule(jobInfo);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try{

                    Thread.sleep(100);
                }catch (InterruptedException e){
                }

                ComponentName componentName = new ComponentName(view.getContext(), LoadImageJobService.class);

                PersistableBundle bundle = new PersistableBundle();

                bundle.putString("link", "https://loremflickr.com/720/1080");
                bundle.putString("path", fullPath);
                JobInfo.Builder jobInfoBuilder = new JobInfo.Builder(0,componentName)
//                .setMinimumLatency(Math.max(1,1000*60))
                        .setPeriodic(1000 * 60 * 15 + 1)
                        .setOverrideDeadline(0)
//                .setTriggerContentMaxDelay(1000*30)
                        .setExtras(bundle);
                JobInfo jobInfo = jobInfoBuilder.build();

                JobScheduler jobScheduler = (JobScheduler) view.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
                jobScheduler.schedule(jobInfo);


            }
        }).start();



//        if(!new File(fullPath).exists()){
//            jobInfoBuilder = jobInfoBuilder.setOverrideDeadline(0);
//        }else

//        JobInfo jobInfo = new JobInfo.Builder(0, componentName)
//                .setOverrideDeadline(0)
//                .setPeriodic(1000 * 60 * 15 + 1)
//                .setExtras(bundle)
//                .build();





//        new SetBackgroundAsynkTask(mTableLayout,fullpath).execute();


    }

}
