package com.memetro.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.memetro.android.common.AppContext;
import com.memetro.android.oauth.OAuth;
import com.memetro.android.oauth.Utils;

import org.json.JSONObject;

public class MainActivity extends Activity {

    private static String TAG = "Memetro Main";
    private Button register, login;
    private EditText usernameEt, passwordEt;
    private String username, password;
    private Context context;
    private OAuth OAuth = new OAuth();
    private Utils Utils = new Utils();
    private ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        pdialog = new ProgressDialog(this);

        register = (Button) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
        usernameEt = (EditText) findViewById(R.id.username);
        passwordEt = (EditText) findViewById(R.id.password);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEt.getText().toString();
                password = passwordEt.getText().toString();
                new AsyncLogin().execute();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class AsyncLogin extends AsyncTask<String, Integer, JSONObject>{

        protected void onPreExecute(){
            pdialog.setMessage(getString(R.string.login_loading));
            pdialog.show();
        }

        protected JSONObject doInBackground(String... params){
            return OAuth.login(username, password);
        }

        protected void onPostExecute(JSONObject result) {
            if (pdialog.isShowing()) pdialog.dismiss();

            if (AppContext.DEBUG) Log.d(TAG, result.toString());

            // Trying to get the token...
            try{
                String token = result.getString("access_token");
                String refresh_token = result.getString("refresh_token");
                Utils.setToken(context, token, refresh_token);

                // Launch DashBoard
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
                finish();
            }catch(Exception e){
                // Token failed
                if (AppContext.DEBUG) Log.d(TAG, "Login failed. Cause: "+ e.toString());
                Toast.makeText(context, getString(R.string.login_error), Toast.LENGTH_LONG).show();
            }

        }
    }
}
