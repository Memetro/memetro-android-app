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

package com.memetro.android.dataManager;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.DatabaseHelper;
import com.activeandroid.query.Delete;
import com.memetro.android.common.AppContext;
import com.memetro.android.oauth.Utils;

public class dataUtils {

    public static void clearData(Context context){
        context.getSharedPreferences(Utils.SHARED_NAME, 0).edit().clear().commit();
        // context.deleteDatabase(AppContext.DB_NAME);
        // ActiveAndroid.clearCache();
    }

}
