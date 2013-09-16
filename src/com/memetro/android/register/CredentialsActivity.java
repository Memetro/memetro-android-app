/*
 * Copyright 2013 Josei
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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.memetro.android.MainActivity;
import com.memetro.android.R;
import com.memetro.android.common.AppContext;
import com.memetro.android.oauth.OAuth;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CredentialsActivity extends Activity {

    private static String TAG = "Memetro Register Credentials";
    private Button register;
    private EditText usernameEt, passwordEt, repeatPasswordEt;
    private String username, password, repeatPassword;

    private String clientId = "NTFmMDU3YmY2ZDFkMDFl";
    private String clientSecret = "2cb128799bb3886281c6b7a89b7ac0047c06b876";
    private String message = "";

    private Context context;
    private ProgressDialog pdialog;
    private OAuth OAuth = new OAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        context = getApplicationContext();

        pdialog = new ProgressDialog(this);

        register = (Button) findViewById(R.id.register);
        usernameEt = (EditText) findViewById(R.id.username);
        passwordEt = (EditText) findViewById(R.id.password);
        repeatPasswordEt = (EditText) findViewById(R.id.repeat_password);

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

    private class AsyncRegister extends AsyncTask<String, Integer, JSONObject>{

        Boolean success = false;

        protected void onPreExecute(){
            pdialog.setMessage(getString(R.string.register_loading));
            pdialog.show();
        }

        protected JSONObject doInBackground(String... params){

            List<NameValuePair> registerParams = new ArrayList<NameValuePair>(5);

            registerParams.add(new BasicNameValuePair("username", username));
            registerParams.add(new BasicNameValuePair("password", password));
            registerParams.add(new BasicNameValuePair("password2", repeatPassword));
            registerParams.add(new BasicNameValuePair("client_id", clientId));
            registerParams.add(new BasicNameValuePair("client_secret", clientSecret));

            return OAuth.call("Register", "index", registerParams);
        }

        protected void onPostExecute(JSONObject result) {
            if (pdialog.isShowing()) pdialog.dismiss();

            if (AppContext.DEBUG) Log.d(TAG, result.toString());

            try {
                success = result.getBoolean("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (success) {
                Toast.makeText(context, "Register ok", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                try {
                    message = result.getString("message");
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
