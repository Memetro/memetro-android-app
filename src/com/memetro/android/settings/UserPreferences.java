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

    public static final String DEFAULT_CITY = "default_city";
    public static final long DEFAULT_CITY_ID = 1; // BCN

    public static Long getUserCity(Context context) {
        long userCity = getUserPreferences(context).getLong(DEFAULT_CITY, -1);
        if (userCity == -1) {
            setUserCity(context, DEFAULT_CITY_ID);
            userCity = DEFAULT_CITY_ID;
        }
        return userCity;
    }

    public static void setUserCity(Context context, Long cityId){
        final SharedPreferences prefs = getUserPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(DEFAULT_CITY, cityId);
        editor.commit();
    }

    private static SharedPreferences getUserPreferences(Context context) {
        return context.getSharedPreferences("USER_PREFERENCES",
                Context.MODE_PRIVATE);
    }
}
