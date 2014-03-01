/*
 * Copyright 2013 Josei [hi at jrubio dot me]
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

package com.memetro.android.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.memetro.android.MainActivity;
import com.memetro.android.R;
import com.memetro.android.common.Config;
import com.memetro.android.common.LayoutUtils;
import com.memetro.android.common.MemetroDialog;
import com.memetro.android.common.MemetroProgress;
import com.memetro.android.dataManager.DataUtils;
import com.memetro.android.models.City;
import com.memetro.android.models.Country;
import com.memetro.android.oauth.OAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CredentialsActivity extends Activity {

    private static String TAG = "Memetro Register Credentials";
    private Button register;
    private EditText usernameEt, passwordEt, repeatPasswordEt;
    private String username, password, repeatPassword, name, twitter, mail, about;
    private Spinner spinnerCity, spinnerCountry;

    private Context context;
    private MemetroProgress pdialog;
    private OAuth OAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_credentials);
        context = getApplicationContext();
        OAuth = new OAuth(context);

        pdialog = new MemetroProgress(this);

        register = (Button) findViewById(R.id.register);
        usernameEt = (EditText) findViewById(R.id.username);
        passwordEt = (EditText) findViewById(R.id.password);
        repeatPasswordEt = (EditText) findViewById(R.id.repeat_password);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);


        List<Country> countries = DataUtils.getCountries();
        LayoutUtils.setDefaultSpinnerGrey(context, spinnerCountry, countries);

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Country country = (Country) adapterView.getAdapter().getItem(i);
                Log.d("PAIS", country.name);
                List<City> cities = DataUtils.getCities(country.countryId);
                LayoutUtils.setDefaultSpinnerGrey(context, spinnerCity, cities);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            mail = extras.getString("mail");
            twitter = extras.getString("twitter");
            about = extras.getString("about");
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEt.getText().toString();
                password = passwordEt.getText().toString();
                repeatPassword = repeatPasswordEt.getText().toString();
                new AsyncRegister().execute();
            }
        });

    }

    private Long getCitySelected() {
        City city = (City) spinnerCity.getSelectedItem();
        return city.cityId;
    }

    private class AsyncRegister extends AsyncTask<String, Integer, JSONObject>{

        Boolean success = false;

        protected void onPreExecute(){
            pdialog.show();
        }

        protected JSONObject doInBackground(String... params){

            Map<String, String> registerParams = new HashMap<String, String>(9);

            registerParams.put("username", username);
            registerParams.put("password", password);
            registerParams.put("password2", repeatPassword);
            registerParams.put("city_id", String.valueOf(getCitySelected()));
            registerParams.put("name", name);
            registerParams.put("email", mail);
            registerParams.put("twittername", twitter);
            registerParams.put("aboutme", about);

            registerParams.put("client_id", Config.OAUTHCLIENTID);
            registerParams.put("client_secret", Config.OAUTHCLIENTSECRET);

            return OAuth.call("Register", "index", registerParams);
        }

        protected void onPostExecute(JSONObject result) {
            if (pdialog.isShowing()) pdialog.dismiss();

            if (Config.DEBUG) Log.d(TAG, result.toString());

            try {
                success = result.getBoolean("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (success) {
                Toast.makeText(context, getString(R.string.register_ok), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                try {
                    String message = result.getString("message");
                    if ("".equals(message)) {
                        String code = result.getString("error_code");
                        if ("R005".equals(code)) {
                            message = getString(R.string.min_pass_lenght);
                        }
                    }
                    MemetroDialog.showDialog(CredentialsActivity.this, null, message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
