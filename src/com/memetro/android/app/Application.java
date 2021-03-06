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
