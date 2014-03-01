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

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.memetro.android.R;
import com.memetro.android.common.Config;
import com.memetro.android.common.LayoutUtils;
import com.memetro.android.common.MemetroDialog;
import com.memetro.android.common.MemetroProgress;
import com.memetro.android.dataManager.DataUtils;
import com.memetro.android.models.City;
import com.memetro.android.models.Country;
import com.memetro.android.models.User;
import com.memetro.android.oauth.OAuth;
import com.memetro.android.oauth.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsFragment extends Fragment {

    private Activity mActivity;
    private Spinner spinnerCity, spinnerCountry;
    private EditText twitter, name, mail;
    private CheckBox checkNotifications;
    private Button saveButton;
    private MemetroProgress pdialog;
    private User userData;

    @Override
    public void onCreate(Bundle bundleSavedInstance) {
        super.onCreate(bundleSavedInstance);
        this.mActivity = getActivity();
        pdialog = new MemetroProgress(mActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View inflated = inflater.inflate(R.layout.fragment_settings, container, false);

        spinnerCity = (Spinner) inflated.findViewById(R.id.spinnerCity);
        spinnerCountry = (Spinner) inflated.findViewById(R.id.spinnerCountry);
        twitter = (EditText) inflated.findViewById(R.id.twitter_username);
        name = (EditText) inflated.findViewById(R.id.name);
        mail = (EditText) inflated.findViewById(R.id.email);
        checkNotifications = (CheckBox) inflated.findViewById(R.id.check_notifications);
        saveButton = (Button) inflated.findViewById(R.id.save_button);

        checkNotifications.setChecked(!UserPreferences.areNotificationsEnabled(mActivity));

        List<City> cities = DataUtils.getCities(DataUtils.getUserCountryId());

        userData = DataUtils.getUserData();
        if (!userData.twittername.equals("")) {
            twitter.setText("@"+userData.twittername);
        }
        if (!userData.name.equals("")) {
            name.setText(userData.name);
        }
        if (!userData.email.equals("")) {
            mail.setText(userData.email);
        }

        checkNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                UserPreferences.toggleNotifications(mActivity, !checkNotifications.isChecked());
            }
        });

        List<Country> countries = DataUtils.getCountries();
        LayoutUtils.setDefaultSpinnerGrey(mActivity, spinnerCountry, countries);

        for (int i = 0; countries.size() > i; i++) {
            Country country = countries.get(i);
            if (country.countryId == DataUtils.getUserCountryId()) {
                spinnerCountry.setSelection(i);
            }
        }

        Long defaultUserCity = userData.cityId;

        LayoutUtils.setDefaultSpinner(mActivity, spinnerCity, cities);

        for (int i = 0; cities.size() > i; i++) {
            City city = cities.get(i);
            if (city.cityId == defaultUserCity) {
                spinnerCity.setSelection(i);
            }
        }

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Country country = (Country) adapterView.getAdapter().getItem(i);
                Log.d("PAIS", country.name);
                List<City> cities = DataUtils.getCities(country.countryId);
                LayoutUtils.setDefaultSpinnerGrey(mActivity, spinnerCity, cities);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncEditData(mActivity, name.getText().toString(), mail.getText().toString(), getCitySelected()).execute();
            }
        });

        return inflated;
    }

    private class AsyncEditData extends AsyncTask<String, Integer, JSONObject> {

        private Context context;
        private Long cityId;
        private String name, email;

        public AsyncEditData(Context context, String name, String email, Long cityId) {
            this.context = context;
            this.name = name;
            this.email = email;
            this.cityId = cityId;
        }

        @Override
        protected void onPreExecute() {
            pdialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params){
            OAuth OAuth = new OAuth(context);
            Utils Utils = new Utils();

            Map<String, String> postParams = new HashMap<String, String>(4);
            postParams.put("access_token", Utils.getToken(context));
            postParams.put("name", name);
            postParams.put("email", email);
            postParams.put("city_id", String.valueOf(cityId));
            postParams.put("twittername", userData.twittername);
            postParams.put("aboutme", userData.aboutme);

            return OAuth.call("users", "edit_user_data", postParams);

        }

        @Override
        protected void onPostExecute(JSONObject result) {
            pdialog.dismiss();
            if (Config.DEBUG) Log.d("Edit User", result.toString());

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
                // TODO Refresh user data
            }

            MemetroDialog.showDialog(context, null, message);

        }
    }

    private Long getCitySelected() {
        City city = (City) spinnerCity.getSelectedItem();
        return city.cityId;
    }
}