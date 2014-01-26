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

package com.memetro.android.oauth;

import android.content.Context;
import android.util.Log;

import com.memetro.android.R;
import com.memetro.android.common.Config;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OAuth {

    private Context context;
    private String oauthServer = Config.OAUTHSERVER;
    private String clientId = Config.OAUTHCLIENTID;
    private String clientSecret = Config.OAUTHCLIENTSECRET;

    private Boolean refreshAction = false;

    public OAuth(Context context) {
        this.context = context;
    }

    public JSONObject login (String username, String password){

        List<NameValuePair> params = new ArrayList<NameValuePair>(5);

        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("client_id", clientId));
        params.add(new BasicNameValuePair("client_secret", clientSecret));
        params.add(new BasicNameValuePair("grant_type", "password"));

        JSONObject result = call("oauth", "token", params);

        try{
            if(!result.getBoolean("success")){
                JSONObject errorResult = new JSONObject();
                errorResult.put("error", result.getString("message"));
                return errorResult;
            }
        }catch(Exception e){
            return result;
        }
        return result;
    }

    public JSONObject call(String controller, String action, List<NameValuePair> params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(oauthServer+controller+"/"+action);

        try {
            // Add data
            httppost.setEntity(new UrlEncodedFormEntity(params));

            // Execute Post
            HttpResponse response = httpclient.execute(httppost);

            // Catch headers
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200){
                JSONObject returnJ = new JSONObject();
                returnJ.put("success", false);
                returnJ.put("data", new JSONArray());

                Log.d("Http Status Code", String.valueOf(statusCode));

                switch (statusCode){
                    case 401:
                        // TODO Desconectar
                        returnJ.put("message", context.getString(R.string.token_denied));
                        return returnJ;
                    case 404:
                        returnJ.put("message", context.getString(R.string.action_not_found));
                        return returnJ;
                    case 500:
                        returnJ.put("message", context.getString(R.string.server_error));
                        return returnJ;
                    default:
                        returnJ.put("message", context.getString(R.string.internal_error));
                        return returnJ;
                }
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();
            JSONTokener tokener = new JSONTokener(json);

            return new JSONObject(tokener);

        } catch (ClientProtocolException e) {

            // TODO Auto-generated catch block

        } catch (JSONException e) {

            // TODO Auto-generated catch block

        } catch (IOException e) {

            // TODO Auto-generated catch block
        }
        return new JSONObject();
    }

}
