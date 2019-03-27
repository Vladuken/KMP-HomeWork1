package com.vladuken.vladpetrushkevich.activities.main.fragments.desktop;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vladuken.vladpetrushkevich.R;

public class IconImageViewTarger implements Target {

    private final ImageView mImageView;

    public IconImageViewTarger(ImageView imageView) {
        mImageView = imageView;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        if(bitmap.getWidth() < 3 || bitmap.getHeight() < 3){
            mImageView.setImageResource(R.drawable.ic_web);
        }else {
            mImageView.setImageDrawable(new BitmapDrawable(mImageView.getResources(),bitmap));
        }
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
        mImageView.setImageDrawable(errorDrawable);
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        mImageView.setImageDrawable(placeHolderDrawable);
    }
}
