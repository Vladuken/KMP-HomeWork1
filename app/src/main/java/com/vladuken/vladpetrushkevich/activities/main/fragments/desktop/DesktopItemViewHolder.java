package com.vladuken.vladpetrushkevich.activities.main.fragments.desktop;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.activities.main.fragments.gridlauncher.listeners.AppLongClickListener;
import com.vladuken.vladpetrushkevich.activities.main.gestureDetectors.AppGestureDetectorListener;
import com.vladuken.vladpetrushkevich.activities.main.gestureDetectors.DesktopItemGestureDetectorListener;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.entity.App;
import com.vladuken.vladpetrushkevich.db.entity.DesktopItem;

import java.io.IOException;
import java.io.InputStream;

import static com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread;

public class DesktopItemViewHolder extends RecyclerView.ViewHolder {

    protected static final String TAG = "DesktopItemViewHolder";
    protected final ImageView mAppIcon;
    protected final TextView mAppTitle;

    protected final DesktopFragment mDesktopFragment;
    protected final View mView;

    protected final DesktopItem mDesktopItem;
    private final AppDatabase mDatabase;

    public DesktopItemViewHolder(@NonNull View itemView,
                                 DesktopFragment fragment,
                                 DesktopItem desktopItem,
                                 AppDatabase database,
                                 int iconLayoutId,
                                 int titleLayoutId){
        super(itemView);
        mView = itemView;
        mDesktopFragment = fragment;
        mDesktopItem = desktopItem;
        mAppIcon = itemView.findViewById(iconLayoutId);
        mAppTitle = itemView.findViewById(titleLayoutId);
        mDatabase = database;
    }

    public DesktopFragment getDesktopFragment() {
        return mDesktopFragment;
    }

    public void bind(DesktopItem item){

        mView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                switch (event.getAction()){
                    case DragEvent.ACTION_DRAG_STARTED:
                        Log.d(TAG,"Drag Started" + getPositionString(item) );
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        itemView.setBackgroundColor(Color.argb(160,25,25,25));
                        Log.d(TAG,"Drag Entered" + getPositionString(item));
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        itemView.setBackgroundColor(Color.TRANSPARENT);
                        Log.d(TAG,"Drag Exited"+ getPositionString(item));
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
//                        bind(mDesktopItem);

                        Log.d(TAG,"Drag Ended"+ getPositionString(item));
                        break;
                    case DragEvent.ACTION_DROP:
                        itemView.setBackgroundColor(Color.TRANSPARENT);
                        ClipData.Item clipType = event.getClipData().getItemAt(0);
                        ClipData.Item clipData = event.getClipData().getItemAt(1);

                        mDesktopItem.itemType = clipType.getText().toString();
                        mDesktopItem.itemData = clipData.getText().toString();
                        bind(mDesktopItem);

                        Log.d(TAG,"Drag Dropped"+ getPositionString(item));
                        break;
                    default:
                        break;
                }

                return true;
            }

            private String getPositionString(DesktopItem i){
                return " r:" +item.row + " c:" + item.column;
            }
        });

        switch (item.itemType){
            case "empty":
                bind();
                break;
            case "app":
                App app = mDatabase.appDao().getById(item.itemData);
                if(app == null){
                    app = new App(item.itemType,0,System.currentTimeMillis());
                }
                bindApp(app);
                break;
            case "link":
                bindItemView(item);
                break;
            case "contact":
                bindItemView(item);
                break;
            default:
                break;
        }
    }

    private void bindItemView(DesktopItem item){
        if("link".equals(item.itemType)){
            String linkTitle = item.itemData;

            String linkPhoto = "https://favicon.yandex.net/favicon/" + item.itemData + "?size=120";


            Picasso.get()
                    .load(linkPhoto)
                    .placeholder(R.drawable.ic_web)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            if(bitmap.getWidth() < 3 || bitmap.getHeight() < 3){


                                mAppIcon.setImageResource(R.drawable.ic_web);
                            }else {
                                mAppIcon.setImageDrawable(new BitmapDrawable(mAppIcon.getResources(),bitmap));
                            }
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            mAppIcon.setImageResource(R.drawable.ic_web);

                        }
                    });
            mAppTitle.setText(linkTitle);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String url = linkTitle;
                    if (!url.startsWith("http://") && !url.startsWith("https://")){
                        url = "http://" + url;
                    }
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mDesktopFragment.startActivity(i);
                }
            });

//            mView.setOnLongClickListener(new DesktopItemOnLongClickListener());
            mDatabase.desckopAppDao().update(mDesktopItem);

            GestureDetector detector = new GestureDetector(
                    mView.getContext(),
                    new DesktopItemGestureDetectorListener(item,mView,this)
            );

            mView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return detector.onTouchEvent(event);
                }
            });
        }else if("contact".equals(item.itemType)){


            mDatabase.desckopAppDao().update(item);
            String contactPhone = item.itemData;

            if (ActivityCompat.checkSelfPermission(mView.getContext(), android.Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED) {
                getContactName(mView.getContext(),contactPhone);
            } else {
                Picasso.get().load(android.R.drawable.ic_menu_call).into(mAppIcon);
                mAppTitle.setText(item.itemData);
            }

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        String url = contactPhone;
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+url));

                        mDesktopFragment.startActivity(intent);
                }
            });

            GestureDetector detector = new GestureDetector(
                    mView.getContext(),
                    new DesktopItemGestureDetectorListener(item,mView,this)
            );

            mView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return detector.onTouchEvent(event);
                }
            });

        }
    }



    private void getContactName(Context context,final String phoneNumber) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver cr = mView.getContext().getContentResolver();
                Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
                Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME,
                        ContactsContract.PhoneLookup._ID}, null, null, null);
                if (cursor == null) {
                    return;
                }
//                String name = null;
                String contactId = null;
                InputStream input = null;
                String contactName = null;
                if(cursor.moveToFirst()) {
                    contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
                    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));

                    // Get photo of contactId as input stream:
                    Uri uri1 = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
                    input = ContactsContract.Contacts.openContactPhotoInputStream(mView.getContext().getContentResolver(), uri1);

                }

                if(input != null){
                    //TODO put into separate class to create links icons
                    final String contact = contactName;
                    final Bitmap bitmap = BitmapFactory.decodeStream(input);
                    Bitmap dst = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getWidth());
                    final RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(context.getResources(), dst);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            roundedBitmapDrawable.setCornerRadius(50.0f);
                            roundedBitmapDrawable.setAntiAlias(true);
                            mAppIcon.setImageDrawable(roundedBitmapDrawable);
//                            mAppIcon.setImageBitmap(bitmap);
                            mAppTitle.setText(contact);
                        }
                    });
//                    mAppIcon.setImageDrawable(new BitmapDrawable(mView.getResources(),BitmapFactory.decodeStream(input)));
                }else {
                    final String contact = contactName;
//                    final Bitmap bitmap = BitmapFactory.decodeStream(input);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mAppIcon.setImageResource(android.R.drawable.ic_menu_call);
                            mAppTitle.setText(contact);
                        }
                    });
                }
                if(cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }

//                listener.contactName(contactName);
            }
        }).start();


    }

    private void bindApp(App app){
        ResolveInfo resolveInfo = null;
        PackageManager pm = mView.getContext().getPackageManager();

        Intent i = pm.getLaunchIntentForPackage(app.package_name);
        if(i != null){
            resolveInfo = pm.resolveActivity(i,0);

            Drawable icon = resolveInfo.loadIcon(pm);
            String appName = resolveInfo.loadLabel(pm).toString();

            mAppIcon.setImageDrawable(icon);
            mAppTitle.setText(appName);

            mView.setOnClickListener(new DesktopAppOnClickListener(mDatabase,app));
            mView.setOnLongClickListener(null);

            GestureDetector detector = new GestureDetector(
                    mView.getContext(),
                    new AppGestureDetectorListener(
                            app,
                            mView,
                            this)
            );

            mView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return detector.onTouchEvent(event);
                }
            });

            mDesktopItem.itemType = "app";
            mDesktopItem.itemData = app.package_name;
            mDatabase.desckopAppDao().update(mDesktopItem);
        }else{
            //TODO bind empty item bind();
            bind();
        }
    }

    private void bind(){
        mView.setOnClickListener(null);
//        mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Snackbar.make(v,"Empty " + mDesktopItem.itemType + " "+ mDesktopItem.itemData,Snackbar.LENGTH_SHORT)
//                        .show();
//            }
//        });
        mView.setOnTouchListener(null);
        mView.setOnLongClickListener(new DesktopEmptyOnLongClickListener(this,mDatabase,mDesktopItem,mDesktopFragment));
//        mDatabase.desckopAppDao().update(mDesktopItem);
        mAppIcon.setImageDrawable(null);
        mAppTitle.setText(null);
    }

    public DesktopItem getDesktopItem() {
        return mDesktopItem;
    }


}
