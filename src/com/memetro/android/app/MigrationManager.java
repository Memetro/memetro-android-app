/*
 * Copyright 2013 Nytyr [me at nytyr dot me]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
