package com.vladuken.vladpetrushkevich.activities.main;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.view.View;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.utils.picasso.LoadImageJobService;
import com.vladuken.vladpetrushkevich.utils.picasso.SetBackgroundAsynkTask;

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


            //TODO SAME CODE 1
            SharedPreferences preferences = mView.getContext().getSharedPreferences(
                    mView.getContext().getString(R.string.preference_file),0
            );
            ComponentName componentName = new ComponentName(mView.getContext(), LoadImageJobService.class);

            PersistableBundle bundle = new PersistableBundle();

            String link = getLink(mView.getContext(),preferences);
            bundle.putString("link", link);
            bundle.putString("path", mPath);

            JobInfo.Builder jobInfoBuilder = new JobInfo.Builder(1,componentName)
                    .setOverrideDeadline(0)
                    .setExtras(bundle);
            JobInfo jobInfo = jobInfoBuilder.build();


            JobScheduler jobScheduler = (JobScheduler) mView.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(jobInfo);
        }
    }

    private String getLink(Context context, SharedPreferences preferences){
        return preferences.getString(context.getString(
                R.string.preference_source_link),
                "https://loremflickr.com/720/1080");
    }
}
