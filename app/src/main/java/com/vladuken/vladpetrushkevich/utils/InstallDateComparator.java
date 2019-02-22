package com.vladuken.vladpetrushkevich.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.io.File;
import java.util.Comparator;

public class InstallDateComparator implements Comparator<ResolveInfo> {

    PackageManager mPM;

    public InstallDateComparator(PackageManager pm) {
        mPM = pm;
    }

    @Override
    public int compare(ResolveInfo a, ResolveInfo b) {


        long installedA;
        long installedB;

        try{

            ApplicationInfo applicationInfo = mPM.getApplicationInfo(a.activityInfo.packageName,0);
            String appFile = applicationInfo.sourceDir;
            installedA = new File(appFile).lastModified(); //Epoch Time

        }catch (PackageManager.NameNotFoundException e){
            return  -1;
        }

        try{

            ApplicationInfo applicationInfo = mPM.getApplicationInfo(b.activityInfo.packageName,0);
            String appFile = applicationInfo.sourceDir;
            installedB = new File(appFile).lastModified(); //Epoch Time
        }catch (PackageManager.NameNotFoundException e){
            return  -1;
        }


        return (int) (installedA - installedB);
    }


}
