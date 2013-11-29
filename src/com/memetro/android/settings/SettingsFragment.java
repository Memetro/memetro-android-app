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
import android.widget.Spinner;

import com.memetro.android.MainActivity;
import com.memetro.android.R;
import com.memetro.android.common.LayoutUtils;
import com.memetro.android.dataManager.dataUtils;
import com.memetro.android.models.City;

import java.util.List;

public class SettingsFragment extends Fragment {

    private Activity mActivity;
    private Spinner spinnerCity;

    @Override
    public void onCreate(Bundle bundleSavedInstance) {
        super.onCreate(bundleSavedInstance);
        this.mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View inflated = inflater.inflate(R.layout.fragment_settings, container, false);

        // Logout
        View logout = inflated.findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dataUtils.clearData(mActivity);

                Intent intent = new Intent().setClass(mActivity, MainActivity.class);
                startActivity(intent);
                mActivity.finish();

            }
        });

        spinnerCity = (Spinner) inflated.findViewById(R.id.spinnerCity);

        // TODO No harcodear el id
        List<City> cities = dataUtils.getCities("1");

        String defaultUserCity = UserPreferences.getUserCity(mActivity);

        LayoutUtils.setDefaultSpinner(mActivity, spinnerCity, cities);

        for (int i = 0; cities.size() > i; i++) {
            City city = cities.get(i);
            if (city.cityId.equals(defaultUserCity)) {
                spinnerCity.setSelection(i);
            }
        }


        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                City city = (City) adapterView.getAdapter().getItem(i);
                UserPreferences.setUserCity(mActivity, city.cityId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return inflated;
    }
}