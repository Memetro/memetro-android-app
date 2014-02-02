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
import com.activeandroid.query.Select;
import com.memetro.android.R;
import com.memetro.android.common.Config;
import com.memetro.android.common.MemetroDialog;
import com.memetro.android.models.Alert;
import com.memetro.android.models.CitiesTransport;
import com.memetro.android.models.City;
import com.memetro.android.models.Country;
import com.memetro.android.models.Line;
import com.memetro.android.models.LinesStation;
import com.memetro.android.models.Station;
import com.memetro.android.models.Transport;
import com.memetro.android.models.Tweet;
import com.memetro.android.models.User;
import com.memetro.android.oauth.OAuth;
import com.memetro.android.oauth.Utils;
import com.memetro.android.oauth.oauthHandler;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtils {

    public static void clearData(Context context){
        context.getSharedPreferences(Utils.SHARED_NAME, 0).edit().clear().commit();
        // context.deleteDatabase(Config.DB_NAME);
        // ActiveAndroid.clearCache();
    }

    public static User getUserData() {
        return new Select().from(User.class).executeSingle();
    }

    public static List<Transport> getTransport() {
        return new Select().from(Transport.class).execute();
    }

    public static List<Transport> getTransport(Long cityId) {
        List<CitiesTransport> citiesTransports = new Select().
                from(CitiesTransport.class).
                where("CityId = ?", cityId).
                execute();
        if (citiesTransports.size() == 0) return new ArrayList<Transport>();
        if (citiesTransports.size() == 1) {
            return new Select().from(Transport.class).where("TransportId = ?", citiesTransports.get(0).transportId).execute();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < citiesTransports.size() - 1; i++) {
            sb.append(citiesTransports.get(i).transportId);
            sb.append(",");
        }
        sb.append(citiesTransports.get(citiesTransports.size() - 1).transportId);

        return new Select().from(Transport.class).where("TransportId IN ("+sb.toString()+")").execute();
    }

    public static List<Station> getStations(Long lineId) {
        List<LinesStation> linesStations = new Select().
                from(LinesStation.class).
                where("LineId = ?", lineId).
                execute();
        if (linesStations.size() == 0) return new ArrayList<Station>();
        // DEBUG
        for(LinesStation linesStation : linesStations) {
            Log.d("Test", "stationId > "+linesStation.stationId);
        }
        // DEBUG
        if (linesStations.size() == 1) {
            return new Select().from(Station.class).where("stationId = ?", linesStations.get(0).stationId).execute();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < linesStations.size() - 1; i++) {
            sb.append(linesStations.get(i).stationId);
            sb.append(",");
        }
        sb.append(linesStations.get(linesStations.size() - 1).stationId);

        return new Select().from(Station.class).where("stationId IN ("+sb.toString()+")").execute();
    }

    public static List<Line> getLines(Long cityId, Long transportId) {
        List<CitiesTransport> citiesTransports = new Select().
                from(CitiesTransport.class).
                where("CityId = ?", cityId).
                where("TransportId = ?", transportId).
                execute();
        if (citiesTransports.size() == 0) return new ArrayList<Line>();
        if (citiesTransports.size() == 1) {
            return new Select().from(Line.class).where("TransportId = ?", citiesTransports.get(0).transportId).execute();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < citiesTransports.size() - 1; i++) {
            sb.append(citiesTransports.get(i).transportId);
            sb.append(",");
        }
        sb.append(citiesTransports.get(citiesTransports.size() - 1).transportId);

        return new Select().from(Line.class).where("TransportId IN ("+sb.toString()+")").execute();
    }

    public static List<City> getCities(Long countryId) {
        return new Select().from(City.class).where("CountryId = ?", countryId).execute();
    }

    public static List<City> getCities() {
        return new Select().from(City.class).execute();
    }

    public static List<Country> getCountries() {
        return new Select().from(Country.class).execute();
    }

    public static List<Tweet> getTweets() {
        return new Select().from(Tweet.class).execute();
    }

    public static void saveTweets(JSONArray alerts) throws JSONException {
        ActiveAndroid.beginTransaction();

        try {
            new Delete().from(Tweet.class).execute();

            JSONObject currentAlert;
            for (int i = 0; i < alerts.length(); i++) {
                currentAlert = alerts.getJSONObject(i);

                Tweet tweet = new Tweet();
                tweet.text = currentAlert.getString("text");
                tweet.user = currentAlert.getString("user");
                tweet.rtUser = currentAlert.getString("rt_user");
                tweet.date = currentAlert.getString("date");
                tweet.save();
            }

            ActiveAndroid.setTransactionSuccessful();

        }finally {

            ActiveAndroid.endTransaction();

        }

    }

    public static List<Alert> getAlerts() {
        return new Select().from(Alert.class).execute();
    }

    public static void saveAlerts(JSONArray alerts) throws JSONException {
        ActiveAndroid.beginTransaction();

        try {
            new Delete().from(Alert.class).execute();

            JSONObject currentAlert;
            for (int i = 0; i < alerts.length(); i++) {
                currentAlert = alerts.getJSONObject(i);

                Alert alert = new Alert();
                alert.alertId = currentAlert.getLong("id");
                alert.description = currentAlert.getString("alert");
                alert.date = currentAlert.getString("date");
                alert.username = currentAlert.getString("username");
                alert.latitude = currentAlert.getDouble("latitude");
                alert.longitude = currentAlert.getDouble("longitude");
                alert.line = currentAlert.getString("line");
                alert.city = currentAlert.getString("city");
                alert.station = currentAlert.getString("station");
                alert.transport = currentAlert.getString("transport");
                alert.icon = currentAlert.getString("transport_icon");
                alert.save();
            }

            ActiveAndroid.setTransactionSuccessful();

        }finally {

            ActiveAndroid.endTransaction();

        }

    }

    public void syncWSData(Context context, oauthHandler handler) {
        new AsyncSync(context, handler).execute();
    }

    public void syncStaticWSData(Context context, oauthHandler handler) {
        new AsyncStaticSync(context, handler).execute();
    }

    public void login(Context context, String username, String password, oauthHandler handler) {
        new AsyncLogin(context, username, password, handler).execute();
    }

    public void createAlert(Context context, Long stationId, Long lineId, Long cityId, oauthHandler handler) {
        new AsyncCreateAlert(context, handler, stationId, lineId, cityId).execute();
    }

    private class AsyncLogin extends AsyncTask<String, Integer, JSONObject>{

        private Context context;
        private oauthHandler handler;
        private String username, password;
        private OAuth OAuth;
        private Utils Utils = new Utils();

        public AsyncLogin(Context context, String username, String password, oauthHandler handler) {
            this.context = context;
            this.handler = handler;
            this.username = username;
            this.password = password;
            this.OAuth = new OAuth(context);
        }

        @Override
        protected void onPreExecute() {
            handler.onStart();
        }

        protected JSONObject doInBackground(String... params){
            return OAuth.login(username, password);
        }

        protected void onPostExecute(JSONObject result) {
            if (Config.DEBUG) Log.d("Login", result.toString());

            // Trying to get the token...
            try{
                String token = result.getString("access_token");
                String refresh_token = result.getString("refresh_token");
                Utils.setToken(context, token, refresh_token);
                handler.onSuccess();
            }catch(Exception e){
                // Token failed
                if (Config.DEBUG) Log.d("Login", "Login failed. Cause: "+ e.toString());
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
            OAuth OAuth = new OAuth(context);
            Utils Utils = new Utils();

            Map<String, String> postParams = new HashMap<String, String>(1);
            postParams.put("access_token", Utils.getToken(context));
            JSONObject result = OAuth.call("synchronize", "", postParams);

            if (Config.DEBUG) Log.d("Sync", result.toString());

            try{
                //Save sync data
                JSONObject data = result.getJSONObject("data");

                JSONObject currentData;
                ActiveAndroid.beginTransaction();
                try {

                    //Save user data
                    new Delete().from(User.class).execute();
                    JSONObject userData = data.getJSONObject("user");
                    Log.d("User", "User data > "+userData );
                    User user = new User();
                    user.username = userData.getString("username");
                    user.name = userData.getString("name");
                    String email = "";
                    if (!userData.isNull("email")) {
                        email = userData.getString("email");
                    }
                    user.email = email;

                    String twitter = "";
                    if (!userData.isNull("twittername")) {
                        twitter = userData.getString("twittername");
                    }
                    user.twittername = twitter;

                    String aboutme = "";
                    if (!userData.isNull("aboutme")) {
                        aboutme = userData.getString("aboutme");
                    }
                    user.aboutme = aboutme;
                    user.cityId = userData.getLong("city_id");
                    user.save();

                    //Save countries
                    new Delete().from(Country.class).execute();
                    JSONArray countries = data.getJSONObject("country").getJSONArray("data");

                    for (int i = 0; i < countries.length(); i++) {
                        currentData = countries.getJSONObject(i);
                        Country country = new Country();
                        country.name =  currentData.getString("name");
                        country.countryId = currentData.getLong("id");
                        country.save();
                    }

                    //Save cities
                    new Delete().from(City.class).execute();
                    JSONArray cities = data.getJSONObject("city").getJSONArray("data");

                    for (int i = 0; i < cities.length(); i++) {
                        currentData = cities.getJSONObject(i);
                        City city = new City();
                        city.name =  currentData.getString("name");
                        city.cityId = currentData.getLong("id");
                        city.country_id = currentData.getLong("country_id");
                        city.save();
                    }

                    //Save Lines
                    new Delete().from(Line.class).execute();
                    JSONArray lines = data.getJSONObject("line").getJSONArray("data");
                    for (int i = 0; i < lines.length(); i++) {
                        currentData = lines.getJSONObject(i);
                        Line line = new Line();
                        line.name =  currentData.getString("name");
                        //line.cityId = currentData.getLong("city_id");
                        line.lineId = currentData.getLong("id");
                        line.transport_id = currentData.getInt("transport_id");
                        line.save();
                    }

                    //Save Transports
                    new Delete().from(Transport.class).execute();
                    JSONArray transports = data.getJSONObject("transport").getJSONArray("data");
                    for (int i = 0; i < transports.length(); i++) {
                        currentData = transports.getJSONObject(i);
                        Transport transport = new Transport();
                        transport.transportId = currentData.getLong("id");
                        transport.name =  currentData.getString("name");
                        transport.icon = currentData.getString("icon");
                        transport.save();
                    }

                    //Save station
                    new Delete().from(Station.class).execute();
                    JSONArray stations = data.getJSONObject("station").getJSONArray("data");
                    for (int i = 0; i < stations.length(); i++) {
                        currentData = stations.getJSONObject(i);
                        Station station = new Station();
                        station.name =  currentData.getString("name");
                        station.longitude = currentData.getDouble("longitude");
                        station.stationId = currentData.getLong("id");
                        station.latitude = currentData.getDouble("latitude");
                        //station.lineId = currentData.getLong("line_id");
                        station.save();
                    }

                    //Save CitiesTransports
                    new Delete().from(CitiesTransport.class).execute();
                    JSONArray citiesTransports = data.getJSONObject("citiestransport").getJSONArray("data");
                    for (int i = 0; i < citiesTransports.length(); i++) {
                        currentData = citiesTransports.getJSONObject(i);
                        CitiesTransport citiesTransport = new CitiesTransport();
                        citiesTransport.transportId =  currentData.getLong("transport_id");
                        citiesTransport.cityId = currentData.getLong("city_id");
                        citiesTransport.save();
                    }

                    //Save LineStations
                    new Delete().from(LinesStation.class).execute();
                    JSONArray lineStations = data.getJSONObject("linesstations").getJSONArray("data");
                    for (int i = 0; i < lineStations.length(); i++) {
                        currentData = lineStations.getJSONObject(i);
                        LinesStation linesStation = new LinesStation();
                        linesStation.lineId =  currentData.getLong("line_id");
                        linesStation.stationId = currentData.getLong("station_id");
                        linesStation.save();
                    }

                    ActiveAndroid.setTransactionSuccessful();
                }
                finally {
                    ActiveAndroid.endTransaction();
                }
                return true;
            }catch(Exception e){
                if (Config.DEBUG) Log.d("Sync", "Sync failed. Cause: "+ e.toString());
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

    private class AsyncStaticSync extends AsyncTask<String, Integer, Boolean> {

        private Context context;
        private oauthHandler handler;

        public AsyncStaticSync(Context context, oauthHandler handler) {
            this.context = context;
            this.handler = handler;
        }

        @Override
        protected void onPreExecute() {
            handler.onStart();
        }

        @Override
        protected Boolean doInBackground(String... params){
            OAuth OAuth = new OAuth(context);
            Utils Utils = new Utils();

            Map<String, String> postParams = new HashMap<String, String>(1);
            postParams.put("client_id", Config.OAUTHCLIENTID);
            postParams.put("client_secret", Config.OAUTHCLIENTSECRET);
            JSONObject result = OAuth.call("StaticData", "", postParams);

            if (Config.DEBUG) Log.d("SyncStaticData", result.toString());

            try{
                //Save sync data
                JSONObject data = result.getJSONObject("data");

                JSONObject currentData;
                ActiveAndroid.beginTransaction();
                try {
                    //Save countries
                    new Delete().from(Country.class).execute();
                    JSONArray countries = data.getJSONObject("country").getJSONArray("data");

                    for (int i = 0; i < countries.length(); i++) {
                        currentData = countries.getJSONObject(i);
                        Country country = new Country();
                        country.name =  currentData.getString("name");
                        country.countryId = currentData.getLong("id");
                        country.save();
                    }

                    //Save cities
                    new Delete().from(City.class).execute();
                    JSONArray cities = data.getJSONObject("city").getJSONArray("data");

                    for (int i = 0; i < cities.length(); i++) {
                        currentData = cities.getJSONObject(i);
                        City city = new City();
                        city.name =  currentData.getString("name");
                        city.cityId = currentData.getLong("id");
                        city.country_id = currentData.getLong("country_id");
                        city.save();
                    }

                    ActiveAndroid.setTransactionSuccessful();
                }
                finally {
                    ActiveAndroid.endTransaction();
                }
                return true;
            }catch(Exception e){
                if (Config.DEBUG) Log.d("Sync", "Sync failed. Cause: "+ e.toString());
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


    private class AsyncCreateAlert extends AsyncTask<String, Integer, JSONObject> {

        private Context context;
        private oauthHandler handler;
        private Long stationId, lineId, cityId;

        public AsyncCreateAlert(Context context, oauthHandler handler, Long stationId, Long lineId, Long cityId) {
            this.context = context;
            this.handler = handler;
            this.stationId = stationId;
            this.lineId = lineId;
            this.cityId = cityId;
        }

        @Override
        protected void onPreExecute() {
            handler.onStart();
        }

        @Override
        protected JSONObject doInBackground(String... params){
            OAuth OAuth = new OAuth(context);
            Utils Utils = new Utils();

            Map<String, String> postParams = new HashMap<String, String>(4);
            postParams.put("access_token", Utils.getToken(context));
            postParams.put("station_id", String.valueOf(stationId));
            postParams.put("line_id", String.valueOf(lineId));
            postParams.put("city_id", String.valueOf(cityId));

            return OAuth.call("alerts", "add", postParams);

        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (Config.DEBUG) Log.d("Create Alert", result.toString());

            Boolean success = false;
            String message = "";

            try {
                success = result.getBoolean("success");
                message = result.getString("message");
            } catch(Exception e) {
                e.printStackTrace();
                message = context.getString(R.string.json_error);
                success = false;
            }

            if (success) {
                handler.onSuccess();
            }else {
                MemetroDialog.showDialog(context, null, message);
                handler.onFailure();
            }

            handler.onFinish();

        }
    }
}
