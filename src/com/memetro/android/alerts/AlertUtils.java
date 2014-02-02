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
package com.memetro.android.alerts;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.activeandroid.query.Select;
import com.memetro.android.R;
import com.memetro.android.common.Config;
import com.memetro.android.common.MemetroDialog;
import com.memetro.android.dataManager.dataUtils;
import com.memetro.android.models.Alert;
import com.memetro.android.models.User;
import com.memetro.android.oauth.OAuth;
import com.memetro.android.oauth.Utils;
import com.memetro.android.oauth.oauthHandler;
import com.memetro.android.settings.UserPreferences;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertUtils {

    public void getAlerts(Context context, oauthHandler handler) {
        new AsyncGetAlerts(context, handler).execute();
    }

    private class AsyncGetAlerts extends AsyncTask<String, Integer, JSONObject> {

        private Context context;
        private oauthHandler handler;
        private User userData;

        public AsyncGetAlerts(Context context, oauthHandler handler) {
            this.context = context;
            this.handler = handler;
            this.userData = dataUtils.getUserData();
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
            postParams.put("city_id", String.valueOf(userData.cityId));
            return OAuth.call("alerts", "listAlert", postParams);

        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (Config.DEBUG) Log.d("Get Alerts", result.toString());

            Boolean success = false;
            String message = "";
            JSONArray data = new JSONArray();

            try {
                success = result.getBoolean("success");
                message = result.getString("message");
                data = result.getJSONArray("data");
            } catch(Exception e) {
                e.printStackTrace();
                message = context.getString(R.string.json_error);
                success = false;
            }

            if (success) {
                try {
                    dataUtils.saveAlerts(data);
                } catch (JSONException e) {
                    Log.e("Alerts", "Error saving alerts...");
                    e.printStackTrace();
                }
                handler.onSuccess(data);
            }else {
                MemetroDialog.showDialog(context, null, message);
                handler.onFailure();
            }

            handler.onFinish();

        }
    }

    public void getTweets(Context context, oauthHandler handler) {
        new AsyncGetTweets(context, handler).execute();
    }

    private class AsyncGetTweets extends AsyncTask<String, Integer, JSONObject> {

        private Context context;
        private oauthHandler handler;
        private User userData;

        public AsyncGetTweets(Context context, oauthHandler handler) {
            this.context = context;
            this.handler = handler;
            this.userData = dataUtils.getUserData();
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
            postParams.put("city_id", String.valueOf(userData.cityId));
            return OAuth.call("alerts", "getTweets", postParams);

        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (Config.DEBUG) Log.d("Get Alerts", result.toString());

            Boolean success = false;
            String message = "";
            JSONArray data = new JSONArray();

            try {
                success = result.getBoolean("success");
                message = result.getString("message");
                data = result.getJSONArray("data");
            } catch(Exception e) {
                e.printStackTrace();
                message = context.getString(R.string.json_error);
                success = false;
            }

            if (success) {
                try {
                    dataUtils.saveAlerts(data);
                } catch (JSONException e) {
                    Log.e("Alerts", "Error saving alerts...");
                    e.printStackTrace();
                }
                handler.onSuccess(data);
            }else {
                MemetroDialog.showDialog(context, null, message);
                handler.onFailure();
            }

            handler.onFinish();

        }
    }

}
