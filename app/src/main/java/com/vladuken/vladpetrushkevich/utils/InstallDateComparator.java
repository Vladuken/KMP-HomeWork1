package com.vladuken.vladpetrushkevich.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.io.File;
import java.util.Comparator;
import java.util.Date;

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
            installedA = mPM.getPackageInfo(a.activityInfo.packageName,0).firstInstallTime;
        }catch (PackageManager.NameNotFoundException e){
            installedA = new Date().getTime();
        }
        try {
            installedB = mPM.getPackageInfo(b.activityInfo.packageName,0).firstInstallTime;
        }catch (PackageManager.NameNotFoundException e){
            installedB = new Date().getTime();
        }

        int res = 0;
        if(installedB - installedA > 0)
            res = 1;
        if(installedB - installedA < 0)
            res = -1;

        return res;
    }


}
