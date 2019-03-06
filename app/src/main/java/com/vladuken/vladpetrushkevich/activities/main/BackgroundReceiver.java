package com.vladuken.vladpetrushkevich.activities.main;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.utils.picasso.LoadImageJobService;
import com.vladuken.vladpetrushkevich.utils.picasso.SetBackgroundAsynkTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class BackgroundReceiver extends BroadcastReceiver {

    public static final String UPDATE_BACKGROUND = "com.vladuken.vladpetrushkevich.UPDATE_BACKGROUND";


    public static final String UPDATE_BACKGROUND_ONCE = "com.vladuken.vladpetrushkevich.UPDATE_BACKGROUND_ONCE";


    private View mView;
    private String mPath;

    public BackgroundReceiver(View view, String path) {
        mView = view;
        mPath = path;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(UPDATE_BACKGROUND)){
            new SetBackgroundAsynkTask(mView,mPath).execute();
        }else if (intent.getAction().equals(UPDATE_BACKGROUND_ONCE)){


//            //TODO SAME CODE 1
            SharedPreferences preferences = mView.getContext().getSharedPreferences(
                    mView.getContext().getString(R.string.preference_file),0
            );
//            ComponentName componentName = new ComponentName(mView.getContext(), LoadImageJobService.class);
//
//            PersistableBundle bundle = new PersistableBundle();
//
            String link = getLink(mView.getContext(),preferences);
//            bundle.putString("link", link);
//            bundle.putString("path", mPath);
//
//            Random random = new Random();
//            JobInfo.Builder jobInfoBuilder = new JobInfo.Builder(random.nextInt(),componentName)
//                    .setOverrideDeadline(0)
//                    .setExtras(bundle);
//            JobInfo jobInfo = jobInfoBuilder.build();
//
//
//            JobScheduler jobScheduler = (JobScheduler) mView.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
//            jobScheduler.schedule(jobInfo);

////////////////////////////////////////////////////////////


            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
//                    sendBroadcast(new Intent(BackgroundReceiver.UPDATE_BACKGROUND));

                        File file = new File(mPath);
                        URL url = new URL(link);
                        FileOutputStream oFile = new FileOutputStream(file);
                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, oFile);


                        mView.getContext().sendBroadcast(new Intent(BackgroundReceiver.UPDATE_BACKGROUND));
//                        Log.d(TAG,"update background broadcast sent");
//                    File file = File.createTempFile(mPath);
                    }
                    catch (IOException e){
                        return;
                    }
                }
            }).start();



        }
    }

    private String getLink(Context context, SharedPreferences preferences){
        return preferences.getString(context.getString(
                R.string.preference_source_link),
                "https://loremflickr.com/720/1080");
    }
}
