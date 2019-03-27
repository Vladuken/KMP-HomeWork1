package com.vladuken.vladpetrushkevich.activities.main.fragments.desktop;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
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
import com.yandex.metrica.YandexMetrica;

public class DesktopEmptyOnLongClickListener implements View.OnLongClickListener {

    protected final AppDatabase mDatabase;
    protected final DesktopItem mDesktopItem;
    protected final DesktopItemViewHolder mViewHolder;
    protected final DesktopFragment mFragment;

    protected PopupMenu mPopupMenu;

    public DesktopEmptyOnLongClickListener(DesktopItemViewHolder viewHolder,AppDatabase database, DesktopItem desktopItem,DesktopFragment fragment) {
        mDatabase = database;
        mDesktopItem = desktopItem;
        mViewHolder = viewHolder;
        mFragment = fragment;
    }

    @Override
    public boolean onLongClick(View v) {
        mPopupMenu = new PopupMenu(v.getContext(), v);
        MenuInflater inflater = mPopupMenu.getMenuInflater();
        inflater.inflate(R.menu.desktop_empty_item_popup, mPopupMenu.getMenu());

        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.desktop_item_add_contact:
                        if (ActivityCompat.checkSelfPermission(v.getContext(), android.Manifest.permission.READ_CONTACTS)
                                == PackageManager.PERMISSION_GRANTED)
                        {
                            startAddContact(mDesktopItem);
                        } else {
                            startAddContact(mDesktopItem);
                            requestLocationPermission();
                        }

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

            private void requestLocationPermission() {
                if(!ActivityCompat.shouldShowRequestPermissionRationale(
                        mFragment.getActivity(),
                        android.Manifest.permission.READ_CONTACTS)) {
                    ActivityCompat.requestPermissions(
                            mFragment.getActivity(),
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            1);
                }
            }
        });

        mPopupMenu.show();
        return true;
    }

    protected void startAddLinkDialog(DesktopItem desktopItem){
        AlertDialog.Builder builder = new AlertDialog.Builder(mViewHolder.itemView.getContext());
        //TODO
        String title = mViewHolder.mView.getResources().getString(R.string.dialog_link_title);
        builder.setTitle(title);

        // Set up the input
        final EditText input = new EditText(mViewHolder.itemView.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        String addString = mViewHolder.mView.getResources().getString(R.string.dialog_link_add);
        builder.setPositiveButton(addString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                YandexMetrica.reportEvent("Positive button link added dialog pressed");

                String link = input.getText().toString();
                setIconForSite(link,desktopItem);
            }
        });

        String cancelString = mViewHolder.mView.getResources().getString(R.string.dialog_link_cancel);
        builder.setNegativeButton(cancelString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                YandexMetrica.reportEvent("Cancel button link added dialog pressed");
                dialog.cancel();
            }
        });

        builder.show();
    }

    protected void setIconForSite(String link, DesktopItem desktopItem){
        desktopItem.itemType = "link";
        desktopItem.itemData = link;
        mDatabase.desckopAppDao().update(desktopItem);
        mViewHolder.bind(desktopItem);

    }



    protected void startAddContact(DesktopItem desktopItem){
        Intent i=new Intent(Intent.ACTION_PICK);
        i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        mFragment.setLongClickedScreen(desktopItem.screenPosition);
        mFragment.setLongClickedColumn(desktopItem.column);
        mFragment.setLongClickedRow(desktopItem.row);
        mFragment.setLongClickedViewHolder(mViewHolder);
        mFragment.startActivityForResult(i, DesktopFragment.RESULT_CODE_PICK_PHONE);
    }


}


