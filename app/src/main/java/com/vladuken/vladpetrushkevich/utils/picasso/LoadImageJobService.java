package com.vladuken.vladpetrushkevich.utils.picasso;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.vladuken.vladpetrushkevich.activities.main.BackgroundReceiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class LoadImageJobService extends JobService {

    private static final String TAG = "LoadImageJobService";
    private String mLink;
    private String mPath;

    @Override
    public boolean onStartJob(JobParameters params) {

        mLink = params.getExtras().getString("link");
        mPath = params.getExtras().getString("path");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    sendBroadcast(new Intent(BackgroundReceiver.UPDATE_BACKGROUND));

                    File file = new File(mPath);
                    URL url = new URL(mLink);
                    FileOutputStream oFile = new FileOutputStream(file);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, oFile);


                    sendBroadcast(new Intent(BackgroundReceiver.UPDATE_BACKGROUND));
                    Log.d(TAG,"update background broadcast sent");
//                    File file = File.createTempFile(mPath);
                }
                catch (IOException e){
                    return;
                }
            }
        }).start();


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
