package com.vladuken.vladpetrushkevich.utils.picasso;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.view.View;

public class SetBackgroundAsyncTask extends AsyncTask<Void,Void, Bitmap> {

    @SuppressLint("StaticFieldLeak")
    private View mView;
    private final String mPath;

    public SetBackgroundAsyncTask(View view, String path) {
        mView = view;
        mPath = path;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(mPath, options);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Bitmap b = ThumbnailUtils.extractThumbnail(bitmap,mView.getWidth(),mView.getHeight());
        Drawable drawable = new BitmapDrawable(mView.getResources(),b);

        try{
            mView.setBackground(drawable);
        }finally {
            mView = null;
        }
    }
}
