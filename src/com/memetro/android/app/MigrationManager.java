package com.memetro.android.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.memetro.android.oauth.Utils;

public class MigrationManager {

    private Context context;
    public static final String DB_NAME = "Memetro";
    private static final String SHARED_NAME = "cache_shared_prefs";
    private static final String SCHEMA_VERSION = "cache_shared_prefs_schema";

    public MigrationManager(Context context) {
        this.context = context;
    }

    public void initialize(int version) {
        int oldVersion = getSchemaVersion();
        if (oldVersion == 0) {
            setSchemaVersion(version);
            return;
        }

        if (oldVersion<version) {
            deleteSQLite();
            setSchemaVersion(version);
        }

    }

    public int getSchemaVersion(){
        final SharedPreferences prefs = getCachePreferences();
        return prefs.getInt(SCHEMA_VERSION, 0);
    }

    public void setSchemaVersion(int verion){
        final SharedPreferences prefs = getCachePreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SCHEMA_VERSION, verion);
        editor.commit();
    }

    private SharedPreferences getCachePreferences() {
        return context.getSharedPreferences(SHARED_NAME,
                Context.MODE_PRIVATE);
    }

    private void deleteSQLite() {
        context.deleteDatabase(DB_NAME);
        Utils utils = new Utils();
        utils.setToken(context, "", "");
    }

}
