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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.memetro.android.DashboardActivity;
import com.memetro.android.R;
import com.memetro.android.common.LayoutUtils;
import com.memetro.android.common.MemetroProgress;
import com.memetro.android.dataManager.DataUtils;
import com.memetro.android.models.City;
import com.memetro.android.models.Line;
import com.memetro.android.models.Station;
import com.memetro.android.models.Transport;
import com.memetro.android.models.User;
import com.memetro.android.oauth.OAuthHandler;

import java.util.List;

public class AddFragment extends Fragment {

    private DashboardActivity mActivity;
    private DataUtils dataUtils = new DataUtils();
    private MemetroProgress pdialog;
    private Spinner spinnerCity, spinnerTransport, spinnerLine, spinnerStation;
    private EditText descriptionText;
    private Button addAlertButton;

    @Override
    public void onCreate(Bundle bundleSavedInstance) {
        super.onCreate(bundleSavedInstance);
        this.mActivity = (DashboardActivity) getActivity();
        this.pdialog = new MemetroProgress(mActivity);
        mActivity.hideSpeaker();
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivity.showSpeaker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View inflated = inflater.inflate(R.layout.fragment_add_alert, container, false);

        spinnerCity = (Spinner) inflated.findViewById(R.id.spinnerCity);
        spinnerTransport = (Spinner) inflated.findViewById(R.id.spinnerTransport);
        spinnerLine = (Spinner) inflated.findViewById(R.id.spinnerLine);
        spinnerStation = (Spinner) inflated.findViewById(R.id.spinnerStation);
        addAlertButton = (Button) inflated.findViewById(R.id.addAlertButton);
        descriptionText = (EditText) inflated.findViewById(R.id.description_text);

        LayoutUtils.setDefaultSpinner(mActivity, spinnerTransport, DataUtils.getTransport());

        // TODO No harcodear el id
        List<City> cities = DataUtils.getCities((long) 3);

        User userData = DataUtils.getUserData();

        Long defaultUserCity = userData.cityId;

        LayoutUtils.setDefaultSpinner(mActivity, spinnerCity, cities);

        for (int i = 0; cities.size() > i; i++) {
            City city = cities.get(i);
            if (city.cityId == defaultUserCity) {
                spinnerCity.setSelection(i);
            }
        }
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                City city = (City) adapterView.getAdapter().getItem(i);
                Log.d("CIUDAD", city.name);

                LayoutUtils.setDefaultSpinner(mActivity, spinnerTransport, DataUtils.getTransport(city.cityId));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerTransport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Transport transport = (Transport) adapterView.getAdapter().getItem(i);
                Log.d("Transport", transport.name);

                LayoutUtils.setDefaultSpinner(mActivity, spinnerLine, DataUtils.getLines(getCitySelected(), getTransportSelected()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Line line = (Line) adapterView.getAdapter().getItem(i);
                Log.d("LINEA", line.name);
                LayoutUtils.setDefaultSpinner(mActivity, spinnerStation, DataUtils.getStations(line.lineId));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataUtils.createAlert(
                        mActivity,
                        getStationSelected(),
                        getLineSelected(),
                        getCitySelected(),
                        getTransportSelected(),
                        descriptionText.getText().toString(),
                        new OAuthHandler() {
                            @Override
                            public void onStart() {
                                pdialog.show();
                            }

                            @Override
                            public void onSuccess() {
                                Toast.makeText(mActivity.getApplicationContext(), getString(R.string.alert_created), Toast.LENGTH_LONG).show();
                                mActivity.onBackPressed();
                            }

                            @Override
                            public void onFinish() {
                                pdialog.dismiss();
                            }
                        }
                );
            }
        });

        return inflated;
    }

    private Long getStationSelected() {
        Station station = (Station) spinnerStation.getSelectedItem();
        return station.stationId;
    }

    private Long getLineSelected() {
        Line line = (Line) spinnerLine.getSelectedItem();
        return line.lineId;
    }

    private Long getCitySelected() {
        City city = (City) spinnerCity.getSelectedItem();
        return city.cityId;
    }

    private Long getTransportSelected() {
        Transport transport = (Transport) spinnerTransport.getSelectedItem();
        return transport.transportId;
    }
}