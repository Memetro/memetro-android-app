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

package com.memetro.android;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.memetro.android.common.AppContext;
import com.memetro.android.common.MemetroDialog;
import com.memetro.android.common.MemetroProgress;
import com.memetro.android.models.City;
import com.memetro.android.models.Country;
import com.memetro.android.models.User;
import com.memetro.android.oauth.OAuth;
import com.memetro.android.oauth.Utils;
import com.memetro.android.register.PersonalActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static String TAG = "Memetro Main";
    private Button register, login;
    private EditText usernameEt, passwordEt;
    private String username, password;
    private Context context;
    private OAuth OAuth = new OAuth();
    private Utils Utils = new Utils();
    private MemetroProgress pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        pdialog = new MemetroProgress(this);

        register = (Button) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
        usernameEt = (EditText) findViewById(R.id.username);
        passwordEt = (EditText) findViewById(R.id.password);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PersonalActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEt.getText().toString();
                password = passwordEt.getText().toString();
                new AsyncLogin().execute();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class AsyncLogin extends AsyncTask<String, Integer, JSONObject>{

        protected void onPreExecute(){
            pdialog.show();
        }

        protected JSONObject doInBackground(String... params){
            return OAuth.login(username, password);
        }

        protected void onPostExecute(JSONObject result) {
            if (AppContext.DEBUG) Log.d(TAG, result.toString());

            // Trying to get the token...
            try{
                String token = result.getString("access_token");
                String refresh_token = result.getString("refresh_token");
                Utils.setToken(context, token, refresh_token);

                //Sync
                new AsyncSync().execute();
            }catch(Exception e){
                if (pdialog.isShowing()) pdialog.dismiss();
                // Token failed
                if (AppContext.DEBUG) Log.d(TAG, "Login failed. Cause: "+ e.toString());
                MemetroDialog.showDialog(MainActivity.this, null, getString(R.string.login_error));
            }

        }
    }

    private class AsyncSync extends AsyncTask<String, Integer, JSONObject>{


        protected JSONObject doInBackground(String... params){
            List<NameValuePair> postParams = new ArrayList<NameValuePair>(1);
            postParams.add(new BasicNameValuePair("access_token", Utils.getToken(getApplicationContext())));
            return OAuth.call("synchronize", "", postParams);
        }

        protected void onPostExecute(JSONObject result) {
            if (pdialog.isShowing()) pdialog.dismiss();

            if (AppContext.DEBUG) Log.d(TAG, result.toString());

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
                        country.created = currentData.getString("created");
                        country.save();
                    }

                    //Save cities
                    new Delete().from(City.class).execute();
                    JSONArray cities = data.getJSONObject("city").getJSONArray("data");

                    for (int i = 0; i < cities.length(); i++) {
                        currentData = cities.getJSONObject(i);
                        City city = new City();
                        city.name =  currentData.getString("name");
                        city.created = currentData.getString("created");
                        city.country_id = currentData.getInt("country_id");
                        city.save();
                    }

                    ActiveAndroid.setTransactionSuccessful();
                }
                finally {
                    ActiveAndroid.endTransaction();
                }

                // Launch DashBoard
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
                finish();
            }catch(Exception e){
                if (AppContext.DEBUG) Log.d(TAG, "Sync failed. Cause: "+ e.toString());
                e.printStackTrace();
                MemetroDialog.showDialog(MainActivity.this, null, getString(R.string.login_error));
            }

        }
    }
}
