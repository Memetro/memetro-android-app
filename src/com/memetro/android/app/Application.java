package com.memetro.android.app;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

public class Application extends android.app.Application {

    private final int schemaVersion = 3;

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        new MigrationManager(context).initialize(schemaVersion);
        ActiveAndroid.initialize(
                new Configuration.Builder(context).setDatabaseName(MigrationManager.DB_NAME).create()
        );
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

}
