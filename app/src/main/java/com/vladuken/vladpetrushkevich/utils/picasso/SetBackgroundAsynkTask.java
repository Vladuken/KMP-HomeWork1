package com.vladuken.vladpetrushkevich.utils.picasso;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vladuken.vladpetrushkevich.activities.main.fragments.LauncherViewHolder;
import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.LauncherAdapter;
import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.listeners.AppLongClickListener;
import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.listeners.IconOnClickListener;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

public class SetBackgroundAsynkTask extends AsyncTask<Void,Void, Bitmap> {

    private static final String TAG = "SetBackgroundAsynkTask";

    private View mView;
    private String mPath;
    private Resources mResources;

    public SetBackgroundAsynkTask(View view, String path) {
        mView = view;
        mPath = path;

        mResources = view.getResources();
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(mPath, options);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {


        Bitmap b = ThumbnailUtils.extractThumbnail(bitmap,mView.getWidth(),mView.getHeight());
//        Drawable drawable = new BitmapDrawable(
//                mView.getResources(),
//                Bitmap.createScaledBitmap(
//                        bitmap,
//                        mView.getWidth(),
//                        mView.getHeight(),
//                        true));

        Drawable drawable = new BitmapDrawable(mView.getResources(),b);

        mView.setBackground(drawable);
    }
}
