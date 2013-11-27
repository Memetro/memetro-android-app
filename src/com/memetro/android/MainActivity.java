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
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.memetro.android.common.MemetroDialog;
import com.memetro.android.common.MemetroProgress;
import com.memetro.android.dataManager.dataUtils;
import com.memetro.android.oauth.oauthHandler;
import com.memetro.android.register.PersonalActivity;

public class MainActivity extends Activity {

    private Button register, login;
    private EditText usernameEt, passwordEt;
    private Context context;
    private dataUtils dataUtils = new dataUtils();
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
                Intent intent = new Intent(context, PersonalActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(usernameEt.getText().toString(), passwordEt.getText().toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void login(String username, String password) {
        dataUtils.login(context, username, password, new oauthHandler(){
            @Override
            public void onStart() {
                pdialog.show();
            }

            @Override
            public void onFailure() {
                if (pdialog.isShowing()) pdialog.dismiss();
                MemetroDialog.showDialog(MainActivity.this, null, getString(R.string.login_error));
            }

            @Override
            public void onSuccess() {
                sync();
            }
        });
    }

    private void sync() {
        dataUtils.syncWSData(context, new oauthHandler() {

            @Override
            public void onFinish() {
                if (pdialog.isShowing()) pdialog.dismiss();
            }

            @Override
            public void onSuccess() {
                // Launch DashBoard
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure() {
                MemetroDialog.showDialog(MainActivity.this, null, getString(R.string.sync_error));
                com.memetro.android.dataManager.dataUtils.clearData(context);
            }

        });
    }

}
