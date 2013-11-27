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
import android.os.AsyncTask;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.memetro.android.common.AppContext;
import com.memetro.android.models.City;
import com.memetro.android.models.Country;
import com.memetro.android.models.Line;
import com.memetro.android.models.Station;
import com.memetro.android.models.Transport;
import com.memetro.android.models.User;
import com.memetro.android.oauth.OAuth;
import com.memetro.android.oauth.Utils;
import com.memetro.android.oauth.oauthHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class dataUtils {

    public static void clearData(Context context){
        context.getSharedPreferences(Utils.SHARED_NAME, 0).edit().clear().commit();
        // context.deleteDatabase(AppContext.DB_NAME);
        // ActiveAndroid.clearCache();
    }

    public void syncWSData(Context context, oauthHandler handler) {
        new AsyncSync(context, handler).execute();
    }

    public void login(Context context, String username, String password, oauthHandler handler) {
        new AsyncLogin(context, username, password, handler).execute();
    }

    private class AsyncLogin extends AsyncTask<String, Integer, JSONObject>{

        private Context context;
        private oauthHandler handler;
        private String username, password;
        private OAuth OAuth = new OAuth();
        private Utils Utils = new Utils();

        public AsyncLogin(Context context, String username, String password, oauthHandler handler) {
            this.context = context;
            this.handler = handler;
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            handler.onStart();
        }

        protected JSONObject doInBackground(String... params){
            return OAuth.login(username, password);
        }

        protected void onPostExecute(JSONObject result) {
            if (AppContext.DEBUG) Log.d("Login", result.toString());

            // Trying to get the token...
            try{
                String token = result.getString("access_token");
                String refresh_token = result.getString("refresh_token");
                Utils.setToken(context, token, refresh_token);
                handler.onSuccess();
            }catch(Exception e){
                // Token failed
                if (AppContext.DEBUG) Log.d("Login", "Login failed. Cause: "+ e.toString());
                handler.onFailure();
            }
            handler.onFinish();
        }
    }

    private class AsyncSync extends AsyncTask<String, Integer, Boolean> {

        private Context context;
        private oauthHandler handler;

        public AsyncSync(Context context, oauthHandler handler) {
            this.context = context;
            this.handler = handler;
        }

        @Override
        protected void onPreExecute() {
            handler.onStart();
        }

        @Override
        protected Boolean doInBackground(String... params){
            OAuth OAuth = new OAuth();
            Utils Utils = new Utils();

            List<NameValuePair> postParams = new ArrayList<NameValuePair>(1);
            postParams.add(new BasicNameValuePair("access_token", Utils.getToken(context)));
            JSONObject result = OAuth.call("synchronize", "", postParams);

            if (AppContext.DEBUG) Log.d("Sync", result.toString());

            try{
                //Save sync data
                JSONObject data = result.getJSONObject("data");

                JSONObject currentData;
                ActiveAndroid.beginTransaction();
                try {

                    //Save user data
                    new Delete().from(User.class).execute();
                    JSONObject userData = data.getJSONObject("user");
                    User user = new User();
                    user.username = userData.getString("username");
                    user.name = userData.getString("name");
                    user.email = userData.getString("email");
                    user.twittername = userData.getString("twittername");
                    user.avatar = userData.getString("avatar");
                    user.aboutme = userData.getString("aboutme");
                    user.save();

                    //Save countries
                    new Delete().from(Country.class).execute();
                    JSONArray countries = data.getJSONObject("country").getJSONArray("data");

                    for (int i = 0; i < countries.length(); i++) {
                        currentData = countries.getJSONObject(i);
                        Country country = new Country();
                        country.name =  currentData.getString("name");
                        country.countryId = currentData.getString("id");
                        country.save();
                    }

                    //Save cities
                    new Delete().from(City.class).execute();
                    JSONArray cities = data.getJSONObject("city").getJSONArray("data");

                    for (int i = 0; i < cities.length(); i++) {
                        currentData = cities.getJSONObject(i);
                        City city = new City();
                        city.name =  currentData.getString("name");
                        city.cityId = currentData.getString("id");
                        city.country_id = currentData.getInt("country_id");
                        city.save();
                    }

                    //Save Lines
                    new Delete().from(Line.class).execute();
                    JSONArray lines = data.getJSONObject("line").getJSONArray("data");
                    for (int i = 0; i < lines.length(); i++) {
                        currentData = lines.getJSONObject(i);
                        Line line = new Line();
                        line.name =  currentData.getString("name");
                        line.number = currentData.getInt("number");
                        line.transport_id = currentData.getInt("transport_id");
                        line.save();
                    }

                    //Save Transports
                    new Delete().from(Transport.class).execute();
                    JSONArray transports = data.getJSONObject("transport").getJSONArray("data");
                    for (int i = 0; i < transports.length(); i++) {
                        currentData = transports.getJSONObject(i);
                        Transport transport = new Transport();
                        transport.name =  currentData.getString("name");
                        transport.icon = currentData.getInt("icon");
                        transport.save();
                    }

                    //Save station
                    new Delete().from(Station.class).execute();
                    JSONArray stations = data.getJSONObject("station").getJSONArray("data");
                    for (int i = 0; i < stations.length(); i++) {
                        currentData = stations.getJSONObject(i);
                        Station station = new Station();
                        station.name =  currentData.getString("name");
                        station.longitude = currentData.getLong("longitude");
                        station.latitude = currentData.getInt("latitude");
                        station.save();
                    }

                    ActiveAndroid.setTransactionSuccessful();
                }
                finally {
                    ActiveAndroid.endTransaction();
                }
                return true;
            }catch(Exception e){
                if (AppContext.DEBUG) Log.d("Sync", "Sync failed. Cause: "+ e.toString());
                e.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result) {
                handler.onSuccess();
            }else {
                handler.onFailure();
            }

            handler.onFinish();

        }
    }
}
