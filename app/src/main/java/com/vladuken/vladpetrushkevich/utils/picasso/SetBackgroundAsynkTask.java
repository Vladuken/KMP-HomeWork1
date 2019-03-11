package com.vladuken.vladpetrushkevich.utils.picasso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.view.View;

public class SetBackgroundAsynkTask extends AsyncTask<Void,Void, Bitmap> {

//    private static final String TAG = "SetBackgroundAsynkTask";

    private final View mView;
    private final String mPath;

    public SetBackgroundAsynkTask(View view, String path) {
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
