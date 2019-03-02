package com.vladuken.vladpetrushkevich.activities.main.fragments.desktop;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.DragEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.vladuken.vladpetrushkevich.R;
import com.vladuken.vladpetrushkevich.db.AppDatabase;
import com.vladuken.vladpetrushkevich.db.entity.DesktopItem;
import com.yandex.metrica.YandexMetrica;

public class DesktopEmptyOnLongClickListener implements View.OnLongClickListener {

    private AppDatabase mDatabase;
    private DesktopItem mDesktopItem;
    private DesktopItemViewHolder mViewHolder;

    private PopupMenu mPopupMenu;


    public DesktopEmptyOnLongClickListener(DesktopItemViewHolder viewHolder,AppDatabase database, DesktopItem desktopItem) {
        mDatabase = database;
        mDesktopItem = desktopItem;
        mViewHolder = viewHolder;
    }

    @Override
    public boolean onLongClick(View v) {
        mPopupMenu = new PopupMenu(v.getContext(), v);
        MenuInflater inflater = mPopupMenu.getMenuInflater();
        inflater.inflate(R.menu.desktop_empty_item_popup, mPopupMenu.getMenu());

        mViewHolder.itemView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if(event.getAction() == DragEvent.ACTION_DRAG_EXITED){
                    mPopupMenu.dismiss();
                }
                return true;
            }
        });


        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.desktop_item_add_contact:
                        startAddContact(mDesktopItem);
                        YandexMetrica.reportEvent("Add contact in desktop pressed");

                        return true;

                    case R.id.desktop_item_add_link:
                        YandexMetrica.reportEvent("Add link in desktop pressed");

                        startAddLinkDialog(mDesktopItem);

                        return true;
                    default:
                        return false;
                }
            }
        });

        mPopupMenu.show();
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
                YandexMetrica.reportEvent("Positive button link added dialog pressed");

                String link = input.getText().toString();
                setIconForSite(link,desktopItem);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                YandexMetrica.reportEvent("Cancel button link added dialog pressed");
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

    private void startAddContact(DesktopItem desktopItem){

        Intent i=new Intent(Intent.ACTION_PICK);
        i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        i.putExtra("HELP",6);
//        Activity h = new Activity();
//        h.startActivityForResult(i, DesktopFragment.RESULT_CODE_PICK_PHONE);
    }
}


