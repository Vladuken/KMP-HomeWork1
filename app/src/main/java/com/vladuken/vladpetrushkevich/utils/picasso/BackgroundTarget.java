package com.vladuken.vladpetrushkevich.utils.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class BackgroundTarget implements Target {

    protected static final String TAG = "BackgroundTarget";
    private View mView;

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        Log.d(TAG,"loaded");
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
        Log.d(TAG,"failed");

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        Log.d(TAG,"prepareload");

    }



//
//    public class BackgroundTargetCallback implements Callback{
//
//    }

}
