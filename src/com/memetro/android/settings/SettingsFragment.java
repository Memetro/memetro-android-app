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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.memetro.android.MainActivity;
import com.memetro.android.R;
import com.memetro.android.common.LayoutUtils;
import com.memetro.android.dataManager.dataUtils;
import com.memetro.android.models.City;
import com.memetro.android.models.User;

import java.util.List;

public class SettingsFragment extends Fragment {

    private Activity mActivity;
    private Spinner spinnerCity;
    private EditText twitter, name, mail;

    @Override
    public void onCreate(Bundle bundleSavedInstance) {
        super.onCreate(bundleSavedInstance);
        this.mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View inflated = inflater.inflate(R.layout.fragment_settings, container, false);

        spinnerCity = (Spinner) inflated.findViewById(R.id.spinnerCity);
        twitter = (EditText) inflated.findViewById(R.id.twitter_username);
        name = (EditText) inflated.findViewById(R.id.name);
        mail = (EditText) inflated.findViewById(R.id.email);

        // TODO No harcodear el id
        List<City> cities = dataUtils.getCities((long) 3);

        User userData = dataUtils.getUserData();
        if (!userData.twittername.equals("")) {
            twitter.setText("@"+userData.twittername);
        }
        if (!userData.name.equals("")) {
            name.setText(userData.name);
        }
        if (!userData.email.equals("")) {
            mail.setText(userData.email);
        }

        Long defaultUserCity = userData.cityId;

        LayoutUtils.setDefaultSpinner(mActivity, spinnerCity, cities);

        for (int i = 0; cities.size() > i; i++) {
            City city = cities.get(i);
            if (city.cityId == defaultUserCity) {
                spinnerCity.setSelection(i);
            }
        }

        return inflated;
    }
}