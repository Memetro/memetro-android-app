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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.memetro.android.common.MemetroDialog;
import com.memetro.android.common.MemetroProgress;
import com.memetro.android.dataManager.DataUtils;
import com.memetro.android.dataManager.RecoverPassUtils;
import com.memetro.android.models.User;
import com.memetro.android.oauth.OAuthHandler;
import com.memetro.android.register.PersonalActivity;

import org.json.JSONArray;

public class MainActivity extends Activity {

    private Button register, login;
    private EditText usernameEt, passwordEt;
    private TextView recoverPassButton;
    private Context context;
    private DataUtils dataUtils = new DataUtils();
    private MemetroProgress pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        pdialog = new MemetroProgress(this);

        getStaticData();

        register = (Button) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
        usernameEt = (EditText) findViewById(R.id.username);
        passwordEt = (EditText) findViewById(R.id.password);
        recoverPassButton = (TextView) findViewById(R.id.recover_pass_button);

        recoverPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPassDialog(MainActivity.this);
            }
        });

        User userData = dataUtils.getUserData();
        if (userData != null) {
            usernameEt.setText(userData.username);
            passwordEt.requestFocus();
        }

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

    private void getStaticData() {
        if (!dataUtils.getCities().isEmpty() && !dataUtils.getCountries().isEmpty()) {
            return;
        }
        dataUtils.syncStaticWSData(context, new OAuthHandler() {
            @Override
            public void onStart() {
                pdialog.show();
            }

            @Override
            public void onFailure() {
                if (pdialog.isShowing()) pdialog.dismiss();
                MemetroDialog.showDialog(MainActivity.this, null, getString(R.string.sync_error));
            }

            @Override
            public void onFinish() {
                if (pdialog.isShowing()) pdialog.dismiss();
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
        dataUtils.login(context, username, password, new OAuthHandler(){
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
        dataUtils.syncWSData(context, new OAuthHandler() {

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
                DataUtils.clearData(context);
            }

        });
    }

    public void showRecoverPassDialog(final Context context) {

        final Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_recover);
        mDialog.setCancelable(true);

        final EditText emailText = (EditText) mDialog.findViewById(R.id.email);
        Button sendButton = (Button) mDialog.findViewById(R.id.send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RecoverPassUtils.recoverPass(context, emailText.getText().toString(), new OAuthHandler(){
                    public void onStart() {
                        pdialog.show();
                    }

                    public void onSuccess() {
                        mDialog.dismiss();
                        MemetroDialog.showDialog(MainActivity.this, null, getString(R.string.recover_ok));
                    }

                    public void onFailure() {
                        MemetroDialog.showDialog(MainActivity.this, null, getString(R.string.recover_ko));
                    }

                    public void onFinish() {
                        pdialog.dismiss();
                    }
                });
            }
        });

        mDialog.show();
    }

}
