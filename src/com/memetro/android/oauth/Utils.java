package com.memetro.android.oauth;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Nytyr on 18/08/13.
 */
public class Utils {

    private static final String TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String SHARED_NAME = "oauth_shared_prefs";

    public String getToken(Context context) {
        final SharedPreferences prefs = getOAuthPreferences(context);
        return prefs.getString(TOKEN, "");
    }

    public String getRefreshToken(Context context){
        final SharedPreferences prefs = getOAuthPreferences(context);
        return prefs.getString(REFRESH_TOKEN, "");
    }

    public void setToken(Context context, String accessToken, String refreshToken){
        final SharedPreferences prefs = getOAuthPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN, accessToken);
        editor.putString(REFRESH_TOKEN, refreshToken);
        editor.commit();
    }

    private SharedPreferences getOAuthPreferences(Context context) {
        return context.getSharedPreferences(SHARED_NAME,
                Context.MODE_PRIVATE);
    }
}
