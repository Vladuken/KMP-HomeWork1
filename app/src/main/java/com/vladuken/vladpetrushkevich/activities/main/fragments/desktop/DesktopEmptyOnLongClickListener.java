package com.vladuken.vladpetrushkevich.activities.main.fragments.desktop;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.entity.DesktopItem;

public class DesktopEmptyOnLongClickListener implements View.OnLongClickListener {

    private AppDatabase mDatabase;
    private DesktopItem mDesktopItem;
    private DesktopItemViewHolder mViewHolder;

    public DesktopEmptyOnLongClickListener(DesktopItemViewHolder viewHolder,AppDatabase database, DesktopItem desktopItem) {
        mDatabase = database;
        mDesktopItem = desktopItem;
        mViewHolder = viewHolder;
    }

    @Override
    public boolean onLongClick(View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.desktop_empty_item_popup, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.desktop_item_add_contact:
                        return true;

                    case R.id.desktop_item_add_link:
                        startAddLinkDialog(mDesktopItem);

                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();
        return true;
    }

    private void startAddLinkDialog(DesktopItem desktopItem){

        AlertDialog.Builder builder = new AlertDialog.Builder(mViewHolder.itemView.getContext());
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(mViewHolder.itemView.getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String link = input.getText().toString();
                setIconForSite(link,desktopItem);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void setIconForSite(String link, DesktopItem desktopItem){
        desktopItem.itemType = "link";
        desktopItem.itemData = link;
        mDatabase.desckopAppDao().update(desktopItem);
        mViewHolder.bind(desktopItem);

    }
}


