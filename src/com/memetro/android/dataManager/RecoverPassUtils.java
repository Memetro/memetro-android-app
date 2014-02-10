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
package com.memetro.android.dataManager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.memetro.android.common.Config;
import com.memetro.android.oauth.OAuth;
import com.memetro.android.oauth.OAuthHandler;
import com.memetro.android.oauth.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecoverPassUtils {

    public static void recoverPass(Context context, String email, OAuthHandler handler) {
        new AsyncRecover(context, handler, email).execute();
    }

    private static class AsyncRecover extends AsyncTask<String, Integer, Boolean> {

        private Context context;
        private OAuthHandler handler;
        private String email;

        public AsyncRecover(Context context, OAuthHandler handler, String email) {
            this.context = context;
            this.handler = handler;
            this.email = email;
        }

        @Override
        protected void onPreExecute() {
            handler.onStart();
        }

        @Override
        protected Boolean doInBackground(String... params){
            OAuth OAuth = new OAuth(context);
            Utils Utils = new Utils();

            Map<String, String> postParams = new HashMap<String, String>(4);
            postParams.put("access_token", Utils.getToken(context));
            postParams.put("client_id", Config.OAUTHCLIENTID);
            postParams.put("client_secret", Config.OAUTHCLIENTSECRET);
            postParams.put("email", email);
            JSONObject result = OAuth.call("register", "recoverPassword", postParams);

            if (Config.DEBUG) Log.d("Sync", result.toString());

            try{

                return result.getBoolean("success");
            }catch(Exception e){
                if (Config.DEBUG) Log.d("Recover", "Recover pass failed. Cause: "+ e.toString());
                e.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result) {
                handler.onSuccess();
            }else {
                handler.onFailure();
            }

            handler.onFinish();

        }
    }
}
