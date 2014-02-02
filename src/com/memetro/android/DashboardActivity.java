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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.memetro.android.alerts.AddFragment;
import com.memetro.android.alerts.HashtagFragment;
import com.memetro.android.alerts.MapFragment;
import com.memetro.android.alerts.ThermometerFragment;
import com.memetro.android.common.SlideHolder;
import com.memetro.android.dataManager.DataUtils;
import com.memetro.android.info.AssociationFragment;
import com.memetro.android.models.User;
import com.memetro.android.settings.SettingsFragment;
import com.memetro.android.web.WebViewer;

public class DashboardActivity extends FragmentActivity {

    private SlideHolder mSlideHolder;
    private Boolean lastWindow = true;

    private TextView usernameMenu;
    private ImageView actionbarAlerts, actionbarTwitter, actionbarMap;
    private ImageView speakerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dashboard);

        mSlideHolder = (SlideHolder) findViewById(R.id.slideHolder);
        usernameMenu = (TextView) findViewById(R.id.usernameMenu);
        actionbarAlerts = (ImageView) findViewById(R.id.actionbarAlerts);
        actionbarTwitter = (ImageView) findViewById(R.id.actionbarTws);
        actionbarMap = (ImageView) findViewById(R.id.actionbarMap);

        // Launch default fragment
        changeMainFragment(new ThermometerFragment(), true);

        User userData = DataUtils.getUserData();
        usernameMenu.setText("@"+userData.username);

        actionbarAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMainFragment(new ThermometerFragment(), true);
            }
        });

        actionbarTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMainFragment(new HashtagFragment());
            }
        });

        actionbarMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMainFragment(new MapFragment());
            }
        });


        // Speaker Button
        speakerButton = (ImageView) findViewById(R.id.speakerButton);
        speakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMainFragment(new AddFragment());
            }
        });

        // Toggler listener
        View toggleView = findViewById(R.id.actionbarToggler);
        toggleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideHolder.toggle();
            }
        });


        /// Menu buttons ///

        // Logout
        View logout = findViewById(R.id.logoutMenu);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataUtils.clearData(getApplicationContext());

                Intent intent = new Intent().setClass(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        // Asociacion
        View asociacion = findViewById(R.id.memetroMenu);
        asociacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMainFragment(new AssociationFragment());
            }
        });

        // Config
        View config = findViewById(R.id.configMenu);
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMainFragment(new SettingsFragment());
            }
        });

        // Info
        View info = findViewById(R.id.infoMenu);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebViewer webViewer = new WebViewer();
                Bundle arguments = new Bundle();
                arguments.putString("url", "http://www.memetro.net/infoapp/");
                webViewer.setArguments(arguments);
                changeMainFragment(webViewer);
            }
        });

        // Alerts
        View alertsMenu = findViewById(R.id.alertsMenu);
        alertsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMainFragment(new ThermometerFragment(), true);
            }
        });
    }

    @Override
    public void onBackPressed(){
        if (mSlideHolder.isOpened()){
            mSlideHolder.close();
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0){
            super.onBackPressed();
            return;
        }

        if (!lastWindow){
            changeMainFragment(new ThermometerFragment(), true);
            return;
        }

        super.onBackPressed();
    }

    public void showSpeaker() {
        if (speakerButton != null) speakerButton.setVisibility(View.VISIBLE);
    }

    public void hideSpeaker() {
        if (speakerButton != null) speakerButton.setVisibility(View.GONE);
    }

    public void disableSliderMenu() {
        mSlideHolder.setEnabled(false);
    }

    public void activateSliderMenu() {
        mSlideHolder.setEnabled(true);
    }

    public void fullActionBar() {
        if (actionbarTwitter != null) {
            actionbarTwitter.setVisibility(View.VISIBLE);
            actionbarMap.setVisibility(View.VISIBLE);
        }
    }

    public void compressActionBar() {
        if (actionbarTwitter != null) {
            actionbarTwitter.setVisibility(View.GONE);
            actionbarMap.setVisibility(View.GONE);
        }
    }

    public void changeMainFragment(Fragment fragment){
        changeMainFragment(fragment, false);
    }

    private void changeMainFragment(Fragment fragment, Boolean isLastWindow) {
        lastWindow = isLastWindow;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentData, fragment).commit();

        // Toggle left menu
        if (mSlideHolder.isOpened()){
            mSlideHolder.close();
        }
    }
}
