package com.vladuken.contentproviderapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class AppCursorAdapter extends CursorAdapter {

    public AppCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public AppCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.layout_app_text, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView name = (TextView) view.findViewById(R.id.textview_package);
        name.setText(cursor.getString(0));

        TextView phone = (TextView) view.findViewById(R.id.textview_launch_count);
        phone.setText(cursor.getString(1));

    }

}
