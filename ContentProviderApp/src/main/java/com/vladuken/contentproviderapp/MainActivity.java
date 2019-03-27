package com.vladuken.contentproviderapp;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.preference.PreferenceCategory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";

    TextView mTextView;

    Button mLastLaunchedButton;
    Button mAppsButton;


    private AppCompatSpinner mSpinner;
    private EditText mEditText;
    private Button mUpdateButton;

    ArrayAdapter<App> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mTextView = findViewById(R.id.textView);
        mAppsButton = findViewById(R.id.all_app_btn);
        mLastLaunchedButton = findViewById(R.id.last_launched_app_btn);
        mSpinner = findViewById(R.id.spinner);
        mUpdateButton = findViewById(R.id.update_button);
        mEditText = findViewById(R.id.launch_count_edittext);

        mAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri yourURI = buildUri(AppInfoProviderContract.PATH_ALL_APPS);
                mTextView.setText(getResString(yourURI));
            }
        });
        mLastLaunchedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri yourURI = buildUri(AppInfoProviderContract.PATH_LAST_LAUNCHED_APP);

                mTextView.setText(getResString(yourURI));
            }
        });



        Uri yourURI = buildUri(AppInfoProviderContract.PATH_ALL_APPS);
        List<App> list = getAppList(yourURI);



        if(!list.isEmpty()){
            mEditText.setText("0");
        }

        //TODO add
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mSpinner.getSelectedItemPosition();
                if(position == -1){
                    return;
                }
                App app = mAdapter.getItem(position);
                int launchCount = Integer.decode(mEditText.getText().toString());
                app.launches_count = launchCount;
                ContentValues values = new ContentValues();
                values.put(App.PACKAGE_NAME,app.package_name);
                values.put(App.LAUNCH_COUNT,launchCount);
                values.put(App.LAST_TIME_LAUNCHED,app.last_time_launched);

                ContentResolver resolver = getContentResolver();
                try{
                    resolver.update(buildUri(AppInfoProviderContract.PATH_UPDATE_APP),values,null,null);
                    Uri yourURI = buildUri(AppInfoProviderContract.PATH_ALL_APPS);
                    List<App> list = getAppList(yourURI);

                    mAdapter = new ArrayAdapter<>(v.getContext(),android.R.layout.simple_spinner_dropdown_item,list);
                    mSpinner.setAdapter(mAdapter);
                }catch (SecurityException e){
                    mTextView.setText("No write permission");
                }

            }
        });
        mAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,list);
        mSpinner.setAdapter(mAdapter);
    }

    private Uri buildUri(String path){
        Uri.Builder builder = new Uri.Builder();

        return builder.scheme("content")
                .authority(AppInfoProviderContract.AUTHORITY)
                .appendPath(path)
                .build();
    }

    private String getResString(Uri yourURI){

        List<App> list = getAppList(yourURI);
        StringBuilder resString = new StringBuilder();

        final PackageManager pm = getApplicationContext().getPackageManager();

        for (App app:list){
            resString.append(getAppLabel(app.package_name, pm)).append(" ").append(app.package_name).append(" ").append(app.launches_count).append("\n");
        }


        return resString.toString();
    }

    private String getAppLabel(String packageName, PackageManager pm){

        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo( packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return  (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
    }

    private List<App> getAppList(Uri yourURI){
        ArrayList<App> list = new ArrayList<>();

        ContentProviderClient yourCR = getContentResolver().acquireContentProviderClient(yourURI);
        if(yourCR == null){
            mTextView.setText("No such ContentProvider");
        }else {
            try{
                Cursor yourCursor = yourCR.query(
                        yourURI,
                        null,
                        null,
                        null,
                        null);

                if(yourCursor != null){
                    while (yourCursor.moveToNext()){
                        list.add(
                                new App(yourCursor.getString(0),yourCursor.getInt(1),yourCursor.getLong(2))
                        );
                    }
                }
            }catch (RemoteException e){
                Log.d(TAG,"remote exeption");
            }catch (NullPointerException e) {
                Log.d(TAG,"null pointer exeption");
            }catch (SecurityException e){
                Log.d(TAG,"security exeption");
                mUpdateButton.setEnabled(false);
                mAppsButton.setEnabled(false);
                mLastLaunchedButton.setEnabled(false);
                mTextView.setText("No permissions");
            }finally {
                Log.d(TAG,"Close cursor");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    yourCR.close();
                }else{
                    yourCR.release();
                }
            }
        }

        return list;
    }

}
