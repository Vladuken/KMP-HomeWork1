package com.vladuken.vladpetrushkevich.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.PreferenceManager;

import com.vladuken.vladpetrushkevich.R;

import java.util.Collections;
import java.util.List;

public class AppSorter {

//
//    public static void sortByPreference(List<ResolveInfo> activities,
//                                        SharedPreferences sharedPreferences,
//                                        Context context,
//                                        PackageManager pm){
//
//        int sortMethod = Integer.parseInt(
//                sharedPreferences.getString(
//                        context.getString(R.string.preference_key_sort_method),"0"
//                )
//        );
//
//        switch (sortMethod){
//            case 0:
//                break;
//            case 1:
//                Collections.sort(activities, new ResolveInfo.DisplayNameComparator(pm));
//                break;
//            case 2:
//                Collections.sort(activities, new ResolveInfo.DisplayNameComparator(pm));
//                Collections.reverse(activities);
//                break;
//            case 3:
//                Collections.sort(activities, new InstallDateComparator(pm));
//                break;
//            case 4:
//                Collections.sort(activities, new LaunchCountComparator(mDatabase));
//                break;
//
//            default:
//                break;
//        }
//    }
}
