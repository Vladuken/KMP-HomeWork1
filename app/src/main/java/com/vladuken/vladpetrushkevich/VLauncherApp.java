package com.vladuken.vladpetrushkevich;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class VLauncherApp extends Application {

    private static final String APPMETRIKA_API_KEY = "aece5e3d-94ee-4e76-b66e-34adf2576fc2";

    @Override
    public void onCreate() {
        super.onCreate();

        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder(APPMETRIKA_API_KEY).build();
        YandexMetrica.activate(getApplicationContext(),config);
        YandexMetrica.enableActivityAutoTracking(this);
    }
}
