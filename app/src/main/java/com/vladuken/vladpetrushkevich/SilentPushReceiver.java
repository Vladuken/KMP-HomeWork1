package com.vladuken.vladpetrushkevich;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.vladuken.vladpetrushkevich.activities.profile.ProfilePageActivity;
import com.yandex.metrica.push.YandexMetricaPush;
public class SilentPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Извлечение данных из вашего push-уведомления.
        String payload = intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD);
        Intent  i= new Intent(context,ProfilePageActivity.class);
        context.startActivity(i);
        Toast.makeText(context,payload,Toast.LENGTH_LONG).show();
    }
}