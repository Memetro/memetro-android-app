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
package com.memetro.android.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    public static final String NOTIFICATIONS_KEY = "default_city";
    public static final boolean DEFAULT_NOTIFICATION = true;

    public static boolean areNotificationsEnabled(Context context) {
        return getUserPreferences(context).getBoolean(NOTIFICATIONS_KEY, DEFAULT_NOTIFICATION);
    }

    public static void toggleNotifications(Context context, boolean enabled){
        final SharedPreferences prefs = getUserPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(NOTIFICATIONS_KEY, enabled);
        editor.commit();
    }

    private static SharedPreferences getUserPreferences(Context context) {
        return context.getSharedPreferences("USER_PREFERENCES",
                Context.MODE_PRIVATE);
    }
}
